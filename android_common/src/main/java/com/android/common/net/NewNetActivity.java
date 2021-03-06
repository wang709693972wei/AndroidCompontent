package com.android.common.net;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.common.BaseApplication;
import com.android.common.R;
import com.android.newcommon.utils.FileUtils;

import com.android.newcommon.net.LoginService;
import com.android.newcommon.net.MessageCodeEntity;
import com.android.newcommon.net.ZResponse;
import com.android.newcommon.net.fileLoad.MediaUploadResponse;
import com.android.newcommon.net.fileLoad.callback.ZUploadCallback;
import com.android.newcommon.net.fileLoad.upload.entity.FileAndParamName;
import com.android.newcommon.net.fileLoad.upload.entity.UploadInfo;
import com.android.newcommon.net.utils.GlideEngine;
import com.android.newcommon.net.utils.ZNetworkCallback;
import com.android.newcommon.net.ZNetwork;
import com.android.newcommon.net.utils.Callback;
import com.android.newcommon.utils.GlideUtils;
import com.android.newcommon.utils.LuBanUtils;
import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import androidx.annotation.Nullable;
import io.reactivex.Observable;
import io.reactivex.functions.Action;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;


public class NewNetActivity extends RxAppCompatActivity {
    private TextView text;
    private Button button1, block;
    // https://www.wanandroid.com/article/list/0/json?cid=60
    ImageView image;

