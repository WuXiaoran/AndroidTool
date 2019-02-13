# AndroidTool
Android快速开发工具

自行下载使用

主要功能：  
1.多图上传及单视频上传组件  
2.查看大图  
3.富文本编辑及预览  
4.视频的简易预览  
5.带进度图片

多图上传及单视频上传组件
-
```JavaScript
UploadConfig uploadConfig = new UploadConfig(this,RecyclerView,newArrayList<LocalMedia(),UploadConfig.SELECT_PIC_NUM_9,UploadConfig.SELECT_PIC_NUM_3);
RecyclerView.setAdapter(uploadConfig.getAdapter());
```
```JavaScript
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
                break;
            case UploadConfig.REQUEST_CODE_MAIN:
                // 预览返回
                uploadConfig.previewDelete(data);
                break;
        }
    }
}
```

查看大图
---
####单图

```JavaScript
PhotoViewer photoViewer = new PhotoViewer();
photoViewer.setClickSingleImg("https://www.duba.com/static/images/public/20181115/4a5d2d7608a3d088c0d0ea5fe5c77c08.gif", img)
        .setShowImageViewInterface(new PhotoViewer.ShowImageViewInterface() {
            @Override
            public void show(ProgressImageView iv, String url) {
                GlideUtil.loadImage(MainActivity.this, url, iv);
            }
        })
        .start(MainActivity.this);
```
####多图
```JavaScript
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
```

富文本编辑及预览
---
####编辑
```JavaScript
// 富文本示例
List<LocalMedia> picList = PictureSelector.obtainMultipleResult(data);
if (picList.size() > 0){
    if (picList.get(0).getPictureType().contains("image")){
        editor.insertImage(picList.get(0).getCompressPath(),RichText.IMAGE);
    }else{
        editor.insertImage(picList.get(0).getPath(),RichText.VIDEO);
    }
}
// 加载网络图片或视频会进度提示
editor.insertImage("http://eb18035.ebenny.com/socialfianace/upload/video/1545669058794.mp4",RichText.VIDEO,500,500);
// 获取编辑数据
editor.getViewData();
```
####预览
```JavaScript
RichText.ViewData viewData = new RichText.ViewData();
richTextView.setViewData(dataList);
```

视频的简易预览
---
```JavaScript
VideoPreview.preview(activity,pic.path);
```

带进度图片
---
```JavaScript
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
```
