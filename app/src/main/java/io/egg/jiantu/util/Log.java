package io.egg.jiantu.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by apolloyujl on 7/29/14.
 */
public class Log {
    private static List<String> list;

    static {
        if (list == null) {
            list = new ArrayList<String>();
        }
    }

    public static void writeLogs(String log) {
        list.add(log);
    }

    public static List<String> getLogs () {
        Collections.reverse(list);

        return list;
    }
}
