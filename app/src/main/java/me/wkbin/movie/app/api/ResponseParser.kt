package me.wkbin.movie.app.api

import okhttp3.Response
import rxhttp.wrapper.annotation.Parser
import rxhttp.wrapper.exception.ParseException
import rxhttp.wrapper.parse.TypeParser
import rxhttp.wrapper.utils.Converter


/**
 * 自定义Parser
 */
@Parser(name = "Response")
class ResponseParser<T> : TypeParser<T>() {
    override fun onParse(response: Response?): T {
        val data = Converter.convertTo<ApiResponse<T>>(response, ApiResponse::class.java, *types)
        val t = data.data
        if(data.code != Net.REQUEST_SUC){
            throw ParseException(data.code.toString(),data.msg,response)
        }
        return t
    }
}