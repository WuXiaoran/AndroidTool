package com.example.ran.androidmodule;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.example.ran.androidmodule.utils.PropertiesUtil;

public class NetConfig {

    public final static String httpUrl = PropertiesUtil.getInstance().getProp("url_http");
    public final static String imgUrl = PropertiesUtil.getInstance().getProp("pic_url_http");

    public static int getVersionCode(Context context){
        int localVersion = 100;
        try {
            PackageInfo packageInfo = context.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            localVersion = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }
}
