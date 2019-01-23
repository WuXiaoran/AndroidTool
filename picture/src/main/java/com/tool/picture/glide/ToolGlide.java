package com.tool.picture.glide;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * @作者      吴孝然
 * @创建日期 2019/1/17 18:15
 * @描述      工具Glide
 **/
public class ToolGlide {

    // 图片加载动画时长
    private static final int durationMillis = 300;

    /**
     * 加载普通图片
     * @param context   上下文
     * @param url       路径
     * @param iv        控件
     */
    public static void loadImage(Context context, Object url, ImageView iv) {
        loadImage(context, url, iv, null,false);
    }

    /**
     * 加载普通图片
     * @param context   上下文
     * @param url       路径
     * @param iv        控件
     * @param options   glide配置
     * @param anim      动画
     */
    public static void loadImage(Context context, Object url, ImageView iv, RequestOptions options,boolean anim) {
        RequestOptions requestOptions = new RequestOptions();
        if (options != null){
            requestOptions = options;
        }
        if (anim){
            // 交叉淡入 解决Glide4.0后占位图和过渡动画冲突
            DrawableCrossFadeFactory drawableCrossFadeFactory = new DrawableCrossFadeFactory.Builder(durationMillis).setCrossFadeEnabled(true).build();
            Glide.with(context)
                    .load(url)
                    .transition(DrawableTransitionOptions.with(drawableCrossFadeFactory))
                    .apply(requestOptions)
                    .into(iv);
        }else{
            Glide.with(context)
                    .load(url)
                    .apply(requestOptions)
                    .into(iv);
        }
    }

    /**
     * 加载圆形图片
     * @param context   上下文
     * @param url       路径
     * @param iv        控件
     */
    public static void loadCircleImage(Context context, Object url, ImageView iv) {
        loadCircleImage(context, url, iv,null,false);
    }

    /**
     * 加载圆形图片
     * @param context   上下文
     * @param url       路径
     * @param iv        控件
     * @param options   glide配置
     * @param anim      动画
     */
    public static void loadCircleImage(Context context, Object url, ImageView iv, RequestOptions options,boolean anim) {
        RequestOptions requestOptions = new RequestOptions();
        if (options != null){
            requestOptions = options;
        }
        // 图片圆形处理
        requestOptions.circleCrop();
        if (anim){
            // 交叉淡入 解决Glide4.0后占位图和过渡动画冲突
            DrawableCrossFadeFactory drawableCrossFadeFactory = new DrawableCrossFadeFactory.Builder(300).setCrossFadeEnabled(true).build();
            Glide.with(context)
                    .load(url)
                    .transition(DrawableTransitionOptions.with(drawableCrossFadeFactory))
                    .apply(requestOptions)
                    .into(iv);
        }else{
            Glide.with(context)
                    .load(url)
                    .apply(requestOptions)
                    .into(iv);
        }
    }

    /**
     * 加载圆角图片
     * @param context   上下文
     * @param url       路径
     * @param iv        控件
     * @param radius    圆角角度
     */
    public static void loadRoundImage(Context context, Object url, ImageView iv, int radius) {
        loadRoundImage(context, url, iv, radius, null,false);
    }

    /**
     * 加载圆角图片
     * @param context   上下文
     * @param url       路径
     * @param iv        控件
     * @param radius    圆角角度
     * @param options   glide配置
     * @param anim      动画
     */
    public static void loadRoundImage(Context context, Object url, ImageView iv, int radius, RequestOptions options,boolean anim) {
        RequestOptions requestOptions = new RequestOptions();
        if (options != null){
            requestOptions = options;
        }
        requestOptions.transform(new RoundedCornersTransformation(radius, 0, RoundedCornersTransformation.CornerType.ALL));
        if (anim){
            // 交叉淡入 解决Glide4.0后占位图和过渡动画冲突
            DrawableCrossFadeFactory drawableCrossFadeFactory = new DrawableCrossFadeFactory.Builder(300).setCrossFadeEnabled(true).build();
            Glide.with(context)
                    .load(url)
                    .transition(DrawableTransitionOptions.with(drawableCrossFadeFactory))
                    .apply(requestOptions)
                    .into(iv);
        }else{
            Glide.with(context)
                    .load(url)
                    .apply(requestOptions)
                    .into(iv);
        }
    }

    /**
     * 加载普通图片
     * @param context   上下文
     * @param url       路径
     * @param options   glide配置
     * @param anim      动画
     */
    public static RequestBuilder intoImage(Context context, Object url,RequestOptions options, boolean anim) {
        RequestOptions requestOptions = new RequestOptions();
        if (options != null){
            requestOptions = options;
        }
        if (anim){
            // 交叉淡入 解决Glide4.0后占位图和过渡动画冲突
            DrawableCrossFadeFactory drawableCrossFadeFactory = new DrawableCrossFadeFactory.Builder(durationMillis).setCrossFadeEnabled(true).build();
            return Glide.with(context)
                    .load(url)
                    .transition(DrawableTransitionOptions.with(drawableCrossFadeFactory))
                    .apply(requestOptions);
        }else{
            return Glide.with(context)
                    .load(url)
                    .apply(requestOptions);
        }
    }
}
