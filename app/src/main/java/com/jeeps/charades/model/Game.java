package com.jeeps.charades.model;

import java.util.Collections;
import java.util.List;

/**
 * Created by jeeps on 2/24/2017.
 */
public class Game {
    private int mCorrect;
    private int mIncorrect;

    public void addCorrect() {
        mCorrect++;
    }

    public void addIncorrect() {
        mIncorrect++;
    }

    public int getCorrect() {
        return mCorrect;
    }

    public int getIncorrect() {
        return mIncorrect;
    }
}
