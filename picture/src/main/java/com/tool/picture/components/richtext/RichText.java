package com.tool.picture.components.richtext;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.request.transition.Transition;
import com.tool.picture.R;
import com.tool.picture.components.progressimg.CircleProgressView;
import com.tool.picture.components.progressimg.OnProgressListener;
import com.tool.picture.components.video.VideoPreview;
import com.tool.picture.utils.Util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @作者          吴孝然
 * @创建日期      2019/1/22 15:24
 * @描述          富文本基类
 **/
@SuppressLint({ "NewApi", "InflateParams" })
public class RichText extends ScrollView {

    //// view类型 ////
    public static final String EDIT = "edit";
    public static final String TEXT = "text";
    public static final String IMAGE = "image";
    public static final String VIDEO = "video";

    //// 文本提示语 ////
    protected String defaultText = "请输入内容";
    protected String addText = "插入文字";
    protected String emptyText = "没有内容";

    //// 缓存缩略图主目录及后缀 ////
    private static final String ROOTPATH = "toolComponents/cacheimages/";
    private static final String SUFFIX = "_cacheimage.jpg";

    protected Activity activity;
    protected int viewTagIndex = 1; // 新生的view都会打一个tag，对每个view来说，这个tag是唯一的。
    protected LinearLayout allLayout; // 这个是所有子view的容器，scrollView内部的唯一一个ViewGroup
    protected LayoutInflater inflater;
    protected OnClickListener btnListener; // 图片右上角红叉按钮监听器
    protected int disappearingImageIndex = 0;
    protected ArrayList<String> imagePaths; // 图片地址集合
    protected RichTextEditor.OnDeleteImageListener onDeleteImageListener; // 删除图片的接口

    public interface OnDeleteImageListener{
        void onDeleteImage(String imagePath);
    }


    public RichText(Context context) {
        super(context,null);
    }

    public RichText(Context context, AttributeSet attrs) {
        super(context, attrs,0);
    }

    public RichText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        activity = (Activity) context;
        imagePaths = new ArrayList<>();
        inflater = LayoutInflater.from(context);

        // 初始化allLayout
        allLayout = new LinearLayout(context);
        allLayout.setOrientation(LinearLayout.VERTICAL);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        allLayout.setPadding(Util.dp2px(activity,12),Util.dp2px(activity,6),Util.dp2px(activity,12),Util.dp2px(activity,6));// 设置间距，防止生成图片时文字太靠边，不能用margin，否则有黑边
        addView(allLayout, layoutParams);

