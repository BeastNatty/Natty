package com.jeeps.charades.controller;

import android.widget.Toast;

import com.jeeps.charades.SetupGame;
import com.jeeps.charades.model.Game;

/**
 * Created by jeeps on 2/24/2017.
 */
public class Player implements Runnable {
    private Game game;

    public Player(Game game) {
        this.game = game;
    }

    @Override
    public void run() {

        while (game.isRunning()) {
            try {
                Thread.sleep(1000);
                game.tickTime();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public Game getGame() {
        return game;
    }
}
