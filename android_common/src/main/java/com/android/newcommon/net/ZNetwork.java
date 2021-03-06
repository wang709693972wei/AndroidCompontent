package com.android.newcommon.net;

import com.android.newcommon.net.download.DownloadInfo;
import com.android.newcommon.net.download.DownloadManager;
import com.android.newcommon.net.download.IDownloadCallback;
import com.android.newcommon.net.manager.RequestManager;
import com.android.newcommon.net.retrofit.ZRetrofit;
import com.trello.rxlifecycle2.LifecycleProvider;


public class ZNetwork {

    /**
     * 获取service
     * @param service
     * @param <T>
     * @return
     */
    public static <T> T getService(Class<T> service) {
        return ZRetrofit.getInstance().create(service);
    }




    /**
     * 进行网络请求
     *
     * @param lifecycleProvider 绑定生命周期
     * @return 请求管理器
     */
    public static RequestManager with(LifecycleProvider lifecycleProvider) {
        return new RequestManager(lifecycleProvider);
    }

    /**
     * 进行网络请求
     * (不绑定生命周期)
     *
     * @return 请求管理器
     */
    public static RequestManager with() {
        return new RequestManager(null);
    }




    public static void download(DownloadInfo info, IDownloadCallback iDownloadCallback) {
        DownloadManager.getInstance().addDownloadTask(info, iDownloadCallback);
    }

    public static void removeDownloadTask(DownloadInfo info) {
        DownloadManager.getInstance().removeDownloadTask(info);
    }

}
