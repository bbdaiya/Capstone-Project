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
}