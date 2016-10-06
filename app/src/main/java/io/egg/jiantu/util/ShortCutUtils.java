package io.egg.jiantu.util;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

import io.egg.jiantu.R;
import io.egg.jiantu.wxapi.WXEntryActivity;

/**
 * Created by apolloyujl on 14-6-20.
 */
public class ShortCutUtils {

    public static boolean isAddShortCut(final Context pContext) {
        boolean isInstallShortcut = false;
        final ContentResolver cr = pContext.getContentResolver();
        int versionLevel = android.os.Build.VERSION.SDK_INT;

        String AUTHORITY;
        //2.2以上的系统的文件文件名字是不一样的
        if (versionLevel >= 8) {
            AUTHORITY = "com.android.launcher2.settings";
        } else {
            AUTHORITY = "com.android.launcher.settings";
        }

        final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
                + "/favorites?notify=true");
        Cursor c = cr.query(CONTENT_URI,
                new String[]{"title", "iconResource"}, "title=?",
                new String[]{pContext.getString(R.string.app_name)}, null);
        //Log.d("","shortcut count->"+c.getCount());
        if (c != null && c.getCount() > 0) {
            isInstallShortcut = true;
            Log.d("", "shortcut count->" + c.getCount());
        } else {
            Log.d("", "shortcut null->");
        }
        return isInstallShortcut;
    }

    public static void doAddShortCut(Context pContext) {
        String key = "isAppInstalled";

        SharedPreferences appPreferences = PreferenceManager.getDefaultSharedPreferences(pContext);

        boolean isAppInstalled = appPreferences.getBoolean(key, false);
        if (isAppInstalled) {
            return;
        }

        removeShortcut(pContext);

        addShortcut(pContext);

        SharedPreferences.Editor editor = appPreferences.edit();
        editor.putBoolean(key, true);
        editor.commit();
    }

    private static void addShortcut(Context pContext) {
        Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");

        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, pContext.getResources().getString(R.string.app_name));
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                Intent.ShortcutIconResource.fromContext(pContext, R.drawable.appicon));
        Intent intent = new Intent(pContext, WXEntryActivity.class);
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");

        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);

        pContext.sendBroadcast(shortcut);
    }

    private static void removeShortcut(Context pContext) {
        Intent intent = new Intent(pContext, WXEntryActivity.class);
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");

        Intent addIntent = new Intent();
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, pContext.getResources().getString(R.string.app_name));

        addIntent.setAction("com.android.launcher.action.UNINSTALL_SHORTCUT");
        pContext.sendBroadcast(addIntent);
    }
}
