package com.jeeps.charades;

import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.jeeps.charades.model.CustomTextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.jeeps.charades.PlayGame.*;
import static com.jeeps.charades.SetupGame.*;

public class GameScore extends AppCompatActivity {

    protected MediaPlayer clickSound;

    @BindView(R.id.message) CustomTextView message;
    @BindView(R.id.correct_score) CustomTextView correctText;
    @BindView(R.id.incorrect_score) CustomTextView incorrectText;
    @BindView(R.id.play_again_button) Button playAgainButton;
    @BindView(R.id.restart_button) Button restartButton;

    private ArrayList<String> words;
    private int duration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_score);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        getSupportActionBar().hide();

        //Create click sound
        clickSound = MediaPlayer.create(getApplicationContext(), R.raw.click);

        //fonts for numbers
        Typeface typeface = Typeface.createFromAsset(getAssets(), CustomTextView.ARCHITECTS_DAUGHTER);
        correctText.setTypeface(typeface);
        incorrectText.setTypeface(typeface);

        //Fonts for buttons
        Typeface tf = Typeface.createFromAsset(getAssets(), CustomTextView.getFont());
        playAgainButton.setTypeface(tf);
        restartButton.setTypeface(tf);

        displayScore();
    }

    private void displayScore() {
        //Get previous values
        final Intent intent = getIntent();
        int correct = intent.getIntExtra(CORRECT_SCORE, 0);
        int incorrect = intent.getIntExtra(INCORRECT_SCORE, 0);
        int finishEvent = intent.getIntExtra(FINISH_EVENT, 0);

        //Get game values
        words = intent.getStringArrayListExtra(WORDS_LIST);
        duration = intent.getIntExtra(DURATION, 0);

        //Set values
        if (finishEvent == GUESSED_ALL)
            message.setText(R.string.completed_message);
        else if (finishEvent == TIMES_UP)
            message.setText(R.string.times_up_message);

        correctText.setText(correct + "");
        incorrectText.setText(incorrect + "");
    }

    @OnClick(R.id.play_again_button)
    public void playAgain() {
        playClickSound(getApplicationContext());

        Intent playAgain = new Intent(GameScore.this, PlayGame.class);
        playAgain.putExtra(WORDS_LIST, words);
        playAgain.putExtra(DURATION, duration);
        startActivity(playAgain);
        finish();
    }

    @OnClick(R.id.restart_button)
    public void restartGame() {
        playClickSound(getApplicationContext());

        Intent restart = new Intent(GameScore.this, SetupGame.class);
        startActivity(restart);
        finish();
    }

}
