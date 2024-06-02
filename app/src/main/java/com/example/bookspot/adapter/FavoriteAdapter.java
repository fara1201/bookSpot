package com.example.bookspot.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bookspot.DetailActivity;
import com.example.bookspot.R;
import com.example.bookspot.model.Book;

import java.util.List;


public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.BookViewHolder> {
    private final FragmentManager fragmentManager;
    private List<Book> books;
    private Context context;
    private final int userId;

    public FavoriteAdapter(FragmentManager fragmentManager, List<Book> books, int userId) {
        this.fragmentManager = fragmentManager;
        this.books = books;
        this.userId = userId;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_favorite, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = books.get(position);
        holder.titleTextView.setText(book.getTitle());
        holder.authorTextView.setText(book.getAuthor());
        holder.genreTextView.setText(book.getGenre());
        Glide.with(holder.itemView.getContext())
                .load(book.getImgUrl())
                .into(holder.bookImageView);

        holder.itemView.setOnClickListener(v -> {
            // spy ketika menekan list salah satu buku di fav akan menampilkan detail activity
            Intent intent = new Intent(holder.itemView.getContext(), DetailActivity.class);
            intent.putExtra("book", book);
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    class BookViewHolder extends RecyclerView.ViewHolder {
        ImageView bookImageView;
        TextView titleTextView, authorTextView, genreTextView;

        BookViewHolder(View itemView) {
            super(itemView);
            bookImageView = itemView.findViewById(R.id.gambarFav);
            titleTextView = itemView.findViewById(R.id.titleFav);
            authorTextView = itemView.findViewById(R.id.authorFav);
            genreTextView = itemView.findViewById(R.id.genreFav);
        }
    }
}
