package io.egg.jiantu.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

import io.egg.jiantu.R;
import io.egg.jiantu.manager.WeChatSharingManager;
import io.egg.jiantu.ui.activity.MainActivity_;
import io.egg.jiantu.util.GAConstant;
import io.egg.jiantu.analytics.GAUtil;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    // IWXAPI 是第三方app和微信通信的openapi接口
    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        api = WeChatSharingManager.getApi(this);

        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setIntent(intent);

        api.handleIntent(intent, this);
    }

    // 微信发送请求到第三方应用时，会回调到该方法
    @Override
    public void onReq(BaseReq req) {
        Intent intent = new Intent(this, MainActivity_.class);
        startActivity(intent);

        finish();
    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    @Override
    public void onResp(BaseResp resp) {
        int result;

        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                result = R.string.errcode_success;

                if (WeChatSharingManager.shareWeiXin == WeChatSharingManager.SHARE_TO_SESSION) {
                    GAUtil.trackShare(this, GAConstant.share_to_wechat_session_finish);
                } else {
                    GAUtil.trackShare(this, GAConstant.share_to_wechat_timeline_finish);
                }

                break;

            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = R.string.errcode_cancel;

                if (WeChatSharingManager.shareWeiXin == WeChatSharingManager.SHARE_TO_SESSION) {
                    GAUtil.trackShare(this, GAConstant.share_to_wechat_session_cancel);
                } else {
                    GAUtil.trackShare(this, GAConstant.share_to_wechat_timeline_cancel);
                }

                break;

            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = R.string.errcode_deny;

                if (WeChatSharingManager.shareWeiXin == WeChatSharingManager.SHARE_TO_SESSION) {
                    GAUtil.trackShare(this, GAConstant.share_to_wechat_session_fail);
                } else {
                    GAUtil.trackShare(this, GAConstant.share_to_wechat_timeline_fail);
                }

                break;

            default:
                result = R.string.errcode_unknown;

                if (WeChatSharingManager.shareWeiXin == WeChatSharingManager.SHARE_TO_SESSION) {
                    GAUtil.trackShare(this, GAConstant.share_to_wechat_session_fail);
                } else {
                    GAUtil.trackShare(this, GAConstant.share_to_wechat_timeline_fail);
                }

                break;
        }

        // TODO 需要删除微信分享出去的图片
//        if (!TextUtils.isEmpty(waitDeletedPicurePath)) {
//            File file = new File(waitDeletedPicurePath);
//            if (file.exists()) {
//                file.delete();
//            }
//
//            waitDeletedPicurePath = "";
//        }

        Toast.makeText(this, result, Toast.LENGTH_LONG).show();

        finish();
    }
}
