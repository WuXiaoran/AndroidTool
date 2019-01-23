package com.tool.picture.components.photoviewer;

import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tool.picture.R;
import com.tool.picture.utils.Util;
import com.tool.picture.components.progressimg.ProgressImageView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

/**
 * @作者          吴孝然
 * @创建日期      2019/1/18 15:51
 * @描述          查看大图
 **/
public class PhotoViewer {

    private final String INDICATOR_TYPE_DOT = "INDICATOR_TYPE_DOT";
    private final String INDICATOR_TYPE_TEXT = "INDICATOR_TYPE_TEXT";

    public  static ShowImageViewInterface mInterface = null;
    private OnPhotoViewerCreatedListener mCreatedInterface = null;
    private OnPhotoViewerDestroyListener mDestroyInterface = null;

    private ArrayList<String> imgData;  // 图片数据
    private WeakReference<ViewGroup> container;   // 存放图片的容器， ListView/GridView/RecyclerView
    private int currentPage = 0;    // 当前页

    private WeakReference<View> clickView = null; //点击那一张图片时候的view
    private OnLongClickListener longClickListener = null;

    private String indicatorType = INDICATOR_TYPE_DOT;   // 默认type为小圆点

    interface OnPhotoViewerCreatedListener {
        void onCreated();
    }


    interface OnPhotoViewerDestroyListener {
        void onDestroy();
    }

    public PhotoViewer setOnPhotoViewerCreatedListener(OnPhotoViewerCreatedListener mCreatedInterface) {
        this.mCreatedInterface = mCreatedInterface;
        return this;
    }

    public PhotoViewer setOnPhotoViewerDestroyListener(OnPhotoViewerDestroyListener mDestroyInterface) {
        this.mDestroyInterface = mDestroyInterface;
        return this;
    }

    /**
     * 小圆点的drawable
     * 下标0的为没有被选中的
     * 下标1的为已经被选中的
     */
    private int[] mDot = new int[]{R.drawable.tool_no_selected_dot, R.drawable.tool_selected_dot};


    public interface ShowImageViewInterface {
        void show(ProgressImageView iv, String url);
    }

    /**
     * 设置显示ImageView的接口
     */
    public PhotoViewer setShowImageViewInterface(ShowImageViewInterface i) {
        mInterface = i;
        return this;
    }

    /**
     * 设置点击一个图片
     */
    public PhotoViewer setClickSingleImg(String data, View view) {
        imgData = new ArrayList<String>();
        imgData.add(data);
        clickView = new  WeakReference(view);
        return this;
    }

    /**
     * 设置图片数据
     */
    public PhotoViewer setData(ArrayList<String> data) {
        imgData = data;
        return this;
    }


    public PhotoViewer setImgContainer(AbsListView container) {
        this.container = new WeakReference(container);
        return this;
    }

    public PhotoViewer setImgContainer(RecyclerView container) {
        this.container = new WeakReference(container);
        return this;
    }

    public PhotoViewer setImgContainer(View container) {
        this.container = new WeakReference(container);
        return this;
    }

    /**
     * 获取itemView
     */
    private View getItemView() {
        if (clickView == null) {
            View itemView = null;
            if (container.get() instanceof AbsListView) {
                AbsListView absListView = (AbsListView) container.get();
                itemView = absListView.getChildAt(currentPage - absListView.getFirstVisiblePosition());
            } else if (container.get() instanceof RecyclerView){
                itemView = ((RecyclerView)container.get()).getLayoutManager().findViewByPosition(currentPage);
            }else{
                itemView = container.get();
                return itemView;
            }
            View result = null;
            if (itemView instanceof ViewGroup) {
                for (int i = 0; i < ((ViewGroup) itemView).getChildCount(); i++) {
                    if (((ViewGroup) itemView).getChildAt(i) instanceof ImageView) {
                        result = (ImageView)((ViewGroup) itemView).getChildAt(i);
                        break;
                    }
                }
            } else {
                result = (ImageView)itemView;
            }
            return result;
        } else {
            return clickView.get();
        }
    }

    /**
     * 获取现在查看到的图片的原始位置 (中间)
     */
    private int[] getCurrentViewLocation() {
        int[] result = new int[2];
        getItemView().getLocationInWindow(result);
        result[0] += getItemView().getMeasuredWidth() / 2;
        result[1] += getItemView().getMeasuredHeight() / 2;
        return result;
    }


