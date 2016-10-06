package io.egg.jiantu.util;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * Created by apolloyujl on 14-7-16.
 */
public class CacheUtil {
    private static LruCache<String, Bitmap> mMemoryCache;

    private static CacheUtil cacheUtil;

    public static CacheUtil getInstance() {
        if (cacheUtil == null) {
            final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

            final int cacheSize = maxMemory / 8;

            mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
                @Override
                protected int sizeOf(String key, Bitmap bitmap) {
                    return bitmap.getByteCount() / 1024;
                }
            };

            cacheUtil = new CacheUtil();
        }

        return cacheUtil;
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

    public void removeBitmapFromCache(String key) {
        mMemoryCache.remove(key);
    }
}
