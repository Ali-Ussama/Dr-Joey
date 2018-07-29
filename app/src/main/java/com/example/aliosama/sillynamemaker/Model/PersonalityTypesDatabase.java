package com.example.aliosama.sillynamemaker.Model;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.aliosama.sillynamemaker.Activities.PersonalityType.PersonalityTypeItemActivity;
import com.example.aliosama.sillynamemaker.Fragments.PersonalityTypes.PersonalityTypesFragment;
import com.example.aliosama.sillynamemaker.Model.POJO.PersonalityTypesModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ali Ussama on 6/13/2018.
 */

public class PersonalityTypesDatabase {

    private DatabaseReference mDatabaseReference;
    private String[] characters= {"intj", "intp", "entj", "entp", "infj", "infp", "enfj", "enfp",
                                  "istj", "isfj", "estj", "esfj", "istp", "isfp", "estp", "esfp"};
    private PersonalityTypesFragment mPersonalityTypesFragment;
    private PersonalityTypeItemActivity mPersonalityTypeItemActivity;
    private ArrayList<PersonalityTypesModel> PersonalityTypesFragmentRecViewdata;
    private PersonalityTypesModel mPersonalityTypesModel;
    private ArrayList<PersonalityTypesModel.YouMayKnow> YouMayKnowData;
    private ArrayList<PersonalityTypesModel.FullDescription> FullDescriptionData;

    private int index = 0;


    public PersonalityTypesDatabase(PersonalityTypesFragment personalityTypesFragment) {
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        this.mPersonalityTypesFragment = personalityTypesFragment;
        this.PersonalityTypesFragmentRecViewdata = new ArrayList<>();
    }

    public PersonalityTypesDatabase(PersonalityTypeItemActivity personalityTypeItemActivity) {
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        this.mPersonalityTypeItemActivity = personalityTypeItemActivity;
    }

//    Personality Types Fragment.
    public void getAllPersonalityTypes(){

        try {
            if (index != characters.length){
                PersonalityTypesModel mPersonalityTypesModel = new PersonalityTypesModel();
                ReadCharacterTitleFromFirebase(mPersonalityTypesModel,characters[index],PersonalityTypesFragmentRecViewdata);
            }else {
                RetrunDataToPersonalityTypesFragment(mPersonalityTypesFragment,PersonalityTypesFragmentRecViewdata);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void RetrunDataToPersonalityTypesFragment(PersonalityTypesFragment mPersonalityTypesFragment,ArrayList<PersonalityTypesModel> PersonalityTypesFragmentRecViewdata) {
        mPersonalityTypesFragment.NotifyAdapter(PersonalityTypesFragmentRecViewdata);
    }

    private void ReadCharacterTitleFromFirebase(final PersonalityTypesModel mPersonalityTypesModel, final String Character,final ArrayList<PersonalityTypesModel> PersonalityTypesFragmentRecViewdata){
        mDatabaseReference.child("PersonalityTypes").child(Character).child("FullDescription").child("D1").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    String text = (String) dataSnapshot.getValue();
                    String Title = "";
                    String Acronym = "";
                    for (int x = 0; x < text.length(); x++) {
                        if (text.charAt(x) == ' ') {
                            Title = text.substring(0, x);
                            Acronym = text.substring(x + 1, text.length());
                            break;
                        }
                    }
                    mPersonalityTypesModel.setCharacterTitle(Title);
                    mPersonalityTypesModel.setCharacterAcronymText(Acronym);
                    mPersonalityTypesModel.setAcronym(Character);
                    ReadMinDescriptionFromFirebase(mPersonalityTypesModel, Character, PersonalityTypesFragmentRecViewdata);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("ReadCharacterTitle",databaseError.toException().toString());
            }
        });
    }

