package com.tool.picture.components.upload;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.luck.picture.lib.entity.LocalMedia;
import com.tool.picture.R;
import com.tool.picture.utils.PictureSelectorUtil;

import java.util.ArrayList;

/**
 * @作者          吴孝然
 * @创建日期      2019/1/21 14:56
 * @描述          添加选择拍照，相册，录像，视频
 **/
public class SelectDialog extends Dialog {

    //// 0 图片 1 视频 2 全部
    public static final int PICTURE = 0;
    public static final int VIDEO = 1;
    public static final int ALL = 2;

    public SelectDialog(final Context context, int type, final ArrayList<LocalMedia> mPicList, final int max) {
        super(context, R.style.tool_upload_dialog);
        setContentView(R.layout.tool_dialog_select);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.gravity = Gravity.BOTTOM;
        lp.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        lp.width = LinearLayout.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(lp);
        TextView tvCancel = findViewById(R.id.tv_cancel);
        TextView tvTakePhoto = findViewById( R.id.tv_take_photo);
        View v1 = findViewById(R.id.v_1);
        TextView tvGoAlbum = findViewById(R.id.tv_go_album);
        View v2 = findViewById(R.id.v_2);
        TextView tvVideotape = findViewById(R.id.tv_videotape);
        View v3 = findViewById(R.id.v_3);
        TextView tvVideo = findViewById(R.id.tv_video);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        if (type == 0){
            v2.setVisibility(View.GONE);
            tvVideotape.setVisibility(View.GONE);
            v3.setVisibility(View.GONE);
            tvVideo.setVisibility(View.GONE);
        }else if (type == 1){
            v1.setVisibility(View.GONE);
            tvTakePhoto.setVisibility(View.GONE);
            v2.setVisibility(View.GONE);
            tvGoAlbum.setVisibility(View.GONE);
        }

        tvTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PictureSelectorUtil.openCamera((Activity) context, false, 0);
                dismiss();
            }
        });
        tvGoAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PictureSelectorUtil.openGallery((Activity) context, false, 0, mPicList, max);
                dismiss();
            }
        });
        tvVideotape.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PictureSelectorUtil.openCamera((Activity) context, false, 1);
                dismiss();
            }
        });
        tvVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PictureSelectorUtil.openGallery((Activity) context, false, 1, mPicList, max);
                dismiss();
            }
        });
    }
}
