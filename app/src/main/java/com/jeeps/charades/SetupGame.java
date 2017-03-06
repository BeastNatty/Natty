package com.jeeps.charades;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.jeeps.charades.controller.Player;
import com.jeeps.charades.model.CustomTextView;
import com.jeeps.charades.model.Game;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class SetupGame extends AppCompatActivity {
    protected static String WORDS_LIST = "words list";
    protected static String DURATION = "duration";

    protected MediaPlayer clickSound;

    protected RelativeLayout initialLayout;
    protected RelativeLayout setupLayout;

    protected Button playButton;

    protected ArrayAdapter wordsAdapter;
    protected ListView wordsListView;
    protected ImageView addWordButton;
    protected EditText listItemEditText;
    protected Button startButton;

    private List<String> wordList;
    private Game game;

    protected EditText durationEditText;

    public CustomTextView test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().hide();

        //create click sound
        clickSound = MediaPlayer.create(getApplicationContext(), R.raw.click);

        //Soft keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        // Initialize vies
        initialLayout = (RelativeLayout) findViewById(R.id.initial_layout);
        playButton = (Button) findViewById(R.id.play_button);

        setupLayout = (RelativeLayout) findViewById(R.id.setup_layout);
        wordsListView = (ListView) findViewById(R.id.words_list_view);
        addWordButton = (ImageView) findViewById(R.id.add_word_button);
        listItemEditText = (EditText) findViewById(R.id.list_item_text);
        durationEditText = (EditText) findViewById(R.id.duration_edit_text);
        startButton = (Button) findViewById(R.id.start_button);

        //Fonts
        Typeface tf = Typeface.createFromAsset(getAssets(), CustomTextView.getFont());
        listItemEditText.setTypeface(tf);
        startButton.setTypeface(tf);
        playButton.setTypeface(tf);

        //Initial layout
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playClickSound(getApplicationContext());

                switchLayouts();
            }
        });

        //Setup layout
        wordList = new ArrayList<>();
        //Set adapter for words
        wordsAdapter = new ArrayAdapter(this, R.layout.list_item, wordList);
        wordsListView.setAdapter(wordsAdapter);

        //Ability to delete single words
        wordsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                wordList.remove(position);
                wordsAdapter.notifyDataSetChanged();
                return true;
            }
        });

        addWordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String word = listItemEditText.getText().toString();

                if (!word.isEmpty()) {
                    wordList.add(word);
                    wordsAdapter.notifyDataSetChanged();

                    //Empty edit text
                    listItemEditText.setText("");
                } else {
                    Toast.makeText(SetupGame.this, "Can't add an empty word", Toast.LENGTH_LONG).show();
                }
            }
        });

        //Start game
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playClickSound(getApplicationContext());

                try {
                    // Get duration
                    int duration = Integer.parseInt(durationEditText.getText().toString());
                    if (wordList.isEmpty()) {
                        Toast.makeText(SetupGame.this, "Please add some words", Toast.LENGTH_LONG).show();
                    } else {

                        // pass game data to next activity
                        Intent intent = new Intent(SetupGame.this, PlayGame.class);
                        intent.putExtra(WORDS_LIST, (ArrayList<String>) wordList);
                        intent.putExtra(DURATION, duration);

                        startActivity(intent);
                        finish();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(SetupGame.this, "That is not a valid number for the duration", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void testGame() {
        /*final Player player = new Player(game);
        Thread thread = new Thread(player);
        thread.start();
        player.getGame().startGame();
*/
        int duration = Integer.parseInt(durationEditText.getText().toString());
        game = new Game(wordList, duration);
        game.startGame();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (game.isRunning()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            test.setText(game.getSeconds() + "");
                        }
                    });
                    game.tickTime();

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        thread.start();

    }

    public static void playClickSound(final Context context) {
        //Play click sound
        /*Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                MediaPlayer mp = MediaPlayer.create(context, R.raw.click);
                mp.start();
            }
        });
        t.start();*/

        MediaPlayer mp = MediaPlayer.create(context, R.raw.click);
        mp.start();
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
    }

    private void switchLayouts() {
        initialLayout.setVisibility(View.INVISIBLE);
        setupLayout.setVisibility(View.VISIBLE);

        //Backgrounds
        RelativeLayout mainBG = (RelativeLayout) findViewById(R.id.main_background);
        RelativeLayout secondBG = (RelativeLayout) findViewById(R.id.second_background);

        mainBG.setVisibility(View.INVISIBLE);
        secondBG.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
