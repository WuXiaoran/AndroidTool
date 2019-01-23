package com.tool.picture.components.richtext;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;

import com.tool.picture.R;
import com.tool.picture.components.progressimg.ProgressImageView;
import com.tool.picture.utils.Util;

/**
 * @作者          吴孝然
 * @创建日期      2019/1/21 18:01
 * @描述          自定义ImageView，可以存放Bitmap和Path等信息
 **/
@SuppressLint("AppCompatCustomView")
public class DataImageView extends ProgressImageView {

    // 图片或视频路径
    private String absolutePath;
    // 中间的播放按钮
    private Bitmap playBitmap;
    // 是否显示播放按钮
    private boolean isCenterImgShow;
    // 图片类型
    private String type;
    // 缩略图
    public String thumbImage;
    // 资源宽
    public int resourceWidth;
    // 资源高
    public int resourceHeight;
    private Paint paint;

    public DataImageView(Context context) {
        this(context, null);
        init();
    }

    public DataImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init();
    }

    public DataImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void setCenterImgShow(boolean centerImgShow) {
        isCenterImgShow = centerImgShow;
        if (isCenterImgShow) {
            playBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.tool_img_play);
            // 按比例缩放下bitmap
            int originalWidth = playBitmap.getWidth();
            int originalHeight = playBitmap.getHeight();
            int newHeight = Util.dp2px(getContext(),65); // 65高度
            float scale = ((float) newHeight) / originalHeight;
            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale);
            playBitmap = Bitmap.createBitmap(playBitmap, 0, 0,
                    originalWidth, originalHeight, matrix, true);
            invalidate();
        }
    }

    private void init() {
        paint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isCenterImgShow && playBitmap != null) {
            // 居中绘制
            canvas.drawBitmap(playBitmap, getMeasuredWidth() / 2 - playBitmap.getWidth() / 2, getMeasuredHeight() / 2 - playBitmap.getHeight() / 2, paint);
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
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
