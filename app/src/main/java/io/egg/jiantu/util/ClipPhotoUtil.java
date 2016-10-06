package io.egg.jiantu.util;

import java.io.File;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

public class ClipPhotoUtil {
	
	public static final int OPTION_CANCEL = 0;
	public static final int OPTION_CAMER = 1;
	public static final int OPTION_FILE = 2;
	public static final int OPTION_SHOW_HEADER = 3;
	private static final String TAG = ClipPhotoUtil.class.getSimpleName();
	
	private FragmentActivity activity;
	private CallbackHandler handler;
	
	private boolean mSave = true;
	private boolean mIsRegister = false;
	
	public static interface CallbackHandler {
		public void onBitmapReady(Bitmap bitmap);
		public void onUploadFinish(boolean result, String url, int errCode, String msg);
		public void onSaveFinish(boolean result, String url, int errCode, String msg);
		public void showHeader();
		public void resumePlay();
	}
	
	public void startChangeUserHeader(FragmentActivity activity, CallbackHandler handler, int type, boolean save) {
		this.activity = activity;
		this.handler = handler;
		this.mSave = save;
		
	}
	
	
	private void startPhotoZoom(Uri uri) {
		String fileName = CameraUtil.getPhotoFileName();
		SharedPreferences share = this.activity.getSharedPreferences(CameraUtil.PIC_ZOOM_OUTPUT_DB, 0);
		share.edit().putString(CameraUtil.PIC_ZOOM_OUTPUT_FILE_KEY2, fileName).commit();
		
		Uri imageUri = Uri.fromFile(new File(CameraUtil.IMAGE_FINAL_FILE_DIR, fileName));
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);// 裁剪框比例
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 135);// 输出图片大小
		intent.putExtra("outputY", 135);
		intent.putExtra("scale", true);
		intent.putExtra("return-data", false);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true); // no face detection
		
		try {
			this.activity.startActivityForResult(intent, CameraUtil.PHOTO_REQUEST_CUT_HEADER);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (this.activity == null || this.activity.isFinishing() || resultCode != -1) {
			return;
		}
		switch (requestCode) {
		case CameraUtil.PHOTO_REQUEST_TAKEPHOTO_HEADER:
			SharedPreferences share = this.activity.getSharedPreferences(CameraUtil.SHARED_PREFERENCES_NAME, 0);
			String fileName = share.getString(CameraUtil.SHARED_CAMERA_EXTRA_OUTPUT, "");
			File tempFile = new File(CameraUtil.IMAGE_ORIGI_FILE_DIR, fileName);
			startPhotoZoom(Uri.fromFile(tempFile));
			break;

		case CameraUtil.PHOTO_REQUEST_GALLERY_HEADER:
			if (data != null) {
				startPhotoZoom(data.getData());
			}
			break;

		case CameraUtil.PHOTO_REQUEST_CUT_HEADER:
			SharedPreferences shareTmp = this.activity.getSharedPreferences(CameraUtil.PIC_ZOOM_OUTPUT_DB, 0);
			String fileNameTmp = shareTmp.getString(CameraUtil.PIC_ZOOM_OUTPUT_FILE_KEY2, "");
			File file = new File(CameraUtil.IMAGE_FINAL_FILE_DIR, fileNameTmp);
			Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
			Log.d("","cut file");
			file.delete();
			handler.onBitmapReady(bitmap);
			break;
		}
	}
	

	/*@Override
	public void onCallback(int result) {
		switch (result) {
		case OPTION_CAMER:
			startCamearPicCut(); // 打开相机
			break;
		case OPTION_FILE:
			CameraUtil.startImageCaptrue(activity, CameraUtil.PHOTO_REQUEST_GALLERY_HEADER); // 打开图库
			break;
		case OPTION_SHOW_HEADER:
			this.handler.showHeader();
		case OPTION_CANCEL:
			
			break;
		default:
			break;
		}
		
	}*/
	

	private void startCamearPicCut() {
		
		
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra("camerasensortype", 2);
		intent.putExtra("autofocus", true);
		intent.putExtra("fullScreen", false);
		intent.putExtra("showActionIcons", false);

		String fileName = CameraUtil.getPhotoFileName();
		SharedPreferences share = this.activity.getSharedPreferences(
				CameraUtil.SHARED_PREFERENCES_NAME, 0);
		share.edit().putString(CameraUtil.SHARED_CAMERA_EXTRA_OUTPUT, fileName)
				.commit();
		File tempFile = new File(CameraUtil.IMAGE_ORIGI_FILE_DIR, fileName);

		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
		this.activity.startActivityForResult(intent, CameraUtil.PHOTO_REQUEST_TAKEPHOTO_HEADER);
	}

}
