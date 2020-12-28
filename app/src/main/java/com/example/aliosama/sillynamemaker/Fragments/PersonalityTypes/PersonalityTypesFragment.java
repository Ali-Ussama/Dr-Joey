package com.example.aliosama.sillynamemaker.Fragments.PersonalityTypes;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.example.aliosama.sillynamemaker.Model.POJO.PersonalityTypesModel;
import com.example.aliosama.sillynamemaker.Model.PersonalityTypesDatabase;
import com.example.aliosama.sillynamemaker.R;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;


public class PersonalityTypesFragment extends Fragment {

    RecyclerView mRecyclerView;
    ArrayList<PersonalityTypesModel> Recyclerdata;
    PersonalityTypesRecAdapter mPersonalityTypesRecAdapter;
    AVLoadingIndicatorView mAvLoadingIndicatorView;

    public PersonalityTypesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_personality_types, container, false);
        try {

            mAvLoadingIndicatorView = view.findViewById(R.id.fragment_personality_types_avi);
            Recyclerdata = new ArrayList<>();
            mPersonalityTypesRecAdapter = new PersonalityTypesRecAdapter(Recyclerdata);
            mRecyclerView = view.findViewById(R.id.fragment_personality_types_recycler_view);
            mRecyclerView.setLayoutManager(new GridLayoutManager(view.getContext(),2));
            mRecyclerView.setAdapter(mPersonalityTypesRecAdapter);
            startAnim();

            PersonalityTypesDatabase mPersonalityTypesDatabase = new PersonalityTypesDatabase(PersonalityTypesFragment.this);
            mPersonalityTypesDatabase.getAllPersonalityTypes();

        }catch (Exception e){
            e.printStackTrace();
        }


        return view;
    }

    public void startAnim(){
        try {
            mAvLoadingIndicatorView.smoothToShow();
            mRecyclerView.setVisibility(View.GONE);
            mAvLoadingIndicatorView.setVisibility(View.VISIBLE);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void stopAnim(){
        try {
            mAvLoadingIndicatorView.hide();
            mAvLoadingIndicatorView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void NotifyAdapter(ArrayList<PersonalityTypesModel> personalityTypesFragmentRecViewdata) {
        try {
            Recyclerdata = personalityTypesFragmentRecViewdata;
            stopAnim();
            mPersonalityTypesRecAdapter.updateData(Recyclerdata);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
