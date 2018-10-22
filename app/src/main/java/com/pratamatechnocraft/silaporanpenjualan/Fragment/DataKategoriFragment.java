package com.pratamatechnocraft.silaporanpenjualan.Fragment;

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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.pratamatechnocraft.silaporanpenjualan.Adapter.AdapterRecycleViewDataKategori;
import com.pratamatechnocraft.silaporanpenjualan.Model.BaseUrlApiModel;
import com.pratamatechnocraft.silaporanpenjualan.Model.ListItemDataKategori;
import com.pratamatechnocraft.silaporanpenjualan.R;
import com.pratamatechnocraft.silaporanpenjualan.Service.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//import com.pratamatechnocraft.silaporanpenjualan.TambahSuratMasukActivity;

public class DataKategoriFragment extends Fragment {

    private RecyclerView recyclerViewDataKategori;
    private RecyclerView.Adapter adapterDataKategori;
    LinearLayout noDataKategori, koneksiDataKategori;
    SwipeRefreshLayout refreshDataKategori;
    ImageButton imageButtonTambahKategori;
    ProgressBar progressBarDataKategori;
    Button cobaLagiDataKategori;
    SessionManager sessionManager;

    private List<ListItemDataKategori> listItemDataKategoris;

    BaseUrlApiModel baseUrlApiModel = new BaseUrlApiModel();
    private String baseUrl=baseUrlApiModel.getBaseURL();
    private static final String API_URL = "api/surat_masuk?api=suratmasukall";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.activity_data_kategori_fragment, container, false);
        noDataKategori = view.findViewById( R.id.noDataKategori );
        refreshDataKategori = (SwipeRefreshLayout) view.findViewById(R.id.refreshDataKategori);
        imageButtonTambahKategori = view.findViewById( R.id.imageButtonTambahKategori );
        cobaLagiDataKategori = view.findViewById( R.id.cobaLagiMasuk );
        koneksiDataKategori = view.findViewById( R.id.koneksiDataKategori );

        sessionManager = new SessionManager( getContext() );
        HashMap<String, String> kategori = sessionManager.getUserDetail();

        recyclerViewDataKategori = (RecyclerView) view.findViewById(R.id.recycleViewDataKategori);
        recyclerViewDataKategori.setHasFixedSize(true);
        recyclerViewDataKategori.setLayoutManager(new LinearLayoutManager(getContext()));

        listItemDataKategoris = new ArrayList<>();
        adapterDataKategori = new AdapterRecycleViewDataKategori( listItemDataKategoris, getContext());

        progressBarDataKategori = view.findViewById( R.id.progressBarDataKategori );

        loadSuratMasuk();

        refreshDataKategori.setOnRefreshListener( new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listItemDataKategoris.clear();
                adapterDataKategori.notifyDataSetChanged();
                loadSuratMasuk();
            }
        } );

        cobaLagiDataKategori.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                koneksiDataKategori.setVisibility( View.GONE );
                progressBarDataKategori.setVisibility( View.VISIBLE );
                loadSuratMasuk();
            }
        } );

        recyclerViewDataKategori.setAdapter( adapterDataKategori );

        imageButtonTambahKategori.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        } );

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Data Kategori");
    }

    private void loadSuratMasuk(){


        ListItemDataKategori listItemDataKategori = new ListItemDataKategori(
                "",
                "Makanan"
        );

        listItemDataKategoris.add( listItemDataKategori );
        adapterDataKategori.notifyDataSetChanged();

        ListItemDataKategori listItemDataKategori1 = new ListItemDataKategori(
                "",
                "Minuman"
        );

        listItemDataKategoris.add( listItemDataKategori1 );
        adapterDataKategori.notifyDataSetChanged();

        refreshDataKategori.setRefreshing( false );
        progressBarDataKategori.setVisibility( View.GONE );
        koneksiDataKategori.setVisibility( View.GONE);

        /*StringRequest stringRequest = new StringRequest( Request.Method.GET, baseUrl+API_URL,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getInt( "jml_data" )==0){
                            noDataKategori.setVisibility( View.VISIBLE );
                        }else{
                            noDataKategori.setVisibility( View.GONE );
                            JSONArray data = jsonObject.getJSONArray("data");
                            for (int i = 0; i<data.length(); i++){
                                JSONObject suratmasukobject = data.getJSONObject( i );

                                ListItemDataKategori listItemDataKategori = new ListItemDataKategori(
                                        suratmasukobject.getString( "id_surat_masuk"),
                                        suratmasukobject.getString( "asal_surat" ),
                                        suratmasukobject.getString( "perihal" ),
                                        suratmasukobject.getString( "tgl_arsip")
                                );

                                listItemDataKategoris.add( listItemDataKategori );
                                adapterDataKategori.notifyDataSetChanged();
                            }
                        }
                        refreshDataKategori.setRefreshing( false );
                        progressBarDataKategori.setVisibility( View.GONE );
                        koneksiDataKategori.setVisibility( View.GONE);
                    }catch (JSONException e){
                        e.printStackTrace();
                        refreshDataKategori.setRefreshing( false );
                        progressBarDataKategori.setVisibility( View.GONE );
                        noDataKategori.setVisibility( View.GONE );
                        listItemDataKategoris.clear();
                        koneksiDataKategori.setVisibility( View.VISIBLE );
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    refreshDataKategori.setRefreshing( false );
                    progressBarDataKategori.setVisibility( View.GONE );
                    noDataKategori.setVisibility( View.GONE );
                    listItemDataKategoris.clear();
                    koneksiDataKategori.setVisibility( View.VISIBLE );
                }
            }
        );

        RequestQueue requestQueue = Volley.newRequestQueue( getContext() );
        requestQueue.add( stringRequest );*/
    }
}
