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
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pratamatechnocraft.silaporanpenjualan.Adapter.AdapterRecycleViewDataBarang;
import com.pratamatechnocraft.silaporanpenjualan.Model.BaseUrlApiModel;
import com.pratamatechnocraft.silaporanpenjualan.Model.ListItemDataBarang;
import com.pratamatechnocraft.silaporanpenjualan.R;
import com.pratamatechnocraft.silaporanpenjualan.Service.SessionManager;
//import com.pratamatechnocraft.silaporanpenjualan.TambahSuratMasukActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataBarangFragment extends Fragment {

    private RecyclerView recyclerViewDataBarang;
    private RecyclerView.Adapter adapterDataBarang;
    LinearLayout noDataBarang, koneksiDataBarang;
    SwipeRefreshLayout refreshDataBarang;
    FloatingActionButton floatingActionButton1;
    ProgressBar progressBarDataBarang;
    Button cobaLagiDataBarang;
    SessionManager sessionManager;

    private List<ListItemDataBarang> listItemDataBarangs;

    BaseUrlApiModel baseUrlApiModel = new BaseUrlApiModel();
    private String baseUrl=baseUrlApiModel.getBaseURL();
    private static final String API_URL = "api/surat_masuk?api=suratmasukall";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.activity_data_barang_fragment, container, false);
        noDataBarang = view.findViewById( R.id.noDataBarang );
        refreshDataBarang = (SwipeRefreshLayout) view.findViewById(R.id.refreshDataBarang);
        floatingActionButton1 = view.findViewById( R.id.floatingActionButton );
        cobaLagiDataBarang = view.findViewById( R.id.cobaLagiBarang );
        koneksiDataBarang = view.findViewById( R.id.koneksiDataBarang );

        sessionManager = new SessionManager( getContext() );
        HashMap<String, String> user = sessionManager.getUserDetail();

        recyclerViewDataBarang = (RecyclerView) view.findViewById(R.id.recycleViewDataBarang);
        recyclerViewDataBarang.setHasFixedSize(true);
        recyclerViewDataBarang.setLayoutManager(new LinearLayoutManager(getContext()));

        listItemDataBarangs = new ArrayList<>();
        adapterDataBarang = new AdapterRecycleViewDataBarang( listItemDataBarangs, getContext());

        progressBarDataBarang = view.findViewById( R.id.progressBarDataBarang );

        loadSuratMasuk();

        refreshDataBarang.setOnRefreshListener( new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listItemDataBarangs.clear();
                adapterDataBarang.notifyDataSetChanged();
                loadSuratMasuk();
            }
        } );

        cobaLagiDataBarang.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                koneksiDataBarang.setVisibility( View.GONE );
                progressBarDataBarang.setVisibility( View.VISIBLE );
                loadSuratMasuk();
            }
        } );

        recyclerViewDataBarang.setAdapter( adapterDataBarang );

        floatingActionButton1.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent i = new Intent(getContext(), TambahSuratMasukActivity.class);
                getContext().startActivity(i);*/
            }
        } );

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Data Barang");
    }

    private void loadSuratMasuk(){

        for (int i=0;i<10;i++){
            ListItemDataBarang listItemDataBarang = new ListItemDataBarang(
                    "",
                    "Barang "+i,
                    ""+i,
                    ""+1000*i,
                    ""
            );

            listItemDataBarangs.add( listItemDataBarang );
            adapterDataBarang.notifyDataSetChanged();
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
                                JSONObject suratmasukobject = data.getJSONObject( i );

                                ListItemDataBarang listItemDataBarang = new ListItemDataBarang(
                                        suratmasukobject.getString( "id_surat_masuk"),
                                        suratmasukobject.getString( "asal_surat" ),
                                        suratmasukobject.getString( "perihal" ),
                                        suratmasukobject.getString( "tgl_arsip")
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
    }
}
