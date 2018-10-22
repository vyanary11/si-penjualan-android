package com.pratamatechnocraft.silaporanpenjualan.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pratamatechnocraft.silaporanpenjualan.Model.ListItemDataKategori;
import com.pratamatechnocraft.silaporanpenjualan.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterRecycleViewDataKategori extends RecyclerView.Adapter<AdapterRecycleViewDataKategori.ViewHolder> {

    private List<ListItemDataKategori> listItemDataKategoris;
    private Context context;

    public AdapterRecycleViewDataKategori(List<ListItemDataKategori> listItemDataKategoris, Context context) {
        this.listItemDataKategoris = listItemDataKategoris;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate( R.layout.list_item_data_kategori,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final ListItemDataKategori listItemDataKategori = listItemDataKategoris.get(position);

        holder.txtNamaKategori.setText(listItemDataKategori.getNamaKategori());

        holder.cardViewDataKategori.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent i = new Intent(context, DetailSuratMasukActivity.class);
                i.putExtra("idSuratMasuk", listItemDataKategori.getIdSuratMasuk());
                context.startActivity(i);*/
            }
        });

    }

    @Override
    public int getItemCount() {
        return listItemDataKategoris.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView txtNamaKategori;
        public CardView cardViewDataKategori;

        public ViewHolder(View itemView) {
            super(itemView);
            txtNamaKategori = (TextView) itemView.findViewById(R.id.txtNamaKategori);
            cardViewDataKategori = (CardView) itemView.findViewById(R.id.cardViewDataKategori);
        }
    }



}
