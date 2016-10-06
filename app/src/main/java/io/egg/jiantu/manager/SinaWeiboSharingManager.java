package io.egg.jiantu.manager;

import android.content.Context;
import android.graphics.Bitmap;

import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;

/**
 * Created by apolloyujl on 14-6-30.
 */
public class SinaWeiboSharingManager {

    private static IWeiboShareAPI mWeiboShareAPI = null;

    public static void init(Context context) {
        // 创建微博分享接口实例
        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(context, "898971157");// 2045436852
        // 注册第三方应用到微博客户端中，注册成功后该应用将显示在微博的应用列表中。
        // 但该附件栏集成分享权限需要合作申请，详情请查看 Demo 提示
        // NOTE：请务必提前注册，即界面初始化的时候或是应用程序初始化时，进行注册
        if (mWeiboShareAPI.isWeiboAppInstalled()) {
            mWeiboShareAPI.registerApp();
        }
    }

    public static boolean isWeiboAppInstalled() {
        return mWeiboShareAPI.isWeiboAppInstalled();
    }

    public static void send(String text, Bitmap bitmap) {
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();//初始化微博的分享消息 if (hasText) {

        weiboMessage.textObject = new TextObject();
        weiboMessage.textObject.text = text + " 发自@简图app";

        weiboMessage.imageObject = new ImageObject();
        weiboMessage.imageObject.setImageObject(bitmap);

        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.multiMessage = weiboMessage;

        mWeiboShareAPI.sendRequest(request); //发送请求消息到微博,唤起微博分享界面
    }
}
