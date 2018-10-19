package com.pratamatechnocraft.silaporanpenjualan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.pratamatechnocraft.silaporanpenjualan.Fragment.DashboardFragment;
import com.pratamatechnocraft.silaporanpenjualan.Model.BaseUrlApiModel;
import com.pratamatechnocraft.silaporanpenjualan.Service.SessionManager;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static String urlGambar = "";
    public static TextView namaUser,levelUser;
    public static ImageView fotoUser;
    public Fragment fragment = null;
    NavigationView navigationView;
    SessionManager sessionManager;
    HashMap<String, String> user=null;
    BaseUrlApiModel baseUrlApiModel = new BaseUrlApiModel();
    private String baseUrl=baseUrlApiModel.getBaseURL();
    private static final String API_URL_LOAD = "api/user?api=profile&kd_user=";

    private static final String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );

        DrawerLayout drawer = (DrawerLayout) findViewById( R.id.drawer_layout );
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close );
        //noinspection deprecation
        drawer.setDrawerListener( toggle );
        toggle.syncState();

        displaySelectedScreen( R.id.nav_dashboard );

        sessionManager = new SessionManager( this );
        sessionManager.checkLogin();
        user = sessionManager.getUserDetail();

        navigationView = (NavigationView) findViewById( R.id.nav_view );

        View headerView = navigationView.getHeaderView(0);
        namaUser = headerView.findViewById( R.id.textViewNamaUser );
        fotoUser =  headerView.findViewById( R.id.imageViewFotoUser );
        levelUser = headerView.findViewById( R.id.textViewLevelUser );

        loadProfile(user.get( sessionManager.KD_USER ));

        namaUser.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        } );
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById( R.id.drawer_layout );
        if (drawer.isDrawerOpen( GravityCompat.START )) {
            drawer.closeDrawer( GravityCompat.START );
            sessionManager.checkLogin();
        } else {
            super.onBackPressed();
            sessionManager.checkLogin();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        displaySelectedScreen( item.getItemId() );
        Toast.makeText(MainActivity.this, item.getItemId(), Toast.LENGTH_SHORT).show();
        return true;
    }

    private void displaySelectedScreen(int itemId) {
        int id = itemId;

        if (id == R.id.nav_dashboard) {
            fragment = new DashboardFragment();
        } else if (itemId == R.id.nav_transaksi_penjualan) {

        } else if (id == R.id.nav_transaksi_pembelian) {

        } else if (id == R.id.nav_laporan_harian) {

        } else if (id == R.id.nav_laporan_bulanan) {

        } else if (id == R.id.nav_laporan_tahunan) {

        }else if (id == R.id.nav_laporan_labarugi) {

        }else if (id == R.id.nav_barang) {

        }else if (id == R.id.nav_kategori) {

        }else if (id == R.id.nav_user) {

        }else if (id == R.id.nav_profile) {

        }else if (id == R.id.nav_logout) {
            sessionManager.logout();
        }

        // fragmentLast=id;

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace( R.id.screen_area, fragment );
            ft.addToBackStack(null);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById( R.id.drawer_layout );
        drawer.closeDrawer( GravityCompat.START );
    }

    private void loadProfile(String kd_user){
        StringRequest stringRequest = new StringRequest( Request.Method.GET, baseUrl+API_URL_LOAD+kd_user,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        final JSONObject userprofile = new JSONObject(response);
                        namaUser.setText( userprofile.getString( "nama_depan" )+" "+userprofile.getString( "nama_belakang" ) );
                        if (userprofile.getInt( "level_user" )==0){
                            levelUser.setText( "Owner" );
                            navigationView.inflateMenu( R.menu.activity_main_drawer );
                            navigationView.setNavigationItemSelectedListener( MainActivity.this );
                            navigationView.getMenu().getItem(0).getSubMenu().getItem(0).setChecked(true);
                        }else{
                            levelUser.setText( "Kasir" );
                            navigationView.inflateMenu( R.menu.activity_main_drawer_kasir );
                            navigationView.setNavigationItemSelectedListener( MainActivity.this );
                            navigationView.getMenu().getItem(0).getSubMenu().getItem(0).setChecked(true);
                        }
                        urlGambar = baseUrl+String.valueOf( userprofile.getString( "foto" )  );
                        Glide.with(MainActivity.this)
                                // LOAD URL DARI INTERNET
                                .load(urlGambar)
                                // LOAD GAMBAR AWAL SEBELUM GAMBAR UTAMA MUNCUL, BISA DARI LOKAL DAN INTERNET
                                .into(fotoUser);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "Periksa koneksi & coba lagi1", Toast.LENGTH_SHORT).show();
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, "Periksa koneksi & coba lagi", Toast.LENGTH_SHORT).show();
                }
            }
        );

        RequestQueue requestQueue = Volley.newRequestQueue( MainActivity.this );
        requestQueue.add( stringRequest );
    }



}
