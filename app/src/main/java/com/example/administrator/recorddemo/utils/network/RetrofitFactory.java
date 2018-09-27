package com.example.administrator.recorddemo.utils.network;

import com.example.administrator.recorddemo.utils.RetrofitLogInterceptor;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by wangmaobo on 2018/8/23.
 */
public class RetrofitFactory {

    private static String BASE_URL = "";
    private static final long TIMEOUT = 15;

    private static OkHttpClient httpClient = new OkHttpClient.Builder()
            .addInterceptor(new RetrofitLogInterceptor())
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .build();

    private static API mApi;

    public static API getInstance() {
        if (null == mApi) {
            BASE_URL = "http://192.168.100.236:7777/image-service/common/";
            mApi = new Retrofit.Builder()
                    .baseUrl(BASE_URL)//RetrofitLogInterceptor
                    .addConverterFactory(GsonConverterFactory.create(createGSon()))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(httpClient)
                    .build()
                    .create(API.class);
        }
        return mApi;
    }

    private static Gson createGSon() {
        return new GsonBuilder()
                .serializeNulls()
                .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();
    }

    public void upLoadFile(String url, File file,
                           FileUploadObserver<ResponseBody> fileUploadObserver) {

        UploadFileRequestBody uploadFileRequestBody =
                new UploadFileRequestBody(file, fileUploadObserver);
        getInstance()
                .uploadFile(url, MultipartBuilder.fileToMultipartBody(file,
                        uploadFileRequestBody))
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(fileUploadObserver);

    }

    public void upLoadFiles(String url, List<File> files,
                            FileUploadObserver<ResponseBody> fileUploadObserver) {
        getInstance()
                .uploadFile(url, MultipartBuilder.filesToMultipartBody(files,
                        fileUploadObserver))
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(fileUploadObserver);

    }


}
