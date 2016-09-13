package com.example.enigma_sidd.zappos;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.LruCache;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Enigma_Sidd on 9/13/2016.
 * RecyclerView & Placeholder
 */
public class PlaceHolderFragment extends Fragment {
    public AlertDialog mAlertDialog;
    ImageView imageViewProduct;
    public AlertDialog.Builder alertBuilder;
    private ProductsView ProductsView;
    private ProductsNoView ProductsNoView;
    private RecyclerView recyclerView, recyclerViewNoResult;
    private ArrayList<HashMap<String, String>> arrayListPLPDetails;
    private String searchQuery = "";
    private static PlaceHolderFragment placeHolderFragment;

    public static PlaceHolderFragment newInstance() {
        if (placeHolderFragment == null) {
            placeHolderFragment = new PlaceHolderFragment();
        }
        return placeHolderFragment;
    }

    public void clearVariables() {
        // must clear static variables on exit from application
        if (arrayListPLPDetails != null) {
            arrayListPLPDetails.clear();
            ProductsNoView.SingletonInstance().updateData(false);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null)
            searchQuery = savedInstanceState.getString(Tag.SEARCH_QUERY);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(Tag.SEARCH_QUERY, searchQuery);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setHasOptionsMenu(true);

        //initialize list that holds results of search
        if (arrayListPLPDetails == null) {
            arrayListPLPDetails = new ArrayList<>();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanse) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        final Context context = getActivity();
        //recycler view in case if no data is returned
        recyclerViewNoResult = (RecyclerView) rootView.findViewById(R.id.recycler_view_home_noresult);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewNoResult.setLayoutManager(linearLayoutManager);
        ProductsNoView = ProductsNoView.SingletonInstance();
        recyclerViewNoResult.setAdapter(ProductsNoView);
        //recycler view if data is returned and handling configuration changes too
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_home);
        GridLayoutManager gridLayoutManager;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        } else
            gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        ProductsView = ProductsView.SingletonInstance();
        ProductsView.updateCacheAndInitializeArray(BitmapCache.getCache());
        recyclerView.setAdapter(ProductsView);
        ProductsView.updateData(arrayListPLPDetails);
        //uses method that is declared in adaptor
        ProductsView.onListItemSelectedListener(new ProductsView.OnListItemSelected() {

            @Override
            public void onListItemSelected(String asinID, String title, String price) {
                //starting PIP activity with necessary data
                if (NetworkOps.isNetworkAvailable(getActivity())) {
                    //searching is done in async task
                    NetworkOps.onProgressBarShow(getActivity());
                    BgTask_DownloadText BgTask_DownloadText = new BgTask_DownloadText();
                    BgTask_DownloadText.execute(new String[]{Tag.SIXPM_BEFURL + searchQuery + Tag.SIXPM_AFTURL, Tag.PIP, ""});


                } else {
                    Toast.makeText(getActivity(), Tag.NO_INTERNET, Toast.LENGTH_SHORT).show();
                }


                Bitmap bitmap;
                LruCache<String, Bitmap> lruCache = BitmapCache.getCache();
               /* bitmap = lruCache.get(imageURL);
                imageViewProduct = (ImageView)View.findViewById(R.id.image_product); setRetainInstance(true);
                if (bitmap == null) {
                    BgTask_DownloadImage BgTask_DownloadImage = new BgTask_DownloadImage(imageViewProduct, lruCache, null);
                    BgTask_DownloadImage.execute(new String[]{imageURL, Tag.PLP});
                } else {
                    imageViewProduct.setImageBitmap(bitmap);
                }*/

             compare();

            }


        });

        // if no data is returned then hide PLPRecyclerView otherwise hide PLPRecyclerNoResultView
        if (arrayListPLPDetails.size() == 0) {
            setPLPRecyclerNoResultView();
        } else {
            setPLPRecyclerView();
        }
        return rootView;
    }


    //function to hide ResultView and display NoResultView
    public void setPLPRecyclerNoResultView() {
        recyclerView.setVisibility(View.GONE);
        recyclerViewNoResult.setVisibility(View.VISIBLE);
    }

    //function to hide NoResultView and display ResultView
    public void setPLPRecyclerView() {
        recyclerView.setVisibility(View.VISIBLE);
        recyclerViewNoResult.setVisibility(View.GONE);
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }


    //update data required for orientation change
    public void updateData(ArrayList<HashMap<String, String>> arrayListPLPDetailsParam) {
        arrayListPLPDetails.clear();
        arrayListPLPDetails.addAll(arrayListPLPDetailsParam);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        // Inflate the menu; this adds items to the action bar if it is present.

        menuInflater.inflate(R.menu.searchmenu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = null;
        if (menuItem != null)
            searchView = (SearchView) menuItem.getActionView();

        if (searchView != null) {
            searchView.setQueryHint(Tag.ENTER_PRODUCT);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                @Override
                public boolean onQueryTextSubmit(String query) {
                    if (NetworkOps.isNetworkAvailable(getActivity())) {
                        //searching is done in async task
                        searchQuery = query;
                        NetworkOps.onProgressBarShow(getActivity());
                        BgTask_DownloadText BgTask_DownloadText = new BgTask_DownloadText();
                        BgTask_DownloadText.execute(new String[]{Tag.BEF_URL + query + Tag.AFT_URL, Tag.PLP, ""});
                    } else {
                        Toast.makeText(getActivity(), Tag.NO_INTERNET, Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }


                @Override
                public boolean onQueryTextChange(String newText) {
                    return true;
                }
            });
        }
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    void compare() {
        alertBuilder = new AlertDialog.Builder(getActivity());
        mAlertDialog = alertBuilder.create();

        alertBuilder.setView(LayoutInflater.from(getActivity()).inflate(R.layout.dialog_compare,null));


        alertBuilder.setCancelable(true);
        alertBuilder.show();
    }
}