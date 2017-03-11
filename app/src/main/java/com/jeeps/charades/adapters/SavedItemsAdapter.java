package com.jeeps.charades.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.jeeps.charades.R;
import com.jeeps.charades.SetupGame;
import com.jeeps.charades.model.Phrase;
import com.jeeps.charades.model.Topic;
import com.jeeps.charades.views.CustomButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jeeps on 3/11/2017.
 */

public class SavedItemsAdapter extends BaseAdapter {
    private Context context;
    private Topic[] items;
    LayoutInflater inflater;

    public SavedItemsAdapter(Context context, Topic[] items) {
        this.context = context;
        this.items = items;
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.saved_topic_layout, null);
        }
        final Topic currentTopic = items[position];
        CustomButton button = (CustomButton) convertView.findViewById(R.id.saved_item_text);
        button.setText(currentTopic.getName());
        //Set background
        button.setBackgroundResource(R.drawable.loaded_topics_background);
        GradientDrawable drawable = (GradientDrawable) button.getBackground();
        drawable.setColor(Color.parseColor(currentTopic.getColor()));

        //Set click
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadTopic(currentTopic);
            }
        });

        return convertView;
    }

    private void loadTopic(Topic currentTopic) {
        Intent intent = new Intent(context, SetupGame.class);
        List<Phrase> phrases = currentTopic.getPhrases();
        String[] loadedPhrases = new String[phrases.size()];
        for (int i = 0; i < phrases.size(); i++)
            loadedPhrases[i] = phrases.get(i).getText();

        intent.putExtra(SetupGame.LOADED_PHRASES, loadedPhrases);
        intent.putExtra(SetupGame.LOADING_LIST, true);

        context.startActivity(intent);
    }


    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public Object getItem(int position) {
        return items[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
