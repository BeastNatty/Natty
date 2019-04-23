package com.jeeps.charades.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.jeeps.charades.R;
import com.jeeps.charades.SetupGame;
import com.jeeps.charades.database.TopicDataSource;
import com.jeeps.charades.model.Phrase;
import com.jeeps.charades.model.Topic;
import com.jeeps.charades.views.CustomButton;
import com.jeeps.charades.views.CustomTextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.appcompat.app.AlertDialog;

/**
 * Created by jeeps on 3/11/2017.
 */

public class SavedItemsAdapter extends BaseAdapter {
    private Context context;
    private Topic[] items;
    private Topic[] itemsBeforeDelete;
    LayoutInflater inflater;
    TopicDataSource mTopicDataSource;

    private boolean deleted;

    public SavedItemsAdapter(Context context, Topic[] items) {
        this.context = context;
        this.items = items;
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mTopicDataSource = new TopicDataSource(context);
    }

    public View getView(int position, View convertView, final ViewGroup parent) {
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
        //Set font
        Typeface tf = Typeface.createFromAsset(context.getAssets(), CustomTextView.getFont());
        button.setTypeface(tf);

        //Set click
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadTopic(currentTopic);
            }
        });
        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                promptForDeletion(currentTopic, parent);
                return true;
            }
        });

        return convertView;
    }

    private void promptForDeletion(final Topic currentTopic, final ViewGroup viewGroup) {
        deleted = true;
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Delete Topic?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //Prompt undo un snackbar
                        Snackbar snackbar = Snackbar.make(viewGroup, "TOPIC DELETED",
                                Snackbar.LENGTH_SHORT)
                                .setAction("UNDO", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //UNDO deletion
                                        items = itemsBeforeDelete;
                                        notifyDataSetChanged();
                                        deleted = false;
                                    }
                                });

                        //Callback for snackbar when dismissed
                        snackbar.addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                            @Override
                            public void onDismissed(Snackbar transientBottomBar, int event) {
                                super.onDismissed(transientBottomBar, event);
                                //Delete item if it action wasn't undone
                                if (deleted) {
                                    mTopicDataSource.delete(currentTopic.getId());

                                    //Update topics
                                    List<Topic> loadedTopics = mTopicDataSource.read();
                                    items = listToArray(loadedTopics);

                                    notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onShown(Snackbar transientBottomBar) {
                                super.onShown(transientBottomBar);
                                //remove topic from local dataset
                                List<Topic> topics = new ArrayList<>(Arrays.asList(items));
                                topics.remove(currentTopic);
                                itemsBeforeDelete = items;
                                items = listToArray(topics);

                                notifyDataSetChanged();
                            }
                        });
                        snackbar.show();

                        dialog.dismiss();

//                        Toast.makeText(context, "Topic Deleted", Toast.LENGTH_SHORT).show();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        alertDialog.show();
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

    private Topic[] listToArray(List<Topic> topics) {
        Topic[] topicsA = new Topic[topics.size()];
        topicsA = topics.toArray(topicsA);
        return topicsA;
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
