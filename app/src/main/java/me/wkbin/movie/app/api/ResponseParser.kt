package me.wkbin.movie.app.api

import okhttp3.Response
import rxhttp.wrapper.annotation.Parser
import rxhttp.wrapper.exception.ParseException
import rxhttp.wrapper.parse.TypeParser
import rxhttp.wrapper.utils.Converter
import java.lang.reflect.Type


/**
 * 自定义Parser
 */
@Parser(name = "Response")
open class ResponseParser<T> : TypeParser<T> {
    //该构造方法是必须的
    protected constructor() : super()
    //如果依赖了RxJava，该构造方法也是必须的
    constructor(type: Type) : super(type)
    override fun onParse(response: Response?): T {
        val data = Converter.convertTo<ApiResponse<T>>(response, ApiResponse::class.java, *types)
        var t = data.data
        if(data.code != Net.REQUEST_SUC){
            throw ParseException(data.code.toString(),data.msg,response)
        }
        if(t == null && (types.first() == String::class.java || types.first() == Any::class.java)){
            t = "" as T
        }
        return t!!
    }
}