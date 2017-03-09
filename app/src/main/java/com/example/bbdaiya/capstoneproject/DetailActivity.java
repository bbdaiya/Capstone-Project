package com.example.bbdaiya.capstoneproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bbdaiya.capstoneproject.Util.Article;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {
    public TextView title, author, readMore, publishedAt, description;
    public ImageView article_photo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

    }
}
