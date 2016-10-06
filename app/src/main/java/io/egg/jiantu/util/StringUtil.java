package io.egg.jiantu.util;

import android.text.TextUtils;

/**
 * Created by apolloyujl on 14-6-22.
 */
public class StringUtil {

    public static int trimedEnter = 0;

    public static String trimString(String rawString) {
        trimedEnter = 0;

        String enter = "\n";
        String tab = "\t";
        String space = " ";

        if (TextUtils.isEmpty(rawString)) {
            return rawString;
        }

        while (rawString.contains(enter) ||
                rawString.contains(tab) ||
                rawString.contains(space)) {
            if (rawString.indexOf(enter) == 0) {
                trimedEnter++;
                rawString = rawString.replaceFirst(enter, "");
            } else {
                if (rawString.indexOf(tab) == 0) {
                    rawString = rawString.replaceFirst(tab, "");
                } else {
                    if (rawString.indexOf(space) == 0) {
                        rawString = rawString.replaceFirst(space, "");
                    } else {
                        break;
                    }
                }
            }
        }

        while (rawString.contains(enter) ||
                rawString.contains(tab) ||
                rawString.contains(space)) {
            if (rawString.lastIndexOf(enter) == rawString.length() - 1) {
                rawString = rawString.substring(0, rawString.length() - 1);
            } else {
                if (rawString.lastIndexOf(tab) == rawString.length() - 1) {
                    rawString = rawString.substring(0, rawString.length() - 1);
                } else {
                    if (rawString.lastIndexOf(space) == rawString.length() - 1) {
                        rawString = rawString.substring(0, rawString.length() - 1);
                    } else {
                        break;
                    }
                }
            }
        }

        return rawString;
    }
}
