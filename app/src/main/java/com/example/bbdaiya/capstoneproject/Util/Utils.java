package com.example.bbdaiya.capstoneproject.Util;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;


import com.example.bbdaiya.capstoneproject.data.NewsContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by bbdaiya on 22-Feb-17.
 */

public class Utils{
    public static Context context;

    public Utils(Context context) {
        this.context = context;
    }

    final String LOG = Utils.class.getSimpleName();
    public static ArrayList<NewsSource> getSourceData(String newssourcejson) throws JSONException{

        final String SOURCES = "sources";
        final String ID = "id";
        final String NAME = "name";
        final String COUNTRY = "country";
        final String URLSTOLOGO = "urlsToLogos";
        final String SMALL = "small";

        JSONObject sources = new JSONObject(newssourcejson);
        JSONArray resultArr = sources.getJSONArray(SOURCES);

        ArrayList<NewsSource> newsSourcesList = new ArrayList<>();
        Vector<ContentValues> cVVector = new Vector<ContentValues>(resultArr.length());
        for(int i = 0; i < resultArr.length(); i++){
            JSONObject eachObject = resultArr.getJSONObject(i);
            NewsSource newsSource = new NewsSource();
            newsSource.setId(eachObject.getString(ID));
            newsSource.setName(eachObject.getString(NAME));
            newsSource.setCountry(eachObject.getString(COUNTRY));
            JSONObject logo = eachObject.getJSONObject(URLSTOLOGO);
            newsSource.setLogo_url(logo.getString(SMALL));

            ContentValues contentValues = new ContentValues();
            contentValues.put(NewsContract.SourceEntry.COLUMN_SOURCE_ID, newsSource.getId());
            contentValues.put(NewsContract.SourceEntry.COLUMN_NAME, newsSource.getName());
            contentValues.put(NewsContract.SourceEntry.COLUMN_COUNTRY, newsSource.getCountry());
            contentValues.put(NewsContract.SourceEntry.COLUMN_LOGO_URL, newsSource.getLogo_url());
            cVVector.add(contentValues);
            newsSourcesList.add(newsSource);

        }
        if(cVVector.size()>0){

            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);
            context.getContentResolver().bulkInsert(NewsContract.SourceEntry.CONTENT_URI, cvArray);
        }
        return newsSourcesList;
    }

    public static ArrayList<Article> getArticleData(String articlejson) throws JSONException{

        final String SOURCE = "source";
        final String ARTICLES = "articles";
        final String AUTHOR = "author";
        final String TITLE = "title";
        final String DESCRIPTION = "description";
        final String URL = "url";
        final String URLTOIMAGE = "urlToImage";
        final String PUBLISHEDAT = "publishedAt";
        final String SORTBY = "sortBy";

        JSONObject list = new JSONObject(articlejson);
        String source = list.getString(SOURCE);
        String sortBy = list.getString(SORTBY);
        JSONArray resultArr = list.getJSONArray(ARTICLES);
        Vector<ContentValues> cVVector = new Vector<ContentValues>(resultArr.length());
        ArrayList<Article> articleList = new ArrayList<>();
        for(int i = 0; i < resultArr.length(); i++){
            JSONObject eachObject = resultArr.getJSONObject(i);
            Article article= new Article(
                    source,
                    eachObject.getString(AUTHOR),
                    eachObject.getString(TITLE),
                    eachObject.getString(DESCRIPTION),
                    eachObject.getString(URL),
                    eachObject.getString(URLTOIMAGE),
                    eachObject.getString(PUBLISHEDAT),
                    sortBy
            );
            ContentValues contentValues = new ContentValues();
            contentValues.put(NewsContract.ArticlesEntry.COLUMN_SOURCE, article.getSource());
            contentValues.put(NewsContract.ArticlesEntry.COLUMN_AUTHOR, article.getAuthor());
            contentValues.put(NewsContract.ArticlesEntry.COLUMN_TITLE, article.getTitle());
            contentValues.put(NewsContract.ArticlesEntry.COLUMN_DESCRIPTION, article.getDescription());
            contentValues.put(NewsContract.ArticlesEntry.COLUMN_PUBLISHEDAT, article.getPublishedAt());
            contentValues.put(NewsContract.ArticlesEntry.COLUMN_URL, article.getUrl());
            contentValues.put(NewsContract.ArticlesEntry.COLUMN_URLTOLOGO, article.getUrlToImage());
            contentValues.put(NewsContract.ArticlesEntry.COLUMN_CATEGORY, article.getCategory());
            cVVector.add(contentValues);
            articleList.add(article);
            Log.v(Utils.class.getSimpleName(), article.getTitle());
        }
        if(cVVector.size()>0){

            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);
            context.getContentResolver().bulkInsert(NewsContract.ArticlesEntry.CONTENT_URI, cvArray);
        }
        return articleList;
    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

}