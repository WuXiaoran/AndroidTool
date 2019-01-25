package com.tool.network.retrofit.utils;

import android.content.res.Resources;
import android.net.Uri;
import android.os.Looper;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tool.network.retrofit.ContentType;
import com.tool.network.retrofit.ResponseCallback;
import com.tool.network.retrofit.UploadRequestBody;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * @作者          吴孝然
 * @创建日期      2019/1/24 17:54
 * @描述          创建retrofit请求时需要的body
 **/
public class Util {

    public static final String MULTIPART_FORM_DATA = "multipart/form-data;";
    public static final String MULTIPART_IMAGE_DATA = "image/*; charset=utf-8";
    public static final String MULTIPART_JSON_DATA = "application/json; charset=utf-8";
    public static final String MULTIPART_VIDEO_DATA = "video/*";
    public static final String MULTIPART_AUDIO_DATA = "audio/*";
    public static final String MULTIPART_TEXT_DATA = "text/plain";
    public static final String MULTIPART_APK_DATA = "application/vnd.android.package-archive";
    public static final String MULTIPART_JAVA_DATA = "java/*";
    public static final String MULTIPART_MESSAGE_DATA = "message/rfc822";

    public static <T> T checkNotNull(T object, String message) {
        if (object == null) {
            throw new NullPointerException(message);
        }
        return object;
    }

    public static boolean checkMain() {
        return Thread.currentThread() == Looper.getMainLooper().getThread();
    }

    public static RequestBody createJson(String jsonString) {
        checkNotNull(jsonString, "json not null!");
        return RequestBody.create(MediaType.parse(MULTIPART_JSON_DATA), jsonString);
    }

    /**
     * @param text
     * @return RequestBody
     */
    public static RequestBody createText(String text) {
        checkNotNull(text, "text not null!");
        return RequestBody.create(MediaType.parse(MULTIPART_TEXT_DATA), text);
    }

