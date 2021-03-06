package com.android.newcommon.net.retrofit;

import com.android.common.UrlUtil;
import com.android.newcommon.net.adapter.GsonDoubleDefaultAdapter;
import com.android.newcommon.net.adapter.GsonFloatDefaultAdapter;
import com.android.newcommon.net.adapter.GsonIntegerDefaultAdapter;
import com.android.newcommon.net.adapter.GsonLongDefaultAdapter;
import com.android.newcommon.net.interceptor.AddHeaderInterceptor;
import com.android.newcommon.net.interceptor.DownloadInterceptor;
import com.android.newcommon.net.interceptor.LogInterceptor;
import com.android.newcommon.net.utils.HttpsUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLSocketFactory;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ZRetrofit {
    private static final String TAG = ZRetrofit.class.getSimpleName();
    private static final String HTTPS_PREFIX = "https://";

    private static ZRetrofit instance;


    private Retrofit mHttpsRetrofit;


    public static ZRetrofit getInstance() {
        if (instance == null) {
            synchronized (ZRetrofit.class) {
                if (instance == null) {
                    instance = new ZRetrofit();
                }
            }
        }
        return instance;
    }

    private Retrofit getRetrofit() {
        if (mHttpsRetrofit == null) {
            mHttpsRetrofit = initRetrofit();
        }
        return mHttpsRetrofit;
    }


    private Retrofit initRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(UrlUtil.url)
                .addConverterFactory(GsonConverterFactory.create(buildGSON()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(getOkHttpClient())
                .build();
    }

    private Gson buildGSON() {
        return new GsonBuilder()
                .setLenient()
                .serializeNulls()
                .registerTypeAdapter(Integer.class, new GsonIntegerDefaultAdapter())
                .registerTypeAdapter(int.class, new GsonIntegerDefaultAdapter())
                .registerTypeAdapter(Double.class, new GsonDoubleDefaultAdapter())
                .registerTypeAdapter(double.class, new GsonDoubleDefaultAdapter())
                .registerTypeAdapter(Float.class, new GsonFloatDefaultAdapter())
                .registerTypeAdapter(float.class, new GsonFloatDefaultAdapter())
                .registerTypeAdapter(Long.class, new GsonLongDefaultAdapter())
                .registerTypeAdapter(long.class, new GsonLongDefaultAdapter())
                .create();
    }

    private final static int CONNECT_TIMEOUT = 30;
    private final static int READ_TIMEOUT = 100;
    private final static int WRITE_TIMEOUT = 100;

    public OkHttpClient getOkHttpClient() {

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)//连接超时时间
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(new AddHeaderInterceptor())
                .addInterceptor(new DownloadInterceptor())//在非下载的情况下  可考虑不加这个拦截器
                .addInterceptor(new LogInterceptor());

        SSLSocketFactory sslSocketFactory = HttpsUtil.getNoCheckSSLSocketFactory();
        if (sslSocketFactory != null) {
            builder.sslSocketFactory(sslSocketFactory);
        }
        return builder.build();
    }


    public <T> T create(Class<T> service) {
        return getRetrofit().create(service);
    }


}
