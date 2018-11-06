package com.pratamatechnocraft.silaporanpenjualan.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.pratamatechnocraft.silaporanpenjualan.Adapter.AdapterRecycleViewDataPiutang;
import com.pratamatechnocraft.silaporanpenjualan.Adapter.AdapterRecycleViewDataTransaksiPembelian;
import com.pratamatechnocraft.silaporanpenjualan.Adapter.AdapterRecycleViewDataTransaksiPenjualan;
import com.pratamatechnocraft.silaporanpenjualan.Adapter.AdapterRecycleViewDataUtang;
import com.pratamatechnocraft.silaporanpenjualan.CheckoutActivity;
import com.pratamatechnocraft.silaporanpenjualan.Model.BaseUrlApiModel;
import com.pratamatechnocraft.silaporanpenjualan.Model.ListItemTransaksi;
import com.pratamatechnocraft.silaporanpenjualan.R;
import com.pratamatechnocraft.silaporanpenjualan.Service.SessionManager;
import com.pratamatechnocraft.silaporanpenjualan.TransaksiBaruActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@SuppressLint("ValidFragment")
public class TransaksiFragment extends Fragment{

    Integer menuTab,jenisTransaksi;
    private RecyclerView recyclerViewDataTransaksi;
    private RecyclerView.Adapter adapterDataTransaksi;
    LinearLayout noDataTransaksi, koneksiDataTransaksi;
    SwipeRefreshLayout refreshDataTransaksi;
    ProgressBar progressBarDataTransaksi;
    Button cobaLagiDataTransaksi;
    FloatingActionButton fabTransaksiBaru;
    SessionManager sessionManager;
    NavigationView navigationView;

    private List<ListItemTransaksi> listItemTransaksis;

    BaseUrlApiModel baseUrlApiModel = new BaseUrlApiModel();
    private String baseUrl=baseUrlApiModel.getBaseURL();
    private static final String API_URL = "api/surat_masuk?api=suratmasukall";

    public TransaksiFragment(Integer menuTab, Integer jenisTransaksi) {
        this.menuTab = menuTab;
        this.jenisTransaksi = jenisTransaksi;
    }

