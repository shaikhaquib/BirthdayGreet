package com.image.process.stickerview;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by snowbean on 16-8-5.
 */
public class FileUtil {
  private static final String TAG = "FileUtil";

  public static String getFolderName(String name) {
    File mediaStorageDir =
            new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    name);

    if (!mediaStorageDir.exists()) {
      if (!mediaStorageDir.mkdirs()) {
        return "";
      }
    }
    return mediaStorageDir.getAbsolutePath();
  }

  /**
   * 判断sd卡是否可以用
   */
  private static boolean isSDAvailable() {
    return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
  }

  public static File getNewFile(Context context, String folderName) {

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);

    String timeStamp = simpleDateFormat.format(new Date());

    String path;
    if (isSDAvailable()) {
      Log.d("save_filename: ",getFolderName(folderName)+" "+timeStamp+" saved");
      path = getFolderName(folderName) + File.separator +"Digital"+timeStamp + ".jpg";
    } else {
      path = context.getFilesDir().getPath() + File.separator +"Digital"+timeStamp + ".jpg";
    }




    if (TextUtils.isEmpty(path)) {
      return null;
    }

    return new File(path);
  }


}
