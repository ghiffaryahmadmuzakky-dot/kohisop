package com.Kohisop;
public class toEUR extends TukarUang {
    private final double rate = 14;

    @Override
    public double Tukar(double Total){
        return Total / rate;
    }

    @Override
    public String getMataUang() {
        return "EUR";
    }
}