package com.pratamatechnocraft.silaporanpenjualan.Model;

public class ListItemBarangTransaksi {
    private String kd_barang, nama_barang, harga_jual ,gambar_barang;

    public ListItemBarangTransaksi(String kd_barang, String nama_barang, String harga_jual, String gambar_barang) {
        this.kd_barang = kd_barang;
        this.nama_barang = nama_barang;
        this.harga_jual = harga_jual;
        this.gambar_barang = gambar_barang;
    }

    public String getKd_barang() {
        return kd_barang;
    }

    public String getNama_barang() {
        return nama_barang;
    }

    public String getHarga_jual() {
        return harga_jual;
    }

    public String getGambar_barang() {
        return gambar_barang;
    }
}
