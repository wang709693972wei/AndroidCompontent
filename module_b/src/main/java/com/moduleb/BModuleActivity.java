package com.moduleb;

import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.android.common.router.ActivityPath;
import com.android.common.base.BaseActivity;
import com.android.common.base.BasePresenter;

/**
 * Created by wangwei on 2018/4/17.
 */
@Route(path = ActivityPath.BModuleActivity)
public class BModuleActivity extends BaseActivity {
//    @Autowired
//    Author author;
//

    TextView txt;


    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int initLayout() {
        return R.layout.b_module_layout;
    }




    @Override
    protected void initData() {

    }

    @Override
    protected void initViews() {
        txt = findViewById(R.id.txt);
       // ARouter.getInstance().inject(this);

      //  Author author = (Author) getIntent().getSerializableExtra("author");
      //  txt.setText("name:" + author.getName());

        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });
    }
}
