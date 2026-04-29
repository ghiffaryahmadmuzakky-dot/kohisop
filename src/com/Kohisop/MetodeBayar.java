package com.Kohisop;

public abstract class MetodeBayar {
    protected double wallet;
    protected String name;

    public abstract void topUp(double amount);
    public abstract void pay(double amount);
    public abstract int getDiskon();
    public abstract int getBiayaAdmin();
    public abstract String getNamaBayar();

    public void cekWallet() {
        System.out.printf("%s Anda memiliki nilai sejumlah: %.2f (IDR)%n", this.name, this.wallet);
    }
}