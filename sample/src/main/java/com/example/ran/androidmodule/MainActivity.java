package com.example.ran.androidmodule;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.request.transition.Transition;
import com.example.ran.androidmodule.retrofit.TestApi;
import com.example.ran.androidmodule.retrofit.UploadResulte;
import com.example.ran.androidmodule.utils.GlideUtil;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.tool.network.retrofit.ToolRetrofit;
import com.tool.network.retrofit.listener.HttpOnNextListener;
import com.tool.picture.components.photoviewer.PhotoViewer;
import com.tool.picture.components.progressimg.CircleProgressView;
import com.tool.picture.components.progressimg.OnProgressListener;
import com.tool.picture.components.progressimg.ProgressImageView;
import com.tool.picture.components.richtext.RichText;
import com.tool.picture.components.richtext.RichTextEditor;
import com.tool.picture.components.richtext.RichTextView;
import com.tool.picture.components.upload.UploadConfig;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends RxAppCompatActivity {

    private static final String TAG = "MainActivity";

    private ProgressImageView img;
    private CircleProgressView circleProgressView;
    private RecyclerView recyclerView;
    private Banner banner;
    private RecyclerView rv_upload;
    private RichTextEditor editor;
    private RichTextView richTextView;

    private UploadConfig uploadConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img = findViewById(R.id.img);
        circleProgressView = findViewById(R.id.progressView1);
        recyclerView = findViewById(R.id.recyclerView);
        banner = findViewById(R.id.banner);
        rv_upload = findViewById(R.id.rv_upload);
        editor = findViewById(R.id.rte);
        richTextView = findViewById(R.id.rtv);
        ///// 带进度的imageView start /////
        // 第二个参数是设置缩略图，没有就传占位图
        img.load("https://www.duba.com/static/images/public/20181115/4a5d2d7608a3d088c0d0ea5fe5c77c08.gif", R.mipmap.icon_default_store, new OnProgressListener() {
            @Override
            public void onProgress(boolean isComplete, int percentage, long bytesRead, long totalBytes) {
                if (isComplete) {
                    circleProgressView.setVisibility(View.GONE);
                } else {
                    circleProgressView.setVisibility(View.VISIBLE);
                    circleProgressView.setProgress(percentage);
                }
            }

            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) { }
        });
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhotoViewer photoViewer = new PhotoViewer();
                photoViewer.setClickSingleImg("https://www.duba.com/static/images/public/20181115/4a5d2d7608a3d088c0d0ea5fe5c77c08.gif", img)
                        .setShowImageViewInterface(new PhotoViewer.ShowImageViewInterface() {
                            @Override
                            public void show(ProgressImageView iv, String url) {
                                GlideUtil.loadImage(MainActivity.this, url, iv);
                            }
                        })
                        .start(MainActivity.this);
            }
        });
        ///// 带进度的imageView end /////
        ///// 列表查看大图 start /////
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        final ArrayList<String> picData = new ArrayList<>();
        picData.add("https://wallpapers.wallhaven.cc/wallpapers/full/wallhaven-704146.jpg");
        picData.add("https://qiniucdn.fairyever.com/15149577854174.png");
        picData.add("https://qiniucdn.fairyever.com/15149579640159.jpg");
        picData.add("https://qiniucdn.fairyever.com/15149577854174.png");
        picData.add("https://qiniucdn.fairyever.com/15248077829234.jpg");
        picData.add("https://qiniucdn.fairyever.com/15149579640159.jpg");
        picData.add("https://qiniucdn.fairyever.com/15149577854174.png");
        picData.add("https://qiniucdn.fairyever.com/15248077829234.jpg");
        picData.add("https://qiniucdn.fairyever.com/15149579640159.jpg");
        picData.add("https://qiniucdn.fairyever.com/15149577854174.png");
        picData.add("https://qiniucdn.fairyever.com/15248077829234.jpg");
        picData.add("https://qiniucdn.fairyever.com/15149579640159.jpg");
        picData.add("https://qiniucdn.fairyever.com/15149577854174.png");
        picData.add("https://qiniucdn.fairyever.com/15248077829234.jpg");
        MainAdapter adapter = new MainAdapter(picData,this,recyclerView);
        recyclerView.setAdapter(adapter);
        ///// 列表查看大图 end /////
        ///// banner查看大图 start /////
        final Banner banner = (Banner) findViewById(R.id.banner);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(picData);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                PhotoViewer photoViewer = new PhotoViewer();
                photoViewer.setData(picData)
                        .setShowImageViewInterface(new PhotoViewer.ShowImageViewInterface() {
                            @Override
                            public void show(ProgressImageView iv, String url) {
                                GlideUtil.loadImage(MainActivity.this, url, iv);
                            }
                        })
                        .setCurrentPage(position)
                        .setImgContainer(banner)
                        .start(MainActivity.this);
