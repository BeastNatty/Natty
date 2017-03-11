package com.jeeps.charades.dialogs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.jeeps.charades.R;
import com.jeeps.charades.adapters.CustomAdapterSpinner;
import com.jeeps.charades.database.TopicDataSource;
import com.jeeps.charades.model.CardColor;
import com.jeeps.charades.model.Phrase;
import com.jeeps.charades.model.Topic;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jeeps on 3/10/2017.
 */

@SuppressLint("ValidFragment")
public class SaveTopicDialog extends DialogFragment {

    TopicDataSource mTopicDataSource;
    Context mContext;
    String mSelectedColor;

    @SuppressLint("ValidFragment")
    public SaveTopicDialog(Context context) {
        super();
        mContext = context;
        //Initialize datasource
        mTopicDataSource = new TopicDataSource(mContext);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setTitle("Save topic");

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View layout = inflater.inflate(R.layout.save_topic_dialog, null);

        //Edit text
        final EditText editText = (EditText) layout.findViewById(R.id.topic_name);

        //Spinner
        final Spinner spinner = (Spinner) layout.findViewById(R.id.color_spinner);
        CustomAdapterSpinner adapterSpinner = new CustomAdapterSpinner(layout.getContext(), CardColor.values());
        spinner.setAdapter(adapterSpinner);

        builder.setView(layout)
                // Add action buttons
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String topicName = editText.getText().toString();

                        String colorName = spinner.getSelectedItem().toString().toUpperCase();
                        CardColor cardColor = CardColor.valueOf(colorName);

                        if (!topicName.isEmpty())
                            saveTopic(topicName, cardColor.getHexCode());
                        else
                            Toast.makeText(mContext, "Please enter a topic name", Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SaveTopicDialog.this.getDialog().cancel();
                    }
                });


        return builder.create();
    }

    private void saveTopic(String name, String hexColor) {
        List<Phrase> phrases = new ArrayList<>();
        phrases.add(new Phrase(1, "Potato"));
        phrases.add(new Phrase(1, "Potato"));
        phrases.add(new Phrase(1, "Potato"));
        phrases.add(new Phrase(1, "Potato"));

        Topic topic = new Topic(1, name, hexColor, phrases);
        mTopicDataSource.create(topic);
    }


}
