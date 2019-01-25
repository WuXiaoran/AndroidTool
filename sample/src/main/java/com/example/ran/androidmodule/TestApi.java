package com.example.ran.androidmodule;

import com.tool.network.retrofit.api.BaseApi;
import com.tool.network.retrofit.api.SimpleApi;
import com.tool.network.retrofit.listener.HttpOnNextListener;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.List;

import retrofit2.Retrofit;
import rx.Observable;

public class TestApi {
    public BaseApi getAllVedioBys(final boolean once_no, RxAppCompatActivity rxAppCompatActivity, HttpOnNextListener listener){
        return new SimpleApi<List<SubjectResulte>>(rxAppCompatActivity,listener){
            @Override
            public Observable getObservable(Retrofit retrofit) {
                return retrofit.create(HttpPostService.class).getAllVedioBy(once_no);
            }
        };
    }
}
