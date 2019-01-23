package com.tool.picture.components.progressimg;

import android.graphics.drawable.Drawable;

import com.bumptech.glide.request.transition.Transition;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @作者          吴孝然
 * @创建日期      2019/1/18 10:21
 * @描述          加載進度
 **/
public interface OnProgressListener {
    void onProgress(boolean isComplete, int percentage, long bytesRead, long totalBytes);
    void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition);
}
