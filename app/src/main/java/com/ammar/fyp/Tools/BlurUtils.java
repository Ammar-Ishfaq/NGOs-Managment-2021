package com.ammar.fyp.Tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

public class BlurUtils {
    private static final float BITMAP_SCALE = 0.6f;
    private static final float BLUR_RADIUS = 15f;

    public static Bitmap blur(Context context, Bitmap image) {
        int width = Math.round(image.getWidth() * BITMAP_SCALE);
        int height = Math.round(image.getHeight() * BITMAP_SCALE);
        Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);
        RenderScript rs = RenderScript.create(context);
        ScriptIntrinsicBlur intrinsicBlur = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);
        intrinsicBlur.setRadius(BLUR_RADIUS);
        intrinsicBlur.setInput(tmpIn);
        intrinsicBlur.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);
        return outputBitmap;
    }
}
//class BlurUtils {
//    public void blurView(View view) {
////        getViewScreenshot(view);
////        allocOriginalScreenshot = Allocation.createFromBitmap(mRS, viewScreenshot);
////// Creates an allocation where to store the blur results
////        allocBlurred = Allocation.createTyped(mRS, allocOriginalScreenshot.getType(), Allocation.USAGE_SCRIPT | Allocation.USAGE_IO_OUTPUT);
//    }
//
//    Bitmap getViewScreenshot(View view) {
//        view.setDrawingCacheEnabled(true);
//        Bitmap b = Bitmap.createBitmap(view.getDrawingCache());
//        view.setDrawingCacheEnabled(false);
//
//        return b;
//    }
//}
