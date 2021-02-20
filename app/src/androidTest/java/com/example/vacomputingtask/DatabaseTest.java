package com.example.vacomputingtask;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
class DatabaseTest {

    EquationDao equationDao;

    @Mock
    LiveData<List<Equation>> pendingEquationsMock;


    @Mock
    LiveData<List<Equation>> solvedEquationsMock;

    @Before
    public void setUp()
    {
        Context context = ApplicationProvider.getApplicationContext();
        EquationٍRoomDatabase database = EquationٍRoomDatabase.getDatabase(context);
        equationDao = database.equationDao();
    }

    @Test
    public void loadPendingEquations()
    {
        LiveData<List<Equation>> results = equationDao.getAllPendingEquation(0);

        assertEquals(results,pendingEquationsMock);
    }


    @Test
    public void loadSolvedEquations()
    {
        LiveData<List<Equation>> results = equationDao.getAllSlovedEquation(1);

        assertEquals(results,solvedEquationsMock);
    }


}