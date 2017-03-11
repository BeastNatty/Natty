package com.jeeps.charades.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by jeeps on 3/10/2017.
 */

public class TopicSQLiteHelper extends SQLiteOpenHelper {
    //Database config
    private static final String DB_NAME = "topics.db";
    private static final int DB_VERSION = 1;

    //Topics table
    public static final String TOPIC_TABLE = "TOPICS";
    public static final String COL_TOPIC_COLOR = "COLOR";
    public static final String COL_TOPIC_NAME = "NAME";

    private static final String CREATE_TOPICS =
            "CREATE TABLE " + TOPIC_TABLE +
                    " (" + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COL_TOPIC_COLOR + " TEXT, " +
                    COL_TOPIC_NAME + " TEXT)";

    //Phrases table
    public static final String PHRASES_TABLE = "PHRASES";
    public static final String COL_PHRASES_PHRASE = "PHRASE";
    public static final String COL_PHRASES_FOREIGN_TOPIC_ID = "TOPIC_ID";

    private static final String CREATE_PHRASES =
            "CREATE TABLE " + PHRASES_TABLE +
                    " (" + BaseColumns._ID +
                    " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_PHRASES_PHRASE + " TEXT, " +
                    COL_PHRASES_FOREIGN_TOPIC_ID + " INTEGER, " +
                    "FOREIGN KEY(" + COL_PHRASES_FOREIGN_TOPIC_ID + ") REFERENCES MEMES(_ID))";


    public TopicSQLiteHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TOPICS);
        db.execSQL(CREATE_PHRASES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
