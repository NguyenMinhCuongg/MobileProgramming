package com.example.fonosapp.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class StatusBoundService extends Service {
    private final IBinder binder = new LocalBinder();

    public class LocalBinder extends Binder {
        public StatusBoundService getService() {
            return StatusBoundService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public String getAppStatus() {
        return "Fonos App đang sẵn sàng phục vụ!";
    }
}
