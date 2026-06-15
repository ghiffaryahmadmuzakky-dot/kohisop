package com.Kohisop.utils;

import com.Kohisop.models.MenuItem;
import com.Kohisop.models.OrderItem;
import java.util.ArrayList;
import java.util.LinkedList;

public class Printer {

    public static ArrayList<MenuItem> sortedMenuForDisplay(ArrayList<MenuItem> menu) {
        ArrayList<MenuItem> sorted = new ArrayList<>(menu);
        sorted.sort((a, b) -> {
            int katCmp = a.kategori.compareTo(b.kategori);
            if (katCmp != 0) return -katCmp;
            return a.kode.compareTo(b.kode);
        });
        return sorted;
    }

    public static LinkedList<OrderItem> sortedOrderList(LinkedList<OrderItem> pesanan) {
        LinkedList<OrderItem> sorted = new LinkedList<>(pesanan);
        sorted.sort((a, b) -> {
            int katCmp = a.kategori.compareTo(b.kategori);
            if (katCmp != 0) return katCmp;
            return Integer.compare(a.harga, b.harga);
        });
        return sorted;
    }

    public static void printMenu(String kategori, ArrayList<MenuItem> sortedMenu) {
        System.out.println("+------+------------------------------------+-----------+");
        System.out.printf("| %-4s | %-34s | %-9s |\n", "Kode", "Nama " + kategori, "Harga(Rp)");
        System.out.println("+------+------------------------------------+-----------+");
        for (MenuItem item : sortedMenu) {
            if (!item.kategori.equals(kategori)) continue;
            System.out.printf("| %-4s | %-34s | %-9d |\n", item.kode, item.nama, item.harga);
        }
        System.out.println("+------+------------------------------------+-----------+");
        System.out.println();
    }

    public static void printOrderTable(LinkedList<OrderItem> sortedPesanan, String kategori) {
        boolean hasItem = false;
        for (OrderItem oi : sortedPesanan) {
            if (oi.kategori.equals(kategori)) { hasItem = true; break; }
        }
        if (!hasItem) return;

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
}