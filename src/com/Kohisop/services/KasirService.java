package com.Kohisop.services;

import com.Kohisop.models.Member;
import com.Kohisop.models.MenuItem;
import com.Kohisop.models.OrderItem;
import com.Kohisop.utils.Printer;
import com.Kohisop.currency.*;
import com.Kohisop.payment.Emoney;
import com.Kohisop.payment.Qris;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

public class KasirService {
    private ArrayList<MenuItem> menu;
    private Qris qris;
    private Emoney emoney;
    private DapurService dapur;
    private ArrayList<Member> membersDb;
    private int jumlahPelanggan;

    public KasirService() {
        this.menu = buildMenu();
        this.qris = new Qris();
        this.emoney = new Emoney();
        this.dapur = new DapurService();
        this.membersDb = new ArrayList<>();
        this.jumlahPelanggan = 0;
    }

    private ArrayList<MenuItem> buildMenu() {
        ArrayList<MenuItem> list = new ArrayList<>();
        list.add(new MenuItem("A1", "Caffe Latte", 46, "Minuman"));
        list.add(new MenuItem("A2", "Cappuccino", 46, "Minuman"));
        list.add(new MenuItem("E1", "Caffe Americano", 37, "Minuman"));
        list.add(new MenuItem("E2", "Caffe Mocha", 55, "Minuman"));
        list.add(new MenuItem("E3", "Caramel Macchiato", 59, "Minuman"));
        list.add(new MenuItem("E4", "Asian Dolce Latte", 55, "Minuman"));
        list.add(new MenuItem("E5", "Double Shots Iced Shaken Espresso", 50, "Minuman"));
        list.add(new MenuItem("B1", "Freshly Brewed Coffee", 23, "Minuman"));
        list.add(new MenuItem("B2", "Vanilla Sweet Cream Cold Brew", 50, "Minuman"));
        list.add(new MenuItem("B3", "Cold Brew", 44, "Minuman"));
        list.add(new MenuItem("M1", "Petemania Pizza", 112, "Makanan"));
        list.add(new MenuItem("M2", "Mie Rebus Super Mario", 35, "Makanan"));
        list.add(new MenuItem("M3", "Ayam Bakar Goreng Rebus Spesial", 72, "Makanan"));
        list.add(new MenuItem("M4", "Soto Kambing Iga Guling", 124, "Makanan"));
        list.add(new MenuItem("S1", "Singkong Bakar A La Carte", 37, "Makanan"));
        list.add(new MenuItem("S2", "Ubi Cilembu Bakar Arang", 58, "Makanan"));
        list.add(new MenuItem("S3", "Tempe Mendoan", 18, "Makanan"));
        list.add(new MenuItem("S4", "Tahu Bakso Extra Telur", 28, "Makanan"));
        return list;
    }

    private MenuItem findMenu(String kode) {
        for (MenuItem m : menu) {
            if (m.kode.equalsIgnoreCase(kode)) return m;
        }
        return null;
    }

    private String generateMemberCode() {
        String chars = "ABCDEF0123456789";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(6);
        for(int i=0; i<6; i++) sb.append(chars.charAt(rnd.nextInt(chars.length())));
        return sb.toString();
    }

    public void mulaiSistemKasir() {
        Scanner in = new Scanner(System.in);
        ArrayList<MenuItem> menuTampil = Printer.sortedMenuForDisplay(menu);

        MainApp:
        while (true) {
            qris.wallet = 0;
            emoney.wallet = 0;

            System.out.println("\n=======================================================");
            System.out.println("  Selamat datang dalam aplikasi Kohisop (Pelanggan Ke-" + (jumlahPelanggan + 1) + ")");
            System.out.println("=======================================================\n");

            Printer.printMenu("Makanan", menuTampil);
            Printer.printMenu("Minuman", menuTampil);

            System.out.println("Silahkan masukkan kode pesanan");
            System.out.println("Catatan: Max 5 jenis makanan dan 5 jenis minuman berbeda");
            System.out.println("Ketik 'SELESAI' apabila telah melakukan pemesanan");
            System.out.println("Untuk keluar dari program ketik: 'CC'");

            LinkedList<OrderItem> pesanan = new LinkedList<>();
            int jumlahMakanan = 0;
            int jumlahMinuman = 0;

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
                        System.out.println("Keranjang kosong. Silahkan masukkan pesanan atau batal dengan ketik 'CC'");
                        continue;
                    }
                    break LoopPesanan;
                }

