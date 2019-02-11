package com.example.ran.androidmodule.retrofit;

import com.tool.network.retrofit.ContentType;
import com.tool.network.retrofit.api.BaseApi;
import com.tool.network.retrofit.api.SimpleApi;
import com.tool.network.retrofit.listener.HttpOnNextListener;
import com.tool.network.retrofit.utils.Util;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.io.File;
import java.util.List;

import retrofit2.Retrofit;
import rx.Observable;

public class TestApi {
    public BaseApi getSong(final int page, RxAppCompatActivity rxAppCompatActivity, HttpOnNextListener listener){
        return new SimpleApi<List<RetrofitEntity.ResultBean>>(rxAppCompatActivity,listener){
            @Override
            public Observable getObservable(Retrofit retrofit) {
                return retrofit.create(HttpPostService.class).getSong(page);
            }
        };
    }

    public BaseApi<UploadResulte> uploadImage(final String url,final File file, RxAppCompatActivity rxAppCompatActivity, HttpOnNextListener listener){
        return new SimpleApi<UploadResulte>(rxAppCompatActivity,listener){
            @Override
            public Observable getObservable(Retrofit retrofit) {
                return retrofit.create(HttpPostService.class).uploadImage(url,Util.createRequestBody(file,ContentType.IMAGE,listener.get()));
            }
        };
    }
}
