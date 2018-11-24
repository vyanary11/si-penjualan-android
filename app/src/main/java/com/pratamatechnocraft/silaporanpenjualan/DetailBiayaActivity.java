package com.pratamatechnocraft.silaporanpenjualan;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pratamatechnocraft.silaporanpenjualan.Model.BaseUrlApiModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class DetailBiayaActivity extends AppCompatActivity {

    private AlertDialog alertDialog;
    private SwipeRefreshLayout refreshDetailBiaya;
    private TextView txtDetailNamaBiaya, txtTanggalBiaya, txtJumlahBiaya;
    BaseUrlApiModel baseUrlApiModel = new BaseUrlApiModel();
    private String baseUrl=baseUrlApiModel.getBaseURL();
    private static final String API_URL = "api/biaya?api=biayadetail&kd_biaya=";
    Intent intent;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_detail_biaya );
        refreshDetailBiaya = (SwipeRefreshLayout) findViewById( R.id.refreshDetailBiaya );
        intent = getIntent();

        /*TEXT VIEW*/
        txtJumlahBiaya = findViewById( R.id.txtDetailJumlahBiaya);
        txtTanggalBiaya = findViewById( R.id.txtDetailTanggalBiaya);
        txtDetailNamaBiaya = findViewById( R.id.txtDetailNamaBiaya);

        Toolbar ToolBarAtas2 = (Toolbar)findViewById(R.id.toolbar_detailbiaya );
        setSupportActionBar(ToolBarAtas2);
        this.setTitle("Data Biaya");
        ToolBarAtas2.setSubtitle( "Detail Biaya" );
        ToolBarAtas2.setSubtitleTextColor( ContextCompat.getColor(this, R.color.colorIcons) );
        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_black_24dp);
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.colorIcons), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadDetail(intent.getStringExtra( "kdBiaya" ));

        refreshDetailBiaya.setOnRefreshListener( new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadDetail(intent.getStringExtra( "kdBiaya" ));
            }
        } );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_activity_detail,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
            case R.id.icon_edit:
                Intent i = new Intent(DetailBiayaActivity.this, FormBiayaActivity.class );
                i.putExtra( "type", "edit" );
                i.putExtra( "kdBiaya",intent.getStringExtra( "kdBiaya" ) );
                startActivity(i);
                return true;
            case R.id.icon_hapus:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("Yakin Ingin Menghapus Data Ini ??");
                alertDialogBuilder.setPositiveButton("Iya",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                deleteBiaya(intent.getStringExtra( "kdBiaya" ));
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
        loadDetail(intent.getStringExtra( "kdBiaya" ));
    }

    private void loadDetail(String kdBiaya){
        refreshDetailBiaya.setRefreshing(true);
        StringRequest stringRequest = new StringRequest( Request.Method.GET, baseUrl+API_URL+kdBiaya,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            DecimalFormat formatter = new DecimalFormat("#,###,###");
                            final JSONObject biayadetail = new JSONObject(response);
                            txtDetailNamaBiaya.setText(  biayadetail.getString( "nama_biaya" ));
                            txtTanggalBiaya.setText( biayadetail.getString( "tgl_biaya" ) );
                            txtJumlahBiaya.setText(  "Rp. "+formatter.format(Double.parseDouble(biayadetail.getString( "jumlah_biaya" ))));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(DetailBiayaActivity.this, "Periksa koneksi & coba lagi", Toast.LENGTH_SHORT).show();
                        }
                        refreshDetailBiaya.setRefreshing( false );
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DetailBiayaActivity.this, "Periksa koneksi & coba lagi1", Toast.LENGTH_SHORT).show();
                        refreshDetailBiaya.setRefreshing( false );
                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue( DetailBiayaActivity.this );
        requestQueue.add( stringRequest );
    }

    private void deleteBiaya(String kdBiaya){
        StringRequest stringRequest = new StringRequest( Request.Method.GET, baseUrl+"api/biaya?api=delete&kd_biaya="+kdBiaya,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String kode = jsonObject.getString("kode");
                            if (kode.equals("1")) {
                                /*JSONObject data = jsonObject.getJSONObject("data");
                                String idBiaya = data.getString("kd_user").trim();

                                Intent i = new Intent(FormBiayaActivity.this, DetailBiayaActivity.class);
                                i.putExtra( "idBiaya", idBiaya );
                                startActivity(i);*/
                                finish();
                                Toast.makeText(DetailBiayaActivity.this, "Berhasil Menghapus Biaya", Toast.LENGTH_SHORT).show();

                            }else{
                                Toast.makeText(DetailBiayaActivity.this, "Gagal Menghapus Biaya", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(DetailBiayaActivity.this, "Periksa koneksi & coba lagi", Toast.LENGTH_SHORT).show();
                        }
                        refreshDetailBiaya.setRefreshing( false );
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DetailBiayaActivity.this, "Periksa koneksi & coba lagi1", Toast.LENGTH_SHORT).show();
                        refreshDetailBiaya.setRefreshing( false );
                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue( DetailBiayaActivity.this );
        requestQueue.add( stringRequest );
    }
}
