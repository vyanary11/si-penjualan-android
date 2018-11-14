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
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.pratamatechnocraft.silaporanpenjualan.Adapter.AdapterRecycleViewDataTransaksi;
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
    private RecyclerView.Adapter recycleViewAdapter;
    private AdapterRecycleViewDataTransaksi adapterDataTransaksi;
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
    private static String API_URL ="";

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
        progressBarDataTransaksi = view.findViewById( R.id.progressBarDataTransaksi );

        sessionManager = new SessionManager( getContext() );
        HashMap<String, String> transaksi = sessionManager.getUserDetail();

        recyclerViewDataTransaksi = (RecyclerView) view.findViewById(R.id.recycleViewDataTransaksi);

        if (menuTab==0){
            fabTransaksiBaru.setVisibility( View.VISIBLE );
        }else {
            fabTransaksiBaru.setVisibility( View.GONE );
        }

        fabTransaksiBaru.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), TransaksiBaruActivity.class );
                startActivity(i);
            }
        } );

        refreshDataTransaksi.setOnRefreshListener( new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listItemTransaksis.clear();
                if (jenisTransaksi==0){
                    if (menuTab==0){
                        loadDataTransaksi("penjualan");
                    }else {
                        loadDataTransaksi("piutang");
                    }
                }else{
                    if (menuTab==0){
                        loadDataTransaksi("pembelian");
                    }else {
                        loadDataTransaksi("utang");
                    }
                }
            }
        } );

        cobaLagiDataTransaksi.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                koneksiDataTransaksi.setVisibility( View.GONE );
                progressBarDataTransaksi.setVisibility( View.VISIBLE );
                if (jenisTransaksi==0){
                    if (menuTab==0){
                        loadDataTransaksi("penjualan");
                    }else {
                        loadDataTransaksi("piutang");
                    }
                }else{
                    if (menuTab==0){
                        loadDataTransaksi("pembelian");
                    }else {
                        loadDataTransaksi("utang");
                    }
                }
            }
        } );

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        setHasOptionsMenu( true );
        if (jenisTransaksi==0){
            getActivity().setTitle("Trasaksi Penjualan");
            if (menuTab==0){
                loadDataTransaksi("penjualan");
            }else {
                loadDataTransaksi("piutang");
            }
        }else{
            getActivity().setTitle("Trasaksi Pembelian");
            if (menuTab==0){
                loadDataTransaksi("pembelian");
            }else {
                loadDataTransaksi("utang");
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.ic_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener( new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapterDataTransaksi.getFilter().filter(s);
                return false;
            }
        } );
        searchView.setQueryHint("Cari: No Invoice, Tanggal");

    }

    private void loadDataTransaksi(String jenis){
        listItemTransaksis = new ArrayList<>();
        if (jenis.equals( "penjualan" )){
            API_URL = "api/transaksi?api=penjualan";
        }else if (jenis.equals( "piutang" )){
            API_URL = "api/transaksi?api=piutang";
        }else if (jenis.equals( "pembelian" )){
            API_URL = "api/transaksi?api=pembelian";
        }else if (jenis.equals( "utang" )){
            API_URL = "api/transaksi?api=utang";
        }


        ListItemTransaksi listItemDataTransaksi = new ListItemTransaksi(
                "#123",
                "Rp. 15.000",
                "22 Oktober 2018"
        );

        listItemTransaksis.add( listItemDataTransaksi );

        ListItemTransaksi listItemDataTransaksi1 = new ListItemTransaksi(
                "#2928",
                "Rp. 900.000",
                "22 Oktober 2018"
        );

        listItemTransaksis.add( listItemDataTransaksi1 );

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
                                JSONObject transaksiobject = data.getJSONObject( i );

                                ListItemTransaksi listItemTransaksi = new ListItemTransaksi(
                                        transaksiobject.getString( "kd_transaksi"),
                                        transaksiobject.getString( "total_harga" ),
                                        transaksiobject.getString( "tgl_transaksi" )
                                );

                                listItemTransaksis.add( listItemTransaksi );
                            }
                        }
                        refreshDataTransaksi.setRefreshing( false );
                        progressBarDataTransaksi.setVisibility( View.GONE );
                        koneksiDataTransaksi.setVisibility( View.GONE);
                        setUpRecycleView();
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

        setUpRecycleView();
    }

    private void setUpRecycleView(){
        recyclerViewDataTransaksi.setHasFixedSize(true);
        recyclerViewDataTransaksi.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterDataTransaksi = new AdapterRecycleViewDataTransaksi( listItemTransaksis, getContext());
        recyclerViewDataTransaksi.setAdapter( adapterDataTransaksi );
        adapterDataTransaksi.notifyDataSetChanged();
    }
}
