/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.egg.jiantu.widget;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * A layout which handles the preview aspect ratio.
 */
public class PreviewFrameLayout extends RelativeLayout implements LayoutChangeNotifier {

    private static final String TAG = "CAM_preview";

    /**
     * A callback to be invoked when the preview frame's size changes.
     */
    public interface OnSizeChangedListener {
        public void onSizeChanged(int width, int height);
    }

    private double mAspectRatio;
    private OnSizeChangedListener mListener;
    private LayoutChangeHelper mLayoutChangeHelper;

    public PreviewFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setAspectRatio(4.0 / 3.0);
        mLayoutChangeHelper = new LayoutChangeHelper(this);
    }

    @Override
    protected void onFinishInflate() {
    }

    public void setAspectRatio(double ratio) {
        if (ratio <= 0.0) throw new IllegalArgumentException();

        if (mAspectRatio != ratio) {
            mAspectRatio = ratio;
            requestLayout();
        }
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        int previewWidth = MeasureSpec.getSize(widthSpec);
        int previewHeight = MeasureSpec.getSize(heightSpec);

        if (!(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)) {
            // Get the padding of the border background.
            int hPadding = getPaddingLeft() + getPaddingRight();
            int vPadding = getPaddingTop() + getPaddingBottom();

            // Resize the preview frame with correct aspect ratio.
            previewWidth -= hPadding;
            previewHeight -= vPadding;

            boolean widthLonger = previewWidth > previewHeight;
            int longSide = (widthLonger ? previewWidth : previewHeight);
            int shortSide = (widthLonger ? previewHeight : previewWidth);
            if (longSide > shortSide * mAspectRatio) {
                longSide = (int) ((double) shortSide * mAspectRatio);
            } else {
                shortSide = (int) ((double) longSide / mAspectRatio);
            }
            if (widthLonger) {
                previewWidth = longSide;
                previewHeight = shortSide;
            } else {
                previewWidth = shortSide;
                previewHeight = longSide;
            }

            // Add the padding of the border.
            previewWidth += hPadding;
            previewHeight += vPadding;
        }

        // Ask children to follow the new preview dimension.
        super.onMeasure(MeasureSpec.makeMeasureSpec(previewWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(previewHeight, MeasureSpec.EXACTLY));
    }

    public void setOnSizeChangedListener(OnSizeChangedListener listener) {
        mListener = listener;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (mListener != null) mListener.onSizeChanged(w, h);
    }

    @Override
    public void setOnLayoutChangeListener(
            LayoutChangeNotifier.Listener listener) {
        mLayoutChangeHelper.setOnLayoutChangeListener(listener);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        mLayoutChangeHelper.onLayout(changed, l, t, r, b);
    }
}
