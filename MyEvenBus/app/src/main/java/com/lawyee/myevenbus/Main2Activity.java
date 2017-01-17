package com.lawyee.myevenbus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener {

    private Button mBtmFirstEvent;
    private Button mBtnList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        initView();
    }

    private void initView() {
        mBtmFirstEvent = (Button) findViewById(R.id.btm_first_event);
        mBtmFirstEvent.setOnClickListener(this);
        mBtnList = (Button) findViewById(R.id.btn_list);
        mBtnList.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btm_first_event:
                EventBus.getDefault().post(new FirsEvent("firsteven btn clciked"));
                finish();
                break;
            case R.id.btn_list:
                EventBus.getDefault().post(new TwoEven(addList()));
                finish();
                break;
        }
    }
    private ArrayList<String> addList(){
        ArrayList<String> list = new ArrayList<>();
         String s ="小米";
        for (int i = 0; i < 5; i++) {
            list.add(s+i);
        }
        return list;
    }
}
