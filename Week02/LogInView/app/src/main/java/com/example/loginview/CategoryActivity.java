package com.example.loginview;

import android.content.Intent;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class CategoryActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_category);

        // Demo: Click first category to go to Book Detail
        findViewById(R.id.cvCategory1).setOnClickListener(v -> {
            Intent intent = new Intent(CategoryActivity.this, BookDetailActivity.class);
            startActivity(intent);
        });
    }
}
