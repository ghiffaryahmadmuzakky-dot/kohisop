package com.Kohisop;
import com.Kohisop.currency.*;
import com.Kohisop.payment.Emoney;
import com.Kohisop.payment.Qris;
import java.util.*;

public class App {


    static class MenuItem {
        String kode;
        String nama;
        int    harga;
        String kategori; //Minuman Makanan

        MenuItem(String kode, String nama, int harga, String kategori) {
            this.kode     = kode;
            this.nama     = nama;
            this.harga    = harga;
            this.kategori = kategori;
        }
    }


    static class OrderItem {
        String kode;
        String nama;
        int    harga;
        int    jumlah;
        String kategori;

        OrderItem(String kode, String nama, int harga, int jumlah, String kategori) {
            this.kode     = kode;
            this.nama     = nama;
            this.harga    = harga;
            this.jumlah   = jumlah;
            this.kategori = kategori;
        }
    }

    static ArrayList<MenuItem> buildMenu() {
        ArrayList<MenuItem> menu = new ArrayList<>();

        // Minuman bre
        menu.add(new MenuItem("A1", "Caffe Latte", 46, "Minuman"));
        menu.add(new MenuItem("A2", "Cappuccino", 46, "Minuman"));
        menu.add(new MenuItem("E1", "Caffe Americano", 37, "Minuman"));
        menu.add(new MenuItem("E2", "Caffe Mocha", 55, "Minuman"));
        menu.add(new MenuItem("E3", "Caramel Macchiato", 59, "Minuman"));
        menu.add(new MenuItem("E4", "Asian Dolce Latte", 55, "Minuman"));
        menu.add(new MenuItem("E5", "Double Shots Iced Shaken Espresso", 50, "Minuman"));
        menu.add(new MenuItem("B1", "Freshly Brewed Coffee", 23, "Minuman"));
        menu.add(new MenuItem("B2", "Vanilla Sweet Cream Cold Brew", 50, "Minuman"));
        menu.add(new MenuItem("B3", "Cold Brew", 44, "Minuman"));

        // Makanan bre 
        menu.add(new MenuItem("M1", "Petemania Pizza", 112, "Makanan"));
        menu.add(new MenuItem("M2", "Mie Rebus Super Mario", 35, "Makanan"));
        menu.add(new MenuItem("M3", "Ayam Bakar Goreng Rebus Spesial", 72, "Makanan"));
        menu.add(new MenuItem("M4", "Soto Kambing Iga Guling", 124, "Makanan"));
        menu.add(new MenuItem("S1", "Singkong Bakar A La Carte", 37, "Makanan"));
        menu.add(new MenuItem("S2", "Ubi Cilembu Bakar Arang", 58, "Makanan"));
        menu.add(new MenuItem("S3", "Tempe Mendoan", 18, "Makanan"));
        menu.add(new MenuItem("S4", "Tahu Bakso Extra Telur", 28, "Makanan"));

        return menu;
    }

    static ArrayList<MenuItem> sortedMenuForDisplay(ArrayList<MenuItem> menu) {
        ArrayList<MenuItem> sorted = new ArrayList<>(menu);
        sorted.sort((a, b) -> {
            // "Makanan".compareTo("Minuman") < 0,
            // kita balik (-katCmp) agar Minuman tampil duluan
            int katCmp = a.kategori.compareTo(b.kategori);
            if (katCmp != 0) return -katCmp;
            return a.kode.compareTo(b.kode);
        });
        return sorted;
    }

    static LinkedList<OrderItem> sortedOrderList(LinkedList<OrderItem> pesanan) {
        LinkedList<OrderItem> sorted = new LinkedList<>(pesanan);
        sorted.sort((a, b) -> {
            // "Makanan" < "Minuman" => Makanan muncul lebih dulu (ascending)
            int katCmp = a.kategori.compareTo(b.kategori);
            if (katCmp != 0) return katCmp;
            return Integer.compare(a.harga, b.harga);
        });
        return sorted;
    }

