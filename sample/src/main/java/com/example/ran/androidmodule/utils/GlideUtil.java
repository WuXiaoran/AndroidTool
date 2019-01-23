package com.example.ran.androidmodule.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.ran.androidmodule.R;
import com.tool.picture.glide.ToolGlide;

/**
 * @作者          吴孝然
 * @创建日期      2019/1/18 9:55
 * @描述          Glide 工具類
 **/
public class GlideUtil {

    // 默认glide配置
    public static RequestOptions requestOptions = new RequestOptions()
            .placeholder(R.mipmap.ic_launcher)
            .error(R.mipmap.ic_launcher)
            .diskCacheStrategy(DiskCacheStrategy.ALL);

    /**
     * 圆形图片加载,不啟用options,不启用动画
     * @param cxt   上下文
     * @param url   路径
     * @param iv    控件
     */
    public static void loadDefaultImage(Context cxt, String url, ImageView iv) {
        ToolGlide.loadImage(cxt, url, iv);
    }

    /**
     * 普通图片加载,不启用动画
     * @param cxt   上下文
     * @param url   路径
     * @param iv    控件
     */
    public static void loadImage(Context cxt, Object url, ImageView iv) {
        ToolGlide.loadImage(cxt, url, iv, requestOptions,false);
    }

    /**
     * 普通图片加载,启用动画
     * @param cxt   上下文
     * @param url   路径
     * @param iv    控件
     */
    public static void loadImageAn(Context cxt, Object url, ImageView iv) {
        ToolGlide.loadImage(cxt, url, iv, requestOptions,true);
    }

    /**
     * 普通图片加载
     * @param cxt               上下文
     * @param url               路径
     * @param iv                控件
     * @param requestOptions    glide配置
     * @param anim              动画
     */
    public static void loadImage(Context cxt, Object url, ImageView iv,RequestOptions requestOptions,boolean anim) {
        ToolGlide.loadImage(cxt, url, iv, requestOptions,anim);
    }

    /**
     * 圆形图片加载,不啟用options,不启用动画
     * @param cxt   上下文
     * @param url   路径
     * @param iv    控件
     */
    public static void loadDefaultCircleImage(Context cxt, Object url, ImageView iv) {
        ToolGlide.loadCircleImage(cxt, url, iv);
    }

    /**
     * 圆形图片加载,不启用动画
     * @param cxt   上下文
     * @param url   路径
     * @param iv    控件
     */
    public static void loadCircleImage(Context cxt, Object url, ImageView iv) {
        ToolGlide.loadCircleImage(cxt, url, iv, requestOptions,false);
    }

    /**
     * 圆形图片加载,启用动画
     * @param cxt   上下文
     * @param url   路径
     * @param iv    控件
     */
    public static void loadCircleImageAn(Context cxt, Object url, ImageView iv) {
        ToolGlide.loadCircleImage(cxt, url, iv, requestOptions,true);
    }

    /**
     * 圆形图片加载
     * @param cxt               上下文
     * @param url               路径
     * @param iv                控件
     * @param requestOptions    glide配置
     * @param anim              动画
     */
    public static void loadCircleImage(Context cxt, Object url, ImageView iv,RequestOptions requestOptions,boolean anim) {
        ToolGlide.loadCircleImage(cxt, url, iv, requestOptions,anim);
    }

    /**
     * 圆角图片加载,不啟用options,不启用动画
     * @param cxt       上下文
     * @param url       路径
     * @param iv        控件
     * @param radius    圆角
     */
    public static void loadDefaulRoundImage(Context cxt, Object url, ImageView iv, int radius) {
        ToolGlide.loadRoundImage(cxt, url, iv, radius);
    }

    /**
     * 圆角图片加载,不启用动画
     * @param cxt       上下文
     * @param url       路径
     * @param iv        控件
     * @param radius    圆角
     */
    public static void loadRoundImage(Context cxt, Object url, ImageView iv, int radius) {
        ToolGlide.loadRoundImage(cxt, url, iv, radius, requestOptions,false);
    }

    /**
     * 圆角图片加载,启用动画
     * @param cxt       上下文
     * @param url       路径
     * @param iv        控件
     * @param radius    圆角
     */
    public static void loadRoundImageAn(Context cxt, Object url, ImageView iv, int radius) {
        ToolGlide.loadRoundImage(cxt, url, iv, radius, requestOptions,true);
    }

    /**
     * 圆角图片加载
     * @param cxt               上下文
     * @param url               路径
     * @param iv                控件
     * @param radius            圆角
     * @param requestOptions    glide配置
     * @param anim              动画
     */
    public static void loadRoundImage(Context cxt, Object url, ImageView iv, int radius,RequestOptions requestOptions,boolean anim) {
        ToolGlide.loadRoundImage(cxt, url, iv, radius, requestOptions,anim);
    }
}