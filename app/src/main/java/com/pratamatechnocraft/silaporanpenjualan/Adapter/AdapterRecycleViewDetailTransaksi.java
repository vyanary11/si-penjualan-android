package com.pratamatechnocraft.silaporanpenjualan.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.pratamatechnocraft.silaporanpenjualan.InvoiceActivity;
import com.pratamatechnocraft.silaporanpenjualan.Model.ListItemDetailTransaksi;
import com.pratamatechnocraft.silaporanpenjualan.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterRecycleViewDetailTransaksi extends RecyclerView.Adapter<AdapterRecycleViewDetailTransaksi.ViewHolder> {

    private List<ListItemDetailTransaksi> listItemDetailTransaksis;
    private Context context;


    public AdapterRecycleViewDetailTransaksi(List<ListItemDetailTransaksi> listItemDetailTransaksis, Context context) {
        this.listItemDetailTransaksis = listItemDetailTransaksis;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate( R.layout.list_item_detail_transaksi,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final ListItemDetailTransaksi listItemDetailTransaksi = listItemDetailTransaksis.get(position);

        int subTotal = Integer.parseInt(listItemDetailTransaksi.getQty()) * Integer.parseInt(listItemDetailTransaksi.getHarga());

        holder.txtNamaBarangDetailTransaksi.setText(listItemDetailTransaksi.getNamaBarang());
        holder.txtQtyDetailTransaksi.setText(listItemDetailTransaksi.getQty());
        holder.txtHargaDetailTransaksi.setText(listItemDetailTransaksi.getHarga());
        holder.txtSubTotalDetailTransaksi.setText("Rp. "+String.valueOf( subTotal ));

    }

    @Override
    public int getItemCount() {
        return listItemDetailTransaksis.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView txtNamaBarangDetailTransaksi, txtQtyDetailTransaksi, txtHargaDetailTransaksi, txtSubTotalDetailTransaksi;

        public ViewHolder(View itemView) {
            super(itemView);
            txtNamaBarangDetailTransaksi = (TextView) itemView.findViewById(R.id.txtNamaBarangDetailTransaksi);
            txtQtyDetailTransaksi = (TextView) itemView.findViewById(R.id.txtQtyDetailTransaksi);
            txtHargaDetailTransaksi = (TextView) itemView.findViewById(R.id.txtHargaDetailTransaksi);
            txtSubTotalDetailTransaksi = (TextView) itemView.findViewById(R.id.txtSubTotalDetailTransaksi);
        }
    }
}
