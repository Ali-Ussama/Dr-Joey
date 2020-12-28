package com.example.aliosama.sillynamemaker.Activities.PersonalityType;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.example.aliosama.sillynamemaker.Model.POJO.PersonalityTypesModel;
import com.example.aliosama.sillynamemaker.Model.PersonalityTypesDatabase;
import com.example.aliosama.sillynamemaker.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PersonalityTypeItemActivity extends AppCompatActivity {

    ImageView HeaderImage, MidImage;
    TextView Header1, Header2, Description1, Description2, YouMayKnowTextView;
    RecyclerView mYouMayKnowRecyclerView;
    PersonalityTypeItemRecAdapter mPersonalityTypeItemRecAdapter;
    PersonalityTypesModel mPersonalityTypesModel;
    PersonalityTypesDatabase mPersonalityTypesDatabase;
    ScrollView SkeletonRootView;
    SkeletonScreen mSkeletonScreen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personality_type_item);
        try {
            Intent intent = getIntent();
            mPersonalityTypesModel = (PersonalityTypesModel) intent.getSerializableExtra("PersonalityTypeRowItemData");
            Init();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void Init() {
        try {
            HeaderImage = findViewById(R.id.activity_personality_type_item_headerImage);
            MidImage = findViewById(R.id.activity_personality_type_item_MidImage);
            Header1 = findViewById(R.id.activity_personality_type_item_Header1);
            Header2 = findViewById(R.id.activity_personality_type_item_Header2);
            Description1 = findViewById(R.id.activity_personality_type_item_Description1);
            Description2 = findViewById(R.id.activity_personality_type_item_Description2);
            YouMayKnowTextView = findViewById(R.id.activity_personality_type_item_YouMayKnowText);

            mYouMayKnowRecyclerView = findViewById(R.id.activity_personality_type_item_YouMayKnow_recyclerView);
            mYouMayKnowRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            mPersonalityTypeItemRecAdapter = new PersonalityTypeItemRecAdapter(new ArrayList<PersonalityTypesModel.YouMayKnow>(14));
            mYouMayKnowRecyclerView.setAdapter(mPersonalityTypeItemRecAdapter);

            SkeletonRootView = findViewById(R.id.activity_personality_type);
            mSkeletonScreen = Skeleton.bind(SkeletonRootView)
                    .load(R.layout.activity_personality_type_item_skeleton)
                    .show();

            mPersonalityTypesDatabase = new PersonalityTypesDatabase(PersonalityTypeItemActivity.this);
            mPersonalityTypesDatabase.getPersonalityTypeItemData(mPersonalityTypesModel);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void NotifyAdapter(PersonalityTypesModel mPersonalityTypesModel){
        try {
            this.mPersonalityTypesModel = mPersonalityTypesModel;
            Header1.setText(mPersonalityTypesModel.getmFullDescriptions().get(3).getHeader());
            Description1.setText(mPersonalityTypesModel.getmFullDescriptions().get(0).getHeader());
            Header2.setText(mPersonalityTypesModel.getmFullDescriptions().get(1).getHeader());
            Description2.setText(mPersonalityTypesModel.getmFullDescriptions().get(2).getHeader());
            Picasso.get().load(mPersonalityTypesModel.getHeaderImageUrl()).into(HeaderImage);
            Picasso.get().load(mPersonalityTypesModel.getMidImageUrl()).into(MidImage);
            mPersonalityTypeItemRecAdapter.UpdateAdapter(this.mPersonalityTypesModel.getmYouMayKnow());

            String CharacterTitle = mPersonalityTypesModel.getCharacterTitle().toLowerCase();
            // Change the first letter to upper case and the rest of the word to lowercase + You May Know
            CharacterTitle = CharacterTitle.substring(0,1).toUpperCase().concat(CharacterTitle.substring(1,CharacterTitle.length()).toLowerCase()).concat("s You May Know");
            YouMayKnowTextView.setText(CharacterTitle);
            mSkeletonScreen.hide();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
