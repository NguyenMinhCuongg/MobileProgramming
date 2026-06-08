package com.example.fonosapp.services;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

public class ImageDownloadService extends JobIntentService {
    private static final int JOB_ID = 1001;

    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, ImageDownloadService.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        String imageUrl = intent.getStringExtra("url");
        Log.d("ImageDownloadService", "Đang tải ảnh từ: " + imageUrl);
        
        try {
            Thread.sleep(3000);
            Log.d("ImageDownloadService", "Tải ảnh hoàn tất!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
