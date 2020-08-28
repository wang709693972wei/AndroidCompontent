package com.android.newcommon.monitor.crash;

import android.app.Application;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.widget.Toast;

import com.android.newcommon.monitor.block.core.LogHelper;
import com.android.newcommon.monitor.util.FileManager;
import com.android.newcommon.monitor.util.ThrowableUtils;
import com.android.newcommon.monitor.util.Utils;
import com.google.gson.Gson;

import java.io.File;

/**
 * Created by wanglikun on 2019-06-12
 */
public class CrashCaptureManager implements Thread.UncaughtExceptionHandler {
    private static final String TAG = "CrashCaptureManager";
    private final Thread.UncaughtExceptionHandler mDefaultHandler;
    private final Handler mHandler;
    private Application mContext;

    private CrashCaptureManager() {
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        HandlerThread handlerThread = new HandlerThread(TAG);
        handlerThread.start();
        mHandler = new Handler(handlerThread.getLooper());
    }

    private static class Holder {
        private static final CrashCaptureManager INSTANCE = new CrashCaptureManager();
    }

    public static CrashCaptureManager getInstance() {
        return Holder.INSTANCE;
    }

    private boolean isShowCrashPanel;

    public void init(Application context, boolean showCrashPanel) {
        mContext = context;
        isShowCrashPanel = showCrashPanel;
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(final Thread t, final Throwable e) {
        LogHelper.d(TAG, t.toString());
        LogHelper.d(TAG, Log.getStackTraceString(e));

        String crashPath = FileManager.getCrashDir().getAbsolutePath() + File.separator + "crash_" + FileManager.createFile();
        FileManager.writeTxtToFile(new Gson().toJson(ThrowableUtils.getFullStackTrace(e)).toString(), crashPath);
        Log.w("TAG", "crashPath--->" + crashPath);

        if (isShowCrashPanel) {
            Utils.startCrashService(mContext, crashPath);
             /*Intent crashIntent = new Intent(mContext, CrashPanelAty.class);
             crashIntent.putExtra(Utils.FLAG_INFO, crashPath);
             mContext.startActivity(crashIntent);*/
        }
        post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(mContext, "应用崩溃了", Toast.LENGTH_SHORT).show();
            }
        });
        postDelay(new Runnable() {
            @Override
            public void run() {
                if (mDefaultHandler != null) {
                    mDefaultHandler.uncaughtException(t, e);
                }
            }
        }, 2000);
    }

    private void post(Runnable r) {
        mHandler.post(r);
    }

    private void postDelay(Runnable r, long delayMillis) {
        mHandler.postDelayed(r, delayMillis);
    }


}