package com.jeeps.charades;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.jeeps.charades.model.CardColor;
import com.jeeps.charades.model.CustomTextView;
import com.jeeps.charades.model.Game;

import static com.jeeps.charades.SetupGame.*;

import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.grantland.widget.AutofitTextView;

public class PlayGame extends AppCompatActivity {

    protected static String CORRECT_SCORE = "correct score";
    protected static String INCORRECT_SCORE = "incorrect score";
    protected static String FINISH_EVENT = "finish event";
    public static int TIMES_UP = 0;
    public static int GUESSED_ALL = 1;

    private int duration;
    private ArrayList<String> words;

    @BindView(R.id.play_game_layout) RelativeLayout playGameLayout;
    @BindView(R.id.duration_counter) CustomTextView durationText;
    @BindView(R.id.words_text) AutofitTextView wordsText;
    @BindView(R.id.correct_button) ImageButton correctButton;
    @BindView(R.id.incorrect_button) ImageButton incorrectButton;

    private Game game;

    private boolean isAnswering;
    private boolean gameFinished = false;
    private String previousColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        //Hide action bar
        getSupportActionBar().hide();

        //Get previous values
        Intent intent = getIntent();
        words = intent.getStringArrayListExtra(WORDS_LIST);
        duration = intent.getIntExtra(DURATION, 0);

        //Auto fit textView
        wordsText.setTypeface(Typeface.createFromAsset(getAssets(), CustomTextView.getFont()));
        //AutofitHelper.create(wordsText);

        //Initial background color
        playGameLayout.setBackgroundColor(Color.parseColor(getRandomCardColor()));
        setupGame();
    }

    private void setupGame() {
        //Create Game
        game = new Game(words, duration);
        isAnswering = true;

        //Start Game
        game.startGame();
        //Setup Duration countdown
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (game.isRunning()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            durationText.setText(game.getSeconds() + "");
                        }
                    });
                    game.tickTime();

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (!gameFinished)
                    finishGame(game.getCorrect(), game.getIncorrect(), TIMES_UP);
            }
        });
        thread.start();

        //Words
        Thread wordsThread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (final String word : words) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            wordsText.setText(word);
                        }
                    });

                    while (isAnswering) {
                        try {
                            Thread.sleep(0);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    isAnswering = true;
                }
                if (!gameFinished)
                    finishGame(game.getCorrect(), game.getIncorrect(), GUESSED_ALL);
            }
        });
        wordsThread.start();
    }

    @OnClick(R.id.correct_button)
    public void addCorrect() {
        //play sound
        playCorrectSound();

        //Increase count
        game.addCorrect();
        //Next word
        nextWord();
    }

    @OnClick(R.id.incorrect_button)
    public void addIncorrect() {
        //play sound
        playIncorrectSound();

        //Increase count
        game.addIncorrect();
        //Next word
        nextWord();
    }

    private void nextWord() {
        isAnswering = false;
        //Change background color
        playGameLayout.setBackgroundColor(Color.parseColor(getRandomCardColor()));
    }

    private void finishGame(int correct, int incorrect, int finishEvent) {
        gameFinished = true;
        Intent intent = new Intent(this, GameScore.class);
        intent.putExtra(CORRECT_SCORE, correct);
        intent.putExtra(INCORRECT_SCORE, incorrect);
        intent.putExtra(FINISH_EVENT, finishEvent);

        //Put previous game data
        intent.putExtra(WORDS_LIST, words);
        intent.putExtra(DURATION, duration);

        startActivity(intent);
        finish();
    }

    private void playCorrectSound() {
        MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.correct);
        mp.start();
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
    }

    private void playIncorrectSound() {
        MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.incorrect);
        mp.start();
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
    }

    private String getRandomCardColor() {
        int size = CardColor.values().length;
        Random random = new Random();
        boolean isNewColor = true;
        String color = "";
        while (isNewColor) {
            int randomIndex = random.nextInt(size);
            color = CardColor.values()[randomIndex].getHexCode();
            if (previousColor != color)
                isNewColor = false;
        }
        previousColor = color;
        return color;
    }

}
