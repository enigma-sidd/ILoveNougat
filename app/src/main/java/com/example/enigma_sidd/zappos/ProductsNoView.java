package com.example.enigma_sidd.zappos;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Enigma_Sidd on 9/11/2016.
 * Class used when there are no results
 * Also shows the homepage before any search is made
 */
public class ProductsNoView extends RecyclerView.Adapter<ProductsNoView.ViewHolder> {


    private boolean errorFlag = false;
    private static ProductsNoView ProductsNoView;

    //updates flag - true errorFlag stands for no result is returned
    public void updateData(boolean errorFlag) {
        this.errorFlag = errorFlag;
    }

    private ProductsNoView() {
    }

    //Create a Singleton Object for this class
    public static ProductsNoView SingletonInstance() {
        if (ProductsNoView == null) {
            ProductsNoView = new ProductsNoView();
        }
        return ProductsNoView;
    }


    @Override
    public ProductsNoView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.no_result_fragment, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ProductsNoView.ViewHolder holder, int position) {
        holder.bindData();
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.missing_image);
            textView = (TextView) itemView.findViewById(R.id.missing_text);
        }

        //binds the data for the first time and also after updating
        public void bindData() {
            if (errorFlag) {
                imageView.setImageResource(R.drawable.error);
                textView.setText(Tag.NOT_FOUND);
            } else {

                textView.setText("Go on...Search for your favourite products...!");
            }
        }
    }
}
