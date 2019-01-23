package com.tool.picture.components.photoviewer;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.tool.picture.R;
import com.tool.picture.components.photoviewer.photoview.OnViewDragListener;
import com.tool.picture.components.photoviewer.photoview.PhotoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @作者          吴孝然
 * @创建日期      2019/1/18 15:51
 * @描述          查看大图的fragment  默认100页缓存
 **/
public class PhotoViewerFragment extends ToolLazyFragment {

    public OnExitListener exitListener = null;
    public OnLongClickListener longClickListener = null;

    private int[] mImgSize = new int[2];
    private int[] mExitLocation = new int[2];
    private Boolean mInAnim = true;
    private String mPicData = "";

    private PhotoView mIv;
    private ProgressBar loading;
    private FrameLayout root;

    /**
     * 每次选中图片后设置图片信息
     */
    public void setData(int[] imgSize,int[] exitLocation,String picData,Boolean inAnim) {
        mImgSize = imgSize;
        mExitLocation = exitLocation;
        mInAnim = inAnim;
        mPicData = picData;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tool_adapter_photoviewer, container, false);
        mIv = view.findViewById(R.id.mIv);
        loading = view.findViewById(R.id.loading);
        root = view.findViewById(R.id.root);
        return view;
    }

    interface OnExitListener {
        void exit();
    }

    @Override
    void onLazyLoad() {
        if (PhotoViewer.mInterface != null) {
            PhotoViewer.mInterface.show(mIv, mPicData);
        } else {
            throw new RuntimeException("请设置图片加载回调 ShowImageViewInterface");
        }

        final float[] alpha = {1f};  // 透明度
        mIv.setExitLocation(mExitLocation);
        mIv.setImgSize(mImgSize);
        mIv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (longClickListener != null) {
                    longClickListener.onLongClick(view);
                }
                return true;
            }
        });

        // 循环查看是否添加上了图片
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (mIv.getDrawable() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                loading.setVisibility(View.GONE);
                            }
                        });
                        break;
                    }
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        final int[] intAlpha = {255};
        root.getBackground().setAlpha(intAlpha[0]);
        mIv.setRootView(root);
        mIv.setOnViewFingerUpListener(new PhotoView.OnViewFingerUpL() {
            @Override
            public void up() {
                alpha[0] = 1f;
                intAlpha[0] = 255;
            }
        });

        // 注册退出Activity 滑动大于一定距离后退出
        mIv.setExitListener(new PhotoView.OnExitListener() {
            @Override
            public void exit() {
                if (exitListener != null) {
                    exitListener.exit();
                }
            }
        });

        // 添加点击进入时的动画
        if (mInAnim)
            mIv.post(new Runnable() {
                @Override
                public void run() {
                    ObjectAnimator scaleOa = ObjectAnimator.ofFloat(mIv, "scale", mImgSize[0] / mIv.getWidth(), 1f);
                    ObjectAnimator xOa = ObjectAnimator.ofFloat(mIv, "translationX", mExitLocation[0] - mIv.getWidth() / 2, 0f);
                    ObjectAnimator yOa = ObjectAnimator.ofFloat(mIv, "translationY", mExitLocation[1] - mIv.getHeight() / 2, 0f);

                    AnimatorSet set = new AnimatorSet();
                    set.setDuration(250);
                    set.playTogether(scaleOa, xOa, yOa);
                    set.start();
                }
            });

        root.setFocusableInTouchMode(true);
        root.requestFocus();
        root.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_BACK) {
                    mIv.exit();
                    return true;
                }
                return false;
            }
        });

        mIv.setOnViewDragListener(new OnViewDragListener() {
            @Override
            public void onDrag(float dx, float dy) {
                mIv.scrollBy((int)(-dx), (int)(-dy));  // 移动图像
                alpha[0] -= dy * 0.001f;
                intAlpha[0] -= (dy * 0.5);
                if (alpha[0] > 1) alpha[0] = 1f;
                else if (alpha[0] < 0) alpha[0] = 0f;
                if (intAlpha[0] < 0) intAlpha[0] = 0;
                else if (intAlpha[0] > 255) intAlpha[0] = 255;
                root.getBackground().setAlpha(intAlpha[0]);   // 更改透明度

                if (alpha[0] >= 0.6)
                    mIv.getAttacher().setScale(alpha[0]);   // 更改大小
            }
        });

        mIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIv.exit();
            }
        });

    }
}
