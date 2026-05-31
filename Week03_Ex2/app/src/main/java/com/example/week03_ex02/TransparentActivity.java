package com.example.week03_ex02;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class TransparentActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transparent);
        
        findViewById(R.id.btnCloseTransparent).setOnClickListener(v -> finish());
    }
}
