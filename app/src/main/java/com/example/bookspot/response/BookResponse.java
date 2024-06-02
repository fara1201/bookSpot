package com.example.bookspot.response;

import com.example.bookspot.model.Book;
import java.util.List;

public class BookResponse {

    private List<Book> books;

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
