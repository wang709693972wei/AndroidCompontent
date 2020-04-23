package com.module.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.module.ui.R;
import com.module.ui.util.DividerItemDecoration;

import java.util.ArrayList;


public class UIActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private MainAdapter mAdapter;
    private LinearLayoutManager linearLayoutManager;
    private String arr[] = {"圆形进度条", "模糊搜索", "尺子刻度", "图片查看", "横向滚动View",
            "图片预览", "支付宝城市服务", "高斯模糊圖片", "步骤条Step"};

    private Class<?>[] ACTIVITY = {CircleProgressActivity.class,
            SearchActivity.class, RulerViewActivity.class, PhotoGalleryActivity.class,
            HorizontalScrollViewActivity.class, PhotoGalleryActivity.class,
            RecyleViewTabActivity.class, MoHuImageActivity.class, StepActivity.class};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui);

        mRecyclerView = findViewById(R.id.rv_list);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration());
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());    // 设置item动画
        mAdapter = new MainAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        for (String item : arr) {
            mAdapter.addItem(item);
        }
        setListener();

    }

    //跳转单张图片
    private void showOriginalPics(int position) {
        final ArrayList<String> thumbnails = new ArrayList<String>();
        thumbnails.add("http://img3.3lian.com/2013/c4/95/d/18.jpg");
        thumbnails.add("https://resources.ninghao.org/images/candy-shop.jpg");
        thumbnails.add("https://resources.ninghao.org/images/contained.jpg");
        Intent intent = new Intent(this, PhotoGalleryActivity.class);
        intent.putExtra("image_urls", thumbnails);
        intent.putExtra("image_index", position);
        Log.w("qwert", "" + position + "" + thumbnails);
        startActivity(intent);
    }

    private void setListener() {
        mAdapter.setOnItemClickLitener(new MainAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(int index) {

                Intent intent = new Intent(UIActivity.this, ACTIVITY[index]);
                startActivity(intent);
            }
        });
    }
}
