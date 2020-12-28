package com.example.aliosama.sillynamemaker.Model;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.aliosama.sillynamemaker.Activities.Personality.PersonalityTestActivity;
import com.example.aliosama.sillynamemaker.Activities.Personality.PersonalityTestResultActivity;
import com.example.aliosama.sillynamemaker.Model.POJO.PersonalityQuestionsModel;
import com.example.aliosama.sillynamemaker.Model.POJO.PersonalityTestResultModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ali Ussama on 5/30/2018.
 */

public class PersonalityQuestionsDatabase {

    //Reading Questions Attributes.
    private  List<PersonalityQuestionsModel> Q;
    private  ArrayList<PersonalityQuestionsModel.Fields> mFields;
    private  PersonalityQuestionsModel mPersonalityQuestionsModel;
    private  ArrayList<PersonalityQuestionsModel.Fields.Question> QuestionsList;
    private  ArrayList<PersonalityQuestionsModel.Fields.Question> AllQuestionsList;

    //Calculating Result Attributes.
    private ArrayList<PersonalityTestResultModel.Categories> result;
    private String [] Categories = {"Mind", "Energy", "Nature", "Tactics", "Identity"};
    private String [] Fields = {"Extraverted", "Introverted", "Intuitive", "Observant", "Feeling", "Thinking",
            "Judging", "Prospecting", "Assertive", "Turbulent" };
    private String [] CategoriesDescription = {"This trait determines how we interact with our environment.",
                                    "This trait shows where we direct our mental energy.",
                                    "This trait determines how we make decisions and cope with emotions.",
                                    "This trait reflects our approach to work, planning and decision-making.",
                                    "This trait underpins all others, showing how confident we are in our abilities and decisions."};

    private String FinalAcronymResult = "";

    private PersonalityTestResultActivity mPersonalityTestResultActivity;
    private PersonalityTestActivity mPersonalityTestActivity;

    public PersonalityQuestionsDatabase(PersonalityTestResultActivity personalityTestResultActivity) {
        mPersonalityTestResultActivity = personalityTestResultActivity;
    }

    public PersonalityQuestionsDatabase(PersonalityTestActivity personalityTestActivity) {
        mPersonalityTestActivity = personalityTestActivity;
    }


