package io.egg.jiantu.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.hardware.Camera;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.widget.Toast;

import io.egg.jiantu.CameraSizeComparator;

public class CameraUtil {
	public static final String TAG = "CameraUtil";
	
	public static final String SHARED_PREFERENCES_NAME = "SAILFISH_SHARED_PREFERENCES";
	public static final String PIC_ZOOM_OUTPUT_DB = "PIC_ZOOM_OUTPUT_DB";
	public static final String PIC_ZOOM_OUTPUT_FILE_KEY = "PIC_ZOOM_OUTPUT_FILE_KEY";
	public static final String PIC_ZOOM_OUTPUT_FILE_KEY2 = "PIC_ZOOM_OUTPUT_FILE_KEY2";
	public static final String PIC_ZOOM_OUTPUT_FILE_KEY3 = "PIC_ZOOM_OUTPUT_FILE_KEY3";
	public static final String SHARED_CAMERA_EXTRA_OUTPUT = "SHARED_CAMERA_EXTRA_OUTPUT";
	
	public static final int PHOTO_REQUEST_TAKEPHOTO = 1;
	public static final int PHOTO_REQUEST_GALLERY = 2;
	public static final int PHOTO_REQUEST_CUT = 3;
	
	public static final int PHOTO_REQUEST_TAKEPHOTO_HEADER = 4;
	public static final int PHOTO_REQUEST_GALLERY_HEADER = 5;
	public static final int PHOTO_REQUEST_CUT_HEADER = 6;
	
	public static final String STORAGE_DIR = Environment.getExternalStorageDirectory().getAbsolutePath();
	public static final String SAILFISH_DIR = STORAGE_DIR+"/jiantu";
	public static final File IMAGE_CROP_TEMP_FILE = new File(SAILFISH_DIR, "crop_temp.jpg");
	public static final File IMAGE_ORIGI_FILE_DIR = new File(SAILFISH_DIR + "/picture_original");
	public static final File IMAGE_FINAL_FILE_DIR = new File(SAILFISH_DIR + "/picture_final");
	
	static {
		if (!IMAGE_ORIGI_FILE_DIR.exists()) {
			IMAGE_ORIGI_FILE_DIR.mkdirs();
		}
		
		if (!IMAGE_FINAL_FILE_DIR.exists()) {
			IMAGE_FINAL_FILE_DIR.mkdirs();
		}
	}

    public static String getPhotoFileName() {  
        Date date = new Date(System.currentTimeMillis());  
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");  
        return dateFormat.format(date) + ".jpg";  
    } 
    
  	public static void saveBitmap(Bitmap bitmap, File imgFile){
  		FileOutputStream bos = null;
  		try {
  			bos = new FileOutputStream(imgFile);
  			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
  		} catch (FileNotFoundException e) {
  			e.printStackTrace();
  		}finally{
  			try {
  				bos.flush();
  				bos.close();
  			} catch (IOException e) {
  				e.printStackTrace();
  			}
  		}
  	}
  	
  	public static void startImageCapture(Activity activity) {
		try {
	        Intent intent = new Intent();
	        intent.setType("image/*");
	        intent.setAction(Intent.ACTION_GET_CONTENT);
	        activity.startActivityForResult(intent, CameraUtil.PHOTO_REQUEST_GALLERY);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
  	
  	public static void startImageCaptrue(Activity activity, int requestCode) {
		try {
	        Intent intent = new Intent();
	        intent.setType("image/*");
	        intent.setAction(Intent.ACTION_GET_CONTENT);
	        activity.startActivityForResult(intent, requestCode);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}


    public static Camera.Size findBestPictureSize(Context context, Camera mCamera, Camera.Parameters parameters) {
        Camera.Size originalSize = parameters.getPictureSize();
        if (originalSize.width >= 480 && originalSize.width <= 1920) {
            return null;
        }

        List<Camera.Size> sizeList = parameters.getSupportedPictureSizes();
        if (sizeList == null || sizeList.size() == 0) {
            return null;
        }
        Collections.sort(sizeList, new CameraSizeComparator());
        Collections.reverse(sizeList);

        float defaultRatio = 0.75f;

        for (Camera.Size size : sizeList) {
            if (size.width >= 640 && size.width <= 1920) {
                float ratio = size.height * 1.0f / size.width;
                if (ratio > defaultRatio - 0.05f && ratio < defaultRatio + 0.05f) {
                    return size;
                }
            }
        }

        return null;
    }

    public static Camera.Size findBestPreviewSize(Context context, Camera mCamera, Camera.Parameters parameters) {
        Pattern COMMA_PATTERN = Pattern.compile(",");

        String previewSizeValueString = null;
        int diff = Integer.MAX_VALUE;
        previewSizeValueString = parameters.get("preview-size-values");

        if (previewSizeValueString == null) {
            previewSizeValueString = parameters.get("preview-size-value");
        }

        if (previewSizeValueString == null) {  // 有些手机例如m9获取不到支持的预览大小   就直接返回屏幕大小
            return mCamera.new Size(getScreenWH(context).widthPixels, getScreenWH(context).heightPixels);
        }
        Debug.d("tag", "previewSizeValueString : " + previewSizeValueString);
        int bestX = 0;
        int bestY = 0;

        for (String prewsizeString : COMMA_PATTERN.split(previewSizeValueString)) {
            prewsizeString = prewsizeString.trim();

            int dimPosition = prewsizeString.indexOf('x');
            if (dimPosition == -1) {
                Debug.e("Bad prewsizeString:" + prewsizeString);
                continue;
            }

            int newX = 0;
            int newY = 0;

            try {
                newX = Integer.parseInt(prewsizeString.substring(0, dimPosition));
                newY = Integer.parseInt(prewsizeString.substring(dimPosition + 1));
            } catch (NumberFormatException e) {
                Debug.e("Bad prewsizeString:" + prewsizeString);
                continue;
            }

            Point screenResolution = new Point(getScreenWH(context).widthPixels, getScreenWH(context).heightPixels);

            int newDiff = Math.abs(newX - screenResolution.x) + Math.abs(newY - screenResolution.y);

            if (newDiff == diff) {
                bestX = newX;
                bestY = newY;
                break;
            } else if (newDiff < diff) {
                if ((3 * newX) == (4 * newY)) {
                    bestX = newX;
                    bestY = newY;
                    diff = newDiff;
                }
            }
        }
        if (bestX > 0 && bestY > 0) {
            return mCamera.new Size(bestX, bestY);
        }

        return null;
    }

    public static DisplayMetrics getScreenWH(Context context) {
        return context.getResources().getDisplayMetrics();
    }
}
