package com.tool.network.retrofit.api;


import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.*;
import rx.Observable;

import java.util.List;
import java.util.Map;

/**
 * @作者          吴孝然
 * @创建日期      2019/1/24 17:53
 * @描述          retrofit Api 配置
 **/
public interface BaseApiService {
    @POST()
    @FormUrlEncoded
    <T> Observable<ResponseBody> executePost(
            @Url() String url,
            @FieldMap Map<String, Object> maps);

    @POST()
    Observable<ResponseBody> executePostBody(
            @Url String url,
            @Body Object object);

    @GET()
    <T> Observable<ResponseBody> executeGet(
            @Url String url,
            @QueryMap Map<String, Object> maps);

    @DELETE()
    <T> Observable<ResponseBody> executeDelete(
            @Url String url,
            @QueryMap Map<String, Object> maps);

    @PUT()
    <T> Observable<ResponseBody> executePut(
            @Url String url,
            @FieldMap Map<String, Object> maps);

    @Multipart
    @POST()
    Observable<ResponseBody> upLoadImage(
            @Url() String url,
            @Part("image\"; filename=\"image.jpg") RequestBody requestBody);

    @Multipart
    @POST()
    Observable<ResponseBody> uploadFlie(
            @Url String fileUrl,
            @Part("description") RequestBody description,
            @Part("image\"; filename=\"image.jpg") MultipartBody.Part file);

    @POST()
    Observable<ResponseBody> uploadFiles(
            @Url() String url,
            @Body Map<String, RequestBody> maps);

    @Multipart
    @POST()
    Observable<ResponseBody> uploadFlieWithPart(
            @Url String fileUrl,
            @Part() MultipartBody.Part file);

    @Multipart
    @POST()
    Observable<ResponseBody> uploadFlieWithPartList(
            @Url String fileUrl,
            @Part List<MultipartBody.Part> list);


    @Multipart
    @POST()
    Observable<ResponseBody> uploadFlieWithPartMap(
            @Url String fileUrl,
            @PartMap Map<String, MultipartBody.Part> maps);


    @POST()
    Observable<ResponseBody> uploadFile(
            @Url() String url,
            @Body RequestBody file);

    @Multipart
    @POST
    Observable<ResponseBody> uploadFileWithPartMap(
            @Url() String url,
            @PartMap() Map<String, RequestBody> partMap,
            @Part() MultipartBody.Part file);

    @Streaming
    @GET
    Observable<ResponseBody> downloadFile(@Url String fileUrl);


    @GET
    Observable<ResponseBody> downloadSmallFile(@Url String fileUrl);


    @GET
    <T> Observable<ResponseBody> getTest(@Url String fileUrl,
                                         @QueryMap Map<String, Object> maps);

    @FormUrlEncoded
    @POST()
    <T> Observable<ResponseBody> postForm(
            @Url() String url,
            @FieldMap Map<String, Object> maps);

    @POST()
    Observable<ResponseBody> postRequestBody(
            @Url() String url,
            @Body RequestBody Body);
}


