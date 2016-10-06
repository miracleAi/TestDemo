package io.egg.jiantu.widget;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import io.egg.jiantu.R;


/**
 * 图片调色处理
 * banking
 */
public class ToneLayer {

    /**
     * 亮度
     */
    public static final int FLAG_LIGHTNESS = 0x0;

    /**
     * 模糊度
     */
    public static final int FLAG_BLUR = 0x1;

    /**
     * 模糊度
     */
    private SeekBar mBlurBar;

    /**
     * 亮度
     */
    private SeekBar mLightnessBar;

    private LinearLayout mParent;

    public ToneLayer(Context context, LinearLayout parent) {
        mParent = parent;

        init();
    }

    private void init() {
//        mBlurBar = (SeekBar) mParent.findViewById(R.id.blur_seekbar);
//        mBlurBar.setMax(100);
//        mBlurBar.setProgress(0);
//        mBlurBar.setTag(FLAG_BLUR);
//
//        mLightnessBar = (SeekBar) mParent.findViewById(R.id.lightness_seekbar);
//        mLightnessBar.setMax(100);
//        mLightnessBar.setProgress(0);
//        mLightnessBar.setTag(FLAG_LIGHTNESS);
    }

    public View getParentView() {
        return mParent;
    }

    public void setVisibility(int visibility) {
        mParent.setVisibility(visibility);
    }

    public SeekBar getBlurBar() {
        return mBlurBar;
    }

    public SeekBar getLightnessBar() {
        return mLightnessBar;
    }
}
