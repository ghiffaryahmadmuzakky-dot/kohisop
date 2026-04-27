package com.Kohisop;
// ini masih proof of concept, masih bisa salah dan perlu di modif
public abstract class  MetodeBayar {
    protected double wallet;
    protected String name;
    public abstract void topUp(double amount);
    public abstract void pay(double amount);
    public void cekWallet() {
        System.out.printf("%s Anda memiliki nilai sejumlah: %.2f (IDR)", this.name, this.wallet);
    }
}
