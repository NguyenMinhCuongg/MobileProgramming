package com.example.fonosapp.ui.activities;

import android.content.ComponentName;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.Player;
import androidx.media3.session.MediaController;
import androidx.media3.session.SessionToken;

import com.example.fonosapp.R;
import com.example.fonosapp.services.AudioPlaybackService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;

public class PlayerActivity extends AppCompatActivity {
    private ListenableFuture<MediaController> controllerFuture;
    private FloatingActionButton fabPlayPause;
    private ImageView ivBookCover;
    private TextView tvTitle, tvAuthor;
    private RotateAnimation rotateAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_player);

        initViews();
        setupAnimations();
    }

    private void initViews() {
        fabPlayPause = findViewById(R.id.fabPlayPause);
        
        androidx.cardview.widget.CardView cvCover = findViewById(R.id.cvPlayerCover);
        if (cvCover.getChildCount() > 0 && cvCover.getChildAt(0) instanceof ImageView) {
            ivBookCover = (ImageView) cvCover.getChildAt(0);
        }
        
        tvTitle = findViewById(R.id.tvPlayerTitle);
        tvAuthor = findViewById(R.id.tvPlayerAuthor);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }

    private void setupAnimations() {
        rotateAnimation = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setDuration(15000);
        rotateAnimation.setRepeatCount(Animation.INFINITE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        SessionToken sessionToken = new SessionToken(this, new ComponentName(this, AudioPlaybackService.class));
        controllerFuture = new MediaController.Builder(this, sessionToken).buildAsync();
        controllerFuture.addListener(() -> {
            try {
                MediaController controller = controllerFuture.get();
                setupController(controller);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, MoreExecutors.directExecutor());
    }

    private void setupController(MediaController controller) {
        updateUI(controller.isPlaying());
        
        fabPlayPause.setOnClickListener(v -> {
            if (controller.isPlaying()) {
                controller.pause();
            } else {
                controller.play();
            }
        });

        controller.addListener(new Player.Listener() {
            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
                updateUI(isPlaying);
            }
        });
    }

    private void updateUI(boolean isPlaying) {
        if (isPlaying) {
            fabPlayPause.setImageResource(android.R.drawable.ic_media_pause);
            if (ivBookCover != null) ivBookCover.startAnimation(rotateAnimation);
        } else {
            fabPlayPause.setImageResource(android.R.drawable.ic_media_play);
            if (ivBookCover != null) ivBookCover.clearAnimation();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (controllerFuture != null) {
            MediaController.releaseFuture(controllerFuture);
        }
    }
}
