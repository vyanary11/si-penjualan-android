package com.pratamatechnocraft.silaporanpenjualan;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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
import com.pratamatechnocraft.silaporanpenjualan.Service.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BarangTransaksiActivity extends AppCompatActivity {

    private RecyclerView recyclerViewBarangTransaksi;
    private AdapterRecycleViewDataBarang adapterRecycleViewBarangTransaksi;
    LinearLayout noBarangTransaksi, koneksiBarangTransaksi;
    SwipeRefreshLayout refreshBarangTransaksi;
    FloatingActionButton floatingActionButton1;
    ProgressBar progressBarBarangTransaksi;
    Button cobaLagiBarangTransaksi;
    SessionManager sessionManager;
    private Intent intent;

    private List<ListItemDataBarang> listItemDataBarangs;

    BaseUrlApiModel baseUrlApiModel = new BaseUrlApiModel();
    private String baseUrl=baseUrlApiModel.getBaseURL();
    private static final String API_URL = "api/barang?api=barangall";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_barang_transaksi );
        intent=getIntent();

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbarbarangtransaksi);
        setSupportActionBar(toolbar);
        this.setTitle("Barang");
        toolbar.setSubtitleTextColor( ContextCompat.getColor(this, R.color.colorIcons) );
        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.ic_close_white_24dp);
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.colorIcons), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        noBarangTransaksi = findViewById( R.id.noBarangTransaksi );
        refreshBarangTransaksi = (SwipeRefreshLayout) findViewById(R.id.refreshBarangTransaksi);
        floatingActionButton1 = findViewById( R.id.floatingActionButton );
        cobaLagiBarangTransaksi = findViewById( R.id.cobaLagiBarang );
        koneksiBarangTransaksi = findViewById( R.id.koneksiBarangTransaksi );
        progressBarBarangTransaksi = findViewById( R.id.progressBarBarangTransaksi );
        recyclerViewBarangTransaksi = (RecyclerView) findViewById(R.id.recycleViewBarangTransaksi);

        sessionManager = new SessionManager( this );
        HashMap<String, String> user = sessionManager.getUserDetail();

        loadBarangTransaksi();

        refreshBarangTransaksi.setOnRefreshListener( new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listItemDataBarangs.clear();
                loadBarangTransaksi();
            }
        } );

        cobaLagiBarangTransaksi.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                koneksiBarangTransaksi.setVisibility( View.GONE );
                progressBarBarangTransaksi.setVisibility( View.VISIBLE );
                loadBarangTransaksi();
            }
        } );


        floatingActionButton1.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent i = new Intent(BarangTransaksiActivity.this, FormBarangActivity.class);
                i.putExtra( "type","tambah" );
                i.putExtra( "typedua","keranjang" );
                if (intent.getStringExtra( "type" ).equals( "0" )){
                    i.putExtra( "typetiga","penjualan" );
                }else{
                    i.putExtra( "typetiga","pembelian" );
                }
                startActivity(i);
            }
        } );
    }

    private void loadBarangTransaksi(){
        refreshBarangTransaksi.setEnabled( true );
        listItemDataBarangs = new ArrayList<>();
        StringRequest stringRequest = new StringRequest( Request.Method.GET, baseUrl+API_URL,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getInt( "jml_data" )==0){
                            noBarangTransaksi.setVisibility( View.VISIBLE );
                        }else{
                            noBarangTransaksi.setVisibility( View.GONE );
                            JSONArray data = jsonObject.getJSONArray("data");
                            for (int i = 0; i<data.length(); i++){
                                JSONObject barangobject = data.getJSONObject( i );

                                if(intent.getStringExtra( "type" ).equals( "0" )){
                                    ListItemDataBarang itemDataBarang = new ListItemDataBarang(
                                            barangobject.getString( "kd_barang"),
                                            barangobject.getString( "nama_barang" ),
                                            barangobject.getString( "stok" ),
                                            barangobject.getString( "harga_jual"),
                                            barangobject.getString( "gambar_barang")
                                    );

                                    listItemDataBarangs.add( itemDataBarang );
                                }else{
                                    ListItemDataBarang itemDataBarang = new ListItemDataBarang(
                                            barangobject.getString( "kd_barang"),
                                            barangobject.getString( "nama_barang" ),
                                            barangobject.getString( "stok" ),
                                            barangobject.getString( "harga_beli"),
                                            barangobject.getString( "gambar_barang")
                                    );
                                    listItemDataBarangs.add( itemDataBarang );
                                }
                            }
                        }
                        refreshBarangTransaksi.setRefreshing( false );
                        progressBarBarangTransaksi.setVisibility( View.GONE );
                        koneksiBarangTransaksi.setVisibility( View.GONE);
                        setUpRecycleView();
                    }catch (JSONException e){
                        e.printStackTrace();
                        refreshBarangTransaksi.setRefreshing( false );
                        progressBarBarangTransaksi.setVisibility( View.GONE );
                        noBarangTransaksi.setVisibility( View.GONE );
                        setUpRecycleView();
                        listItemDataBarangs.clear();
                        adapterRecycleViewBarangTransaksi.notifyDataSetChanged();
                        koneksiBarangTransaksi.setVisibility( View.VISIBLE );
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    refreshBarangTransaksi.setRefreshing( false );
                    progressBarBarangTransaksi.setVisibility( View.GONE );
                    noBarangTransaksi.setVisibility( View.GONE );
                    setUpRecycleView();
                    listItemDataBarangs.clear();
                    adapterRecycleViewBarangTransaksi.notifyDataSetChanged();
                    koneksiBarangTransaksi.setVisibility( View.VISIBLE );
                }
            }
        );

        RequestQueue requestQueue = Volley.newRequestQueue( this);
        requestQueue.add( stringRequest );
    }

    private void setUpRecycleView() {
        recyclerViewBarangTransaksi.setHasFixedSize(true);
        recyclerViewBarangTransaksi.setLayoutManager(new LinearLayoutManager(this));
        adapterRecycleViewBarangTransaksi = new AdapterRecycleViewDataBarang( listItemDataBarangs, BarangTransaksiActivity.this,1, Integer.parseInt(intent.getStringExtra("type")));
        recyclerViewBarangTransaksi.setAdapter( adapterRecycleViewBarangTransaksi );
        adapterRecycleViewBarangTransaksi.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_search,menu);
        MenuItem searchItem = menu.findItem(R.id.ic_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener( new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapterRecycleViewBarangTransaksi.getFilter().filter(s);
                return false;
            }
        } );

        searchView.setQueryHint("Search");
        return true;
    }
}
