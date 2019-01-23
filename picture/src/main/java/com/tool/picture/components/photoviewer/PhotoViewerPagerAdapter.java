package com.tool.picture.components.photoviewer;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

/**
 * @作者          吴孝然
 * @创建日期      2019/1/18 14:12
 * @描述          大图viewpager适配器
 **/
public class PhotoViewerPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<PhotoViewerFragment> mData;
    private FragmentManager fragmentManager;

    public PhotoViewerPagerAdapter(ArrayList<PhotoViewerFragment> mData,@NonNull FragmentManager fm) {
        super(fm);
        this.mData = mData;
        this.fragmentManager = fm;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mData.get(position);
    }

    @Override
    public int getCount() {
        return mData.size();
    }
}
