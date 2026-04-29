package com.Kohisop;
public class toMYR extends TukarUang {
    private final double rate = 4;

    @Override
    public double Tukar(double Total){
        return Total / rate;
    }

    @Override
    public String getMataUang() {
        return "MYR";
    }
}