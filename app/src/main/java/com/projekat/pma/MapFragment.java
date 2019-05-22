package com.projekat.pma;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MapFragment extends Fragment {

    private View inflatedView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       this.inflatedView = inflater.inflate(R.layout.map_fragment, container, false);

       Button selectedDateButton = (Button) inflatedView.findViewById(R.id.button);
        selectedDateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //prikazati date picker activity
                Intent intent = new Intent(getActivity(), CalendarActivity.class);
                startActivity(intent);
            }
        });

        return this.inflatedView;
    }
}
