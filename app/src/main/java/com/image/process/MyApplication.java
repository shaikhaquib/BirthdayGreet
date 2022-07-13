package com.image.process;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import java.io.File;

public class MyApplication extends Application {
    //private static MyApplication instance;
    @Override
    public void onCreate() {
        super.onCreate();
        //instance = this;
    }

    public MyApplication(Context mycontext) {
        clearApplicationData(mycontext);
        deleteCache(mycontext);
    }

    /*public static MyApplication getInstance(){
            return instance;
        }*/
    public void clearApplicationData(Context mycontext) {
        File cache = mycontext.getCacheDir();
        File appDir = new File(cache.getParent());
        if(appDir.exists()){
            String[] children = appDir.list();
            for(String s : children){
                if(!s.equals("lib")){
                    deleteDir(new File(appDir, s));
                    Log.i("TAG", "File /data/data/APP_PACKAGE/" + s +" DELETED ");
                }
            }
        }
    }
    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    //
    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir2(dir);
        } catch (Exception e) { e.printStackTrace();}
    }

    public static boolean deleteDir2(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir2(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }
}
