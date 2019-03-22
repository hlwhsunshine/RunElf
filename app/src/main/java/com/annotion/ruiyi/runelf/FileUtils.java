package com.annotion.ruiyi.runelf;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


/**
 * Function:
 * Project:RunElf
 * Date:2019/1/15
 * Created by xiaojun .
 */

public class FileUtils {

    public static void UnZipFolder(InputStream zipInputStream, String outPathString) throws Exception {
        ZipInputStream inZip = new ZipInputStream(zipInputStream);
        ZipEntry zipEntry;
        String  szName = "";
        while ((zipEntry = inZip.getNextEntry()) != null) {
            szName = zipEntry.getName();
            if (zipEntry.isDirectory()) {
                //获取部件的文件夹名
                szName = szName.substring(0, szName.length() - 1);
                File folder = new File(outPathString + File.separator + szName);
                folder.mkdirs();
            } else {
                File file = new File(outPathString + File.separator + szName);
                if (!file.exists()){
                    file.getParentFile().mkdirs();
                    file.createNewFile();
                }
                // 获取文件的输出流
                FileOutputStream out = new FileOutputStream(file);
                int len;
                byte[] buffer = new byte[1024];
                // 读取（字节）字节到缓冲区
                while ((len = inZip.read(buffer)) != -1) {
                    // 从缓冲区（0）位置写入（字节）字节
                    out.write(buffer, 0, len);
                    out.flush();
                }
                out.close();
            }
        }
        inZip.close();
    }

    public static void deleteAll(File file) {

        if (file.isFile() || file.list().length == 0) {
            file.delete();
        } else {
            for (File f : file.listFiles()) {
                deleteAll(f); // 递归删除每一个文件

            }
            file.delete(); // 删除文件夹
        }
    }

    public static List<PackageInfo> getSystemApp(Context context){

        List<String> systemApps = new ArrayList<String>();
        List<PackageInfo> packageInfos = context.getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packageInfos.size(); i++) {
            PackageInfo packageInfo = packageInfos.get(i);
            String packageName = packageInfo.packageName;
            ApplicationInfo appInfo = packageInfo.applicationInfo;
            if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                systemApps.add(packageName);
            }
        }
        return packageInfos;
    }

    public static List<String> getSystemApp(){
        File[] file = new File("/system/app").listFiles();
        ArrayList<String> strings = new ArrayList<>();
        for (int i = 0; i < file.length; i++) {
            strings.add(file[i].getName());
        }
        return strings;
    }

}
