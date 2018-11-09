package com.pratamatechnocraft.silaporanpenjualan.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pratamatechnocraft.silaporanpenjualan.Service.SessionManager;
import com.pratamatechnocraft.silaporanpenjualan.R;

import java.util.HashMap;

public class DashboardFragment extends Fragment {

    private CardView kliktransaksijual, kliktransaksibeli, klikbarang, klikkategori, klikuser, kliklapharian, kliklapbulanan, kliklaptahunan, kliklaplabarugi, klikbiaya;
    NavigationView navigationView;
    SessionManager sessionManager;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_dashboard, container, false);
        kliktransaksijual = view.findViewById(R.id.cardhometransaksipenjualan);
        kliktransaksibeli = view.findViewById(R.id.cardhometransaksipembelian);
        klikbarang = view.findViewById(R.id.cardhomebarang);
        klikkategori = view.findViewById(R.id.cardhomekategori);
        klikuser = view.findViewById(R.id.cardhomeuser);
        kliklapharian = view.findViewById(R.id.cardhomelapharian);
        kliklapbulanan = view.findViewById(R.id.cardhomelapbulanan);
        kliklaptahunan = view.findViewById(R.id.cardhomelaptahunan);
        kliklaplabarugi = view.findViewById(R.id.cardhomelaplabarugi);
        klikbiaya = view.findViewById(R.id.cardhomebiaya);
        navigationView = getActivity().findViewById( R.id.nav_view );

        sessionManager = new SessionManager( getContext() );
        HashMap<String, String> user = sessionManager.getUserDetail();

        kliktransaksijual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigationView.setCheckedItem( R.id.nav_transaksi_penjualan );
                TabLayoutFragment tabLayoutFragment = new TabLayoutFragment(0);

                getFragmentManager().beginTransaction()
                        .replace(R.id.screen_area, tabLayoutFragment )
                        .addToBackStack(null)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
            }
        });

        kliktransaksibeli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigationView.setCheckedItem( R.id.nav_transaksi_pembelian );
                TabLayoutFragment tabLayoutFragment = new TabLayoutFragment(1);

                getFragmentManager().beginTransaction()
                        .replace(R.id.screen_area, tabLayoutFragment )
                        .addToBackStack(null)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
            }
        });

        klikbarang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigationView.setCheckedItem( R.id.nav_barang );
                DataBarangFragment dataBarangFragment = new DataBarangFragment();

                getFragmentManager().beginTransaction()
                        .replace(R.id.screen_area, dataBarangFragment )
                        .addToBackStack(null)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
            }
        });

        klikuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigationView.setCheckedItem( R.id.nav_user );
                DataUserFragment dataUserFragment = new DataUserFragment();

                getFragmentManager().beginTransaction()
                        .replace(R.id.screen_area, dataUserFragment )
                        .addToBackStack(null)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
            }
        });

        klikkategori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigationView.setCheckedItem( R.id.nav_kategori_barang );
                DataKategoriBarangFragment dataKategoriBarangFragment = new DataKategoriBarangFragment();

                getFragmentManager().beginTransaction()
                        .replace(R.id.screen_area, dataKategoriBarangFragment )
                        .addToBackStack(null)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
            }
        });

        klikbiaya.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationView.setCheckedItem( R.id.nav_biaya );
                BiayaFragment biayaFragment = new BiayaFragment();

                getFragmentManager().beginTransaction()
                        .replace(R.id.screen_area, biayaFragment )
                        .addToBackStack(null)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
            }
        } );

        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        //getActivity().setTitle(R.string.app_name);

    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

}
