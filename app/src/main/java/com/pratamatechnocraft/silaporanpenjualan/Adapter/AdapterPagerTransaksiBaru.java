package com.pratamatechnocraft.silaporanpenjualan.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.pratamatechnocraft.silaporanpenjualan.BarangTransaksiActivity;
import com.pratamatechnocraft.silaporanpenjualan.Model.ModelKeranjang;
import com.pratamatechnocraft.silaporanpenjualan.R;

import java.util.ArrayList;

public class AdapterPagerTransaksiBaru extends PagerAdapter {

    private int[] layouts;
    private LayoutInflater layoutInflater;
    private Context context;
    private RecyclerView recyclerViewKeranjang;
    private AdapterRecycleViewKeranjang adapterRecycleViewKeranjang;
    private DBDataSourceKeranjang dbDataSourceKeranjang;
    private ArrayList<ModelKeranjang> modelKeranjangs;
    private TextView noDataKeranjang,txtJmlItemKeranjang, txtHargaTotalKeranjang;
    private Button tambahBarangKeKeranjang;

    public AdapterPagerTransaksiBaru(int[] layouts, Context context) {
        this.layouts = layouts;
        layoutInflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        this.context = context;
    }

    @Override
    public int getCount() {
        return layouts.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view==o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = layoutInflater.inflate( layouts[position],container,false );
        container.addView( view );
        dbDataSourceKeranjang = new DBDataSourceKeranjang(context);
        // buka kontroller
        dbDataSourceKeranjang.open();

        // ambil semua data barang
        modelKeranjangs = dbDataSourceKeranjang.getAllKeranjang();
        noDataKeranjang = view.findViewById( R.id.noDataKeranjang );
        txtJmlItemKeranjang = view.findViewById( R.id.txtJmlItemKeranjang );
        txtHargaTotalKeranjang = view.findViewById( R.id.txtHargaTotallKeranjang );
        recyclerViewKeranjang = view.findViewById( R.id.recycleViewKeranjang );
        tambahBarangKeKeranjang =view.findViewById( R.id.tambahBarangKeKeranjang );
        if (position==0){
            recyclerViewKeranjang.setHasFixedSize(true);
            recyclerViewKeranjang.setLayoutManager(new LinearLayoutManager(context));
            adapterRecycleViewKeranjang = new AdapterRecycleViewKeranjang( modelKeranjangs, context);
            recyclerViewKeranjang.setAdapter( adapterRecycleViewKeranjang );
            txtJmlItemKeranjang.setText(String.valueOf( adapterRecycleViewKeranjang.getJmlItem() ));
            txtHargaTotalKeranjang.setText( "Rp. "+String.valueOf( adapterRecycleViewKeranjang.getTotalHarga()) );
            adapterRecycleViewKeranjang.notifyDataSetChanged();
            if (adapterRecycleViewKeranjang.getItemCount()==0){
                noDataKeranjang.setVisibility( View.VISIBLE );
                recyclerViewKeranjang.setVisibility( View.GONE );
            }

            tambahBarangKeKeranjang.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent( context.getApplicationContext(), BarangTransaksiActivity.class);
                    intent.putExtra( "type", "0" );
                    context.getApplicationContext().startActivity(intent);
                }
            } );
        }else if(position==1){

        }
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        View view = (View) object;
        container.removeView( view );
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}
