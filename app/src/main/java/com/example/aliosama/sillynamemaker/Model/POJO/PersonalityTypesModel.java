package com.example.aliosama.sillynamemaker.Model.POJO;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ali Ussama on 6/15/2018.
 */

public class PersonalityTypesModel implements Serializable{

    private String CharacterImageUrl;
    private String CharacterTitle;
    private String CharacterAcronymText;
    private String Acronym;
    private String MinDescription;
    private String HeaderImageUrl;
    private String MidImageUrl;
    private ArrayList<FullDescription> mFullDescriptions;
    private ArrayList<YouMayKnow> mYouMayKnow;

    public PersonalityTypesModel() {
    }

    public PersonalityTypesModel(String characterImageUrl, String characterTitle, String characterAcronymText, String acronym, String minDescription) {
        CharacterImageUrl = characterImageUrl;
        CharacterTitle = characterTitle;
        CharacterAcronymText = characterAcronymText;
        Acronym = acronym;
        MinDescription = minDescription;
    }

    public PersonalityTypesModel(String headerImageUrl, String midImageUrl, ArrayList<FullDescription> mFullDescriptions, ArrayList<YouMayKnow> mYouMayKnow) {
        HeaderImageUrl = headerImageUrl;
        MidImageUrl = midImageUrl;
        this.mFullDescriptions = mFullDescriptions;
        this.mYouMayKnow = mYouMayKnow;
    }

    public String getCharacterImageUrl() {
        return CharacterImageUrl;
    }

    public void setCharacterImageUrl(String characterImageUrl) {
        CharacterImageUrl = characterImageUrl;
    }

    public String getCharacterTitle() {
        return CharacterTitle;
    }

    public void setCharacterTitle(String characterTitle) {
        CharacterTitle = characterTitle;
    }

    public String getCharacterAcronymText() {
        return CharacterAcronymText;
    }

    public void setCharacterAcronymText(String characterAcronymText) {
        CharacterAcronymText = characterAcronymText;
    }

    public String getAcronym() {
        return Acronym;
    }

    public void setAcronym(String acronym) {
        Acronym = acronym;
    }

    public String getMinDescription() {
        return MinDescription;
    }

    public void setMinDescription(String minDescription) {
        MinDescription = minDescription;
    }

    public String getHeaderImageUrl() {
        return HeaderImageUrl;
    }

    public void setHeaderImageUrl(String headerImageUrl) {
        HeaderImageUrl = headerImageUrl;
    }

    public String getMidImageUrl() {
        return MidImageUrl;
    }

    public void setMidImageUrl(String midImageUrl) {
        MidImageUrl = midImageUrl;
    }

    public ArrayList<FullDescription> getmFullDescriptions() {
        return mFullDescriptions;
    }

    public void setmFullDescriptions(ArrayList<FullDescription> mFullDescriptions) {
        this.mFullDescriptions = mFullDescriptions;
    }

    public ArrayList<YouMayKnow> getmYouMayKnow() {
        return mYouMayKnow;
    }

    public void setmYouMayKnow(ArrayList<YouMayKnow> mYouMayKnow) {
        this.mYouMayKnow = mYouMayKnow;
    }

    public static class YouMayKnow implements Serializable{
        String Name;
        String ImageUrl;

        public YouMayKnow() {

        }

        public YouMayKnow(String name, String imageUrl) {
            Name = name;
            ImageUrl = imageUrl;
        }

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }

        public String getImageUrl() {
            return ImageUrl;
        }

        public void setImageUrl(String imageUrl) {
            ImageUrl = imageUrl;
        }
    }

    public static class FullDescription implements Serializable{
        String Header;

        public FullDescription() {

        }

        public FullDescription(String header) {
            Header = header;
        }

        public String getHeader() {
            return Header;
        }

        public void setHeader(String header) {
            Header = header;
        }

    }
}
