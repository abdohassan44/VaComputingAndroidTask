package com.example.vacomputingtask;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class EquationRepository {
    private final EquationDao equationDao;
    private final LiveData<List<Equation>> allpendingEquation;
    private final LiveData<List<Equation>> allslovedEquation;
    private long rowId = -1;
    private CountDownLatch mLatch;


    EquationRepository(Application application) {
        EquationٍRoomDatabase db = EquationٍRoomDatabase.getDatabase(application);
        equationDao = db.equationDao();
        allpendingEquation = equationDao.getAllPendingEquation(0);
        allslovedEquation = equationDao.getAllSlovedEquation(1);
    }


    LiveData<List<Equation>> getAllPendingEquations() {
        return allpendingEquation;
    }

    LiveData<List<Equation>> getAllSlovedEquations() {
        return allslovedEquation;
    }

    //    void insertEquation(Equation equation) {
//        return new insertAsyncTask(equationDao).execute(equation).get();
//
//        EquationٍRoomDatabase.databaseWriteExecutor.execute(() -> {
//            equationDao.insert(equation);
//        });
//    }
    public long insertEquation(Equation equation) {
        mLatch = new CountDownLatch(1);
        new insertAsyncTask(equationDao).execute(equation);
        try {
            mLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i("LOG_TAG", String.valueOf(rowId));

        return rowId;
    }

    void updateEquation(Equation equation) {
        EquationٍRoomDatabase.databaseWriteExecutor.execute(() -> {
            equationDao.update(equation);
        });
    }

    class insertAsyncTask extends AsyncTask<Equation, Void, Void> {

        public insertAsyncTask(EquationDao equationDao) {

        }

        @Override
        protected Void doInBackground(Equation... equations) {
            rowId = equationDao.insert(equations[0]);
            mLatch.countDown();
            return null;
        }
    }


}
