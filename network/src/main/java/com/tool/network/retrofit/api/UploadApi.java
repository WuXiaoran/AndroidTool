package com.tool.network.retrofit.api;

import com.tool.network.retrofit.listener.HttpOnNextListener;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

public abstract class UploadApi<T> extends BaseApi<T> {
    public UploadApi(RxAppCompatActivity rxAppCompatActivity, HttpOnNextListener listener) {
        super(rxAppCompatActivity, listener);
    }
}
