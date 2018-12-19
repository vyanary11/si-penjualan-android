package com.pratamatechnocraft.silaporanpenjualan.Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pratamatechnocraft.silaporanpenjualan.BarangTransaksiActivity;
import com.pratamatechnocraft.silaporanpenjualan.DetailBiayaActivity;
import com.pratamatechnocraft.silaporanpenjualan.FormBiayaActivity;
import com.pratamatechnocraft.silaporanpenjualan.InvoiceActivity;
import com.pratamatechnocraft.silaporanpenjualan.Model.BaseUrlApiModel;
import com.pratamatechnocraft.silaporanpenjualan.Model.ModelKeranjang;
import com.pratamatechnocraft.silaporanpenjualan.R;
import com.pratamatechnocraft.silaporanpenjualan.Service.SessionManager;
import com.pratamatechnocraft.silaporanpenjualan.TransaksiBaruActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdapterPagerTransaksiBaru extends PagerAdapter {

    private int[] layouts=null;
    private LayoutInflater layoutInflater;
    private Context context;
    private RecyclerView recyclerViewKeranjang;
    private AdapterRecycleViewKeranjang adapterRecycleViewKeranjang;
    private DBDataSourceKeranjang dbDataSourceKeranjang;
    private ArrayList<ModelKeranjang> modelKeranjangs;
    private TextView noDataKeranjang,txtJmlItemKeranjang, txtHargaTotalKeranjang;
    private Button tambahBarangKeKeranjang,buttonSimpanTransaksi,buttonBayarTransaksi;
    private TextInputLayout inputLayoutCatatanTransaksi;
    private EditText inputCatatanTransaksi;
    private int type;
    private ProgressDialog progress;
    SessionManager sessionManager;
    HashMap<String, String> user=null;
    private BaseUrlApiModel baseUrlApiModel = new BaseUrlApiModel();
    private String baseUrl=baseUrlApiModel.getBaseURL();
    private static final String API_URL = "api/transaksi";
    private int jmlItem;
    private int totalHarga;

    public AdapterPagerTransaksiBaru(int[] layouts, Context context, int type) {
        this.layouts = layouts;
        layoutInflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        this.context = context;
        this.type = type;
    }

    @Override
    public int getCount() {
        return layouts.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view==o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = layoutInflater.inflate( layouts[position],container,false );
        container.addView( view );
        sessionManager = new SessionManager( context );
        user = sessionManager.getUserDetail();
        progress = new ProgressDialog(context);
        dbDataSourceKeranjang = new DBDataSourceKeranjang(context);
        dbDataSourceKeranjang.open();
        modelKeranjangs = dbDataSourceKeranjang.getAllKeranjang();
        noDataKeranjang = view.findViewById( R.id.noDataKeranjang );
        txtJmlItemKeranjang = view.findViewById( R.id.txtJmlItemKeranjang );
        txtHargaTotalKeranjang = view.findViewById( R.id.txtHargaTotallKeranjang );
        recyclerViewKeranjang = view.findViewById( R.id.recycleViewKeranjang );
        tambahBarangKeKeranjang =view.findViewById( R.id.tambahBarangKeKeranjang );

        buttonBayarTransaksi = view.findViewById( R.id.buttonBayarTransaksi );
        buttonSimpanTransaksi = view.findViewById( R.id.buttonSimpanTransaksi );
        inputLayoutCatatanTransaksi = view.findViewById(R.id.inputLayoutCatatanTransaksi);
        inputCatatanTransaksi = view.findViewById(R.id.inputCatatanTransaksi);

        if (position==0){
            recyclerViewKeranjang.setHasFixedSize(true);
            recyclerViewKeranjang.setLayoutManager(new LinearLayoutManager(context));
            adapterRecycleViewKeranjang = new AdapterRecycleViewKeranjang( modelKeranjangs, context, txtJmlItemKeranjang, txtHargaTotalKeranjang, type);
            recyclerViewKeranjang.setAdapter( adapterRecycleViewKeranjang );
            adapterRecycleViewKeranjang.notifyDataSetChanged();

            if (adapterRecycleViewKeranjang.getItemCount()==0){
                noDataKeranjang.setVisibility( View.VISIBLE );
                recyclerViewKeranjang.setVisibility( View.GONE );
            }

            tambahBarangKeKeranjang.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent( context, BarangTransaksiActivity.class);
                    if (type==0){
                        intent.putExtra( "type", "0" );
                    }else{
                        intent.putExtra( "type", "1" );
                    }
                    context.startActivity(intent);
                }
            } );
        }else if(position==1){
            /*if (adapterRecycleViewKeranjang.getItemCount()==0){
                notifyDataSetChanged();
            }*/
            buttonSimpanTransaksi.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    progress.setMessage("Mohon Ditunggu, Sedang diProses.....");
                    progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progress.setIndeterminate(false);
                    progress.setCanceledOnTouchOutside(false);
                    dbDataSourceKeranjang.close();
                    dbDataSourceKeranjang.open();
                    ModelKeranjang modelKeranjang;
                    modelKeranjangs = dbDataSourceKeranjang.getAllKeranjang();
                    for (int i=0;i<modelKeranjangs.size();i++){
                        modelKeranjang = modelKeranjangs.get( i );
                        int subTotal = modelKeranjang.getHargaBarang() * modelKeranjang.getQty();
                        jmlItem=jmlItem+modelKeranjang.getQty();
                        totalHarga=totalHarga+subTotal;
                    }
                    try {
                        prosesKeDB(
                                user.get( SessionManager.KD_USER),
                                String.valueOf( jmlItem ),
                                String.valueOf(totalHarga),
                                "1",
                                inputCatatanTransaksi.getText().toString().trim(),
                                String.valueOf( type ),
                                convertObjectArrayToString(dbDataSourceKeranjang.getArrayKdBarangKeranjang(),","),
                                convertObjectArrayToString(dbDataSourceKeranjang.getArrayQtyKeranjang(),",")
                        );
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } );

            buttonBayarTransaksi.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    progress.setMessage("Mohon Ditunggu, Sedang diProses.....");
                    progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progress.setIndeterminate(false);
                    progress.setCanceledOnTouchOutside(false);
                    dbDataSourceKeranjang.close();
                    dbDataSourceKeranjang.open();
                    ModelKeranjang modelKeranjang;
                    modelKeranjangs = dbDataSourceKeranjang.getAllKeranjang();
                    for (int i=0;i<modelKeranjangs.size();i++){
                        modelKeranjang = modelKeranjangs.get( i );
                        int subTotal = modelKeranjang.getHargaBarang() * modelKeranjang.getQty();
                        jmlItem=jmlItem+modelKeranjang.getQty();
                        totalHarga=totalHarga+subTotal;
                    }
                    try {
                        prosesKeDB(
                                user.get( SessionManager.KD_USER),
                                String.valueOf( jmlItem ),
                                String.valueOf(totalHarga),
                                "0",
                                inputCatatanTransaksi.getText().toString().trim(),
                                String.valueOf( type ),
                                convertObjectArrayToString(dbDataSourceKeranjang.getArrayKdBarangKeranjang(),","),
                                convertObjectArrayToString(dbDataSourceKeranjang.getArrayQtyKeranjang(),",")
                        );
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } );
        }
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        View view = (View) object;
        container.removeView( view );
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    private void prosesKeDB(final String kdUser, final String jmlItem, final String hargaTotal, final String status, final String catatan, final String jenisTransaksi, final String kdBarangKeranjang, final String qtyKeranjang) {
        progress.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, baseUrl+API_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String kode = jsonObject.getString("kode");
                    if (kode.equals("1")) {
                        Toast.makeText(context, "Transaksi Berhasil", Toast.LENGTH_SHORT).show();
                        ((TransaksiBaruActivity)context).finish();
                        Intent i = new Intent(context, InvoiceActivity.class);
                        i.putExtra("done", true);
                        i.putExtra("kdTransaksi", jsonObject.getString( "kd_transaksi" ));
                        context.startActivity(i);
                    }else{
                        Toast.makeText(context, "Transaksi Gagal", Toast.LENGTH_SHORT).show();
                    }
                    progress.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d( "TAG", e.toString() );
                    Toast.makeText(context, "Periksa koneksi & coba lagi", Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                }
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(context, "Periksa koneksi & coba lagi1", Toast.LENGTH_SHORT).show();
                progress.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("kd_user", kdUser);
                params.put("jml_item", jmlItem);
                params.put("harga_total", hargaTotal);
                params.put("status", status);
                params.put("catatan", catatan);
                params.put("jenis_transaksi", jenisTransaksi);
                params.put("kd_barang_keranjang", kdBarangKeranjang);
                params.put("qty_keranjang", qtyKeranjang);
                params.put( "api", "tambah" );
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    private static String convertObjectArrayToString(JSONArray arr, String delimiter) throws JSONException {
        StringBuilder sb = new StringBuilder();
        for (int i=0;i<arr.length();i++){
            sb.append(arr.getString( i )).append(delimiter);
        }
        return sb.substring(0, sb.length() - 1);

    }
}
