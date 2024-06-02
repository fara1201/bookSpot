package com.example.bookspot.api;

import com.example.bookspot.response.BookResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface ApiService {
    String RAPID_API_KEY = "7a05f24693msh5400a7f3b2540c6p172d53jsn92d4a44c32de";
    String RAPID_API_HOST = "book-information-library.p.rapidapi.com";

    @Headers({
            "X-RapidAPI-Key: " + RAPID_API_KEY,
            "X-RapidAPI-Host: " + RAPID_API_HOST
    })
    @GET("api/books/getall-books")
    Call<BookResponse> getAllBooks();
}
