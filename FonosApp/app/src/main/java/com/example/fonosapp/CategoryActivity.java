package com.example.fonosapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity {

    private BookManager bookManager;
    private RecyclerView rvCategories;
    private CategoryAdapter adapter;
    private ProgressBar progressBar;
    private ArrayList<Category> categoryList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_category);

        rvCategories = findViewById(R.id.rvCategories);
        progressBar = findViewById(R.id.progressBar);

        bookManager = new BookManager();
        
        setupRecyclerView();
        loadCategories();
    }

    private void setupRecyclerView() {
        adapter = new CategoryAdapter(categoryList, category -> {
            progressBar.setVisibility(View.VISIBLE);
            bookManager.fetchBooksByCategory(category.getId(), (books, error) -> {
                progressBar.setVisibility(View.GONE);
                if (error != null) {
                    Toast.makeText(this, "Lỗi khi tải sách: " + error, Toast.LENGTH_SHORT).show();
                } else if (books != null && !books.isEmpty()) {
                    // Lấy cuốn sách đầu tiên của thể loại này
                    Book firstBook = books.get(0);
                    Intent intent = new Intent(CategoryActivity.this, BookDetailActivity.class);
                    intent.putExtra("BOOK_ID", firstBook.getId());
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Không có sách nào trong thể loại này", Toast.LENGTH_SHORT).show();
                }
                return kotlin.Unit.INSTANCE;
            });
        });
        rvCategories.setLayoutManager(new GridLayoutManager(this, 2));
        rvCategories.setAdapter(adapter);
    }

    private void loadCategories() {
        progressBar.setVisibility(View.VISIBLE);
        bookManager.fetchCategories((categories, error) -> {
            progressBar.setVisibility(View.GONE);
            if (error != null) {
                Toast.makeText(this, "Lỗi: " + error, Toast.LENGTH_LONG).show();
            } else if (categories != null) {
                categoryList.clear();
                categoryList.addAll(categories);
                adapter.notifyDataSetChanged();
            }
            return kotlin.Unit.INSTANCE;
        });
    }
}