                MenuItem dipilih = findMenu(input);
                if (dipilih == null) {
                    System.out.println("Kode tidak valid! Masukkan kode yang tersedia di menu.");
                    continue;
                }

                boolean sudahAda = false;
                for (OrderItem oi : pesanan) {
                    if (oi.kode.equals(dipilih.kode)) { sudahAda = true; break; }
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

                LoopQty:
                while (true) {
                    System.out.printf("Catatan: Maksimal jumlah dari %s adalah %d%n", dipilih.nama, maxQty);
                    System.out.println("         Ketik '0' / 'S' untuk membatalkan, Enter untuk default (1)");
                    System.out.print("Masukkan jumlah " + dipilih.nama + ": ");
                    String qInput = in.nextLine().trim();

                    switch (qInput.toUpperCase()) {
                        case "CC": break MainApp;
                        case "": jumlahMenu = 1; break LoopQty;
                        case "0": case "S": skip = true; break LoopQty;
                    }

                    try {
                        jumlahMenu = Integer.parseInt(qInput);
                        if (jumlahMenu >= 1 && jumlahMenu <= maxQty) break LoopQty;
                        else System.out.println("Masukkan angka antara 1 dan " + maxQty);
                    } catch (NumberFormatException e) {
                        System.out.println("Input tidak valid, masukkan angka / S / Enter");
                    }
                }

                if (!skip) {
                    pesanan.add(new OrderItem(dipilih.kode, dipilih.nama, dipilih.harga, jumlahMenu, dipilih.kategori));
                    if (dipilih.kategori.equals("Minuman")) jumlahMinuman++;
                    else jumlahMakanan++;

                    System.out.printf("%s (x%d) berhasil ditambahkan%n", dipilih.nama, jumlahMenu);
                    LinkedList<OrderItem> sorted = Printer.sortedOrderList(pesanan);
                    Printer.printOrderTable(sorted, "Makanan");
                    Printer.printOrderTable(sorted, "Minuman");
                }
            }
            if (pesanan.isEmpty()) continue;

            Member currentMember = null;
            int poinSebelumTransaksi = 0;

            System.out.println("\n--------------------------------------------");
            LoopMember:
            while (true) {
                System.out.print("Apakah Anda sudah memiliki Kode Member? (Y/N): ");
                String statusMember = in.nextLine().trim().toUpperCase();

                if (statusMember.equals("Y")) {
                    System.out.print("Masukkan Kode Member Anda (atau ketik 'BATAL' untuk daftar baru): ");
                    String inputKode = in.nextLine().trim().toUpperCase();

                    if (inputKode.equals("BATAL")) {
                        continue LoopMember;
                    }

                    for (Member m : membersDb) {
                        if (m.kode.equals(inputKode)) {
                            currentMember = m;
                            poinSebelumTransaksi = m.poin;
                            break;
                        }
                    }

                    if (currentMember != null) {
                        System.out.println("Selamat datang kembali, " + currentMember.nama + "!");
                        break LoopMember;
                    } else {
                        System.out.println("[Error] Kode tidak ditemukan di database. Silahkan coba lagi.");
                    }

                } else if (statusMember.equals("N")) {
                    System.out.println("[Sistem] Mengalihkan ke pendaftaran Member Otomatis...");
                    System.out.print("Masukkan Nama Anda: ");
                    String namaBaru = in.nextLine().trim();
                    String kodeBaru = generateMemberCode();

                    currentMember = new Member(kodeBaru, namaBaru, 0);
                    membersDb.add(currentMember);

                    System.out.println("Pendaftaran berhasil! Kode Member Anda: " + kodeBaru);
                    break LoopMember;

                } else {
                    System.out.println("Input tidak valid. Harap masukkan 'Y' atau 'N'.");
                }
            }
            System.out.println("--------------------------------------------");

