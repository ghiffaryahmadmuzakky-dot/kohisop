package com.Kohisop.services;

import com.Kohisop.models.OrderItem;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Stack;

public class DapurService {

    // PriorityQueue untuk Makanan: Diurutkan dari harga termahal ke termurah (Descending)
    private final PriorityQueue<OrderItem> antreanMakanan;

    // Stack untuk Minuman: Last-Ordered-First-Served (LIFO)
    private final Stack<OrderItem> antreanMinuman;

    // Penghitung untuk memicu proses dapur setelah 3 pelanggan
    private int jumlahPelangganSelesai;

    public DapurService() {
        // Lambda expression untuk sorting PriorityQueue berdasarkan harga (b.harga - a.harga)
        this.antreanMakanan = new PriorityQueue<>((a, b) -> Integer.compare(b.harga, a.harga));
        this.antreanMinuman = new Stack<>();
        this.jumlahPelangganSelesai = 0;
    }

    // Fungsi yang dipanggil oleh Kasir setelah kuitansi dicetak
    public void terimaPesananDariKasir(LinkedList<OrderItem> pesananKasir) {
        jumlahPelangganSelesai++;

        // Memisahkan Makanan dan Minuman ke struktur datanya masing-masing
        for (OrderItem oi : pesananKasir) {
            if (oi.kategori.equalsIgnoreCase("Makanan")) {
                antreanMakanan.add(oi);
            } else if (oi.kategori.equalsIgnoreCase("Minuman")) {
                antreanMinuman.push(oi);
            }
        }

        // Cek Pemicu: Jika sudah 3 pelanggan, jalankan dapur
        if (jumlahPelangganSelesai == 3) {
            prosesAntrean();
            jumlahPelangganSelesai = 0; // Reset ke 0 untuk 3 pelanggan berikutnya
        } else {
            System.out.println("\n(Pesanan masuk antrean dapur. Menunggu " + (3 - jumlahPelangganSelesai) + " pelanggan lagi untuk mulai dimasak...)");
        }
    }

    // Fungsi utama untuk mencetak antrean yang diproses
    private void prosesAntrean() {
        System.out.println("\n=======================================================");
        System.out.println("   KASIR SELESAI (3 PELANGGAN)! PESANAN DIKIRIM KE DAPUR   ");
        System.out.println("=======================================================");

        System.out.println("\n======= TIM DAPUR MEMPROSES MAKANAN =======");
        System.out.println("[    Diproses berdasarkan harga tertinggi    ]");
        int noMakan = 1;
        while (!antreanMakanan.isEmpty()) {
            // poll() menyedot dan menghapus elemen dengan prioritas tertinggi (paling mahal)
            OrderItem makanan = antreanMakanan.poll();
            System.out.printf("%d. %-30s x%d  (Rp%d/porsi)\n", noMakan++, makanan.nama, makanan.jumlah, makanan.harga);
        }

        System.out.println("\n======= TIM DAPUR MEMPROSES MINUMAN =======");
        System.out.println("[    Diproses berdasarkan Last-Ordered-First-Served    ]");
        int noMinum = 1;
        while (!antreanMinuman.isEmpty()) {
            // pop() menyedot dan menghapus elemen yang masuk paling akhir
            OrderItem minuman = antreanMinuman.pop();
            System.out.printf("%d. %-30s x%d  (Rp%d/porsi)\n", noMinum++, minuman.nama, minuman.jumlah, minuman.harga);
        }
        System.out.println("=======================================================\n");
    }

    public void intipMinumanTeratas() {
        if (!antreanMinuman.isEmpty()) {
            OrderItem teratas = antreanMinuman.peek();
            System.out.printf("\n[INFO DAPUR] Minuman teratas yang akan dibuat duluan: %s x%d\n", teratas.nama, teratas.jumlah);
        } else {
            System.out.println("\n[INFO DAPUR] Antrean minuman saat ini sedang kosong.");
        }
    }

    public void batalkanMinumanTerakhir() {
        if (!antreanMinuman.isEmpty()) {
            OrderItem dibatalkan = antreanMinuman.pop();
            System.out.printf("\n[BATAL PESANAN] '%s x%d' berhasil dikeluarkan dari tumpukan minuman dapur.\n", dibatalkan.nama, dibatalkan.jumlah);
        } else {
            System.out.println("\n[BATAL PESANAN] Gagal membatalkan, antrean minuman kosong.");
        }
    }

    public int getJumlahAntreanMinuman() {
        return antreanMinuman.size();
    }

    // Fungsi darurat jika Kasir menekan 'N' (Tutup Toko) padahal belum mencapai 3 pelanggan
    public void prosesSisaPesananTutupToko() {
        if (!antreanMakanan.isEmpty() || !antreanMinuman.isEmpty()) {
            System.out.println("\n[INFO] Toko tutup. Memproses sisa pesanan di dapur secara paksa...");
            prosesAntrean();
        }
    }
}