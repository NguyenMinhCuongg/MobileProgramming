package com.example.week03_ex02;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class DialogActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        
        findViewById(R.id.btnCloseDialog).setOnClickListener(v -> finish());
    }
}
