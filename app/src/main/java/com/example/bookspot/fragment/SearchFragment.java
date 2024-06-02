package com.example.bookspot.fragment;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookspot.R;
import com.example.bookspot.adapter.SearchAdapter;
import com.example.bookspot.model.Book;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {
    private List<Book> bookList;
    private SearchAdapter searchAdapter;
    private RecyclerView recyclerView;
    private EditText et_search;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.tc_result);
        et_search = view.findViewById(R.id.btn_search);

        // Dummy data
        bookList = new ArrayList<>();
        bookList.add(new Book("661b6e7f8985d0d869825090", "To Kill a Mockingbird", "Harper Lee", "Fiction", "A novel set in the American South during the 1930s that deals with serious issues such as racial injustice and moral growth through the eyes of a young girl named Scout Finch.", "https://libgen.rs/covers/506000/af4e3928d016d9b7919cb7da5009aa70-d.jpg"));
        bookList.add(new Book("661b6e9a39cb851a9d28f2cc", "1984", "George Orwell", "Science Fiction", "A dystopian novel set in a totalitarian society where individualism is forbidden and government surveillance is omnipresent.", "https://libgen.rs/covers/324000/5a704baa98ae56c21e78fbe88e397b09-d.jpg"));
        bookList.add(new Book("661b6eb100c534436716b3e5", "The Great Gatsby", "F. Scott Fitzgerald", "Fiction", "Set in the 1920s, this novel explores themes of wealth, love, and the American Dream through the eyes of the mysterious Jay Gatsby.", "https://libgen.rs/covers/736000/39AB4AB67A3701800EE7DF354454A5CF-g.jpg"));
        bookList.add(new Book("661b6ebdf3e280ec80f3034a", "Harry Potter and the Philosopher's Stone", "J.K. Rowling", "Fantasy", "The first book in the Harry Potter series, which follows the young wizard Harry Potter as he discovers his magical heritage and attends Hogwarts School of Witchcraft and Wizardry.", "https://libgen.rs/covers/2392000/2970b42cee393b615a3a93d6ed6608fa-d.jpg"));
        bookList.add(new Book("661b6ef74ec90eb2c555504e", "The Catcher in the Rye", "J.D. Salinger", "Fiction", "Narrated by the disillusioned teenager Holden Caulfield, this novel explores themes of alienation, identity, and adolescence.", "https://libgen.rs/covers/220000/b4d30020e2f1311ca89166d5e1b578ae-d.jpg"));
        bookList.add(new Book("661b6f02d33f8eeac6ce55d9", "The Hobbit", "J.R.R. Tolkien", "Fantasy", "A fantasy novel that follows the journey of Bilbo Baggins, a hobbit who is swept into an epic quest to reclaim the treasure guarded by the dragon Smaug.", "https://libgen.rs/covers/643000/89ee622230907c6ff3175bd3f30b80ec-d.jpg"));
        bookList.add(new Book("661b6f0ddab73f705f270cc1", "To the Lighthouse", "Virginia Woolf", "Fiction", "A modernist novel that explores the inner thoughts and experiences of the Ramsay family as they spend a summer at their vacation home in the Hebrides.", "https://libgen.rs/covers/436000/5a78675c4fed61838946eed50b8eea7a-d.jpg"));
        bookList.add(new Book("661b6f1db35e6a9812cb2dc2", "Moby-Dick", "Herman Melville", "Adventure", "An epic tale of obsession and revenge, following Captain Ahab's relentless pursuit of the elusive white whale, Moby Dick.", "https://libgen.rs/covers/268000/ad68152110bfd420c962911da22c2a14-d.jpg"));
        bookList.add(new Book("661b93f6c8d2c8cf9ec4c2ef", "The Hunger Games", "Suzanne Collins", "Dystopian Fiction", "In a dystopian future, the totalitarian nation of Panem forces each of its twelve districts to send a teenage boy and girl to compete in the Hunger Games. Katniss Everdeen volunteers to take her younger sister's place for the latest match.", "https://libgen.rs/covers/536000/8AEE34DEE4A493443EE178B8F3C2D93A-g.jpg"));
        bookList.add(new Book("661b6db3f9c6802b06957ec2", "Pride and Prejudice", "Jane Austen", "Romance", "A classic novel about the complexities of love, marriage, and societal expectations in 19th-century England.", "https://libgen.rs/covers/268000/2ec14543ea6697679fd47c49fae965e4-d.jpg"));

        searchAdapter = new SearchAdapter(bookList, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(searchAdapter);

        et_search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    performSearch();
                    return true;
                }
                return false;
            }
        });
    }

    private void performSearch() {
        String searchQuery = et_search.getText().toString().trim().toLowerCase();
        if (!searchQuery.isEmpty()) {
            List<Book> searchResult = new ArrayList<>();
            for (Book book : bookList) {
                if (book.getTitle().toLowerCase().contains(searchQuery)) {
                    searchResult.add(book);
                }
            }
            if (!searchResult.isEmpty()) {
                searchAdapter.updateData(searchResult);
            } else {
                Toast.makeText(getContext(), "No books found", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
