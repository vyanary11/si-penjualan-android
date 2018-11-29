package com.pratamatechnocraft.silaporanpenjualan.Model;

public class ListItemTransaksi {
    private String noInvoice;
    private String totalHarga;
    private String tanggalTransaksi;
    private String catatan;

    public ListItemTransaksi(String noInvoice, String totalHarga, String tanggalTransaksi, String catatan) {
        this.noInvoice = noInvoice;
        this.totalHarga = totalHarga;
        this.tanggalTransaksi = tanggalTransaksi;
        this.catatan = catatan;
    }

    public String getNoInvoice() {
        return noInvoice;
    }

    public String getTotalHarga() {
        return totalHarga;
    }

    public String getTanggalTransaksi() {
        return tanggalTransaksi;
    }

    public String getCatatan() {
        return catatan;
    }

}
