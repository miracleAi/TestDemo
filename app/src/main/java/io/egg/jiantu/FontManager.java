package io.egg.jiantu;

import android.content.Context;
import android.graphics.Typeface;

import java.io.File;
import java.util.ArrayList;

import io.egg.jiantu.util.FontUtil;

/**
 * Created by jiajixin on 15/6/26.
 */
public class FontManager {


    //字体是否已经存在
    public static boolean isExist(Context context, int id, String name) {
        File file = new File(context.getFilesDir(), id + "-" + name + ".ttf");
        return file.exists();
    }

    //删除字体
    public static void deleteFont(Context context, int id, String name) {
        File file = new File(context.getFilesDir(), id + "-" + name + ".ttf");
        file.delete();
    }

    //获取本地字体属性
    public static ArrayList<FontUtil.JiantuFont> getFonts(Context context) {
        File dir = context.getFilesDir();
        String[] fontFileNames = dir.list();
        FontUtil.otherFonts.clear();
        for (String fontFileName : fontFileNames) {
            if (fontFileName.endsWith(".ttf")) {
                FontUtil.JiantuFont font = new FontUtil.JiantuFont();
                font.setTypeFace(Typeface.createFromFile(context.getFilesDir() + "/" + fontFileName));
                int startIndex = fontFileName.indexOf("-") + 1;
                int finishIndex = fontFileName.indexOf(".ttf");
                font.setFontName(fontFileName.substring(startIndex, finishIndex));
                FontUtil.otherFonts.add(font);
            }
        }
        return FontUtil.otherFonts;
    }

}
