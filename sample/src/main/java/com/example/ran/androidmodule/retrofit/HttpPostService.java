package com.example.ran.androidmodule.retrofit;

import com.tool.network.retrofit.api.BaseResultEntity;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

public interface HttpPostService {
    @GET("getSongPoetry")
    Observable<RetrofitEntity> getSong(@Query("page") int page);

    @POST("AppFiftyToneGraph/videoLink")
    Observable<RetrofitEntity> getAllVedioBy(@Body boolean once_no);

    @FormUrlEncoded
    @POST("AppFiftyToneGraph/videoLink")
    Observable<BaseResultEntity<List<SubjectResulte>>> getAllVedioBys(@Field("once") boolean once_no);

    /*上传文件*/
    @POST()
    Observable<BaseResultEntity<UploadResulte>> uploadImage(@Url() String url,
                                                            @Body RequestBody Body);

}
