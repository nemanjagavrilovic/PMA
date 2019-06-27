package com.projekat.pma;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.projekat.pma.model.News;
import com.projekat.pma.model.Notification;

import java.util.List;

public class NotificationListAdapter extends ArrayAdapter<Notification> {

    private Context mContext;
    private int mResource;
    public NotificationListAdapter(Context context, int resource, List<Notification> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String title = getItem(position).getTitle();
        String text  = getItem(position).getText();

        Notification notification = new Notification(title,text);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource,parent,false);

        TextView tvTitle = (TextView) convertView.findViewById(R.id.textView1);
        TextView tvText = (TextView) convertView.findViewById(R.id.textView2);

        tvTitle.setText(title);
        tvText.setText(text.substring(0,title.length()*90/100)+"...");

        return convertView;
    }

}
