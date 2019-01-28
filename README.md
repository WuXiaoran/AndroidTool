# AndroidTool
Android快速开发工具    

项目依赖   [![](https://jitpack.io/v/WuXiaoran/AndroidTool.svg)](https://jitpack.io/#WuXiaoran/AndroidTool)  
`implementation 'com.github.WuXiaoran:AndroidTool:v0.1'`

主要功能：  
1.多图上传及单视频上传组件  
2.查看大图  
3.富文本编辑及预览  
4.视频的简易预览  
5.带进度图片
  
多图上传及单视频上传组件   
-
```
UploadConfig uploadConfig = new UploadConfig(this,RecyclerView,newArrayList<LocalMedia(),UploadConfig.SELECT_PIC_NUM_9,UploadConfig.SELECT_PIC_NUM_3);  
RecyclerView.setAdapter(uploadConfig.getAdapter());
```  
```  
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
