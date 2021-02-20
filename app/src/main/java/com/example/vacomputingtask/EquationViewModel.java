package com.example.vacomputingtask;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class EquationViewModel extends AndroidViewModel {

    private final LiveData<List<Equation>> allpendingEquation;
    private final LiveData<List<Equation>> allslovedEquation;
    private final EquationRepository equationRepository;

    public EquationViewModel(Application application) {
        super(application);
        equationRepository = new EquationRepository(application);
        allpendingEquation = equationRepository.getAllPendingEquations();
        allslovedEquation = equationRepository.getAllSlovedEquations();
    }

    LiveData<List<Equation>> getAllpendingEquation() {
        return allpendingEquation;
    }

    LiveData<List<Equation>> getAllslovedEquation() {
        return allslovedEquation;
    }

    long insert(Equation equation) {
        return equationRepository.insertEquation(equation);
    }

    void update(Equation equation) {
        equationRepository.updateEquation(equation);
    }

}
