package com.example.prj_clock;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import androidx.core.app.NotificationCompat;

import java.util.Locale;

public class StopwatchService extends Service {

    private static final String CHANNEL_ID = "StopwatchChannel";
    private static final int NOTIFICATION_ID = 1;

    private int seconds = 0;
    private boolean running = false;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final IBinder binder = new StopwatchBinder();

    public class StopwatchBinder extends Binder {
        StopwatchService getService() {
            return StopwatchService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            if ("START".equals(action)) {
                startStopwatch();
            } else if ("STOP".equals(action)) {
                stopStopwatch();
            } else if ("RESET".equals(action)) {
                resetStopwatch();
            }
        }
        return START_STICKY;
    }

    private void startStopwatch() {
        if (!running) {
            running = true;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                startForeground(NOTIFICATION_ID, getNotification("00:00:00"), ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE);
            } else {
                startForeground(NOTIFICATION_ID, getNotification("00:00:00"));
            }
            handler.post(runTimer);
        }
    }

    private void stopStopwatch() {
        running = false;
        handler.removeCallbacks(runTimer);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            stopForeground(STOP_FOREGROUND_DETACH);
        } else {
            stopForeground(false);
        }
        updateNotification(formatTime(seconds));
    }

    private void resetStopwatch() {
        running = false;
        seconds = 0;
        handler.removeCallbacks(runTimer);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            stopForeground(STOP_FOREGROUND_REMOVE);
        } else {
            stopForeground(true);
        }
    }

    private final Runnable runTimer = new Runnable() {
        @Override
        public void run() {
            if (running) {
                seconds++;
                updateNotification(formatTime(seconds));
                handler.postDelayed(this, 1000);
            }
        }
    };

    private void updateNotification(String time) {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.notify(NOTIFICATION_ID, getNotification(time));
        }
    }

    private Notification getNotification(String time) {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Stopwatch Running")
                .setContentText(time)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .setOnlyAlertOnce(true)
                .build();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Stopwatch Service Channel",
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(serviceChannel);
            }
        }
    }

    private String formatTime(int seconds) {
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int secs = seconds % 60;
        return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, secs);
    }

    public int getSeconds() {
        return seconds;
    }

    public boolean isRunning() {
        return running;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runTimer);
    }
}
