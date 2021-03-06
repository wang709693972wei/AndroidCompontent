package com.moudlea;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.android.common.bean.Author;
import com.android.common.router.ActivityPath;
import com.android.common.router.IUserModuleService;
import com.android.newcommon.base.BaseFragment;
import com.android.newcommon.utils.FileUtils;
import com.moudlea.jetpack.JetPackStudyActivity;
import com.moudlea.rxjava.RxActivity;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by wangwei on 2018/4/26.
 */
@Route(path = ActivityPath.AFragment)
public class AFragment extends BaseFragment {

    @BindView(R2.id.btn1)
    Button btn1;
    @BindView(R2.id.btn2)
    Button btn2;
    @BindView(R2.id.btn3)
    Button btn3;
    @BindView(R2.id.btn4)
    Button btn4;
    @BindView(R2.id.btn5)
    Button btn5;
    @BindView(R2.id.btn6)
    Button btn6;


    @Override
    protected int layoutResID() {
        return R.layout.fragment_a;
    }

    @Override
    protected void initView(View view) {
        setListener();
        FileUtils.getInstance().getVideoFile();

        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(mActivity);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("ww.LocalBroadcastManager");

        broadcastManager.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.w("TAG","registerReceiver1");
                String change = intent.getStringExtra("change");
                Toast.makeText(mActivity, change, Toast.LENGTH_LONG).show();
            }
        }, intentFilter);



    }


    /**
     * 注销广播
     */
    @Override
    public void onDetach() {
        super.onDetach();
        //broadcastManager.unregisterReceiver(mAdDownLoadReceiver);
    }


    @OnClick({R2.id.btn4, R2.id.btn5, R2.id.btn6, R2.id.btn7})
    void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.btn4) {
            btn4.setText(getUserAddress());
        } else if (id == R.id.btn5) {
            startActivity(new Intent(mActivity, JetPackStudyActivity.class));
        } else if (id == R.id.btn6) {
            FileUtils.getInstance().getVideoFile();
           // startActivity(new Intent(mActivity, RxActivity.class));
        } else if (id == R.id.btn7) {
            //https://www.cnblogs.com/zhaoyanjun/p/6048369.html
            //发送广播， LocalBroadcastManager用的单例模式，getInstance获取对象即可调用发送 注册等；使用完成也要取消注册
            Intent intent = new Intent("ww.LocalBroadcastManager");
            intent.putExtra("change", "yes");
            LocalBroadcastManager.getInstance(mActivity).sendBroadcast(intent);

        }
    }

    public String getUserAddress() {
        IUserModuleService userModuleService = ARouter.getInstance().navigation(IUserModuleService.class);
        if (userModuleService != null) {
            return userModuleService.getUserInfo();
        }
        return "";
    }

    @Override
    protected void initData() {

    }

    private void setListener() {
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // EventBus.getDefault().post(new MemberEvent(""));//刷新会员
                ARouter.getInstance().build(ActivityPath.UIActivity).navigation();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Author author = new Author();
                author.setName("Margaret Mitchell");
                author.setCounty("USA");
                ARouter.getInstance().build(ActivityPath.BModuleActivity)
                        .withString("name", "老王")
                        .withInt("age", 18)
                        .withString("url", "https://a.b.c")
                        .withSerializable("author", author)
                        .navigation();

            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ARouter.getInstance().build(ActivityPath.BModuleActivity).navigation(getActivity(), 100);

            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(getActivity(), "onActivityResult", Toast.LENGTH_LONG).show();
        Log.w("TAG", "-------onActivityResult");

    }
}
