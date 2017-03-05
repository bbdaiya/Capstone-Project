package com.example.bbdaiya.capstoneproject.Util;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;


import com.example.bbdaiya.capstoneproject.UI.NewsSourceCardAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by bbdaiya on 22-Feb-17.
 */

public class Utils{
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
        for(int i = 0; i < resultArr.length(); i++){
            JSONObject eachObject = resultArr.getJSONObject(i);
            NewsSource newsSource = new NewsSource();
            newsSource.setId(eachObject.getString(ID));
            newsSource.setName(eachObject.getString(NAME));
            newsSource.setCountry(eachObject.getString(COUNTRY));
            JSONObject logo = eachObject.getJSONObject(URLSTOLOGO);
            newsSource.setLogo_url(logo.getString(SMALL));
            newsSourcesList.add(newsSource);

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

            articleList.add(article);
            Log.v(Utils.class.getSimpleName(), article.getTitle());
        }
        return articleList;
    }
}