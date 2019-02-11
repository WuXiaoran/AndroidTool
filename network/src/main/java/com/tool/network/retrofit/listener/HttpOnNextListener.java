package com.tool.network.retrofit.listener;

import rx.Observable;

/**
 * @作者          吴孝然
 * @创建日期      2019/2/11 10:12
 * @描述          成功回调处理
 **/
public abstract class HttpOnNextListener<T> {
    /**
     * 成功后回调方法
     * @param t
     */
    public abstract void onNext(T t);

    /**
     * 緩存回調結果
     * @param t
     */
    public void onCacheNext(T t){

    }

    /**
     * 成功后的ober返回，扩展链接式调用
     * @param observable
     */
    public void onNext(Observable observable){

    }

    /**
     * 失败或者错误方法
     * 主动调用，更加灵活
     * @param e
     */
    public  void onError(Throwable e){

    }

    /**
     * 取消回調
     */
    public void onCancel(){

    }

    /**
     * 上传进度
     * @param currentBytesCount
     * @param totalBytesCount
     */
    public void onProgress(long currentBytesCount, long totalBytesCount){

    }


}
