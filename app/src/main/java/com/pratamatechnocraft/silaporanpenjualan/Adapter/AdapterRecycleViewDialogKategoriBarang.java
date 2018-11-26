package com.pratamatechnocraft.silaporanpenjualan.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pratamatechnocraft.silaporanpenjualan.Model.BaseUrlApiModel;
import com.pratamatechnocraft.silaporanpenjualan.Model.ListItemDataKategoriBarang;
import com.pratamatechnocraft.silaporanpenjualan.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AdapterRecycleViewDialogKategoriBarang extends RecyclerView.Adapter<AdapterRecycleViewDialogKategoriBarang.ViewHolder>{

    private List<ListItemDataKategoriBarang> listItemDataKategorisBarangs;
    private Context context;
    private AlertDialog alertDialog;
    BaseUrlApiModel baseUrlApiModel = new BaseUrlApiModel();
    private String baseUrl=baseUrlApiModel.getBaseURL();
    private String type;

    public AdapterRecycleViewDialogKategoriBarang(List<ListItemDataKategoriBarang> listItemDataKategorisBarangs, Context context, String type) {
        this.listItemDataKategorisBarangs = listItemDataKategorisBarangs;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate( R.layout.list_item_data_kategori_barang,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ListItemDataKategoriBarang listItemDataKategoriBarang = listItemDataKategorisBarangs.get(position);

        holder.txtNamaKategoriBarang.setText( listItemDataKategoriBarang.getNamaKategori());
        holder.imgBtnDeleteKategoriBarang.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d( "TAG", "onClick: "+listItemDataKategoriBarang.getKdKategori() );
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setMessage("Yakin Ingin Menghapus Data Ini ??");
                alertDialogBuilder.setPositiveButton("Iya",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                deleteKategori(listItemDataKategoriBarang.getKdKategori());
                                notifyItemRemoved( position );
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
            }
        } );

    }

    @Override
    public int getItemCount() {
        return listItemDataKategorisBarangs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView txtNamaKategoriBarang;
        public CardView cardViewDataKategoriBarang;
        public ImageButton imgBtnDeleteKategoriBarang;

        public ViewHolder(View itemView) {
            super(itemView);
            txtNamaKategoriBarang = (TextView) itemView.findViewById(R.id.txtNamaKategoriBarang);
            cardViewDataKategoriBarang = (CardView) itemView.findViewById(R.id.cardViewDataKategoriBarang);
            imgBtnDeleteKategoriBarang = (ImageButton) itemView.findViewById( R.id.imgBtnDeleteKategoriBarang );
        }
    }

    private void deleteKategori(String kdKategori){
        StringRequest stringRequest = new StringRequest( Request.Method.GET, baseUrl+"api/kategori?api=delete&kd_kategori="+kdKategori,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String kode = jsonObject.getString("kode");
                            if (kode.equals("1")) {
                                Toast.makeText(context, "Berhasil Menghapus Kategori", Toast.LENGTH_SHORT).show();

                            }else{
                                Toast.makeText(context, "Gagal Menghapus Kategori", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Periksa koneksi & coba lagi", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Periksa koneksi & coba lagi1", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue( context );
        requestQueue.add( stringRequest );
    }

}
