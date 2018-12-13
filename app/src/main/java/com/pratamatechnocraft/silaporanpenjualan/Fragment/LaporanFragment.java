package com.pratamatechnocraft.silaporanpenjualan.Fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
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
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pratamatechnocraft.silaporanpenjualan.Adapter.AdapterRecycleViewBarangTerjual;
import com.pratamatechnocraft.silaporanpenjualan.Adapter.AdapterRecycleViewDetailTransaksi;
import com.pratamatechnocraft.silaporanpenjualan.InvoiceActivity;
import com.pratamatechnocraft.silaporanpenjualan.Model.BaseUrlApiModel;
import com.pratamatechnocraft.silaporanpenjualan.Model.ListItemBarangTerjual;
import com.pratamatechnocraft.silaporanpenjualan.Model.ListItemDetailTransaksi;
import com.pratamatechnocraft.silaporanpenjualan.R;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

@SuppressLint("ValidFragment")
public class LaporanFragment extends Fragment{
    private   TextView txtTanggalHarian;
    private TextView txtBulan, txtTahun, txtJmlTransaksi, txtPendapatanLaporanPenjualan, txtHargaTotalBarangTerjual,txtDataKosongLapPenjualan;
    private LinearLayout LinearLayoutLapHarian,LinearLayoutLapBulanan,LinearLayoutLapTahunan;
    private SimpleDateFormat dateFormatter;
    private Integer jenisLaporan;
    Calendar newCalendar = Calendar.getInstance();
    int selectedDayV;
    int selectedMonthV;
    int selectedYearV;
    DateRangePickerFragment dateRangePickerFragment;
    private WebView myWebView;
    private SwipeRefreshLayout refreshLaporan;
    private RecyclerView recycleViewBarangTerjual;
    private AdapterRecycleViewBarangTerjual adapterRecycleViewBarangTerjual;
    private List<ListItemBarangTerjual> listItemBarangTerjuals;
    BaseUrlApiModel baseUrlApiModel = new BaseUrlApiModel();
    private String baseUrl=baseUrlApiModel.getBaseURL();
    private String tanggalDari, tanggalSampai;

