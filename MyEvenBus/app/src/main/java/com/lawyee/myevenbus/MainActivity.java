package com.lawyee.myevenbus;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lawyee.myevenbus.Service.NetService;
import com.lawyee.myevenbus.ServiceImp.MyService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private NetService service;

    private Button mBtnSend;
    private TextView mTv;
    private TextView mTv1;
    private TextView mTv2;
    private Button mSendFragment;
    private Button mSendService;
    private Button mStopService;
    private Intent intent1;
    private ServiceConnection connection;
    private ArrayList<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();

    }

    private void initData() {

        connection = new ServiceConnection() {

            //J解绑
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                Log.e("服务绑定成功", "onServiceDisconnected: ");
//                MyService.mynetservice binder = (MyService.mynetservice) iBinder;
//                list = binder.AddData();
//                for (int i = 0; i < list.size(); i++) {
//                mTv2.setText(list.get(i));
//                }
                service = (NetService) iBinder;
                ArrayList<String> strings = service.AddData();
                for (int i = 0; i < strings.size(); i++) {
                    mTv2.setText(strings.get(i));
                }
            }

            //绑定成功
            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                Log.e("服务解绑成功", "onServiceConnected: ");

            }
        };


        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.content_fragment, new BlankFragment());
        transaction.commit();


    }

    @Override

    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(MainActivity.this);
        }
    }

    private void initView() {


        mBtnSend = (Button) findViewById(R.id.btn_send);
        mTv = (TextView) findViewById(R.id.tv);
        mBtnSend.setOnClickListener(this);
        mTv1 = (TextView) findViewById(R.id.tv1);
        mTv1.setOnClickListener(this);
        mTv2 = (TextView) findViewById(R.id.tv2);
        mTv2.setOnClickListener(this);
        mSendService = (Button) findViewById(R.id.Send_Service);
        mSendService.setOnClickListener(this);
        mStopService = (Button) findViewById(R.id.Stop_Service);
        mStopService.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send:
                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                startActivity(intent);
                break;
            case R.id.Send_Service:
                intent1 = new Intent(MainActivity.this, MyService.class);
                bindService(intent1, connection, BIND_AUTO_CREATE);
                break;
            case R.id.Stop_Service:
                unbindService(connection);
                break;
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvenMain(FirsEvent event) {
        Log.d("//在ui线程执行", "onEvenMain: " + event.getmMsg());
        mTv.setText(event.getmMsg());
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onFirstEven(FirsEvent e) {
        Log.d("//在后台线程执行", "onFirstEven: " + e.getmMsg());
    }

    @Subscribe(threadMode = ThreadMode.ASYNC) //强制在后台执行
    public void onFirstEvens(FirsEvent event) {
        Log.d("//强制在后台执行", "onUserEvent: " + event.getmMsg());
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onFirstEvenPost(FirsEvent event) {
        Log.d("默认方式, 在发送线程执行", "onFirstEvenPost: " + event.getmMsg());
        mTv1.setText(event.getmMsg());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnTwoEven(TwoEven even) {
        Log.e("Ui线程", "OnTwoEven: " + even.getList().size());
        for (int i = 0; i < even.getList().size(); i++) {
            mTv2.setText(even.getList().get(i) + "\n");
        }

        String[] string = even.getList().toArray(new String[even.getList().size()]);

        for (int i = 0; i < string.length; i++) {
            Log.d("===", "==" + string[i]);
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFragment(FragmentEven even) {
        mTv2.setText(even.getMsg().toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

    }

}
