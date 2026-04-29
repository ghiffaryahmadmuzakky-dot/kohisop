package com.Kohisop;

public class Qris extends MetodeBayar {

    public Qris() {
        this.name = "QRIS";
        this.wallet = 0;
    }

    @Override
    public void topUp(double amount) {
        this.wallet += amount;
        System.out.printf("Top up QRIS sebesar %.2f (IDR) berhasil. Saldo: %.2f (IDR)%n", amount, this.wallet);
    }

    @Override
    public void pay(double amount) {
        this.wallet -= amount;
        System.out.printf("Pembayaran QRIS sebesar %.2f (IDR) berhasil. Saldo: %.2f (IDR)%n", amount, this.wallet);
    }

    @Override
    public int getDiskon() {
        return 5; // diskon 5%
    }

    @Override
    public int getBiayaAdmin() {
        return 0; // tidak ada biaya admin
    }

    @Override
    public String getNamaBayar() {
        return "QRIS";
    }
}