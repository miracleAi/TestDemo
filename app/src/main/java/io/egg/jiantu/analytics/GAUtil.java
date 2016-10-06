package io.egg.jiantu.analytics;

import android.content.Context;

import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.Tracker;

import io.egg.jiantu.Constants;
import io.egg.jiantu.util.DownloadGaUtil;
import io.egg.jiantu.util.GAConstant;
import io.egg.jiantu.util.Log;

public class GAUtil {
    private static Tracker mTracker;

    public static Tracker getTracker(Context context) {
        if (mTracker == null) {
            mTracker = GoogleAnalytics.getInstance(context).getTracker(Constants.GA_ID);
            mTracker.set(Fields.CAMPAIGN_NAME, "android_launch");
            mTracker.set(Fields.CAMPAIGN_SOURCE, DownloadGaUtil.getChannel(context));
            mTracker.set(Fields.SCREEN_NAME, "Editor");
        }

        return mTracker;
    }


    // ----------- Edit Start ------------- //

    /**
     * 选择字体
     *
     * @param label   按钮按下后得到的对应字体中文名，不是按下时的；默认字体叫「默认」
     */
    public static void trackPickFont(Context context, String label) {
        getTracker(context).send(buildEvents(
                GAConstant.category_edit,
                GAConstant.pick_font_action,
                label,
                null)
                .set(Fields.CAMPAIGN_SOURCE, DownloadGaUtil.getChannel(context))
                .set(Fields.CAMPAIGN_NAME, "android_launch")
                .build());
    }

    /**
     * @param label  按钮按下后得到的颜色中文名，不是按下时的
     */
    public static void trackPickColor(Context context, String label) {
        getTracker(context).send(buildEvents(
                GAConstant.category_edit,
                GAConstant.pick_color_action,
                label,
                null)
                .set(Fields.CAMPAIGN_SOURCE, DownloadGaUtil.getChannel(context))
                .set(Fields.CAMPAIGN_NAME, "android_launch")
                .build());

    }

    /**
     *
     * @param context
     * @param label 按钮按下后得到的纹理中文名，不是按下时的
     */
    public static void trackPickTexture(Context context,String label){
        getTracker(context).send(buildEvents(
            GAConstant.category_edit,
            GAConstant.pick_texture_action,
            label,
            null)
            .set(Fields.CAMPAIGN_SOURCE, DownloadGaUtil.getChannel(context))
            .set(Fields.CAMPAIGN_NAME, "android_launch")
            .build());

    }

    /**
     * @param label  `library` 移除来自 camera roll 的图片
     *               `camera`  移除刚刚拍摄图片
     */
    public static void trackRemovePicture(Context context, String label) {
        getTracker(context).send(buildEvents(
                GAConstant.category_edit,
                GAConstant.remove_picture_action,
                label,
                null)
                .set(Fields.CAMPAIGN_SOURCE, DownloadGaUtil.getChannel(context))
                .set(Fields.CAMPAIGN_NAME, "android_launch")
                .build());
    }

    public static void trackBlurImage(Context context, Long value) {
        getTracker(context).send(buildEvents(
                GAConstant.category_edit,
                GAConstant.blur_image_action,
                null,
                value)
                .set(Fields.CAMPAIGN_SOURCE, DownloadGaUtil.getChannel(context))
                .set(Fields.CAMPAIGN_NAME, "android_launch")
                .build());
    }

    public static void trackDarkenImage(Context context, Long value) {
        getTracker(context).send(buildEvents(
                GAConstant.category_edit,
                GAConstant.darken_image_action,
                null,
                value)
                .set(Fields.CAMPAIGN_SOURCE, DownloadGaUtil.getChannel(context))
                .set(Fields.CAMPAIGN_NAME, "android_launch")
                .build());
    }

    public static void trackChangeTextPostion(Context context) {
        getTracker(context).send(buildEvents(
                GAConstant.category_edit,
                GAConstant.position_text_action,
                null,
                null)
                .set(Fields.CAMPAIGN_SOURCE, DownloadGaUtil.getChannel(context))
                .set(Fields.CAMPAIGN_NAME, "android_launch")
                .build());
    }

    public static void trackEditInit(Context context) {
        getTracker(context).send(buildEvents(
                GAConstant.category_edit,
                GAConstant.edit_text_init_action,
                null,
                null)
                .set(Fields.CAMPAIGN_SOURCE, DownloadGaUtil.getChannel(context))
                .set(Fields.CAMPAIGN_NAME, "android_launch")
                .build());
    }

    public static void trackEditFinish(Context context) {
        getTracker(context).send(buildEvents(
                GAConstant.category_edit,
                GAConstant.edit_text_finish_action,
                null,
                null)
                .set(Fields.CAMPAIGN_SOURCE, DownloadGaUtil.getChannel(context))
                .set(Fields.CAMPAIGN_NAME, "android_launch")
                .build());
    }

    // ----------- Pick Picture Start ------------- //

    /**
     *
     * @param action `_0_init` 按下选择图片菜单按钮
     *               `_1_finish` 成功选择图片作为简图底图
     *               `_2_cancel` 中途以任何形式取消或者返回
     *               `_3_fail` 中途出错或者异常
     */
    public static void trackPickPictureFromLibrary(Context context, String action) {
        getTracker(context).send(buildEvents(
                GAConstant.category_pick_picture,
                action,
                null,
                null)
                .set(Fields.CAMPAIGN_SOURCE, DownloadGaUtil.getChannel(context))
                .set(Fields.CAMPAIGN_NAME, "android_launch")
                .build());
    }