    @SuppressLint("RestrictedApi")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_transaksi, container, false);
        navigationView = getActivity().findViewById( R.id.nav_view );
        noDataTransaksi = view.findViewById( R.id.noDataTransaksi );
        refreshDataTransaksi = (SwipeRefreshLayout) view.findViewById(R.id.refreshDataTransaksi);
        cobaLagiDataTransaksi = view.findViewById( R.id.cobaLagiTransaksi );
        koneksiDataTransaksi = view.findViewById( R.id.koneksiDataTransaksi );
        fabTransaksiBaru = view.findViewById( R.id.fabTransaksiBaru );

        sessionManager = new SessionManager( getContext() );
        HashMap<String, String> transaksi = sessionManager.getUserDetail();

        recyclerViewDataTransaksi = (RecyclerView) view.findViewById(R.id.recycleViewDataTransaksi);
        recyclerViewDataTransaksi.setHasFixedSize(true);
        recyclerViewDataTransaksi.setLayoutManager(new LinearLayoutManager(getContext()));

        listItemTransaksis = new ArrayList<>();
        if (jenisTransaksi==0){
            if (menuTab==0){
                adapterDataTransaksi = new AdapterRecycleViewDataTransaksiPenjualan( listItemTransaksis, getContext());
                fabTransaksiBaru.setVisibility( View.VISIBLE );
            }else {
                adapterDataTransaksi = new AdapterRecycleViewDataPiutang( listItemTransaksis, getContext());
                fabTransaksiBaru.setVisibility( View.GONE );
            }
        }else{
            if (menuTab==0){
                adapterDataTransaksi = new AdapterRecycleViewDataTransaksiPembelian( listItemTransaksis, getContext());
                fabTransaksiBaru.setVisibility( View.VISIBLE );
            }else {
                adapterDataTransaksi = new AdapterRecycleViewDataUtang( listItemTransaksis, getContext());
                fabTransaksiBaru.setVisibility( View.GONE );
            }
        }

        fabTransaksiBaru.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), TransaksiBaruActivity.class );
                startActivity(i);
            }
        } );

        progressBarDataTransaksi = view.findViewById( R.id.progressBarDataTransaksi );

        loadSuratMasuk();

        refreshDataTransaksi.setOnRefreshListener( new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listItemTransaksis.clear();
                adapterDataTransaksi.notifyDataSetChanged();
                loadSuratMasuk();
            }
        } );

        cobaLagiDataTransaksi.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                koneksiDataTransaksi.setVisibility( View.GONE );
                progressBarDataTransaksi.setVisibility( View.VISIBLE );
                loadSuratMasuk();
            }
        } );

        recyclerViewDataTransaksi.setAdapter( adapterDataTransaksi );

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        if (jenisTransaksi==0){
            getActivity().setTitle("Trasaksi Penjualan");
        }else{
            getActivity().setTitle("Trasaksi Pembelian");
        }

    }

    private void loadSuratMasuk(){


        ListItemTransaksi listItemDataTransaksi = new ListItemTransaksi(
                "#2928",
                "Rp. 15.000",
                "22 Oktober 2018"
        );

        listItemTransaksis.add( listItemDataTransaksi );
        adapterDataTransaksi.notifyDataSetChanged();

        ListItemTransaksi listItemDataTransaksi1 = new ListItemTransaksi(
                "#2928",
                "Rp. 900.000",
                "22 Oktober 2018"
        );

        listItemTransaksis.add( listItemDataTransaksi1 );
        adapterDataTransaksi.notifyDataSetChanged();

        refreshDataTransaksi.setRefreshing( false );
        progressBarDataTransaksi.setVisibility( View.GONE );
        koneksiDataTransaksi.setVisibility( View.GONE);

        /*StringRequest stringRequest = new StringRequest( Request.Method.GET, baseUrl+API_URL,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getInt( "jml_data" )==0){
                            noDataTransaksi.setVisibility( View.VISIBLE );
                        }else{
                            noDataTransaksi.setVisibility( View.GONE );
                            JSONArray data = jsonObject.getJSONArray("data");
                            for (int i = 0; i<data.length(); i++){
                                JSONObject suratmasukobject = data.getJSONObject( i );

                                ListItemDataTransaksi listItemDataTransaksi = new ListItemDataTransaksi(
                                        suratmasukobject.getString( "id_surat_masuk"),
                                        suratmasukobject.getString( "asal_surat" ),
                                        suratmasukobject.getString( "perihal" ),
                                        suratmasukobject.getString( "tgl_arsip")
                                );

                                listItemTransaksis.add( listItemDataTransaksi );
                                adapterDataTransaksi.notifyDataSetChanged();
                            }
                        }
                        refreshDataTransaksi.setRefreshing( false );
                        progressBarDataTransaksi.setVisibility( View.GONE );
                        koneksiDataTransaksi.setVisibility( View.GONE);
                    }catch (JSONException e){
                        e.printStackTrace();
                        refreshDataTransaksi.setRefreshing( false );
                        progressBarDataTransaksi.setVisibility( View.GONE );
                        noDataTransaksi.setVisibility( View.GONE );
                        listItemTransaksis.clear();
                        koneksiDataTransaksi.setVisibility( View.VISIBLE );
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    refreshDataTransaksi.setRefreshing( false );
                    progressBarDataTransaksi.setVisibility( View.GONE );
                    noDataTransaksi.setVisibility( View.GONE );
                    listItemTransaksis.clear();
                    koneksiDataTransaksi.setVisibility( View.VISIBLE );
                }
            }
        );

        RequestQueue requestQueue = Volley.newRequestQueue( getContext() );
        requestQueue.add( stringRequest );*/
    }
}
