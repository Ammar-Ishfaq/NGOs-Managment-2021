package com.ammar.fyp.Tools;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.ammar.fyp.R;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.ByteArrayOutputStream;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

public class ImageUtils {
    /**
     * Will get the image from image view
     *
     * @param imageView
     * @return
     */
    private byte[] getByteArray(ImageView imageView) {
        // Get the data from an ImageView as bytes
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = imageView.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        return data;
    }

    public void enablePopUpZoom(Context context, ImageView imageView, String url) {
        imageView.setOnClickListener(v -> {
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View mView = inflater.inflate(R.layout.dialog_zoom_img_helper, null);
            PhotoView photoView = mView.findViewById(R.id.imageView);
            Glide.with(context)
                    .load(url)
                    .into(photoView);
            mBuilder.setView(mView);
            AlertDialog mDialog = mBuilder.create();
            mDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            mDialog.show();
        });
    }

    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    public Bitmap getResizedBitmap(Context context, String url, int newWidth, int newHeight) {
        Glide.with(context)
                .asBitmap()
                .load(url)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap bm, @Nullable Transition<? super Bitmap> transition) {
                        int width = bm.getWidth();
                        int height = bm.getHeight();
                        float scaleWidth = ((float) newWidth) / width;
                        float scaleHeight = ((float) newHeight) / height;
                        // CREATE A MATRIX FOR THE MANIPULATION
                        Matrix matrix = new Matrix();
                        // RESIZE THE BIT MAP
                        matrix.postScale(scaleWidth, scaleHeight);

                        // "RECREATE" THE NEW BITMAP
                        Bitmap resizedBitmap = Bitmap.createBitmap(
                                bm, 0, 0, width, height, matrix, false);
                        bm.recycle();
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });

        return null;
    }


    public static Bitmap loadBitmapFromView(View v) {
        Bitmap b = Bitmap.createBitmap(v.getLayoutParams().width, v.getLayoutParams().height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
        v.draw(c);
        return b;
    }

    /**
     * Get bitmap of a view
     *
     * @param view source view
     * @return generated bitmap object
     */
    public static Bitmap getBitmapFromView(View view) {
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.layout(0, 0, view.getWidth(), view.getHeight());
        Log.d("", "combineImages: width: " + view.getWidth());
        Log.d("", "combineImages: height: " + view.getHeight());
        view.draw(canvas);
        return bitmap;
    }

}
