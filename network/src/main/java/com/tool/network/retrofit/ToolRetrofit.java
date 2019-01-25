package com.tool.network.retrofit;

import android.app.Application;

/**
 * @作者          吴孝然
 * @创建日期      2019/1/24 17:36
 * @描述          全局app
 **/
public class ToolRetrofit {
    private static Application application;
    private static boolean debug; // 是否是调试，方便日志输出
    private static String baseUrl; // 主域名，方便主模块调用

    public static void init(Application app,String baseUrl){
        init(app,baseUrl,true);
    }

    public static void init(Application app,String baseUrl,boolean debug){
        setApplication(app);
        setBaseUrl(baseUrl);
        setDebug(debug);
    }

    public static Application getApplication() {
        return application;
    }

    private static void setApplication(Application application) {
        ToolRetrofit.application = application;
    }

    public static String getBaseUrl() {
        return baseUrl;
    }

    public static void setBaseUrl(String baseUrl) {
        ToolRetrofit.baseUrl = baseUrl;
    }

    public static boolean isDebug() {
        return debug;
    }

    public static void setDebug(boolean debug) {
        ToolRetrofit.debug = debug;
    }
}
