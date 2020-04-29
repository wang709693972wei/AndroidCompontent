package com.basemodule;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.arouter.launcher.ARouter;

/**
 * author chmyy
 * created on 2018/1/22
 * email fat_chao@163.com.
 */

public abstract class BaseFragment extends Fragment {

    private View mContentView = null;
    public Activity mActivity = null;
    private Unbinder mUnBinder;

    protected abstract void initView(View view);  // 初始化view

    protected abstract void initData();//初始化

    protected abstract int initLayout();//设置布局


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(initLayout(), container, false);
        mUnBinder = ButterKnife.bind(this, mContentView);
        ARouter.getInstance().inject(this);
        initView(mContentView);
        initData();
        return mContentView;
    }





    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mActivity != null) {
            mActivity = null;
        }
        if (mUnBinder != null) {
            mUnBinder.unbind();//解绑
        }

    }
}