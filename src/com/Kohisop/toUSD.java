package com.Kohisop;
public class toUSD extends TukarUang {
    private final double rate = 15;

    @Override
    public double Tukar(double Total){
        return Total / rate;
    }

    @Override
    public String getMataUang() {
        return "USD";
    }
}