package com.example.bbdaiya.capstoneproject.UI;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
import com.example.bbdaiya.capstoneproject.data.NewsContract;
import com.squareup.picasso.Picasso;

/**
 * Created by bbdaiya on 21-Feb-17.
 */

public class NewsSourceAdapter extends RecyclerView.Adapter<NewsSourceAdapter.MyViewHolder> {
    private Context mContext;
    public NewsSourceAdapter(Context mContext) {
        this.mContext = mContext;
    }
    Cursor cursor;

    public Cursor getCursor() {
        return cursor;
    }

    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_source_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        cursor.moveToPosition(position);
        String name = cursor.getString(cursor.getColumnIndex(NewsContract.SourceEntry.COLUMN_NAME));
        String url = cursor.getString(cursor.getColumnIndex(NewsContract.SourceEntry.COLUMN_LOGO_URL));
        holder.news_source.setText(name);
        Picasso.with(mContext).load(url).into(holder.logo);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(NewsSourceAdapter.class.getSimpleName(), "clicked");
                cursor.moveToPosition(position);
                Intent intent = new Intent(v.getContext(), ArticleList.class);
                intent.putExtra("ID", cursor.getString(cursor.getColumnIndex(NewsContract.SourceEntry.COLUMN_SOURCE_ID)));
                v.getContext().startActivity(intent);
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
