package com.example.enigma_sidd.zappos;
/**
 * Created by Enigma_Sidd on 9/11/2016.
 * validates whether a stable internet conn. is available
 * & uses HTTP connection to download json and
 * image data
 */

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.net.NetworkInfo;
import android.graphics.Bitmap;
import android.app.ProgressDialog;
import android.content.Context;

import java.net.UnknownHostException;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.net.ConnectivityManager;


public class NetworkOps {

    //network available ?
    public static boolean isNetworkAvailable(Activity activity) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    private static ProgressDialog contentLoadingProgressBar;

    // Getting images
    public static Bitmap downloadImage(String url) {
        Bitmap bitmap = null;
        InputStream stream = getHttpConnection(url);
        if (stream != null) {
            bitmap = BitmapFactory.decodeStream(stream);
            try {
                stream.close();
            } catch (IOException e1) {
                Log.e("Exception:", "", e1);
                e1.printStackTrace();
            }
        }

        return bitmap;
    }

    // Get Json
    public static String downloadJSON(String url) {
        String json = null, line;

        InputStream stream = getHttpConnection(url);
        if (stream != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder out = new StringBuilder();
            try {
                while ((line = reader.readLine()) != null) {
                    out.append(line);
                }
                reader.close();
                json = out.toString();
            } catch (IOException ex) {
                Log.d("MyDebugMsg", "IOException in downloadJSON()");
                ex.printStackTrace();
            }
        }
        return json;
    }



    public static InputStream getHttpConnection(String urlString) {
        InputStream stream = null;
        try {
            URL url = new URL(urlString);
            HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setRequestMethod("GET");
            httpConnection.connect();
            if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                stream = httpConnection.getInputStream();
            }
        } catch (UnknownHostException e1) {
            Log.d("Exception: ", "", e1);
            e1.printStackTrace();
        } catch (Exception ex) {
            Log.d("Exception: ", "", ex);
            ex.printStackTrace();
        }
        return stream;
    }

    //progress bar
    public static void onProgressBarShow(Activity activity) {
        contentLoadingProgressBar = new ProgressDialog(activity);
        contentLoadingProgressBar.setCancelable(false);
        contentLoadingProgressBar.setMessage("We r almost there");
        contentLoadingProgressBar.show();
    }

    //dismiss progress bar
    public static void onDismiss() {
        try {
            if (contentLoadingProgressBar != null)
                contentLoadingProgressBar.dismiss();
        } catch (IllegalArgumentException e) {
            Log.e("Exception: ", "", e);
        }
    }


}