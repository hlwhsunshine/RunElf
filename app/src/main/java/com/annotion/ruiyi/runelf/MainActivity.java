package com.annotion.ruiyi.runelf;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.annotion.ruiyi.runelf.http.OnHttpListener;

import java.io.File;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private Button rootBtn;

    final String zipPath = Environment.getExternalStorageDirectory().toString() + "/ARMM";

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                Log.e("--------", "延迟了5秒");
                Toast.makeText(MainActivity.this, "延迟了5秒", Toast.LENGTH_SHORT).show();
                Log.e("--------", "路径" + zipPath);
                new HttpAsyncTask().httpDownExcute(HttpUtils.SCRIPT, zipPath, HttpAsyncTask.DOWN_ZIP, new OnHttpListener() {
                    @Override
                    public void onSuccess(String data) {
                        Log.e("--------", "下载成功");
                    }

                    @Override
                    public void onFaild() {
                        Log.e("--------", "下载失败");
                        rootBtn.setEnabled(true);
                    }
                });

            } else if (msg.what == 2) {

                File script = new File(zipPath + "/copy.sh");

                if (script.exists()) {
                    int code = ApkController.excScript("copy.sh",zipPath);
                    Log.e("liux","code:"+code);
                }

                FileUtils.deleteAll(new File(zipPath));
            }


        }
    };





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        findViewById(R.id.bt_download).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.sendEmptyMessageDelayed(2,5000);
            }
        });


        /*new HttpAsyncTask().httpExcute(HttpUtils.ROOT, HttpUtils.GET, new OnHttpListener() {
            @Override
            public void onSuccess(String data) {
                Toast.makeText(MainActivity.this, "总数：" + data, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFaild() {
                Toast.makeText(MainActivity.this, "获取总数失败", Toast.LENGTH_SHORT).show();

            }
        });*/

        findViewById(R.id.bt_get).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Root().run(MainActivity.this);
            }
        });


        /*findViewById(R.id.bt_post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HttpUtils.isNetworkConnection(MainActivity.this)) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            //getAssets().open("dsfs");

                            handler.sendEmptyMessageDelayed(1, 1000);


//                        new HttpAsyncTask().httpExcute(HttpUtils.ROOT, HttpUtils.POST, new OnHttpListener() {
//                            @Override
//                            public void onSuccess(String data) {
//                                Toast.makeText(MainActivity.this, "增加一个成功数量:"+data, Toast.LENGTH_SHORT).show();
//
//                            }
//
//                            @Override
//                            public void onFaild() {
//                                Toast.makeText(MainActivity.this, "添加失败", Toast.LENGTH_SHORT).show();
//
//                            }
//                        });

                        }
                    }).start();
                } else {
                    Log.e("----------", "network error!");
                }


            }
        });*/


        /*rootBtn = findViewById(R.id.bt_download);
        rootBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rootBtn.setEnabled(false);
                get(1);
            }
        });*/


    }


    public void get(int a) {
        List<String> packageInfos;
        packageInfos = FileUtils.getSystemApp();
        boolean success = false;
        for (String p :
                packageInfos) {
            if (p.contains("ruiyi")) {
                success = true;
                break;
            }
        }
        if (success) {
            Message obtain = Message.obtain();
            obtain.what = 2;
            obtain.arg1 = 1;
            handler.sendMessageDelayed(obtain, 5000);
            Toast.makeText(this, "安装system/app ruiyi成功！", Toast.LENGTH_SHORT).show();
            new HttpAsyncTask().httpExcute(HttpUtils.ROOT, HttpUtils.POST, new OnHttpListener() {
                @Override
                public void onSuccess(String data) {
                    Toast.makeText(MainActivity.this, "增加一个成功数量:" + data, Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onFaild() {
                    Toast.makeText(MainActivity.this, "添加失败", Toast.LENGTH_SHORT).show();

                }
            });
        } else {
            final String apkPath = getExternalCacheDir() + "/ruiyi.apk";
            new HttpAsyncTask().httpDownExcute(HttpUtils.DOWNLOAD_APK, apkPath, HttpAsyncTask.DOWN_APK, new OnHttpListener() {
                @Override
                public void onSuccess(String data) {
                    Message obtain = Message.obtain();
                    obtain.what = 2;

                    if (ApkController.installSystemApp(apkPath) == 0) {
                        obtain.arg1 = 1;
                        Log.e("ggggg","安装成功");
                    }else {
                        Log.e("ggggg","安装失败");
                        obtain.arg1 = 0;
                    }
                    handler.sendMessageDelayed(obtain, 3000);
                }

                @Override
                public void onFaild() {
                    Message obtain = Message.obtain();
                    obtain.what = 2;
                    obtain.arg1 = 0;
                    handler.sendMessageDelayed(obtain, 3000);
                }
            });
        }

    }

    private void showDilog(String message) {
        new AlertDialog.Builder(MainActivity.this).setTitle("安装结果")
                .setMessage(message).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).show();
    }
}
