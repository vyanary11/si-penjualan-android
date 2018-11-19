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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.pratamatechnocraft.silaporanpenjualan.Adapter.AdapterRecycleViewDataBarang;
import com.pratamatechnocraft.silaporanpenjualan.FormBarangActivity;
import com.pratamatechnocraft.silaporanpenjualan.Model.BaseUrlApiModel;
import com.pratamatechnocraft.silaporanpenjualan.Model.ListItemDataBarang;
import com.pratamatechnocraft.silaporanpenjualan.R;
import com.pratamatechnocraft.silaporanpenjualan.Service.SessionManager;
//import com.pratamatechnocraft.silaporanpenjualan.TambahSuratMasukActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataBarangFragment extends Fragment {

    private RecyclerView recyclerViewDataBarang;
    private AdapterRecycleViewDataBarang adapterDataBarang;
    LinearLayout noDataBarang, koneksiDataBarang;
    SwipeRefreshLayout refreshDataBarang;
    FloatingActionButton floatingActionButton1;
    ProgressBar progressBarDataBarang;
    Button cobaLagiDataBarang;
    SessionManager sessionManager;
    private Boolean statusFragment = false;

    private List<ListItemDataBarang> listItemDataBarangs;

    BaseUrlApiModel baseUrlApiModel = new BaseUrlApiModel();
    private String baseUrl=baseUrlApiModel.getBaseURL();
    private static final String API_URL = "api/surat_masuk?api=suratmasukall";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_data_barang, container, false);
        noDataBarang = view.findViewById( R.id.noDataBarang );
        refreshDataBarang = (SwipeRefreshLayout) view.findViewById(R.id.refreshDataBarang);
        floatingActionButton1 = view.findViewById( R.id.floatingActionButton );
        cobaLagiDataBarang = view.findViewById( R.id.cobaLagiBarang );
        koneksiDataBarang = view.findViewById( R.id.koneksiDataBarang );
        progressBarDataBarang = view.findViewById( R.id.progressBarDataBarang );
        recyclerViewDataBarang = (RecyclerView) view.findViewById(R.id.recycleViewDataBarang);

        sessionManager = new SessionManager( getContext() );
        HashMap<String, String> user = sessionManager.getUserDetail();


        refreshDataBarang.setOnRefreshListener( new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listItemDataBarangs.clear();
                adapterDataBarang.notifyDataSetChanged();
                loadDataBarang();
            }
        } );

        cobaLagiDataBarang.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                koneksiDataBarang.setVisibility( View.GONE );
                progressBarDataBarang.setVisibility( View.VISIBLE );
                loadDataBarang();
            }
        } );


        floatingActionButton1.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), FormBarangActivity.class);
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
        getActivity().setTitle("Data Barang");
        setHasOptionsMenu( true );
        loadDataBarang();
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
                adapterDataBarang.getFilter().filter(s);
                return false;
            }
        } );
        
        searchView.setQueryHint("Search");

    }

    @Override
    public void onPause() {
        super.onPause();
        statusFragment=true;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (statusFragment) {
            loadDataBarang();
        }
    }

    private void loadDataBarang(){
        refreshDataBarang.setEnabled( true );
        listItemDataBarangs = new ArrayList<>();
        for (int i=0;i<10;i++){
            ListItemDataBarang listItemDataBarang = new ListItemDataBarang(
                    "",
                    "Barang "+i,
                    ""+i,
                    ""+1000*i,
                    ""
            );
            listItemDataBarangs.add( listItemDataBarang );
        }

        refreshDataBarang.setRefreshing( false );
        progressBarDataBarang.setVisibility( View.GONE );
        koneksiDataBarang.setVisibility( View.GONE);

        /*StringRequest stringRequest = new StringRequest( Request.Method.GET, baseUrl+API_URL,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getInt( "jml_data" )==0){
                            noDataBarang.setVisibility( View.VISIBLE );
                        }else{
                            noDataBarang.setVisibility( View.GONE );
                            JSONArray data = jsonObject.getJSONArray("data");
                            for (int i = 0; i<data.length(); i++){
                                JSONObject barangobject = data.getJSONObject( i );

                                ListItemDataBarang listItemDataBarang = new ListItemDataBarang(
                                        barangobject.getString( "kd_barang"),
                                        barangobject.getString( "nama_barang" ),
                                        barangobject.getString( "stok_barang" ),
                                        barangobject.getString( "harga_jual"),
                                        barangobject.getString( "gamabar_barang")
                                );

                                listItemDataBarangs.add( listItemDataBarang );
                                adapterDataBarang.notifyDataSetChanged();
                            }
                        }
                        refreshDataBarang.setRefreshing( false );
                        progressBarDataBarang.setVisibility( View.GONE );
                        koneksiDataBarang.setVisibility( View.GONE);
                    }catch (JSONException e){
                        e.printStackTrace();
                        refreshDataBarang.setRefreshing( false );
                        progressBarDataBarang.setVisibility( View.GONE );
                        noDataBarang.setVisibility( View.GONE );
                        listItemDataBarangs.clear();
                        koneksiDataBarang.setVisibility( View.VISIBLE );
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    refreshDataBarang.setRefreshing( false );
                    progressBarDataBarang.setVisibility( View.GONE );
                    noDataBarang.setVisibility( View.GONE );
                    listItemDataBarangs.clear();
                    koneksiDataBarang.setVisibility( View.VISIBLE );
                }
            }
        );

        RequestQueue requestQueue = Volley.newRequestQueue( getContext() );
        requestQueue.add( stringRequest );*/

        setUpRecycleView();
    }

    private void setUpRecycleView() {
        recyclerViewDataBarang.setHasFixedSize(true);
        recyclerViewDataBarang.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterDataBarang = new AdapterRecycleViewDataBarang( listItemDataBarangs, getContext(),0);
        recyclerViewDataBarang.setAdapter( adapterDataBarang );
        adapterDataBarang.notifyDataSetChanged();
    }
}
