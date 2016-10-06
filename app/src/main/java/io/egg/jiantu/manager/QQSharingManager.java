package io.egg.jiantu.manager;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

import com.tencent.connect.share.QQShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

import io.egg.jiantu.Constants;
import io.egg.jiantu.qzone.TencentUtil;

/**
 * Created by AirZcm on 1572.
 */
public class QQSharingManager extends Activity {
    public static Tencent mTencent;
    static final String SCOPE = "upload_pic, list_album";

    public static void init(Context context) {
        mTencent = Tencent.createInstance(Constants.qqAppId(), context);
    }

    public void loginByQQ(Activity activity) {
        if (mTencent.isSupportSSOLogin(activity)) {
            if (!mTencent.isSessionValid()) {
                mTencent.login(activity, SCOPE, loginListener);
            }
        }

    }

    IUiListener loginListener = new BaseUiListener() {
        @Override
        protected void doComplete(JSONObject values) {
            Log.d("SDKQQAgentPref", "AuthorSwitch_SDK:" + SystemClock.elapsedRealtime());
            initOpenidAndToken(values);
        }
    };

    private class BaseUiListener implements IUiListener {

        @Override
        public void onComplete(Object response) {
            if (null == response) {
                return;
            }
            JSONObject jsonResponse = (JSONObject) response;
            if (null != jsonResponse && jsonResponse.length() == 0) {
                return;
            }
            doComplete((JSONObject) response);
        }

        protected void doComplete(JSONObject values) {

        }

        @Override
        public void onError(UiError e) {
            TencentUtil.dismissDialog();
        }

        @Override
        public void onCancel() {
            TencentUtil.dismissDialog();
        }
    }

    public static void initOpenidAndToken(JSONObject jsonObject) {
        try {
            String token = jsonObject.getString(com.tencent.connect.common.Constants.PARAM_ACCESS_TOKEN);
            String expires = jsonObject.getString(com.tencent.connect.common.Constants.PARAM_EXPIRES_IN);
            String openId = jsonObject.getString(com.tencent.connect.common.Constants.PARAM_OPEN_ID);
            if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)
                    && !TextUtils.isEmpty(openId)) {
                mTencent.setAccessToken(token, expires);
                mTencent.setOpenId(openId);
            }
        } catch (Exception e) {
        }
    }

    public static boolean ready() {
        if (mTencent == null) {
            return false;
        }
        boolean isReady = mTencent.isSessionValid() && mTencent.getQQToken().getOpenId() != null;
        return isReady;
    }

    public void onClickShare(String path, Activity activity) {
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, path);
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "简图");
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, "http://jiantuapp.com/");
        mTencent.shareToQQ(activity, params, new BaseUiListener());
    }

}