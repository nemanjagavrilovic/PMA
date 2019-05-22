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

        Notification notification1 = new Notification("Notification title 1","Notification text 1");
        Notification notification2 = new Notification("Notification title 2","Notification text 2");
        Notification notification3 = new Notification("Notification title 3","Notification text 3");

        ArrayList<Notification> notifications = new ArrayList<>();
        notifications.add(notification1);
        notifications.add(notification2);
        notifications.add(notification3);

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
