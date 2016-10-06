package io.egg.jiantu.manager;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;

import io.egg.jiantu.util.Debug;
import io.egg.jiantu.util.ImageUtil;

/**
 * Created by apolloyujl on 14-6-30.
 */
public class ImageManager {

    private ImageSaveListener listener;

    public String saveViewToSDCard(Context context, View rootView) {
        if (listener != null) {
            listener.onSaveStarted();
        }

        Bitmap bitmap = ImageUtil.getBitmapFromView(context, rootView);

        Bitmap finalBitmap = ImageUtil.resize(bitmap, ImageUtil.THUMB_SIZE_640);
        String path = ImageUtil.saveBitmap2file(context, finalBitmap);

        try {
            ImageUtil.scanPhotos(path, context);
        } catch (Exception e) {
            Debug.e(e);
        }

        if (listener != null) {
            listener.onSaveFinished();
        }

        return path;
    }

    public void setImageSaveListener(ImageSaveListener listener) {
        this.listener = listener;
    }

    public interface ImageSaveListener {
        void onSaveStarted();

        void onSaveFinished();
    }

}
