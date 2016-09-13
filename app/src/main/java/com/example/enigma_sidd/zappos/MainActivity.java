package com.example.enigma_sidd.zappos;

import android.app.Activity;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.util.LruCache;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Enigma_Sid on 9/11/2016.
 * Displays search results of the user in
 * a RecyclerView
 */

public class MainActivity extends AppCompatActivity {
    Toolbar toolbarSearch;
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        final Intent intent = getIntent();
        final Uri myURI = intent.getData();
        BitmapCache BitmapCache = new BitmapCache();
        BitmapCache.setLRUCache();

        if (!NetworkOps.isNetworkAvailable(this)) {
            setContentView(R.layout.no_result);
        } else {

            if (myURI == null) {
                setContentView(R.layout.activity_main);
                toolbarSearch = (Toolbar) findViewById(R.id.tool_bar);
                setSupportActionBar(toolbarSearch);
                toolbarSearch.setTitleTextColor(Color.WHITE);
                getSupportFragmentManager().beginTransaction().replace(R.id.container, PlaceHolderFragment.newInstance()).commit();
            }

        }
    }


    //quits on pressing back button twice
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            PlaceHolderFragment.newInstance().clearVariables();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, Tag.BACK, Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 3000);
    }




}