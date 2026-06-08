package com.example.fonosapp.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

/**
 * 3) Bound Services: Dùng để giao tiếp trực tiếp giữa Activity và Service
 * Giúp Activity có thể gọi các hàm trực tiếp trong Service để lấy trạng thái app.
 */
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

    // Function 1: Lấy trạng thái hệ thống (Example for Report)
    public String getAppStatus() {
        return "Fonos App đang sẵn sàng phục vụ!";
    }

    // Function 2: Kiểm tra dữ liệu đồng bộ (Example for Report)
    public boolean isSyncing() {
        return false;
    }

    // Function 3: Lấy thông tin user hiện tại (Example for Report)
    public String getCurrentUserSession() {
        return "User Session: Active";
    }
}
