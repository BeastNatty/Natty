package com.jeeps.charades;

import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;


import com.jeeps.charades.model.CustomTextView;

import java.util.ArrayList;

import static com.jeeps.charades.PlayGame.*;
import static com.jeeps.charades.SetupGame.*;

public class GameScore extends AppCompatActivity {

    protected MediaPlayer clickSound;

    protected CustomTextView message;
    protected CustomTextView correctText;
    protected CustomTextView incorrectText;
    protected Button playAgainButton;
    protected Button restartButton;

    private ArrayList<String> words;
    private int duration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_score);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().hide();

        //Create click sound
        clickSound = MediaPlayer.create(getApplicationContext(), R.raw.click);

        //Get previous values
        final Intent intent = getIntent();
        int correct = intent.getIntExtra(CORRECT_SCORE, 0);
        int incorrect = intent.getIntExtra(INCORRECT_SCORE, 0);
        int finishEvent = intent.getIntExtra(FINISH_EVENT, 0);

        //Get game values
        words = intent.getStringArrayListExtra(WORDS_LIST);
        duration = intent.getIntExtra(DURATION, 0);

        //Set Views
        message = (CustomTextView) findViewById(R.id.message);
        correctText = (CustomTextView) findViewById(R.id.correct_score);
        incorrectText = (CustomTextView) findViewById(R.id.incorrect_score);
        playAgainButton = (Button) findViewById(R.id.play_again_button);
        restartButton = (Button) findViewById(R.id.restart_button);

        //fonts for numbers
        Typeface typeface = Typeface.createFromAsset(getAssets(), CustomTextView.ARCHITECTS_DAUGHTER);
        correctText.setTypeface(typeface);
        incorrectText.setTypeface(typeface);

        //Fonts for buttons
        Typeface tf = Typeface.createFromAsset(getAssets(), CustomTextView.getFont());
        playAgainButton.setTypeface(tf);
        restartButton.setTypeface(tf);

        //Set values
        if (finishEvent == GUESSED_ALL)
            message.setText(R.string.completed_message);
        else if (finishEvent == TIMES_UP)
            message.setText(R.string.times_up_message);

        correctText.setText(correct + "");
        incorrectText.setText(incorrect + "");

        playAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playClickSound(getApplicationContext());

                Intent playAgain = new Intent(GameScore.this, PlayGame.class);
                playAgain.putExtra(WORDS_LIST, words);
                playAgain.putExtra(DURATION, duration);
                startActivity(playAgain);
                finish();
            }
        });

        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playClickSound(getApplicationContext());

                Intent restart = new Intent(GameScore.this, SetupGame.class);
                startActivity(restart);
                finish();
            }
        });
    }

}
