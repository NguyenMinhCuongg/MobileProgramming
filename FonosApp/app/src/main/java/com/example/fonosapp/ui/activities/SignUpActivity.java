package com.example.fonosapp.ui.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.fonosapp.R;
import com.example.fonosapp.data.remote.SupabaseAuthManager;
import com.example.fonosapp.utils.NotificationHelper;

public class SignUpActivity extends AppCompatActivity {

    private SupabaseAuthManager authManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        authManager = new SupabaseAuthManager((success, error) -> {
            if (success) {
                // Hiển thị Notification khi đăng ký thành công
                NotificationHelper.showNotification(this, "Đăng ký thành công", "Chào mừng bạn đến với Fonos! Hãy đăng nhập để bắt đầu.");
                finish();
            } else {
                Toast.makeText(this, "Sign Up Failed: " + error, Toast.LENGTH_LONG).show();
            }
            return kotlin.Unit.INSTANCE;
        });

        EditText etEmail = findViewById(R.id.etEmail);
        EditText etPassword = findViewById(R.id.etPassword);
        Button btnSignUp = findViewById(R.id.btnSignUp);

        btnSignUp.setOnClickListener(v -> {
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();
            authManager.signUp(email, password);
        });

        TextView tvLoginLink = findViewById(R.id.tvLoginLink);
        tvLoginLink.setOnClickListener(v -> {
            finish(); // Quay lại màn hình Login
        });
    }
}
