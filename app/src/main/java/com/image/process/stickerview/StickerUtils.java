package com.image.process.stickerview;

import static java.lang.Math.round;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

//import android.support.annotation.NonNull;

/**
 * @author wupanjie
 */
class StickerUtils {
  private static final String TAG = "StickerView";





  public static File saveImageToGallery(@NonNull File file, @NonNull Bitmap bmp) {
  //  Log.d(TAG,bmp.toString());
    if (bmp == null) {
      throw new IllegalArgumentException("bmp should not be null");
    }
    try {

      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      bmp.compress(Bitmap.CompressFormat.PNG, 100 , bos);
      byte[] bitmapdata = bos.toByteArray();



      InputStream is = new ByteArrayInputStream(bitmapdata);

     // InputStream is = bs;
    //  Log.d(TAG, "on do in background, url get input stream");
      BufferedInputStream bis = new BufferedInputStream(is);
    //  Log.d(TAG, "on do in background, create buffered input stream");

      ByteArrayOutputStream baos = new ByteArrayOutputStream();
    //  Log.d(TAG, "on do in background, create buffered array output stream");

      byte[] img = new byte[1024];

      int current = 0;

     // Log.d(TAG, "on do in background, write byte to baos");
      while ((current = bis.read()) != -1) {
     //  Bitmap bmp1 = BitmapFactory.decodeStream(is);
//      // Log.d("bitmap_converted",bmp1.toString());


        baos.write(current);
      }

     // Log.d(TAG, "on do in background, done write");

     // Log.d(TAG, "on do in background, create fos");
      FileOutputStream fos = new FileOutputStream(file);

      fos.write(baos.toByteArray());

     // Log.d(TAG, "on do in background, write to fos");
      fos.flush();

      fos.close();
      is.close();

      Log.e(TAG, "saveImageToGallery: the path of bmp is " + file.getAbsolutePath());
    } catch (IOException e) {
      e.printStackTrace();
      Log.d(TAG,"error :"+bmp.toString());
    }


    return file;
  }





  public static void notifySystemGallery(@NonNull Context context, @NonNull File file) {
    if (file == null || !file.exists()) {
      throw new IllegalArgumentException("bmp should not be null");
    }

    try {
      MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(),
          file.getName(), null);
    } catch (FileNotFoundException e) {
      throw new IllegalStateException("File couldn't be found");
    }
    context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
  }

  @NonNull public static RectF trapToRect(@NonNull float[] array) {
    RectF r = new RectF();
    trapToRect(r, array);
    return r;
  }

  public static void trapToRect(@NonNull RectF r, @NonNull float[] array) {
    r.set(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY,
        Float.NEGATIVE_INFINITY);
    for (int i = 1; i < array.length; i += 2) {
      float x = round(array[i - 1] * 10) / 10.f;
      float y = round(array[i] * 10) / 10.f;


      r.left = (x < r.left) ? x : r.left;
      r.top = (y < r.top) ? y : r.top;
      r.right = (x > r.right) ? x : r.right;
      r.bottom = (y > r.bottom) ? y : r.bottom;



    }
    r.sort();
  }


}
