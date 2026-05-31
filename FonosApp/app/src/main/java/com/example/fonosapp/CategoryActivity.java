package com.example.fonosapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    private BookManager bookManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_category);

        bookManager = new BookManager();
        testSupabaseConnection();

        // Demo: Click first category to go to Book Detail
        findViewById(R.id.cvCategory1).setOnClickListener(v -> {
            Intent intent = new Intent(CategoryActivity.this, BookDetailActivity.class);
            startActivity(intent);
        });
    }

    private void testSupabaseConnection() {
        bookManager.fetchCategories((categories, error) -> {
            if (error != null) {
                Log.e("SupabaseTest", "Error fetching categories: " + error);
                Toast.makeText(this, "Supabase Error: " + error, Toast.LENGTH_LONG).show();
            } else if (categories != null) {
                Log.d("SupabaseTest", "Fetched " + categories.size() + " categories");
                if (categories.isEmpty()) {
                    Toast.makeText(this, "Connected to Supabase! (Table 'categories' is empty)", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Connected! Found " + categories.size() + " categories.", Toast.LENGTH_LONG).show();
                    for (Category cat : categories) {
                        Log.d("SupabaseTest", "Category: " + cat.getNameVi());
                    }
                }
            }
            return kotlin.Unit.INSTANCE;
        });
    }
}
