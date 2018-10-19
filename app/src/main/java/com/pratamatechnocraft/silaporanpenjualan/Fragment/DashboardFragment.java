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

    private CardView kliktransaksijual, kliktransaksibeli, klikbarang, klikkategori, klikuser, kliklapharian, kliklapbulanan, kliklaptahunan, kliklaplabarugi, klikprofile;
    NavigationView navigationView;
    SessionManager sessionManager;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.activity_dashboard_fragment, container, false);
        kliktransaksijual = view.findViewById(R.id.cardhometransaksipenjualan);
        kliktransaksibeli = view.findViewById(R.id.cardhometransaksipembelian);
        klikbarang = view.findViewById(R.id.cardhomebarang);
        klikkategori = view.findViewById(R.id.cardhomekategori);
        klikuser = view.findViewById(R.id.cardhomeuser);
        kliklapharian = view.findViewById(R.id.cardhomelapharian);
        kliklapbulanan = view.findViewById(R.id.cardhomelapbulanan);
        kliklaptahunan = view.findViewById(R.id.cardhomelaptahunan);
        kliklaplabarugi = view.findViewById(R.id.cardhomelaplabarugi);
        klikprofile = view.findViewById(R.id.cardhomeprofile);
        navigationView = getActivity().findViewById( R.id.nav_view );

        sessionManager = new SessionManager( getContext() );
        HashMap<String, String> user = sessionManager.getUserDetail();

        kliktransaksijual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigationView.getMenu().getItem(1).setChecked(true);
                TransaksiJualFragment transaksiJualFragment = new TransaksiJualFragment();

                getFragmentManager().beginTransaction()
                        .replace(R.id.screen_area, transaksiJualFragment)
                        .addToBackStack(null)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
            }
        });

        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Dashboard");

    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}
