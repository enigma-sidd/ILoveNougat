package com.example.enigma_sidd.zappos;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;


/**
 * Created by Enigma_Sidd on 9/11/2016.
 * cache class for storing images
 */

public class BitmapCache {

    private static LruCache lruCache;

    public void setLRUCache() {
        if (lruCache == null) {
            //proper cache memory is allotted to hold images
            final int maxMemory = (int) (Runtime.getRuntime().maxMemory()) / 1024;
            final int cacheSize = maxMemory / 8;
            lruCache = new LruCache<String, Bitmap>(cacheSize) {
                @Override
                protected int sizeOf(String key, Bitmap bitmap) {
                    return bitmap.getByteCount() / 1024;
                }
            };
        }
    }

    public static LruCache getCache() {
        return lruCache;
    }
}