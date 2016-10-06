package io.egg.jiantu.manager;

import android.content.Context;
import android.graphics.Bitmap;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import io.egg.jiantu.Constants;
import io.egg.jiantu.wechat.WeChatEntity;

/**
 * Created by apolloyujl on 14-6-30.
 */
public class WeChatSharingManager {
    public final static int SHARE_TO_SESSION = 1;

    public final static int SHARE_TO_TIMELINE = 2;

    public static int shareWeiXin;

    private static IWXAPI api;

    public static void init(Context context) {
        if (api == null) {
            api = WXAPIFactory.createWXAPI(context, Constants.APP_ID, false);
            // 将该app注册到微信
            api.registerApp(Constants.APP_ID);
        }
    }

    public static IWXAPI getApi(Context context) {
        init(context);

        return api;
    }

    public static void sendToFriend(Context context, Bitmap bitmap) {
        shareWeiXin = SHARE_TO_SESSION;

        WeChatEntity.sendToWechat(api, context, bitmap, SendMessageToWX.Req.WXSceneSession);
    }

    public static void sendToTimeline(Context context, Bitmap bitmap) {
        shareWeiXin = SHARE_TO_TIMELINE;

        WeChatEntity.sendToWechat(api, context, bitmap, SendMessageToWX.Req.WXSceneTimeline);
    }
}
