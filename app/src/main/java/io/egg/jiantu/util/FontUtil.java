package io.egg.jiantu.util;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

import java.util.ArrayList;

public class FontUtil {

    final static int DEFAULT = 0;

    public static final int FONT_SIZE = 1;

    public static ArrayList<JiantuFont> otherFonts = new ArrayList<>();

    public static JiantuFont getFont(Context context, int font_tag) {
        JiantuFont font = new JiantuFont();
        switch (font_tag) {
            case DEFAULT:
                return null;
            default:
                return otherFonts.get(font_tag - 1);

        }


    }

    public static class JiantuFont {
        Typeface typeFace;
        String fontName;

        public Typeface getTypeFace() {
            return typeFace;
        }

        public void setTypeFace(Typeface typeFace) {
            this.typeFace = typeFace;
        }

        public String getFontName() {
            return fontName;
        }

        public void setFontName(String fontName) {
            this.fontName = fontName;
        }

    }

}
