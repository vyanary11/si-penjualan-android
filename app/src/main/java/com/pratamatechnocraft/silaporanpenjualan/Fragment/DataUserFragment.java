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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pratamatechnocraft.silaporanpenjualan.Adapter.AdapterRecycleViewDataUser;
import com.pratamatechnocraft.silaporanpenjualan.Model.BaseUrlApiModel;
import com.pratamatechnocraft.silaporanpenjualan.Model.ListItemDataUser;
import com.pratamatechnocraft.silaporanpenjualan.R;
import com.pratamatechnocraft.silaporanpenjualan.Service.SessionManager;
import com.pratamatechnocraft.silaporanpenjualan.FormUserActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//import com.pratamatechnocraft.silaporanpenjualan.TambahSuratMasukActivity;

public class DataUserFragment extends Fragment {

    private RecyclerView recyclerViewDataUser;
    private RecyclerView.Adapter adapterDataUser;
    LinearLayout noDataUser, koneksiDataUser;
    SwipeRefreshLayout refreshDataUser;
    FloatingActionButton fabTambahDataUser;
    ProgressBar progressBarDataUser;
    Button cobaLagiDataUser;
    SessionManager sessionManager;

    private List<ListItemDataUser> listItemDataUsers;

    BaseUrlApiModel baseUrlApiModel = new BaseUrlApiModel();
    private String baseUrl=baseUrlApiModel.getBaseURL();
    private static final String API_URL = "api/user?api=all";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_data_user, container, false);
        noDataUser = view.findViewById( R.id.noDataUser );
        refreshDataUser = (SwipeRefreshLayout) view.findViewById(R.id.refreshDataUser);
        fabTambahDataUser = view.findViewById( R.id.fabTambahDataUser );
        cobaLagiDataUser = view.findViewById( R.id.cobaLagiUser );
        koneksiDataUser = view.findViewById( R.id.koneksiDataUser );

        sessionManager = new SessionManager( getContext() );
        HashMap<String, String> user = sessionManager.getUserDetail();

        recyclerViewDataUser = (RecyclerView) view.findViewById(R.id.recycleViewDataUser);
        recyclerViewDataUser.setHasFixedSize(true);
        recyclerViewDataUser.setLayoutManager(new LinearLayoutManager(getContext()));

        listItemDataUsers = new ArrayList<>();
        adapterDataUser = new AdapterRecycleViewDataUser( listItemDataUsers, getContext());

        progressBarDataUser = view.findViewById( R.id.progressBarDataUser );

        loadSuratMasuk();

        refreshDataUser.setOnRefreshListener( new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listItemDataUsers.clear();
                adapterDataUser.notifyDataSetChanged();
                loadSuratMasuk();
            }
        } );

        cobaLagiDataUser.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                koneksiDataUser.setVisibility( View.GONE );
                progressBarDataUser.setVisibility( View.VISIBLE );
                loadSuratMasuk();
            }
        } );

        recyclerViewDataUser.setAdapter( adapterDataUser );

        fabTambahDataUser.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), FormUserActivity.class);
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
        getActivity().setTitle("Data User");
    }

    private void loadSuratMasuk(){


            ListItemDataUser listItemDataUser = new ListItemDataUser(
                    "",
                    "Owner Kantin",
                    "081543xxxxxx",
                    "Owner",
                    ""
            );

            listItemDataUsers.add( listItemDataUser );
            adapterDataUser.notifyDataSetChanged();

            ListItemDataUser listItemDataUser1 = new ListItemDataUser(
                    "",
                    "Kasir Kantin",
                    "081465xxxxxx",
                    "Kasir",
                    ""
            );

            listItemDataUsers.add( listItemDataUser1 );
            adapterDataUser.notifyDataSetChanged();

        refreshDataUser.setRefreshing( false );
        progressBarDataUser.setVisibility( View.GONE );
        koneksiDataUser.setVisibility( View.GONE);

        /*StringRequest stringRequest = new StringRequest( Request.Method.GET, baseUrl+API_URL,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getInt( "jml_data" )==0){
                            noDataUser.setVisibility( View.VISIBLE );
                        }else{
                            noDataUser.setVisibility( View.GONE );
                            JSONArray data = jsonObject.getJSONArray("data");
                            for (int i = 0; i<data.length(); i++){
                                JSONObject userobject = data.getJSONObject( i );

                                ListItemDataUser listItemDataUser = new ListItemDataUser(
                                        userobject.getString( "kd_user"),
                                        userobject.getString( "nama_depan" )+" "+userobject.getString( "nama_belakang" ),
                                        userobject.getString( "no_telp" ),
                                        userobject.getString( "level_user"),
                                        userobject.getString( "foto_user")
                                );

                                listItemDataUsers.add( listItemDataUser );
                                adapterDataUser.notifyDataSetChanged();
                            }
                        }
                        refreshDataUser.setRefreshing( false );
                        progressBarDataUser.setVisibility( View.GONE );
                        koneksiDataUser.setVisibility( View.GONE);
                    }catch (JSONException e){
                        e.printStackTrace();
                        refreshDataUser.setRefreshing( false );
                        progressBarDataUser.setVisibility( View.GONE );
                        noDataUser.setVisibility( View.GONE );
                        listItemDataUsers.clear();
                        koneksiDataUser.setVisibility( View.VISIBLE );
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    refreshDataUser.setRefreshing( false );
                    progressBarDataUser.setVisibility( View.GONE );
                    noDataUser.setVisibility( View.GONE );
                    listItemDataUsers.clear();
                    koneksiDataUser.setVisibility( View.VISIBLE );
                }
            }
        );

        RequestQueue requestQueue = Volley.newRequestQueue( getContext() );
        requestQueue.add( stringRequest );*/
    }
}
