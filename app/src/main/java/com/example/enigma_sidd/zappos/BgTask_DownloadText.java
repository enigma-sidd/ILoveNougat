package com.example.enigma_sidd.zappos;

/**
 * * Created by Enigma_Sidd on 9/11/2016.
 * This java file uses AsyncTask to download details from URL.
 * The URL has a link to JSON file
 * Both tasks eg.product listing page and product information page
 * uses this code to download details.
 */


import android.os.AsyncTask;
        import android.util.Log;
        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;
        import java.util.ArrayList;
        import java.util.HashMap;
        import jp.wasabeef.recyclerview.animators.adapters.AlphaInAnimationAdapter;
        import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;


public class BgTask_DownloadText extends AsyncTask<String,Void,String> {
    private ArrayList<HashMap<String, String>> arrayListProductDetails;
    public ArrayList<HashMap<String, String>> arrayListProductDetails2;
    private HashMap<String, String> hashMap;
    private String jsonString;
    private boolean errorFlag = false;
    private String price;

    @Override
    protected String doInBackground(String... urlAndTypePage) {
        //download json data and place it into string using Network Utility file
        jsonString = NetworkOps.downloadJSON(urlAndTypePage[0]);
        arrayListProductDetails = new ArrayList<>();
        if (jsonString != null) {
            try {


                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray jsonArray = (JSONArray) jsonObject.get(Tag.RESULT);
                for (int i = 0; i < jsonArray.length(); i++) {
                    hashMap = new HashMap<>();
                    JSONObject jsonObjectInner = (JSONObject) jsonArray.get(i);
                    hashMap.put(Tag.json_brand, (String) jsonObjectInner.get(Tag.json_brand));
                    hashMap.put(Tag.json_ImageUrl, (String) jsonObjectInner.get(Tag.json_ImageUrl));
                    hashMap.put(Tag.json_pid, (String) jsonObjectInner.get(Tag.json_pid));
                    hashMap.put(Tag.json_ogprice, (String) jsonObjectInner.get(Tag.json_ogprice));
                    hashMap.put(Tag.json_sid, (String) jsonObjectInner.get(Tag.json_sid));
                    hashMap.put(Tag.json_price, jsonObjectInner.get(Tag.json_price).toString());
                    hashMap.put(Tag.json_discount, (String) jsonObjectInner.get(Tag.json_discount));
                    hashMap.put(Tag.json_purl, (String) jsonObjectInner.get(Tag.json_purl));
                    hashMap.put(Tag.json_pname, (String) jsonObjectInner.get(Tag.json_pname));
                    //store the data into an arraylist which will be used later
                    if (urlAndTypePage[1].equals(Tag.PLP)) {
                        arrayListProductDetails.add(hashMap);
                    } else {
                        arrayListProductDetails2.add(hashMap);

                    }

                }


            } catch (JSONException ee) {
                Log.e(Tag.EXCEPTION_CATCH, "", ee);
            }
        } else {
            errorFlag = true;
        }
        return urlAndTypePage[1];
    }


    @Override
    protected void onPostExecute(String typePage) {
        NetworkOps.onDismiss();
        if (typePage.equals(Tag.PLP)) //refreshes the adaptor
        {
            if (!errorFlag) {
                PlaceHolderFragment.newInstance().setPLPRecyclerView();
                ProductsView.SingletonInstance().updateData(arrayListProductDetails);
                ProductsView.SingletonInstance().notifyDataSetChanged();
                AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(ProductsView.SingletonInstance());
                alphaAdapter.setDuration(2000);
                PlaceHolderFragment.newInstance().getRecyclerView().setAdapter(new ScaleInAnimationAdapter(alphaAdapter));
            } else {
                PlaceHolderFragment.newInstance().setPLPRecyclerNoResultView();
                ProductsNoView.SingletonInstance().updateData(errorFlag);
                ProductsNoView.SingletonInstance().notifyDataSetChanged();
                PlaceHolderFragment.newInstance().updateData(arrayListProductDetails);
            }
      /*  else
        {
            ProductInformationPageAdaptor.SingletonInstance().updateData(arrayListProductDetails, price);
            ProductInformationPageAdaptor.SingletonInstance().notifyDataSetChanged();
        } */
        }
    }
}