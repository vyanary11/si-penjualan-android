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
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pratamatechnocraft.silaporanpenjualan.Adapter.AdapterRecycleViewDataBiaya;
import com.pratamatechnocraft.silaporanpenjualan.Model.BaseUrlApiModel;
import com.pratamatechnocraft.silaporanpenjualan.Model.ListItemBiaya;
import com.pratamatechnocraft.silaporanpenjualan.R;
import com.pratamatechnocraft.silaporanpenjualan.Service.SessionManager;
import com.pratamatechnocraft.silaporanpenjualan.FormBiayaActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BiayaFragment extends Fragment {

    private RecyclerView recyclerViewDataBiaya;
    private AdapterRecycleViewDataBiaya adapterDataBiaya;
    LinearLayout noDataBiaya, koneksiDataBiaya;
    SwipeRefreshLayout refreshDataBiaya;
    FloatingActionButton fabTambahDataBiaya;
    ProgressBar progressBarDataBiaya;
    Button cobaLagiDataBiaya;
    SessionManager sessionManager;
    private Boolean statusFragment = false;

    private List<ListItemBiaya> listItemBiayas;

    BaseUrlApiModel baseUrlApiModel = new BaseUrlApiModel();
    private String baseUrl=baseUrlApiModel.getBaseURL();
    private static final String API_URL = "api/biaya?api=biayaall";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_biaya, container, false);
        noDataBiaya = view.findViewById( R.id.noDataBiaya );
        refreshDataBiaya = (SwipeRefreshLayout) view.findViewById(R.id.refreshDataBiaya);
        fabTambahDataBiaya = view.findViewById( R.id.fabTambahDataBiaya );
        cobaLagiDataBiaya = view.findViewById( R.id.cobaLagiBiaya );
        koneksiDataBiaya = view.findViewById( R.id.koneksiDataBiaya );
        progressBarDataBiaya = view.findViewById( R.id.progressBarDataBiaya );
        recyclerViewDataBiaya = (RecyclerView) view.findViewById(R.id.recycleViewDataBiaya);

        sessionManager = new SessionManager( getContext() );
        HashMap<String, String> biaya = sessionManager.getUserDetail();

        refreshDataBiaya.setOnRefreshListener( new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapterDataBiaya = new AdapterRecycleViewDataBiaya( listItemBiayas, getContext());
                recyclerViewDataBiaya.setAdapter( adapterDataBiaya );
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

        fabTambahDataBiaya.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), FormBiayaActivity.class);
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
        getActivity().setTitle("Biaya");
        setHasOptionsMenu( true );
        loadBiaya();
    }

    @Override
    public void onPause() {
        super.onPause();
        statusFragment = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (statusFragment) {
            loadBiaya();
        }
    }

    private void loadBiaya(){
        listItemBiayas = new ArrayList<>();

        StringRequest stringRequest = new StringRequest( Request.Method.GET, baseUrl+API_URL,
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
                                JSONObject biayaobject = data.getJSONObject( i );

                                ListItemBiaya listItemBiaya = new ListItemBiaya(
                                        biayaobject.getString( "kd_biaya"),
                                        biayaobject.getString( "nama_biaya" ),
                                        "Rp. "+biayaobject.getString( "jumlah_biaya" ),
                                        biayaobject.getString( "tgl_biaya" )
                                );

                                listItemBiayas.add( listItemBiaya );
                            }
                        }
                        refreshDataBiaya.setRefreshing( false );
                        progressBarDataBiaya.setVisibility( View.GONE );
                        koneksiDataBiaya.setVisibility( View.GONE);
                        setUpRecycleView();
                    }catch (JSONException e){
                        e.printStackTrace();
                        Log.d( "TAG", e.toString() );
                        refreshDataBiaya.setRefreshing( false );
                        progressBarDataBiaya.setVisibility( View.GONE );
                        noDataBiaya.setVisibility( View.GONE );
                        adapterDataBiaya = new AdapterRecycleViewDataBiaya( listItemBiayas, getContext());
                        recyclerViewDataBiaya.setAdapter( adapterDataBiaya );
                        listItemBiayas.clear();
                        adapterDataBiaya.notifyDataSetChanged();
                        koneksiDataBiaya.setVisibility( View.VISIBLE );
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    Log.d( "TAG", error.toString() );
                    refreshDataBiaya.setRefreshing( false );
                    progressBarDataBiaya.setVisibility( View.GONE );
                    noDataBiaya.setVisibility( View.GONE );
                    adapterDataBiaya = new AdapterRecycleViewDataBiaya( listItemBiayas, getContext());
                    recyclerViewDataBiaya.setAdapter( adapterDataBiaya );
                    listItemBiayas.clear();
                    adapterDataBiaya.notifyDataSetChanged();
                    koneksiDataBiaya.setVisibility( View.VISIBLE );
                }
            }
        );

        RequestQueue requestQueue = Volley.newRequestQueue( getContext() );
        requestQueue.add( stringRequest );
    }

    private void setUpRecycleView() {
        recyclerViewDataBiaya.setHasFixedSize(true);
        recyclerViewDataBiaya.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterDataBiaya = new AdapterRecycleViewDataBiaya( listItemBiayas, getContext());
        recyclerViewDataBiaya.setAdapter( adapterDataBiaya );
        adapterDataBiaya.notifyDataSetChanged();
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
                adapterDataBiaya.getFilter().filter(s);
                return false;
            }
        } );
        searchView.setQueryHint("Cari: Nama Biaya");

    }
}
