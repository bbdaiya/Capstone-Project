package com.example.bbdaiya.capstoneproject;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.Toast;

import com.example.bbdaiya.capstoneproject.UI.ArticleListAdapter;
import com.example.bbdaiya.capstoneproject.Util.Article;
import com.example.bbdaiya.capstoneproject.Util.Utils;
import com.example.bbdaiya.capstoneproject.data.NewsContract;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

/**
 * A placeholder fragment containing a simple view.
 */
public class ArticleListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public final int LOADER_ID = 2;
    private RecyclerView recyclerView;
    private ArticleListAdapter adapter;
    String source_id;
    public String sortOrder = "top";
    FetchArticleList fetchArticleList;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview =  inflater.inflate(R.layout.fragment_article_list, container, false);
        recyclerView = (RecyclerView) rootview.findViewById(R.id.recycler_view_article_list);
        adapter = new ArticleListAdapter(getActivity());


        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        if(!Utils.isTablet(getActivity())) {
            source_id = getActivity().getIntent().getExtras().getString("ID");
        }
        else{
            Bundle bundle = getArguments();
            source_id = bundle.getString("ID");
        }
        recyclerView.setAdapter(adapter);
        fetchArticleList = new FetchArticleList(getActivity(), source_id);
        fetchArticleList.execute(sortOrder);
        getLoaderManager().initLoader(LOADER_ID, null, this);
        return rootview;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_article_list, menu);

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
            case R.id.action_settings:
                Intent intent = new Intent(getActivity(),SettingsActivity.class);
                startActivity(intent);
                break;
            default:
                sortOrder = "top";
        }

        getLoaderManager().restartLoader(LOADER_ID, null, this);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = NewsContract.ArticlesEntry.buildArticleUriCategory(source_id, sortOrder);
        Log.v(ArticleListFragment.class.getSimpleName(), uri.toString());
        if(sortOrder.equals("top")){
            Toast.makeText(getActivity(), R.string.showing_top, Toast.LENGTH_SHORT).show();
        }
        else if (sortOrder.equals("latest")){
            Toast.makeText(getActivity(), R.string.showing_latest, Toast.LENGTH_SHORT).show();

        }
        CursorLoader cursorLoader = new CursorLoader(this.getActivity(),
                uri,
                null,
                null,
                null,
                null);

        return cursorLoader;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data!=null) {
            adapter.setCursor(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.setCursor(null);
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
        private ArticleListAdapter adapter;
        private Context mContext;
        private String id;
        boolean invalidApiKey = false;
        Utils utils = new Utils(getContext());
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

                if(urlConnection.getResponseCode()==401) {



                                invalidApiKey = true;
                                if (Utils.checkConnection(getContext())) {
                                    Log.v(LOG, "checkConnection true");
                                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                            getContext());

                                    // set title
                                    alertDialogBuilder.setTitle(getContext().getString(R.string.api_key));

                                    // set dialog message
                                    alertDialogBuilder
                                            .setMessage(getContext().getString(R.string.invalid_api_key))
                                            .setCancelable(false)
                                            .setPositiveButton(getContext().getString(R.string.exit), new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    // if this button is clicked, close
                                                    // current activity
                                                    Intent intent = new Intent(getActivity(), MainActivity.class);
                                                    intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP);
                                                    getActivity().startActivity(intent);

                                                }
                                            });

                                    Activity activity = getActivity();
                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            // create alert dialog
                                            AlertDialog alertDialog = alertDialogBuilder.create();

                                            // show it
                                            alertDialog.show();
                                        }
                                    });

                                }


                }
                InputStream is = urlConnection.getInputStream();
                Log.v(LOG, is.toString());
                StringBuffer buffer = new StringBuffer();
                if(is==null){
                    Log.v(LOG, "is is null");
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
                Log.v(LOG, "IOEXception");
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
                if(!invalidApiKey)
                    return utils.getArticleData(articlejson);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

    }


}
