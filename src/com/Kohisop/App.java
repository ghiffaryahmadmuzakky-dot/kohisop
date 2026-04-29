package com.Kohisop;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        // declare semua variabel
        Scanner in = new Scanner(System.in);

        // instansiasi class metodeBayar
        Qris qris = new Qris();
        Emoney emoney = new Emoney();

        String [][]menu = {
                {"A1","Caffe Latte","46","Minuman"},
                {"A2","Cappuccino","46","Minuman"},
                {"E1","Caffe Americano","37","Minuman"},
                {"E2","Caffe Mocha","55","Minuman"},
                {"E3","Caramel Macchiato","59","Minuman"},
                {"E4","Asian Dolce Latte","55","Minuman"},
                {"E5","Double Shots Iced Shaken Espresso","50","Minuman"},
                {"B1","Freshly Brewed Coffee","23","Minuman"},
                {"B2","Vanilla Sweet Cream Cold Brew","50","Minuman"},
                {"B3","Cold Brew","44","Minuman"},
                {"M1","Petemania Pizza","112","Makanan"},
                {"M2","Mie Rebus Super Mario","35","Makanan"},
                {"M3","Ayam Bakar Goreng Rebus Spesial","72","Makanan"},
                {"M4","Soto Kambing Iga Guling","124","Makanan"},
                {"S1","Singkong Bakar A La Carte","37","Makanan"},
                {"S2","Ubi Cilembu Bakar Arang","58","Makanan"},
                {"S3","Tempe Mendoan","18","Makanan"},
                {"S4","Tahu Bakso Extra Telur","28","Makanan"}
        };

        //biar aplikasi tetep berjalan walau exception keterkecuali ada "CC"
        MainApp: while(true){

            // header judul menu
            System.out.println();
            System.out.print("Selamat datang dalam aplikasi Kohisop\n");
            System.out.print("Berikut merupakan menu yang tersedia pada kafe Kohisop\n\n");

            // updated: karena tabel makanan & minuman sama, bikin fungsi buat nampilin jadi 1
            printMenu("Minuman", menu);
            printMenu("Makanan", menu);

            System.out.println();
            System.out.println("Silahkan masukkan kode pesanan");
            System.out.println("Catatan: Max 5 jenis makanan dan 5 jenis minuman berbeda");
            System.out.println("Ketik 'SELESAI' apabila telah melakukan pemesanan");
            System.out.println("Untuk keluar dari program ketik: 'CC'");

            // nyimpen buat output di kuitansi
            String[] keranjangKode = new String[10];
            String[] keranjangNama = new String[10];
            int[] keranjangHarga = new int[10];
            int[] keranjangJumlah = new int[10];
            String[] keranjangKategori = new String[10];
            int totalItemPesanan = 0;

            // ini nanti buat ngecek batasan
            int jumlahMakanan = 0;
            int jumlahMinuman = 0;

            // buat ngedeteksi proses pesanan selesai apa belum
            boolean pesananBatal = false;

            LoopPesanan: while (true){
                System.out.print("\nMasukkan kode: ");
                String kodePesanan = in.nextLine().toUpperCase();

                if (kodePesanan.equals("CC")){
                    System.out.println("Pesanan dibatalkan dan program akan dihentikan");
                    break MainApp;
                } else if (kodePesanan.equals("SELESAI")){
                    if (totalItemPesanan == 0) {
                        System.out.println("Silahkan masukkan pesanan atau batal dengan ketik 'CC'");
                        continue LoopPesanan;
                    }
                    break LoopPesanan; // biar langsung ke kuitansi
                }

                boolean kodeValid = false;
                for (int i=0; i< menu.length;i++){
                    // ganti if statement biar nggak pake curly bracket
                    if (!kodePesanan.equals(menu[i][0])) continue;

                    kodeValid = true;
                    String namaMenu = menu[i][1];
                    int harga = Integer.parseInt(menu[i][2]);
                    String kategori = menu[i][3];

                    if (kategori.equals("Minuman") && jumlahMinuman >= 5){
                        System.out.println("Anda telah mencapai batas pemesanan jenis minuman");
                        break;
                    }
                    if (kategori.equals("Makanan") && jumlahMakanan >= 5){
                        System.out.println("Anda telah mencapai batas pemesanan jenis makanan");
                        break;
                    }

                    //jumlah default makanan/minuman = 1
                    int jumlahMenu = 1;
                    boolean skipPesanan = false;
                    // biar gampang print max per kategori berapa
                    String limitKategori = kategori.equals("Minuman") ? "3" : "2";

                    LoopKuantitas: while (true) {
                        System.out.printf("""
                                Catatan: Maksimal jumlah dari %s adalah %s
                                         Ketik '0'/'S' untuk membatalkan pesanan
                                         Pencet Enter untuk kuantitas default (1)
                                """, namaMenu, limitKategori );
                        System.out.print("Masukkan jumlah " + namaMenu + ": ");

                        String inputJumlah = in.nextLine();
                        // user input CC pas di input jumlah
                        switch (inputJumlah) {
                            case "CC":
                                System.out.println("Pesanan dibatalkan dan program akan dihentikan");
                                break MainApp;

                            // kalo user langsung enter nanti kesini
                            case "":
                                jumlahMenu = 1;
                                break LoopKuantitas;

                            // klo user skip pesanan
                            case "0":
                            case "S":
                                skipPesanan = true;
                                break LoopKuantitas;
                        }
                        // user masukkan angka
                        try {
                            jumlahMenu = Integer.parseInt(inputJumlah);
                            if (kategori.equals("Minuman") && (jumlahMenu < 1 || jumlahMenu > 3)){
                                System.out.println("Masukkan porsi yang valid (1-3)");
                            } else if (kategori.equals("Makanan") && (jumlahMenu < 1 || jumlahMenu > 2)){
                                System.out.println("Masukkan porsi yang valid (1-2)");
                            } else {
                                break LoopKuantitas;
                            }
                        } catch (Exception e) {
                            System.out.println("Input tidak valid, Masukkan Angka / S / Enter");
                        }
                    }

                    if (skipPesanan){
                        System.out.printf("Pesanan %s diskip\n" ,namaMenu);
                    } else {
                        keranjangNama[totalItemPesanan] = namaMenu;
                        keranjangHarga[totalItemPesanan] = harga;
                        keranjangJumlah[totalItemPesanan] = jumlahMenu;
                        keranjangKategori[totalItemPesanan] = kategori;
                        keranjangKode[totalItemPesanan] = kodePesanan;

                        totalItemPesanan++;

                        if(kategori.equals("Minuman")){jumlahMinuman++;}
                        if(kategori.equals("Makanan")){jumlahMakanan++;}

                        System.out.println(namaMenu + " berjumlahkan " + jumlahMenu + " berhasil ditambahkan\n");
                        // print item yang udah di list

                        System.out.println("\nDaftar item yang sudah di list");
                        printList(keranjangKode, keranjangNama, keranjangJumlah, keranjangKategori, totalItemPesanan, "Minuman");
                        printList(keranjangKode, keranjangNama, keranjangJumlah, keranjangKategori, totalItemPesanan, "Makanan");
                    }
//
                    break;

                }

                if (!kodeValid && !kodePesanan.isEmpty()){
                    System.out.println("Masukkan kode makanan atau minuman yang valid");
                }
            }
            if (!pesananBatal && totalItemPesanan > 0) {

                 TukarUang MataUang = new toIDR(); //Variabel untuk penukaran default "IDR"
                 boolean valid = false;

                 do {
                     //Memilih mata uang yang digunakan
                     System.out.println("");
                     System.out.println("""
                             +--------------------------------------------+
                             |             Pilihan Mata Uang              |
                             +--------------------------------------------+""");
                     System.out.printf("| %-10s | %-12s | %-14s | \n", "Mata Uang", "Nilai Tukar", "Dalam Rupiah");
                     System.out.println("+--------------------------------------------+");
                     System.out.printf("| %-10s | %-12s | %-14s | \n", "IDR", "1 IDR", "1 IDR");
                     System.out.printf("| %-10s | %-12s | %-14s | \n", "USD", "1 USD", "15 IDR");
                     System.out.printf("| %-10s | %-12s | %-14s | \n", "JPY", "10 JPY", "1 IDR");
                     System.out.printf("| %-10s | %-12s | %-14s | \n", "MYR", "1 MYR", "4 IDR");
                     System.out.printf("| %-10s | %-12s | %-14s | \n", "EUR", "1 EUR", "14 IDR");
                     System.out.println("+--------------------------------------------+");

                     System.out.println("\nTulis Mata Uang yang sesuai, contoh 'IDR'");
                     System.out.print("Mata uang Anda: ");
                     String pilihan = in.nextLine().toUpperCase(); //Biar input user selalu sama seperti case

                     switch(pilihan) {
                         case "IDR":
                             MataUang = new toIDR();
                             valid = true;
                             break;
                         case "USD":
                             MataUang = new toUSD();
                             valid = true;
                             break;
                         case "JPY":
                             MataUang = new toJPY();
                             valid = true;
                             break;
                         case "MYR":
                             MataUang = new toMYR();
                             valid = true;
                             break;
                         case "EUR":
                             MataUang = new toEUR();
                             valid = true;
                             break;
                         default:
                             System.out.println("Pilihan tidak valid, coba lagi!");
                     }
                 } while (!valid);

                // Data Dummy, ini yang perlu diganti sama temen-temen (dari variable maupun functionya)
                String currency = MataUang.getMataUang(); // dapat mata uang yang dipakai
                String paymentMethod = "Tunai";
                int diskon = 0;
                int biayaAdmin = 0;
                double pajakPerItem = 0; // ini kemungkinan pake while loop buat ngambil per row

                double totalMinumanNoTax = 0;
                double totalMinumanTax = 0;
                double totalMakananNoTax = 0;
                double totalMakananTax = 0;
                double totalTagihanAwal = totalMinumanNoTax + totalMakananNoTax;
                double totalTagihanAkhir = totalMinumanTax + totalMakananNoTax - diskon + biayaAdmin;

                System.out.println("""
                        Masukkan metode pembayaran yang ingin digunakan:
                        1. Qris
                        2. Emoney
                        3. Tunai
                        Atau anda bisa mengetik 'CC' untuk membatalkan pesanan dan keluar dari program""");
                String metodePembayaran = in.nextLine().toLowerCase();

                switch (metodePembayaran) {
                    case "1":
                    case "qris":
                        diskon      = (int)(totalTagihanAwal * qris.getDiskon() / 100.0);
                        biayaAdmin  = qris.getBiayaAdmin();
                        paymentMethod = qris.getNamaBayar();

                        double totalQris = totalTagihanAwal - diskon + biayaAdmin;
                        if (qris.wallet >= totalQris) {
                            qris.pay(totalQris);
                        } else {
                            System.out.println("""
                                    Wallet tidak cukup, apakah ingin melakukan top up?
                                    Masukkan nominal (IDR) atau 'CC' untuk keluar program""");
                            String nominal = in.nextLine().toUpperCase();
                            if (nominal.equals("CC")) break MainApp;
                            else try {
                                qris.topUp(Integer.parseInt(nominal));
                                qris.pay(totalQris);
                            } catch (Exception e) {
                                System.out.println("Masukkan angka bulat atau 'CC' untuk keluar");
                            }
                        }
                        break;

                    case "2":
                    case "emoney":
                        diskon      = (int)(totalTagihanAwal * emoney.getDiskon() / 100.0);
                        biayaAdmin  = emoney.getBiayaAdmin();
                        paymentMethod = emoney.getNamaBayar();

                        double totalEmoney = totalTagihanAwal - diskon + biayaAdmin;
                        if (emoney.wallet >= totalEmoney) {
                            emoney.pay(totalEmoney);
                        } else {
                            System.out.println("""
                                    Wallet tidak cukup, apakah ingin melakukan top up?
                                    Masukkan nominal (IDR) atau 'CC' untuk keluar program""");
                            String nominal = in.nextLine().toUpperCase();
                            if (nominal.equals("CC")) break MainApp;
                            else try {
                                emoney.topUp(Integer.parseInt(nominal));
                                emoney.pay(totalEmoney);
                            } catch (Exception e) {
                                System.out.println("Masukkan angka bulat atau 'CC' untuk keluar");
                            }
                        }
                        break;

                    case "3":
                    case "tunai":
                        paymentMethod = "Tunai";
                        // diskon = 0, biayaAdmin = 0 (sudah default)
                        break;
                }

                 System.out.println("");
                 System.out.println("""
                         +------------------------------------------------------------------+
                         |                         Kuitansi Kohisop                         |
                         +------------------------------------------------------------------+""");
                 System.out.println("\nDaftar Minuman");
                 System.out.println("Mata Uang: " + currency);
                 System.out.printf("%-4s | %-25s | %-3s | %-8s | %-5s | %s\n", "Kode", "Nama Menu", "Qty", "Hrg/Porsi", "Pajak", "Subtotal");
                 System.out.println("------------------------------------------------------------------");
                 for (int i=0; i<totalItemPesanan; i++){
                     if (keranjangKategori[i].equals("Minuman")){
                         double finalHarga = MataUang.Tukar(keranjangHarga[i]);
                         double totalPerItem = keranjangJumlah[i] * finalHarga;
                         double pajakTotalItem = 0; // data dummy
                         System.out.printf("%-4s | %-25s | %-3d | %-8.2f | %-5.2f | %.2f\n",
                                 keranjangKode[i], keranjangNama[i], keranjangJumlah[i], finalHarga, pajakTotalItem, totalPerItem);

                         totalMinumanNoTax += totalPerItem;
                         totalMinumanTax += (totalPerItem + pajakTotalItem);
                     }
                 }
                 System.out.println("\nDaftar Makanan");
                 System.out.println("Mata Uang: " + currency);
                 System.out.printf("%-4s | %-25s | %-3s | %-8s | %-5s | %s\n", "Kode", "Nama Menu", "Qty", "Hrg/Porsi", "Pajak", "Subtotal");
                 System.out.println("------------------------------------------------------------------");
                 for (int i = 0; i < totalItemPesanan; i++) {
                     if (keranjangKategori[i].equals("Makanan")) {
                         double finalHarga = MataUang.Tukar(keranjangHarga[i]);
                         double totalPerItem = finalHarga * keranjangJumlah[i];
                         double pajakTotalItem = 0;

                         System.out.printf("%-4s | %-25s | %-3d | %-8.2f | %-5.2f | %.2f\n",
                                 keranjangKode[i], keranjangNama[i], keranjangJumlah[i], finalHarga, pajakTotalItem, totalPerItem);

                         totalMakananNoTax += totalPerItem;
                         totalMakananTax += (totalPerItem + pajakTotalItem);
                     }
                 }

                 System.out.println("\n-------------------------------------------------------");
                 System.out.println("Ringkasan Pembayaran (" + currency + ")");
                 System.out.println("-------------------------------------------------------");
                 System.out.printf("%-40s : %.2f\n", "Total Minuman (Tanpa Pajak)", totalMinumanNoTax);
                 System.out.printf("%-40s : %.2f\n", "Total Minuman (Termasuk Pajak)", totalMinumanTax);
                 System.out.println("-------------------------------------------------------");
                 System.out.printf("%-40s : %.2f\n", "Total Makanan (Tanpa Pajak)", totalMakananNoTax);
                 System.out.printf("%-40s : %.2f\n", "Total Makanan (Termasuk Pajak)", totalMakananTax);
                 System.out.println("-------------------------------------------------------");
                 System.out.printf("%-40s : %.2f\n", "Total Tagihan (Sebelum Pajak/Diskon)", totalTagihanAwal);
                 System.out.printf("%-40s : %d\n", "Diskon Channel Pembayaran", diskon);
                 System.out.printf("%-40s : %d\n", "Biaya Admin Channel", biayaAdmin);
                 System.out.printf("%-40s : %s\n", "Metode Pembayaran", paymentMethod);
                 System.out.println("-------------------------------------------------------");
                 System.out.printf("%-40s : %s %.2f\n", "Total Tagihan Akhir", currency, totalTagihanAkhir);
                 System.out.println("-------------------------------------------------------");

                 System.out.println("\n        Terima kasih dan silahkan datang kembali       ");
                 System.out.println("-------------------------------------------------------\n");

                while (true) {
                    System.out.print("Apakah anda melakukan pemesanan baru? (Y/N): ");
                    String opsiAkhir = in.nextLine().toUpperCase();

                    if (opsiAkhir.equals("N")) {
                        System.out.println("Program akan ditutup");
                        break MainApp;

                    } else if (opsiAkhir.equals("Y")) {
                        System.out.println("\nPesanan baru dibuat");
                        break;

                    } else {
                        System.out.println("Input tidak valid. Masukkan Y / N");
                    }
                }

            }

        }
    }

    public static void printMenu(String kategori, String[][] menu) {
        System.out.println("+------+----------------------------------------+-----------+");
        System.out.printf("%-5s %-40s %s|\n", "| Kode", ("| Nama "+kategori), "| Harga (Rp)");
        System.out.println("+------+----------------------------------------+-----------+");
        for (int i=0; i<menu.length; i++){
            if (menu[i][3].equals(kategori)) {
                System.out.printf("%-5s  %-40s %-11s |\n", ("| "+menu[i][0]),("| " + menu[i][1]), ("| " + menu[i][2]));
            }
        }
        System.out.println("+------+----------------------------------------+-----------+");
        System.out.println();
    }

    public static void printList(String[] kode, String[] nama, int[] qty, String[] arrayKategori, int totalItem, String kategori){

        boolean keranjangTerIsi = false;
        for (int i=0; i< totalItem; i++){
            // buat ngecek ada data atau nggak di dalam list pesanan
            if (arrayKategori[i].equals(kategori)){
                keranjangTerIsi = true;
            }
            if (!keranjangTerIsi) return;
        }

        //ini buat ngeprint
        System.out.println("+------+----------------------------------------+-----------+");
        System.out.printf("%-5s %-40s %-7s|\n", "| Kode", ("| Nama " + kategori), "| Jumlah");
        System.out.println("+------+----------------------------------------+-----------+");
        for (int j = 0; j < totalItem; j++) {
            if (arrayKategori[j].equals(kategori)) {
                System.out.printf("%-5s %-40s %-7d|\n", ("| " + kode[j]), ("| " + nama[j]), qty[j]);
            }
        }
        System.out.println("+------+----------------------------------------+-----------+");
    }
}
