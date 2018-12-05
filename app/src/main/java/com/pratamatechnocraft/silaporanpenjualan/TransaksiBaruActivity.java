package com.pratamatechnocraft.silaporanpenjualan;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pratamatechnocraft.silaporanpenjualan.Adapter.AdapterPagerTransaksiBaru;
import com.pratamatechnocraft.silaporanpenjualan.Adapter.DBDataSourceKeranjang;
import com.pratamatechnocraft.silaporanpenjualan.Model.ModelKeranjang;

import java.util.ArrayList;

public class TransaksiBaruActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private int[] layouts = {R.layout.fragment_transaksi_baru_satu,R.layout.fragment_transaksi_baru_dua};
    private AdapterPagerTransaksiBaru adapterPagerTransaksiBaru;

    private LinearLayout dots_layout;
    private ImageView[] dots;
    private Button btnLanjut,btnKembali;
    private AlertDialog alertDialog;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_transaksi_baru );
        intent = getIntent();

        Toolbar ToolBarAtas2 = (Toolbar)findViewById(R.id.toolbar_transaksi_baru);
        setSupportActionBar(ToolBarAtas2);
        ToolBarAtas2.setLogoDescription("Transaksi ");
        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.ic_close_white_24dp);
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.colorIcons), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mViewPager = findViewById( R.id.viewPager );
        if (intent.getStringExtra( "type" ).equals( "0" )){
            this.setTitle("Transaksi Penjualan Baru");
            adapterPagerTransaksiBaru = new AdapterPagerTransaksiBaru( layouts,this,0 );
        }else{
            this.setTitle("Transaksi Pembelian Baru");
            adapterPagerTransaksiBaru = new AdapterPagerTransaksiBaru( layouts,this,1 );
        }

        mViewPager.setAdapter( adapterPagerTransaksiBaru );

        dots_layout = findViewById( R.id.dotsLayouts );
        btnLanjut = findViewById( R.id.lanjut );
        btnKembali = findViewById( R.id.kembali );

        btnKembali.setVisibility( View.GONE );

        btnLanjut.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem( 1 );
            }
        } );

        btnKembali.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem( 0 );
            }
        } );
        createDots( 0 );

        mViewPager.addOnPageChangeListener( new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                createDots( i );
                if (i==0){
                    btnKembali.setVisibility( View.GONE );
                    btnLanjut.setVisibility( View.VISIBLE );
                }else if(i==1){
                    btnLanjut.setVisibility( View.GONE );
                    btnKembali.setVisibility( View.VISIBLE );
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        } );

    }

    private void createDots(int current_position){
        if (dots_layout!=null)
            dots_layout.removeAllViews();

        dots=new  ImageView[layouts.length];
        for (int i=0;i<layouts.length;i++){
            dots[i] = new ImageView( this );
            if (i==current_position){
                dots[i].setImageDrawable( ContextCompat.getDrawable( this, R.drawable.active_dots ) );
            }else{
                dots[i].setImageDrawable( ContextCompat.getDrawable( this, R.drawable.inactive_dots ) );
            }

            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams( ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT );
            params.setMargins( 4,0,4,0 );
            dots_layout.addView( dots[i], params );

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("Yakin Ingin Keluar ??");
                alertDialogBuilder.setPositiveButton("Iya",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                onBackPressed();
                            }
                        });

                alertDialogBuilder.setNegativeButton("Tidak",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });

                alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapterPagerTransaksiBaru.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
