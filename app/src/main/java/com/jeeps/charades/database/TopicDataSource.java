package com.jeeps.charades.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.jeeps.charades.model.Phrase;
import com.jeeps.charades.model.Topic;

import java.util.ArrayList;

/**
 * Created by jeeps on 3/10/2017.
 */

public class TopicDataSource {
    private Context mContext;
    private TopicSQLiteHelper mTopicSQLiteHelper;

    public TopicDataSource(Context context) {
        mContext = context;
        mTopicSQLiteHelper = new TopicSQLiteHelper(mContext);
    }

    //Open database
    private SQLiteDatabase open() {
        return mTopicSQLiteHelper.getWritableDatabase();
    }

    //READ
    public ArrayList<Topic> read() {
        ArrayList<Topic> topics = readTopics();
        addTopicPhrases(topics);
        return topics;
    }

    private ArrayList<Topic> readTopics() {
        SQLiteDatabase database = open();

        //Create cursor to iterate through table
        String[] columns = {BaseColumns._ID, TopicSQLiteHelper.COL_TOPIC_COLOR, TopicSQLiteHelper.COL_TOPIC_NAME};
        Cursor cursor = database.query(
                TopicSQLiteHelper.TOPIC_TABLE,
                columns,
                null, //selection
                null, //selection args
                null, //group by
                null, //having
                null //order
        );

        //topics
        ArrayList<Topic> topics = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                Topic topic = new Topic(
                        getIntFromColumnName(cursor, BaseColumns._ID),
                        getStringFromColumnName(cursor, TopicSQLiteHelper.COL_TOPIC_NAME),
                        getStringFromColumnName(cursor, TopicSQLiteHelper.COL_TOPIC_COLOR),
                        null
                );
                topics.add(topic);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return topics;
    }

    private void addTopicPhrases(ArrayList<Topic> topics) {
        SQLiteDatabase database = open();

        //Iterate through every topic and its words
        for (Topic topic : topics) {
            ArrayList<Phrase> phrases = new ArrayList<>();

            //Create cursor
            Cursor cursor = database.rawQuery(
                    "SELECT * FROM " + TopicSQLiteHelper.PHRASES_TABLE +
                            " WHERE " + TopicSQLiteHelper.COL_PHRASES_FOREIGN_TOPIC_ID + " = " + topic.getId(), null);

            if (cursor.moveToFirst()) {
                do {
                    Phrase phrase = new Phrase(
                            getIntFromColumnName(cursor, BaseColumns._ID),
                            getStringFromColumnName(cursor, TopicSQLiteHelper.COL_PHRASES_PHRASE)
                    );
                    phrases.add(phrase);
                } while(cursor.moveToNext());
            }

            topic.setPhrases(phrases);
            cursor.close();
        }
        database.close();
    }

    private int getIntFromColumnName(Cursor cursor, String colName) {
        int columnIndex = cursor.getColumnIndex(colName);
        return cursor.getInt(columnIndex);
    }

    private String getStringFromColumnName(Cursor cursor, String colName) {
        int columnIndex = cursor.getColumnIndex(colName);
        return cursor.getString(columnIndex);
    }

    //READING^^

    public void delete(int topicID) {
        SQLiteDatabase database = open();
        database.beginTransaction();

        //Delete phrases
        database.delete(TopicSQLiteHelper.PHRASES_TABLE,
                String.format("%s=%s", TopicSQLiteHelper.COL_PHRASES_FOREIGN_TOPIC_ID, String.valueOf(topicID)),
                null);
        //Delete topic
        database.delete(TopicSQLiteHelper.TOPIC_TABLE,
                String.format("%s=%s", BaseColumns._ID, String.valueOf(topicID)),
                null);

        //End transaction and close db
        database.setTransactionSuccessful();
        database.endTransaction();
        database.close();
    }

    public void create(Topic topic) {
        SQLiteDatabase database = open();
        //begin transaction
        database.beginTransaction();

        //Insert
        ContentValues topicValues = new ContentValues();
        topicValues.put(TopicSQLiteHelper.COL_TOPIC_NAME, topic.getName());
        topicValues.put(TopicSQLiteHelper.COL_TOPIC_COLOR, topic.getColor());
        long topicId = database.insert(TopicSQLiteHelper.TOPIC_TABLE, null, topicValues);

        //Insert phrases
        for (Phrase phrase : topic.getPhrases()) {
            ContentValues phraseValues = new ContentValues();
            phraseValues.put(TopicSQLiteHelper.COL_PHRASES_PHRASE, phrase.getText());
            phraseValues.put(TopicSQLiteHelper.COL_PHRASES_FOREIGN_TOPIC_ID, topicId);

            database.insert(TopicSQLiteHelper.PHRASES_TABLE, null, phraseValues);
        }

        //commit transaction
        database.setTransactionSuccessful();
        database.endTransaction();
        database.close();
    }


}
