package com.example.bookspot.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bookspot.R;
import com.example.bookspot.adapter.FavoriteAdapter;
import com.example.bookspot.api.ApiConfig;
import com.example.bookspot.api.ApiService;
import com.example.bookspot.model.Book;
import com.example.bookspot.response.BookResponse;
import com.example.bookspot.sqlLite.DbConfig;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FavoriteFragment extends Fragment {

    private FavoriteAdapter favoriteAdapter;
    private RecyclerView recyclerView;
    private DbConfig dbConfig;
    private ApiService service;
    private SharedPreferences preferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView2);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        service = ApiConfig.getClient();

        dbConfig = new DbConfig(requireActivity());
        preferences = requireActivity().getSharedPreferences("login_prefs", Context.MODE_PRIVATE);

        // Refresh data when fragment is created
        refreshFavoriteResto();
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshFavoriteResto();
    }

    private void refreshFavoriteResto() {
        int userId = preferences.getInt("user_id", 0);

        Cursor cursor = dbConfig.getFavoriteBooksByUserId(userId);
        ArrayList<String> favoriteRestoIds = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                String restoId = cursor.getString(cursor.getColumnIndexOrThrow(DbConfig.COLUMN_BOOK_ID));
                favoriteRestoIds.add(restoId);
            } while (cursor.moveToNext());
        }

        recyclerView.setVisibility(View.GONE);
        Call<BookResponse> call = service.getAllBooks();
        call.enqueue(new Callback<BookResponse>() {
            @Override
            public void onResponse(@NonNull Call<BookResponse> call, @NonNull Response<BookResponse> response) {
                if (response.isSuccessful() && isAdded()) {
                    BookResponse bookResponse = response.body();
                    List<Book> bookList = bookResponse.getBooks();
                    List<Book> bookFavorite = new ArrayList<>();
                    if (bookList != null) {
                        for (Book book : bookList) {
                            if (favoriteRestoIds.contains(book.getId())) {
                                bookFavorite.add(book);
                            }
                        }
                    }
                    favoriteAdapter = new FavoriteAdapter(getParentFragmentManager(), bookFavorite, userId);
                    recyclerView.setAdapter(favoriteAdapter);

                    if (bookFavorite.isEmpty()) {
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<BookResponse> call, @NonNull Throwable t) {
                Log.e("FavoriteFragment", "Error fetching favorite restos: " + t.getMessage());
            }
        });
    }
}