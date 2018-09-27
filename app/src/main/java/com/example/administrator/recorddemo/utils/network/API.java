package com.example.administrator.recorddemo.utils.network;


import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * Created by Administrator on 2018/9/13.
 */
public interface API {
    String testUrl = "http://192.168.100.236:7777/image-service/common/file_get?systemId=dataPlatform&key=1536894987446";
    String url = "file_upload?systemId=dataPlatform&secret=9e0891a7a8c8e885&key=";

    @POST
    Observable<ResponseBody> uploadFile(@Url String url, @Body MultipartBody body);

}
