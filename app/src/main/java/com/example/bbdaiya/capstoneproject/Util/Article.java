package com.example.bbdaiya.capstoneproject.Util;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by bbdaiya on 28-Feb-17.
 */

public class Article implements Parcelable{
    private String source;
    private String author;
    private String title;
    private String description;
    private String url;
    private String urlToImage;
    private String publishedAt;
    private String category;

    public Article(String source, String author, String title, String description, String url, String urlToImage, String publishedAt,String category) {
        this.source = source;
        this.author = author;
        this.title = title;
        this.description = description;
        this.url = url;
        this.urlToImage = urlToImage;
        this.publishedAt = publishedAt;
        this.category = category;
    }
    public Article(Parcel in){
        String[] data = new String[8];

        in.readStringArray(data);
        this.source = data[0];
        this.author = data[1];
        this.title = data[2];
        this.description = data[3];
        this.url = data[4];
        this.urlToImage = data[5];
        this.publishedAt = data[6];
        this.category = data[7];
    }
    public String getAuthor() {
        return author;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String discription) {
        this.description = discription;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{
                this.source,this.author,this.title,this.description, this.url
                ,this.urlToImage, this.publishedAt
        });
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Article createFromParcel(Parcel in) {
            return new Article(in);
        }

        public Article[] newArray(int size) {
            return new Article[size];
        }
    };
}
