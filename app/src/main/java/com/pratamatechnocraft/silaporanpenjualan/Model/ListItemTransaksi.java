package com.pratamatechnocraft.silaporanpenjualan.Model;

public class ListItemTransaksi {
    private String noInvoice;
    private String jotalHarga;
    private String tanggalTransaksi;

    public ListItemTransaksi(String noInvoice, String jotalHarga, String tanggalTransaksi) {
        this.noInvoice = noInvoice;
        this.jotalHarga = jotalHarga;
        this.tanggalTransaksi = tanggalTransaksi;
    }

    public String getNoInvoice() {
        return noInvoice;
    }

    public String getTotalHarga() {
        return jotalHarga;
    }

    public String getTanggalTransaksi() {
        return tanggalTransaksi;
    }

}
