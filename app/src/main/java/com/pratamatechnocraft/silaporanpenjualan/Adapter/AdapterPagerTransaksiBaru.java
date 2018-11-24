package com.pratamatechnocraft.silaporanpenjualan.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.pratamatechnocraft.silaporanpenjualan.BarangTransaksiActivity;
import com.pratamatechnocraft.silaporanpenjualan.Model.ModelKeranjang;
import com.pratamatechnocraft.silaporanpenjualan.R;
import com.pratamatechnocraft.silaporanpenjualan.TransaksiBaruActivity;

import java.util.ArrayList;

public class AdapterPagerTransaksiBaru extends PagerAdapter {

    private int[] layouts=null;
    private LayoutInflater layoutInflater;
    private Context context;
    private RecyclerView recyclerViewKeranjang;
    private AdapterRecycleViewKeranjang adapterRecycleViewKeranjang;
    private DBDataSourceKeranjang dbDataSourceKeranjang;
    private ArrayList<ModelKeranjang> modelKeranjangs;
    private TextView noDataKeranjang,txtJmlItemKeranjang, txtHargaTotalKeranjang;
    private Button tambahBarangKeKeranjang,buttonSimpanTransaksi,buttonBayarTransaksi;
    private TextInputLayout inputLayoutCatatanTransaksi;
    private EditText inputCatatanTransaksi;
    private int type;

    public AdapterPagerTransaksiBaru(int[] layouts, Context context, int type) {
        this.layouts = layouts;
        layoutInflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        this.context = context;
        this.type = type;
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
        dbDataSourceKeranjang.open();
        modelKeranjangs = dbDataSourceKeranjang.getAllKeranjang();
        noDataKeranjang = view.findViewById( R.id.noDataKeranjang );
        txtJmlItemKeranjang = view.findViewById( R.id.txtJmlItemKeranjang );
        txtHargaTotalKeranjang = view.findViewById( R.id.txtHargaTotallKeranjang );
        recyclerViewKeranjang = view.findViewById( R.id.recycleViewKeranjang );
        tambahBarangKeKeranjang =view.findViewById( R.id.tambahBarangKeKeranjang );

        buttonBayarTransaksi = view.findViewById( R.id.buttonBayarTransaksi );
        buttonSimpanTransaksi = view.findViewById( R.id.buttonSimpanTransaksi );
        inputLayoutCatatanTransaksi = view.findViewById(R.id.inputLayoutCatatanTransaksi);
        inputCatatanTransaksi = view.findViewById(R.id.inputCatatanTransaksi);

        if (position==0){
            recyclerViewKeranjang.setHasFixedSize(true);
            recyclerViewKeranjang.setLayoutManager(new LinearLayoutManager(context));
            adapterRecycleViewKeranjang = new AdapterRecycleViewKeranjang( modelKeranjangs, context, txtJmlItemKeranjang, txtHargaTotalKeranjang);
            recyclerViewKeranjang.setAdapter( adapterRecycleViewKeranjang );
            adapterRecycleViewKeranjang.notifyDataSetChanged();

            if (adapterRecycleViewKeranjang.getItemCount()==0){
                noDataKeranjang.setVisibility( View.VISIBLE );
                recyclerViewKeranjang.setVisibility( View.GONE );
            }

            tambahBarangKeKeranjang.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent( context.getApplicationContext(), BarangTransaksiActivity.class);
                    if (type==0){
                        intent.putExtra( "type", "0" );
                    }else{
                        intent.putExtra( "type", "1" );
                    }
                    context.getApplicationContext().startActivity(intent);
                }
            } );
        }else if(position==1){
            inputCatatanTransaksi.addTextChangedListener( new AdapterPagerTransaksiBaru.MyTextWatcher(inputCatatanTransaksi));
            if (adapterRecycleViewKeranjang.getItemCount()==0){
                notifyDataSetChanged();
            }


            buttonSimpanTransaksi.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!validateCatatanTransaksi() ) {
                        return;
                    }else {

                    }
                }
            } );

            buttonBayarTransaksi.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!validateCatatanTransaksi() ) {
                        return;
                    }else {

                    }
                }
            } );
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

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.inputCatatanTransaksi:
                    validateCatatanTransaksi();
                    break;
            }
        }
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private Window getWindow() {
        return getWindow();
    }

    private boolean validateCatatanTransaksi() {
        if (inputCatatanTransaksi.getText().toString().trim().isEmpty()) {
            inputLayoutCatatanTransaksi.setError("Masukkan Catatan");
            requestFocus(inputCatatanTransaksi);
            return false;
        } else {
            inputLayoutCatatanTransaksi.setErrorEnabled(false);
        }
        return true;
    }
}
