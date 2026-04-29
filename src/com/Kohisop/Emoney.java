package com.Kohisop;

public class Emoney extends MetodeBayar {

    public Emoney() {
        this.name = "eMoney";
        this.wallet = 0;
    }

    @Override
    public void topUp(double amount) {
        this.wallet += amount;
        System.out.printf("Top up eMoney sebesar %.2f (IDR) berhasil. Saldo: %.2f (IDR)%n", amount, this.wallet);
    }

    @Override
    public void pay(double amount) {
        this.wallet -= amount;
        System.out.printf("Pembayaran eMoney sebesar %.2f (IDR) berhasil. Saldo: %.2f (IDR)%n", amount, this.wallet);
    }

    @Override
    public int getDiskon() {
        return 7; // diskon 7%
    }

    @Override
    public int getBiayaAdmin() {
        return 20; // biaya admin 20 IDR
    }

    @Override
    public String getNamaBayar() {
        return "eMoney";
    }
}