    static void printMenu(String kategori, ArrayList<MenuItem> sortedMenu) {
        System.out.println("+------+------------------------------------+-----------+");
        System.out.printf("| %-4s | %-34s | %-9s |\n",
                "Kode", "Nama " + kategori, "Harga(Rp)");
        System.out.println("+------+------------------------------------+-----------+");
        for (MenuItem item : sortedMenu) {
            if (!item.kategori.equals(kategori)) continue;
            System.out.printf("| %-4s | %-34s | %-9d |\n",
                    item.kode, item.nama, item.harga);
        }
        System.out.println("+------+------------------------------------+-----------+");
        System.out.println();
    }


    static void printOrderList(LinkedList<OrderItem> pesanan) {
        if (pesanan.isEmpty()) return;

        LinkedList<OrderItem> sorted = sortedOrderList(pesanan);

        System.out.println("\nDaftar item yang sudah dipesan:");
        System.out.println("+------+------------------------------------+-----------+---------+");
        System.out.printf("| %-4s | %-34s | %-9s | %-7s |\n",
                "Kode", "Nama Menu", "Harga", "Jumlah");
        System.out.println("+------+------------------------------------+-----------+---------+");

        String lastKat = "";
        for (OrderItem oi : sorted) {
            if (!oi.kategori.equals(lastKat)) {
                System.out.printf("| >> %-56s |\n", "[" + oi.kategori + "]");
                lastKat = oi.kategori;
            }
            System.out.printf("| %-4s | %-34s | %-9d | %-7d |\n",
                    oi.kode, oi.nama, oi.harga, oi.jumlah);
        }
        System.out.println("+------+------------------------------------+-----------+---------+");
    }

 
    static MenuItem findMenu(String kode, ArrayList<MenuItem> menu) {
        for (MenuItem m : menu) {
            if (m.kode.equalsIgnoreCase(kode)) return m;
        }
        return null;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        Qris   qris   = new Qris();
        Emoney emoney = new Emoney();

       
        ArrayList<MenuItem> menu      = buildMenu();
        
        ArrayList<MenuItem> menuTampil = sortedMenuForDisplay(menu);

      
        MainApp:
        while (true) {

            // Tampilan menu
            System.out.println();
            System.out.println("Selamat datang dalam aplikasi Kohisop");
            System.out.println("Berikut merupakan menu yang tersedia pada kafe Kohisop\n");
            printMenu("Minuman", menuTampil);
            printMenu("Makanan", menuTampil);

            System.out.println("Silahkan masukkan kode pesanan");
            System.out.println("Catatan: Max 5 jenis makanan dan 5 jenis minuman berbeda");
            System.out.println("Ketik 'SELESAI' apabila telah melakukan pemesanan");
            System.out.println("Untuk keluar dari program ketik: 'CC'");

            // LinkedList nya bray
            LinkedList<OrderItem> pesanan = new LinkedList<>();
            int jumlahMakanan = 0;
            int jumlahMinuman = 0;

            // Loop pemilihan menu 
            LoopPesanan:
            while (true) {
                System.out.print("\nMasukkan kode: ");
                String input = in.nextLine().trim().toUpperCase();

                if (input.equals("CC")) {
                    System.out.println("Pesanan dibatalkan dan program akan dihentikan");
                    break MainApp;
                }

                if (input.equals("SELESAI")) {
                    if (pesanan.isEmpty()) {
                        System.out.println("Silahkan masukkan pesanan atau batal dengan ketik 'CC'");
                        continue;
                    }
                    break LoopPesanan;
                }

                // Validasi kode
                MenuItem dipilih = findMenu(input, menu);
                if (dipilih == null) {
                    System.out.println("Kode tidak valid! Masukkan kode yang tersedia di menu.");
                    continue;
                }

                // Cek duplikat
                boolean sudahAda = false;
                for (OrderItem oi : pesanan) {
                    if (oi.kode.equals(dipilih.kode)) { sudahAda = true; break; }
                }
                if (sudahAda) {
                    System.out.println("Item " + dipilih.nama + " sudah ada dalam pesanan.");
                    continue;
                }

                // Batas 5 bray
                if (dipilih.kategori.equals("Minuman") && jumlahMinuman >= 5) {
                    System.out.println("Anda telah mencapai batas 5 jenis minuman.");
                    continue;
                }
                if (dipilih.kategori.equals("Makanan") && jumlahMakanan >= 5) {
                    System.out.println("Anda telah mencapai batas 5 jenis makanan.");
                    continue;
                }


                int     jumlahMenu = 1;
                boolean skip       = false;
                int     maxQty     = dipilih.kategori.equals("Minuman") ? 3 : 2;

                LoopQty:
                while (true) {
                    System.out.printf("Catatan: Maksimal jumlah dari %s adalah %d%n",
                            dipilih.nama, maxQty);
                    System.out.println("         Ketik '0' / 'S' untuk membatalkan, Enter untuk default (1)");
                    System.out.print("Masukkan jumlah " + dipilih.nama + ": ");
                    String qInput = in.nextLine().trim();

                    switch (qInput.toUpperCase()) {
                        case "CC":
                            System.out.println("Pesanan dibatalkan dan program akan dihentikan");
                            break MainApp;
                        case "":
                            jumlahMenu = 1;
                            break LoopQty;
                        case "0":
                        case "S":
                            skip = true;
                            break LoopQty;
                    }

                    try {
                        jumlahMenu = Integer.parseInt(qInput);
                        if (jumlahMenu < 1 || jumlahMenu > maxQty) {
                            System.out.println("Masukkan angka antara 1 dan " + maxQty);
                        } else {
                            break LoopQty;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Input tidak valid, masukkan angka / S / Enter");
                    }
                }

                if (skip) {
                    System.out.printf("Pesanan %s diskip%n", dipilih.nama);
                } else {
                    pesanan.add(new OrderItem(
                            dipilih.kode, dipilih.nama,
                            dipilih.harga, jumlahMenu, dipilih.kategori));

                    if (dipilih.kategori.equals("Minuman")) jumlahMinuman++;
                    else                                     jumlahMakanan++;

                    System.out.printf("%s (x%d) berhasil ditambahkan%n",
                            dipilih.nama, jumlahMenu);

                    printOrderList(pesanan);
                }
            }

            if (pesanan.isEmpty()) continue;

            // Pilih mata uang
            TukarUang mataUang  = new toIDR();
            boolean   validCurr = false;
            do {
                System.out.println();
                System.out.println("+--------------------------------------------+");
                System.out.println("|             Pilihan Mata Uang              |");
                System.out.println("+--------------------------------------------+");
                System.out.printf("| %-10s | %-12s | %-14s |\n",
                        "Mata Uang", "Nilai Tukar", "Dalam Rupiah");
                System.out.println("+--------------------------------------------+");
                System.out.printf("| %-10s | %-12s | %-14s |\n", "IDR", "1 IDR",  "1 IDR");
                System.out.printf("| %-10s | %-12s | %-14s |\n", "USD", "1 USD",  "15 IDR");
                System.out.printf("| %-10s | %-12s | %-14s |\n", "JPY", "10 JPY", "1 IDR");
                System.out.printf("| %-10s | %-12s | %-14s |\n", "MYR", "1 MYR",  "4 IDR");
                System.out.printf("| %-10s | %-12s | %-14s |\n", "EUR", "1 EUR",  "14 IDR");
                System.out.println("+--------------------------------------------+");
                System.out.print("Mata uang Anda: ");
                String pil = in.nextLine().trim().toUpperCase();
                switch (pil) {
                    case "IDR": mataUang = new toIDR(); validCurr = true; break;
                    case "USD": mataUang = new toUSD(); validCurr = true; break;
                    case "JPY": mataUang = new toJPY(); validCurr = true; break;
                    case "MYR": mataUang = new toMYR(); validCurr = true; break;
                    case "EUR": mataUang = new toEUR(); validCurr = true; break;
                    default: System.out.println("Pilihan tidak valid, coba lagi!");
                }
            } while (!validCurr);

            String currency = mataUang.getMataUang();

            // Hitung total IDR 
            double totalIDR = 0;
            for (OrderItem oi : pesanan) totalIDR += (double) oi.harga * oi.jumlah;

            // Metode pembayaran
            String paymentMethod = "Tunai";
            int    diskon        = 0;
            int    biayaAdmin    = 0;

            System.out.println("""
                    Masukkan metode pembayaran:
                    1. QRIS
                    2. eMoney
                    3. Tunai
                    Atau ketik 'CC' untuk membatalkan""");
            String metode = in.nextLine().trim().toLowerCase();

            switch (metode) {
                case "cc":
                    System.out.println("Pesanan dibatalkan dan program akan dihentikan");
                    break MainApp;

                case "1": case "qris":
                    diskon        = (int)(totalIDR * qris.getDiskon() / 100.0);
                    biayaAdmin    = qris.getBiayaAdmin();
                    paymentMethod = qris.getNamaBayar();
                    double totalQ = totalIDR - diskon + biayaAdmin;
                    if (qris.wallet < totalQ) {
                        System.out.println("Wallet tidak cukup. Masukkan nominal top up (IDR) atau 'CC':");
                        String nom = in.nextLine().toUpperCase();
                        if (nom.equals("CC")) break MainApp;
                        try { qris.topUp(Integer.parseInt(nom)); }
                        catch (Exception e) { System.out.println("Input tidak valid"); }
                    }
                    qris.pay(Math.min(totalQ, qris.wallet));
                    break;

                case "2": case "emoney":
                    diskon        = (int)(totalIDR * emoney.getDiskon() / 100.0);
                    biayaAdmin    = emoney.getBiayaAdmin();
                    paymentMethod = emoney.getNamaBayar();
                    double totalE = totalIDR - diskon + biayaAdmin;
                    if (emoney.wallet < totalE) {
                        System.out.println("Wallet tidak cukup. Masukkan nominal top up (IDR) atau 'CC':");
                        String nom = in.nextLine().toUpperCase();
                        if (nom.equals("CC")) break MainApp;
                        try { emoney.topUp(Integer.parseInt(nom)); }
                        catch (Exception e) { System.out.println("Input tidak valid"); }
                    }
                    emoney.pay(Math.min(totalE, emoney.wallet));
                    break;

                default:
                    paymentMethod = "Tunai";
            }

            double totalAkhirIDR  = totalIDR - diskon + biayaAdmin;
            double totalAkhirKonv = mataUang.Tukar(totalAkhirIDR);

            //  KUITANSI

            LinkedList<OrderItem> sortedPesanan = sortedOrderList(pesanan);

            System.out.println();
            System.out.println("+------------------------------------------------------------------+");
            System.out.println("|                        Kuitansi Kohisop                         |");
            System.out.println("+------------------------------------------------------------------+");
            System.out.println("| Mata Uang: " + currency);

            double totalMakananNoTax = 0, totalMakananTax = 0;
            double totalMinumanNoTax = 0, totalMinumanTax = 0;

            System.out.println("+------+------------------------------------+-----------+---------+----------+----------+");
            System.out.printf("| %-4s | %-34s | %-9s | %-7s | %-8s | %-8s |\n",
                    "Kode", "Nama Makanan", "Hrg/Porsi", "Jumlah", "Pajak", "Subtotal");
            System.out.println("+------+------------------------------------+-----------+---------+----------+----------+");
            for (OrderItem oi : sortedPesanan) {
                if (!oi.kategori.equals("Makanan")) continue;
                double hargaKonv = mataUang.Tukar(oi.harga);
                double subtotal  = hargaKonv * oi.jumlah;
                double pajak     = 0; // placeholder – implementasikan pajak di sini
                System.out.printf("| %-4s | %-34s | %-9.2f | %-7d | %-8.2f | %-8.2f |\n",
                        oi.kode, oi.nama, hargaKonv, oi.jumlah, pajak, subtotal);
                totalMakananNoTax += subtotal;
                totalMakananTax   += subtotal + pajak;
            }
            System.out.println("+------+------------------------------------+-----------+---------+----------+----------+");

            // -- Minuman --
            System.out.println("+------+------------------------------------+-----------+---------+----------+----------+");
            System.out.printf("| %-4s | %-34s | %-9s | %-7s | %-8s | %-8s |\n",
                    "Kode", "Nama Minuman", "Hrg/Porsi", "Jumlah", "Pajak", "Subtotal");
            System.out.println("+------+------------------------------------+-----------+---------+----------+----------+");
            for (OrderItem oi : sortedPesanan) {
                if (!oi.kategori.equals("Minuman")) continue;
                double hargaKonv = mataUang.Tukar(oi.harga);
                double subtotal  = hargaKonv * oi.jumlah;
                double pajak     = 0;
                System.out.printf("| %-4s | %-34s | %-9.2f | %-7d | %-8.2f | %-8.2f |\n",
                        oi.kode, oi.nama, hargaKonv, oi.jumlah, pajak, subtotal);
                totalMinumanNoTax += subtotal;
                totalMinumanTax   += subtotal + pajak;
            }
            System.out.println("+------+------------------------------------+-----------+---------+----------+----------+");

            // -- Ringkasan --
            System.out.println("\n-------------------------------------------------------");
            System.out.println("Ringkasan Pembayaran (" + currency + ")");
            System.out.println("-------------------------------------------------------");
            System.out.printf("%-40s : %.2f%n", "Total Makanan (Tanpa Pajak)",        totalMakananNoTax);
            System.out.printf("%-40s : %.2f%n", "Total Makanan (Termasuk Pajak)",     totalMakananTax);
            System.out.println("-------------------------------------------------------");
            System.out.printf("%-40s : %.2f%n", "Total Minuman (Tanpa Pajak)",        totalMinumanNoTax);
            System.out.printf("%-40s : %.2f%n", "Total Minuman (Termasuk Pajak)",     totalMinumanTax);
            System.out.println("-------------------------------------------------------");
            System.out.printf("%-40s : %.2f%n", "Total Tagihan (Sebelum Pajak/Diskon)", mataUang.Tukar(totalIDR));
            System.out.printf("%-40s : %.2f%n", "Diskon Channel Pembayaran",          mataUang.Tukar(diskon));
            System.out.printf("%-40s : %.2f%n", "Biaya Admin Channel",                mataUang.Tukar(biayaAdmin));
            System.out.printf("%-40s : %s%n",   "Metode Pembayaran",                  paymentMethod);
            System.out.println("-------------------------------------------------------");
            System.out.printf("%-40s : %s %.2f%n", "Total Tagihan Akhir", currency, totalAkhirKonv);
            System.out.println("-------------------------------------------------------");
            System.out.println("\n        Terima kasih dan silahkan datang kembali       ");
            System.out.println("-------------------------------------------------------\n");

            while (true) {
                System.out.print("Apakah anda melakukan pemesanan baru? (Y/N): ");
                String opsi = in.nextLine().trim().toUpperCase();
                if      (opsi.equals("N")) { System.out.println("Program akan ditutup"); break MainApp; }
                else if (opsi.equals("Y")) { System.out.println("\nPesanan baru dibuat"); break; }
                else                        { System.out.println("Input tidak valid. Masukkan Y / N"); }
            }
        }
    }
}