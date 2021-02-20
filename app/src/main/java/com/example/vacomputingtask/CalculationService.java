package com.example.vacomputingtask;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

public class CalculationService extends Service {
     Calculator mCalculator;
     EquationViewModel equationViewModel;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        mCalculator = new Calculator();
        equationViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(EquationViewModel.class);

        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);


    }
    public void getResult(Equation equation) {
        int deleySec = equation.getDeley() * 1000;
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (equation.getOperationId() == 1) {
                    equation.setResult(mCalculator.add(equation.getNumber1(), equation.getNumber2()));
                } else if (equation.getOperationId() == 2) {
                    equation.setResult(mCalculator.sub(equation.getNumber1() ,equation.getNumber2()));
                } else if (equation.getOperationId() == 3) {
                    equation.setResult(mCalculator.mul(equation.getNumber1() ,equation.getNumber2()));
                } else if (equation.getOperationId() == 4) {
                    if(equation.getNumber1()==0)equation.setResult(0);
                    else equation.setResult(mCalculator.div(equation.getNumber1() ,equation.getNumber2()));
                }
                equation.setStatus(1);
                equationViewModel.update(equation);
            }
        }, deleySec);

    }

}
