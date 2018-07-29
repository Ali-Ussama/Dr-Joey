package com.example.aliosama.sillynamemaker.Model.POJO;

import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ali Ussama on 5/30/2018.
 */

@IgnoreExtraProperties
public class PersonalityQuestionsModel {

    private String CategoryTitle;
    private ArrayList<Fields> mFields;


    public PersonalityQuestionsModel() {
    }

    public ArrayList<Fields> getmFields() {
        return mFields;
    }

    public String getCategoryTitle() {
        return CategoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        CategoryTitle = categoryTitle;
    }

    public void setmFields(ArrayList<Fields> mFields) {
        this.mFields = mFields;
    }

    public static class Fields {
        String FieldName;
        String FieldAcronym;
        ArrayList<Question> Questions;
        int countAgree = 0;
        int countDisAgree= 0;

        public Fields() {

        }

        public Fields(ArrayList<Question> question,String fieldName, String fieldAcronym) {
            FieldName = fieldName;
            Questions = question;
            FieldAcronym = fieldAcronym;
        }

        public ArrayList<Question> getQuestion() {
            return Questions;
        }

        public void setQuestion(ArrayList<Question> question) {
            Questions = question;
        }

        public int getCountAgree() {
            return countAgree;
        }

        public void setCountAgree(int countAgree) {
            this.countAgree = countAgree;
        }

        public int getCountDisAgree() {
            return countDisAgree;
        }

        public void setCountDisAgree(int countDisAgree) {
            this.countDisAgree = countDisAgree;
        }

        public String getFieldName() {
            return FieldName;
        }

        public void setFieldName(String fieldName) {
            FieldName = fieldName;
        }

        public String getFieldAcronym() {
            return FieldAcronym;
        }

        public void setFieldAcronym(String fieldAcronym) {
            FieldAcronym = fieldAcronym;
        }

        public static class Question implements Serializable {

            String Question;
            String CategoryTitle;
            String FieldName;
            String FieldAcronym;
            int QuestionValue;

            public Question(String question) {
                Question = question;
            }

            public Question(String question,String categoryTitle, String fieldName, String fieldAcronym) {
                Question = question;
                this.FieldName = fieldName;
                this.FieldAcronym = fieldAcronym;
                this.CategoryTitle = categoryTitle;
            }


            public String getQuestion() {
                return Question;
            }

            public void setQuestion(String question) {
                Question = question;
            }

            public String getFieldName() {
                return FieldName;
            }

            public void setFieldName(String fieldName) {
                FieldName = fieldName;
            }

            public String getFieldAcronym() {
                return FieldAcronym;
            }

            public void setFieldAcronym(String fieldAcronym) {
                FieldAcronym = fieldAcronym;
            }

            public int getQuestionValue() {
                return QuestionValue;
            }

            public void setQuestionValue(int questionValue) {
                QuestionValue = questionValue;
            }

            public String getCategoryTitle() {
                return CategoryTitle;
            }

            public void setCategoryTitle(String categoryTitle) {
                CategoryTitle = categoryTitle;
            }
        }
    }

}
