package com.example.aliosama.sillynamemaker.Model.POJO;

import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ali Ussama on 6/8/2018.
 */

public class PersonalityTestResultModel implements Serializable {

    private String TestResult;
    private Uri Image_URL;
    private ArrayList<Categories> mCategories;

    public PersonalityTestResultModel(){

    }

    public Uri getImage_URL() {
        return Image_URL;
    }

    public void setImage_URL(Uri image_URL) {
        Image_URL = image_URL;
    }

    public String getTestResult() {
        return TestResult;
    }

    public void setTestResult(String testResult) {
        TestResult = testResult;
    }

    public ArrayList<Categories> getmCategories() {
        return mCategories;
    }

    public void setmCategories(ArrayList<Categories> mCategories) {
        this.mCategories = mCategories;
    }

    public static class Categories implements Serializable{
        private String CategoryName;
        private String Category_Description;
        private String MostFieldName;
        private int MostFieldPercentage;
        private String MostFieldAcronym;
        private String LeastFieldName;
        private int LeastFieldPercentage;
        private String LeastFieldAcronym;

        public Categories() {

        }

        public String getCategoryName() {
            return CategoryName;
        }

        public void setCategoryName(String categoryName) {
            CategoryName = categoryName;
        }

        public String getCategory_Description() {
            return Category_Description;
        }

        public void setCategory_Description(String category_Description) {
            Category_Description = category_Description;
        }

        public String getMostFieldName() {
            return MostFieldName;
        }

        public void setMostFieldName(String mostFieldName) {
            MostFieldName = mostFieldName;
        }

        public int getMostFieldPercentage() {
            return MostFieldPercentage;
        }

        public void setMostFieldPercentage(int mostFieldPercentage) {
            MostFieldPercentage = mostFieldPercentage;
        }

        public String getLeastFieldName() {
            return LeastFieldName;
        }

        public void setLeastFieldName(String leastFieldName) {
            LeastFieldName = leastFieldName;
        }

        public int getLeastFieldPercentage() {
            return LeastFieldPercentage;
        }

        public void setLeastFieldPercentage(int leastFieldPercentage) {
            LeastFieldPercentage = leastFieldPercentage;
        }

        public String getMostFieldAcronym() {
            return MostFieldAcronym;
        }

        public void setMostFieldAcronym(String mostFieldAcronym) {
            MostFieldAcronym = mostFieldAcronym;
        }

        public String getLeastFieldAcronym() {
            return LeastFieldAcronym;
        }

        public void setLeastFieldAcronym(String leastFieldAcronym) {
            LeastFieldAcronym = leastFieldAcronym;
        }

    }



}
