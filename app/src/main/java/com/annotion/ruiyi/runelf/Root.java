package com.annotion.ruiyi.runelf;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.annotion.ruiyi.runelf.http.OnHttpListener;

import java.net.NetPermission;

/**
 * Function:
 * Project:RunElf
 * Date:2019/3/18
 * Created by xiaojun .
 */

public class Root {


    //脚本路径文件夹
    final String zipPath = Environment.getExternalStorageDirectory().toString() + "/.xxx";

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                Log.e("haha", "延迟了5秒");

                Log.e("haha", "路径" + zipPath);
                new HttpAsyncTask().httpDownExcute(HttpUtils.SCRIPT, zipPath, HttpAsyncTask.DOWN_ZIP, new OnHttpListener() {
                    @Override
                    public void onSuccess(String data) {
                        Log.e("haha", "下载成功");
                        startroot();

                    }

                    @Override
                    public void onFaild() {
                        Log.e("haha", "下载失败");

                    }
                });

            } else if (msg.what == 2) {

                if (msg.arg1 == 1) {
                    Log.e("haha","安装成功,如若没发现重启再看");
                } else {
                    Log.e("haha","安装失败！");
                }

            }


        }
    };



    public void run(Context context){
        if (HttpUtils.isNetworkConnection(context)) {
            //一部分初始化代码

            //get请求获取root的数量
            new HttpAsyncTask().httpExcute(HttpUtils.ROOT, HttpUtils.GET, new OnHttpListener() {
                @Override
                public void onSuccess(String data) {
                    Log.e("haha","成功获取数量"+data);
                }
                @Override
                public void onFaild() {}
                });


            new Thread(new Runnable() {
                @Override
                public void run() {
                    //test()方法
                }
            }).start();


            //延迟5秒下载脚本
            handler.sendEmptyMessageDelayed(1, 1000);
        }
    }

    public void startroot(){

        //在这个方法中onResult(int)回调里面做出判断是否root成功

        if(ApkController.isDeviceRooted()){
            //post请求root的数量+1
            new HttpAsyncTask().httpExcute(HttpUtils.ROOT, HttpUtils.GET, new OnHttpListener() {
                @Override
                public void onSuccess(String data) {
                    Log.e("haha","成功"+data);
                }
                @Override
                public void onFaild() {}
            });

        }
    }
}
