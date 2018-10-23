package com.pratamatechnocraft.silaporanpenjualan.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pratamatechnocraft.silaporanpenjualan.Model.ListItemBiaya;
import com.pratamatechnocraft.silaporanpenjualan.R;

import java.util.List;

public class AdapterRecycleViewDataBiaya extends RecyclerView.Adapter<AdapterRecycleViewDataBiaya.ViewHolder> {

    private List<ListItemBiaya> listItemBiayas;
    private Context context;

    public AdapterRecycleViewDataBiaya(List<ListItemBiaya> listItemBiayas, Context context) {
        this.listItemBiayas = listItemBiayas;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate( R.layout.list_item_biaya,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final ListItemBiaya listItemBiaya = listItemBiayas.get(position);

        holder.txtNamaBiaya.setText(listItemBiaya.getNamaBiaya());
        holder.txtTanggalBiaya.setText(listItemBiaya.getTanggalBiaya());
        holder.txtJmlBiaya.setText(listItemBiaya.getJmlBiaya());

        holder.cardViewDataBiaya.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent i = new Intent(context, DetailSuratMasukActivity.class);
                i.putExtra("idSuratMasuk", listItemDataBiaya.getIdSuratMasuk());
                context.startActivity(i);*/
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItemBiayas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView txtNamaBiaya, txtTanggalBiaya, txtJmlBiaya;
        public CardView cardViewDataBiaya;

        public ViewHolder(View itemView) {
            super(itemView);
            txtNamaBiaya = (TextView) itemView.findViewById(R.id.txtNamaBiaya);
            txtTanggalBiaya = (TextView) itemView.findViewById(R.id.txtTanggalBiaya);
            txtJmlBiaya = (TextView) itemView.findViewById(R.id.txtJmlBiaya);
            cardViewDataBiaya = (CardView) itemView.findViewById(R.id.cardViewDataBiaya);
        }
    }



}
