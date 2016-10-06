package io.egg.jiantu.util;

/**
 * Created by apolloyujl on 14-7-1.
 */
public enum DebugLevel implements Comparable<DebugLevel> {
    NONE, ERROR, WARNING, INFO, DEBUG, VERBOSE;

    public static DebugLevel ALL = DebugLevel.VERBOSE;

    public boolean isSameOrLessThan(final DebugLevel pDebugLevel) {
        return this.compareTo(pDebugLevel) >= 0;
    }
}