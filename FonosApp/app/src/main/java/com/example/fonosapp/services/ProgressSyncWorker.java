package com.example.fonosapp.services;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class ProgressSyncWorker extends Worker {

    public ProgressSyncWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            // Giả lập việc đồng bộ
            Thread.sleep(2000);
            return Result.success();
        } catch (InterruptedException e) {
            return Result.failure();
        }
    }
}
