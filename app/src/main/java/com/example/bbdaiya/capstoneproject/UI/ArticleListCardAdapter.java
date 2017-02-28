package com.example.bbdaiya.capstoneproject.UI;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bbdaiya.capstoneproject.ArticleList;
import com.example.bbdaiya.capstoneproject.R;
import com.example.bbdaiya.capstoneproject.Util.Article;
import com.example.bbdaiya.capstoneproject.Util.NewsSource;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by bbdaiya on 28-Feb-17.
 */

public class ArticleListCardAdapter extends RecyclerView.Adapter<ArticleListCardAdapter.MyViewHolder> {
    private Context mContext;
    private List<Article> list;
    public ArticleListCardAdapter(Context mContext, List<Article> list) {
        this.mContext = mContext;
        this.list = list;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.article_list_card, parent, false);

        return new ArticleListCardAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.title.setText(list.get(position).getTitle());
        Picasso.with(mContext).load(list.get(position).getUrlToImage()).into(holder.image);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView title;
        public ImageView image;
        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView)itemView.findViewById(R.id.news_source);
            image = (ImageView)itemView.findViewById(R.id.news_source_logo);

        }

    }
}