    /**
     * @param name
     * @return RequestBody
     */
    public static RequestBody createString(String name) {
        checkNotNull(name, "name not null!");
        return RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA + "; charset=utf-8"), name);
    }

    /**
     * @param file
     * @return
     */
    public static RequestBody createFile(File file) {
        checkNotNull(file, "file not null!");
        return RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA + "; charset=utf-8"), file);
    }


    /**
     * @param file file
     * @param type see {@link ContentType}
     * @return
     */
    @NonNull
    public static RequestBody createBody(File file, ContentType type) {
        checkNotNull(file, "file not be null!");
        checkNotNull(type, "type not be null!");
        return createBody(file, typeToString(type));
    }


    @NonNull
    public static RequestBody createBody(File file, String mediaType) {
        checkNotNull(file, "file not null!");
        if (TextUtils.isEmpty(mediaType)) {
            throw new NullPointerException("contentType not be null");
        }
        return RequestBody.create(MediaType.parse(mediaType), file);
    }


    /**
     * createImage
     * @param file file
     * @return
     */
    @NonNull
    public static RequestBody createImage(File file) {
        checkNotNull(file, "file not null!");
        return RequestBody.create(MediaType.parse(MULTIPART_IMAGE_DATA), file);
    }

    /**
     * createPart From String
     * @param descriptionString
     * @return
     */
    @NonNull
    public static RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
                MediaType.parse(MULTIPART_FORM_DATA + "; charset=utf-8"), descriptionString);
    }


    /**
     * ContentType To String
     * @param type see {@link ContentType}
     * @return String mediaType
     */
    @NonNull
    public static String typeToString(@NonNull ContentType type) {
        switch (type) {
            case APK:
                return MULTIPART_APK_DATA;

            case VIDEO:
                return MULTIPART_VIDEO_DATA;

            case AUDIO:
                return MULTIPART_AUDIO_DATA;

            case JAVA:
                return MULTIPART_JAVA_DATA;

            case IMAGE:
                return MULTIPART_IMAGE_DATA;

            case TEXT:
                return MULTIPART_TEXT_DATA;

            case JSON:
                return MULTIPART_JSON_DATA;

            case FORM:
                return MULTIPART_FORM_DATA;
            case MESSAGE:
                return MULTIPART_MESSAGE_DATA;
            default:
                return MULTIPART_IMAGE_DATA;
        }

    }

    /**
     * createRequestBody
     * @param file file
     * @param type  see {@link ContentType}
     * @return UploadRequestBody
     */
    @NonNull
    public static UploadRequestBody createRequestBody(@NonNull File file, @NonNull ContentType type) {
        return createRequestBody(file, type, null);
    }

    /**
     * createRequestBody
     * @param file file
     * @param type  see {@link ContentType}
     * @return UploadRequestBody
     */
    @NonNull
    public static UploadRequestBody createRequestBody(@NonNull File file, @NonNull ContentType type, ResponseCallback callback) {
        return new UploadRequestBody(createBody(file, type), callback);
    }

    @NonNull
    public static MultipartBody.Part createPart(String partName ,File file) {
        // create RequestBody instance from file
                RequestBody requestFile =
                RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA + "; charset=utf-8"), file);
        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    @NonNull
    public static MultipartBody.Part createPart(String partName ,File file, @NonNull ContentType type) {
        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(MediaType.parse(typeToString(type) + "; charset=utf-8"), file);
        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri) {
        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
        // use the FileUtils to get the actual file by uri
        File file = FileUtil.getUirFile(fileUri);
        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), file);

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }


    /**
     * create MultipartBody Parts
     * @param partName
     * @param maps Files
     * @param type ContentType type
     * @param callback ResponseCallback
     * @return  Map<String, MultipartBody.Part>
     */
    @NonNull
    public static  Map<String, MultipartBody.Part> createParts(String partName , Map<String, File> maps, @NonNull ContentType type, ResponseCallback callback ) {
        // create RequestBody instance from file
        Map<String, MultipartBody.Part> parts = new HashMap<>();
        if (maps != null && maps.size() > 0) {
            Iterator<String> keys = maps.keySet().iterator();
            UploadRequestBody requestBody = null;
            while(keys.hasNext()){
                String i = keys.next();
                File file = maps.get(i);
                if (!FileUtil.exists(file)) {
                    throw new Resources.NotFoundException(file.getPath() + "file 路径无法找到");
                } else {
                    requestBody = createRequestBody(file, type, callback);
                    // MultipartBody.Part is used to send also the actual file name
                    MultipartBody.Part body =
                            MultipartBody.Part.createFormData(partName, file.getName(), requestBody);
                    parts.put(i, body);
                }
            }
        }
        return parts;
    }

    /**
     * create MultipartBody Parts
     * @param partName
     * @param list List<File>
     * @param type ContentType type
     * @param callback ResponseCallback
     * @return  Map<String, MultipartBody.Part>
     */
    @NonNull
    public static List<MultipartBody.Part> createPartLists(String partName , List<File> list, @NonNull ContentType type, ResponseCallback callback ) {
        List<MultipartBody.Part> parts = new ArrayList<>();
        if (list!= null && list.size() > 0) {

            UploadRequestBody requestBody = null;
            for (File file: list) {
                if (!FileUtil.exists(file)) {
                    throw new Resources.NotFoundException(file.getPath() + "file 路径无法找到");
                } else {
                    requestBody = createRequestBody(file, type, callback);
                    // MultipartBody.Part is used to send also the actual file name
                    MultipartBody.Part body =
                            MultipartBody.Part.createFormData(partName, file.getName(), requestBody);
                    parts.add(body);
                }
            }
        }
        return parts;
    }


    /** createNovateRequestBody
     * @param requestBody requestBody
     * @param callback  ResponseCallback
     * @return UploadRequestBody
     */
    @NonNull
    public static UploadRequestBody createNovateRequestBody(RequestBody requestBody, ResponseCallback callback) {
        return new UploadRequestBody(requestBody, callback);
    }


    public static <T> List<T> jsonToList(String json, Class<T> clazz) {
        if (null == json) {
            return null;
        }
        return new Gson().fromJson(json, new TypeToken<T>(){}.getType());
    }

    public static int checkDuration(String name, long duration, TimeUnit unit) {
        if (duration < 0) throw new IllegalArgumentException(name + " < 0");
        if (unit == null) throw new NullPointerException("unit == null");
        long millis = unit.toMillis(duration);
        if (millis > Integer.MAX_VALUE) throw new IllegalArgumentException(name + " too large.");
        if (millis == 0 && duration > 0) throw new IllegalArgumentException(name + " too small.");
        return (int) millis;
    }
}
