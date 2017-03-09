package com.example.bbdaiya.capstoneproject.widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Binder;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.bbdaiya.capstoneproject.R;
import com.example.bbdaiya.capstoneproject.UI.NewsSourceAdapter;
import com.example.bbdaiya.capstoneproject.Util.Article;
import com.example.bbdaiya.capstoneproject.Util.NewsSource;
import com.example.bbdaiya.capstoneproject.data.NewsContract;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by bbdaiya on 09-Mar-17.
 */

public class NewsViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    Context context;
    Cursor cursor;
    public NewsViewsFactory(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        if(cursor!=null)
            cursor.close();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String source = sharedPreferences.getString("widget", null);

        Uri uri = NewsContract.ArticlesEntry.buildArticleUriCategory(source, "top");
        final long identityToken = Binder.clearCallingIdentity();
        cursor = context.getContentResolver().query(uri, null, null,null,null);
        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (cursor == null) {
            return 0;
        } else {
            return cursor.getCount();
        }
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (!cursor.moveToPosition(position)) {
            return null;
        }

        cursor.moveToPosition(position);
        String title = cursor.getString(cursor.getColumnIndex(NewsContract.ArticlesEntry.COLUMN_TITLE));
        String urlToImage = cursor.getString(cursor.getColumnIndex(NewsContract.ArticlesEntry.COLUMN_URLTOLOGO));

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_list_view_item);
        views.setTextViewText(R.id.article_title, title);
        try {
            Bitmap b = Picasso.with(context).load(urlToImage).get();
            views.setImageViewBitmap(R.id.article_image, b);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String author = cursor.getString(cursor.getColumnIndex(NewsContract.ArticlesEntry.COLUMN_AUTHOR));
        String description = cursor.getString(cursor.getColumnIndex(NewsContract.ArticlesEntry.COLUMN_DESCRIPTION));
        String source = cursor.getString(cursor.getColumnIndex(NewsContract.ArticlesEntry.COLUMN_SOURCE));
        String url = cursor.getString(cursor.getColumnIndex(NewsContract.ArticlesEntry.COLUMN_URL));
        String category = cursor.getString(cursor.getColumnIndex(NewsContract.ArticlesEntry.COLUMN_CATEGORY));
        String publishedAt = cursor.getString(cursor.getColumnIndex(NewsContract.ArticlesEntry.COLUMN_PUBLISHEDAT));

        final Article article = new Article(
                source,
                author,
                title,
                description,
                url,
                urlToImage,
                publishedAt,
                category
        );
        final Intent fillInIntent = new Intent();
        fillInIntent.putExtra("article", article);
        views.setOnClickFillInIntent(R.id.list_item, fillInIntent);
        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
