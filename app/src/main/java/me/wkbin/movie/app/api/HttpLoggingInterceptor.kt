package me.wkbin.movie.app.api

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.RequestBody
import okhttp3.Response
import okhttp3.internal.http.promisesBody
import okio.Buffer
import okio.GzipSource
import timber.log.Timber
import java.io.EOFException
import java.net.URLDecoder
import java.nio.charset.Charset
import java.nio.charset.UnsupportedCharsetException
import java.util.Locale
import java.util.concurrent.TimeUnit

class HttpLoggingInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val startNs = System.nanoTime()
        val response: Response = try {
            chain.proceed(request)
        } catch (e: Exception) {
            throw e
        }

        val logInfo = StringBuilder("")

        val requestBody = request.body

        val protocol = response.protocol
        logInfo.append("${request.method.uppercase(Locale.getDefault())}: ${request.url} $protocol\n")

        val hasRequestBody = null != requestBody
        if (hasRequestBody) {
            logInfo.append("Content-Type: ${requestBody?.contentType()}\n")
            logInfo.append("Content-Length: ${requestBody?.contentLength()}\n")
        }

        var headers = request.headers

        logInfo.append("\nRequest headers =>\n")

        var count  = headers.size
        var i = 0
        while (i < count) {
            val name = headers.name(i)
            if (!"Content-Type".equals(name, ignoreCase = true) && !"Content-Length".equals(name, ignoreCase = true)) {
                logInfo.append("$name: ${headers.value(i)}\n")
            }
            i++
        }

        logInfo.append("\nRequest params =>\n")
        val utf8 = Charset.forName("UTF-8")

        if (hasRequestBody) {
            var charset: Charset? = null
            val buffer = Buffer()
            requestBody!!.writeTo(buffer)

            val contentType = requestBody.contentType()
            if (contentType != null) {
                charset = contentType.charset(utf8)
            }

            if (isPlaintext(buffer)) {
                var params = buffer.readString(charset ?: utf8)
                params = URLDecoder.decode(params, "UTF-8")
                logInfo.append("$params\n")
            } else {
                logInfo.append("it is not plain text, i can't print it...\n")
            }
        } else {
            logInfo.append("Nothing...")
        }

        logInfo.append("\nResponse =>\n")

        val tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)

        logInfo.append("Total time: ${tookMs}ms\n")

        val responseBody = response.body
        val contentLength = responseBody!!.contentLength()
        val bodySize = if (contentLength != -1L) "$contentLength-byte" else "unknown-length"

        logInfo.append("Status code: ${response.code} message: ${response.message} body size: ${bodySize}\n")

        logInfo.append("\nResponse headers =>\n")

        headers = response.headers
        i = 0
        count = headers.size
        while (i < count) {
            logInfo.append("${headers.name(i)} : ${headers.value(i)}\n")
            i++
        }


        logInfo.append("\nResponse body =>\n")
        if (response.promisesBody() && !bodyEncoded(response.headers)) {
            val source = responseBody.source()
            source.request(Long.MAX_VALUE) // Buffer the entire body.
            val buffer = source.buffer
            var charset: Charset? = utf8
            val contentType = responseBody.contentType()
            if (contentType != null) {
                charset = try {
                    contentType.charset(utf8)
                } catch (e: UnsupportedCharsetException) {
                    logInfo.append("Couldn't decode the response body; charset is likely malformed.\n")
                    return response
                }
            }

            if (!isPlaintext(buffer)) {
                logInfo.append("binary " + buffer.size + "-byte body omitted\n")
                return response
            }

            if (contentLength != 0L) {
                logInfo.append("${buffer.clone().readString(charset ?: utf8)}\n")
            }
        }
        Timber.d(logInfo.toString())
        /*
        "method": response.request.method,
        "path": response.request.path,
        "headers": response.request.headers,
        "params": response.request.queryParameters,
        "request time": endTime - startTime,
        "response": response?.data
        */
        val source = responseBody.source()
        source.request(Long.MAX_VALUE) // Buffer the entire body.
        var buffer = source.buffer
        if ("gzip".equals(headers["Content-Encoding"], ignoreCase = true)) {
            GzipSource(buffer.clone()).use { gzippedResponseBody ->
                buffer = Buffer()
                buffer.writeAll(gzippedResponseBody)
            }
        }
        val contentType = responseBody.contentType()
        val charset: Charset = contentType?.charset(Charsets.UTF_8) ?: Charsets.UTF_8

        val map = linkedMapOf<String, Any?>()
        map["method"] = request.method
        map["path"] = response.request.url.toString()
        map["headers"] = response.request.headers.toMap()
        map["params"] = response.request.body?.string() ?: ""
        map["request time"] = tookMs
        map["response"] = if (buffer.isProbablyUtf8()) {
            val json = buffer.clone().readString(charset)
            kotlin.runCatching {
                val type = object : TypeToken<Map<String, Any?>>() {}.type
                Gson().fromJson<Map<String, Any?>>(json, type)
            }.getOrElse { json }
        } else {
            null
        }
        return response
    }

    private fun RequestBody.string(): String {
        return kotlin.runCatching {
            val buffer = Buffer()
            writeTo(buffer)
            buffer.readUtf8()
        }.getOrElse { "" }
    }

    private fun Buffer.isProbablyUtf8(): Boolean {
        try {
            val prefix = Buffer()
            val byteCount = size.coerceAtMost(64)
            copyTo(prefix, 0, byteCount)
            for (i in 0 until 16) {
                if (prefix.exhausted()) {
                    break
                }
                val codePoint = prefix.readUtf8CodePoint()
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false
                }
            }
            return true
        } catch (_: EOFException) {
            return false // Truncated UTF-8 sequence.
        }
    }

    private fun Headers.toMap(): Map<String, String?> {
        val map = linkedMapOf<String, String?>()
        this.names().forEach {
            map[it] = get(it)
        }
        return map
    }

    private fun isPlaintext(buffer: Buffer): Boolean {
        return try {
            val prefix = Buffer()
            val byteCount = if (buffer.size < 64) buffer.size else 64
            buffer.copyTo(prefix, 0, byteCount)
            for (i in 0..15) {
                if (prefix.exhausted()) {
                    break
                }
                val codePoint = prefix.readUtf8CodePoint()
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false
                }
            }
            true
        } catch (e: EOFException) {
            false
        }
    }

    private fun bodyEncoded(headers: Headers): Boolean {
        val contentEncoding = headers["Content-Encoding"]
        return contentEncoding != null && !contentEncoding.equals("identity", ignoreCase = true)
    }
}