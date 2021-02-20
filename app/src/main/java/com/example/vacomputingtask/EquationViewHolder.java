package com.example.vacomputingtask;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class EquationViewHolder extends RecyclerView.ViewHolder {

    private final TextView equation;


    private EquationViewHolder(View itemView) {
        super(itemView);
        equation = itemView.findViewById(R.id.TV_equation);


    }

    static EquationViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.equation_item, parent, false);
        return new EquationViewHolder(view);
    }

    public void bind(Equation equationObject) {

        if (equationObject.getStatus() == 1)
            equation.setText(equationObject.getNumber1() + equationObject.getOperation() + equationObject.getNumber2() + " = " + equationObject.getResult());

        else
            equation.setText(equationObject.getNumber1() + equationObject.getOperation() + equationObject.getNumber2());

    }
}