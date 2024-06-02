package com.example.bookspot;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.bookspot.adapter.BookAdapter;
import com.example.bookspot.api.ApiConfig;
import com.example.bookspot.api.ApiService;
import com.example.bookspot.model.Book;
import com.example.bookspot.response.BookResponse;
import com.example.bookspot.sqlLite.DbConfig;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    ImageView iv_back, iv_love, iv_book;
    DbConfig dbConfig;
    private boolean isFavorite;
    private BookAdapter bookAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        dbConfig = new DbConfig(this);

        iv_back = findViewById(R.id.btn_back);
        iv_love = findViewById(R.id.btn_love);
        iv_book = findViewById(R.id.image);

        iv_back.setOnClickListener(v -> finish());
        iv_love.setOnClickListener(v -> toggleFavorite());

        Book bookModel = getIntent().getParcelableExtra("book");
        if (bookModel != null) {
            displayBookDetails(bookModel);
            int loggedInUserId = getLoggedInUserId();
            isFavorite = isBookFavorite(loggedInUserId, bookModel.getId());
            updateFavoriteIcon();
        }

        loadBooks();
    }

    private void displayBookDetails(Book bookModel) {
        TextView tv_title = findViewById(R.id.titleDetail);
        TextView tv_author = findViewById(R.id.authorDetail);
        TextView tv_genre = findViewById(R.id.genreDetail);
        TextView tv_summary = findViewById(R.id.summaryDetail);

        tv_title.setText(bookModel.getTitle());
        tv_author.setText(bookModel.getAuthor());
        tv_genre.setText(bookModel.getGenre());
        tv_summary.setText(bookModel.getSummary());
        Picasso.get().load(bookModel.getImgUrl()).into(iv_book);
    }

    private void toggleFavorite() {
        int loggedInUserId = getLoggedInUserId();
        Book bookModel = getIntent().getParcelableExtra("book");

        if (bookModel != null) {
            if (isFavorite) {
                dbConfig.deleteFavorite(loggedInUserId, bookModel.getId());
                iv_love.setImageResource(R.drawable.ic_favorite);
                Toast.makeText(this, "Removed from favorites", Toast.LENGTH_SHORT).show();
            } else {
                dbConfig.insertFavorite(loggedInUserId, bookModel.getId());
                iv_love.setImageResource(R.drawable.ic_favorite2);
                Toast.makeText(this, "Added to favorites", Toast.LENGTH_SHORT).show();
            }
            isFavorite = !isFavorite;
        }
    }

    private void loadBooks() {
        ApiService apiService = ApiConfig.getClient();
        Call<BookResponse> call = apiService.getAllBooks();
        call.enqueue(new Callback<BookResponse>() {
            @Override
            public void onResponse(Call<BookResponse> call, Response<BookResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Book> bookList = response.body().getBooks();
                    if (bookAdapter != null) {
                        bookAdapter.setBooks(bookList);
                    }
                } else {
                    Toast.makeText(DetailActivity.this, "Failed to get data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BookResponse> call, Throwable t) {
                Toast.makeText(DetailActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateFavoriteIcon() {
        if (isFavorite) {
            iv_love.setImageResource(R.drawable.ic_favorite2);
        } else {
            iv_love.setImageResource(R.drawable.ic_favorite);
        }
    }

    private boolean isBookFavorite(int userId, String bookId) {
        Cursor cursor = dbConfig.getFavoriteBooksByUserId(userId);
        boolean isFavorite = false;
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String isbn = cursor.getString(cursor.getColumnIndexOrThrow(DbConfig.COLUMN_BOOK_ID));
                if (isbn.equals(bookId)) {
                    isFavorite = true;
                    break;
                }
            }
            cursor.close();
        }
        return isFavorite;
    }

    private int getLoggedInUserId() {
        SharedPreferences sharedPreferences = getSharedPreferences("login_prefs", MODE_PRIVATE);
        return sharedPreferences.getInt("user_id", -1);
    }
}