package com.example.bbdaiya.capstoneproject.UI;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bbdaiya.capstoneproject.ArticleList;
import com.example.bbdaiya.capstoneproject.R;
import com.example.bbdaiya.capstoneproject.Util.NewsSource;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by bbdaiya on 21-Feb-17.
 */

public class NewsSourceCardAdapter extends RecyclerView.Adapter<NewsSourceCardAdapter.MyViewHolder> {
    private Context mContext;
    private List<NewsSource> list;
    public NewsSourceCardAdapter(Context mContext, List<NewsSource> list) {
        this.mContext = mContext;
        this.list = list;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_source_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.news_source.setText(list.get(position).getName());
        Picasso.with(mContext).load(list.get(position).getLogo_url()).into(holder.logo);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(NewsSourceCardAdapter.class.getSimpleName(), "clicked");
                Intent intent = new Intent(v.getContext(), ArticleList.class);
                intent.putExtra("ID", list.get(position).getId());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView news_source;
        public ImageView logo;
        public CardView cardView;
         public LinearLayout layout;
        public MyViewHolder(View itemView) {
            super(itemView);
            layout = (LinearLayout) itemView.findViewById(R.id.lin_layout);
            news_source = (TextView)itemView.findViewById(R.id.news_source);
            logo = (ImageView)itemView.findViewById(R.id.news_source_logo);
            cardView = (CardView)itemView.findViewById(R.id.card_view);
        }

    }

}
