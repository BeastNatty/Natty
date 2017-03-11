package com.jeeps.charades;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.GridView;

import com.jeeps.charades.adapters.SavedItemsAdapter;
import com.jeeps.charades.database.TopicDataSource;
import com.jeeps.charades.model.CardColor;
import com.jeeps.charades.model.Topic;

import java.util.List;

public class SelectLoadedTopics extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_loaded_topics);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().hide();

        //Load saved Items
        TopicDataSource dataSource = new TopicDataSource(this);
        List<Topic> loadedTopics = dataSource.read();
        //Convert to array
        Topic[] topics = new Topic[loadedTopics.size()];
        topics = loadedTopics.toArray(topics);

        GridView gridView = (GridView) findViewById(R.id.saved_items_grid);
        gridView.setAdapter(new SavedItemsAdapter(this, topics));
    }

}
