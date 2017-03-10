package com.example.bbdaiya.capstoneproject;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
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
import com.example.bbdaiya.capstoneproject.data.NewsContract;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.content.Context.LOCATION_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 *
 * to handle interaction events.
 * Use the {@link LocalNewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LocalNewsFragment extends Fragment implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LoaderManager.LoaderCallbacks<Cursor>{

    final String LOG = LocalNewsFragment.class.getSimpleName();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView recyclerView;
    private NewsSourceAdapter adapter;
    public static final int LOADER_ID = 1;

    private List<String> list;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String countryName;
    private GoogleApiClient mGoogleApiClient;
    public Location mLastLocation;


    public LocalNewsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LocalNewsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LocalNewsFragment newInstance(String param1, String param2) {
        LocalNewsFragment fragment = new LocalNewsFragment();
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
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            mGoogleApiClient.connect();
        }

        LocationManager service = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        boolean enabled = service
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

// check if enabled and if not send user to the GSP settings
// Better solution would be to display a dialog and suggesting to
// go to the settings
        if (!enabled) {
            Log.v(LOG, "location");
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(intent, 1);
        }
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    200);


        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.v(LOG, "result");
        getCountry();
    }

    @Override
    public void onResume() {
        getCountry();
        super.onResume();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_local_news, container, false);
        //Recycler View
        recyclerView = (RecyclerView) rootview.findViewById(R.id.recycler_view_country);
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
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        return rootview;
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        getCountry();
    }
    public String getCountry(){
        String countryCode = "";
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            Log.v(LOG, "null");
            return null;
        }
        try {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            if (mLastLocation != null) {

                Geocoder gcd = new Geocoder(getContext(), Locale.getDefault());
                try {
                    List<Address> addresses = gcd.getFromLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude(), 1);

                    if (addresses.size() > 0) {
                        countryName = addresses.get(0).getCountryName();
                        Log.v(LOG, "country"+countryName);

                        if(countryName.equals("Australia")){
                            countryCode = "au";
                        }
                        else if(countryName.equals("Germany")){
                            countryCode = "de";
                        }
                        else if(countryName.equals("United Kingdom")){
                            countryCode = "gb";
                        }
                        else if(countryName.equals("India")){
                            countryCode = "in";
                        }
                        else if(countryName.equals("Italy")){
                            countryCode = "it";
                        }
                        else if(countryName.equals("United States")){
                            countryCode = "us";
                        }
                        Log.v(LOG, "code"+countryCode);
                        getLoaderManager().initLoader(LOADER_ID, null, this);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return countryCode;
    }
    @Override
    public void onConnectionSuspended(int i) {
        Log.v(LOG, "Connection suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.v(LOG, "connection failed");
    }

    @Override
    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();

    }



    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = NewsContract.SourceEntry.buildSourceUriLocation(getCountry());
        Log.v(LOG, uri.toString());
        CursorLoader cursorLoader = new CursorLoader(this.getActivity(),
                uri,
                null,
                null,
                null,
                null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data!=null) {
            Log.v(LOG, "data not null");
            adapter.setCursor(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.setCursor(null);
    }

//    /*
//    *
//    *
//    * Class for fetching news sources
//    *
//    *
//     */
//    public class FetchCountryNewsSource extends AsyncTask<String, Void, ArrayList<NewsSource>> {
//        final String LOG = LocalNewsFragment.FetchCountryNewsSource.class.getSimpleName();
//        private NewsSourceAdapter adapter;
//        private Context mContext;
//
//
//        public FetchCountryNewsSource(Context mContext) {
//            this.mContext = mContext;
//        }
//
//        @Override
//        protected ArrayList<NewsSource> doInBackground(String... params) {
//
//            HttpURLConnection urlConnection = null;
//            BufferedReader reader = null;
//            String newssourcejson = null;
//
//            try{
//                final String SOURCE_BASE_URL = "https://newsapi.org/v1/sources?language=en";
//                final String COUNTRY = "country";
//                URL url = null;
//                Uri builtUri = Uri.parse(SOURCE_BASE_URL).buildUpon().appendQueryParameter(COUNTRY, params[0]).build();
//                try{
//                    url = new URL(builtUri.toString());
//                    Log.v(LOG, url.toString());
//                }
//                catch(MalformedURLException e){
//                    e.printStackTrace();
//                }
//                urlConnection = (HttpURLConnection) url.openConnection();
//                urlConnection.setRequestMethod("GET");
//                urlConnection.connect();
//                InputStream is = urlConnection.getInputStream();
//                StringBuffer buffer = new StringBuffer();
//                if(is==null){
//                    return null;
//                }
//
//                reader = new BufferedReader(new InputStreamReader(is));
//                String line;
//                while((line=reader.readLine())!=null){
//                    buffer.append(line+"\n");           //for making debugging easy we add newline
//                }
//                if(buffer.length()==0){
//                    //Stream is empty
//                    return null;
//                }
//
//                newssourcejson = buffer.toString();
//            }
//            catch (IOException e){
//                e.printStackTrace();
//            }finally {
//                if(urlConnection!=null){
//                    urlConnection.disconnect();
//                }
//                if(reader!=null){
//                    try {
//                        reader.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//            try {
//                return Utils.getSourceData(newssourcejson);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//
//        @Override
//        protected void onPostExecute(ArrayList<NewsSource> newsSources) {
//            adapter = new NewsSourceAdapter(getContext(), newsSources);
//            recyclerView.setAdapter(adapter);
//            super.onPostExecute(newsSources);
//        }
//    }
}
