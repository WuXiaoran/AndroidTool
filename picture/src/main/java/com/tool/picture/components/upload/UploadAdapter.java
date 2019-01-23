package com.tool.picture.components.upload;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.luck.picture.lib.entity.LocalMedia;
import com.tool.picture.R;
import com.tool.picture.glide.ToolGlide;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @作者          吴孝然
 * @创建日期      2019/1/21 11:09
 * @描述          上传组件适配器
 **/
public class UploadAdapter extends RecyclerView.Adapter<UploadAdapter.ViewHolder> {

    private Context context; // 上下文
    private List<LocalMedia> mList; // 数据
    private int listMax; // 最多数量 一般为9个
    private int col; // 行最多数量 一般为3个
    private OnItemClickListener onItemClickListener; // 点击回调
    private boolean isPhotoAndVideo = false; // 是否可存在图片和视频 默认为不行

    public interface OnItemClickListener{
        void onItemDelete(View v,int position);
        void onItemClick(View v,int position);
    }

    public UploadAdapter(Context context, List<LocalMedia> mList, int listMax, int col,OnItemClickListener onItemClickListener) {
        this.context = context;
        this.mList = mList;
        this.listMax = listMax;
        this.col = col;
        this.onItemClickListener = onItemClickListener;
    }

//    暂不提供图片与视频共存功能
//    public void setPhotoAndVideo(boolean photoAndVideo) {
//        isPhotoAndVideo = photoAndVideo;
//    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.tool_adapter_upload,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        String imgPath = "";
        holder.iv_play.setVisibility(View.GONE);
        holder.itemView.setVisibility(View.VISIBLE);
        if (position < mList.size()) {
            // 代表+号之前的需要正常显示图片
            if (mList.get(position).getPictureType().contains("image")){
                //被压缩后的图片路径
                if (mList.get(position).isCompressed()) {
                    imgPath = mList.get(position).getCompressPath(); // 压缩后的图片路径
                }
            }else{
                imgPath = mList.get(position).getPath(); //视频的缩略图路径
                holder.iv_play.setVisibility(View.VISIBLE);
            }
            ToolGlide.loadImage(context,imgPath,holder.iv_picture);
            holder.iv_delete.setVisibility(View.VISIBLE);
        } else {
            if (mList.size() > 0){
                if (!isPhotoAndVideo){
                    if (!mList.get(0).getPictureType().contains("image")){
                        // 视频只允许一个
                        holder.itemView.setVisibility(View.GONE);
                    }
                }
            }
            holder.iv_picture.setImageResource(R.mipmap.tool_img_upload);// 最后一个显示加号图片
            holder.ll_current.setVisibility(View.GONE);
            holder.tv_current.setText(position + "");
            holder.tv_max.setText(listMax + "");
            holder.iv_delete.setVisibility(View.GONE);
        }
        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null){
                    onItemClickListener.onItemDelete(view,position);
                }
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null){
                    onItemClickListener.onItemClick(view,position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        int count = mList == null ? 1 : mList.size() + 1;
        if (count > listMax) {
            return mList.size();
        } else {
            return count;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView iv_picture;
        private LinearLayout ll_current;
        private TextView tv_current;
        private TextView tv_max;
        private ImageView iv_delete;
        private ImageView iv_play;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_picture = itemView.findViewById(R.id.iv_picture);
            ll_current = itemView.findViewById(R.id.ll_current);
            tv_current = itemView.findViewById(R.id.tv_current);
            tv_max = itemView.findViewById(R.id.tv_max);
            iv_delete = itemView.findViewById(R.id.iv_delete);
            iv_play = itemView.findViewById(R.id.iv_play);
        }
    }
}
