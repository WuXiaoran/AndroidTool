package com.tool.picture.components.upload;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.luck.picture.lib.entity.LocalMedia;
import com.tool.picture.R;
import com.tool.picture.components.photoviewer.photoview.PhotoView;
import com.tool.picture.glide.ToolGlide;

import java.util.List;

import androidx.viewpager.widget.PagerAdapter;


/**
 * @作者          吴孝然
 * @创建日期      2019/1/21 17:53
 * @描述          查看大图的pager适配器
 **/
public class PlusImagePagerAdapter extends PagerAdapter {

    private Context context;
    private List<LocalMedia> imgList; //图片的数据源

    public PlusImagePagerAdapter(Context context, List<LocalMedia> imgList) {
        this.context = context;
        this.imgList = imgList;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return imgList.size();
    }

    //判断当前的View 和 我们想要的Object(值为View) 是否一样;返回 true/false
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }

    //instantiateItem()：将当前view添加到ViewGroup中，并返回当前View
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = getItemView(R.layout.tool_adapter_upload_img);
        PhotoView imageView = itemView.findViewById(R.id.iv_img);
        String imgPath = "";
        if (imgList.get(position).getPictureType().contains("image")){
            //被压缩后的图片路径
            if (imgList.get(position).isCompressed()) {
                imgPath = imgList.get(position).getCompressPath(); // 压缩后的图片路径
            }
        }else{
            imgPath = imgList.get(position).getPath(); // 音视频的缩略图路径
        }
        ToolGlide.loadImage(context,imgPath,imageView);
        container.addView(itemView);
        return itemView;
    }

    //destroyItem()：删除当前的View;  
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    private View getItemView(int layoutId) {
        View itemView = LayoutInflater.from(this.context).inflate(layoutId, null, false);
        return itemView;
    }
}
