package com.example.bookspot.model;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import com.google.gson.annotations.SerializedName;

public class Book implements Parcelable {

    @SerializedName("_id")
    private String _id;
    @SerializedName("title")
    private String title;
    @SerializedName("author")
    private String author;
    @SerializedName("genre")
    private String genre;
    @SerializedName("summary")
    private String summary;
    @SerializedName("img_url")
    private String img_url;

    public Book(String _id, String title, String author, String genre, String summary, String img_url) {
        this._id = _id;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.summary = summary;
        this.img_url = img_url;
    }

    protected Book(Parcel in) {
        _id = in.readString();
        title = in.readString();
        author = in.readString();
        genre = in.readString();
        summary = in.readString();
        img_url = in.readString();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    // Getters and Setters
    public String getId() { return _id; }
    public void setId(String _id) { this._id = _id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }
    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }
    public String getImgUrl() { return img_url; }
    public void setImgUrl(String img_url) { this.img_url = img_url; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(_id);
        dest.writeString(title);
        dest.writeString(author);
        dest.writeString(genre);
        dest.writeString(summary);
        dest.writeString(img_url);
    }
}