    /*使用bindToLifecycle()
    https://www.jianshu.com/p/8311410de676
以Activity为例，在Activity中使用bindToLifecycle()方法，完成Observable发布的事件和当前的组件绑定，实现生命周期同步。从而实现当前组件生命周期结束时，自动取消对Observable订阅，代码如下：
*/
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_example);
        initViews();
        // 当执行onDestory()时， 自动解除订阅
      /*  Observable.interval(3, TimeUnit.SECONDS)
                .doOnDispose(new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.w("TAG", "Unsubscribing subscription from onCreate()");
                    }
                })
                //bindUntilEvent()，内部传入指定生命周期参数
                .compose(this.<Long>bindUntilEvent(ActivityEvent.DESTROY))
                //  .compose(this.<Long>bindToLifecycle())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long num) throws Exception {
                        Log.w("TAG", "Started in onCreate(), running until onDestory(): " + num);
                    }
                });
*/
    }

    protected void initViews() {
        image = findViewById(R.id.image);
        text = findViewById(R.id.text);
        button1 = findViewById(R.id.button1);
        button1.setText("图片选择");
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PictureSelector.create(NewNetActivity.this)
                        .openGallery(PictureMimeType.ofImage())
                        .loadImageEngine(GlideEngine.createGlideEngine()) // Please refer to the Demo GlideEngine.java
                        .forResult(PictureConfig.CHOOSE_REQUEST);

            }
        });
        block = findViewById(R.id.block);
        block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   testDestoryRequest();
                //  startRequest();
                startRequestGet();
            }
        });

    }

    /**
     * 测试关闭Activity 后 自动取消订阅
     */
    private void testDestoryRequest() {
        Observable observable = Observable.interval(3, TimeUnit.SECONDS)
                .doOnDispose(new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.w("TAG", "Unsubscribing subscription from onCreate()");
                    }
                });
        ZNetwork.with(this)
                .api(observable)
                .callback(new Callback<Long>() {
                    @Override
                    public void onNext(Long response) {
                        Log.w("TAG", "next" + response);

                    }
                });

    }


    private void startRequestPost() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("phone", "18565851235");
     /*   Observable<ZResponse<MessageCodeEntity>> observable
                = ZNetwork.getService(LoginService.class)
                .login(params);
*/
        ZNetwork.with(this)
                .api(ZNetwork.getService(LoginService.class).login(params))
                .callback(new ZNetworkCallback<ZResponse<MessageCodeEntity>>() {

                    @Override
                    public void onSuccess(ZResponse<MessageCodeEntity> response) {
                        if (response.data != null) {
                            text.setText("" + new Gson().toJson(response.data));
                            Toast.makeText(BaseApplication.getInstance(), response.data.toString(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(BaseApplication.getInstance(), "登录成功", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailed(int errorCode, String errorMessage) {
                        text.setText(errorCode + " " + errorMessage);
                        Toast.makeText(BaseApplication.getInstance(), errorMessage, Toast.LENGTH_SHORT).show();
                    }

                });

    }

    private void startRequestGet() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("beLikeMemberId", "0");
        params.put("likeMemberId", "1256981313");
        ZNetwork.with(this)
                .api(ZNetwork.getService(LoginService.class).template(params))
                .callback(new ZNetworkCallback<ZResponse<String>>() {

                    @Override
                    public void onBegin() {
                        super.onBegin();
                        Log.w("TAG", "-----onBegin---");
                    }

                    @Override
                    public void onEnd() {
                        super.onEnd();
                        Log.w("TAG", "-----onEnd---");
                    }

                    @Override
                    public void onSuccess(ZResponse<String> response) {
                        if (response.data != null) {
                            text.setText("" + new Gson().toJson(response.data));
                            Toast.makeText(BaseApplication.getInstance(), response.data.toString(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(BaseApplication.getInstance(), "登录成功", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailed(int errorCode, String errorMessage) {
                        text.setText(errorCode + " " + errorMessage);
                        Toast.makeText(BaseApplication.getInstance(), errorMessage, Toast.LENGTH_SHORT).show();
                    }

                });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:


                    // onResult Callback
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    String path = selectList.get(0).getPath();


                    LuBanUtils.LuBanCompressImage(path, new LuBanUtils.CompressListener() {
                        @Override
                        public void onSuccess(File file) {
                            Log.w("TAG", "onSuccess--" + Thread.currentThread());
                            Log.w("TAG", "-压缩后-->" + FileUtils.getInstance().computeSize(file)[0] + "--->" + FileUtils.getInstance().computeSize(file)[1]);
                            Log.w("TAG", "---压缩后---->" + file.length() / 1024 + "KB");
                        }

                        @Override
                        public void onError() {
                            Log.w("TAG", "onError--" + Thread.currentThread());

                        }
                    });


                    GlideUtils.loadImage(NewNetActivity.this, image, path);
                    File file = new File(path);
                    Log.w("TAG", path + "---size--onActivityResult---" + file.length()/* / 1024 + "KB"*/);
                    //uploadVideo(path);
                    newUploadVideo(path);
                    break;
                default:
                    break;
            }
        }
    }

    public void newUploadVideo(String mediaPath) {
        if (mediaPath == null || TextUtils.isEmpty(mediaPath)) {
            return;
        }
        File file = new File(mediaPath);
        if (!file.exists()) {
            return;
        }
        List<FileAndParamName> fileAndParamNames = new ArrayList<>();
        FileAndParamName fileAndParamName = new FileAndParamName(file, "multipartFile");
        fileAndParamNames.add(fileAndParamName);
        //fileAndParamNames.add(fileAndParamName);


        UploadInfo<ZResponse<MediaUploadResponse>> uploadInfo = new UploadInfo<ZResponse<MediaUploadResponse>>(fileAndParamNames) {
            @Override
            public Observable<ZResponse<MediaUploadResponse>> getApi(HashMap<String, RequestBody> params) {
                return ZNetwork.getService(LoginService.class).uploadVideo(1256981313, params);
            }
        };


        ZNetwork.with(this)
                .upload(uploadInfo)
                .callback(new ZUploadCallback<ZResponse<MediaUploadResponse>>() {


                    @Override
                    public void onProgress(int index, long allProgress, long allTotalProgress,
                                           long currentOneProgress, long currentOneTotalProgress, boolean done) {
                        Log.w("TAG", "----" + (int) ((float) allProgress / allTotalProgress * 100));
                    }

                    @Override
                    public void onSuccess(ZResponse<MediaUploadResponse> response) {
                        if (response.data != null) {
                            text.setText("" + new Gson().toJson(response.data));
                        }

                    }
                });
    }


























    /*-----------------------------------------------*/

    /**
     * @param mediaPath 文件路径
     */
    public void uploadVideo(String mediaPath) {
        if (mediaPath == null || TextUtils.isEmpty(mediaPath)) {
            return;
        }
        File file = new File(mediaPath);
        if (!file.exists()) {
            Log.w("TAG", "文件不存在");
            return;
        }
        HashMap<String, Object> params = new HashMap<>();
        params.put("memberId", "" + 1256981313);
        params.put("multipartFile", file);
        MultipartBody multipartBody = buildMultipartFileBody(params);
        ZNetwork.with(this)
                .api(ZNetwork.getService(LoginService.class).upload(multipartBody.parts()))
                .callback(new ZUploadCallback<ZResponse<MediaUploadResponse>>() {
                    @Override
                    public void onProgress(int index, long allProgress, long allTotalProgress,
                                           long currentOneProgress, long currentOneTotalProgress, boolean done) {
                        // view.onProgress((int) ((float) allProgress / allTotalProgress * 100));
                    }

                    @Override
                    public void onSuccess(ZResponse<MediaUploadResponse> response) {
                        Bundle bundle = new Bundle();
                        if (response.data != null) {

                        }
                    }
                });
    }


    public MultipartBody buildMultipartFileBody(Map<String, Object> params) {
        MultipartBody.Builder builder = new MultipartBody.Builder("**-----------**").setType(MultipartBody.FORM);
        for (Map.Entry<String, Object> stringObjectEntry : params.entrySet()) {
            Object value = stringObjectEntry.getValue();
            if (value instanceof String) {
                builder.addFormDataPart(stringObjectEntry.getKey(), (String) stringObjectEntry.getValue());
            }
            if (value instanceof Integer)
                builder.addFormDataPart(stringObjectEntry.getKey(), stringObjectEntry.getValue() + "");
            if (value instanceof JSONObject)
                builder.addFormDataPart(stringObjectEntry.getKey(), stringObjectEntry.getValue().toString());
            else if (value instanceof File) {
                File value1 = (File) value;
                builder.addFormDataPart(stringObjectEntry.getKey(), value1.getName(), createCustomRequestBody(MediaType.parse("application/octet-stream"), value1));
            }
        }
        MultipartBody build = builder.build();
        return build;
    }

    public static RequestBody createCustomRequestBody(final MediaType contentType, final File file) {
        return new RequestBody() {
            @Override
            public MediaType contentType() {
                return contentType;
            }

            @Override
            public long contentLength() {
                return file.length();
            }


            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                Source source;
                try {
                    source = Okio.source(file);
                    //sink.writeAll(source);
                    Buffer buf = new Buffer();
                    Long remaining = contentLength();
                    for (long readCount; (readCount = source.read(buf, 2048)) != -1; ) {
                        sink.write(buf, readCount);
                        Log.w("TAG", "" + contentLength() + remaining + readCount + remaining);
                        // listener.onProgress(contentLength(), remaining -= readCount, remaining == 0);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }


}


