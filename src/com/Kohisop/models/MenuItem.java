package com.Kohisop.models;

public class MenuItem {
    public String kode;
    public String nama;
    public int harga;
    public String kategori;

    public MenuItem(String kode, String nama, int harga, String kategori) {
        this.kode = kode;
        this.nama = nama;
        this.harga = harga;
        this.kategori = kategori;
    }
}