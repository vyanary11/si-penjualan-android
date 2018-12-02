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
import android.support.v7.widget.SearchView;

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

public class DataUserFragment extends Fragment {

    private RecyclerView recyclerViewDataUser;
    private AdapterRecycleViewDataUser adapterDataUser;
    LinearLayout noDataUser, koneksiDataUser;
    SwipeRefreshLayout refreshDataUser;
    FloatingActionButton fabTambahDataUser;
    ProgressBar progressBarDataUser;
    Button cobaLagiDataUser;
    SessionManager sessionManager;
    HashMap<String, String> user;

    private Boolean statusFragment = false;

    private List<ListItemDataUser> listItemDataUsers;

    BaseUrlApiModel baseUrlApiModel = new BaseUrlApiModel();
    private String baseUrl=baseUrlApiModel.getBaseURL();
    private static final String API_URL = "api/user?api=userall";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_data_user, container, false);
        noDataUser = view.findViewById( R.id.noDataUser );
        refreshDataUser = (SwipeRefreshLayout) view.findViewById(R.id.refreshDataUser);
        fabTambahDataUser = view.findViewById( R.id.fabTambahDataUser );
        cobaLagiDataUser = view.findViewById( R.id.cobaLagiUser );
        koneksiDataUser = view.findViewById( R.id.koneksiDataUser );
        progressBarDataUser = view.findViewById( R.id.progressBarDataUser );
        recyclerViewDataUser = (RecyclerView) view.findViewById(R.id.recycleViewDataUser);

        sessionManager = new SessionManager( getContext() );
        user = sessionManager.getUserDetail();

        refreshDataUser.setOnRefreshListener( new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listItemDataUsers.clear();
                adapterDataUser.notifyDataSetChanged();
                loadDataUser();
            }
        } );

        cobaLagiDataUser.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                koneksiDataUser.setVisibility( View.GONE );
                progressBarDataUser.setVisibility( View.VISIBLE );
                loadDataUser();
            }
        } );

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
        setHasOptionsMenu( true );
        loadDataUser();
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
                adapterDataUser.getFilter().filter(s);
                return false;
            }
        } );
        searchView.setQueryHint("Cari: Nama, Jenis User");

    }

    @Override
    public void onPause() {
        super.onPause();
        statusFragment=true;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (statusFragment){
            loadDataUser();
        }
    }

    private void loadDataUser(){
        listItemDataUsers = new ArrayList<>();

        StringRequest stringRequest = new StringRequest( Request.Method.GET, baseUrl+API_URL+"&kd_user_login="+user.get( sessionManager.KD_USER ),
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
                                String level_user;
                                if (userobject.getString( "level_user" ).equals( "0" )){
                                    level_user="Owner";
                                }else{
                                    level_user="Kasir";
                                }

                                ListItemDataUser listItemDataUser = new ListItemDataUser(
                                        userobject.getString( "kd_user"),
                                        userobject.getString( "nama_depan" )+" "+userobject.getString( "nama_belakang" ),
                                        userobject.getString( "no_telp" ),
                                        level_user,
                                        userobject.getString( "foto")
                                );

                                listItemDataUsers.add( listItemDataUser );
                                //adapterDataUser.notifyDataSetChanged();
                            }
                        }
                        refreshDataUser.setRefreshing( false );
                        progressBarDataUser.setVisibility( View.GONE );
                        koneksiDataUser.setVisibility( View.GONE);
                        setUpRecycleView();
                    }catch (JSONException e){
                        e.printStackTrace();
                        Log.d( "TAG", e.toString() );
                        refreshDataUser.setRefreshing( false );
                        progressBarDataUser.setVisibility( View.GONE );
                        noDataUser.setVisibility( View.GONE );
                        setUpRecycleView();
                        listItemDataUsers.clear();
                        adapterDataUser.notifyDataSetChanged();
                        koneksiDataUser.setVisibility( View.VISIBLE );
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    Log.d( "TAG", error.toString() );
                    refreshDataUser.setRefreshing( false );
                    progressBarDataUser.setVisibility( View.GONE );
                    noDataUser.setVisibility( View.GONE );
                    setUpRecycleView();
                    listItemDataUsers.clear();
                    adapterDataUser.notifyDataSetChanged();
                    koneksiDataUser.setVisibility( View.VISIBLE );
                }
            }
        );

        RequestQueue requestQueue = Volley.newRequestQueue( getContext() );
        requestQueue.add( stringRequest );
    }

    private void setUpRecycleView(){
        recyclerViewDataUser.setHasFixedSize(true);
        recyclerViewDataUser.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterDataUser = new AdapterRecycleViewDataUser( listItemDataUsers, getContext());
        recyclerViewDataUser.setAdapter( adapterDataUser );
        adapterDataUser.notifyDataSetChanged();
    }
}
