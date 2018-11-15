package com.pratamatechnocraft.silaporanpenjualan.Model;

public class ModelKeranjang {
    long kdKeranjang;
    String kdBarang;
    String qty;

    public long getKdKeranjang() {
        return kdKeranjang;
    }

    public String getKdBarang() {
        return kdBarang;
    }

    public String getQty() {
        return qty;
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

}
