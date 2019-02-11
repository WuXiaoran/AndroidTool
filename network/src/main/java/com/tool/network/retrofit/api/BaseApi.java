package com.tool.network.retrofit.api;

import android.text.TextUtils;

import com.tool.network.retrofit.ToolRetrofit;
import com.tool.network.retrofit.exception.HttpTimeException;
import com.tool.network.retrofit.listener.HttpOnNextListener;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.lang.ref.SoftReference;

import retrofit2.Retrofit;
import rx.Observable;
import rx.functions.Func1;

/**
 * @作者          吴孝然
 * @创建日期      2019/1/24 17:28
 * @描述          请求数据统一封装类
 **/
public abstract class BaseApi<T> implements Func1<BaseResultEntity<T>, T> {
    // rx生命周期管理
    protected SoftReference<RxAppCompatActivity> rxAppCompatActivity;
    // 回调
    protected SoftReference<HttpOnNextListener> listener;
    // 是否能取消加载框
    protected boolean cancel;
    // 是否显示加载框
    protected boolean showProgress;
    // 是否需要缓存处理
    protected boolean cache;
    // 基础url
    protected String baseUrl;
    // 方法-如果需要缓存必须设置这个参数；不需要不用設置
    protected String method = "";
    // 超时时间-默认6秒
    protected int connectionTime = 6;
    // 有网情况下的本地缓存时间默认60秒
    protected int cookieNetWorkTime = 60;
    // 无网络的情况下本地缓存时间默认30天
    protected int cookieNoNetWorkTime = 24 * 60 * 60 * 30;
    // 失败后retry次数
    protected int retryCount = 1;
    // 失败后retry延迟
    protected long retryDelay = 100;
    // 失败后retry叠加延迟
    protected long retryIncreaseDelay = 10;
    // 缓存url-可手动设置
    protected String cacheUrl;

    public BaseApi(RxAppCompatActivity rxAppCompatActivity,HttpOnNextListener listener) {
        setListener(listener);
        setRxAppCompatActivity(rxAppCompatActivity);
        setShowProgress(true);
        setCache(false);
    }

    /**
     * 设置参数
     * @param retrofit      通过retrofit对象去获得接口类实现
     * @return
     */
    public abstract Observable getObservable(Retrofit retrofit);

    public int getCookieNoNetWorkTime() {
        return cookieNoNetWorkTime;
    }

    public BaseApi setCookieNoNetWorkTime(int cookieNoNetWorkTime) {
        this.cookieNoNetWorkTime = cookieNoNetWorkTime;
        return this;
    }

    public int getCookieNetWorkTime() {
        return cookieNetWorkTime;
    }

    public BaseApi setCookieNetWorkTime(int cookieNetWorkTime) {
        this.cookieNetWorkTime = cookieNetWorkTime;
        return this;
    }

    public int getConnectionTime() {
        return connectionTime;
    }

    public BaseApi setConnectionTime(int connectionTime) {
        this.connectionTime = connectionTime;
        return this;
    }

    public String getMethod() {
        return method;
    }

    public BaseApi setMethod(String method) {
        this.method = method;
        return this;
    }

    public String getBaseUrl() {
        if (TextUtils.isEmpty(baseUrl)){
            baseUrl = ToolRetrofit.getBaseUrl();
        }
        return baseUrl;
    }

    public BaseApi setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    public RxAppCompatActivity getRxAppCompatActivity() {
        return rxAppCompatActivity.get();
    }

    public BaseApi setRxAppCompatActivity(RxAppCompatActivity rxAppCompatActivity) {
        this.rxAppCompatActivity = new SoftReference(rxAppCompatActivity);
        return this;
    }

    public boolean isCache() {
        return cache;
    }

    public BaseApi setCache(boolean cache) {
        this.cache = cache;
        return this;
    }

    public boolean isShowProgress() {
        return showProgress;
    }

    public BaseApi setShowProgress(boolean showProgress) {
        this.showProgress = showProgress;
        return this;
    }

    public boolean isCancel() {
        return cancel;
    }

    public BaseApi setCancel(boolean cancel) {
        this.cancel = cancel;
        return this;
    }

    public SoftReference<HttpOnNextListener> getListener() {
        return listener;
    }

    public BaseApi setListener(HttpOnNextListener listener) {
        this.listener = new SoftReference(listener);
        return this;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public BaseApi setRetryCount(int retryCount) {
        this.retryCount = retryCount;
        return this;
    }

    public long getRetryDelay() {
        return retryDelay;
    }

    public BaseApi setRetryDelay(long retryDelay) {
        this.retryDelay = retryDelay;
        return this;
    }

    public long getRetryIncreaseDelay() {
        return retryIncreaseDelay;
    }

    public BaseApi setRetryIncreaseDelay(long retryIncreaseDelay) {
        this.retryIncreaseDelay = retryIncreaseDelay;
        return this;
    }
    public String getCacheUrl() {
        return cacheUrl;
    }

    public BaseApi setCacheUrl(String cacheUrl) {
        this.cacheUrl = cacheUrl;
        return this;
    }

    /**
     * 给缓存设置key
     * @return      返回请求的url 不包含请求参数
     */
    public String getUrl() {
        /* 在没有手动设置url情况下，简单拼接 */
        if (null == getCacheUrl() || "".equals(getCacheUrl())) {
            return getBaseUrl() + getMethod();
        }
        return getCacheUrl();
    }

    @Override
    public T call(BaseResultEntity<T> httpResult) {
        if (httpResult == null) return null;
        if (httpResult.getCode() == 0) {
            throw new HttpTimeException(httpResult.getMessage());
        }
        return httpResult.getResult();
    }
}
