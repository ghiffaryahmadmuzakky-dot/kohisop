package com.Kohisop.models;

public class OrderItem {
    public String kode;
    public String nama;
    public int harga;
    public int jumlah;
    public String kategori;

    public OrderItem(String kode, String nama, int harga, int jumlah, String kategori) {
        this.kode = kode;
        this.nama = nama;
        this.harga = harga;
        this.jumlah = jumlah;
        this.kategori = kategori;
    }
}