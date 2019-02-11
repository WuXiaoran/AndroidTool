package com.tool.network.retrofit.api;

import com.tool.network.retrofit.listener.HttpOnNextListener;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import retrofit2.Retrofit;
import rx.Observable;

/**
 * @作者          吴孝然
 * @创建日期      2019/1/24 17:19
 * @描述          简单使用的Api
 **/
public abstract class SimpleApi<T> extends BaseApi<T> {
    public SimpleApi(RxAppCompatActivity rxAppCompatActivity, HttpOnNextListener listener) {
        super(rxAppCompatActivity, listener);
    }

    @Override
    public Observable getObservable(Retrofit retrofit) {
        return null;
    }
}
