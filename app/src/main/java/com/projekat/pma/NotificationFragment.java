package com.projekat.pma;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.projekat.pma.model.News;
import com.projekat.pma.model.Notification;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NotificationFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RelativeLayout view = (RelativeLayout) inflater.inflate(R.layout.notification_fragment, container, false);

        DatabaseHelper db = new DatabaseHelper(getActivity());

        final ArrayList<Notification> notifications = db.getData();

        NotificationListAdapter adapter = new NotificationListAdapter(getContext(),R.layout.adapter_view_layout,notifications);
        ListView listView = (ListView) view.findViewById(R.id.listViewNotification);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Notification notification = notifications.get(position);

                final Intent intent = new Intent(getContext(), NewsInfo.class);
                intent.putExtra("title",notification.getTitle());
                intent.putExtra("text",notification.getText());
                intent.putExtra("identificator", notification.getIdentificator());
                startActivity(intent);
            }
        });
        return  view;
    }

}
