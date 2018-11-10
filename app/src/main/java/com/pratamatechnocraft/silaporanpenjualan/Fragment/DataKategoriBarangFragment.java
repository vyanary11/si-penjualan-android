package com.pratamatechnocraft.silaporanpenjualan.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import com.pratamatechnocraft.silaporanpenjualan.Adapter.AdapterRecycleViewDataKategoriBarang;
import com.pratamatechnocraft.silaporanpenjualan.Model.BaseUrlApiModel;
import com.pratamatechnocraft.silaporanpenjualan.Model.ListItemDataKategoriBarang;
import com.pratamatechnocraft.silaporanpenjualan.R;
import com.pratamatechnocraft.silaporanpenjualan.Service.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//import com.pratamatechnocraft.silaporanpenjualan.TambahSuratMasukActivity;

public class DataKategoriBarangFragment extends Fragment {

    private RecyclerView recyclerViewDataKategoriBarang;
    private AdapterRecycleViewDataKategoriBarang adapterDataKategoriBarang;
    LinearLayout noDataKategoriBarang, koneksiDataKategoriBarang;
    SwipeRefreshLayout refreshDataKategoriBarang;
    ImageButton imageButtonTambahKategoriBarang;
    ProgressBar progressBarDataKategoriBarang;
    Button cobaLagiDataKategoriBarang;
    SessionManager sessionManager;

    private List<ListItemDataKategoriBarang> listItemDataKategorisBarangs;

    BaseUrlApiModel baseUrlApiModel = new BaseUrlApiModel();
    private String baseUrl=baseUrlApiModel.getBaseURL();
    private static final String API_URL = "api/surat_masuk?api=suratmasukall";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_data_kategori_barang, container, false);
        noDataKategoriBarang = view.findViewById( R.id.noDataKategoriBarang );
        refreshDataKategoriBarang = (SwipeRefreshLayout) view.findViewById(R.id.refreshDataKategoriBarang);
        imageButtonTambahKategoriBarang = view.findViewById( R.id.imageButtonTambahKategoriBarang );
        cobaLagiDataKategoriBarang = view.findViewById( R.id.cobaLagiKategoriBarang );
        koneksiDataKategoriBarang = view.findViewById( R.id.koneksiDataKategoriBarang );
        progressBarDataKategoriBarang = view.findViewById( R.id.progressBarDataKategoriBarang );
        recyclerViewDataKategoriBarang = (RecyclerView) view.findViewById(R.id.recycleViewDataKategoriBarang);

        sessionManager = new SessionManager( getContext() );
        HashMap<String, String> kategori = sessionManager.getUserDetail();

        refreshDataKategoriBarang.setOnRefreshListener( new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listItemDataKategorisBarangs.clear();
                adapterDataKategoriBarang.notifyDataSetChanged();
                loadKategoriBarang();
            }
        } );

        cobaLagiDataKategoriBarang.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                koneksiDataKategoriBarang.setVisibility( View.GONE );
                progressBarDataKategoriBarang.setVisibility( View.VISIBLE );
                loadKategoriBarang();
            }
        } );

        imageButtonTambahKategoriBarang.setOnClickListener( new View.OnClickListener() {
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
        loadKategoriBarang();
    }

    private void loadKategoriBarang(){
        listItemDataKategorisBarangs = new ArrayList<>();
        ListItemDataKategoriBarang listItemDataKategoriBarang = new ListItemDataKategoriBarang(
                "",
                "Makanan"
        );

        listItemDataKategorisBarangs.add( listItemDataKategoriBarang );

        ListItemDataKategoriBarang listItemDataKategoriBarang1 = new ListItemDataKategoriBarang(
                "",
                "Minuman"
        );

        listItemDataKategorisBarangs.add( listItemDataKategoriBarang1 );

        refreshDataKategoriBarang.setRefreshing( false );
        progressBarDataKategoriBarang.setVisibility( View.GONE );
        koneksiDataKategoriBarang.setVisibility( View.GONE);

        setUpRecycleView();
    }

    private void setUpRecycleView(){
        recyclerViewDataKategoriBarang.setHasFixedSize(true);
        recyclerViewDataKategoriBarang.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterDataKategoriBarang = new AdapterRecycleViewDataKategoriBarang( listItemDataKategorisBarangs, getContext());
        recyclerViewDataKategoriBarang.setAdapter( adapterDataKategoriBarang );
        adapterDataKategoriBarang.notifyDataSetChanged();
    }
}
