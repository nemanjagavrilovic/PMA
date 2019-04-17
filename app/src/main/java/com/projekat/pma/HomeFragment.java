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

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RelativeLayout view = (RelativeLayout) inflater.inflate(R.layout.home_fragment, container, false);

        News news1 = new News("Title 1","Text 1");
        News news2 = new News("Title 2","Text 2");
        News news3 = new News("Title 3","Text 3");
        News news4 = new News("Title 4","Text 4");
        News news5 = new News("Title 5","Text 5");

        ArrayList<News> news = new ArrayList<>();
        news.add(news1);
        news.add(news2);
        news.add(news3);
        news.add(news4);
        news.add(news5);

        NewsListAdapter adapter = new NewsListAdapter(getContext(),R.layout.adapter_view_layout,news);
        ListView listView = (ListView) view.findViewById(R.id.listView);
        System.out.println(listView);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), NewsInfo.class);
                startActivity(intent);
            }
        });
        return  view;
    }
}
