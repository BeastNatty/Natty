package com.jeeps.charades;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.jeeps.charades.adapters.SavedItemsAdapter;
import com.jeeps.charades.database.TopicDataSource;
import com.jeeps.charades.model.CardColor;
import com.jeeps.charades.model.Topic;

import java.util.List;

import butterknife.OnItemClick;

public class SelectLoadedTopics extends AppCompatActivity {

    private Topic[] mTopics;

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
        mTopics = new Topic[loadedTopics.size()];
        mTopics = loadedTopics.toArray(mTopics);

        GridView gridView = (GridView) findViewById(R.id.saved_items_grid);
        gridView.setAdapter(new SavedItemsAdapter(this, mTopics));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(SelectLoadedTopics.this, mTopics[position].getName(), Toast.LENGTH_LONG).show();
            }
        });

    }

    /*@OnItemClick(R.id.saved_items_grid)
    public void onTopicClicked(AdapterView<?> parent, View view, int position, long id) {

    }*/

}
