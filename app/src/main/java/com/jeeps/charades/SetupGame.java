package com.jeeps.charades;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.jeeps.charades.adapters.CustomAdapterSpinner;
import com.jeeps.charades.dialogs.SaveTopicDialog;
import com.jeeps.charades.model.CardColor;
import com.jeeps.charades.model.CustomTextView;
import com.jeeps.charades.model.Game;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemLongClick;

public class SetupGame extends AppCompatActivity {
    protected static String WORDS_LIST = "words list";
    protected static String DURATION = "duration";

    protected MediaPlayer clickSound;

    @BindView(R.id.initial_layout) RelativeLayout initialLayout;
    @BindView(R.id.setup_layout) RelativeLayout setupLayout;

    @BindView(R.id.play_button) Button playButton;

    protected ArrayAdapter wordsAdapter;
    @BindView(R.id.words_list_view) ListView wordsListView;
    @BindView(R.id.add_word_button) ImageView addWordButton;
    @BindView(R.id.list_item_text) EditText listItemEditText;
    @BindView(R.id.start_button) Button startButton;
    @BindView(R.id.duration_edit_text) EditText durationEditText;

    //Settings drawer
    @BindView(R.id.settings_drawer) LinearLayout settingsDrawer;
    @BindView(R.id.settings_drawer_arrow) ImageView toggleDrawer;
    @BindView(R.id.load_list) ImageView loadList;
    @BindView(R.id.save_list) ImageView SaveList;

    private List<String> wordList;

    private boolean isDrawerOpened;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        getSupportActionBar().hide();

        //create click sound
        clickSound = MediaPlayer.create(getApplicationContext(), R.raw.click);

        //Soft keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        //Fonts
        Typeface tf = Typeface.createFromAsset(getAssets(), CustomTextView.getFont());
        listItemEditText.setTypeface(tf);
        startButton.setTypeface(tf);
        playButton.setTypeface(tf);

        //Setup layout
        wordList = new ArrayList<>();
        //Set adapter for words
        wordsAdapter = new ArrayAdapter(this, R.layout.list_item, wordList);
        wordsListView.setAdapter(wordsAdapter);
        isDrawerOpened = false;
    }

    @OnClick(R.id.play_button)
    public void play(View view) {
        playClickSound(getApplicationContext());

        transitionToSetup();
    }

    @OnItemLongClick(R.id.words_list_view)
    public boolean deleteAddedItem(AdapterView<?> parent, View view, int position, long id) {
        wordList.remove(position);
        wordsAdapter.notifyDataSetChanged();
        return true;
    }

    @OnClick(R.id.add_word_button)
    public void addWord() {
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

    @OnClick(R.id.start_button)
    public void startGame() {
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

    @OnClick(R.id.settings_drawer_arrow)
    public void toggleSettingsDrawer() {
        setToggleDrawer();
    }

    private void transitionToSetup() {
        setupLayout.setAlpha(0);
        setupLayout.setVisibility(View.VISIBLE);
        settingsDrawer.setAlpha(0);
        settingsDrawer.setVisibility(View.VISIBLE);
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(initialLayout, "alpha", 1, 0);
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(setupLayout, "alpha", 0, 1);
        ObjectAnimator fadeDrawerIn = ObjectAnimator.ofFloat(settingsDrawer, "alpha", 0, 1);

        AnimatorSet fade = new AnimatorSet();
        fade.playTogether(fadeOut, fadeIn, fadeDrawerIn);
        fade.setDuration(400);
        fade.start();
    }

    private void setToggleDrawer() {
        //Convert 100dp to pixels
        Resources r = getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, r.getDisplayMetrics());
        if (isDrawerOpened) {
            // Closing animator
            ObjectAnimator close = ObjectAnimator.ofFloat(settingsDrawer, "translationX", 0, px);
            close.setInterpolator(new AccelerateDecelerateInterpolator());
            close.start();

            //Change image
            toggleDrawer.setImageResource(R.drawable.left_arrow);
            isDrawerOpened = false;
        } else {
            // Opening animator
            ObjectAnimator open = ObjectAnimator.ofFloat(settingsDrawer, "translationX", px, 0);
            open.setInterpolator(new AccelerateDecelerateInterpolator());
            open.start();

            //Change image
            toggleDrawer.setImageResource(R.drawable.right_arrow);
            isDrawerOpened = true;
        }
    }

    public static void playClickSound(final Context context) {
        //Play click sound
        MediaPlayer mp = MediaPlayer.create(context, R.raw.click);
        mp.start();
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
    }

    @OnClick(R.id.save_list)
    public void saveTopic() {
        SaveTopicDialog dialog = new SaveTopicDialog(this.getApplicationContext());

        dialog.show(getFragmentManager(), "TAG");
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
        // as you spinnerecify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspinnerection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
