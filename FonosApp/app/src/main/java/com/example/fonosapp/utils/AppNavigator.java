package com.example.fonosapp.utils;

import android.app.Activity;
import android.content.Intent;
import androidx.core.app.ActivityOptionsCompat;
import com.example.fonosapp.R;

public class AppNavigator {

    public static void navigateTo(Activity currentActivity, Class<?> targetActivityClass, boolean finishCurrent) {
        Intent intent = new Intent(currentActivity, targetActivityClass);
        
        // Cải tiến Animation: Slide Up chuyên nghiệp cho Fonos
        ActivityOptionsCompat options = ActivityOptionsCompat.makeCustomAnimation(
                currentActivity, R.anim.slide_up, android.R.anim.fade_out);
        
        currentActivity.startActivity(intent, options.toBundle());
        
        if (finishCurrent) {
            currentActivity.finish();
        }
    }
}
