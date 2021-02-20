package com.example.vacomputingtask;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface EquationDao {

    @Query("SELECT * FROM equation_table where status = :status")
    LiveData<List<Equation>> getAllPendingEquation(int status);

    @Query("SELECT * FROM equation_table where status = :status")
    LiveData<List<Equation>> getAllSlovedEquation(int status);

    @Insert
    long insert(Equation equation);

    @Update
    void update(Equation equation);


}
