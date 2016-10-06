package io.egg.jiantu.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import io.egg.jiantu.R;

public class CircleProgressBar extends View {
    private int maxProgress = 100;
    private int progress = 0;
    //画圆所在的距形区域
    RectF oval;
    Paint paint;
    public CircleProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        oval = new RectF();
        paint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = this.getWidth();
        int height = this.getHeight();

        if(width!=height)
        {
            int min=Math.min(width, height);
            width=min;
            height=min;
        }

        paint.setAntiAlias(true);
        paint.setColor(Color.rgb(0x70, 0x70, 0x70));
        canvas.drawColor(Color.TRANSPARENT);
        paint.setStrokeWidth(2);
        paint.setStyle(Paint.Style.STROKE);

        oval.left = 1;
        oval.top =  1;
        oval.right = width - 1;
        oval.bottom = height - 1;

        canvas.drawArc(oval, -90, 360, false, paint);
        paint.setStrokeWidth(6);
        oval.left = 5;
        oval.top =  5;
        oval.right = width - 5;
        oval.bottom = height - 5;
        canvas.drawArc(oval, -90, ((float) progress / maxProgress) * 360, false, paint);

//        paint.setStyle(Paint.Style.FILL);
//        paint.setStrokeWidth(1);
//        canvas.drawRect(width / 3, height / 3, 2 * width /3, 2 * height / 3, paint);

        String text = progress + "%";
        int textHeight = height / 4;
        paint.setTextSize(textHeight);
        int textWidth = (int) paint.measureText(text, 0, text.length());
        paint.setStyle(Paint.Style.FILL);
        canvas.drawText(text, width / 2 - textWidth / 2, height / 2 +textHeight/2, paint);

    }



    public int getMaxProgress() {
        return maxProgress;
    }

    public void setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
        this.invalidate();
    }

    /**
     * 非ＵＩ线程调用
     */
    public void setProgressNotInUiThread(int progress) {
        this.progress = progress;
        this.postInvalidate();
    }
}
