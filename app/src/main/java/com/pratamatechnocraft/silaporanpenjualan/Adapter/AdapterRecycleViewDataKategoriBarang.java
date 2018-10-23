package com.pratamatechnocraft.silaporanpenjualan.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pratamatechnocraft.silaporanpenjualan.Model.ListItemDataKategoriBarang;
import com.pratamatechnocraft.silaporanpenjualan.R;

import java.util.List;

public class AdapterRecycleViewDataKategoriBarang extends RecyclerView.Adapter<AdapterRecycleViewDataKategoriBarang.ViewHolder> {

    private List<ListItemDataKategoriBarang> listItemDataKategorisBarangs;
    private Context context;

    public AdapterRecycleViewDataKategoriBarang(List<ListItemDataKategoriBarang> listItemDataKategorisBarangs, Context context) {
        this.listItemDataKategorisBarangs = listItemDataKategorisBarangs;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate( R.layout.list_item_data_kategori_barang,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final ListItemDataKategoriBarang listItemDataKategoriBarang = listItemDataKategorisBarangs.get(position);

        holder.txtNamaKategoriBarang.setText( listItemDataKategoriBarang.getNamaKategori());

        holder.cardViewDataKategoriBarang.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent i = new Intent(context, DetailSuratMasukActivity.class);
                i.putExtra("idSuratMasuk", listItemDataKategoriBarang.getIdSuratMasuk());
                context.startActivity(i);*/
            }
        });

    }

    @Override
    public int getItemCount() {
        return listItemDataKategorisBarangs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView txtNamaKategoriBarang;
        public CardView cardViewDataKategoriBarang;

        public ViewHolder(View itemView) {
            super(itemView);
            txtNamaKategoriBarang = (TextView) itemView.findViewById(R.id.txtNamaKategoriBarang);
            cardViewDataKategoriBarang = (CardView) itemView.findViewById(R.id.cardViewDataKategoriBarang);
        }
    }



}
