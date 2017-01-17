package com.lawyee.myevenbus.ServiceImp;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.lawyee.myevenbus.Service.NetService;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

public class MyService extends Service {
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new mynetservice();
    }
    private ArrayList<String> addDatas() {
        int i = 10;
        ArrayList<String> ints = new ArrayList<>();
        for (int i1 = 0; i1 < i; i1++) {
            ints.add("小红"+i1);
        }
        return ints;
    }


    public class mynetservice extends Binder implements NetService {

        @Override
        public ArrayList<String> AddData() {
            return addDatas();
        }
    }


}
