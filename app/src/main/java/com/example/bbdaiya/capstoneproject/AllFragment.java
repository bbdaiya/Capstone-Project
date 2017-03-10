package com.example.bbdaiya.capstoneproject;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bbdaiya.capstoneproject.UI.NewsSourceAdapter;
import com.example.bbdaiya.capstoneproject.Util.NewsSource;
import com.example.bbdaiya.capstoneproject.Util.Utils;
import com.example.bbdaiya.capstoneproject.data.NewsContract;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 *  interface
 * to handle interaction events.
 * Use the
 * create an instance of this fragment.
 */
public class AllFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private RecyclerView recyclerView;
    private NewsSourceAdapter adapter;
    private List<String> list;
    public static final int LOADER_ID = 1;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public AllFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootview = inflater.inflate(R.layout.fragment_all, container, false);
        //Recycler View
        recyclerView = (RecyclerView) rootview.findViewById(R.id.recycler_view_all);
        ArrayList<NewsSource> newsSources = new ArrayList<>();
        adapter = new NewsSourceAdapter(getContext());

        AdView mAdView = (AdView) rootview.findViewById(R.id.admob_adview);
        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
        String android_id = Settings.Secure.getString(getContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(android_id)
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);

        FetchNewsSource fetchNewsSource = new FetchNewsSource(getContext());
        fetchNewsSource.execute();
        RecyclerView.LayoutManager mLayoutManager;
        if(!Utils.isTablet(getContext())){
            mLayoutManager= new GridLayoutManager(getActivity(), 2);
        }
        else{
            mLayoutManager = new GridLayoutManager(getActivity(), 3);
        }
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        return rootview;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cursorLoader = new CursorLoader(this.getActivity(),
                NewsContract.SourceEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.v(AllFragment.class.getSimpleName(), String.valueOf(data.getCount()));
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
    * Class for fetching news sources
    *
    *
     */
    public class FetchNewsSource extends AsyncTask<Void, Void, ArrayList<NewsSource>> {
        final String LOG = FetchNewsSource.class.getSimpleName();
        private NewsSourceAdapter adapter;
        private Context mContext;
        Utils utils = new Utils(getContext());

        public FetchNewsSource(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        protected ArrayList<NewsSource> doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String newssourcejson = null;

            try{
                final String SOURCE_BASE_URL = "https://newsapi.org/v1/sources?language=en";
                URL url = null;
                try{
                    url = new URL(SOURCE_BASE_URL);
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

                newssourcejson = buffer.toString();
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
                return utils.getSourceData(newssourcejson);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(ArrayList<NewsSource> newsSources) {

            super.onPostExecute(newsSources);
        }
    }

}
