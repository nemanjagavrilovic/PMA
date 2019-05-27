package com.projekat.pma;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.projekat.pma.R;

public class NewsViewItemHolder extends RecyclerView.ViewHolder {

    private TextView newsTitleText = null;

    private ImageView newsImageView = null;

    public NewsViewItemHolder(View itemView) {
        super(itemView);

        if(itemView != null)
        {
            newsTitleText = (TextView)itemView.findViewById(R.id.card_view_image_title);

            newsImageView = (ImageView)itemView.findViewById(R.id.card_view_image);
        }
    }

    public TextView getNewsTitleText() {
        return newsTitleText;
    }

    public ImageView getNewsImageView() {
        return newsImageView;
    }
}
