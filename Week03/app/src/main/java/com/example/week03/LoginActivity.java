package com.example.week03;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login_root), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        final View shineView = findViewById(R.id.shine_view);
        final View loginButtonContainer = findViewById(R.id.btn_login_container);

        // Wait for the button to be laid out to get its width
        loginButtonContainer.post(() -> startShineAnimation(shineView, loginButtonContainer.getWidth()));
    }

    private void startShineAnimation(View shineView, int containerWidth) {
        // Animate from left (-100dp approx) to right (containerWidth + 100)
        ObjectAnimator animator = ObjectAnimator.ofFloat(
                shineView,
                "translationX",
                -200f,
                containerWidth + 200f
        );
        animator.setDuration(1500); // 1.5 seconds per cycle
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.setStartDelay(500); // Wait a bit between cycles
        animator.start();
    }
}