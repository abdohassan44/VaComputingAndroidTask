package com.example.vacomputingtask;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.ViewModelProvider;

public class CalculationService extends Service {
    Calculator mCalculator;
    EquationViewModel equationViewModel;
    MyServiceReceiver myServiceReceiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startMyOwnForeground();
        else
            startForeground(1, new Notification());

        myServiceReceiver = new MyServiceReceiver();
        mCalculator = new Calculator();
        equationViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(EquationViewModel.class);
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("sendEquation");
        registerReceiver(myServiceReceiver, intentFilter);
        return START_STICKY;
    }
    public void getResult(Equation equation) {
        int deleySec = equation.getDeley() * 1000;
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (equation.getOperationId() == 1) {
                    equation.setResult(mCalculator.add(equation.getNumber1(), equation.getNumber2()));
                } else if (equation.getOperationId() == 2) {
                    equation.setResult(mCalculator.sub(equation.getNumber1(), equation.getNumber2()));
                } else if (equation.getOperationId() == 3) {
                    equation.setResult(mCalculator.mul(equation.getNumber1(), equation.getNumber2()));
                } else if (equation.getOperationId() == 4) {
                    if (equation.getNumber1() == 0) equation.setResult(0);
                    else
                        equation.setResult(mCalculator.div(equation.getNumber1(), equation.getNumber2()));
                }
                equation.setStatus(1);
                equationViewModel.update(equation);
                Log.d("sloved", equation.getResult() + "");
            }
        }, deleySec);

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myServiceReceiver);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startMyOwnForeground() {
        String NOTIFICATION_CHANNEL_ID = "com.example.simpleapp";
        String channelName = "My Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }

    class MyServiceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (action.equals("sendEquation")) {
                Equation equation = (Equation) intent.getSerializableExtra("equation");
                getResult(equation);
            }
        }
    }
}

