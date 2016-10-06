package io.egg.jiantu.weibo;

import java.io.File;

import android.content.Context;
import android.graphics.Bitmap;


public class SharePicUtil {
	private static final String TAG = SharePicUtil.class.getSimpleName();

	/*
	 * path:相对路径
	 */
	public static final String HEAD = "share_";

	public static String getPictureSaveFile(Context context, String name) {
		String str = null;
//		try {
//			File file1 = CacheManager.getInstance().getDiscCache().get(name);
//			String path = file1.getAbsolutePath();
//			int m = path.lastIndexOf('/');
//			String sub = path.substring(0, m);
//			str = sub + "/" + name;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		return str;
	}

	public static String saveBitmap2file(Context context, Bitmap bmp, String path) {
//		String name = WeishiHttpClient.md5(path);
//		String filename = getPictureSaveFile(context, name);
//		FileUtil.saveBitmap2file(bmp, filename);
		return "filename";
	}

	/*
	 * path:相对路径
	 */
	public static String getFilePath(Context context, String path) {
		//String name = WeishiHttpClient.md5(path);
		String filename = getPictureSaveFile(context, "name");
		return filename;
	}

	/*
	 * path:相对路径
	 */
	public static boolean fileIsExists(Context context, String path) {
		File f = new File(getFilePath(context, path));
		if (!f.exists()) {
			return false;
		}
		return true;
	}
}
