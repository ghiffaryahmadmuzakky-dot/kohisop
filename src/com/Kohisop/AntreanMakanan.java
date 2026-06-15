package com.Kohisop;
import java.util.PriorityQueue;


public class AntreanMakanan {
    
class daftarMakanan implements Comparable<daftarMakanan> {
    String nama;
    int harga;
    int jumlah;

    public daftarMakanan (String nama, int harga, int jumlah) {
        this.nama = nama;
        this.harga = harga;
        this.jumlah = jumlah;
    }

    @Override
    public int compareTo(daftarMakanan other) {
        return Integer.compare(other.harga, this.harga);
    }
}

    private final PriorityQueue<daftarMakanan> antrean;

    public AntreanMakanan() {
        this.antrean = new PriorityQueue<>();
    }

    public void tambahPesanan(String nama, int harga, int jumlah) {
        antrean.add(new daftarMakanan(nama, harga, jumlah));
    }

    public void prosesAntrean() {
        System.out.println("======= TIM DAPUR MEMPROSES MAKANAN =======");
        System.out.println("[    Diproses berdasarkan harga tertinggi    ]");
        System.out.println("============================================");
        int no = 1;
        while (!antrean.isEmpty()) {
            daftarMakanan makanan = antrean.poll();
            System.out.printf("%d. %-35s x%d  (Rp%d/porsi)%n", no, makanan.nama, makanan.jumlah, makanan.harga);
            no++;
        }
    }

    public boolean isEmpty() {
        return antrean.isEmpty();
    }
}
