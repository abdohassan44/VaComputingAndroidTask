package com.example.vacomputingtask;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;


@Entity(tableName = "equation_table")

public class Equation implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "number1")
    private double number1;

    @ColumnInfo(name = "number2")
    private double number2;

    @ColumnInfo(name = "operation")
    private String operation;

    @ColumnInfo(name = "operationId")
    private int operationId;

    @ColumnInfo(name = "result")
    private double result;

    @ColumnInfo(name = "status")
    private int status;

    @ColumnInfo(name = "deley")
    private int deley;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getNumber1() {
        return number1;
    }

    public void setNumber1(double number1) {
        this.number1 = number1;
    }

    public double getNumber2() {
        return number2;
    }

    public void setNumber2(double number2) {
        this.number2 = number2;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public double getResult() {
        return result;
    }

    public void setResult(double result) {
        this.result = result;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getDeley() {
        return deley;
    }

    public void setDeley(int deley) {
        this.deley = deley;
    }

    public int getOperationId() {
        return operationId;
    }

    public void setOperationId(int operationId) {
        this.operationId = operationId;
    }
}
