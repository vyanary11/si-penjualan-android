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

    private CardView kliknotifikasi, kliksuratmasuk, kliksuratkeluar, klikdisposisi;
    NavigationView navigationView;
    SessionManager sessionManager;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.activity_dashboard_fragment, container, false);
        kliknotifikasi = view.findViewById(R.id.cardhomenotifikasi);
        kliksuratmasuk = view.findViewById(R.id.cardhomesuratmasuk);
        kliksuratkeluar = view.findViewById(R.id.cardhomesuratkeluar);
        klikdisposisi = view.findViewById(R.id.cardhomedisposisi);
        navigationView = getActivity().findViewById( R.id.nav_view );

        sessionManager = new SessionManager( getContext() );
        HashMap<String, String> user = sessionManager.getUserDetail();

        /*kliknotifikasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigationView.getMenu().getItem(1).setChecked(true);
                NotifikasiFragment Notifikasi = new NotifikasiFragment();

                getFragmentManager().beginTransaction()
                        .replace(R.id.screen_area, Notifikasi)
                        .addToBackStack(null)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
            }
        });

        kliksuratmasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigationView.getMenu().getItem(2).setChecked(true);
                SuratMasukFragment SuratMasuk = new SuratMasukFragment();

                getFragmentManager().beginTransaction()
                        .replace(R.id.screen_area, SuratMasuk)
                        .addToBackStack(null)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
            }
        });

        kliksuratkeluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigationView.getMenu().getItem(3).setChecked(true);
                SuratKeluarFragment SuratKeluar = new SuratKeluarFragment();

                getFragmentManager().beginTransaction()
                        .replace(R.id.screen_area, SuratKeluar)
                        .addToBackStack(null)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
            }
        });

        klikdisposisi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigationView.getMenu().getItem(4).setChecked(true);
                DisposisiFragment Disposisi = new DisposisiFragment();

                getFragmentManager().beginTransaction()
                        .replace(R.id.screen_area, Disposisi)
                        .addToBackStack(null)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
            }
        });*/

        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("E-Arsip | Dashboard");

    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}
