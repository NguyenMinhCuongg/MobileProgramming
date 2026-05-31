package com.example.week03_ex02;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView textViewLifecycle;
    private final StringBuilder lifecycleEvents = new StringBuilder();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        
        // Handling window insets for Edge-to-Edge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        textViewLifecycle = findViewById(R.id.textViewLifecycle);
        
        findViewById(R.id.btnSecond).setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(this, SecondActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.btnDialog).setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(this, DialogActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.btnTransparent).setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(this, TransparentActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.btnFinish).setOnClickListener(v -> finish());

        findViewById(R.id.btnClear).setOnClickListener(v -> {
            lifecycleEvents.setLength(0);
            textViewLifecycle.setText("");
        });

        updateLifecycle("onCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateLifecycle("onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateLifecycle("onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        updateLifecycle("onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        updateLifecycle("onStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        updateLifecycle("onRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        updateLifecycle("onDestroy");
    }

    private void updateLifecycle(String eventName) {
        String currentTime = dateFormat.format(new Date());
        String logEntry = String.format("[%s] %s\n", currentTime, eventName);
        
        Log.d("Lifecycle", eventName + " called at " + currentTime);
        Toast.makeText(this, eventName, Toast.LENGTH_SHORT).show();
        
        lifecycleEvents.insert(0, logEntry); // Insert at the top so newest is first
        if (textViewLifecycle != null) {
            textViewLifecycle.setText(lifecycleEvents.toString());
        }
    }
}