    public LaporanFragment(Integer jenisLaporan) {this.jenisLaporan = jenisLaporan;}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_laporan_penjualan, container, false);
        selectedDayV=newCalendar.get(Calendar.DAY_OF_MONTH);
        selectedMonthV=newCalendar.get(Calendar.MONTH);
        selectedYearV=newCalendar.get(Calendar.YEAR);

        myWebView = view.findViewById(R.id.webviewLaporan);

        /*LINEAR LAYOUT*/
        LinearLayoutLapBulanan = view.findViewById(R.id.LinearLayoutLapBulanan);
        LinearLayoutLapHarian = view.findViewById(R.id.LinearLayoutLapHarian);
        LinearLayoutLapTahunan = view.findViewById(R.id.LinearLayoutLapTahunan);

        /*TEXT VIEW*/
        txtTanggalHarian=view.findViewById(R.id.txtTanggalHarian);
        txtBulan=view.findViewById(R.id.txtBulan);
        txtTahun=view.findViewById(R.id.txtTahun);
        txtJmlTransaksi=view.findViewById(R.id.txtJmlTransaksi);
        txtPendapatanLaporanPenjualan=view.findViewById(R.id.txtPendapatanLaporanPenjualan);
        txtHargaTotalBarangTerjual=view.findViewById(R.id.txtHargaTotalBarangTerjual);
        txtDataKosongLapPenjualan=view.findViewById(R.id.txtDataKosongLapPenjualan);

        recycleViewBarangTerjual = (RecyclerView) view.findViewById(R.id.recycleViewBarangTerjual);
        recycleViewBarangTerjual.setHasFixedSize(true);
        recycleViewBarangTerjual.setLayoutManager(new LinearLayoutManager(getContext()));

        refreshLaporan = view.findViewById( R.id.refreshLaporan );

        listItemBarangTerjuals = new ArrayList<>();
        adapterRecycleViewBarangTerjual = new AdapterRecycleViewBarangTerjual( listItemBarangTerjuals, getContext());



        if(jenisLaporan==0){
            LinearLayoutLapBulanan.setVisibility(View.GONE);
            LinearLayoutLapTahunan.setVisibility(View.GONE);
            LinearLayoutLapHarian.setVisibility(View.VISIBLE);
            dateFormatter = new SimpleDateFormat("dd MMMM yyyy ", Locale.US);
            tanggalDari=dateFormatter.format(newCalendar.getTime());
            tanggalSampai=dateFormatter.format(newCalendar.getTime());
            txtTanggalHarian.setText(dateFormatter.format(newCalendar.getTime())+" - "+dateFormatter.format(newCalendar.getTime()));

            refreshLaporan.setOnRefreshListener( new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    listItemBarangTerjuals.clear();
                    adapterRecycleViewBarangTerjual.notifyDataSetChanged();
                    loadLaporan(tanggalDari,tanggalSampai,null,null,0);
                }
            } );
        }else if(jenisLaporan==1){
            LinearLayoutLapBulanan.setVisibility(View.VISIBLE);
            LinearLayoutLapTahunan.setVisibility(View.GONE);
            LinearLayoutLapHarian.setVisibility(View.GONE);
            dateFormatter = new SimpleDateFormat("MMMM yyyy ", Locale.US);
            txtBulan.setText(dateFormatter.format(newCalendar.getTime()));

            refreshLaporan.setOnRefreshListener( new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    listItemBarangTerjuals.clear();
                    adapterRecycleViewBarangTerjual.notifyDataSetChanged();
                    loadLaporan(null,null,String.valueOf((selectedMonthV+1)),String.valueOf(selectedYearV),1);
                }
            } );
        }else{
            LinearLayoutLapBulanan.setVisibility(View.GONE);
            LinearLayoutLapTahunan.setVisibility(View.VISIBLE);
            LinearLayoutLapHarian.setVisibility(View.GONE);
            dateFormatter = new SimpleDateFormat("yyyy ", Locale.US);
            txtTahun.setText(dateFormatter.format(newCalendar.getTime()));

            refreshLaporan.setOnRefreshListener( new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    listItemBarangTerjuals.clear();
                    adapterRecycleViewBarangTerjual.notifyDataSetChanged();
                    loadLaporan(null,null,null,String.valueOf(selectedYearV),2);
                }
            } );
        }

        recycleViewBarangTerjual.setAdapter( adapterRecycleViewBarangTerjual );

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        setHasOptionsMenu(true);
        if(jenisLaporan==0){
            getActivity().setTitle("Laporan Harian");
            loadLaporan(tanggalDari,tanggalSampai,null,null,0);
        }else if(jenisLaporan==1){
            getActivity().setTitle("Laporan Bulanan");
            loadLaporan(null,null,String.valueOf((selectedMonthV+1)),String.valueOf(selectedYearV),1);
        }else{
            getActivity().setTitle("Laporan Tahunan");
            loadLaporan(null,null,null,String.valueOf(selectedYearV),2);
        }
    }

    private void showDateDialog(){
        if(jenisLaporan==0){
            dateRangePickerFragment= DateRangePickerFragment.newInstance((DateRangePickerFragment.OnDateRangeSelectedListener) getContext(),false);
            dateRangePickerFragment.setOnDateRangeSelectedListener(new DateRangePickerFragment.OnDateRangeSelectedListener() {
                @Override
                public void onDateRangeSelected(int startDay, int startMonth, int startYear, int endDay, int endMonth, int endYear) {
                    Calendar start = Calendar.getInstance();
                    start.set(startYear, startMonth, startDay);
                    Calendar ends = Calendar.getInstance();
                    ends.set(endYear, endMonth, endDay);
                    tanggalDari=dateFormatter.format(start.getTime());
                    tanggalSampai=dateFormatter.format(ends.getTime());
                    txtTanggalHarian.setText(dateFormatter.format(start.getTime())+" - "+dateFormatter.format(ends.getTime()));
                    loadLaporan(tanggalDari,tanggalSampai,null,null,0);
                }
            });
            dateRangePickerFragment.show(getActivity().getSupportFragmentManager(),"datePicker");
        }else if(jenisLaporan==1){
            MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(getContext(), new MonthPickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(int selectedMonth, int selectedYear) {
                    Calendar newDate = Calendar.getInstance();
                    newDate.set(selectedYear, selectedMonth, Calendar.DAY_OF_MONTH);
                    txtBulan.setText(dateFormatter.format(newDate.getTime()));
                    selectedMonthV=selectedMonth;
                    selectedYearV=selectedYear;
                    loadLaporan(null,null,String.valueOf((selectedMonth+1)),String.valueOf(selectedYear),1);
                }
            }, selectedYearV, selectedMonthV);

            builder.setMinYear(1990)
                    .setMaxYear(2030)
                    .setTitle("Pilih Bulan : ")
                    .build()
                    .show();
        }else {
            MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(getContext(), new MonthPickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(int selectedMonth, int selectedYear) {
                    Calendar newDate = Calendar.getInstance();
                    newDate.set(selectedYear, selectedMonth, Calendar.DAY_OF_YEAR);
                    txtTahun.setText(dateFormatter.format(newDate.getTime()));
                    selectedMonthV=selectedMonth;
                    selectedYearV=selectedYear;
                    loadLaporan(null,null,null,String.valueOf(selectedYear),2);
                }
            }, selectedYearV, selectedMonthV);

            builder.showYearOnly()
                    .setTitle("Pilih Tahun : ")
                    .setYearRange(1990, 2030)
                    .build()
                    .show();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_laporan, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ic_print:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    createWebPrintJob(myWebView);
                }
                return true;
            case R.id.ic_bagikan_laporan:
                bagikan();
                return true;
            case R.id.ic_datepicker:
                showDateDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void createWebPrintJob(WebView webView) {

        PrintAttributes printAttributes = new PrintAttributes.Builder()
                .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                .setMinMargins(PrintAttributes.Margins.NO_MARGINS).build();

        PrintManager printManager = (PrintManager) getActivity()
                .getSystemService(Context.PRINT_SERVICE);

        String namaDocumnent = null;

        if (jenisLaporan==0){
            namaDocumnent = "Laporan_Harian_Periode_"+txtTanggalHarian.getText();
        }else if(jenisLaporan==1){
            namaDocumnent ="Laporan_Bulanan_Periode_"+txtBulan.getText();
        }else {
            namaDocumnent ="Laporan_Tahunan_Periode_"+txtTahun.getText();
        }

        PrintDocumentAdapter printAdapter =
                webView.createPrintDocumentAdapter(namaDocumnent);

        String jobName = getString(R.string.app_name) + "Print Laba Rugi";

        printManager.print(jobName, printAdapter, printAttributes);
    }

    private void bagikan(){
        Picture picture = myWebView.capturePicture();
        Bitmap b = Bitmap.createBitmap(
                picture.getWidth()-900, picture.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        picture.draw(c);
        Uri bmpUri = getBitmapFromDrawable(b);
        final Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/jpg");
        /*final File photoFile = new File(getFilesDir(), "foo.jpg");*/
        shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);

        startActivity(Intent.createChooser(shareIntent, "Share image using"));
    }

    // Method when launching drawable within Glide
    public Uri getBitmapFromDrawable(Bitmap bmp){

        // Store image to default external storage directory
        Uri bmpUri = null;
        File file;
        try {
            if (jenisLaporan==0){
                file =  new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "Laporan_Harian_Periode_"+txtTanggalHarian.getText()+"_"+System.currentTimeMillis()+".jpg");
            }else if(jenisLaporan==1){
                file =  new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "Laporan_Bulanan_Periode_"+txtBulan.getText()+"_"+System.currentTimeMillis()+".jpg");
            }else {
                file =  new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "Laporan_Tahunan_Periode_"+txtTahun.getText()+"_"+System.currentTimeMillis()+".jpg");
            }
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.close();

            bmpUri = FileProvider.getUriForFile(getContext(), "com.codepath.fileprovider", file);

        } catch (IOException e) {
            Log.d("TAG", "getBitmapFromDrawable: "+e);
            e.printStackTrace();
        }
        return bmpUri;
    }


    private void loadLaporan(String dari,String sampai,String bulan,String tahun, Integer jenisLaporan){
        listItemBarangTerjuals.clear();
        adapterRecycleViewBarangTerjual.notifyDataSetChanged();
        Log.d("TAG", "loadLaporan: "+bulan);
        refreshLaporan.setRefreshing(true);
        String API_URL = null;
        if (jenisLaporan==0){
            API_URL="api/transaksi?api=laporan&lap=harian&dari="+dari+"&sampai="+sampai;
            myWebView.loadUrl(baseUrl+"print_laporan?lap=harian&dari="+dari+"&sampai="+sampai);
        }else if(jenisLaporan==1){
            API_URL="api/transaksi?api=laporan&lap=bulanan&bulan="+bulan+"&tahun="+tahun;
            myWebView.loadUrl(baseUrl+"print_laporan?lap=bulanan&bulan="+bulan+"&tahun="+tahun);
            Log.d("TAG", "API: "+API_URL);
        }else{
            API_URL="api/transaksi?api=laporan&lap=tahunan&tahun="+tahun;
            myWebView.loadUrl(baseUrl+"print_laporan?lap=tahunan&tahun="+tahun);
        }
        StringRequest stringRequest = new StringRequest( Request.Method.GET, baseUrl+API_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            final JSONObject jsonObject = new JSONObject(response);
                            DecimalFormat formatter = new DecimalFormat("#,###,###");
                            JSONObject dataObject = jsonObject.getJSONObject("data");

                            if (dataObject.getInt("jml_transaksi")!=0){
                                txtDataKosongLapPenjualan.setVisibility(View.GONE);
                            }else{
                                txtDataKosongLapPenjualan.setVisibility(View.VISIBLE);
                            }

                            txtJmlTransaksi.setText(dataObject.getString("jml_transaksi"));
                            txtPendapatanLaporanPenjualan.setText("Rp. "+formatter.format(Double.parseDouble(dataObject.getString("pendapatan"))));
                            txtHargaTotalBarangTerjual.setText("Rp. "+formatter.format(Double.parseDouble(dataObject.getString("total_harga_semua"))));

                            JSONArray barangTerjual = dataObject.getJSONArray("barangTerjual");
                            for (int i = 0; i<barangTerjual.length(); i++){
                                JSONObject barangTerjualJSONObject = barangTerjual.getJSONObject( i );
                                ListItemBarangTerjual listItemBarangTerjual = new ListItemBarangTerjual(
                                        barangTerjualJSONObject.getString( "nama_barang" ),
                                        barangTerjualJSONObject.getString( "harga_jual_detail" ),
                                        barangTerjualJSONObject.getString( "qty"),
                                        String.valueOf(barangTerjualJSONObject.getInt("qty")*barangTerjualJSONObject.getInt("harga_jual_detail"))
                                );

                                listItemBarangTerjuals.add( listItemBarangTerjual );
                                adapterRecycleViewBarangTerjual.notifyDataSetChanged();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Periksa koneksi & coba lagi", Toast.LENGTH_SHORT).show();
                        }
                        refreshLaporan.setRefreshing( false );
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Periksa koneksi & coba lagi1", Toast.LENGTH_SHORT).show();
                        refreshLaporan.setRefreshing( false );
                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue( getContext() );
        requestQueue.add( stringRequest );
    }
}
