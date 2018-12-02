package com.pratamatechnocraft.silaporanpenjualan;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pratamatechnocraft.silaporanpenjualan.Adapter.AdapterRecycleViewDataUser;
import com.pratamatechnocraft.silaporanpenjualan.Adapter.AdapterRecycleViewDetailTransaksi;
import com.pratamatechnocraft.silaporanpenjualan.Model.BaseUrlApiModel;
import com.pratamatechnocraft.silaporanpenjualan.Model.ListItemDetailTransaksi;
import com.pratamatechnocraft.silaporanpenjualan.Model.ListItemTransaksi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InvoiceActivity extends AppCompatActivity {

    BaseUrlApiModel baseUrlApiModel = new BaseUrlApiModel();
    private String baseUrl=baseUrlApiModel.getBaseURL();
    private ProgressDialog progress;
    private AlertDialog alertDialog,alertDialog1;
    private AdapterRecycleViewDetailTransaksi adapterRecycleViewDetailTransaksi;
    private Button buttonBayarDetailTransaksi,buttonPrintDetailTransaksi;
    private RecyclerView recyclerViewDetailTransaksi;
    private SwipeRefreshLayout refreshInvoice;
    private TextView txtNoInvoiceDetailTransaksi, txtTanggalTransaksiDetail, txtNamaKasirDetailTransaksi,txtStatusTransaksiDetailTransaksi,txtHargaTotalDetailInvoice,txtCatatanDetailTransaksi;
    private static final String API_URL = "api/transaksi?api=transaksidetail&kd_transaksi=";
    Intent intent;
    private List<ListItemDetailTransaksi> listItemDetailTransaksis;
    private WebView myWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_invoice );

        buttonPrintDetailTransaksi = findViewById( R.id.buttonPrintDetailTransaksi );

        WebView webView = new WebView(this);
        webView.setWebViewClient(new WebViewClient() {

            public boolean shouldOverrideUrlLoading(WebView view,
                                                    String url)
            {
                return false;
            }

        });


        webView.loadUrl("https://si-penjualan.pratamatechnocraft.com/print_invoice");

        myWebView = webView;

        buttonPrintDetailTransaksi.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                createWebPrintJob(myWebView);
            }
        });

        intent = getIntent();
        progress = new ProgressDialog(this);

        Toolbar ToolBarAtas2 = (Toolbar)findViewById(R.id.toolbar_invoice);
        setSupportActionBar(ToolBarAtas2);
        ToolBarAtas2.setLogoDescription("Detail Invoice");
        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_black_24dp);
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.colorIcons), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerViewDetailTransaksi = (RecyclerView) findViewById(R.id.recycleViewDetailTransaksi);
        recyclerViewDetailTransaksi.setHasFixedSize(true);
        recyclerViewDetailTransaksi.setLayoutManager(new LinearLayoutManager(this));

        refreshInvoice = findViewById( R.id.refreshInvoice );

        txtNoInvoiceDetailTransaksi = findViewById( R.id.txtNoInvoiceDetailTransaksi );
        txtTanggalTransaksiDetail = findViewById( R.id.txtTanggalTransaksiDetail );
        txtStatusTransaksiDetailTransaksi = findViewById( R.id.txtStatusTransaksiDetailTransaksi );
        txtCatatanDetailTransaksi = findViewById( R.id.txtCatatanDetailTransaksi );
        txtHargaTotalDetailInvoice = findViewById( R.id.txtHargaTotalDetailInvoice );
        txtNamaKasirDetailTransaksi = findViewById( R.id.txtNamaKasirDetailTransaksi );
        buttonBayarDetailTransaksi = findViewById( R.id.buttonBayarDetailTransaksi );

        buttonBayarDetailTransaksi.setVisibility( View.GONE );

        listItemDetailTransaksis = new ArrayList<>();
        adapterRecycleViewDetailTransaksi = new AdapterRecycleViewDetailTransaksi( listItemDetailTransaksis, this);

        loadDetail( intent.getStringExtra( "kdTransaksi" ) );

        refreshInvoice.setOnRefreshListener( new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listItemDetailTransaksis.clear();
                adapterRecycleViewDetailTransaksi.notifyDataSetChanged();
                loadDetail(intent.getStringExtra( "kdTransaksi" ));
            }
        } );

        buttonBayarDetailTransaksi.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBayarDialog();
            }
        } );

        recyclerViewDetailTransaksi.setAdapter( adapterRecycleViewDetailTransaksi );
    }

    /*PRINT*/
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void createWebPrintJob(WebView webView) {

        PrintManager printManager = (PrintManager) this
                .getSystemService(Context.PRINT_SERVICE);

        PrintDocumentAdapter printAdapter =
                webView.createPrintDocumentAdapter("MyDocument");

        String jobName = getString(R.string.app_name) + " Print Invoice";

        printManager.print(jobName, printAdapter,
                new PrintAttributes.Builder().build());
    }

    private void showBayarDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Anda Yakin ??");
        alertDialogBuilder.setPositiveButton("Iya",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        progress.setMessage("Mohon Ditunggu, Sedang diProses.....");
                        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progress.setIndeterminate(false);
                        progress.setCanceledOnTouchOutside(false);
                        prosesBayar();
                    }
                });

        alertDialogBuilder.setNegativeButton("Tidak",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog1.dismiss();
            }
        });

        alertDialog1 = alertDialogBuilder.create();
        alertDialog1.show();
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
                Intent i = new Intent(InvoiceActivity.this, FormEditTransaksiActivity.class );
                i.putExtra( "kdTransaksi",intent.getStringExtra( "kdTransaksi" ) );
                startActivity(i);
                return true;
            case R.id.icon_hapus:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("Yakin Ingin Menghapus Data Ini ??");
                alertDialogBuilder.setPositiveButton("Iya",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                deleteBiaya(intent.getStringExtra( "kdTransaksi" ));
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

    private void loadDetail(String kdTransaksi){
        refreshInvoice.setRefreshing(true);
        StringRequest stringRequest = new StringRequest( Request.Method.GET, baseUrl+API_URL+kdTransaksi,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            final JSONObject invoicedetail = new JSONObject(response);
                            if (invoicedetail.getString( "status" ).equals( "0" )){
                                txtStatusTransaksiDetailTransaksi.setText( "Lunas" );
                                buttonBayarDetailTransaksi.setVisibility( View.GONE );
                            }else{
                                txtStatusTransaksiDetailTransaksi.setText( "Belum Lunas" );
                                buttonBayarDetailTransaksi.setVisibility( View.VISIBLE );
                            }
                            if (invoicedetail.getString( "jenis_transaksi" ).equals( "0" )){
                                txtNoInvoiceDetailTransaksi.setText(  "#PL"+invoicedetail.getString( "kd_transaksi" ));
                            }else{
                                txtNoInvoiceDetailTransaksi.setText(  "#PB"+invoicedetail.getString( "kd_transaksi" ));
                            }

                            txtTanggalTransaksiDetail.setText( invoicedetail.getString( "tgl_transaksi" ) );
                            txtHargaTotalDetailInvoice.setText(  "Rp. "+invoicedetail.getString( "harga_total" ));
                            if (!invoicedetail.getString( "catatan" ).isEmpty() ){
                                txtCatatanDetailTransaksi.setText( invoicedetail.getString( "catatan" ) );
                            }

                            JSONArray detailinvoice = invoicedetail.getJSONArray("detailinvoice");
                            for (int i = 0; i<detailinvoice.length(); i++){
                                JSONObject detailInvoiceObject = detailinvoice.getJSONObject( i );
                                String harga;
                                if (invoicedetail.getString( "jenis_transaksi" ).equals( "0" )){
                                    harga=detailInvoiceObject.getString( "harga_jual" );
                                }else{
                                    harga=detailInvoiceObject.getString( "harga_beli" );
                                }
                                ListItemDetailTransaksi listItemDetailTransaksi = new ListItemDetailTransaksi(
                                        detailInvoiceObject.getString( "nama_barang" ),
                                        detailInvoiceObject.getString( "qty" ),
                                        harga
                                );

                                Log.d( "TAG", "nama_barang: "+detailInvoiceObject.getString( "nama_barang" ) );

                                listItemDetailTransaksis.add( listItemDetailTransaksi );
                                adapterRecycleViewDetailTransaksi.notifyDataSetChanged();
                            }

                            txtNamaKasirDetailTransaksi.setText( invoicedetail.getString( "nama_user" ) );
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(InvoiceActivity.this, "Periksa koneksi & coba lagi", Toast.LENGTH_SHORT).show();
                        }
                        refreshInvoice.setRefreshing( false );
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(InvoiceActivity.this, "Periksa koneksi & coba lagi1", Toast.LENGTH_SHORT).show();
                        refreshInvoice.setRefreshing( false );
                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue( InvoiceActivity.this );
        requestQueue.add( stringRequest );
    }

    private void deleteBiaya(String kdTransaksi){
        StringRequest stringRequest = new StringRequest( Request.Method.GET, baseUrl+"api/transaksi?api=delete&kd_transaksi="+kdTransaksi,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String kode = jsonObject.getString("kode");
                            if (kode.equals("1")) {
                                finish();
                                Toast.makeText(InvoiceActivity.this, "Berhasil Menghapus Trnsaksi", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(InvoiceActivity.this, "Gagal Menghapus Trnsaksi", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(InvoiceActivity.this, "Periksa koneksi & coba lagi", Toast.LENGTH_SHORT).show();
                        }
                        refreshInvoice.setRefreshing( false );
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(InvoiceActivity.this, "Periksa koneksi & coba lagi1", Toast.LENGTH_SHORT).show();
                        refreshInvoice.setRefreshing( false );
                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue( InvoiceActivity.this );
        requestQueue.add( stringRequest );
    }

    private void prosesBayar() {
        progress.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, baseUrl+"api/transaksi", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String kode = jsonObject.getString("kode");
                    if (kode.equals("1")) {
                        finish();
                        Toast.makeText(InvoiceActivity.this, "Berhasil Bayar", Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(InvoiceActivity.this, "Gagal Bayar", Toast.LENGTH_SHORT).show();
                    }
                    progress.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d( "TAG", e.toString() );
                    Toast.makeText(InvoiceActivity.this, "Periksa koneksi & coba lagi", Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                }
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(InvoiceActivity.this, "Periksa koneksi & coba lagi1", Toast.LENGTH_SHORT).show();
                progress.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put( "kd_transaksi", intent.getStringExtra( "kdTransaksi" ) );
                params.put( "api", "bayar" );
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
