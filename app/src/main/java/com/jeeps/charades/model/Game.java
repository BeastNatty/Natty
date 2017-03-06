package com.jeeps.charades.model;

import java.util.Collections;
import java.util.List;

/**
 * Created by jeeps on 2/24/2017.
 */
public class Game {
    private List<String> mWords;
    private int mSeconds;
    private boolean isRunning;
    private int mCorrect;
    private int mIncorrect;

    public Game(List<String> words, int seconds) {
        mWords = words;
        mSeconds = seconds;
        isRunning = false;
    }

    public void startGame() {
        //Shuffle
        Collections.shuffle(mWords);
        isRunning = true;
    }

    public void tickTime() {
        mSeconds--;
        if (mSeconds <= 0)
            isRunning = false;
    }

    public void addCorrect() {
        mCorrect++;
    }

    public void addIncorrect() {
        mIncorrect++;
    }

    public List<String> getWords() {
        return mWords;
    }

    public int getSeconds() {
        return mSeconds;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public int getCorrect() {
        return mCorrect;
    }

    public int getIncorrect() {
        return mIncorrect;
    }
}
