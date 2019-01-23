package com.tool.picture.components.video;


import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.tool.picture.R;
import com.tool.picture.glide.ToolGlide;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class VideoPreviewActivity extends AppCompatActivity {

    private SurfaceVideoViewCreator surfaceVideoViewCreator;
    private static final int writeCode = 1;
    private boolean isAutoPlay = true; // 是否自动播放，默认为true
    private int surfaceWidth = 0; // 视频显示区域宽度，默认为0 即适配手机宽度
    private int surfaceHeight = 0; // 视频显示区域高度，默认为0 即适配手机高度
    private String thumbImage; // 视频缩略图，如果没有就会去拿视频首帧
    private String secondVideoCachePath; // 二级目录
    private String videoPath; // 视频路径
    private boolean debugModel = false; // 模式，区别主要是正式环境会用到二级目录
    private boolean useCache = true; // 是否缓存，默认为true

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tool_activity_video_preview);
        Bundle bundle = getIntent().getExtras();
        isAutoPlay = bundle.getBoolean("isAutoPlay",true);
        surfaceWidth = bundle.getInt("surfaceWidth",0);
        surfaceHeight = bundle.getInt("surfaceHeight",0);
        thumbImage = bundle.getString("thumbImage","");
        secondVideoCachePath = bundle.getString("secondVideoCachePath","");
        videoPath = bundle.getString("videoPath","");
        debugModel = bundle.getBoolean("debugModel",false);
        useCache = bundle.getBoolean("useCache",true);
        // 预览视频路径不能为空
        if (TextUtils.isEmpty(videoPath)){
            finish();
        }
        // 获取存储权限
        ActivityCompat.requestPermissions(VideoPreviewActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, writeCode);
        // 配置预览视频信息
        surfaceVideoViewCreator = new SurfaceVideoViewCreator(this,(RelativeLayout)findViewById(R.id.rl)) {
                    @Override
                    protected Activity getActivity() {
                        return VideoPreviewActivity.this;     /** 当前的 Activity */
                    }

                    @Override
                    protected boolean setAutoPlay() {
                        return isAutoPlay;                 /** true 适合用于，已进入就自动播放的情况 */
                    }

                    @Override
                    protected int getSurfaceWidth() {
                        return surfaceWidth;                     /** Video 的显示区域宽度，0 就是适配手机宽度 */
                    }
                    @Override
                    protected int getSurfaceHeight() {
                        return surfaceHeight;                   /** Video 的显示区域高度，dp 为单位 */
                    }
                    @Override
                    protected void setThumbImage(ImageView thumbImageView) {
                        RequestOptions requestOptions = new RequestOptions();
                        requestOptions.centerCrop()
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL);
                        ToolGlide.loadImage(VideoPreviewActivity.this,TextUtils.isEmpty(thumbImage) ? videoPath : thumbImage,thumbImageView,
                                requestOptions,false);
                    }

                    /** 这个是设置返回自己的缓存路径，
                     * 应对这种情况：
                     * 录制的时候是在另外的目录，播放的时候默认是在下载的目录，所以可以在这个方法处理返回缓存
                     */
                    @Override
                    protected String getSecondVideoCachePath() {
                        return secondVideoCachePath;
                    }

                    @Override
                    protected String getVideoPath() {
                        return videoPath;
                    }
                };
        surfaceVideoViewCreator.debugModel = debugModel;
        surfaceVideoViewCreator.setUseCache(useCache);
    }

    @Override
    protected void onPause() {
        super.onPause();
        surfaceVideoViewCreator.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        surfaceVideoViewCreator.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        surfaceVideoViewCreator.onDestroy();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        surfaceVideoViewCreator.onKeyEvent(event); /** 声音的大小调节 */
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case writeCode:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    break;
                }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
