package com.annotion.ruiyi.runelf;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.annotion.ruiyi.runelf.http.HttpResultBean;
import com.annotion.ruiyi.runelf.http.OnHttpListener;

/**
 * Function:
 * Project:RunElf
 * Date:2019/1/15
 * Created by xiaojun .
 */

public class HttpAsyncTask extends AsyncTask<String, String, HttpResultBean> {

    public static final String DOWN_ZIP = "downZip";
    public static final String DOWN_APK = "downAPK";

    private OnHttpListener listener;

    @Override
    protected HttpResultBean doInBackground(String... strings) {
        //第三个参数判断是否是下载zip
        if (strings.length > 2 && !TextUtils.isEmpty(strings[2])){
            if (strings[2].equals(DOWN_ZIP)){
                return HttpUtils.downloadZipAndUn(strings[0], strings[1]);
            }else if (strings[2].equals(DOWN_APK)){
                return HttpUtils.downloadApk(strings[0], strings[1]);
            }
        } else {
            return HttpUtils.getMessage(strings[0], strings[1]);
        }
        return null;
    }

    @Override
    protected void onPostExecute(HttpResultBean s) {
        super.onPostExecute(s);
        if (s.result){
            listener.onSuccess(s.data);
        }else {
            listener.onFaild();
        }
    }

    /**
     *
     * @param method   接口
     * @param httType   post or get
     */
    public void httpExcute(String method,String httType,OnHttpListener listener){
        this.listener = listener;
        execute(method,httType);
    }

    /**
     *
     * @param method   接口
     * @param downPath   解压文件的路径
     * @param downTag   下载类型
     */
    public void httpDownExcute(String method,String downPath,String downTag,OnHttpListener listener){
        this.listener = listener;
        execute(method,downPath,downTag);
    }
}
