package com.step.home_work.models;

import com.step.home_work.utils.Utils;

public class Triangle {

    private double sideA;

    private double sideB;

    private double sideC;

    public Triangle(double sideA, double sideB, double sideC) {
        this.sideA = sideA;
        this.sideB = sideB;
        this.sideC = sideC;
    }

    public Triangle() {

        //Генерировать значиения, пока они не будут корректными
        do{
            this.sideA = Utils.getRandom(3.,20.);
            this.sideB = Utils.getRandom(3.,20.);
            this.sideC = Utils.getRandom(3.,20.);
        }while (!isTriangle(sideA,sideB,sideC));
    }

    //Вычисление площади
    public double square(){
        double p = perimeter()/2;
        return Math.sqrt(p*(p-sideA)*(p-sideB)*(p-sideC));
    }

    //Вычисление периметра
    public double perimeter(){
        return sideA+sideB+sideC;
    }

    public static boolean isTriangle(double a, double b, double c){
        return a+b>c && a+c>b && b+c>a && (a>0 && b>0 && c>0);
    }

    //region Accessors
    public double getSideA() {
        return sideA;
    }

    public void setSideA(double sideA) {
        this.sideA = sideA;
    }

    public double getSideB() {
        return sideB;
    }

    public void setSideB(double sideB) {
        this.sideB = sideB;
    }

    public double getSideC() {
        return sideC;
    }

    public void setSideC(double sideC) {
        this.sideC = sideC;
    }
    //endregion
}
