package com.example.bbdaiya.capstoneproject.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by bbdaiya on 05-Mar-17.
 */

public class NewsDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "news.db";
    public NewsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    final String SQL_CREATE_SOURCES_TABLE = "CREATE TABLE "+ NewsContract.SourceEntry.TABLE_NAME+
            " ( "+ NewsContract.SourceEntry._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
            NewsContract.SourceEntry.COLUMN_SOURCE_ID+" TEXT NOT NULL, "+
            NewsContract.SourceEntry.COLUMN_NAME+" TEXT NOT NULL, "+
            NewsContract.SourceEntry.COLUMN_COUNTRY+" TEXT, "+
            NewsContract.SourceEntry.COLUMN_LOGO_URL+" TEXT NOT NULL, UNIQUE("+
            NewsContract.SourceEntry.COLUMN_SOURCE_ID+") ON CONFLICT REPLACE);";

    final String SQL_CREATE_ARTICLES_TABLE = "CREATE TABLE "+ NewsContract.ArticlesEntry.TABLE_NAME+
            " ( "+ NewsContract.ArticlesEntry._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
            NewsContract.ArticlesEntry.COLUMN_SOURCE+" TEXT NOT NULL, "+
            NewsContract.ArticlesEntry.COLUMN_AUTHOR+" TEXT, "+
            NewsContract.ArticlesEntry.COLUMN_TITLE+" TEXT NOT NULL, "+
            NewsContract.ArticlesEntry.COLUMN_DESCRIPTION+" TEXT NOT NULL, "+
            NewsContract.ArticlesEntry.COLUMN_URL+" TEXT NOT NULL, "+
            NewsContract.ArticlesEntry.COLUMN_URLTOLOGO+" TEXT NOT NULL, "+
            NewsContract.ArticlesEntry.COLUMN_PUBLISHEDAT+" TEXT NOT NULL, "+
            NewsContract.ArticlesEntry.COLUMN_CATEGORY+" TEXT, UNIQUE("+
            NewsContract.ArticlesEntry.COLUMN_TITLE+", "+ NewsContract.ArticlesEntry.COLUMN_CATEGORY+
            ") ON CONFLICT REPLACE);";
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_SOURCES_TABLE);
        db.execSQL(SQL_CREATE_ARTICLES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + NewsContract.SourceEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + NewsContract.ArticlesEntry.TABLE_NAME);
        onCreate(db);
    }
}
