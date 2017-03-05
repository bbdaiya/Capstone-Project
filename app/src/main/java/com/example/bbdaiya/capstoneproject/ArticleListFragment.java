package com.example.bbdaiya.capstoneproject;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.bbdaiya.capstoneproject.UI.ArticleListCardAdapter;
import com.example.bbdaiya.capstoneproject.UI.NewsSourceCardAdapter;
import com.example.bbdaiya.capstoneproject.Util.Article;
import com.example.bbdaiya.capstoneproject.Util.NewsSource;
import com.example.bbdaiya.capstoneproject.Util.Utils;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * A placeholder fragment containing a simple view.
 */
public class ArticleListFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArticleListCardAdapter adapter;
    private String id;
    public String sortOrder = "top";
    FetchArticleList fetchArticleList;
    public ArticleListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview =  inflater.inflate(R.layout.fragment_article_list, container, false);
        recyclerView = (RecyclerView) rootview.findViewById(R.id.recycler_view_article_list);


        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        String id = getActivity().getIntent().getExtras().getString("ID");
        fetchArticleList = new FetchArticleList(getContext(), id);
        getArticleList(sortOrder);
        return rootview;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_article_list, menu);

    }
    public void getArticleList(String sortOrder){
        fetchArticleList.execute(sortOrder);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.top:
                sortOrder = "top";
                break;
            case R.id.latest:
                sortOrder = "latest";
                break;
            default:
                sortOrder = "top";
        }
        getArticleList(sortOrder);
        return super.onOptionsItemSelected(item);
    }

    /*
            *
            *
            * Class for fetching news articles
            *
            *
             */
    public class FetchArticleList extends AsyncTask<String, Void, ArrayList<Article>> {
        final String LOG = FetchArticleList.class.getSimpleName();
        private ArticleListCardAdapter adapter;
        private Context mContext;
        private String id;

        public FetchArticleList(Context mContext, String id) {
            this.mContext = mContext;
            this.id = id;
        }

        @Override
        protected ArrayList<Article> doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String articlejson = null;

            try{
                final String BASE_URL = "https://newsapi.org/v1/articles";
                final String SOURCE = "source";
                final String APIKEY = "apiKey";
                final String SORTBY = "sortBy";
                Uri builtUri = Uri.parse(BASE_URL).buildUpon().appendQueryParameter(SOURCE, id)
                        .appendQueryParameter(SORTBY, String.valueOf(params[0]))
                        .appendQueryParameter(APIKEY, BuildConfig.API_KEY).build();
                URL url = null;
                try{
                    url = new URL(builtUri.toString());
                    Log.v(LOG, url.toString());
                }
                catch(MalformedURLException e){
                    e.printStackTrace();
                }
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream is = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if(is==null){
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(is));
                String line;
                while((line=reader.readLine())!=null){
                    buffer.append(line+"\n");           //for making debugging easy we add newline
                }
                if(buffer.length()==0){
                    //Stream is empty
                    return null;
                }

                articlejson = buffer.toString();
            }
            catch (IOException e){
                e.printStackTrace();
            }finally {
                if(urlConnection!=null){
                    urlConnection.disconnect();
                }
                if(reader!=null){
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            try {
                return Utils.getArticleData(articlejson);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(ArrayList<Article> articleList) {
            Log.v(LOG, articleList.get(0).getTitle());
            adapter = new ArticleListCardAdapter(getContext(), articleList);
            recyclerView.setAdapter(adapter);
            super.onPostExecute(articleList);
        }
    }


}
