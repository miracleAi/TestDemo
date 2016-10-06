package io.egg.jiantu.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Locale;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;


public class FileUtil {
	private static final String TAG = FileUtil.class.getSimpleName();

    public static String saveToSystemPhotoAlbumSavePath(Context context, Bitmap bitmap) {

		ContentResolver cr = context.getContentResolver();
		String uri = MediaStore.Images.Media.insertImage(cr, bitmap, "target.png", "save pic");
		Log.d("","pic save-->"+uri);
		Uri targetUri = Uri.parse(uri);
		String[] filePath = {MediaStore.Images.Media.DATA};
		String myPath = "";
		Cursor cursor = null;
		try {
			cursor = context.getContentResolver().query(targetUri, filePath, null, null, null);
			if (cursor != null) {
				cursor.moveToFirst();
				int columnIndex = cursor.getColumnIndex(filePath[0]);
				myPath = cursor.getString(columnIndex);
			} else {
			}
		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		Log.d("","pic save path-->"+myPath);
		//Toast.makeText(context, "保存图片到"+url, 2000).show();
		return myPath;
	}
	
	public static String getNewApkFilePath(Context context, String code, String name) {
		File updateDir = null;
		File updateFile = null;

		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			updateDir = new File(android.os.Environment.getExternalStorageDirectory(), "Android/data/" + context.getPackageName() + "/apk/" + code);
		} else {
			updateDir = context.getCacheDir();
		}

		updateFile = new File(updateDir, name + ".apk");

		if (!updateDir.exists()) {
			updateDir.mkdirs();
		}

		Log.d(TAG, "File path: " + updateFile.getAbsolutePath());

		return updateFile.getAbsolutePath();
	}

	public static String createNewApkFile(Context context, String code, String name) {
		String path = getNewApkFilePath(context, code, name);

		Log.d(TAG, "createFile Delete: " + path);
		
		File updateFile = new File(path);
		updateFile.delete();

		try {
			updateFile.createNewFile();
		} catch (IOException e) {
			Log.e(TAG, e.toString());

			return null;
		}

		return updateFile.getAbsolutePath();
	}
	
	public static boolean existFile(String path){
		if (TextUtils.isEmpty(path)) {
			return false;
		}
		if (new File(path).exists()) {
			return true;
		}
		return false;
	}
}
