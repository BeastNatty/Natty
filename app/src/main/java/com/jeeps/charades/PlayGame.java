package com.jeeps.charades;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;

import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.jeeps.charades.controller.Player;
import com.jeeps.charades.model.CardColor;
import com.jeeps.charades.util.Timer;
import com.jeeps.charades.views.CustomTextView;
import com.jeeps.charades.model.Game;

import static com.jeeps.charades.SetupGame.*;

import java.util.ArrayList;
import java.util.Queue;
import java.util.Random;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.grantland.widget.AutofitTextView;

public class PlayGame extends AppCompatActivity
        implements Timer.TimerListener {

    protected static String CORRECT_SCORE = "correct score";
    protected static String INCORRECT_SCORE = "incorrect score";
    protected static String FINISH_EVENT = "finish event";
    public static int TIMES_UP = 0;
    public static int GUESSED_ALL = 1;

    private int duration;
    private ArrayList<String> words;
    private Queue<String> wordsQueue;

    @BindView(R.id.play_game_layout) RelativeLayout playGameLayout;
    @BindView(R.id.duration_counter) CustomTextView durationText;
    @BindView(R.id.words_text) AutofitTextView wordsText;
    @BindView(R.id.correct_button) ImageButton correctButton;
    @BindView(R.id.incorrect_button) ImageButton incorrectButton;
    @BindView(R.id.game_progress_bar) ProgressBar gameProgressBar;

    private Game game;

    private boolean isAnswering;
    private boolean gameFinished = false;
    private String previousColor;
    private int totalWords;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        //Hide action bar
        getSupportActionBar().hide();

        //Get previous values
        Intent intent = getIntent();
        words = intent.getStringArrayListExtra(WORDS_LIST);
        duration = intent.getIntExtra(DURATION, 0);

        //Setup progressBar
        gameProgressBar.setMax(words.size());

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
        totalWords = game.getWords().size();

        //Start Game
        game.startGame();
        //Setup Duration countdown
        timer = new Timer(this, game.getSeconds());
        timer.start();

        //Words
        Thread wordsThread = new Thread(() -> {
            for (String word : words) {
                runOnUiThread(() -> wordsText.setText(word));

                while (isAnswering) {
                    // break out of loop if game finished
                    if (gameFinished)
                        break;
                    try {
                        Thread.sleep(0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                isAnswering = true;
                // break out of loop if game finished
                if (gameFinished)
                    break;
            }
            if (!gameFinished)
                finishGame(game.getCorrect(), game.getIncorrect(), GUESSED_ALL);
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
        int wordsPassed = game.getCorrect() + game.getIncorrect();
        if (wordsPassed < totalWords) {
            //Change background color
            playGameLayout.setBackgroundColor(Color.parseColor(getRandomCardColor()));
            runOnUiThread(() -> {
                //Increment progress
                int progress = game.getCorrect() + game.getIncorrect();
                gameProgressBar.setProgress(progress);
            });
        }

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
        mp.setOnCompletionListener(MediaPlayer::release);
    }

    private void playIncorrectSound() {
        MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.incorrect);
        mp.start();
        mp.setOnCompletionListener(MediaPlayer::release);
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

    @Override
    public void onTick(int seconds) {
        runOnUiThread(() -> durationText.setText(seconds + ""));
        game.tickTime();
    }

    @Override
    public void onFinish() {
        finishGame(game.getCorrect(), game.getIncorrect(), TIMES_UP);
        timer.stop();
    }
}
