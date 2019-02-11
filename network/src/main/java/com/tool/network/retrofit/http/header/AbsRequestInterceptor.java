package com.tool.network.retrofit.http.header;


import java.io.UnsupportedEncodingException;

import okhttp3.Interceptor;
import okhttp3.Request;

/**
 * @作者          吴孝然
 * @创建日期      2019/2/11 15:09
 * @描述          拦截器，控制量的更新及新增
 **/
public abstract class AbsRequestInterceptor implements Interceptor {

    public enum Type {
        ADD, UPDATE, REMOVE
    }

    public Type control;

    public Type getControlType() {
        return control;
    }

    public void setControlType(Type control) {
        this.control = control;
    }

    abstract Request interceptor(Request request) throws UnsupportedEncodingException;
}
