package com.example.bbdaiya.capstoneproject;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bbdaiya.capstoneproject.UI.NewsSourceCardAdapter;
import com.example.bbdaiya.capstoneproject.Util.NewsSource;
import com.example.bbdaiya.capstoneproject.Util.Utils;

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
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 *  interface
 * to handle interaction events.
 * Use the {@link AllFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView recyclerView;
    private NewsSourceCardAdapter adapter;
    private List<String> list;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public AllFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AllFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AllFragment newInstance(String param1, String param2) {
        AllFragment fragment = new AllFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootview = inflater.inflate(R.layout.fragment_all, container, false);
        //Recycler View
        recyclerView = (RecyclerView) rootview.findViewById(R.id.recycler_view_all);
        ArrayList<NewsSource> newsSources = new ArrayList<>();

        FetchNewsSource fetchNewsSource = new FetchNewsSource(getContext());
        fetchNewsSource.execute();
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
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

    /*
    *
    *
    * Class for fetching news sources
    *
    *
     */
    public class FetchNewsSource extends AsyncTask<Void, Void, ArrayList<NewsSource>> {
        final String LOG = FetchNewsSource.class.getSimpleName();
        private NewsSourceCardAdapter adapter;
        private Context mContext;


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
                return Utils.getSourceData(newssourcejson);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(ArrayList<NewsSource> newsSources) {
            adapter = new NewsSourceCardAdapter(getContext(), newsSources);
            recyclerView.setAdapter(adapter);
            super.onPostExecute(newsSources);
        }
    }

}
