package io.egg.jiantu.util;

import io.egg.jiantu.R;
import android.content.Context;
import android.util.Log;

public class TextColorUtil {

	public static double lumdiff(float r1, float g1, float b1, float r2, float g2, float b2) {
		double result1 = 0.2126f * Math.pow(r1 / 255, 2.2) + 0.7152f
				* Math.pow(g1 / 255, 2.2) + 0.0722f * Math.pow(b1 / 255, 2.2);

		double result2 = 0.2126f * Math.pow(r2 / 255, 2.2) + 0.7152f
				* Math.pow(g2 / 255, 2.2) + 0.0722f * Math.pow(b2 / 255, 2.2);

		if (result1 > result2) {
			return (result1 + 0.05) / (result2 + 0.05);
		} else {
			return (result2 + 0.05) / (result1 + 0.05);
		}
	}
	public static int[] rgbValue(int color){
		int[] rgb = new int[3];
		rgb[0] = (color&0xff0000)>>16;//color>>16;//color/(256*256);
		rgb[1] = (color&0xff00)>>8;//(color-rgb[0]*256*256)/256;
		rgb[2] = color&0xff;
		return rgb;
		
	}
	
	public static int getSuitableHintColorValue(Context context,int bgColor){
		
		int[] bgColorRgb = rgbValue(bgColor);
		Log.d("","bgColor -->"+bgColorRgb[0]+" "+bgColorRgb[1]+" "+bgColorRgb[2]);
		int color_light = context.getResources().getColor(R.color.hint_color_light);
		int color_dark = context.getResources().getColor(R.color.hint_color_dark);
		int[] lightColorRgb = rgbValue(color_light);
		int[] darkColorRgb = rgbValue(color_dark);
		double lightDiff = lumdiff((float)bgColorRgb[0],(float)bgColorRgb[1],(float)bgColorRgb[2], (float)lightColorRgb[0], (float)lightColorRgb[1], (float)lightColorRgb[2]);
		double darkDiff = lumdiff((float)bgColorRgb[0],(float)bgColorRgb[1],(float)bgColorRgb[2], (float)darkColorRgb[0],  (float)darkColorRgb[1], (float)darkColorRgb[2]);
		Log.d("","light darkDiff"+lightDiff+" "+darkDiff);
		if(lightDiff>darkDiff){
			return color_light;
		}else{
			return color_dark;
		}
	}
	
	public static int getSuitableTextColorValue(Context context,int bgColor){
		
		int[] bgColorRgb = rgbValue(bgColor);
		Log.d("","bgColor -->"+bgColorRgb[0]+" "+bgColorRgb[1]+" "+bgColorRgb[2]);
		int color_white = context.getResources().getColor(R.color.white);
		int color_black = context.getResources().getColor(R.color.black);
		int[] lightColorRgb = rgbValue(color_white);
		int[] darkColorRgb = rgbValue(color_black);
		double whiteDiff = lumdiff((float)bgColorRgb[0],(float)bgColorRgb[1],(float)bgColorRgb[2], (float)lightColorRgb[0], (float)lightColorRgb[1], (float)lightColorRgb[2]);
		double blackDiff = lumdiff((float)bgColorRgb[0],(float)bgColorRgb[1],(float)bgColorRgb[2], (float)darkColorRgb[0],  (float)darkColorRgb[1], (float)darkColorRgb[2]);
		if(whiteDiff>5 || Math.abs(whiteDiff - blackDiff) <10){
			return color_white;
		}else{
			return color_black;
		}
		
	}

}