        // 图片删除处理
        btnListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v instanceof DataImageView){
                    onImageClick((RelativeLayout) v.getParent().getParent());
                } else if (v instanceof ImageView){
                    RelativeLayout parentView = (RelativeLayout) v.getParent();
                    onImageCloseClick(parentView);
                }
            }
        };
    }

    /**
     * 处理图片删除的点击事件
     * @param view		整个image对应的relativeLayout view
     * @type 			删除类型 0代表backspace删除 1代表按红叉按钮删除
     */
    protected void onImageCloseClick(View view) {
        disappearingImageIndex = allLayout.indexOfChild(view);
        List<ViewData> dataList = buildEditData();
        ViewData editData = dataList.get(disappearingImageIndex);
        imagePaths.remove(editData.path);
        allLayout.removeView(view);
        if (editData.path != null){
            if (onDeleteImageListener != null){
                onDeleteImageListener.onDeleteImage(editData.path);
            }
        }
    }

    /**
     * 在特定位置添加ImageView
     * @param index		位置下标
     * @param imagePath	图片路径
     * @param type			图片具体类型，可能是图片或者是视频的首帧
     * @param _width 		预设宽度
     * @param _height 		预设高度
     */
    protected void addImageViewAtIndex(final int index, final String imagePath, final String type, int _width, int _height) {
        imagePaths.add(imagePath);
        final RelativeLayout imageLayout = createImageLayout();
        final DataImageView imageView = (DataImageView) imageLayout.findViewById(R.id.div_data);
        final CircleProgressView circleProgressView = (CircleProgressView) imageLayout.findViewById(R.id.pv);
        final ImageView deleteImage = (ImageView) imageLayout.findViewById(R.id.iv_close);
        // 加载前预设置下宽高，通常是加载网络图片或视频
        if (_width != 0 && _height != 0){
            int width = allLayout.getWidth() - allLayout.getPaddingLeft() - allLayout.getPaddingRight(); // 控件固定宽度
            // 宽度固定,然后根据原始宽高比得到此固定宽度需要的高度
            int height = width * _height / _width;
            ViewGroup.LayoutParams para = imageView.getLayoutParams();
            para.height = height;
            para.width = width;
            imageView.setLayoutParams(para);
        }
        imageView.load(imagePath, R.mipmap.tool_img_fail, new OnProgressListener() {
            @Override
            public void onProgress(boolean isComplete, int percentage, long bytesRead, long totalBytes) {
                if (isComplete) {
                    circleProgressView.setVisibility(View.GONE);
                    if (type.equals(VIDEO)){
                        imageView.setCenterImgShow(true);
                    }
                } else {
                    circleProgressView.setVisibility(View.VISIBLE);
                    circleProgressView.setProgress(percentage);
                }
            }

            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                Bitmap resourceB = ((BitmapDrawable)resource).getBitmap();
                int imageWidth = resourceB.getWidth();
                int imageHeight = resourceB.getHeight();
                // 判断是否是视频，如果是视频就固定250高度，FIT_CENTER
                if (type.equals(VIDEO)){
                    int width = allLayout.getWidth() - allLayout.getPaddingLeft() - allLayout.getPaddingRight(); // 控件固定宽度
                    ViewGroup.LayoutParams para = imageView.getLayoutParams();
                    if (imageHeight < Util.dp2px(activity,250)){
                        para.height = imageHeight;
                    }
                    para.width = width;
                    imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) deleteImage.getLayoutParams();
                    layoutParams.setMargins(0,Util.dp2px(activity,4),Util.dp2px(activity,4),0); // 美观下删除键
                    deleteImage.setLayoutParams(layoutParams);
                    // 视频的话存储宽高,并且缓存一张关键帧的图片
                    imageView.setResourceWidth(imageWidth);
                    imageView.setResourceHeight(imageHeight);
                    imageView.setThumbImage(saveThumb(imagePath,resourceB));
                }else{
                    int width = allLayout.getWidth() - allLayout.getPaddingLeft() - allLayout.getPaddingRight(); // 控件固定宽度
                    // 宽度固定,然后根据原始宽高比得到此固定宽度需要的高度
                    int height = width * imageHeight / imageWidth;
                    ViewGroup.LayoutParams para = imageView.getLayoutParams();
                    para.height = height;
                    para.width = width;
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) deleteImage.getLayoutParams();
                    layoutParams.setMargins(0,Util.dp2px(activity,4),Util.dp2px(activity,4),0); // 美观下删除键
                    deleteImage.setLayoutParams(layoutParams);
                    // 图片的话单纯存储宽高
                    imageView.setResourceWidth(imageWidth);
                    imageView.setResourceHeight(imageHeight);
                }
            }
        });
        imageView.setAbsolutePath(imagePath);
        imageView.setType(type);
        allLayout.addView(imageLayout, index);
    }

    /**
     * 处理图片点击事件
     * @param view		控件
     */
    protected void onImageClick(View view) {
        disappearingImageIndex = allLayout.indexOfChild(view);
        // 根据点击的view所在位置获取到图片地址
        List<ViewData> dataList = buildEditData();
        ViewData editData = dataList.get(disappearingImageIndex);
        if (editData.path != null){
            if (editData.type.equals(VIDEO)){
                VideoPreview.preview(activity,editData.path);
            }
        }
    }

    /**
     * 清空所有布局
     */
    public void clearAllLayout(){
        allLayout.removeAllViews();
    }

    /**
     * 生成图片View
     * @return		包含删除的图片View
     */
    protected RelativeLayout createImageLayout() {
        RelativeLayout layout = (RelativeLayout) inflater.inflate(
                R.layout.tool_rich_imageview, null);
        layout.setTag(viewTagIndex++);
        View closeView = layout.findViewById(R.id.iv_close);
        closeView.setTag(layout.getTag());
        closeView.setOnClickListener(btnListener);
        View imageView = layout.findViewById(R.id.div_data);
        imageView.setOnClickListener(btnListener);
        return layout;
    }

    /**
     * 内部调用，获取所有view的数据，包括空的输入框
     * @return		所有view的数据
     */
    protected List<ViewData> buildEditData() {
        List<ViewData> dataList = new ArrayList<ViewData>();
        int num = allLayout.getChildCount();
        for (int index = 0; index < num; index++) {
            View itemView = allLayout.getChildAt(index);
            ViewData itemData = new ViewData();
            if (itemView instanceof EditText) {
                EditText item = (EditText) itemView;
                itemData.inputStr = item.getText().toString();
                itemData.type = EDIT;
            } else if (itemView instanceof TextView){
                TextView item = (TextView) itemView;
                itemData.inputStr = item.getText().toString();
                itemData.type = TEXT;
            } else if (itemView instanceof RelativeLayout) {
                DataImageView item = (DataImageView) itemView.findViewById(R.id.div_data);
                itemData.path = item.getAbsolutePath();
                itemData.type = item.getType();
                itemData.resourceWidth = item.getResourceWidth();
                itemData.resourceHeight = item.getResourceHeight();
                if (VIDEO.equals(itemData.type)){
                    itemData.thumbImage = item.getThumbImage();
                }
            }
            dataList.add(itemData);
        }
        return dataList;
    }

    /**
     * 外部调用，获取所有view的数据，不包括空的输入框
     * @return		所有view的数据
     */
    public List<ViewData> getViewData() {
        List<ViewData> dataList = new ArrayList<ViewData>();
        int num = allLayout.getChildCount();
        for (int index = 0; index < num; index++) {
            View itemView = allLayout.getChildAt(index);
            ViewData itemData = new ViewData();
            if (itemView instanceof EditText) {
                EditText item = (EditText) itemView;
                itemData.inputStr = item.getText().toString();
                itemData.type = EDIT;
            } else if (itemView instanceof TextView){
                TextView item = (TextView) itemView;
                itemData.inputStr = item.getText().toString();
                itemData.type = TEXT;
            } else if (itemView instanceof RelativeLayout) {
                DataImageView item = (DataImageView) itemView.findViewById(R.id.div_data);
                itemData.path = item.getAbsolutePath();
                itemData.type = item.getType();
                itemData.resourceWidth = item.getResourceWidth();
                itemData.resourceHeight = item.getResourceHeight();
                if (VIDEO.equals(itemData.type)){
                    itemData.thumbImage = item.getThumbImage();
                }
            }
            if ((itemData.path == null || itemData.path.isEmpty()) && (itemData.inputStr == null || itemData.inputStr.isEmpty())){
            }else{
                dataList.add(itemData);
            }
        }
        return dataList;
    }

    /** 保存方法 */
    public String saveThumb(String videoPath,Bitmap bitmap) {
        String[] temp = videoPath.split("/");
        String childFilePath = temp[temp.length - 1];
        childFilePath = childFilePath.substring(0,childFilePath.indexOf('.')) + SUFFIX;
        File thumbImage = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + ROOTPATH + childFilePath);
        try {
            if (thumbImage.exists()) {
                thumbImage.delete();
            }else{
                if (!thumbImage.mkdirs()) {
                    throw new NullPointerException("创建 rootPath 失败，注意 6.0+ 的动态申请权限");
                }
            }
            thumbImage.createNewFile();
            FileOutputStream out = new FileOutputStream(thumbImage);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return thumbImage.getAbsolutePath();
        }
    }

    public static class ViewData {
        // 文本
        public String inputStr;
        // 图片资源还是视频资源
        public String path;
        // 标识视频还是图片或者输入框 video-视频 image-图片 edit-输入框
        public String type;
        // 缩略图
        public String thumbImage;
        // 资源宽
        public int resourceWidth;
        // 资源高
        public int resourceHeight;

        public String getInputStr() {
            return inputStr;
        }

        public void setInputStr(String inputStr) {
            this.inputStr = inputStr;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getThumbImage() {
            return thumbImage;
        }

        public void setThumbImage(String thumbImage) {
            this.thumbImage = thumbImage;
        }

        public int getResourceWidth() {
            return resourceWidth;
        }

        public void setResourceWidth(int resourceWidth) {
            this.resourceWidth = resourceWidth;
        }

        public int getResourceHeight() {
            return resourceHeight;
        }

        public void setResourceHeight(int resourceHeight) {
            this.resourceHeight = resourceHeight;
        }
    }

    public ArrayList<String> getImagePaths() {
        return imagePaths;
    }

    public void setImagePaths(ArrayList<String> imagePaths) {
        this.imagePaths = imagePaths;
    }

    public LinearLayout getAllLayout() {
        return allLayout;
    }

    public void setAllLayout(LinearLayout allLayout) {
        this.allLayout = allLayout;
    }

    public RichTextEditor.OnDeleteImageListener getOnDeleteImageListener() {
        return onDeleteImageListener;
    }

    public void setOnDeleteImageListener(RichTextEditor.OnDeleteImageListener onDeleteImageListener) {
        this.onDeleteImageListener = onDeleteImageListener;
    }

    public String getDefaultText() {
        return defaultText;
    }

    public void setDefaultText(String defaultText) {
        this.defaultText = defaultText;
    }

    public String getAddText() {
        return addText;
    }

    public void setAddText(String addText) {
        this.addText = addText;
    }

    public String getEmptyText() {
        return emptyText;
    }

    public void setEmptyText(String emptyText) {
        this.emptyText = emptyText;
    }
}
