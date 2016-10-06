package io.egg.jiantu.util;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by apolloyujl on 14-7-1.
 */
public class ImageUtil {

    public static final int THUMB_SIZE_640 = 640;

    public static final int THUMB_SIZE_320 = 320;

    public static byte[] bmpToByteArray(final Bitmap bmp) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        bmp.recycle();

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static void startPhotoZoom(Uri uri, Fragment fragment) {
        String fileName = CameraUtil.getPhotoFileName();

        SharedPreferences share = fragment.getActivity().getSharedPreferences(CameraUtil.PIC_ZOOM_OUTPUT_DB, 0);
        share.edit().putString(CameraUtil.PIC_ZOOM_OUTPUT_FILE_KEY2, fileName).commit();

        int targetImageWidth = fragment.getResources().getDisplayMetrics().widthPixels;

        Uri imageUri = Uri.fromFile(new File(CameraUtil.IMAGE_FINAL_FILE_DIR, fileName));
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);// 裁剪框比例
        intent.putExtra("aspectY", 1);

        intent.putExtra("outputX", targetImageWidth);//targetImageWidth
        intent.putExtra("outputY", targetImageWidth);
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection

        try {
            fragment.startActivityForResult(intent, CameraUtil.PHOTO_REQUEST_CUT_HEADER);
        } catch (Exception e) {
            Debug.e(e);
        }
    }

    public static Bitmap resize(Bitmap bmp, int compressSize) {
        return resize(bmp, compressSize, 100);
    }

    public static Bitmap resize(Bitmap bmp, int compressSize, int compressQuality) {
        if (bmp == null) {
            return null;
        }

        return Bitmap.createScaledBitmap(bmp, compressSize, compressSize, true);
    }

    public static void scanPhotos(String filePath, Context context) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(new File(filePath));
        intent.setData(uri);
        context.sendBroadcast(intent);

        MediaScannerConnection.scanFile(context, new String[]{filePath}, null, null);
    }

    public static Bitmap rotate(Bitmap bitmap, int degree) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        Matrix mtx = new Matrix();
        mtx.postRotate(degree);

        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
    }

    public static boolean saveBitmap2file(Bitmap bmp, String filename) {
        if (filename == null) {
            return false;
        }

        OutputStream fileOutputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = null;

        try {
            fileOutputStream = new FileOutputStream(filename);
            byteArrayOutputStream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream); // bm is the bitmap object
            byte[] bsResized = byteArrayOutputStream.toByteArray();
            fileOutputStream.write(bsResized);
        } catch (IOException e) {
            Debug.e(e);
            return false;
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
                if (byteArrayOutputStream != null) {
                    byteArrayOutputStream.close();
                }
            } catch (IOException e) {
                Debug.e(e);
            }
        }

        return true;
    }

    public static String getAnUniqueName() {
        Calendar calendar = Calendar.getInstance();
        String filename = String.format(Locale.getDefault(), "PID_%d%02d%02d_%02d%02d%02d.jpg", calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + (1 - Calendar.JANUARY), calendar.get(Calendar.DATE), calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
        return filename;//jpg
    }

    public static String saveBitmap2file(Context context, Bitmap bmp) {
        String filename = getPictureSavePath(context) + "/" + getAnUniqueName();

        saveBitmap2file(bmp, filename);

        return filename;
    }

    public static String getPictureSavePath(Context context) {
        File dir;
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            dir = new File(android.os.Environment.getExternalStorageDirectory(), "jiantu/简图/");//test/imageeditor"
        else
            dir = context.getCacheDir();
        if (!dir.exists())
            dir.mkdirs();
        return dir.getAbsolutePath();
    }

    public static Bitmap getBitmapFromView(Context context, View rootView) {
        int canvasWidth = context.getResources().getDisplayMetrics().widthPixels;
        int canvasHeight = canvasWidth;

        Bitmap bitmap = Bitmap.createBitmap(canvasWidth, canvasHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        Drawable bgDrawable = rootView.getBackground();
        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        }

        rootView.draw(canvas);

        return bitmap;
    }
}
