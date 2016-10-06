package io.egg.jiantu.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;

import io.egg.jiantu.R;

/**
 * Created by apolloyujl on 7/29/14.
 */
public class DemoSeek extends AbsVerticalSeekBar {
    public interface OnSeekBarChangeListener {
        void onProgressChanged(DemoSeek seekBar, int progress, boolean fromUser);

        void onStartTrackingTouch(DemoSeek seekBar);

        void onStopTrackingTouch(DemoSeek seekBar);
    }

    private OnSeekBarChangeListener mOnSeekBarChangeListener;

    private Drawable progressDrawable;
    private Rect barBounds, labelTextRect;
    private Bitmap labelBackground;
    private Point labelPos;
    private Paint labelTextPaint, labelBackgroundPaint;

    int viewWidth, barHeight, labelOffset;
    float progressPosX;
    private String expression;

    public DemoSeek(Context context) {
        super(context);

        progressDrawable = getProgressDrawable();

        labelBackground = BitmapFactory.decodeResource(getResources(), R.drawable.appicon);

        labelTextPaint = new Paint();
        labelTextPaint.setColor(Color.WHITE);
        labelTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
        labelTextPaint.setAntiAlias(true);
        labelTextPaint.setDither(true);
        labelTextPaint.setTextSize(13f);

        labelBackgroundPaint = new Paint();

        barBounds = new Rect();
        labelTextRect = new Rect();

        labelPos = new Point();

    }

    public Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    @Override
    void onProgressRefresh(float scale, boolean fromUser) {
        super.onProgressRefresh(scale, fromUser);

        if (mOnSeekBarChangeListener != null) {
            mOnSeekBarChangeListener.onProgressChanged(this, getProgress(),
                    fromUser);
        }
    }

    public DemoSeek(Context context, AttributeSet attrs) {
        super(context, attrs);

        progressDrawable = getProgressDrawable();

        labelBackground = BitmapFactory.decodeResource(getResources(), R.drawable.slider_up_arrow);

        labelTextPaint = new Paint();
        labelTextPaint.setColor(Color.WHITE);
        labelTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
        labelTextPaint.setAntiAlias(true);
        labelTextPaint.setDither(true);
        labelTextPaint.setTextSize(15f);

        labelBackgroundPaint = new Paint();

        barBounds = new Rect();
        labelTextRect = new Rect();

        labelPos = new Point();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (labelBackground != null) {

            viewWidth = getMeasuredWidth();
            barHeight = getMeasuredHeight() - getPaddingTop()
                    - getPaddingBottom();
            setMeasuredDimension(viewWidth + labelBackground.getWidth(),
                    barHeight + labelBackground.getHeight() / 2);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();

        if (labelBackground != null) {
            barBounds.left = getPaddingLeft();
            barBounds.top = (int) (labelBackground.getHeight() / 2f);
            barBounds.right = barBounds.left + viewWidth - getPaddingRight()
                    - getPaddingLeft();
            barBounds.bottom = barBounds.top + barHeight - getPaddingBottom()
                    - getPaddingTop();

            progressPosX = barBounds.top
                    + ((float) this.getProgress() / (float) this.getMax())
                    * barBounds.height() + getTopPaddingOffset();

            labelPos.y = getBottom() - (int) progressPosX - labelOffset
                    + (int) (getProgress() * 0.1f);
            labelPos.x = getPaddingLeft();

            progressDrawable = getProgressDrawable();

            progressDrawable.setBounds(barBounds.left, barBounds.top,
                    barBounds.right, getBottom());

            progressDrawable.draw(canvas);

            String pro = getProgress() * multiplier + "";
            if (expression != null) {
                pro = pro.concat(expression);
            }
            labelTextPaint.getTextBounds(pro, 0, pro.length(), labelTextRect);

            canvas.drawBitmap(labelBackground, labelPos.x, labelPos.y,
                    labelBackgroundPaint);

            canvas.drawText(pro, labelPos.x + labelBackground.getWidth() / 2
                    - labelTextRect.width() / 2 + 15, labelPos.y
                    + labelBackground.getHeight() / 2 + labelTextRect.height()
                    / 2 - 5, labelTextPaint);

        }
        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        invalidate();
        return super.onTouchEvent(event);
    }

    public void setOnSeekBarChangeListener(OnSeekBarChangeListener onSeekBarChangeListener) {
        mOnSeekBarChangeListener = onSeekBarChangeListener;
    }

    @Override
    void onStartTrackingTouch() {
        if (mOnSeekBarChangeListener != null) {
            mOnSeekBarChangeListener.onStartTrackingTouch(this);
        }
    }

    @Override
    void onStopTrackingTouch() {
        if (mOnSeekBarChangeListener != null) {
            mOnSeekBarChangeListener.onStopTrackingTouch(this);
        }
    }


    private int multiplier = 1;

    public void setMultiplier(int multiplier) {
        this.multiplier = multiplier;
    }

    public int getMultiplier() {
        return multiplier;
    }

    public void setExpression(String expression) {
        this.expression = expression.trim();
    }

    public String getExpression() {
        return expression;
    }
}