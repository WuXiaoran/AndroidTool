package com.example.ran.androidmodule;

import com.tool.network.retrofit.api.BaseApi;
import com.tool.network.retrofit.listener.HttpOnNextListener;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import retrofit2.Retrofit;
import rx.Observable;

public class SubjectPostApi extends BaseApi{
    //    接口需要传入的参数 可自定义不同类型
    private boolean all;
    /*任何你先要传递的参数*/
//    String xxxxx;
//    String xxxxx;
//    String xxxxx;
//    String xxxxx;


    /**
     * 默认初始化需要给定回调和rx周期类
     * 可以额外设置请求设置加载框显示，回调等（可扩展）
     * @param listener
     * @param rxAppCompatActivity
     */
    public SubjectPostApi(HttpOnNextListener listener, RxAppCompatActivity rxAppCompatActivity) {
        super(listener,rxAppCompatActivity);
        setShowProgress(true);
        setCancel(true);
        setCache(true);
        setMethod("AppFiftyToneGraph/videoLink");
        setCookieNetWorkTime(60);
        setCookieNoNetWorkTime(24*60*60);
    }



    public boolean isAll() {
        return all;
    }

    public void setAll(boolean all) {
        this.all = all;
    }

    @Override
    public Observable getObservable(Retrofit retrofit) {
        HttpPostService service = retrofit.create(HttpPostService.class);
        return service.getAllVedioBys(isAll());
    }
}