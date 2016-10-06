package io.egg.jiantu;

import android.hardware.Camera;

import java.util.Comparator;

/**
 * Created by apolloyujl on 8/6/14.
 */
public class CameraSizeComparator implements Comparator<Camera.Size> {
    @Override
    public int compare(Camera.Size lhs, Camera.Size rhs) {
        if (lhs.width == rhs.width) {
            if (lhs.height > rhs.width) {
                return 1;
            } else if (lhs.height == rhs.width) {
                return 0;
            } else {
                return -1;
            }
        } else if (lhs.width > rhs.width) {
            return 1;
        } else {
            return -1;
        }
    }
}
