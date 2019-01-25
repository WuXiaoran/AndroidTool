package com.example.ran.androidmodule;

import android.app.Application;

import com.tool.network.retrofit.ToolRetrofit;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ToolRetrofit.init(this,"");
    }
}
