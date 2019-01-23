package com.tool.picture.components.upload;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.luck.picture.lib.entity.LocalMedia;
import com.tool.picture.R;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

/**
 * @作者          吴孝然
 * @创建日期      2019/1/21 14:09
 * @描述          查看大图
 **/
public class PlusImageActivity extends Activity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    private ViewPager viewPager; // 展示图片的ViewPager
    private TextView positionTv; // 图片的位置，第几张图片
    private Toolbar toolbar; // toolbar
    private TextView positionTvGone; // 禁止删除时候的图片位置
    private ImageView backIvGone; // 禁止删除时候的返回按钮
    private ArrayList<LocalMedia> imgList; // 图片的数据源
    private int mPosition; // 第几张图片
    private PlusImagePagerAdapter mAdapter;
    private boolean isShowToolBar;// 是否显示toolbar,默认显示

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tool_activity_plus_image);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(getResources().getColor(R.color.black));
        }
        initData();
    }

    public void initData() {
        Bundle baseBundle = getIntent().getExtras();
        imgList = (ArrayList<LocalMedia>) baseBundle.getSerializable(UploadConfig.IMG_LIST);
        mPosition = baseBundle.getInt(UploadConfig.POSITION, 0);
        isShowToolBar = baseBundle.getBoolean(UploadConfig.ISSHOWTOOLBAR,true);
        initView();
    }

    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.vp);
        positionTv = (TextView) findViewById(R.id.tv_position);
        toolbar = (Toolbar) findViewById(R.id.tb);
        positionTvGone = (TextView) findViewById(R.id.tv_position_gone);
        backIvGone = (ImageView) findViewById(R.id.iv_back_gone);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.iv_delete).setOnClickListener(this);
        backIvGone.setOnClickListener(this);
        viewPager.addOnPageChangeListener(this);

        if (isShowToolBar){
            positionTv.setText(mPosition + 1 + "/" + imgList.size());
        }else{
            toolbar.setVisibility(View.GONE);
            positionTvGone.setVisibility(View.VISIBLE);
            backIvGone.setVisibility(View.VISIBLE);
            positionTvGone.setText(mPosition + 1 + "/" + imgList.size());
        }

        mAdapter = new PlusImagePagerAdapter(this, imgList);
        viewPager.setAdapter(mAdapter);

        viewPager.setCurrentItem(mPosition);
    }

    //删除图片
    private void deletePic() {
        CancelOrOkDialog dialog = new CancelOrOkDialog(this, "要删除这张图片吗?") {
            @Override
            public void ok() {
                super.ok();
                imgList.remove(mPosition); //从数据源移除删除的图片
                if (imgList.size() == 0){
                    dismiss();
                    back();
                }else{
                    setPosition();
                    dismiss();
                }
            }
        };
        dialog.show();
    }

    //设置当前位置
    private void setPosition() {
        positionTv.setText(mPosition + 1 + "/" + imgList.size());
        positionTvGone.setText(mPosition + 1 + "/" + imgList.size());
        viewPager.setCurrentItem(mPosition);
        mAdapter.notifyDataSetChanged();
    }

    //返回上一个页面
    private void back() {
        Intent intent = getIntent();
        Bundle bundle = new Bundle();
        bundle.putSerializable(UploadConfig.IMG_LIST, imgList);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        mPosition = position;
        positionTv.setText(position + 1 + "/" + imgList.size());
        positionTvGone.setText(mPosition + 1 + "/" + imgList.size());
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_back){
            back();
        }
        if (v.getId() == R.id.iv_delete){
            deletePic();
        }
        if (v.getId() == R.id.iv_back_gone){
            back();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //按下了返回键
            back();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
