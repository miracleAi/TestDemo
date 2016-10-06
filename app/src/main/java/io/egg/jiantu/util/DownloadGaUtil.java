package io.egg.jiantu.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import io.egg.jiantu.R;

public class DownloadGaUtil {
    private static String channel;

    public static String getChannel(Context pContext) {
        if (TextUtils.isEmpty(channel)) {
            channel = pContext.getString(R.string.channel_name);

            if (channel == null) {
                channel = "";
            }

            Log.d("channel", channel);
        }

        return channel;
    }
}