    /**
     *
     * @param action `_0_init` 按下拍照菜单按钮
     *               `_1_finish` 成功拍照作为简图底图
     *               `_2_cancel` 中途以任何形式取消或者返回
     *               `_3_fail` 中途出错或者异常
     */
    public static void trackTakingPictrue(Context context, String action) {
        getTracker(context).send(buildEvents(
                GAConstant.category_pick_picture,
                action,
                null,
                null)
                .set(Fields.CAMPAIGN_SOURCE, DownloadGaUtil.getChannel(context))
                .set(Fields.CAMPAIGN_NAME, "android_launch")
                .build());
    }

    // ----------- Camera Start ------------- //

    public static void trackSwitchToCamera(Context context, String label) {
        getTracker(context).send(buildEvents(
                GAConstant.category_camera,
                GAConstant.switch_to_camera_action,
                label,
                null)
                .set(Fields.CAMPAIGN_SOURCE, DownloadGaUtil.getChannel(context))
                .set(Fields.CAMPAIGN_NAME, "android_launch")
                .build());
    }

    public static void trackFocusCamera(Context context, String label, Long value) {
        getTracker(context).send(buildEvents(
                GAConstant.category_camera,
                GAConstant.focus_camera_action,
                label,
                value)
                .set(Fields.CAMPAIGN_SOURCE, DownloadGaUtil.getChannel(context))
                .set(Fields.CAMPAIGN_NAME, "android_launch")
                .build());

    }

    // ----------- Share Start ------------- //

    public static void trackShare(Context context, String action) {
        getTracker(context).send(buildEvents(
                GAConstant.category_share,
                action,
                null,
                null)
                .set(Fields.CAMPAIGN_SOURCE, DownloadGaUtil.getChannel(context))
                .set(Fields.CAMPAIGN_NAME, "android_launch")
                .build());
    }

    public static void trackJiantuPropertiesWhenGenearatePicture(Context context, String fontName, int textCount, String imageLabel) {
        getTracker(context).send(buildEvents(
                        GAConstant.category_jiantu_properties,
                        GAConstant.font_usage_action,
                        fontName,
                        null)
                        .set(Fields.CAMPAIGN_SOURCE, DownloadGaUtil.getChannel(context))
                        .set(Fields.CAMPAIGN_NAME, "android_launch")
                        .build()
        );

        getTracker(context).send(buildEvents(
                        GAConstant.category_jiantu_properties,
                        GAConstant.text_count_action,
                        imageLabel,
                        (long) textCount)
                        .set(Fields.CAMPAIGN_SOURCE, DownloadGaUtil.getChannel(context))
                        .set(Fields.CAMPAIGN_NAME, "android_launch")
                        .build()
        );
    }

    public static void trackJiantuPropertiesWhenGenearatePictureInPhoto(Context context, String image_usage_label, String imageFrom, long blur, long dark) {
        Tracker easyTracker = getTracker(context);

        easyTracker.send(buildEvents(
                GAConstant.category_jiantu_properties,
                GAConstant.image_usage_action,
                image_usage_label,
                null)
                .set(Fields.CAMPAIGN_SOURCE, DownloadGaUtil.getChannel(context))
                .set(Fields.CAMPAIGN_NAME, "android_launch")
                .build());

        easyTracker.send(buildEvents(
                GAConstant.category_jiantu_properties,
                GAConstant.picture_source_action,
                imageFrom,
                null)
                .set(Fields.CAMPAIGN_SOURCE, DownloadGaUtil.getChannel(context))
                .set(Fields.CAMPAIGN_NAME, "android_launch")
                .build());

        easyTracker.send(buildEvents(
                GAConstant.category_jiantu_properties,
                GAConstant.blur_usage_action,
                null,
                blur)
                .set(Fields.CAMPAIGN_SOURCE, DownloadGaUtil.getChannel(context))
                .set(Fields.CAMPAIGN_NAME, "android_launch")
                .build());

        easyTracker.send(buildEvents(
                GAConstant.category_jiantu_properties,
                GAConstant.darken_usage_action,
                null,
                dark)
                .set(Fields.CAMPAIGN_SOURCE, DownloadGaUtil.getChannel(context))
                .set(Fields.CAMPAIGN_NAME, "android_launch")
                .build());

    }

    public static void trackJiantuPropertiesWhenGenearatePictureInColorTexture(Context context, String colorName, String textureName) {
        String value = colorName + ":" + textureName;

        getTracker(context).send(buildEvents(
                GAConstant.category_jiantu_properties,
                GAConstant.color_and_texture_usage_action,
                value,
                null)
                .set(Fields.CAMPAIGN_SOURCE, DownloadGaUtil.getChannel(context))
                .set(Fields.CAMPAIGN_NAME, "android_launch")
                .build());
    }

    public static MapBuilder buildEvents(String category, String action, String label, Long value) {
        String log = "Event\n";
        log += "Category: " + category + "\n";
        log += "Action: " + action + "\n";
        log += "Label: " + label + "\n";
        log += "value: " + value + "\n";

        Log.writeLogs(log);

        return MapBuilder.createEvent(category, action, label, value);
    }
}
