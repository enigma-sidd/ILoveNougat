package com.example.enigma_sidd.zappos;

/**
 * Created by Enigma_Sidd on 9/11/2016.
 */
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;

public class ProductsView extends RecyclerView.Adapter<ProductsView.ViewHolder> {

    private Bitmap bitmap;
    public LruCache<String,Bitmap> lruCache;
    ArrayList<HashMap<String,String>> arrayListPLPDetails;
    private OnListItemSelected onListItemSelected;
    private static ProductsView ProductsView;
    static float disc=0;

    public interface OnListItemSelected
    {
        void onListItemSelected(String asinID,String title,String price);
    }


    public void onListItemSelectedListener(OnListItemSelected onListItemSelected)
    {
        this.onListItemSelected = onListItemSelected;
    }

    private ProductsView(){}


    public static ProductsView SingletonInstance()
    {
        if(ProductsView == null) {
            ProductsView = new ProductsView();
        }
        return ProductsView;
    }

    public void updateCacheAndInitializeArray(LruCache lruCache)
    {
        this.lruCache = lruCache;
        arrayListPLPDetails = new ArrayList<>();
    }


        public void updateData(ArrayList<HashMap<String,String>> arrayListPLPDetails)
        {
            this.arrayListPLPDetails = arrayListPLPDetails;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_main_row,viewGroup,false));
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            viewHolder.bindData(position);
        }

        @Override
        public int getItemCount() {
            return arrayListPLPDetails.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageViewProduct,imageViewError;
        TextView textViewTitle,textViewDescription,textViewPrice,textDiscount;

        public ViewHolder(final View itemView) {
            super(itemView);
            textViewTitle = (TextView)itemView.findViewById(R.id.text_title);
            textViewDescription = (TextView)itemView.findViewById(R.id.text_description);
            textViewPrice = (TextView)itemView.findViewById(R.id.text_price);
            textDiscount = (TextView)itemView.findViewById(R.id.text_discount);
            imageViewProduct = (ImageView)itemView.findViewById(R.id.image_product);
            imageViewError = (ImageView)itemView.findViewById(R.id.image_product);
            itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    if(onListItemSelected!=null)
                    {
                        onListItemSelected.onListItemSelected(arrayListPLPDetails.get(getPosition()).get(Tag.ASIN),
                                textViewTitle.getText().toString(),textViewPrice.getText().toString());
                    }
                }
            });
        }

        //data binding and update
        void bindData(int position)
        {
            if(arrayListPLPDetails.size() > 0)
            {
                imageViewError.setImageBitmap(null);
                String imageURL = arrayListPLPDetails.get(position).get(Tag.json_ImageUrl);
                String rating = arrayListPLPDetails.get(position).get(Tag.json_discount);
                String textDescription = arrayListPLPDetails.get(position).get(Tag.json_pname);
                if(textDescription.length() > 20)
                {
                    textDescription = textDescription.substring(0,20);
                    textDescription = textDescription + "...";
                }
                textViewTitle.setText(arrayListPLPDetails.get(position).get(Tag.json_brand));
                textViewDescription.setText(textDescription);
                textViewPrice.setText(arrayListPLPDetails.get(position).get(Tag.json_price));
                String discount = arrayListPLPDetails.get(position).get(Tag.json_discount);
                disc = Float.parseFloat(discount.substring(0, discount.length()-1));
                if(disc==0){textDiscount.setText("");}
                else {textDiscount.setText(arrayListPLPDetails.get(position).get(Tag.json_discount) + " Off");
                    disc = 0;}

            //download image if absent in LRU Cache
            bitmap = lruCache.get(imageURL);
                if(bitmap == null)
                {
                    BgTask_DownloadImage BgTask_DownloadImage = new BgTask_DownloadImage(imageViewProduct,lruCache,null);
                    BgTask_DownloadImage.execute(new String[]{imageURL,Tag.PLP});
            }
            else
            {
                imageViewProduct.setImageBitmap(bitmap);
            }
        }
        }


}

}