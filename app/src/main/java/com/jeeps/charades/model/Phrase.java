package com.jeeps.charades.model;

/**
 * Created by jeeps on 3/10/2017.
 */

public class Phrase {
    private int id = -1;
    private String text;

    public Phrase() {}

    public Phrase(int id, String text) {
        this.id = id;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public boolean hasBeenSaved() { return (getId() != -1); }
}
