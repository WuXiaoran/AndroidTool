package com.tool.picture.utils;

import android.app.Activity;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.List;

/**
 * @作者          吴孝然
 * @创建日期      2019/1/21 10:41
 * @描述          进入照相机组件配置,默认不裁剪,不显示gif
 **/
public class PictureSelectorUtil {

      /** 以下是例子：用不到的api可以不写 */
//    public static void initMultiConfig(Activity activity, int maxTotal, Boolean circle, List<LocalMedia> selectList) {
//        PictureSelector.create(activity)
//                .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()
//                .maxSelectNum(maxTotal)// 最大图片选择数量 int
//                .minSelectNum(0)// 最小选择数量 int
//                .imageSpanCount(3)// 每行显示个数 int
//                .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
//                .previewImage(true)// 是否可预览图片 true or false
//                .previewVideo(true)// 是否可预览视频 true or false
//                .enablePreviewAudio(false) // 是否可播放音频 true or false
//                .isCamera(true)// 是否显示拍照按钮 true or false
//                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
//                .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
//                .setOutputCameraPath("/CustomPath")// 自定义拍照保存路径,可不填
//                .enableCrop(true)// 是否裁剪 true or false
//                .compress(true)// 是否压缩 true or false
//                .compressMode(PictureConfig.LUBAN_COMPRESS_MODE)//系统自带 or 鲁班压缩 PictureConfig.SYSTEM_COMPRESS_MODE or LUBAN_COMPRESS_MODE
//                .compressGrade(Luban.THIRD_GEAR)// luban压缩档次，默认3档 Luban.THIRD_GEAR、Luban.FIRST_GEAR、Luban.CUSTOM_GEAR
//                .compressMaxKB(1024)//压缩最大值kb compressGrade()为Luban.CUSTOM_GEAR有效 int
//                .compressWH() // 压缩宽高比 compressGrade()为Luban.CUSTOM_GEAR有效  int
//                .glideOverride(160, 160)// int glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
//                .withAspectRatioctRatio()// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
//                .hideBottomControls(true)// 是否显示uCrop工具栏，默认不显示 true or false
//                .isGif(true)// 是否显示gif图片 true or false
//                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽 true or false
//                .circleDimmedLayer(circle)// 是否圆形裁剪 true or false
//                .showCropFrame(circle ? false : true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
//                .showCropGrid(circle ? false : true)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
//                .openClickSound(false)// 是否开启点击声音 true or false
//                .selectionMedia(selectList)// 是否传入已选图片 List<LocalMedia> list
//                .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
//                .cropWHCompressQuality()// 裁剪压缩质量 默认90 int
//                .cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效 int
//                .rotateEnabled(false) // 裁剪是否可旋转图片 true or false
//                .scaleEnabled(true)// 裁剪是否可放大缩小图片 true or false
//                .videoQuality(1)// 视频录制质量 0 or 1 int
//                .videoSecond()// 显示多少秒以内的视频or音频也可适用 int
//                .recordVideoSecond(60)//视频秒数录制 默认60s int
//                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
//    }

