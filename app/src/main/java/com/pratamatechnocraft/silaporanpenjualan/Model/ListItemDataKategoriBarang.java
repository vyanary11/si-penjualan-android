package com.pratamatechnocraft.silaporanpenjualan.Model;

public class ListItemDataKategoriBarang {
    private String kdKategori, namaKategori;

    public ListItemDataKategoriBarang(String kdKategori, String namaKategori) {
        this.kdKategori = kdKategori;
        this.namaKategori = namaKategori;
    }

    public String getKdKategori() {
        return kdKategori;
    }

    public String getNamaKategori() {
        return namaKategori;
    }

}
