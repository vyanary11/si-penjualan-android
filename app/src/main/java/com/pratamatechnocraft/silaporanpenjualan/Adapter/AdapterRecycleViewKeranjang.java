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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pratamatechnocraft.silaporanpenjualan.DetailUserActivity;
import com.pratamatechnocraft.silaporanpenjualan.Model.BaseUrlApiModel;
import com.pratamatechnocraft.silaporanpenjualan.Model.ListItemDataUser;
import com.pratamatechnocraft.silaporanpenjualan.Model.ModelKeranjang;
import com.pratamatechnocraft.silaporanpenjualan.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterRecycleViewKeranjang extends RecyclerView.Adapter<AdapterRecycleViewKeranjang.ViewHolder>  {

    private ArrayList<ModelKeranjang> modelKeranjangs;
    private Context context;
    BaseUrlApiModel baseUrlApiModel = new BaseUrlApiModel();
    private String baseUrl=baseUrlApiModel.getBaseURL();
    private int totalHarga=0;
    private int jmlItem=0;

    public int getTotalHarga() {
        return totalHarga;
    }

    public int getJmlItem() {
        return jmlItem;
    }

    public AdapterRecycleViewKeranjang(ArrayList<ModelKeranjang> modelKeranjangs, Context context) {
        this.modelKeranjangs = modelKeranjangs;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate( R.layout.list_item_data_user,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final ModelKeranjang modelKeranjang = modelKeranjangs.get(position);

        int subTotal= Integer.parseInt( modelKeranjang.getHargaBarang() )*Integer.parseInt( modelKeranjang.getQty() ) ;
        totalHarga = totalHarga+subTotal;
        jmlItem=jmlItem+Integer.parseInt( modelKeranjang.getQty());

        holder.txtNamaBarangdiKeranjang.setText(modelKeranjang.getNamaBrang());
        holder.txtHargaBarangdiKeranjang.setText(modelKeranjang.getHargaBarang());
        holder.txtQTYBarangdiKeranjang.setText(modelKeranjang.getQty());
        holder.txtSubTotalBarangdiKeranjang.setText( subTotal );

        holder.cardViewDataBarangdiKeranjang.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, DetailUserActivity.class);
                i.putExtra("kdUser", modelKeranjang.getKdKeranjang());
                context.startActivity(i);
            }
        });

        if (!modelKeranjang.getUrlGambarBarang().equals("")){
            holder.adaGambar.setVisibility( View.VISIBLE );
            holder.tidakAdaGambar.setVisibility( View.GONE );
            Glide.with(context)
                    // LOAD URL DARI INTERNET
                    .load(baseUrl+modelKeranjang.getUrlGambarBarang())
                    // LOAD GAMBAR AWAL SEBELUM GAMBAR UTAMA MUNCUL, BISA DARI LOKAL DAN INTERNET
                    .into(holder.fotoDataBarangdiKeranjang1);
        }else {
            holder.adaGambar.setVisibility( View.GONE );
            holder.tidakAdaGambar.setVisibility( View.VISIBLE );
        }
        String namaDepan=modelKeranjang.getNamaBrang();
        holder.hurufDepanBarangdiKeranjang.setText(namaDepan.substring( 0,1 ));

        int color=0;

        if (holder.hurufDepanBarangdiKeranjang.getText().equals( "A" ) || holder.hurufDepanBarangdiKeranjang.getText().equals( "a" )){
            color=R.color.amber_500;
        }else if(holder.hurufDepanBarangdiKeranjang.getText().equals( "B" ) || holder.hurufDepanBarangdiKeranjang.getText().equals( "b" )){
            color=R.color.blue_500;
        }else if(holder.hurufDepanBarangdiKeranjang.getText().equals( "C" ) || holder.hurufDepanBarangdiKeranjang.getText().equals( "c" )){
            color=R.color.blue_grey_500;
        }else if(holder.hurufDepanBarangdiKeranjang.getText().equals( "D" ) || holder.hurufDepanBarangdiKeranjang.getText().equals( "d" )){
            color=R.color.brown_500;
        }else if(holder.hurufDepanBarangdiKeranjang.getText().equals( "E" ) || holder.hurufDepanBarangdiKeranjang.getText().equals( "e" )){
            color=R.color.cyan_500;
        }else if(holder.hurufDepanBarangdiKeranjang.getText().equals( "F" ) || holder.hurufDepanBarangdiKeranjang.getText().equals( "f" )){
            color=R.color.deep_orange_500;
        }else if(holder.hurufDepanBarangdiKeranjang.getText().equals( "G" ) || holder.hurufDepanBarangdiKeranjang.getText().equals( "g" )){
            color=R.color.deep_purple_500;
        }else if(holder.hurufDepanBarangdiKeranjang.getText().equals( "H" ) || holder.hurufDepanBarangdiKeranjang.getText().equals( "h" )){
            color=R.color.green_500;
        }else if(holder.hurufDepanBarangdiKeranjang.getText().equals( "I" ) || holder.hurufDepanBarangdiKeranjang.getText().equals( "i" )){
            color=R.color.grey_500;
        }else if(holder.hurufDepanBarangdiKeranjang.getText().equals( "J" ) || holder.hurufDepanBarangdiKeranjang.getText().equals( "j" )){
            color=R.color.indigo_500;
        }else if(holder.hurufDepanBarangdiKeranjang.getText().equals( "K" ) || holder.hurufDepanBarangdiKeranjang.getText().equals( "k" )){
            color=R.color.teal_500;
        }else if(holder.hurufDepanBarangdiKeranjang.getText().equals( "L" ) || holder.hurufDepanBarangdiKeranjang.getText().equals( "l" )){
            color=R.color.lime_500;
        }else if(holder.hurufDepanBarangdiKeranjang.getText().equals( "M" ) || holder.hurufDepanBarangdiKeranjang.getText().equals( "m" )){
            color=R.color.red_500;
        }else if(holder.hurufDepanBarangdiKeranjang.getText().equals( "N" ) || holder.hurufDepanBarangdiKeranjang.getText().equals( "n" )){
            color=R.color.light_blue_500;
        }else if(holder.hurufDepanBarangdiKeranjang.getText().equals( "O" ) || holder.hurufDepanBarangdiKeranjang.getText().equals( "o" )){
            color=R.color.light_green_500;
        }else if(holder.hurufDepanBarangdiKeranjang.getText().equals( "P" ) || holder.hurufDepanBarangdiKeranjang.getText().equals( "p" )){
            color=R.color.orange_500;
        }else if(holder.hurufDepanBarangdiKeranjang.getText().equals( "Q" ) || holder.hurufDepanBarangdiKeranjang.getText().equals( "q" )){
            color=R.color.pink_500;
        }else if(holder.hurufDepanBarangdiKeranjang.getText().equals( "R" ) || holder.hurufDepanBarangdiKeranjang.getText().equals( "r" )){
            color=R.color.red_600;
        }else if(holder.hurufDepanBarangdiKeranjang.getText().equals( "S" ) || holder.hurufDepanBarangdiKeranjang.getText().equals( "s" )){
            color=R.color.yellow_600;
        }else if(holder.hurufDepanBarangdiKeranjang.getText().equals( "T" ) || holder.hurufDepanBarangdiKeranjang.getText().equals( "t" )){
            color=R.color.blue_600;
        }else if(holder.hurufDepanBarangdiKeranjang.getText().equals( "U" ) || holder.hurufDepanBarangdiKeranjang.getText().equals( "u" )){
            color=R.color.cyan_600;
        }else if(holder.hurufDepanBarangdiKeranjang.getText().equals( "V" ) || holder.hurufDepanBarangdiKeranjang.getText().equals( "v" )){
            color=R.color.green_600;
        }else if(holder.hurufDepanBarangdiKeranjang.getText().equals( "W" ) || holder.hurufDepanBarangdiKeranjang.getText().equals( "w" )){
            color=R.color.purple_600;
        }else if(holder.hurufDepanBarangdiKeranjang.getText().equals( "X" ) || holder.hurufDepanBarangdiKeranjang.getText().equals( "x" )){
            color=R.color.pink_600;
        }else if(holder.hurufDepanBarangdiKeranjang.getText().equals( "Y" ) || holder.hurufDepanBarangdiKeranjang.getText().equals( "y" )){
            color=R.color.lime_600;
        }else if(holder.hurufDepanBarangdiKeranjang.getText().equals( "Z" ) || holder.hurufDepanBarangdiKeranjang.getText().equals( "z" )){
            color=R.color.orange_600;
        }

        holder.fotoDataBarangdiKeranjang.setImageResource(color);
    }

    @Override
    public int getItemCount() {
        return modelKeranjangs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView txtNamaBarangdiKeranjang, txtQTYBarangdiKeranjang, txtHargaBarangdiKeranjang, txtSubTotalBarangdiKeranjang,hurufDepanBarangdiKeranjang;
        public CardView cardViewDataBarangdiKeranjang;
        public CircleImageView fotoDataBarangdiKeranjang1, fotoDataBarangdiKeranjang;
        public RelativeLayout adaGambar, tidakAdaGambar;

        public ViewHolder(View itemView) {
            super(itemView);
            txtNamaBarangdiKeranjang = (TextView) itemView.findViewById(R.id.txtNamaBarangdiKeranjang);
            txtHargaBarangdiKeranjang = (TextView) itemView.findViewById(R.id.txtHargaBarangdiKeranjang);
            txtQTYBarangdiKeranjang = (TextView) itemView.findViewById(R.id.txtQTYBarangdiKeranjang);
            cardViewDataBarangdiKeranjang = (CardView) itemView.findViewById(R.id.cardViewDataBarangdiKeranjang);
            hurufDepanBarangdiKeranjang = (TextView) itemView.findViewById(R.id.hurufDepanUser);
            fotoDataBarangdiKeranjang = (CircleImageView) itemView.findViewById( R.id.fotoDataUser );
            fotoDataBarangdiKeranjang1 = (CircleImageView) itemView.findViewById( R.id.fotoDataUser1 );
            adaGambar = (RelativeLayout) itemView.findViewById( R.id.adaGambar );
            tidakAdaGambar = (RelativeLayout) itemView.findViewById( R.id.tidakAdaGambar );
        }
    }



}
