package io.egg.jiantu.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.SeekBar;

/**
 * Created by apolloyujl on 14-7-17.
 */
public class VerticalSeekBar extends SeekBar {

    private OnSeekBarChangeListener mOnSeekBarChangeListener;

    private Drawable mThumb;

    public VerticalSeekBar(Context context) {
        super(context);
    }

    public VerticalSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public VerticalSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(h, w, oldh, oldw);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(heightMeasureSpec, widthMeasureSpec);

        setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
    }

    @Override
    protected void onDraw(Canvas c) {
        c.rotate(-90);
        c.translate(-getHeight(), 0);

        super.onDraw(c);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }

        int progress = getMax() - (int) (getMax() * (event.getRawY()-getTop()) / getHeight());

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setProgress(progress);

                if (mOnSeekBarChangeListener != null) {
                    mOnSeekBarChangeListener.onStartTrackingTouch(this);
                }

                getParent().requestDisallowInterceptTouchEvent(true);

                break;

            case MotionEvent.ACTION_MOVE:
                setProgress(progress);

                break;

            case MotionEvent.ACTION_UP:
                setProgress(progress);

                if (mOnSeekBarChangeListener != null) {
                    mOnSeekBarChangeListener.onStopTrackingTouch(this);
                }

                getParent().requestDisallowInterceptTouchEvent(false);

                break;

            case MotionEvent.ACTION_CANCEL:
                if (mOnSeekBarChangeListener != null) {
                    mOnSeekBarChangeListener.onStopTrackingTouch(this);
                }

                break;
        }

        return true;
    }

    @Override
    public void setProgress(int progress) {
        super.setProgress(progress);

        onSizeChanged(getWidth(), getHeight(), 0, 0);
    }

    @Override
    public void setThumb(Drawable thumb) {
        super.setThumb(thumb);

        mThumb = thumb;
    }

    public Drawable getSeekBarThumb() {
        return mThumb;
    }

    @Override
    public void setOnSeekBarChangeListener(OnSeekBarChangeListener l) {
        mOnSeekBarChangeListener = l;

        super.setOnSeekBarChangeListener(l);
    }
}
