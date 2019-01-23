package com.tool.picture.components.video;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.tool.picture.R;
import com.tool.picture.components.progressimg.CircleProgressView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

/**
 * @作者          吴孝然
 * @创建日期      2019/1/23 10:38
 * @描述          视频播放控制类
 **/
public abstract class SurfaceVideoViewCreator
        implements
        SurfaceVideoView.OnPlayStateListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnPreparedListener, View.OnClickListener, MediaPlayer.OnCompletionListener,
        MediaPlayer.OnInfoListener {

    private static final String TAG = "SurfaceVideoViewCreator";

    // 缓存文件主目录
    private static final String ROOTPATH = "toolComponents/cachevideos/";

    private RelativeLayout rlcontainer;
    private SurfaceVideoView surfaceVideoView;
    private CircleProgressView progressBar;
    private Button statusButton;
    private ImageView surface_video_screenshot;

    private File videoFile = null; // 缓存文件
    private boolean isUseCache = false; // 是否缓存
    private boolean isExists = false; // 之前是否有缓存
    private boolean mNeedResume; // 是不是需要经过生命周期onResume

    private boolean isFinishDownload = false; // 下载是否完成
    public boolean debugModel = false; // 是否是调试模式
    private String videoPath; // 原本视频路径

    protected abstract Activity getActivity();

    protected abstract boolean setAutoPlay();

    protected abstract int getSurfaceWidth();

    protected abstract int getSurfaceHeight();

    protected abstract void setThumbImage(ImageView thumbImageView);

    protected abstract String getSecondVideoCachePath();

    protected abstract String getVideoPath();

    public void setUseCache(boolean useCache) {
        this.isUseCache = useCache;
    }

    public SurfaceVideoViewCreator(Activity activity, ViewGroup container) {
        View view = LayoutInflater
                .from(activity)
                .inflate(R.layout.tool_surface_video, container, false);

        container.addView(view);

        rlcontainer = view.findViewById(R.id.rl_container);
        surfaceVideoView = (SurfaceVideoView) view.findViewById(R.id.svv);
        progressBar = (CircleProgressView) view.findViewById(R.id.cpv);
        statusButton = (Button) view.findViewById(R.id.btn_play);
        surface_video_screenshot = (ImageView) view.findViewById(R.id.iv_screenshot);
        setThumbImage(surface_video_screenshot);
        videoPath = getVideoPath();

        int width = getSurfaceWidth();
        int height = getSurfaceHeight();
        Bitmap bitmap = getVideoFrame();
        // 无论宽是0还是高是0  全都以屏幕宽做比例适配
        if (width == 0 || height == 0){
            width = activity.getWindowManager().getDefaultDisplay().getWidth();
            if (bitmap == null){
                height = activity.getWindowManager().getDefaultDisplay().getHeight();
            }else{
                height = width * bitmap.getHeight() / bitmap.getWidth();
            }
        }
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rlcontainer.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = height;
        rlcontainer.setLayoutParams(layoutParams);

        surfaceVideoView.setOnPreparedListener(this);
        surfaceVideoView.setOnPlayStateListener(this);
        surfaceVideoView.setOnErrorListener(this);
        surfaceVideoView.setOnInfoListener(this);
        surfaceVideoView.setOnCompletionListener(this);
        surfaceVideoView.setOnClickListener(this);
        statusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击播放
                prepareStart(videoPath);
            }
        });
        if (setAutoPlay()) {
            prepareStart(videoPath);
        }
    }

    /**
     * 通过配置开始播放视频或下载视频
     * @param videoPath     视频路径
     */
    private void prepareStart(String videoPath) {
        try {
            // 查证视频来源
            if (isWebUrl(videoPath)){
                String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath()
                        + File.separator + ROOTPATH;
                File file = new File(rootPath);
                if (!file.exists()) {
                    if (!file.mkdirs()) {
                        throw new NullPointerException("创建 rootPath 失败，注意 6.0+ 的动态申请权限");
                    }
                }
                String[] temp = videoPath.split("/");
                videoFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                                + File.separator + ROOTPATH + temp[temp.length - 1]);
                videoPath = getVideoPath();
                if (debugModel) {
                    // debug模式
                    if (isUseCache) {
                        // 使用缓存播放
                        play(videoFile.getAbsolutePath());
                    } else {
                        // 创建缓存文件
                        if (videoFile.exists()) {
                            videoFile.delete();
                            videoFile.createNewFile();
                        }
                        // 下载完在播放
                        new DownloadVideoAsyncTask().execute(videoPath);
                    }
                    return;
                }
                // 生产模式
                if (videoFile.exists()) {
                    // 存在缓存直接播放
                    isExists = true;
                    play(videoFile.getAbsolutePath());
                } else {
                    // 第二缓存目录，应对此种情况，例如，本地上传是一个目录，那么就可能要到这个目录找一下
                    String secondCacheFilePath = getSecondVideoCachePath();
                    if (!TextUtils.isEmpty(secondCacheFilePath)) {
                        play(secondCacheFilePath);
                        return;
                    }
                    videoFile.createNewFile();
                    // 下载完在播放
                    new DownloadVideoAsyncTask().execute(videoPath);
                }
            }else{
                play(videoPath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onKeyEvent(KeyEvent event) {
        switch (event.getKeyCode()) {
            // 跟随系统音量走
            case KeyEvent.KEYCODE_VOLUME_DOWN:
            case KeyEvent.KEYCODE_VOLUME_UP:
                if (!getActivity().isFinishing())
                    surfaceVideoView.dispatchKeyEvent(getActivity(), event);
                break;
        }
    }

    /**
     * 验证视频是否是网络的
     * @param videoPath     视频路径
     * @return              true 网络 false 本地
     */
    private boolean isWebUrl(String videoPath) {
        return videoPath.contains("https://")
                || videoPath.contains("http://")
                || videoPath.contains("ftp://");
    }

    /**
     * 给宿主生命周期调用
     */
    public void onDestroy() {
        progressBar = null;
        statusButton = null;
        interceptFlag = true;
        if (!isFinishDownload && isWebUrl(videoPath)) {
            // 如果还没下载完，清空缓存文件
            // 同时判断是否是网址链接，防止本地的被删除
            if (videoFile != null && !isExists) {
                videoFile.delete();
            }
        }
        isFinishDownload = false;
        if (surfaceVideoView != null) {
            surfaceVideoView.release();
            surfaceVideoView = null;
        }
    }

    /**
     * 给宿主生命周期调用
     */
    public void onResume() {
        if (surfaceVideoView != null && mNeedResume) {
            mNeedResume = false;
            interceptFlag = false;
            if (surfaceVideoView.isRelease())
                surfaceVideoView.reOpen();
            else
                surfaceVideoView.start();
        }
    }

    /**
     * 给宿主生命周期调用
     */
    public void onPause() {
        if (surfaceVideoView != null) {
            if (surfaceVideoView.isPlaying()) {
                mNeedResume = true;
                surfaceVideoView.pause();
            }
        }
    }

    /**
     * 开始播放
     * @param path      视频路径
     */
    private void play(String path) {
        if (!surfaceVideoView.isPlaying()) {
            progressBar.setVisibility(View.GONE);
            statusButton.setVisibility(View.GONE);
            surfaceVideoView.setVideoPath(path);
        }
    }

    /**
     * 获取视频首帧
     * @return      获取首帧图片
     */
    private Bitmap getVideoFrame(){
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        if (videoPath.startsWith("http"))
            //获取网络视频第一帧图片
            mmr.setDataSource(videoPath, new HashMap());
        else
            //本地视频
            mmr.setDataSource(videoPath);
        Bitmap bitmap = mmr.getFrameAtTime(0);
        return bitmap;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (!getActivity().isFinishing()) statusButton.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.d(TAG, "视频路径：" + videoPath + "，播放失败！错误码：" + what);
        return false;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        switch (what) {
            case MediaPlayer.MEDIA_INFO_BAD_INTERLEAVING:
                // 音频和视频数据不正确
                Log.d(TAG, "视频路径：" + videoPath + "，音频和视频数据不正确！");
                break;
            case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                // 缓冲开始
                if (!getActivity().isFinishing()) {
                    surfaceVideoView.pause();
                }
                break;
            case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                // 缓冲结束
                if (!getActivity().isFinishing())
                    surfaceVideoView.start();
                break;
            case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                // 渲染开始
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    surfaceVideoView.setBackground(null);
                } else {
                    surfaceVideoView.setBackgroundDrawable(null);
                }
                break;
        }
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        // 播放开始
        surfaceVideoView.setVolume(SurfaceVideoView.getSystemVolumn(getActivity()));
        surfaceVideoView.start();
        surface_video_screenshot.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        getActivity().finish();
    }

    @Override
    public void onStateChanged(boolean isPlaying) {
        statusButton.setVisibility(isPlaying ? View.GONE : View.VISIBLE);
    }


    /**
     * 内部下载类，微信的机制是下载好再播放的，也可以直接边下载边播放
     */
    private boolean interceptFlag = false;

    private class DownloadVideoAsyncTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            statusButton.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String[] params) {
            try {
                String urlPath = params[0];
                URL url = new URL(urlPath);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                int length = conn.getContentLength();
                InputStream is = conn.getInputStream();

                FileOutputStream fos = new FileOutputStream(videoFile);
                int count = 0;
                byte buf[] = new byte[1024];
                do {
                    int numread = is.read(buf);
                    count += numread;
                    int progress = (int) (((float) count / length) * 100);
                    //更新进度
//                    Log.d(TAG, "更新进度 " + progress);
                    if (numread <= 0) {
                        publishProgress(100);
                        break;
                    } else {
                        publishProgress(progress);
                    }
                    fos.write(buf, 0, numread);
                } while (!interceptFlag);//点击取消就停止下载.
//                Log.d(TAG, "下载结束 ");
                isFinishDownload = true;
                fos.close();
                is.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @SuppressLint("NewApi")
        @Override
        protected void onProgressUpdate(Integer[] values) {
            if (progressBar == null)
                return;
            int progress = values[0];
            progressBar.setProgress(progress, true);
            if (progress >= 100) {
                // 开始播放
                play(videoFile.getAbsolutePath());
            }
        }

        @Override
        protected void onPostExecute(String o) {
            super.onPostExecute(o);
        }
    }
}
