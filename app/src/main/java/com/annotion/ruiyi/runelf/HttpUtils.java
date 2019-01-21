package com.annotion.ruiyi.runelf;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.annotion.ruiyi.runelf.http.HttpResultBean;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Function:
 * Project:RunElf
 * Date:2019/1/14
 * Created by xiaojun .
 */

public class HttpUtils {
    private static final String base = "http://123.183.211.187:8009";

    //以post方式请求，使数量+1，以get方式请求获取数量
    public static final String ROOT = "/root";
    public static final String SCRIPT = "/scripts.zip";
    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String DOWNLOAD_APK = "/ruiyi0.0.1.apk";

    public static HttpResultBean getMessage(final String method, final String httpType) {
        StringBuilder result = new StringBuilder();
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        HttpResultBean resultBean = new HttpResultBean();
        try {
            URL url = new URL(base + method);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(httpType.endsWith(GET) ? GET : POST);
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() == 200) {
                reader = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                resultBean.result = true;
                resultBean.data = result.toString();
            } else {
                resultBean.result = false;
            }

        } catch (Exception e) {
            resultBean.result = false;
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
        return resultBean;

    }

    public static HttpResultBean downloadZipAndUn(final String method, final String loadPath) {
        HttpResultBean resultBean = new HttpResultBean();
        HttpURLConnection connection = null;
        try {
            URL url = new URL(base + method);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(GET);
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() == 200) {
                FileUtils.UnZipFolder(in, loadPath);
                in.close();
                resultBean.result = true;
                resultBean.data = "下载解压成功";
            }
        } catch (Exception e) {
            resultBean.result = false;
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return resultBean;
    }


    public static HttpResultBean downloadApk(final String method, final String apkPath) {
        HttpResultBean resultBean = new HttpResultBean();
        HttpURLConnection connection = null;
        try {
            URL url = new URL(base + method);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(GET);
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() == 200) {
                File file = new File(apkPath);
                FileOutputStream output = new FileOutputStream(file);
                byte[] buffer = new byte[2048];
                int count;
                while ((count = in.read(buffer)) != -1) {
                    output.write(buffer, 0, count);
                }
                output.flush();
                in.close();
                output.close();
                resultBean.result = true;
                resultBean.data = "下载apk成功";
            }
        } catch (Exception e) {
            resultBean.result = false;
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return resultBean;
    }


    public static boolean isNetworkConnection(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo != null && activeNetworkInfo.isAvailable()) {
                if (activeNetworkInfo.getDetailedState() == NetworkInfo.DetailedState.CONNECTED) {
                    return true;
                }
                new StringBuilder("network is available. but connect state is :").append(activeNetworkInfo.getDetailedState());
            }
        }
        return false;
    }
}
