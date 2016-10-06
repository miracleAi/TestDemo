package io.egg.jiantu.wechat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXImageObject;
import com.tencent.mm.sdk.openapi.WXMediaMessage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class WeChatEntity {
	public static final int TIMELINE_SUPPORTED_VERSION = 0x21020001;
	public static final String APP_ID = "wxd930ea5d5a258f4f";//wx12f3f5b32bf3a532
	//public static final String APP_KEY = "198c6ebe0b3af59683353a382ee4872b";
	private static Context mContext = null;

	static int mQuality;

	/*
	 * 监测微信版本
	 */
	public static boolean checkWechatVersion(Context context,IWXAPI m_wbchat_api) {

		int wxSdkVersion = m_wbchat_api.getWXAppSupportAPI();
		if (wxSdkVersion == 0) {
			//Toast.makeText(context, " 请先安装微信！", Toast.LENGTH_LONG).show();
			return false;
		}

		if (wxSdkVersion < TIMELINE_SUPPORTED_VERSION) {
			//Toast.makeText(context, " 版本过低，请先更新微信！", Toast.LENGTH_LONG).show();
			return false;
		}
		return true;
	}

	public static int sendToWechat(IWXAPI api, Context context, Bitmap bitmap, int requestType) {
        if (!checkWechatVersion(context, api)) {
            return 0;
        }

        WXImageObject imgObj = new WXImageObject(bitmap);

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;

        Bitmap thumbBmp = cutBitmap(mContext, bitmap);
        bitmap.recycle();

        msg.thumbData = Util.bmpToByteArray(thumbBmp, true, mQuality);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = msg;
        req.scene = requestType;

        api.sendReq(req);

        return 1;
    }

    private static String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
	}

	public static Bitmap cutBitmap(Context context, Bitmap bmp) {
		int THUMB_SIZE = 320;//输出图片变成320pixle 320
		Bitmap newbm = null;
		if (bmp == null)
			return newbm;

		ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
	    bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos1);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中

		int width = bmp.getWidth();
		int height = bmp.getHeight();

		float scaleWidth = ((float) THUMB_SIZE) / width;

		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleWidth);
		newbm = Bitmap.createBitmap(bmp, 0, 0, width, height, matrix, true);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    int options = 80;  //初始从90开始
		newbm.compress(Bitmap.CompressFormat.JPEG, options, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中

        while ( baos.toByteArray().length / 1024>32) {  //循环判断如果压缩后图片是否大于32kb,大于继续压缩
        	Log.d("","resize options ->"+baos.toByteArray().length/1024);
            baos.reset();//重置baos即清空baos
            newbm.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        mQuality = options;
        Log.d("","final options resize->"+baos.toByteArray().length/1024);
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap finalBitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片

		return finalBitmap;
	}
}
