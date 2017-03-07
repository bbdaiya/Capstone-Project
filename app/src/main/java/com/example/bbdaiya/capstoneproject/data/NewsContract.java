package com.example.bbdaiya.capstoneproject.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * Created by bbdaiya on 05-Mar-17.
 */

public class NewsContract {

    public static final String CONTENT_AUTHORITY = "com.example.bbdaiya.capstoneproject";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+CONTENT_AUTHORITY);

    public static final String PATH_SOURCES = "sources";
    public static final String PATH_ARTICLES = "articles";


    public static final class SourceEntry implements BaseColumns{
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_SOURCES).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SOURCES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SOURCES;

        public static final String TABLE_NAME = "sources";

        public static final String COLUMN_SOURCE_ID = "id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_COUNTRY = "country";
        public static final String COLUMN_LOGO_URL = "logo_url";

        public static Uri buildSourceUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
        public static Uri buildSourceUriLocation(String country){
            return CONTENT_URI.buildUpon().appendPath(country).build();
        }
        public static String getLocationFromUri(Uri uri){
            Log.v(SourceEntry.class.getSimpleName(), uri.toString());
            return uri.getPathSegments().get(1);
        }
    }

    public static final class ArticlesEntry implements BaseColumns{
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_ARTICLES).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ARTICLES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ARTICLES;

        public static final String TABLE_NAME = "articles";

        public static final String COLUMN_SOURCE = "source";
        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_URL = "url";
        public static final String COLUMN_URLTOLOGO = "urlToLogo";
        public static final String COLUMN_PUBLISHEDAT = "publishedAt";
        public static final String COLUMN_CATEGORY = "category";

        public static String getSourceFromUri(Uri uri){
            return uri.getPathSegments().get(1);
        }
        public static String getCategoryFromUri(Uri uri){
            return uri.getPathSegments().get(2);
        }
        public static Uri buildArticleUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
        public static Uri buildArticleUriCategory(String source, String category){
            return CONTENT_URI.buildUpon().appendPath(source).appendPath(category).build();
        }
    }

}