//                richTextView.clearAllLayout();
//                List<RichText.ViewData> dataList = new ArrayList<>();
//                RichText.ViewData viewData = new RichText.ViewData();
//                viewData.setType("text");
//                viewData.setInputStr("1111");
//                dataList.add(viewData);
//                viewData = new RichText.ViewData();
//                viewData.setType("video");
//                viewData.setPath("http://eb18035.ebenny.com/socialfianace/upload/video/1545669058794.mp4");
//                dataList.add(viewData);
//                viewData = new RichText.ViewData();
//                viewData.setType("edit");
//                viewData.setInputStr("2222");
//                dataList.add(viewData);
//                viewData = new RichText.ViewData();
//                viewData.setType("image");
//                viewData.setPath("https://cdn2.jianshu.io/assets/web/nav-logo-4c7bbafe27adc892f3046e6978459bac.png");
//                dataList.add(viewData);
//                viewData = new RichText.ViewData();
//                viewData.setType("text");
//                viewData.setInputStr("3333");
//                dataList.add(viewData);
//                richTextView.setViewData(dataList);
            }
        });
        ///// banner查看大图 end /////
        ///// 选择上传 start /////
        rv_upload.setLayoutManager(new GridLayoutManager(this,3));
        uploadConfig = new UploadConfig(this,rv_upload,new ArrayList<LocalMedia>(),UploadConfig.SELECT_PIC_NUM_9,UploadConfig.SELECT_PIC_NUM_3);
        rv_upload.setAdapter(uploadConfig.getUploadAdapter());
        ///// 选择上传 end /////
        ///// 富文本 start /////
        // 编辑-》onActivityResult有相关示例
        // 预览-》借用了banner的点击事件
        ///// 富文本 end /////
        //// 普通请求 ////
//        ToolRetrofit.http(new TestApi().getSong(1,this,new HttpOnNextListener() {
//            @Override
//            public void onNext(Object o) {
//                Log.e(TAG,"请求成功");
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                super.onError(e);
//            }
//        }));
        //// 上传文件 ////
        String url = "http://workflow.tjcclz.com/GWWorkPlatform/NoticeServlet?GWType=wifiUploadFile";
        File file = new File("/storage/emulated/0/Pictures/1540550492380.jpg");
        ToolRetrofit.http(new TestApi().uploadImage(url,file, this, new HttpOnNextListener() {

            @Override
            public void onNext(Object object) {
                Log.e(TAG,"上传成功");
            }

            @Override
            public void onProgress(long currentBytesCount, long totalBytesCount) {
                super.onProgress(currentBytesCount, totalBytesCount);
            }
        }));
    }

    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            GlideUtil.loadImage(context, path.toString(), imageView);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //开始轮播
        banner.startAutoPlay();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //结束轮播
        banner.stopAutoPlay();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null)
            return;
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 拍照，相册，录像，视频返回
                    uploadConfig.refreshAdapter(data);
                    // 富文本示例
                    List<LocalMedia> picList = PictureSelector.obtainMultipleResult(data);
                    if (picList.size() > 0){
                        if (picList.get(0).getPictureType().contains("image")){
                            editor.insertImage(picList.get(0).getCompressPath(),RichText.IMAGE);
                        }else{
                            editor.insertImage(picList.get(0).getPath(),RichText.VIDEO);
                        }
                    }
//                  // 加载网络图片或视频会进度提示
//                    editor.insertImage("http://eb18035.ebenny.com/socialfianace/upload/video/1545669058794.mp4",RichText.VIDEO,500,500);
                    break;
                case UploadConfig.REQUEST_CODE_MAIN:
                    // 预览返回
                    uploadConfig.previewDelete(data);
                    break;
            }
        }
    }
}