    /**
     * 单独启动拍照或视频 根据 PictureMimeType 自动识别
     * @param activity      上下文
     * @param circle        是否进行圆形处理
     * @param type          0 拍照 1 录像
     */
    public static void openCamera(Activity activity,Boolean circle,int type){
        PictureSelector.create(activity)
                .openCamera(type == 0 ? PictureMimeType.ofImage() : PictureMimeType.ofVideo())
                .enableCrop(false)// 是否裁剪 true or false
                .compress(true)// 是否压缩 true or false
                .glideOverride(130, 130)// int glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .hideBottomControls(true)// 是否显示uCrop工具栏，默认不显示 true or false
                .isGif(false)// 是否显示gif图片 true or false
                .circleDimmedLayer(circle)
                .freeStyleCropEnabled(false)// 裁剪框是否可拖拽 true or false
                .showCropFrame(circle ? false : true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                .showCropGrid(circle ? false : true)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                .openClickSound(false)// 是否开启点击声音 true or false
                .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
                .rotateEnabled(true) // 裁剪是否可旋转图片 true or false
                .scaleEnabled(true)// 裁剪是否可放大缩小图片 true or false
                .withAspectRatio(1,1)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .videoQuality(1)// 视频录制质量 0 or 1 int
                .recordVideoSecond(10)//视频秒数录制 默认60s int
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    /**
     * 单独启动拍照或视频 根据 PictureMimeType 自动识别
     * @param activity      上下文
     * @param circle        是否进行圆形处理
     * @param type          0 拍照 1 录像
     * @param enableCrop    是否裁剪
     */
    public static void openCamera(Activity activity,Boolean circle,int type,boolean enableCrop){
        PictureSelector.create(activity)
                .openCamera(type == 0 ? PictureMimeType.ofImage() : PictureMimeType.ofVideo())
                .enableCrop(enableCrop)// 是否裁剪 true or false
                .compress(true)// 是否压缩 true or false
                .glideOverride(130, 130)// int glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .hideBottomControls(true)// 是否显示uCrop工具栏，默认不显示 true or false
                .isGif(false)// 是否显示gif图片 true or false
                .circleDimmedLayer(circle)
                .freeStyleCropEnabled(false)// 裁剪框是否可拖拽 true or false
                .showCropFrame(circle ? false : true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                .showCropGrid(circle ? false : true)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                .openClickSound(false)// 是否开启点击声音 true or false
                .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
                .rotateEnabled(true) // 裁剪是否可旋转图片 true or false
                .scaleEnabled(true)// 裁剪是否可放大缩小图片 true or false
                .withAspectRatio(1,1)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .videoQuality(1)// 视频录制质量 0 or 1 int
                .recordVideoSecond(60)//视频秒数录制 默认60s int
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    /**
     * 单独启动拍照或视频 根据 PictureMimeType 自动识别
     * @param activity     上下文
     * @param circle       是否进行圆形处理
     * @param type         0 拍照 1 录像
     * @param x            宽
     * @param y            高
     */
    public static void openCamera(Activity activity,Boolean circle,int type,int x,int y){
        PictureSelector.create(activity)
                .openCamera(type == 0 ? PictureMimeType.ofImage() : PictureMimeType.ofVideo())
                .enableCrop(true)// 是否裁剪 true or false
                .compress(true)// 是否压缩 true or false
                .glideOverride(130, 130)// int glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .hideBottomControls(true)// 是否显示uCrop工具栏，默认不显示 true or false
                .isGif(false)// 是否显示gif图片 true or false
                .circleDimmedLayer(circle)
                .freeStyleCropEnabled(false)// 裁剪框是否可拖拽 true or false
                .showCropFrame(circle ? false : true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                .showCropGrid(circle ? false : true)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                .openClickSound(false)// 是否开启点击声音 true or false
                .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
                .rotateEnabled(true) // 裁剪是否可旋转图片 true or false
                .scaleEnabled(true)// 裁剪是否可放大缩小图片 true or false
                .withAspectRatio(x,y)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .videoQuality(1)// 视频录制质量 0 or 1 int
                .recordVideoSecond(20)//视频秒数录制 默认60s int
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    /**
     * 启动相册
     * @param activity      上下文
     * @param circle        是否进行圆形处理
     * @param type          0 拍照 1 录像
     * @param selectList    之前选择图片或视频
     * @param maxTotal      可选最大数量
     */
    public static void openGallery(Activity activity,Boolean circle,int type,List<LocalMedia> selectList,int maxTotal){
        PictureSelector.create(activity)
                .openGallery(type == 0 ? PictureMimeType.ofImage() : PictureMimeType.ofVideo())
                .maxSelectNum(type == 0 ? maxTotal : 1)// 最大图片选择数量 int
                .minSelectNum(0)// 最小选择数量 int
                .previewImage(true)// 是否可预览图片 true or false
                .isCamera(false)// 是否显示拍照按钮 true or false
                .enableCrop(false)// 是否裁剪 true or false
                .compress(true)// 是否压缩 true or false
                .glideOverride(130, 130)// int glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .hideBottomControls(true)// 是否显示uCrop工具栏，默认不显示 true or false
                .isGif(false)// 是否显示gif图片 true or false
                .circleDimmedLayer(circle)
                .freeStyleCropEnabled(false)// 裁剪框是否可拖拽 true or false
                .showCropFrame(circle ? false : true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                .showCropGrid(circle ? false : true)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                .openClickSound(false)// 是否开启点击声音 true or false
                .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
                .rotateEnabled(true) // 裁剪是否可旋转图片 true or false
                .scaleEnabled(true)// 裁剪是否可放大缩小图片 true or false
                .withAspectRatio(1,1)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .selectionMedia(selectList)// 是否传入已选图片 List<LocalMedia> list
                .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    /**
     * 启动相册
     * @param activity      上下文
     * @param circle        是否进行圆形处理
     * @param type          0 拍照 1 录像
     * @param selectList    之前选择的图片或视频
     * @param maxTotal      可选最大数量
     * @param enableCrop    是否裁剪
     */
    public static void openGallery(Activity activity,Boolean circle,int type,List<LocalMedia> selectList,int maxTotal,boolean enableCrop){
        PictureSelector.create(activity)
                .openGallery(type == 0 ? PictureMimeType.ofImage() : PictureMimeType.ofVideo())
                .maxSelectNum(type == 0 ? maxTotal : 1)// 最大图片选择数量 int
                .minSelectNum(0)// 最小选择数量 int
                .previewImage(true)// 是否可预览图片 true or false
                .isCamera(false)// 是否显示拍照按钮 true or false
                .enableCrop(enableCrop)// 是否裁剪 true or false
                .compress(true)// 是否压缩 true or false
                .glideOverride(130, 130)// int glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .hideBottomControls(true)// 是否显示uCrop工具栏，默认不显示 true or false
                .isGif(false)// 是否显示gif图片 true or false
                .circleDimmedLayer(circle)
                .freeStyleCropEnabled(false)// 裁剪框是否可拖拽 true or false
                .showCropFrame(circle ? false : true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                .showCropGrid(circle ? false : true)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                .openClickSound(false)// 是否开启点击声音 true or false
                .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
                .rotateEnabled(true) // 裁剪是否可旋转图片 true or false
                .scaleEnabled(true)// 裁剪是否可放大缩小图片 true or false
                .withAspectRatio(1,1)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .selectionMedia(selectList)// 是否传入已选图片 List<LocalMedia> list
                .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    /**
     * 启动相册
     * @param activity      上下文
     * @param circle        是否进行圆形处理
     * @param type          0 拍照 1 录像
     * @param selectList    之前选择的图片或视频
     * @param maxTotal      可选最大数量
     * @param x             宽
     * @param y             高
     */
    public static void openGallery(Activity activity,Boolean circle,int type,List<LocalMedia> selectList,int maxTotal,int x,int y){
        PictureSelector.create(activity)
                .openGallery(type == 0 ? PictureMimeType.ofImage() : PictureMimeType.ofVideo())
                .maxSelectNum(type == 0 ? maxTotal : 1)// 最大图片选择数量 int
                .minSelectNum(0)// 最小选择数量 int
                .previewImage(true)// 是否可预览图片 true or false
                .isCamera(false)// 是否显示拍照按钮 true or false
                .enableCrop(true)// 是否裁剪 true or false
                .compress(true)// 是否压缩 true or false
                .glideOverride(130, 130)// int glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .hideBottomControls(true)// 是否显示uCrop工具栏，默认不显示 true or false
                .isGif(false)// 是否显示gif图片 true or false
                .circleDimmedLayer(circle)
                .freeStyleCropEnabled(false)// 裁剪框是否可拖拽 true or false
                .showCropFrame(circle ? false : true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                .showCropGrid(circle ? false : true)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                .openClickSound(false)// 是否开启点击声音 true or false
                .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
                .rotateEnabled(true) // 裁剪是否可旋转图片 true or false
                .scaleEnabled(true)// 裁剪是否可放大缩小图片 true or false
                .withAspectRatio(x,y)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .selectionMedia(selectList)// 是否传入已选图片 List<LocalMedia> list
                .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    /**
     * 初始化单张图片选择的配置
     * @param activity      上下文
     * @param circle        是否进行圆形处理
     */
    public static void initSingleConfig(Activity activity,Boolean circle) {
        // 进入相册 以下是例子：用不到的api可以不写
        PictureSelector.create(activity)
                .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()
                .maxSelectNum(1)// 最大图片选择数量 int
                .minSelectNum(0)// 最小选择数量 int
                .imageSpanCount(3)// 每行显示个数 int
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .previewImage(true)// 是否可预览图片 true or false
                .previewVideo(false)// 是否可预览视频 true or false
                .enablePreviewAudio(false) // 是否可播放音频 true or false
                .isCamera(true)// 是否显示拍照按钮 true or false
                .enableCrop(true)// 是否裁剪 true or false
                .compress(true)// 是否压缩 true or false
                .glideOverride(130, 130)// int glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .hideBottomControls(true)// 是否显示uCrop工具栏，默认不显示 true or false
                .isGif(false)// 是否显示gif图片 true or false
                .circleDimmedLayer(circle)
                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽 true or false
                .showCropFrame(circle ? false : true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                .showCropGrid(circle ? false : true)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                .openClickSound(false)// 是否开启点击声音 true or false
                .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
                .rotateEnabled(true) // 裁剪是否可旋转图片 true or false
                .scaleEnabled(true)// 裁剪是否可放大缩小图片 true or false
                .withAspectRatio(1,1)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }

    /**
     * 初始化单张图片选择的配置
     * @param activity      上下文
     * @param circle        是否进行圆形处理
     * @param x             宽
     * @param y             高
     */
    public static void initSingleConfig(Activity activity,Boolean circle,int x,int y) {
        // 进入相册 以下是例子：用不到的api可以不写
        PictureSelector.create(activity)
                .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()
                .maxSelectNum(1)// 最大图片选择数量 int
                .minSelectNum(0)// 最小选择数量 int
                .imageSpanCount(3)// 每行显示个数 int
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .previewImage(true)// 是否可预览图片 true or false
                .previewVideo(false)// 是否可预览视频 true or false
                .enablePreviewAudio(false) // 是否可播放音频 true or false
                .isCamera(true)// 是否显示拍照按钮 true or false
                .enableCrop(true)// 是否裁剪 true or false
                .compress(true)// 是否压缩 true or false
                .glideOverride(130, 130)// int glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .hideBottomControls(true)// 是否显示uCrop工具栏，默认不显示 true or false
                .isGif(false)// 是否显示gif图片 true or false
                .circleDimmedLayer(circle)
                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽 true or false
                .showCropFrame(circle ? false : true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                .showCropGrid(circle ? false : true)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                .openClickSound(false)// 是否开启点击声音 true or false
                .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
                .rotateEnabled(true) // 裁剪是否可旋转图片 true or false
                .scaleEnabled(true)// 裁剪是否可放大缩小图片 true or false
                .withAspectRatio(x,y)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }
}