    public void getAllQuestions(){

        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mDatabaseReference.keepSynced(true);
        Q = new ArrayList<>();
        try {
                mDatabaseReference.child("personalityTest").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            ExtractQuestions(dataSnapshot);
                            NotifiyAdapter();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Failed to read value
                        Log.i("OnCancelled", "Failed to read value.", error.toException());
                    }
                });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void NotifiyAdapter(){
        try {
            mPersonalityTestActivity.LoadQuestionsToAdapter(AllQuestionsList);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void ExtractQuestions(DataSnapshot dataSnapshot){
        try {
            GenericTypeIndicator<Map<String, Map<String, Map<String, String>>>> t = new GenericTypeIndicator<Map<String, Map<String, Map<String, String>>>>(){};
            Map<String, Map<String, Map<String, String>>> result =   dataSnapshot.getValue(t);
            if (result != null) {

                Q = new ArrayList<>();
                AllQuestionsList = new ArrayList<>();
                for (String CategoryTitle : result.keySet()) { //Category Title:  Mind Tactics Energy Nature Identity
                    try {
                        mPersonalityQuestionsModel = new PersonalityQuestionsModel();
                        mPersonalityQuestionsModel.setCategoryTitle(CategoryTitle);
                        mFields = new ArrayList<>(2);

                        for (String FieldName : result.get(CategoryTitle).keySet()) { //Field Name : Extrovert Vs Introvert .. Thinking Vs Feeling
                            try {
                                Log.i("Extract Questions","FieldName  = "+FieldName);
                                String Acronym = setAcronym(FieldName);
                                Log.i("Extract Questions","Acronym  = "+Acronym);

                                QuestionsList = new ArrayList<>();

                                for (String valueOfQuestion : result.get(CategoryTitle).get(FieldName).values()) { // Value Of Question : Question itself.
                                    QuestionsList.add(new PersonalityQuestionsModel.Fields.Question(valueOfQuestion));
                                    AllQuestionsList.add(new PersonalityQuestionsModel.Fields.Question(valueOfQuestion, "" + CategoryTitle, "" + FieldName, "" + Acronym));
                                }
                                mFields.add(new PersonalityQuestionsModel.Fields(QuestionsList, "" + FieldName, "" + Acronym));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        mPersonalityQuestionsModel.setmFields(mFields);
                        Q.add(mPersonalityQuestionsModel);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String setAcronym(String KeyField) {
        try {
            if (KeyField.matches("Observant")) {
                Log.i("setAcronym","Observant  = S");
                return "S";
            } else if (KeyField.matches("Intuitive")) {
                Log.i("setAcronym","Intuitive  = N");
                return "N";
            } else if (KeyField.matches("Assertive") || KeyField.matches("Turbulent")) {
                Log.i("setAcronym"," Assertive | Turbulent = "+"-" + KeyField.substring(0, 1).toUpperCase());
                return "-" + KeyField.substring(0, 1).toUpperCase();
            } else {
                Log.i("setAcronym","else = " + KeyField.substring(0, 1).toUpperCase());
                return KeyField.substring(0, 1).toUpperCase();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        Log.i("setAcronym","None");
        return "";
    }

    public void getPersonalityTestResult(ArrayList<PersonalityQuestionsModel.Fields.Question> data){
        try {

            FinalAcronymResult = "";
            PersonalityTestResultModel mPersonalityTestResultModel = new PersonalityTestResultModel();
            result = new ArrayList<>();
            result = splittingQuestions(data,result,FinalAcronymResult);
            Log.i("PersonalityTestResult","result Category 0 Name = "+result.get(0).getCategoryName());
            mPersonalityTestResultModel.setmCategories(result);
            Log.i("PersonalityTestResult","Category 0 Name = "+mPersonalityTestResultModel.getmCategories().get(0).getCategoryName());
            getPersonalityTestResultImageUri(FinalAcronymResult,mPersonalityTestResultModel);
            mPersonalityTestResultModel.setTestResult(FinalAcronymResult);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private ArrayList<PersonalityTestResultModel.Categories> splittingQuestions(ArrayList<PersonalityQuestionsModel.Fields.Question> data,ArrayList<PersonalityTestResultModel.Categories> result,String FinalAcronymResult){
       try {
           int field_1_count, field_2_count, zero_count;
           int j = 0; // counter for fields // Extraverted Introverted Thinking Feeling ...etc.
           for (int i = 0; i < Categories.length; i++) { // Categories Loop
               PersonalityTestResultModel.Categories mPersonalityTestResultModel = new PersonalityTestResultModel.Categories();

               mPersonalityTestResultModel.setCategoryName(Categories[i]);
               mPersonalityTestResultModel.setCategory_Description(CategoriesDescription[i]);
               field_1_count = 0;
               field_2_count = 0;
               zero_count = 0;
               String Field_1_Name = "", Field_2_Name = "", Field_1_Acronym = "", Field_2_Acronym = "";


               for (int q = 0; q < data.size(); q++) { // Questions Loop

                   if (data.get(q).getCategoryTitle().matches(Categories[i]) && data.get(q).getFieldName().matches(Fields[j])) {
                       Field_1_Name = data.get(q).getFieldName();
                       Field_1_Acronym = data.get(q).getFieldAcronym();
                       Log.i("splittingQuestions",""+Categories[i]+" , "+Fields[j]+" , Acronym = "+Field_1_Acronym);
                       if (data.get(q).getQuestionValue() == 1)
                           field_1_count++;
                       else if (data.get(q).getQuestionValue() == -1)
                           field_2_count++;
                       else
                           zero_count++;
                   } else if (data.get(q).getCategoryTitle().matches(Categories[i]) && data.get(q).getFieldName().matches(Fields[j + 1])) {
                       Field_2_Name = data.get(q).getFieldName();
                       Field_2_Acronym = data.get(q).getFieldAcronym();
                       Log.i("splittingQuestions",""+Categories[i]+" , "+Fields[j+1]+" , Acronym = "+Field_2_Acronym);

                       if (data.get(q).getQuestionValue() == 1)
                           field_2_count++;
                       else if (data.get(q).getQuestionValue() == -1)
                           field_1_count++;
                       else
                           zero_count++;
                   }
               }
               Log.i("splittingQuestions",""+ "Acronym1 = "+Field_1_Acronym + " Acronym2 = "+Field_2_Acronym);

               mPersonalityTestResultModel = CalculatePercentage(mPersonalityTestResultModel,field_1_count, Field_1_Name, Field_1_Acronym,
                                                                 field_2_count, Field_2_Name, Field_2_Acronym, zero_count);
               result.add(mPersonalityTestResultModel);
               j += 2; // increase fields index by 2 because each category ex: Mind has 2 fields Extraverted Vs. Introverted.
           }
       }catch (Exception e){
           e.printStackTrace();
       }
       return result;
    }

    private PersonalityTestResultModel.Categories CalculatePercentage(PersonalityTestResultModel.Categories mPersonalityTestResultModelCategories,
                                                                      int field_1_count, String field_1_name, String field_1_acronym,
                                                           int field_2_count, String field_2_name, String field_2_acronym, int zero_count) {

        try {
            if (field_1_count > field_2_count){
                double Num1 = (((field_1_count+zero_count) / 12)*100);
                double Num2 = ((field_2_count / 12)*100);
                mPersonalityTestResultModelCategories.setMostFieldName(field_1_name);
                mPersonalityTestResultModelCategories.setMostFieldPercentage((int) (Math.ceil(Num1)));
                mPersonalityTestResultModelCategories.setMostFieldAcronym(field_1_acronym);
                mPersonalityTestResultModelCategories.setLeastFieldName(field_2_name);
                mPersonalityTestResultModelCategories.setLeastFieldPercentage((int) (Math.floor(Num2)));
                mPersonalityTestResultModelCategories.setLeastFieldAcronym(field_2_acronym);
                FinalAcronymResult += field_1_acronym;
            }else if (field_2_count > field_1_count){
                double Num1 = ((field_1_count / 12)*100);
                double Num2 = (((field_2_count+zero_count) / 12)*100);
                mPersonalityTestResultModelCategories.setMostFieldName(field_2_name);
                mPersonalityTestResultModelCategories.setMostFieldPercentage((int) (Math.ceil(Num2)));
                mPersonalityTestResultModelCategories.setMostFieldAcronym(field_2_acronym);
                mPersonalityTestResultModelCategories.setLeastFieldName(field_1_name);
                mPersonalityTestResultModelCategories.setLeastFieldPercentage((int) (Math.floor(Num1)));
                mPersonalityTestResultModelCategories.setLeastFieldAcronym(field_1_acronym);
                FinalAcronymResult += field_2_acronym;
            }else{
                mPersonalityTestResultModelCategories.setMostFieldName(field_2_name);
                mPersonalityTestResultModelCategories.setMostFieldAcronym(field_2_acronym);
                mPersonalityTestResultModelCategories.setMostFieldPercentage(52);
                mPersonalityTestResultModelCategories.setLeastFieldName(field_1_name);
                mPersonalityTestResultModelCategories.setLeastFieldAcronym(field_1_acronym);
                mPersonalityTestResultModelCategories.setLeastFieldPercentage(48);
                FinalAcronymResult += field_2_acronym;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        Log.i("CalculatePercentage","Final Acronym Result = "+FinalAcronymResult);
        return mPersonalityTestResultModelCategories;
    }

    private void getPersonalityTestResultImageUri(String finalAcronymResult, final PersonalityTestResultModel mPersonalityTestResultModel) {
      try {

          Log.i("TestResultImageUri","Category 0 Name = "+mPersonalityTestResultModel.getmCategories().get(0).getCategoryName());


          StorageReference mDatabaseReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://sillynamemaker-78d9a.appspot.com/");
          Map<String, String> Personalities = getPersonalities();

          String name = finalAcronymResult.substring(0, 4);
          String Path = "";
          if (name.contains("NT")) {
              //Analyst.
              Path = "Analyst/" + name.toLowerCase() + ".png";
          } else if (name.contains("NF")) {
              //Diplomats.
              Path = "Diplomats/" + name.toLowerCase() + ".png";
          } else if (name.charAt(1) == 'S' && name.charAt(3) == 'J') {
              //Sentinels.
              Path = "Sentinels/" + name.toLowerCase() + ".png";
          } else if (name.charAt(1) == 'S' && finalAcronymResult.charAt(3) == 'P') {
              //Explorers.
              Path = "Explorers/" + name.toLowerCase() + ".png";
          }

          if (FinalAcronymResult == null){
              Log.i("TestResultImageUri","FinalAcronym is null");
          }else if (name == null){
              Log.i("TestResultImageUri","name is null");

          }else if (Personalities == null){
              Log.i("TestResultImageUri","personalities is null");
          }else if (Personalities.get(name) == null){
              Log.i("TestResultImageUri","Personalities.get(name) is null");
              Log.i("TestResultImageUri",""+name);
          }
          if (Personalities.get(name) != null) {
              FinalAcronymResult = Personalities.get(name).concat(" (").concat(FinalAcronymResult).concat(").");
          }
          mDatabaseReference.child(Path).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
              @Override
              public void onSuccess(Uri uri) {
                  try {
                      mPersonalityTestResultModel.setImage_URL(uri);
                      Log.i("Image Uri = ", uri.toString());

                      ReturnObjectToActivity(mPersonalityTestResultModel);
                  }catch (Exception e){
                      e.printStackTrace();
                  }
              }
          }).addOnFailureListener(new OnFailureListener() {
              @Override
              public void onFailure(@NonNull Exception e) {
                  Log.i("Read Image From Storage", e.toString());
              }
          });
      }catch (Exception e){
          e.printStackTrace();
      }
    }

    private Map<String,String> getPersonalities() {
        Map<String,String> map = new HashMap<>();
        map.put("INTJ","ARCHITECT");
        map.put("INTP","LOGICIAN");
        map.put("ENTJ","COMMANDER");
        map.put("ENTP","DEBATER");

        map.put("ENFP","CAMPAIGNER");
        map.put("ENFJ","PROTAGONIST");
        map.put("INFP","MEDIATOR");
        map.put("INFJ","ADVOCATE");

        map.put("ESFJ","CONSUL");
        map.put("ESTJ","EXECUTIVE");
        map.put("ISFJ","DEFENDER");
        map.put("ISTJ","LOGISTICIAN");

        map.put("ESFP","ENTERTAINER");
        map.put("ESTP","ENTREPRENEUR");
        map.put("ISFP","ADVENTURER");
        map.put("ISTP","VIRTUOSO");

        return map;
    }

    private void ReturnObjectToActivity(PersonalityTestResultModel mPersonalityTestResultModel){
        try {
            mPersonalityTestResultActivity.SetTestResult(mPersonalityTestResultModel);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
