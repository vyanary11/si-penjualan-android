package com.pratamatechnocraft.silaporanpenjualan;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class KeranjangActivity extends AppCompatActivity {
    Button btnSimpanTransaksi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_keranjang );

        Toolbar ToolBarAtas2 = (Toolbar)findViewById(R.id.toolbar_keranjang);
        setSupportActionBar(ToolBarAtas2);
        ToolBarAtas2.setLogoDescription("Keranjang");
        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.colorIcons), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btnSimpanTransaksi = findViewById( R.id.btnSimpanTransaksi );

        btnSimpanTransaksi.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(KeranjangActivity.this, InvoiceActivity.class );
                startActivity(i);
            }
        } );

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


}
