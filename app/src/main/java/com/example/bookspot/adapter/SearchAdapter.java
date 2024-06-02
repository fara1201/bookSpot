package com.example.bookspot.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.bookspot.DetailActivity;
import com.example.bookspot.R;
import com.example.bookspot.model.Book;

import java.util.List;


public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.BookViewHolder> {
    private List<Book> bookList;
    Context context;

    public SearchAdapter(List<Book> bookList, Context context) {
        this.bookList = bookList;
        this.context = context;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_item, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = bookList.get(position);
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
        return bookList.size();
    }

    public void updateData(List<Book> searchResult) {
        bookList.clear(); // Hapus data buku yang sebelumnya ditampilkan
        bookList.addAll(searchResult); // Tambahkan hasil pencarian baru
        notifyDataSetChanged();
    }

    class BookViewHolder extends RecyclerView.ViewHolder {
        ImageView bookImageView;
        TextView titleTextView, authorTextView, genreTextView;

        BookViewHolder(View itemView) {
            super(itemView);
            bookImageView = itemView.findViewById(R.id.bookImageView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            authorTextView = itemView.findViewById(R.id.authorTextView);
            genreTextView = itemView.findViewById(R.id.genreTextView);
        }
    }
}


