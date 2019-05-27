package com.projekat.pma;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.projekat.pma.model.News;

import java.util.List;

public class CardViewAdapter extends RecyclerView.Adapter<NewsViewItemHolder> {

    private List<News> newsItemList;

    public CardViewAdapter(List<News> newsItemList) {
        this.newsItemList = newsItemList;
    }

    @Override
    public NewsViewItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        // Inflate the RecyclerView item layout xml.
        View carItemView = layoutInflater.inflate(R.layout.card_view_item, parent, false);

        // Get car title text view object.
        final TextView newsTitleView = (TextView)carItemView.findViewById(R.id.card_view_image_title);
        // Get car image view object.
        final ImageView newsImageView = (ImageView)carItemView.findViewById(R.id.card_view_image);
        // When click the image.
        newsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String carTitle = newsTitleView.getText().toString();

            }
        });

        // Create and return our custom Car Recycler View Item Holder object.
        NewsViewItemHolder ret = new NewsViewItemHolder(carItemView);
        return ret;
    }

    @Override
    public void onBindViewHolder(NewsViewItemHolder holder,final int position) {
        if(newsItemList != null) {
            // Get car item dto in list.
            News newsItem = newsItemList.get(position);

            if(newsItem != null) {
                // Set car item title.
                holder.getNewsTitleText().setText(newsItem.getTitle());
                // Set car image resource id.
                byte[] encodeByte = Base64.decode(newsItem.getImage(), Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                holder.getNewsImageView().setImageBitmap(bitmap);
            }
            holder.getNewsImageView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("You clicked " +  newsItemList.get(position).getId());
                }
            });
        }
    }
    @Override
    public int getItemCount() {
        int ret = 0;
        if(newsItemList!=null)
        {
            ret = newsItemList.size();
        }
        return ret;
    }
}
