package com.pratamatechnocraft.silaporanpenjualan;

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
import com.pratamatechnocraft.silaporanpenjualan.Fragment.BiayaFragment;
import com.pratamatechnocraft.silaporanpenjualan.Fragment.DashboardFragment;
import com.pratamatechnocraft.silaporanpenjualan.Fragment.DataBarangFragment;
import com.pratamatechnocraft.silaporanpenjualan.Fragment.DataKategoriBarangFragment;
import com.pratamatechnocraft.silaporanpenjualan.Fragment.DataUserFragment;
import com.pratamatechnocraft.silaporanpenjualan.Fragment.DateRangePickerFragment;
import com.pratamatechnocraft.silaporanpenjualan.Fragment.LaporanFragment;
import com.pratamatechnocraft.silaporanpenjualan.Fragment.LaporanLabaRugiFragment;
import com.pratamatechnocraft.silaporanpenjualan.Fragment.ProfileFragment;
import com.pratamatechnocraft.silaporanpenjualan.Fragment.TabLayoutFragment;
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

        if (Integer.parseInt( user.get( sessionManager.LEVEL_USER ) )==0){
            levelUser.setText( "Owner" );
            navigationView.inflateMenu( R.menu.activity_main_drawer );
        }else{
            levelUser.setText( "Kasir" );
            navigationView.inflateMenu( R.menu.activity_main_drawer_kasir );
        }

        navigationView.setNavigationItemSelectedListener( this );
        navigationView.setCheckedItem( R.id.nav_dashboard );


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
        return true;
    }

    private void displaySelectedScreen(int itemId) {
        int id = itemId;

        if (id == R.id.nav_dashboard) {
            fragment = new DashboardFragment();
        } else if (id == R.id.nav_transaksi_penjualan) {
            fragment = new TabLayoutFragment(0);
        } else if (id == R.id.nav_transaksi_pembelian) {
            fragment = new TabLayoutFragment(1);
        } else if (id == R.id.nav_biaya) {
            fragment = new BiayaFragment();
        } else if (id == R.id.nav_laporan_harian) {
            fragment = new LaporanFragment(0);
        } else if (id == R.id.nav_laporan_bulanan) {
            fragment = new LaporanFragment(1);
        } else if (id == R.id.nav_laporan_tahunan) {
            fragment = new LaporanFragment(2);
        }else if (id == R.id.nav_laporan_labarugi) {
            fragment = new LaporanLabaRugiFragment();
        }else if (id == R.id.nav_barang) {
            fragment = new DataBarangFragment();
        }else if (id == R.id.nav_kategori_barang) {
            fragment = new DataKategoriBarangFragment();
        }else if (id == R.id.nav_user) {
            fragment = new DataUserFragment();
        }else if (id == R.id.nav_profile) {
            fragment = new ProfileFragment();
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
