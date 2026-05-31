package com.example.a23110006_ex3;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "LifeLine";
    private TextView tvLogs;
    private StringBuilder logBuilder = new StringBuilder();

    private void updateStatus(String method) {
        String timestamp = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        String message = timestamp + ": " + method;
        
        // 1. Log ra Logcat
        Log.d(TAG, message);
        
        // 2. Hiện Toast
        Toast.makeText(this, method, Toast.LENGTH_SHORT).show();
        
        // 3. Cập nhật lên màn hình
        logBuilder.insert(0, message + "\n");
        if (tvLogs != null) {
            tvLogs.setText(logBuilder.toString());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        
        tvLogs = findViewById(R.id.tv_lifecycle_logs);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String msg = "onCreate";
        if (savedInstanceState != null) {
            msg += " (Restored)";
        }
        updateStatus(msg);
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateStatus("onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateStatus("onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        updateStatus("onPause");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        updateStatus("onSaveInstanceState");
        // Lưu lại log hiện tại để khi xoay màn hình không bị mất
        outState.putString("logs", logBuilder.toString());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        logBuilder.append(savedInstanceState.getString("logs", ""));
        updateStatus("onRestoreInstanceState");
    }

    @Override
    protected void onStop() {
        super.onStop();
        updateStatus("onStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        updateStatus("onRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        updateStatus("onDestroy");
    }
}
