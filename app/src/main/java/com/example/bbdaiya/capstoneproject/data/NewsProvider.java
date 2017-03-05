package com.example.bbdaiya.capstoneproject.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by bbdaiya on 05-Mar-17.
 */

public class NewsProvider extends ContentProvider {

    final String LOG = NewsProvider.class.getSimpleName();
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static final SQLiteQueryBuilder sourceQueryBuilder = new SQLiteQueryBuilder();

    static {
        sourceQueryBuilder.setTables(NewsContract.SourceEntry.TABLE_NAME);
    }

    private static final SQLiteQueryBuilder articleQueryBuilder = new SQLiteQueryBuilder();

    static {
        articleQueryBuilder.setTables(NewsContract.ArticlesEntry.TABLE_NAME);
    }


    private NewsDbHelper newsDbHelper;

    static final int SOURCES = 100;
    static final int ARTICLES = 101;
    static final int SOURCES_LOCATION = 102;


    static UriMatcher buildUriMatcher() {

        UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        sUriMatcher.addURI(NewsContract.CONTENT_AUTHORITY, NewsContract.PATH_SOURCES, SOURCES);
        sUriMatcher.addURI(NewsContract.CONTENT_AUTHORITY, NewsContract.PATH_SOURCES, SOURCES_LOCATION);
        sUriMatcher.addURI(NewsContract.CONTENT_AUTHORITY, NewsContract.PATH_ARTICLES, ARTICLES);
        return sUriMatcher;

    }


    @Override
    public boolean onCreate() {
        newsDbHelper = new NewsDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor retCursor;

        switch (sUriMatcher.match(uri)) {
            case SOURCES: {
                retCursor = newsDbHelper.getReadableDatabase().query(NewsContract.SourceEntry.TABLE_NAME, projection,
                        selection, selectionArgs, null, null, sortOrder);
                break;
            }
            case ARTICLES: {
                retCursor = getArticles(uri, projection, sortOrder);
                break;
            }
            case SOURCES_LOCATION: {
                retCursor = getSourcesFromLocation(uri, projection, sortOrder);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri:" + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);


        switch (match) {
            case SOURCES:
                return NewsContract.SourceEntry.CONTENT_TYPE;
            case ARTICLES:
                return NewsContract.ArticlesEntry.CONTENT_TYPE;
            case SOURCES_LOCATION:
                return NewsContract.SourceEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri:" + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = newsDbHelper.getWritableDatabase();
        Uri returnUri;

        switch (sUriMatcher.match(uri)){
            case SOURCES: {
                long _id = db.insert(NewsContract.SourceEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = NewsContract.SourceEntry.buildSourceUri(_id);
                } else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case ARTICLES: {
                long _id = db.insert(NewsContract.ArticlesEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = NewsContract.ArticlesEntry.buildArticleUri(_id);
                } else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = newsDbHelper.getWritableDatabase();
        int returnRow;

        if(selection==null) selection="1";
        switch (sUriMatcher.match(uri)){
            case SOURCES: {
                returnRow = db.delete(NewsContract.SourceEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case ARTICLES: {
                String selection1 = NewsContract.ArticlesEntry.TABLE_NAME + "." + NewsContract.ArticlesEntry.COLUMN_SOURCE
                        + " = ? AND "+NewsContract.ArticlesEntry.TABLE_NAME + "." + NewsContract.ArticlesEntry.COLUMN_CATEGORY
                        + " = ?";
                String source = NewsContract.ArticlesEntry.getSourceFromUri(uri);
                String sortBy = NewsContract.ArticlesEntry.getCategoryFromUri(uri);
                String[] selectionArgs1 = new String[]{source, sortBy};
                returnRow = db.delete(NewsContract.ArticlesEntry.TABLE_NAME, selection1, selectionArgs1);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if(returnRow!=0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return returnRow;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = newsDbHelper.getWritableDatabase();
        int returnRow;

        if(selection==null) selection="1";
        switch (sUriMatcher.match(uri)){
            case SOURCES: {
                returnRow = db.update(NewsContract.SourceEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            case ARTICLES: {
                returnRow = db.update(NewsContract.ArticlesEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if(returnRow!=0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return returnRow;
    }
    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = newsDbHelper.getWritableDatabase();
        switch (sUriMatcher.match(uri)){
            case SOURCES:
                db.beginTransaction();
                int returnCount = 0;
                try{
                    for(ContentValues value: values){
                        long _id = db.insert(NewsContract.SourceEntry.TABLE_NAME, null, value);
                        if(_id!=-1){
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                }finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            case ARTICLES:
                db.beginTransaction();
                int rCount = 0;
                try{
                    for(ContentValues value: values){
                        long _id = db.insert(NewsContract.ArticlesEntry.TABLE_NAME, null, value);
                        if(_id!=-1){
                            rCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                }finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return rCount;
            default:
                return super.bulkInsert(uri, values);

        }


    }

    public Cursor getArticles(Uri uri, String[] projections, String sortOrder) {

        String selection = NewsContract.ArticlesEntry.TABLE_NAME + "." + NewsContract.ArticlesEntry.COLUMN_SOURCE
                + " = ? AND "+NewsContract.ArticlesEntry.TABLE_NAME + "." + NewsContract.ArticlesEntry.COLUMN_CATEGORY
                + " = ?";
        String source = NewsContract.ArticlesEntry.getSourceFromUri(uri);
        String sortBy = NewsContract.ArticlesEntry.getCategoryFromUri(uri);
        String[] selectionArgs = new String[]{source, sortBy};
        return sourceQueryBuilder.query(newsDbHelper.getReadableDatabase(), projections, selection, selectionArgs, null, null, NewsContract.ArticlesEntry._ID+" DESC ");
    }
    public Cursor getSourcesFromLocation(Uri uri, String[] projections, String sortOrder) {

        String selection = NewsContract.SourceEntry.TABLE_NAME + "." + NewsContract.SourceEntry.COLUMN_COUNTRY
                + " = ?";
        String location = NewsContract.SourceEntry.getLocationFromUri(uri);

        String[] selectionArgs = new String[]{location};
        return sourceQueryBuilder.query(newsDbHelper.getReadableDatabase(), projections, selection, selectionArgs, null, null, NewsContract.ArticlesEntry._ID+" DESC ");
    }
    @Override
    public void shutdown() {
        newsDbHelper.close();
        super.shutdown();
    }
}
