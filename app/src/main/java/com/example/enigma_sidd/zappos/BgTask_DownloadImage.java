package com.example.enigma_sidd.zappos;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.widget.ImageView;


import java.lang.ref.WeakReference;

/**
 * Created by Enigma_Sidd on 9/11/2016.
 * AsyncTask to download images from URL.
 */
public class BgTask_DownloadImage extends AsyncTask<String, Void, String> {

    private Bitmap bitmap;
    private WeakReference<ImageView> imageViewWeakReference, imageViewSquareWeakReference;
    private LruCache<String, Bitmap> lruCache;

    public BgTask_DownloadImage(ImageView imageView, LruCache lruCache, ImageView imageViewSquare) {
        imageViewWeakReference = new WeakReference<>(imageView);
        imageViewSquareWeakReference = new WeakReference<>(imageViewSquare);
        this.lruCache = lruCache;
    }

    @Override
    protected String doInBackground(String... urlAndType) {

        bitmap = NetworkOps.downloadImage(urlAndType[0]);
        if (bitmap != null) {
            lruCache.put(urlAndType[0], bitmap);
        }
        return urlAndType[1];
    }

    @Override
    protected void onPostExecute(String typePage) {
        if (typePage.equals(Tag.PIP)) {
            ImageView imageViewSquare = imageViewSquareWeakReference.get();
            imageViewSquare.setImageBitmap(bitmap);
        }
        ImageView imageView = imageViewWeakReference.get();
        imageView.setImageBitmap(bitmap);

    }
}