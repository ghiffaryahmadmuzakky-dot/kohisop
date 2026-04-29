package com.Kohisop;
public class toJPY extends TukarUang {
    private final double rate = 10;

    @Override
    public double Tukar(double Total){
        return Total * rate;
    }

    @Override
    public String getMataUang() {
        return "JPY";
    }
}