package com.tool.picture.components.upload;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.PictureVideoPlayActivity;
import com.luck.picture.lib.entity.LocalMedia;
import com.tool.picture.components.video.VideoPreview;
import com.tool.picture.components.video.VideoPreviewActivity;
import com.tool.picture.utils.Util;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @作者          吴孝然
 * @创建日期      2019/1/21 11:44
 * @描述          上传组件的配置项
 **/
public class UploadConfig {

    //// 数量0-9 ////
    public static final int SELECT_PIC_NUM_0 = 0;
    public static final int SELECT_PIC_NUM_1 = 1;
    public static final int SELECT_PIC_NUM_2 = 2;
    public static final int SELECT_PIC_NUM_3 = 3;
    public static final int SELECT_PIC_NUM_4 = 4;
    public static final int SELECT_PIC_NUM_5 = 5;
    public static final int SELECT_PIC_NUM_6 = 6;
    public static final int SELECT_PIC_NUM_7 = 7;
    public static final int SELECT_PIC_NUM_8 = 8;
    public static final int SELECT_PIC_NUM_9 = 9;

    public static final String IMG_LIST = "img_list"; // 图片集合
    public static final String POSITION = "position"; // 第几张图片
    public static final String ISSHOWTOOLBAR = "isShow"; // 是否显示toolbar
    public static final int REQUEST_CODE_MAIN = 9999; //请求码

    private UploadAdapter uploadAdapter; // 上传适配器
    private Context context; // 上下文
    private ArrayList<LocalMedia> mPicList = new ArrayList<>(); // 上传的图片凭证的数据源
    private int listMax; // 最多数量 一般为9个
    private int col; // 行最多数量 一般为3个
    private RecyclerView recyclerView; // 宿主RecyclerView

    public UploadConfig(Context context,RecyclerView recyclerView,ArrayList<LocalMedia> mPicList, int listMax, int col) {
        this.context = context;
        this.mPicList = mPicList;
        this.listMax = listMax;
        this.col = col;
        this.recyclerView = recyclerView;
        build();
    }

    /**
     * 构建适配器
     */
    private void build(){
        this.uploadAdapter = new UploadAdapter(context, mPicList, listMax, col, new UploadAdapter.OnItemClickListener() {
            @Override
            public void onItemDelete(View v, int position) {
                mPicList.remove(position);
                uploadAdapter.notifyDataSetChanged();
            }

            @Override
            public void onItemClick(View v, int position) {
                if (position == uploadAdapter.getItemCount() - 1) {
                    if (mPicList.size() == listMax) {
                        preview(position);
                    } else {
                        showSelectDialog();
                    }
                } else {
                    preview(position);
                }
            }
        });
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.bottom = Util.dp2px(context,12);
            }
        });
    }

    /**
     * 预览
     * @param position      当前图片下标
     */
    private void preview(int position) {
        if (mPicList.get(position).getPictureType().contains("image")){
            Bundle bundle = new Bundle();
            bundle.putSerializable(IMG_LIST, mPicList);
            bundle.putInt(POSITION, position);
            Intent intent = new Intent(context,PlusImageActivity.class);
            intent.putExtras(bundle);
            ((Activity)context).startActivityForResult(intent,REQUEST_CODE_MAIN);
        }else{
            VideoPreview.preview(context,mPicList.get(position).getPath());
        }
    }

    /**
     * 显示选择dialog
     */
    private void showSelectDialog() {
        int type = 2;
        if (mPicList.size() > 0){
            if (mPicList.get(0).getPictureType().contains("image")){
                type = 0;
            }else{
                type = 1;
            }
        }
        SelectDialog dialog = new SelectDialog(context, type,mPicList,listMax);
        dialog.show();
    }

    /**
     * 添加后刷新适配器
     * @param data       onActivityResult 返回的数据
     */
    public void refreshAdapter(Intent data) {
        List<LocalMedia> picList = PictureSelector.obtainMultipleResult(data);
        refreshAdapter(picList);
    }

    /**
     * 添加后刷新适配器
     * @param picList       图片或视频列表
     */
    public void refreshAdapter(List<LocalMedia> picList) {
        if (picList.size() > 0) {
            mPicList.clear();
            for (LocalMedia localMedia : picList) {
                //被压缩后的图片路径
                if (localMedia.getPictureType().contains("image")){
                    if (localMedia.isCompressed()) {
                        mPicList.add(localMedia); // 把图片添加到将要上传的图片数组中
                    }
                }else{
                    mPicList.add(localMedia);
                }
            }
            uploadAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 预览中删除
     * @param data      onActivityResult 返回的数据
     */
    public void previewDelete(Intent data){
        List<LocalMedia> toDeletePicList = (List<LocalMedia>) data.getExtras().getSerializable(UploadConfig.IMG_LIST);
        previewDelete(toDeletePicList);
    }

    /**
     * 预览中删除
     * @param picList       要删除的图片的集合
     */
    public void previewDelete(List<LocalMedia> picList){
        mPicList.clear();
        mPicList.addAll(picList);
        uploadAdapter.notifyDataSetChanged();
    }

    public UploadAdapter getUploadAdapter() {
        return uploadAdapter;
    }

    public ArrayList<LocalMedia> getmPicList() {
        return mPicList;
    }
}