    private void ReadMinDescriptionFromFirebase(final PersonalityTypesModel mPersonalityTypesModel, final String Character,final ArrayList<PersonalityTypesModel> PersonalityTypesFragmentRecViewdata){
        mDatabaseReference.child("PersonalityTypes").child(Character).child("MinDescription").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    String value = dataSnapshot.getValue().toString();
                    mPersonalityTypesModel.setMinDescription(value);
                    ReadCharacterImageUrlFromFirebase(mPersonalityTypesModel, Character, PersonalityTypesFragmentRecViewdata);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("ReadMinDescription",databaseError.toException().toString());

            }
        });
    }

    private void ReadCharacterImageUrlFromFirebase(final PersonalityTypesModel mPersonalityTypesModel,String Character,final ArrayList<PersonalityTypesModel> PersonalityTypesFragmentRecViewdata){
        mDatabaseReference.child("PersonalityTypes").child(Character).child("CharacterImage").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    String value = dataSnapshot.getValue().toString();
                    mPersonalityTypesModel.setCharacterImageUrl(value);
                    PersonalityTypesFragmentRecViewdata.add(mPersonalityTypesModel);
                    index++;
                    getAllPersonalityTypes();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("ReadMinDescription",databaseError.toException().toString());

            }
        });
    }


//    Personality Type Item Activity.
    public void getPersonalityTypeItemData(PersonalityTypesModel mPersonalityTypesModel){
        try {
            this.YouMayKnowData = new ArrayList<>();
            this.FullDescriptionData = new ArrayList<>();
            this.mPersonalityTypesModel = mPersonalityTypesModel;
            ReadFullDescription(this.mPersonalityTypesModel,FullDescriptionData,YouMayKnowData);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void ReadFullDescription(final PersonalityTypesModel mPersonalityTypesModel, final ArrayList<PersonalityTypesModel.FullDescription> fullDescriptionData, final ArrayList<PersonalityTypesModel.YouMayKnow> youMayKnowData) {

        mDatabaseReference.child("PersonalityTypes").child(mPersonalityTypesModel.getAcronym()).child("FullDescription").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    Map<String, String> map = (HashMap<String, String>) dataSnapshot.getValue();
                    for (String value : map.values()) {
                        fullDescriptionData.add(new PersonalityTypesModel.FullDescription(value));
                    }
                    mPersonalityTypesModel.setmFullDescriptions(fullDescriptionData);
                    ReadYouMayKnow(mPersonalityTypesModel,youMayKnowData);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("ReadMinDescription",databaseError.toException().toString());

            }
        });
    }

    private void ReadYouMayKnow(final PersonalityTypesModel mPersonalityTypesModel,final ArrayList<PersonalityTypesModel.YouMayKnow> youMayKnowData) {

        mDatabaseReference.child("PersonalityTypes").child(mPersonalityTypesModel.getAcronym()).child("You May Know").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    Map<String, Map<String,String>> map = (HashMap<String, Map<String,String>>) dataSnapshot.getValue();
                    for (String Key: map.keySet()) {
                        youMayKnowData.add(new PersonalityTypesModel.YouMayKnow(map.get(Key).get("name"),map.get(Key).get("image")));
                    }
                    mPersonalityTypesModel.setmYouMayKnow(youMayKnowData);
                    ReadHeaderImageUrl(mPersonalityTypesModel);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("ReadYouMayKnow",databaseError.toException().toString());

            }
        });
    }

    private void ReadHeaderImageUrl(final PersonalityTypesModel mPersonalityTypesModel){

        mDatabaseReference.child("PersonalityTypes").child(mPersonalityTypesModel.getAcronym()).child("HeaderImage").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    String url = (String) dataSnapshot.getValue();
                    mPersonalityTypesModel.setHeaderImageUrl(url);
                    ReadMidImageUrl(mPersonalityTypesModel);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("ReadYouMayKnow",databaseError.toException().toString());

            }
        });

    }

    private void ReadMidImageUrl(final PersonalityTypesModel mPersonalityTypesModel){

        mDatabaseReference.child("PersonalityTypes").child(mPersonalityTypesModel.getAcronym()).child("MidImage").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    String url = (String) dataSnapshot.getValue();
                    mPersonalityTypesModel.setMidImageUrl(url);
                    ReturnDataToPersonalityTypeItemActivity(mPersonalityTypesModel);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("ReadYouMayKnow",databaseError.toException().toString());

            }
        });
    }

    private void ReturnDataToPersonalityTypeItemActivity(PersonalityTypesModel mPersonalityTypesModel) {
        try {
            mPersonalityTypeItemActivity.NotifyAdapter(mPersonalityTypesModel);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
