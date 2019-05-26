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

import com.projekat.pma.model.News;
import com.projekat.pma.model.Notification;

import java.util.ArrayList;

public class NotificationFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RelativeLayout view = (RelativeLayout) inflater.inflate(R.layout.notification_fragment, container, false);

        DatabaseHelper db = new DatabaseHelper(getActivity());

        ArrayList<Notification> notifications = db.getData();

        NotificationListAdapter adapter = new NotificationListAdapter(getContext(),R.layout.adapter_view_layout,notifications);
        ListView listView = (ListView) view.findViewById(R.id.listViewNotification);
        System.out.println(listView);
        listView.setAdapter(adapter);

        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), NewsInfo.class);
                startActivity(intent);
            }
        });*/
        return  view;
    }

}
