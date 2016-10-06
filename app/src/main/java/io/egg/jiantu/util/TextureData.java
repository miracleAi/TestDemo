package io.egg.jiantu.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

import io.egg.jiantu.R;

public class TextureData {

    public static final int BACKGROUD_COLOR_SIZE = 11;
    public static final int TEXTURE_SIZE = 11;

    /**
     * 获得颜色数组
     *
     * @param context
     * @param index
     * @return
     */
    public static BgColor getBackgroundColor(Context context, int index) {
        int[] colors = new int[BACKGROUD_COLOR_SIZE];
        colors[0] = context.getResources().getColor(R.color.white);
        colors[1] = context.getResources().getColor(R.color.color2);
        colors[2] = context.getResources().getColor(R.color.color3);
        colors[3] = context.getResources().getColor(R.color.color4);
        colors[4] = context.getResources().getColor(R.color.color5);
        colors[5] = context.getResources().getColor(R.color.color6);
        colors[6] = context.getResources().getColor(R.color.color7);
        colors[7] = context.getResources().getColor(R.color.color8);
        colors[8] = context.getResources().getColor(R.color.color9);
        colors[9] = context.getResources().getColor(R.color.color10);
        colors[10] = context.getResources().getColor(R.color.color11);

        String[] colorNames = new String[BACKGROUD_COLOR_SIZE];
        colorNames[0] = "纯白";
        colorNames[1] = "青葱";
        colorNames[2] = "香草";
        colorNames[3] = "午夜";
        colorNames[4] = "晴天";
        colorNames[5] = "薰衣草";
        colorNames[6] = "鲜橙";
        colorNames[7] = "蜜桃";
        colorNames[8] = "玫瑰";
        colorNames[9] = "乌云";
        colorNames[10] = "咖啡";

        int colorValue = colors[index];
        String colorName = colorNames[index];
        BgColor color = new BgColor();
        color.setColorValue(colorValue);
        color.setColorName(colorName);
        return color;
    }

    public static Bitmap getBackgroundColorBitmap(Context context, int index) {
        int colorValue = getBackgroundColor(context, index).getColorValue();
        ColorDrawable colorDrawable = new ColorDrawable(colorValue);
        Bitmap colorBitmap = drawableToBitmap(colorDrawable, context);//((BitmapDrawable) colorDrawable).getBitmap();
        return colorBitmap;
    }

	public static Texture getTextureBitmap(Context context,int index){
		int[] drawableIds = new int[TEXTURE_SIZE];
		drawableIds[0] = R.drawable.texture1;
		drawableIds[1] = R.drawable.texture2;
		drawableIds[2] = R.drawable.texture3;
		drawableIds[3] = R.drawable.texture4;
		drawableIds[4] = R.drawable.texture5;
		drawableIds[5] = R.drawable.texture6;
		drawableIds[6] = R.drawable.texture7;
		drawableIds[7] = R.drawable.texture8;
		drawableIds[8] = R.drawable.texture9;
		drawableIds[9] = R.drawable.texture10;
		drawableIds[10] = R.drawable.texture11;

		int drawableId = drawableIds[index];
		Drawable drawable = context.getResources().getDrawable(drawableId);
		Bitmap bitmap = drawableToBitmap(drawable,context);
		Texture texture = new Texture();
		texture.setDrawableId(drawableId);
		texture.setmBitmap(bitmap);
		texture.setBlendMode();
		return texture;
	}

    public static Bitmap drawableToBitmap(Drawable drawable, Context context) {
        // 取 drawable 的长宽
        int w = context.getResources().getDisplayMetrics().widthPixels;//drawable.getIntrinsicWidth();
        int h = context.getResources().getDisplayMetrics().widthPixels;//drawable.getIntrinsicHeight();

        // 取 drawable 的颜色格式
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        // 建立对应 bitmap
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        // 建立对应 bitmap 的画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        // 把 drawable 内容画到画布中
        drawable.draw(canvas);
        return bitmap;
    }

    public static class BgColor {
        int colorValue;
        String colorName;

        public int getColorValue() {
            return colorValue;
        }

        public void setColorValue(int colorValue) {
            this.colorValue = colorValue;
        }

        public String getColorName() {
            return colorName;
        }

        public void setColorName(String colorName) {
            this.colorName = colorName;
        }

    }

    public static class Texture{
        int drawableId;
        BLEND_MODE bleadMode;
        Bitmap mBitmap;

        int getDrawableId() {
            return drawableId;
        }
        public void setDrawableId(int drawableId) {
            this.drawableId = drawableId;
        }
        public Bitmap getmBitmap() {
            return mBitmap;
        }
        public void setmBitmap(Bitmap mBitmap) {
            this.mBitmap = mBitmap;
        }
        public BLEND_MODE getBlendMode(){
            return this.bleadMode;
        }
        public void setBlendMode(){
            switch(this.drawableId){
                case R.drawable.texture1:
                    this.bleadMode = BLEND_MODE.MULTIPLY;
                    break;
                case R.drawable.texture2:
                    this.bleadMode = BLEND_MODE.MULTIPLY;
                    break;
                case R.drawable.texture3:
                    this.bleadMode = BLEND_MODE.SCREEN;
                    break;
                case R.drawable.texture4:
                    this.bleadMode = BLEND_MODE.SCREEN;
                    break;
                case R.drawable.texture5:
                    this.bleadMode = BLEND_MODE.SCREEN;
                    break;
                case R.drawable.texture6:
                    this.bleadMode = BLEND_MODE.MULTIPLY;
                    break;
                case R.drawable.texture7:
                    this.bleadMode = BLEND_MODE.MULTIPLY;
                    break;
                case R.drawable.texture8:
                    this.bleadMode = BLEND_MODE.SCREEN;
                    break;
                case R.drawable.texture9:
                    this.bleadMode = BLEND_MODE.MULTIPLY;
                    break;
                case R.drawable.texture10:
                    this.bleadMode = BLEND_MODE.MULTIPLY;
                    break;
                case R.drawable.texture11:
                    this.bleadMode = BLEND_MODE.MULTIPLY;
                    break;
            }
        }
    }

    public enum BLEND_MODE {
        SCREEN, MULTIPLY
    }
}
