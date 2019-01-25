package com.tool.network.retrofit;

import android.app.Application;
import android.util.Log;

import com.tool.network.retrofit.api.BaseApi;
import com.tool.network.retrofit.exception.RetryWhenNetworkException;
import com.tool.network.retrofit.http.cookie.CookieInterceptor;
import com.tool.network.retrofit.listener.HttpOnNextListener;
import com.tool.network.retrofit.subscribers.ProgressSubscriber;
import com.trello.rxlifecycle.android.ActivityEvent;

import java.lang.ref.SoftReference;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @作者          吴孝然
 * @创建日期      2019/1/24 17:36
 * @描述          全局app
 **/
public class ToolRetrofit {
    private static Application application;
    private static boolean debug; // 是否是调试，方便日志输出
    private static String baseUrl; // 主域名，方便主模块调用

    public static void init(Application app,String baseUrl){
        init(app,baseUrl,true);
    }

    public static void init(Application app,String baseUrl,boolean debug){
        setApplication(app);
        setBaseUrl(baseUrl);
        setDebug(debug);
    }

    /**
     * 处理http请求
     *
     * @param basePar 封装的请求数据
     */
    public static void http(BaseApi basePar) {
        //手动创建一个OkHttpClient并设置超时时间缓存等设置
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(basePar.getConnectionTime(), TimeUnit.SECONDS);
        builder.addInterceptor(new CookieInterceptor(basePar.isCache(), basePar.getUrl()));
        if(ToolRetrofit.isDebug()){
            builder.addInterceptor(getHttpLoggingInterceptor());
        }


        /*创建retrofit对象*/
        Retrofit retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(basePar.getBaseUrl())
                .build();


        /*rx处理*/
        ProgressSubscriber subscriber = new ProgressSubscriber(basePar);
        Observable observable = basePar.getObservable(retrofit)
                /*失败后的retry配置*/
                .retryWhen(new RetryWhenNetworkException(basePar.getRetryCount(),
                        basePar.getRetryDelay(), basePar.getRetryIncreaseDelay()))
                /*生命周期管理*/
//                .compose(basePar.getRxAppCompatActivity().bindToLifecycle())
                .compose(basePar.getRxAppCompatActivity().bindUntilEvent(ActivityEvent.PAUSE))
                /*http请求线程*/
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                /*回调线程*/
                .observeOn(AndroidSchedulers.mainThread())
                /*结果判断*/
                .map(basePar);


        /*链接式对象返回*/
        SoftReference<HttpOnNextListener> httpOnNextListener = basePar.getListener();
        if (httpOnNextListener != null && httpOnNextListener.get() != null) {
            httpOnNextListener.get().onNext(observable);
        }

        /*数据回调*/
        observable.subscribe(subscriber);

    }


    /**
     * 日志输出
     * 自行判定是否添加
     * @return
     */
    private static HttpLoggingInterceptor getHttpLoggingInterceptor(){
        //日志显示级别
        HttpLoggingInterceptor.Level level= HttpLoggingInterceptor.Level.BODY;
        //新建log拦截器
        HttpLoggingInterceptor loggingInterceptor=new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.d("RxRetrofit","Retrofit====Message:"+message);
            }
        });
        loggingInterceptor.setLevel(level);
        return loggingInterceptor;
    }

    public static Application getApplication() {
        return application;
    }

    private static void setApplication(Application application) {
        ToolRetrofit.application = application;
    }

    public static String getBaseUrl() {
        return baseUrl;
    }

    public static void setBaseUrl(String baseUrl) {
        ToolRetrofit.baseUrl = baseUrl;
    }

    public static boolean isDebug() {
        return debug;
    }

    public static void setDebug(boolean debug) {
        ToolRetrofit.debug = debug;
    }
}
