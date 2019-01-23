package com.tool.picture.components.video;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

/**
 * @作者          吴孝然
 * @创建日期      2019/1/22 17:49
 * @描述          跳转视频预览
 **/
public class VideoPreview {

    /**
     * 预览视频
     * @param context       上下文
     * @param videoPath     视频路径
     */
    public static void preview(Context context,String videoPath){
        preview(context, videoPath,"");
    }

    /**
     * 预览视频
     * @param context       上下文
     * @param videoPath     视频路径
     * @param thumbImage    视频缩略图,网络视频建议传
     */
    public static void preview(Context context,String videoPath,String thumbImage){
        preview(context, videoPath, thumbImage,true,0,0,"",false,true);
    }

    /**
     * 预览视频
     * @param context                   上下文
     * @param videoPath                 视频路径
     * @param thumbImage                视频缩略图
     * @param isAutoPlay                是否自动播放
     * @param surfaceWidth              视频显示区域宽
     * @param surfaceHeight             视频显示区域高
     * @param secondVideoCachePath      二级缓存目录
     * @param debugModel                模式
     * @param useCache                  是否缓存
     */
    public static void preview(Context context,String videoPath,String thumbImage,boolean isAutoPlay,int surfaceWidth,int surfaceHeight,String secondVideoCachePath,boolean debugModel,
                               boolean useCache){
        Intent intent = new Intent(context,VideoPreviewActivity.class);
        Bundle bundle = new Bundle();
        if (!TextUtils.isEmpty(videoPath)){
            bundle.putString("videoPath",videoPath);
        }
        if (!TextUtils.isEmpty(thumbImage)){
            bundle.putString("thumbImage",thumbImage);
        }
        bundle.putBoolean("isAutoPlay",isAutoPlay);
        if (!TextUtils.isEmpty(videoPath)){
            bundle.putString("videoPath",videoPath);
        }
        if (surfaceWidth != 0){
            bundle.putInt("surfaceWidth",surfaceWidth);
        }
        if (surfaceHeight != 0){
            bundle.putInt("surfaceHeight",surfaceHeight);
        }
        if (!TextUtils.isEmpty(secondVideoCachePath)){
            bundle.putString("secondVideoCachePath",secondVideoCachePath);
        }
        bundle.putBoolean("debugModel",debugModel);
        bundle.putBoolean("useCache",useCache);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