    /**
     * 设置当前页， 从0开始
     */
    public PhotoViewer setCurrentPage(int page) {
        currentPage = page;
        return this;
    }

    public void start(Fragment fragment) {
        AppCompatActivity activity = (AppCompatActivity) fragment.getActivity();
        start(activity);
    }


    public void start(android.app.Fragment fragment) {
        AppCompatActivity activity = (AppCompatActivity) fragment.getActivity();
        start(activity);
    }


    public void start(AppCompatActivity activity) {
        show(activity);
    }

    public PhotoViewer setOnLongClickListener(OnLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
        return this;
    }

    /**
     * 设置指示器的样式，但是如果图片大于9张，则默认设置为文字样式
     */
    public PhotoViewer setIndicatorType(String type) {
        this.indicatorType = type;
        return this;
    }

    private void show(final AppCompatActivity activity) {

        final ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();

        // 设置添加layout的动画
        LayoutTransition layoutTransition = new LayoutTransition();
        ObjectAnimator alphaOa = ObjectAnimator.ofFloat(null, "alpha", 0f, 1f);
        alphaOa.setDuration(50);
        layoutTransition.setAnimator(LayoutTransition.APPEARING, alphaOa);
        decorView.setLayoutTransition(layoutTransition);

        final FrameLayout frameLayout = new FrameLayout(activity);

        View photoViewLayout = LayoutInflater.from(activity).inflate(R.layout.tool_activity_photoviewer, null);
        ViewPagerFixed viewPager = ((View) photoViewLayout).findViewById(R.id.mLookPicVP);

        final ArrayList<PhotoViewerFragment> fragments = new ArrayList<PhotoViewerFragment>();
        /**
         * 存放小圆点的Group
         */
        final LinearLayout[] mDotGroup = {null};

        /**
         * 存放没有被选中的小圆点Group和已经被选中小圆点
         * 或者存放数字
         */
        final FrameLayout[] mFrameLayout = {null};
        /**
         * 选中的小圆点
         */
        final View[] mSelectedDot = {null};

        /**
         * 文字版本当前页
         */
        final TextView[] tv = {null};

        for (int i = 0; i < imgData.size(); i++) {
            PhotoViewerFragment f = new PhotoViewerFragment();
            f.exitListener = new PhotoViewerFragment.OnExitListener() {
                @Override
                public void exit() {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mDotGroup[0] != null)
                                mDotGroup[0].removeAllViews();
                            frameLayout.removeAllViews();
                            decorView.removeView(frameLayout);
                            fragments.clear();

                            if (mDestroyInterface != null) {
                                mDestroyInterface.onDestroy();
                            }
                        }
                    });
                }
            };
            f.setData(new int[]{getItemView().getMeasuredWidth(), getItemView().getMeasuredHeight()}, getCurrentViewLocation(), imgData.get(i), true);
            f.longClickListener = longClickListener;
            fragments.add(f);
        }

        PhotoViewerPagerAdapter adapter = new PhotoViewerPagerAdapter(fragments, activity.getSupportFragmentManager());

        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(currentPage);
        viewPager.setOffscreenPageLimit(100);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (mSelectedDot[0] != null && imgData.size() > 1) {
                    float dx = mDotGroup[0].getChildAt(1).getX() - mDotGroup[0].getChildAt(0).getX();
                    mSelectedDot[0].setTranslationX((position * dx) + positionOffset * dx);
                }
            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
                /**
                 * 解决RecyclerView获取不到itemView的问题
                 * 如果滑到的view不在当前页面显示，那么则滑动到那个position，再获取itemView
                 */
                if (container.get() instanceof RecyclerView) {
                    RecyclerView.LayoutManager layoutManager = ((RecyclerView)container.get()).getLayoutManager();
                    if (layoutManager instanceof LinearLayoutManager) {
                        if (currentPage < ((LinearLayoutManager)layoutManager).findFirstVisibleItemPosition() || currentPage > ((LinearLayoutManager)layoutManager).findLastVisibleItemPosition()) {
                            layoutManager.scrollToPosition(currentPage);
                        }
                    } else if (layoutManager instanceof GridLayoutManager) {
                        if (currentPage < ((GridLayoutManager)layoutManager).findFirstVisibleItemPosition() || currentPage > ((GridLayoutManager)layoutManager).findLastVisibleItemPosition()) {
                            layoutManager.scrollToPosition(currentPage);
                        }
                    }
                }

                /**
                 * 设置文字版本当前页的值
                 */
                if (tv[0] != null) {
                    String page = currentPage + 1 + "/" +  imgData.size();
                    tv[0].setText(page);
                }

                // 这里延时0.2s是为了解决上面👆的问题。因为如果刚调用ScrollToPosition方法，就获取itemView是获取不到的，所以要延时一下
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        fragments.get(currentPage).setData(new int[]{getItemView().getMeasuredWidth(), getItemView().getMeasuredHeight()}, getCurrentViewLocation(), imgData.get(currentPage), false);
                    }
                }, 200);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        frameLayout.addView(photoViewLayout);

        frameLayout.post(new Runnable() {
            @Override
            public void run() {
                mFrameLayout[0] = new FrameLayout(activity);
                if (1 <= imgData.size() && imgData.size() <= 9 && indicatorType == INDICATOR_TYPE_DOT) {

                    /**
                     * 实例化两个Group
                     */
                    if (mFrameLayout[0] != null) {
                        mFrameLayout[0].removeAllViews();
                    }
                    if (mDotGroup[0] != null) {
                        mDotGroup[0].removeAllViews();
                        mDotGroup[0] = null;
                    }
                    mDotGroup[0] = new LinearLayout(activity);

                    if (mDotGroup[0].getChildCount() != 0)
                        mDotGroup[0].removeAllViews();
                    final LinearLayout.LayoutParams dotParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                    /**
                     * 未选中小圆点的间距
                     */
                    dotParams.leftMargin = Util.dp2px(activity, 6);
                    dotParams.rightMargin = Util.dp2px(activity, 6);

                    /**
                     * 创建未选中的小圆点
                     */
                    for (int i = 0; i < imgData.size(); i++) {
                        ImageView iv = new ImageView(activity);
                        iv.setImageDrawable(activity.getResources().getDrawable(mDot[0]));
                        iv.setLayoutParams(dotParams);
                        mDotGroup[0].addView(iv);
                    }

                    /**
                     * 设置小圆点Group的方向为水平
                     */
                    mDotGroup[0].setOrientation(LinearLayout.HORIZONTAL);
                    /**
                     * 设置小圆点在中间
                     */
                    mDotGroup[0].setGravity(Gravity.CENTER | Gravity.BOTTOM);
                    /**
                     * 两个Group的大小都为match_parent
                     */
                    final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT);


                    params.bottomMargin = Util.dp2px(activity, 70);
                    /**
                     * 首先添加小圆点的Group
                     */
                    frameLayout.addView(mDotGroup[0], params);

                    mDotGroup[0].post(new Runnable() {
                        @Override
                        public void run() {
                            if (mSelectedDot[0] != null) {
                                mSelectedDot[0] = null;
                            }
                            if (mSelectedDot[0] == null) {
                                ImageView iv = new ImageView(activity);
                                iv.setImageDrawable(activity.getResources().getDrawable(mDot[1]));
                                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                /**
                                 * 设置选中小圆点的左边距
                                 */
                                params.leftMargin = (int) mDotGroup[0].getChildAt(0).getX();
                                iv.setTranslationX(dotParams.rightMargin * currentPage * 2 + mDotGroup[0].getChildAt(0).getWidth() * currentPage);
                                params.gravity = Gravity.BOTTOM;
                                mFrameLayout[0].addView(iv, params);
                                mSelectedDot[0] = iv;
                            }
                            /**
                             * 然后添加包含未选中圆点和选中圆点的Group
                             */
                            frameLayout.addView(mFrameLayout[0], params);
                        }
                    });
                } else {
                    tv[0] = new TextView(activity);
                    String page = currentPage + 1 + "/" +  imgData.size();
                    tv[0].setText(page);
                    tv[0].setTextColor(Color.WHITE);
                    tv[0].setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
                    tv[0].setTextSize(18f);
                    mFrameLayout[0].addView(tv[0]);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT);
                    params.bottomMargin = Util.dp2px(activity, 80);
                    frameLayout.addView(mFrameLayout[0], params);

                }
            }
        });
        decorView.addView(frameLayout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        if (mCreatedInterface != null) {
            mCreatedInterface.onCreated();
        }
    }
}
