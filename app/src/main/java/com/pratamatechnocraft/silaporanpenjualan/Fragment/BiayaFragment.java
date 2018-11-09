package com.pratamatechnocraft.silaporanpenjualan.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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

import com.pratamatechnocraft.silaporanpenjualan.Adapter.AdapterRecycleViewDataBiaya;
import com.pratamatechnocraft.silaporanpenjualan.Model.BaseUrlApiModel;
import com.pratamatechnocraft.silaporanpenjualan.Model.ListItemBiaya;
import com.pratamatechnocraft.silaporanpenjualan.R;
import com.pratamatechnocraft.silaporanpenjualan.Service.SessionManager;
import com.pratamatechnocraft.silaporanpenjualan.TambahBiayaActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//import com.pratamatechnocraft.silaporanpenjualan.TambahSuratMasukActivity;

public class BiayaFragment extends Fragment {

    private RecyclerView recyclerViewDataBiaya;
    private RecyclerView.Adapter adapterDataBiaya;
    LinearLayout noDataBiaya, koneksiDataBiaya;
    SwipeRefreshLayout refreshDataBiaya;
    FloatingActionButton fabTambahDataBiaya;
    ProgressBar progressBarDataBiaya;
    Button cobaLagiDataBiaya;
    SessionManager sessionManager;

    private List<ListItemBiaya> listItemBiayas;

    BaseUrlApiModel baseUrlApiModel = new BaseUrlApiModel();
    private String baseUrl=baseUrlApiModel.getBaseURL();
    private static final String API_URL = "api/surat_masuk?api=suratmasukall";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_biaya, container, false);
        noDataBiaya = view.findViewById( R.id.noDataBiaya );
        refreshDataBiaya = (SwipeRefreshLayout) view.findViewById(R.id.refreshDataBiaya);
        fabTambahDataBiaya = view.findViewById( R.id.fabTambahDataBiaya );
        cobaLagiDataBiaya = view.findViewById( R.id.cobaLagiBiaya );
        koneksiDataBiaya = view.findViewById( R.id.koneksiDataBiaya );

        sessionManager = new SessionManager( getContext() );
        HashMap<String, String> biaya = sessionManager.getUserDetail();

        recyclerViewDataBiaya = (RecyclerView) view.findViewById(R.id.recycleViewDataBiaya);
        recyclerViewDataBiaya.setHasFixedSize(true);
        recyclerViewDataBiaya.setLayoutManager(new LinearLayoutManager(getContext()));

        listItemBiayas = new ArrayList<>();
        adapterDataBiaya = new AdapterRecycleViewDataBiaya( listItemBiayas, getContext());

        progressBarDataBiaya = view.findViewById( R.id.progressBarDataBiaya );

        loadBiaya();

        refreshDataBiaya.setOnRefreshListener( new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listItemBiayas.clear();
                adapterDataBiaya.notifyDataSetChanged();
                loadBiaya();
            }
        } );

        cobaLagiDataBiaya.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                koneksiDataBiaya.setVisibility( View.GONE );
                progressBarDataBiaya.setVisibility( View.VISIBLE );
                loadBiaya();
            }
        } );

        recyclerViewDataBiaya.setAdapter( adapterDataBiaya );

        fabTambahDataBiaya.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), TambahBiayaActivity.class);
                i.putExtra( "type","tambah" );
                getContext().startActivity(i);
            }
        } );

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Data Biaya");
    }

    private void loadBiaya(){


        ListItemBiaya listItemDataBiaya = new ListItemBiaya(
                "",
                "Uang Makan Pekerja",
                "Rp. 15.000",
                "22 Oktober 2018"
        );

        listItemBiayas.add( listItemDataBiaya );
        adapterDataBiaya.notifyDataSetChanged();

        ListItemBiaya listItemDataBiaya1 = new ListItemBiaya(
                "",
                "Gaji Pekerja",
                "Rp. 900.000",
                "22 Oktober 2018"
        );

        listItemBiayas.add( listItemDataBiaya1 );
        adapterDataBiaya.notifyDataSetChanged();

        refreshDataBiaya.setRefreshing( false );
        progressBarDataBiaya.setVisibility( View.GONE );
        koneksiDataBiaya.setVisibility( View.GONE);

        /*StringRequest stringRequest = new StringRequest( Request.Method.GET, baseUrl+API_URL,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getInt( "jml_data" )==0){
                            noDataBiaya.setVisibility( View.VISIBLE );
                        }else{
                            noDataBiaya.setVisibility( View.GONE );
                            JSONArray data = jsonObject.getJSONArray("data");
                            for (int i = 0; i<data.length(); i++){
                                JSONObject suratmasukobject = data.getJSONObject( i );

                                ListItemDataBiaya listItemDataBiaya = new ListItemDataBiaya(
                                        suratmasukobject.getString( "id_surat_masuk"),
                                        suratmasukobject.getString( "asal_surat" ),
                                        suratmasukobject.getString( "perihal" ),
                                        suratmasukobject.getString( "tgl_arsip")
                                );

                                listItemBiayas.add( listItemDataBiaya );
                                adapterDataBiaya.notifyDataSetChanged();
                            }
                        }
                        refreshDataBiaya.setRefreshing( false );
                        progressBarDataBiaya.setVisibility( View.GONE );
                        koneksiDataBiaya.setVisibility( View.GONE);
                    }catch (JSONException e){
                        e.printStackTrace();
                        refreshDataBiaya.setRefreshing( false );
                        progressBarDataBiaya.setVisibility( View.GONE );
                        noDataBiaya.setVisibility( View.GONE );
                        listItemBiayas.clear();
                        koneksiDataBiaya.setVisibility( View.VISIBLE );
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    refreshDataBiaya.setRefreshing( false );
                    progressBarDataBiaya.setVisibility( View.GONE );
                    noDataBiaya.setVisibility( View.GONE );
                    listItemBiayas.clear();
                    koneksiDataBiaya.setVisibility( View.VISIBLE );
                }
            }
        );

        RequestQueue requestQueue = Volley.newRequestQueue( getContext() );
        requestQueue.add( stringRequest );*/
    }
}
