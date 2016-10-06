package io.egg.jiantu.weibo;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;


public class SharePic {
	private static final String TAG = SharePic.class.getSimpleName();

	
	
	public static boolean isAvilible(Context context, String packageName){ 
		final PackageManager packageManager = context.getPackageManager();//获取packagemanager 
		List< PackageInfo> pinfo = packageManager.getInstalledPackages(0);//获取所有已安装程序的包信息 
		List<String> pName = new ArrayList<String>();//用于存储所有已安装程序的包名 
		//从pinfo中将包名字逐一取出，压入pName list中 
		if(pinfo != null){ 
		for(int i = 0; i < pinfo.size(); i++){ 
		String pn = pinfo.get(i).packageName; 
		pName.add(pn); 
		} 
		} 
		return pName.contains(packageName);//判断pName中是否有目标程序的包名，有TRUE，没有FALSE 
	} 
	
	
	@SuppressLint("NewApi")
	public static Bitmap getBitmap(Context context, String url, boolean isNeedSave) {
//		if (StringUtils.isBlank(url)) {
//			return null;
//		}
//		if (SharePicUtil.fileIsExists(context, url)) {
//			return BitmapFactory.decodeFile(SharePicUtil.getFilePath(context, url));
//		}

		Bitmap bmp = null;
//		if (url != null) {
//			if (url.startsWith("/storage")) {
//				bmp = BottomOp.loadBitmapFromFile(url);
//			} else {
//				bmp = BottomOp.loadBitmapFromImageLoader(url);
//			}
//
//			if (bmp == null) {
//				InputStream stream = null;
//				try {
//					StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads()
//							.detectDiskWrites().detectNetwork()
//							.penaltyLog().build());
//					StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects()
//							.detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
//					stream = new URL(url).openStream();
//					bmp = BitmapFactory.decodeStream(stream);
//				} catch (Exception e) {
//					Log.e(TAG, "getBitmap:" + e.toString());
//				} finally {
//					if (stream != null) {
//						try {
//							stream.close();
//						} catch (IOException e) {
//							e.printStackTrace();
//						}
//					}
//				}
//			}
//		}
//
//		if (isNeedSave && bmp != null) {
//			saveBitmap(context, url, bmp);
//		}
//
//		if (bmp == null) {
//			bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.pic_head_default_160);
//		}

		return bmp;
	}

	@SuppressLint("NewApi")
	public static String saveBitmap(Context context, String url, Bitmap bitmap) {
		if (SharePicUtil.fileIsExists(context, url)) {
			return SharePicUtil.getFilePath(context, url);
		}

		String name = null;
		if (!SharePicUtil.fileIsExists(context, url) && bitmap != null) {
			name = SharePicUtil.saveBitmap2file(context, bitmap, url);
		}
		return name;
	}

	public static String getAndSave(Context context, String url) {
		if (SharePicUtil.fileIsExists(context, url)) {
			return SharePicUtil.getFilePath(context, url);
		}
		String path = "";
		Bitmap mp = getBitmap(context, url, false);
		if (mp != null) {
			path = saveBitmap(context, url, mp);
			if (!mp.isRecycled()) {
				mp.recycle();
				mp = null;
			}
		}
		return path;
	}

	private static String mergerBitmap(Context context, String url, Bitmap bitmap) {
		Bitmap first = bitmap;
		if (first == null) {
			first = getBitmap(context, url, false);
		}

		Bitmap second = null;
		String tail = "";
		if (second == null) {
			//tail = WeishiConsole.getSingleton(context).getTailPic();
			if (tail != null && tail.length() > 0) {
				second = getBitmap(context, tail, true);
			}
		}

		String name = null;
		if (first == null && second == null) {
			return null;
		} else if (first != null && second != null) {
			Bitmap bmp = null;//CommonDefine.add2Bitmap(first, second);
			if (bmp != null) {
				name = saveBitmap(context, tail + url, bmp);
			}
		}
		return name;
	}

}