            boolean bebasPajak = (currentMember != null && currentMember.kode.contains("A"));
            double totalTagihanAwalIDR = 0;

            for (OrderItem oi : pesanan) {
                double subtotal = oi.harga * oi.jumlah;
                double persentasePajak = 0;

                if (!bebasPajak) {
                    if (oi.kategori.equals("Minuman")) {
                        if (oi.harga < 50) persentasePajak = 0;
                        else if (oi.harga <= 55) persentasePajak = 0.08;
                        else persentasePajak = 0.11;
                    } else {
                        if (oi.harga <= 50) persentasePajak = 0.11;
                        else persentasePajak = 0.08;
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
                System.out.printf("| %-10s | %-12s | %-14s |\n", "IDR", "1 IDR",  "1 IDR");
                System.out.printf("| %-10s | %-12s | %-14s |\n", "USD", "1 USD",  "15 IDR");
                System.out.printf("| %-10s | %-12s | %-14s |\n", "JPY", "10 JPY", "1 IDR");
                System.out.printf("| %-10s | %-12s | %-14s |\n", "MYR", "1 MYR",  "4 IDR");
                System.out.printf("| %-10s | %-12s | %-14s |\n", "EUR", "1 EUR",  "14 IDR");
                System.out.println("+--------------------------------------------+");
                System.out.print("Mata uang Anda (IDR/USD/JPY/MYR/EUR): ");

                switch (in.nextLine().trim().toUpperCase()) {
                    case "IDR": mataUang = new toIDR(); validCurr = true; break;
                    case "USD": mataUang = new toUSD(); validCurr = true; break;
                    case "JPY": mataUang = new toJPY(); validCurr = true; break;
                    case "MYR": mataUang = new toMYR(); validCurr = true; break;
                    case "EUR": mataUang = new toEUR(); validCurr = true; break;
                    default: System.out.println("Pilihan tidak valid, coba lagi!");
                }
            } while (!validCurr);
            String currency = mataUang.getMataUang();

            double potonganPoinIDR = 0;
            int poinTerpakai = 0;
            double totalSisaTagihanIDR = totalTagihanAwalIDR;

            if (currency.equals("IDR") && currentMember != null && currentMember.poin > 0) {
                double saldoPoinIDR = currentMember.poin * 2.0;
                System.out.println("\n[Sistem Poin Aktif] Anda memiliki " + currentMember.poin + " poin (Senilai " + saldoPoinIDR + " IDR)");
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
                System.out.println("- Memotong tagihan sebesar: " + potonganPoinIDR + " IDR (" + poinTerpakai + " poin terpakai)");
            } else if (currentMember != null && currentMember.poin > 0) {
                System.out.println("\n[Info] Poin tidak dapat memotong tagihan karena Anda tidak menggunakan IDR. (Poin tetap utuh).");
            }

            String paymentMethod = "Tunai";
            double diskon = 0, biayaAdmin = 0;

            if (totalSisaTagihanIDR > 0) {
                System.out.println("\nMasukkan metode pembayaran sisa tagihan:");
                System.out.println("1. QRIS\n2. eMoney\n3. Tunai\nAtau ketik 'CC' untuk membatalkan");
                String metode = in.nextLine().trim().toLowerCase();

                if (metode.equals("cc")) break MainApp;

                if (metode.equals("1") || metode.equals("qris")) {
                    diskon = totalSisaTagihanIDR * qris.getDiskon() / 100.0;
                    biayaAdmin = qris.getBiayaAdmin();
                    paymentMethod = qris.getNamaBayar();
                    double totalQ = totalSisaTagihanIDR - diskon + biayaAdmin;
                    while (qris.wallet < totalQ) {
                        System.out.printf("Wallet kurang %.0f IDR. Top up atau 'CC': ", (totalQ - qris.wallet));
                        String nom = in.nextLine().toUpperCase();
                        if (nom.equals("CC")) break MainApp;
                        try { qris.topUp(Double.parseDouble(nom)); } catch (Exception e) {}
                    }
                    qris.pay(totalQ);
                }
                else if (metode.equals("2") || metode.equals("emoney")) {
                    diskon = totalSisaTagihanIDR * emoney.getDiskon() / 100.0;
                    biayaAdmin = emoney.getBiayaAdmin();
                    paymentMethod = emoney.getNamaBayar();
                    double totalE = totalSisaTagihanIDR - diskon + biayaAdmin;
                    while (emoney.wallet < totalE) {
                        System.out.printf("Wallet kurang %.0f IDR. Top up atau 'CC': ", (totalE - emoney.wallet));
                        String nom = in.nextLine().toUpperCase();
                        if (nom.equals("CC")) break MainApp;
                        try { emoney.topUp(Double.parseDouble(nom)); } catch (Exception e) {}
                    }
                    emoney.pay(totalE);
                }
            } else {
                paymentMethod = "Poin Penuh";
            }

            double totalTagihanAkhirIDR = totalSisaTagihanIDR - diskon + biayaAdmin;

            int poinDidapat = 0;
            if (currentMember != null) {
                poinDidapat = (int)(totalTagihanAkhirIDR / 10);
                if (bebasPajak) poinDidapat *= 2;
                currentMember.poin += poinDidapat;
            }

            LinkedList<OrderItem> sortedPesanan = Printer.sortedOrderList(pesanan);

            System.out.println("\n+------------------------------------------------------------------+");
            System.out.println("|                        Kuitansi Kohisop                          |");
            System.out.println("+------------------------------------------------------------------+");
            if (currentMember != null) {
                System.out.printf("| Member: %-15s (Kode: %s)%n", currentMember.nama, currentMember.kode);
                if (bebasPajak) System.out.println("| Status: VIP (Bebas Pajak & Poin x2)");
                System.out.println("+------------------------------------------------------------------+");
            }
            System.out.println("| Mata Uang: " + currency);

            double totalMakananNoTax = 0, totalMakananTax = 0;
            double totalMinumanNoTax = 0, totalMinumanTax = 0;

            System.out.println("+------+------------------------------------+-----------+---------+----------+----------+");
            System.out.printf("| %-4s | %-34s | %-9s | %-7s | %-8s | %-8s |\n", "Kode", "Nama Makanan", "Hrg/Porsi", "Jumlah", "Pajak", "Subtotal");
            System.out.println("+------+------------------------------------+-----------+---------+----------+----------+");
            for (OrderItem oi : sortedPesanan) {
                if (!oi.kategori.equals("Makanan")) continue;
                double hargaKonv = mataUang.Tukar(oi.harga);
                double subtotal  = hargaKonv * oi.jumlah;
                double persentasePajak = (!bebasPajak) ? ((oi.harga <= 50) ? 0.11 : 0.08) : 0;
                double pajak = subtotal * persentasePajak;

                System.out.printf("| %-4s | %-34s | %-9.2f | %-7d | %-8.2f | %-8.2f |\n", oi.kode, oi.nama, hargaKonv, oi.jumlah, pajak, subtotal);
                totalMakananNoTax += subtotal;
                totalMakananTax   += subtotal + pajak;
            }

            System.out.println("+------+------------------------------------+-----------+---------+----------+----------+");
            System.out.printf("| %-4s | %-34s | %-9s | %-7s | %-8s | %-8s |\n", "Kode", "Nama Minuman", "Hrg/Porsi", "Jumlah", "Pajak", "Subtotal");
            System.out.println("+------+------------------------------------+-----------+---------+----------+----------+");
            for (OrderItem oi : sortedPesanan) {
                if (!oi.kategori.equals("Minuman")) continue;
                double hargaKonv = mataUang.Tukar(oi.harga);
                double subtotal  = hargaKonv * oi.jumlah;
                double persentasePajak = 0;
                if (!bebasPajak) {
                    if (oi.harga < 50) persentasePajak = 0;
                    else if (oi.harga <= 55) persentasePajak = 0.08;
                    else persentasePajak = 0.11;
                }
                double pajak = subtotal * persentasePajak;

                System.out.printf("| %-4s | %-34s | %-9.2f | %-7d | %-8.2f | %-8.2f |\n", oi.kode, oi.nama, hargaKonv, oi.jumlah, pajak, subtotal);
                totalMinumanNoTax += subtotal;
                totalMinumanTax   += subtotal + pajak;
            }
            System.out.println("+------+------------------------------------+-----------+---------+----------+----------+");

            double sumPajakKonv = (totalMakananTax - totalMakananNoTax) + (totalMinumanTax - totalMinumanNoTax);
            double totalTagihanAwalKonv = totalMakananNoTax + totalMinumanNoTax;
            double diskonKonv = mataUang.Tukar(diskon);
            double adminKonv = mataUang.Tukar(biayaAdmin);
            double poinKonv = mataUang.Tukar(potonganPoinIDR);
            double totalAkhirKonv = totalTagihanAwalKonv + sumPajakKonv - poinKonv - diskonKonv + adminKonv;

            System.out.println("\n-------------------------------------------------------");
            System.out.println("Ringkasan Pembayaran (" + currency + ")");
            System.out.println("-------------------------------------------------------");
            System.out.printf("%-40s : %.2f%n", "Total Makanan (Tanpa Pajak)",        totalMakananNoTax);
            System.out.printf("%-40s : %.2f%n", "Total Makanan (Termasuk Pajak)",     totalMakananTax);
            System.out.printf("%-40s : %.2f%n", "Total Minuman (Tanpa Pajak)",        totalMinumanNoTax);
            System.out.printf("%-40s : %.2f%n", "Total Minuman (Termasuk Pajak)",     totalMinumanTax);
            System.out.println("-------------------------------------------------------");
            System.out.printf("%-40s : %.2f%n", "Total Tagihan (Awal)",               totalTagihanAwalKonv + sumPajakKonv);
            System.out.printf("%-40s : %.2f%n", "Potongan Poin Member",               poinKonv);
            System.out.printf("%-40s : %.2f%n", "Diskon Channel Pembayaran",          diskonKonv);
            System.out.printf("%-40s : %.2f%n", "Biaya Admin Channel",                adminKonv);
            System.out.printf("%-40s : %s%n",   "Metode Pembayaran",                  paymentMethod);
            System.out.println("-------------------------------------------------------");
            System.out.printf("%-40s : %s %.2f%n", "Total Tagihan Akhir", currency,   totalAkhirKonv);
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
            dapur.terimaPesananDariKasir(pesanan);

            while (true) {
                System.out.print("\nApakah ada antrean pelanggan baru? (Y/N): ");
                String opsi = in.nextLine().trim().toUpperCase();
                if (opsi.equals("N")) {
                    dapur.prosesSisaPesananTutupToko();
                    System.out.println("\nProgram Kasir KohiSop Ditutup. Terima Kasih!");
                    break MainApp;
                }
                else if (opsi.equals("Y")) {
                    System.out.println("\nMelayani pelanggan baru...");
                    break;
                }
                else {
                    System.out.println("Input tidak valid. Masukkan Y / N");
                }
            }
        }
        in.close();
    }
}