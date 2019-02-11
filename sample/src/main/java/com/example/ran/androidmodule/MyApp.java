package com.example.ran.androidmodule;

import android.app.Application;

import com.tencent.bugly.crashreport.CrashReport;
import com.tool.network.retrofit.ToolRetrofit;

public class MyApp extends Application {

    public static final String url = "";

    @Override
    public void onCreate() {
        super.onCreate();
        ToolRetrofit.init(this,NetConfig.httpUrl);
        CrashReport.initCrashReport(getApplicationContext(), "9b83f7b828", false);
    }
}
