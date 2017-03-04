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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        title = (TextView)findViewById(R.id.article_title);
        author = (TextView)findViewById(R.id.article_author);
        readMore = (TextView)findViewById(R.id.read_more);
        publishedAt = (TextView)findViewById(R.id.article_publishedAt);
        description = (TextView)findViewById(R.id.article_description);
        Bundle bundle = getIntent().getExtras();
        final Article article = bundle.getParcelable("article");
        title.setText(article.getTitle());
        author.setText("Author:"+article.getAuthor());

        publishedAt.setText("PublishedAt:"+article.getPublishedAt());
        description.setText(article.getDescription());
        String urlToImage = article.getUrlToImage();
        article_photo = (ImageView)findViewById(R.id.article_detail_photo);
        Picasso.with(getApplicationContext()).load(urlToImage).into(article_photo);
        readMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), WebViewActivity.class);
                intent.putExtra("url", article.getUrl());
                startActivity(intent);
            }
        });
    }
}
