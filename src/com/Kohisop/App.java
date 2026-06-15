package com.Kohisop;

import com.Kohisop.currency.*;
import com.Kohisop.payment.Emoney;
import com.Kohisop.payment.Qris;
import java.util.*;

public class App {

    static class MenuItem {
        String kode;
        String nama;
        int harga;
        String kategori;

        MenuItem(String kode, String nama, int harga, String kategori) {
            this.kode = kode;
            this.nama = nama;
            this.harga = harga;
            this.kategori = kategori;
        }
    }

    static class OrderItem {
        String kode;
        String nama;
        int harga;
        int jumlah;
        String kategori;

        OrderItem(String kode, String nama, int harga, int jumlah, String kategori) {
            this.kode = kode;
            this.nama = nama;
            this.harga = harga;
            this.jumlah = jumlah;
            this.kategori = kategori;
        }
    }

    static class Member {
        String kode;
        String nama;
        int poin;

        Member(String kode, String nama, int poin) {
            this.kode = kode;
            this.nama = nama;
            this.poin = poin;
        }
    }

    static ArrayList<MenuItem> buildMenu() {
        ArrayList<MenuItem> menu = new ArrayList<>();

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

    static String generateMemberCode() {
        String chars = "ABCDEF0123456789";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(6);
        for (int i = 0; i < 6; i++)
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        return sb.toString();
    }

    static ArrayList<MenuItem> sortedMenuForDisplay(ArrayList<MenuItem> menu) {
        ArrayList<MenuItem> sorted = new ArrayList<>(menu);
        sorted.sort((a, b) -> {
            int katCmp = a.kategori.compareTo(b.kategori);
            if (katCmp != 0)
                return -katCmp;
            return a.kode.compareTo(b.kode);
        });
        return sorted;
    }

    static LinkedList<OrderItem> sortedOrderList(LinkedList<OrderItem> pesanan) {
        LinkedList<OrderItem> sorted = new LinkedList<>(pesanan);
        sorted.sort((a, b) -> {
            int katCmp = a.kategori.compareTo(b.kategori);
            if (katCmp != 0)
                return katCmp;
            return Integer.compare(a.harga, b.harga);
        });
        return sorted;
    }

    static void printMenu(String kategori, ArrayList<MenuItem> sortedMenu) {
        System.out.println("+------+------------------------------------+-----------+");
        System.out.printf("| %-4s | %-34s | %-9s |\n", "Kode", "Nama " + kategori, "Harga(Rp)");
        System.out.println("+------+------------------------------------+-----------+");
        for (MenuItem item : sortedMenu) {
            if (!item.kategori.equals(kategori))
                continue;
            System.out.printf("| %-4s | %-34s | %-9d |\n", item.kode, item.nama, item.harga);
        }
        System.out.println("+------+------------------------------------+-----------+");
        System.out.println();
    }

    static void printOrderTable(LinkedList<OrderItem> sortedPesanan, String kategori) {
        boolean hasItem = false;
        for (OrderItem oi : sortedPesanan) {
            if (oi.kategori.equals(kategori)) {
                hasItem = true;
                break;
            }
        }
        if (!hasItem)
            return;

        System.out.println("\nDaftar " + kategori + " Dipesan:");
        System.out.println("+------+------------------------------------+-----------+---------+");
        System.out.printf("| %-4s | %-34s | %-9s | %-7s |\n", "Kode", "Nama " + kategori, "Harga", "Jumlah");
        System.out.println("+------+------------------------------------+-----------+---------+");
        for (OrderItem oi : sortedPesanan) {
            if (oi.kategori.equals(kategori)) {
                System.out.printf("| %-4s | %-34s | %-9d | %-7d |\n", oi.kode, oi.nama, oi.harga, oi.jumlah);
            }
        }
        System.out.println("+------+------------------------------------+-----------+---------+");
    }

    static MenuItem findMenu(String kode, ArrayList<MenuItem> menu) {
        for (MenuItem m : menu) {
            if (m.kode.equalsIgnoreCase(kode))
                return m;
        }
        return null;
    }

    public static void main(String[] args) {
        try (Scanner in = new Scanner(System.in)) {
            Qris qris = new Qris();
            Emoney emoney = new Emoney();

            ArrayList<MenuItem> menu = buildMenu();
            ArrayList<MenuItem> menuTampil = sortedMenuForDisplay(menu);

            ArrayList<Member> membersDb = new ArrayList<>();

            PriorityQueue<OrderItem> antreanMakanan = new PriorityQueue<>((a, b) -> Integer.compare(b.harga, a.harga));
            Stack<OrderItem> antreanMinuman = new Stack<>();

            int jumlahPelanggan = 0;

            MainApp: while (true) {
                qris.wallet = 0;
                emoney.wallet = 0;

                System.out.println("\n=======================================================");
                System.out.println(
                        "  Selamat datang dalam aplikasi Kohisop (Pelanggan Ke-" + (jumlahPelanggan + 1) + ")");
                System.out.println("=======================================================\n");

                printMenu("Makanan", menuTampil);
                printMenu("Minuman", menuTampil);

                System.out.println("Silahkan masukkan kode pesanan");
                System.out.println("Catatan: Max 5 jenis makanan dan 5 jenis minuman berbeda");
                System.out.println("Ketik 'SELESAI' apabila telah melakukan pemesanan");
                System.out.println("Untuk keluar dari program ketik: 'CC'");

                LinkedList<OrderItem> pesanan = new LinkedList<>();
                int jumlahMakanan = 0;
                int jumlahMinuman = 0;

                LoopPesanan: while (true) {
                    System.out.print("\nMasukkan kode: ");
                    String input = in.nextLine().trim().toUpperCase();

                    if (input.equals("CC")) {
                        System.out.println("Pesanan dibatalkan dan program akan dihentikan");
                        break MainApp;
                    }
                    if (input.equals("SELESAI")) {
                        if (pesanan.isEmpty()) {
                            System.out.println(
                                    "Keranjang kosong. Silahkan masukkan pesanan atau batal dengan ketik 'CC'");
                            continue;
                        }
                        break LoopPesanan;
                    }

                    MenuItem dipilih = findMenu(input, menu);
                    if (dipilih == null) {
                        System.out.println("Kode tidak valid! Masukkan kode yang tersedia di menu.");
                        continue;
                    }

                    boolean sudahAda = false;
                    for (OrderItem oi : pesanan) {
                        if (oi.kode.equals(dipilih.kode)) {
                            sudahAda = true;
                            break;
                        }
                    }
                    if (sudahAda) {
                        System.out.println("Item " + dipilih.nama + " sudah ada dalam pesanan.");
                        continue;
                    }

                    if (dipilih.kategori.equals("Minuman") && jumlahMinuman >= 5) {
                        System.out.println("Anda telah mencapai batas 5 jenis minuman.");
                        continue;
                    }
                    if (dipilih.kategori.equals("Makanan") && jumlahMakanan >= 5) {
                        System.out.println("Anda telah mencapai batas 5 jenis makanan.");
                        continue;
                    }

                    int jumlahMenu = 1;
                    boolean skip = false;
                    int maxQty = dipilih.kategori.equals("Minuman") ? 3 : 2;

                    LoopQty: while (true) {
                        System.out.printf("Catatan: Maksimal jumlah dari %s adalah %d%n", dipilih.nama, maxQty);
                        System.out.println("         Ketik '0' / 'S' untuk membatalkan, Enter untuk default (1)");
                        System.out.print("Masukkan jumlah " + dipilih.nama + ": ");
                        String qInput = in.nextLine().trim();

                        switch (qInput.toUpperCase()) {
                            case "CC":
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
                            if (jumlahMenu >= 1 && jumlahMenu <= maxQty)
                                break LoopQty;
                            else
                                System.out.println("Masukkan angka antara 1 dan " + maxQty);
                        } catch (NumberFormatException e) {
                            System.out.println("Input tidak valid, masukkan angka / S / Enter");
                        }
                    }

                    if (!skip) {
                        pesanan.add(
                                new OrderItem(dipilih.kode, dipilih.nama, dipilih.harga, jumlahMenu, dipilih.kategori));
                        if (dipilih.kategori.equals("Minuman"))
                            jumlahMinuman++;
                        else
                            jumlahMakanan++;

                        System.out.printf("%s (x%d) berhasil ditambahkan%n", dipilih.nama, jumlahMenu);
                        LinkedList<OrderItem> sorted = sortedOrderList(pesanan);
                        printOrderTable(sorted, "Makanan");
                        printOrderTable(sorted, "Minuman");
                    }
                }
                if (pesanan.isEmpty())
                    continue;

                Member currentMember = null;
                int poinSebelumTransaksi = 0;

                System.out.println("\n--------------------------------------------");
                System.out.print("Apakah Anda sudah menjadi member? (Y/N): ");
                if (in.nextLine().trim().equalsIgnoreCase("Y")) {
                    System.out.print("Masukkan Kode Member Anda: ");
                    String inputKode = in.nextLine().trim().toUpperCase();
                    for (Member m : membersDb) {
                        if (m.kode.equals(inputKode)) {
                            currentMember = m;
                            poinSebelumTransaksi = m.poin;
                            break;
                        }
                    }
                    if (currentMember != null)
                        System.out.println("Selamat datang kembali, " + currentMember.nama + "!");
                    else
                        System.out.println("Member tidak ditemukan. Lanjut sebagai Non-Member.");
                } else {
                    System.out.print("Masukkan Nama Anda untuk mendaftar Member Baru: ");
                    String namaBaru = in.nextLine().trim();
                    String kodeBaru = generateMemberCode();
                    currentMember = new Member(kodeBaru, namaBaru, 0);
                    membersDb.add(currentMember);
                    System.out.println("Pendaftaran berhasil! Kode Member Anda: " + kodeBaru);
                }

                boolean bebasPajak = (currentMember != null && currentMember.kode.contains("A"));
                double totalTagihanAwalIDR = 0;

                for (OrderItem oi : pesanan) {
                    double subtotal = oi.harga * oi.jumlah;
                    double persentasePajak = 0;

                    if (!bebasPajak) {
                        if (oi.kategori.equals("Minuman")) {
                            if (oi.harga < 50)
                                persentasePajak = 0;
                            else if (oi.harga <= 55)
                                persentasePajak = 0.08;
                            else
                                persentasePajak = 0.11;
                        } else {
                            if (oi.harga <= 50)
                                persentasePajak = 0.11;
                            else
                                persentasePajak = 0.08;
                        }
                    }
                    totalTagihanAwalIDR += (subtotal + (subtotal * persentasePajak));
                }

                TukarUang mataUang = new toIDR();
                boolean validCurr = false;
                do {
                    System.out.println("\n+--------------------------------------------+");
                    System.out.printf("| %-10s | %-12s | %-14s |\n", "Mata Uang", "Nilai Tukar", "Dalam Rupiah");
                    System.out.println("+--------------------------------------------+");
                    System.out.printf("| %-10s | %-12s | %-14s |\n", "IDR", "1 IDR", "1 IDR");
                    System.out.printf("| %-10s | %-12s | %-14s |\n", "USD", "1 USD", "15 IDR");
                    System.out.printf("| %-10s | %-12s | %-14s |\n", "JPY", "10 JPY", "1 IDR");
                    System.out.printf("| %-10s | %-12s | %-14s |\n", "MYR", "1 MYR", "4 IDR");
                    System.out.printf("| %-10s | %-12s | %-14s |\n", "EUR", "1 EUR", "14 IDR");
                    System.out.println("+--------------------------------------------+");
                    System.out.print("Mata uang Anda (IDR/USD/JPY/MYR/EUR): ");

                    switch (in.nextLine().trim().toUpperCase()) {
                        case "IDR":
                            mataUang = new toIDR();
                            validCurr = true;
                            break;
                        case "USD":
                            mataUang = new toUSD();
                            validCurr = true;
                            break;
                        case "JPY":
                            mataUang = new toJPY();
                            validCurr = true;
                            break;
                        case "MYR":
                            mataUang = new toMYR();
                            validCurr = true;
                            break;
                        case "EUR":
                            mataUang = new toEUR();
                            validCurr = true;
                            break;
                        default:
                            System.out.println("Pilihan tidak valid, coba lagi!");
                    }
                } while (!validCurr);
                String currency = mataUang.getMataUang();

                double potonganPoinIDR = 0;
                int poinTerpakai = 0;
                double totalSisaTagihanIDR = totalTagihanAwalIDR;

                if (currency.equals("IDR") && currentMember != null && currentMember.poin > 0) {
                    double saldoPoinIDR = currentMember.poin * 2.0;
                    System.out.println("\n[Sistem Poin Aktif] Anda memiliki " + currentMember.poin + " poin (Senilai "
                            + saldoPoinIDR + " IDR)");
                    if (saldoPoinIDR >= totalSisaTagihanIDR) {
                        potonganPoinIDR = totalSisaTagihanIDR;
                        poinTerpakai = (int) Math.ceil(totalSisaTagihanIDR / 2.0);
                        currentMember.poin -= poinTerpakai;
                        totalSisaTagihanIDR = 0;
                    } else {
                        potonganPoinIDR = saldoPoinIDR;
                        poinTerpakai = currentMember.poin;
                        totalSisaTagihanIDR -= potonganPoinIDR;
                        currentMember.poin = 0;
                    }
                    System.out.println("- Memotong tagihan sebesar: " + potonganPoinIDR + " IDR (" + poinTerpakai
                            + " poin terpakai)");
                }

                String paymentMethod = "Tunai";
                double diskon = 0, biayaAdmin = 0;

                if (totalSisaTagihanIDR > 0) {
                    System.out.println("\nMasukkan metode pembayaran sisa tagihan:");
                    System.out.println("1. QRIS\n2. eMoney\n3. Tunai\nAtau ketik 'CC' untuk membatalkan");
                    String metode = in.nextLine().trim().toLowerCase();

                    if (metode.equals("cc"))
                        break MainApp;

                    if (metode.equals("1") || metode.equals("qris")) {
                        diskon = totalSisaTagihanIDR * qris.getDiskon() / 100.0;
                        biayaAdmin = qris.getBiayaAdmin();
                        paymentMethod = qris.getNamaBayar();
                        double totalQ = totalSisaTagihanIDR - diskon + biayaAdmin;
                        while (qris.wallet < totalQ) {
                            System.out.printf("Wallet kurang %.0f IDR. Top up atau 'CC': ", (totalQ - qris.wallet));
                            String nom = in.nextLine().toUpperCase();
                            if (nom.equals("CC"))
                                break MainApp;
                            try {
                                qris.topUp(Double.parseDouble(nom));
                            } catch (Exception e) {
                            }
                        }
                        qris.pay(totalQ);
                    } else if (metode.equals("2") || metode.equals("emoney")) {
                        diskon = totalSisaTagihanIDR * emoney.getDiskon() / 100.0;
                        biayaAdmin = emoney.getBiayaAdmin();
                        paymentMethod = emoney.getNamaBayar();
                        double totalE = totalSisaTagihanIDR - diskon + biayaAdmin;
                        while (emoney.wallet < totalE) {
                            System.out.printf("Wallet kurang %.0f IDR. Top up atau 'CC': ", (totalE - emoney.wallet));
                            String nom = in.nextLine().toUpperCase();
                            if (nom.equals("CC"))
                                break MainApp;
                            try {
                                emoney.topUp(Double.parseDouble(nom));
                            } catch (Exception e) {
                            }
                        }
                        emoney.pay(totalE);
                    }
                } else {
                    paymentMethod = "Poin Penuh";
                }

                double totalTagihanAkhirIDR = totalSisaTagihanIDR - diskon + biayaAdmin;

                int poinDidapat = 0;
                if (currentMember != null) {
                    poinDidapat = (int) (totalTagihanAkhirIDR / 10);
                    if (bebasPajak)
                        poinDidapat *= 2;
                    currentMember.poin += poinDidapat;
                }

                LinkedList<OrderItem> sortedPesanan = sortedOrderList(pesanan);

                System.out.println("\n+------------------------------------------------------------------+");
                System.out.println("|                        Kuitansi Kohisop                          |");
                System.out.println("+------------------------------------------------------------------+");
                if (currentMember != null) {
                    System.out.printf("| Member: %-15s (Kode: %s)%n", currentMember.nama, currentMember.kode);
                    if (bebasPajak)
                        System.out.println("| Status: VIP (Bebas Pajak & Poin x2)");
                    System.out.println("+------------------------------------------------------------------+");
                }
                System.out.println("| Mata Uang: " + currency);

                double totalMakananNoTax = 0, totalMakananTax = 0;
                double totalMinumanNoTax = 0, totalMinumanTax = 0;

                System.out.println(
                        "+------+------------------------------------+-----------+---------+----------+----------+");
                System.out.printf("| %-4s | %-34s | %-9s | %-7s | %-8s | %-8s |\n", "Kode", "Nama Makanan", "Hrg/Porsi",
                        "Jumlah", "Pajak", "Subtotal");
                System.out.println(
                        "+------+------------------------------------+-----------+---------+----------+----------+");
                for (OrderItem oi : sortedPesanan) {
                    if (!oi.kategori.equals("Makanan"))
                        continue;
                    double hargaKonv = mataUang.Tukar(oi.harga);
                    double subtotal = hargaKonv * oi.jumlah;
                    double persentasePajak = (!bebasPajak) ? ((oi.harga <= 50) ? 0.11 : 0.08) : 0;
                    double pajak = subtotal * persentasePajak;

                    System.out.printf("| %-4s | %-34s | %-9.2f | %-7d | %-8.2f | %-8.2f |\n", oi.kode, oi.nama,
                            hargaKonv, oi.jumlah, pajak, subtotal);
                    totalMakananNoTax += subtotal;
                    totalMakananTax += subtotal + pajak;
                }

                System.out.println(
                        "+------+------------------------------------+-----------+---------+----------+----------+");
                System.out.printf("| %-4s | %-34s | %-9s | %-7s | %-8s | %-8s |\n", "Kode", "Nama Minuman", "Hrg/Porsi",
                        "Jumlah", "Pajak", "Subtotal");
                System.out.println(
                        "+------+------------------------------------+-----------+---------+----------+----------+");
                for (OrderItem oi : sortedPesanan) {
                    if (!oi.kategori.equals("Minuman"))
                        continue;
                    double hargaKonv = mataUang.Tukar(oi.harga);
                    double subtotal = hargaKonv * oi.jumlah;
                    double persentasePajak = 0;
                    if (!bebasPajak) {
                        if (oi.harga < 50)
                            persentasePajak = 0;
                        else if (oi.harga <= 55)
                            persentasePajak = 0.08;
                        else
                            persentasePajak = 0.11;
                    }
                    double pajak = subtotal * persentasePajak;

                    System.out.printf("| %-4s | %-34s | %-9.2f | %-7d | %-8.2f | %-8.2f |\n", oi.kode, oi.nama,
                            hargaKonv, oi.jumlah, pajak, subtotal);
                    totalMinumanNoTax += subtotal;
                    totalMinumanTax += subtotal + pajak;
                }
                System.out.println(
                        "+------+------------------------------------+-----------+---------+----------+----------+");

                double sumPajakKonv = (totalMakananTax - totalMakananNoTax) + (totalMinumanTax - totalMinumanNoTax);
                double totalTagihanAwalKonv = totalMakananNoTax + totalMinumanNoTax;
                double diskonKonv = mataUang.Tukar(diskon);
                double adminKonv = mataUang.Tukar(biayaAdmin);
                double poinKonv = mataUang.Tukar(potonganPoinIDR);
                double totalAkhirKonv = totalTagihanAwalKonv + sumPajakKonv - poinKonv - diskonKonv + adminKonv;

                System.out.println("\n-------------------------------------------------------");
                System.out.println("Ringkasan Pembayaran (" + currency + ")");
                System.out.println("-------------------------------------------------------");
                System.out.printf("%-40s : %.2f%n", "Total Makanan (Tanpa Pajak)", totalMakananNoTax);
                System.out.printf("%-40s : %.2f%n", "Total Makanan (Termasuk Pajak)", totalMakananTax);
                System.out.printf("%-40s : %.2f%n", "Total Minuman (Tanpa Pajak)", totalMinumanNoTax);
                System.out.printf("%-40s : %.2f%n", "Total Minuman (Termasuk Pajak)", totalMinumanTax);
                System.out.println("-------------------------------------------------------");
                System.out.printf("%-40s : %.2f%n", "Total Tagihan (Awal)", totalTagihanAwalKonv + sumPajakKonv);
                System.out.printf("%-40s : %.2f%n", "Potongan Poin Member", poinKonv);
                System.out.printf("%-40s : %.2f%n", "Diskon Channel Pembayaran", diskonKonv);
                System.out.printf("%-40s : %.2f%n", "Biaya Admin Channel", adminKonv);
                System.out.printf("%-40s : %s%n", "Metode Pembayaran", paymentMethod);
                System.out.println("-------------------------------------------------------");
                System.out.printf("%-40s : %s %.2f%n", "Total Tagihan Akhir", currency, totalAkhirKonv);
                System.out.println("-------------------------------------------------------");

                if (currentMember != null) {
                    System.out.println("\n--- Status Poin Membership ---");
                    System.out.println("Poin sebelum transaksi : " + poinSebelumTransaksi);
                    System.out.println("Poin digunakan         : -" + poinTerpakai);
                    System.out.println("Poin didapat (cashback): +" + poinDidapat);
                    System.out.println("Total poin saat ini    : " + currentMember.poin);
                    System.out.println("------------------------------");
                }

                jumlahPelanggan++;
                for (OrderItem oi : pesanan) {
                    if (oi.kategori.equals("Makanan")) {
                        antreanMakanan.add(oi);
                    } else {
                        antreanMinuman.push(oi);
                    }
                }

                if (jumlahPelanggan == 3) {
                    System.out.println("\n=======================================================");
                    System.out.println("   KASIR SELESAI (3 PELANGGAN)! PESANAN DIKIRIM KE DAPUR   ");
                    System.out.println("=======================================================");

                    System.out.println("\n[DAPUR MAKANAN] - Diproses Berdasarkan Prioritas Harga Tertinggi");
                    while (!antreanMakanan.isEmpty()) {
                        OrderItem item = antreanMakanan.poll();
                        System.out.printf("Sedang Memasak : %-30s (x%d) - Harga: Rp%d\n", item.nama, item.jumlah,
                                item.harga);
                    }

                    System.out.println("\n[DAPUR MINUMAN] - Diproses Berdasarkan Last-Ordered-First-Served (Stack)");
                    while (!antreanMinuman.isEmpty()) {
                        OrderItem item = antreanMinuman.pop();
                        System.out.printf("Sedang Meracik : %-30s (x%d)\n", item.nama, item.jumlah);
                    }

                    System.out.println("\n=======================================================");
                    System.out.println("           SEMUA PESANAN SELESAI DIPROSES!             ");
                    System.out.println("=======================================================\n");

                    // Reset penghitung pelanggan
                    jumlahPelanggan = 0;
                } else {
                    System.out.println("\n(Pesanan disimpan di dapur. Menunggu " + (3 - jumlahPelanggan)
                            + " pelanggan lagi untuk diproses...)");
                }

                while (true) {
                    System.out.print("\nApakah ada antrean pelanggan baru? (Y/N): ");
                    String opsi = in.nextLine().trim().toUpperCase();
                    if (opsi.equals("N")) {
                        // Jika toko tutup tapi masih ada pesanan nanggung di dapur, proses paksa
                        if (jumlahPelanggan > 0) {
                            System.out.println("\n[INFO] Toko tutup. Memproses sisa " + jumlahPelanggan
                                    + " pesanan yang ada di dapur secara paksa...");
                            System.out.println("\n[DAPUR MAKANAN] Sisa");
                            while (!antreanMakanan.isEmpty()) {
                                OrderItem item = antreanMakanan.poll();
                                System.out.printf("Sedang Memasak : %-30s (x%d)\n", item.nama, item.jumlah);
                            }
                            System.out.println("\n[DAPUR MINUMAN] Sisa");
                            while (!antreanMinuman.isEmpty()) {
                                OrderItem item = antreanMinuman.pop();
                                System.out.printf("Sedang Meracik : %-30s (x%d)\n", item.nama, item.jumlah);
                            }
                        }
                        System.out.println("\nProgram Kasir KohiSop Ditutup. Terima Kasih!");
                        break MainApp;
                    } else if (opsi.equals("Y")) {
                        System.out.println("\nMelayani pelanggan baru...");
                        break;
                    } else {
                        System.out.println("Input tidak valid. Masukkan Y / N");
                    }
                }
            }
            in.close();
        }
    }
}