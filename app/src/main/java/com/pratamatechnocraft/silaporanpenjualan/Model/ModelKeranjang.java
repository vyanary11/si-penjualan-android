package com.pratamatechnocraft.silaporanpenjualan.Model;

public class ModelKeranjang {
    long kdKeranjang;
    String kdBarang;
    String namaBrang;
    String qty;
    String HargaBarang;
    String urlGambarBarang;

    public long getKdKeranjang() {
        return kdKeranjang;
    }

    public String getKdBarang() {
        return kdBarang;
    }

    public String getQty() {
        return qty;
    }

    public String getNamaBrang() {
        return namaBrang;
    }

    public String getHargaBarang() {
        return HargaBarang;
    }

    public String getUrlGambarBarang() {
        return urlGambarBarang;
    }

    public void setKdKeranjang(long kdKeranjang) {
        this.kdKeranjang = kdKeranjang;
    }

    public void setKdBarang(String kdBarang) {
        this.kdBarang = kdBarang;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public void setNamaBrang(String namaBrang) {
        this.namaBrang = namaBrang;
    }

    public void setHargaBarang(String hargaBarang) {
        HargaBarang = hargaBarang;
    }

    public void setUrlGambarBarang(String urlGambarBarang) {
        this.urlGambarBarang = urlGambarBarang;
    }
}
