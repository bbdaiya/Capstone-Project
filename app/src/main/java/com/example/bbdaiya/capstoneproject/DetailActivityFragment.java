package com.example.bbdaiya.capstoneproject;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bbdaiya.capstoneproject.Util.Article;
import com.example.bbdaiya.capstoneproject.Util.Utils;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailActivityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailActivityFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public TextView title, author, readMore, publishedAt, description;
    public ImageView article_photo;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public DetailActivityFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailActivityFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailActivityFragment newInstance(String param1, String param2) {
        DetailActivityFragment fragment = new DetailActivityFragment();
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
        View rootview = inflater.inflate(R.layout.fragment_detail_activity, container, false);
        Toolbar toolbar = (Toolbar) rootview.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar();
        title = (TextView)rootview.findViewById(R.id.article_title);
        author = (TextView)rootview.findViewById(R.id.article_author);
        readMore = (TextView)rootview.findViewById(R.id.read_more);
        publishedAt = (TextView)rootview.findViewById(R.id.article_publishedAt);
        description = (TextView)rootview.findViewById(R.id.article_description);

        Bundle bundle;
        if(!Utils.isTablet(getActivity())) {
            bundle = getActivity().getIntent().getExtras();
        }
        else{
            bundle = getArguments();

        }
        final Article article = bundle.getParcelable("article");
        title.setText(article.getTitle());
        author.setText("Author:"+article.getAuthor());

        publishedAt.setText("PublishedAt:"+article.getPublishedAt());
        description.setText(article.getDescription());
        String urlToImage = article.getUrlToImage();
        article_photo = (ImageView)rootview.findViewById(R.id.article_detail_photo);
        Picasso.with(getActivity()).load(urlToImage).into(article_photo);
        readMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("url", article.getUrl());
                startActivity(intent);
            }
        });
        return rootview;
    }

}
