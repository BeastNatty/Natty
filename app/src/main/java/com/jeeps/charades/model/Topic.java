package com.jeeps.charades.model;

import java.util.List;

/**
 * Created by jeeps on 3/10/2017.
 */

public class Topic {
    private int id;
    private String mName;
    private String mColor;
    private List<Phrase> mPhrases;


    public Topic(int id, String name, String color, List<Phrase> phrases) {
        this.id = id;
        mName = name;
        mColor = color;
        mPhrases = phrases;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getColor() {
        return mColor;
    }

    public void setColor(String color) {
        mColor = color;
    }

    public List<Phrase> getPhrases() {
        return mPhrases;
    }

    public void setPhrases(List<Phrase> phrases) {
        mPhrases = phrases;
    }
}
