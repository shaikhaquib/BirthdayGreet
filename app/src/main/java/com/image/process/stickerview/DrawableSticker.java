package com.image.process.stickerview;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;


//import android.support.annotation.IntRange;
//import android.support.annotation.NonNull;

/**
 * @author wupanjie
 */
public class DrawableSticker extends Sticker {

  private Drawable drawable;
  private Rect realBounds;

  public DrawableSticker(Drawable drawable) {

    if (drawable!=null) {
      this.drawable = drawable;
      Log.d("ondraw","DrawableSticker called");
      realBounds = new Rect(0, 0, getWidth(), getHeight());    }else {
      realBounds = new Rect(0, 0, 0, 0);
    }
  }

  @NonNull
  @Override
  public Drawable getDrawable() {
    return drawable;
  }

  @Override
  public DrawableSticker setDrawable(@NonNull Drawable drawable) {
    this.drawable = drawable;
    return this;
  }

  @Override
  public void draw(@NonNull Canvas canvas) {
    canvas.save();

    if (drawable!=null) {
      canvas.concat(getMatrix());
      drawable.setBounds(realBounds);

      drawable.draw(canvas);
      canvas.restore();
    }
  }

  @NonNull @Override
  public DrawableSticker setAlpha(@IntRange(from = 0, to = 255) int alpha) {
    drawable.setAlpha(alpha);
    return this;
  }

  @Override
  public int getWidth() {
    if(drawable!=null) {
      return drawable.getIntrinsicWidth();
    }else {
      return 0;
    }
  }

  @Override
  public int getHeight() {
    if (drawable!=null) {
      return drawable.getIntrinsicHeight();
    }else {
      return 0;
    }
  }

  @Override
  public void release() {
    super.release();
    if (drawable != null) {
      drawable = null;
    }
  }
}
