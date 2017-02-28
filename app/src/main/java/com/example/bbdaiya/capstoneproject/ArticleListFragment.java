package com.example.bbdaiya.capstoneproject;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bbdaiya.capstoneproject.UI.ArticleListCardAdapter;
import com.example.bbdaiya.capstoneproject.UI.NewsSourceCardAdapter;
import com.example.bbdaiya.capstoneproject.Util.Article;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * A placeholder fragment containing a simple view.
 */
public class ArticleListFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArticleListCardAdapter adapter;

    public ArticleListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview =  inflater.inflate(R.layout.fragment_article_list, container, false);
        recyclerView = (RecyclerView) rootview.findViewById(R.id.recycler_view_article_list);
        ArrayList<Article> list = new ArrayList<>();
        for(int i = 1; i<10;i++){
            Article article = new Article("", String.valueOf(i),"","","","");
            list.add(article);
        }
        adapter = new ArticleListCardAdapter(getContext(), list);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        return rootview;
    }
}
