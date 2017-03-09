package com.example.bbdaiya.capstoneproject.UI;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bbdaiya.capstoneproject.ArticleListFragment;
import com.example.bbdaiya.capstoneproject.DetailActivity;
import com.example.bbdaiya.capstoneproject.DetailActivityFragment;
import com.example.bbdaiya.capstoneproject.R;
import com.example.bbdaiya.capstoneproject.Util.Article;
import com.example.bbdaiya.capstoneproject.Util.Utils;
import com.example.bbdaiya.capstoneproject.data.NewsContract;
import com.squareup.picasso.Picasso;

/**
 * Created by bbdaiya on 28-Feb-17.
 */

public class ArticleListAdapter extends RecyclerView.Adapter<ArticleListAdapter.MyViewHolder> {
    private Context mContext;
    private Cursor cursor;
    public ArticleListAdapter(Context mContext) {
        this.mContext = mContext;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.article_list_card, parent, false);

        return new ArticleListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        cursor.moveToPosition(position);
        String title = cursor.getString(cursor.getColumnIndex(NewsContract.ArticlesEntry.COLUMN_TITLE));

        String urlToImage = cursor.getString(cursor.getColumnIndex(NewsContract.ArticlesEntry.COLUMN_URLTOLOGO));
        holder.title.setText(title);
        Picasso.with(mContext).load(urlToImage).into(holder.image);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cursor.moveToPosition(position);
                String title = cursor.getString(cursor.getColumnIndex(NewsContract.ArticlesEntry.COLUMN_TITLE));
                String author = cursor.getString(cursor.getColumnIndex(NewsContract.ArticlesEntry.COLUMN_AUTHOR));
                String description = cursor.getString(cursor.getColumnIndex(NewsContract.ArticlesEntry.COLUMN_DESCRIPTION));
                String source = cursor.getString(cursor.getColumnIndex(NewsContract.ArticlesEntry.COLUMN_SOURCE));
                String url = cursor.getString(cursor.getColumnIndex(NewsContract.ArticlesEntry.COLUMN_URL));
                String category = cursor.getString(cursor.getColumnIndex(NewsContract.ArticlesEntry.COLUMN_CATEGORY));
                String publishedAt = cursor.getString(cursor.getColumnIndex(NewsContract.ArticlesEntry.COLUMN_PUBLISHEDAT));
                String urlToImage = cursor.getString(cursor.getColumnIndex(NewsContract.ArticlesEntry.COLUMN_URLTOLOGO));

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
                if(!Utils.isTablet(mContext)) {
                    Intent intent = new Intent(mContext, DetailActivity.class);
                    intent.putExtra("article", article);
                    v.getContext().startActivity(intent);
                }
                else{
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("article", article);
                    FragmentManager fragmentManager = ((Activity)mContext).getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    DetailActivityFragment fragment = new DetailActivityFragment();
                    fragment.setArguments(bundle);
                    fragmentTransaction.add(R.id.fragment2,fragment).commit();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if(cursor==null){
            return 0;
        }
        return cursor.getCount();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public CardView cardView;
        public TextView title;
        public ImageView image;
        public MyViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView)itemView.findViewById(R.id.article_list_card_view);
            title = (TextView)itemView.findViewById(R.id.article_title);
            image = (ImageView)itemView.findViewById(R.id.article_image);

        }

    }

    public Cursor getCursor() {
        return cursor;
    }

    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
        notifyDataSetChanged();
    }
}
