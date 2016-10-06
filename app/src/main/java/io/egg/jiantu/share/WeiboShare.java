package io.egg.jiantu.share;

import android.content.Context;
import android.content.Intent;

import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;

/**
 * Created by apolloyujl on 14-6-22.
 */
public class WeiboShare {

    private static IWeiboShareAPI mWeiboShareAPI = null;

    public static void initWeiboShareInOnCreate(Context pContext) {
        // 创建微博分享接口实例
        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(pContext, "898971157");// 2045436852
        // 注册第三方应用到微博客户端中，注册成功后该应用将显示在微博的应用列表中。
        // 但该附件栏集成分享权限需要合作申请，详情请查看 Demo 提示
        // NOTE：请务必提前注册，即界面初始化的时候或是应用程序初始化时，进行注册
        if (mWeiboShareAPI.isWeiboAppInstalled()) {
            mWeiboShareAPI.registerApp();
        }
    }

    public static boolean isWeiboAppInstalled(Context pContext) {
        if (mWeiboShareAPI == null) {
            initWeiboShareInOnCreate(pContext);
        }

        return mWeiboShareAPI.isWeiboAppInstalled();
    }

    public static void handleWeiboResponse(Context pContext, Intent pIntent, IWeiboHandler.Response pResponse) {
        if (mWeiboShareAPI == null) {
            initWeiboShareInOnCreate(pContext);
        }

        mWeiboShareAPI.handleWeiboResponse(pIntent, pResponse);
    }

    public static boolean sendRequest(Context pContext, SendMultiMessageToWeiboRequest request) {
        if (mWeiboShareAPI == null) {
            initWeiboShareInOnCreate(pContext);
        }

        return mWeiboShareAPI.sendRequest(request);
    }
}
