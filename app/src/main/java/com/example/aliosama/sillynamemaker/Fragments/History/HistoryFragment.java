package com.example.aliosama.sillynamemaker.Fragments.History;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.aliosama.sillynamemaker.R;

import java.util.ArrayList;

import static android.graphics.Color.rgb;

public class HistoryFragment extends Fragment {

    ArrayList< ArrayList<Float> >statusPercentData;
    ArrayList<String> statusLabelsData;
    ArrayList<String> DaysData;
    RecyclerView recyclerView;


    public HistoryFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = null;
        try{
            view = inflater.inflate(R.layout.fragment_history, container, false);
            recyclerView = view.findViewById(R.id.HistoryFragmentRecyclerView);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(container.getContext());
            recyclerView.setLayoutManager(linearLayoutManager);
            DaysData = new ArrayList<>();
            statusPercentData = new ArrayList<>();
            statusLabelsData = new ArrayList<>();
            prepareListData();
            HistoryRecAdapter historyRecAdapter = new HistoryRecAdapter(statusPercentData,statusLabelsData,DaysData);
            recyclerView.setAdapter(historyRecAdapter);
        }catch (Exception e){
            e.printStackTrace();
        }

        return view;
    }

    private void prepareListData() {
        ArrayList<Float> perc = new ArrayList<>();
        perc.add(30f);
        perc.add(40f);
        perc.add(30f);
        statusLabelsData.add("Happy");
        statusLabelsData.add("Sad");
        statusLabelsData.add("Normal");

        for (int i = 0 ; i < 10 ;i++){
            DaysData.add("Day"+String.valueOf(i+1));
            statusPercentData.add(perc);
        }
    }



}
