package com.example.aliosama.sillynamemaker.Activities.Personality;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.example.aliosama.sillynamemaker.Activities.NavDrawerActivity;
import com.example.aliosama.sillynamemaker.Activities.Personality.Adapters.PersonaltyTestResultRecyclerAdapter;
import com.example.aliosama.sillynamemaker.Model.POJO.PersonalityQuestionsModel;
import com.example.aliosama.sillynamemaker.Model.POJO.PersonalityTestResultModel;
import com.example.aliosama.sillynamemaker.Model.PersonalityQuestionsDatabase;
import com.example.aliosama.sillynamemaker.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PersonalityTestResultActivity extends AppCompatActivity implements View.OnClickListener{

    ArrayList<PersonalityQuestionsModel.Fields.Question> data;
    ArrayList<PersonalityTestResultModel.Categories> AdapterData;
    public PersonalityTestResultModel mPersonalityTestResultModel;
    public PersonaltyTestResultRecyclerAdapter mPersonaltyTestResultRecyclerAdapter;
    TextView ResultTextView;
    ImageView ResultImageView;
    RecyclerView mRecyclerView;
    Button LetsStartButton;
    View PersonalityTestResultRootView;
    SkeletonScreen PersonalityTestResultScreenSkeleton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personality_test_result);

        try {

            Intent intent = getIntent();
            if (intent != null) {
                data = (ArrayList<PersonalityQuestionsModel.Fields.Question>) intent.getSerializableExtra("QuestionsData");
            }
            Init();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void Init() {
        try {

            ResultTextView = findViewById(R.id.activity_personality_test_result_Result_TextView);
            ResultImageView = findViewById(R.id.activity_personality_test_result_Result_ImageView);
            LetsStartButton = findViewById(R.id.activity_personality_test_result_Start_Button);

            mRecyclerView = findViewById(R.id.activity_personality_test_result_recyclerView);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            AdapterData = new ArrayList<>(5);
            mPersonaltyTestResultRecyclerAdapter = new PersonaltyTestResultRecyclerAdapter(AdapterData);
            mRecyclerView.setAdapter(mPersonaltyTestResultRecyclerAdapter);
            Log.i("PersonalityTestResult","Adapter Initialized");

            PersonalityTestResultRootView = findViewById(R.id.personality_test_result);
            PersonalityTestResultScreenSkeleton = Skeleton.bind(PersonalityTestResultRootView)
                                                        .load(R.layout.activity_peronality_test_result_skeleton_layout)
                                                        .color(R.color.light_transparent)
                                                        .shimmer(true)
                                                        .show();
            Log.i("PersonalityTestResult","Skeleton Initialized");

            PersonalityQuestionsDatabase mPersonalityQuestionsDatabase = new PersonalityQuestionsDatabase(PersonalityTestResultActivity.this);
            mPersonalityQuestionsDatabase.getPersonalityTestResult(data);
            LetsStartButton.setOnClickListener(this);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setResultTextView() {
        ResultTextView.setText(mPersonalityTestResultModel.getTestResult());
    }

    private void LoadImage() {
        Picasso.get().load(mPersonalityTestResultModel.getImage_URL()).into(ResultImageView);
    }

    public void SetTestResult(PersonalityTestResultModel personalityTestResultModel){
        try {
            try {
                Log.i("setTestResult","Skeleton Hide.");
                PersonalityTestResultScreenSkeleton.hide();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.i("setTestResult","NotifyAdapter.");

            mPersonalityTestResultModel = personalityTestResultModel;
            AdapterData = mPersonalityTestResultModel.getmCategories();
            setResultTextView();
            LoadImage();
            mPersonaltyTestResultRecyclerAdapter.notifyDataSetChanged();
            mPersonaltyTestResultRecyclerAdapter.updateData(AdapterData);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.activity_personality_test_result_Start_Button){
            Intent intent = new Intent(PersonalityTestResultActivity.this, NavDrawerActivity.class);
            startActivity(intent);
        }
    }
}

//        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
//        p.weight = 3;
//        Submit.setLayoutParams(p);