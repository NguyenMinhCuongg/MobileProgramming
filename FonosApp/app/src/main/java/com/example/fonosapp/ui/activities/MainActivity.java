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
import com.example.fonosapp.utils.AppNavigator;
import com.example.fonosapp.utils.NotificationHelper;

public class MainActivity extends AppCompatActivity {

    private SupabaseAuthManager authManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Khởi tạo Notification Channel
        NotificationHelper.createNotificationChannel(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        authManager = new SupabaseAuthManager((success, error) -> {
            if (success) {
                Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show();
                AppNavigator.navigateTo(this, CategoryActivity.class, true);
            } else {
                Toast.makeText(this, "Login Failed: " + error, Toast.LENGTH_LONG).show();
            }
            return kotlin.Unit.INSTANCE;
        });

        EditText etEmail = findViewById(R.id.etEmail);
        EditText etPassword = findViewById(R.id.etPassword);

        TextView tvSignUp = findViewById(R.id.tvSignUp);
        tvSignUp.setOnClickListener(v -> {
            AppNavigator.navigateTo(this, SignUpActivity.class, false);
        });

        Button btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();
            authManager.signIn(email, password);
        });
    }
}
