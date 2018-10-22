package com.pratamatechnocraft.silaporanpenjualan.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pratamatechnocraft.silaporanpenjualan.Model.ListItemBarangTransaksi;
import com.pratamatechnocraft.silaporanpenjualan.R;

import java.util.List;

public class AdapterRecycleViewBarangTransaksiPembelian extends RecyclerView.Adapter<AdapterRecycleViewBarangTransaksiPembelian.ViewHolder> {

    private List<ListItemBarangTransaksi> listItemBarangTransaksis;
    private Context context;

    public AdapterRecycleViewBarangTransaksiPembelian(List<ListItemBarangTransaksi> listItemBarangTransaksis, Context context) {
        this.listItemBarangTransaksis = listItemBarangTransaksis;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from( parent.getContext() )
                .inflate( R.layout.list_barang_transaksi, parent, false );
        return new ViewHolder( v );
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ListItemBarangTransaksi listItemBarangTransaksi = listItemBarangTransaksis.get( position );

        holder.textViewNamaBarang.setText( listItemBarangTransaksi.getNama_barang() );
    }

    @Override
    public int getItemCount() {
        return listItemBarangTransaksis.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewNamaBarang;

        public ViewHolder(View itemView) {
            super( itemView );

            textViewNamaBarang = (TextView) itemView.findViewById( R.id.textViewNamaBarang );

        }
    }
}