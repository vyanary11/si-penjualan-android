package com.pratamatechnocraft.silaporanpenjualan.Fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
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
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pratamatechnocraft.silaporanpenjualan.Adapter.AdapterRecycleViewBiayaPengeluaran;
import com.pratamatechnocraft.silaporanpenjualan.Adapter.AdapterRecycleViewDataKategoriBarang;
import com.pratamatechnocraft.silaporanpenjualan.Model.BaseUrlApiModel;
import com.pratamatechnocraft.silaporanpenjualan.Model.ListItemBiaya;
import com.pratamatechnocraft.silaporanpenjualan.Model.ListItemDataKategoriBarang;
import com.pratamatechnocraft.silaporanpenjualan.R;
import com.pratamatechnocraft.silaporanpenjualan.Service.SessionManager;
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

public class LaporanLabaRugiFragment extends Fragment {
    private SimpleDateFormat dateFormatter;
    private RecyclerView recycleViewBiayaPengeluaran;
    private AdapterRecycleViewBiayaPengeluaran adapterRecycleViewBiayaPengeluaran;
    private SwipeRefreshLayout refreshLabaRugi;
    private SessionManager sessionManager;
    private ProgressDialog progress;
    private AlertDialog dialog;
    private TextView txtBulanLabaRugi, txtIncome, txtExpense, txtNetIncome, txtPendapatan, txtLabaKotor, txtJumlahPengeluaranLainnya, txtTotalBiaya, txtLabaBersih;

    private List<ListItemBiaya> listItemBiayas;

    BaseUrlApiModel baseUrlApiModel = new BaseUrlApiModel();
    private String baseUrl=baseUrlApiModel.getBaseURL();
    private static final String API_URL = "api/kategori?api=kategoriall";
    Calendar newCalendar = Calendar.getInstance();
    int selectedMonthV;
    int selectedYearV;
    private WebView myWebView;
    private LinearLayout linearLayoutBackgroundLabaRugi;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_laporan_laba_rugi, container, false);
        refreshLabaRugi = (SwipeRefreshLayout) view.findViewById(R.id.refreshLabaRugi);
        recycleViewBiayaPengeluaran = (RecyclerView) view.findViewById(R.id.recycleViewBiayaPengeluaran);
        selectedMonthV=newCalendar.get(Calendar.MONTH);
        selectedYearV=newCalendar.get(Calendar.YEAR);

        myWebView = view.findViewById(R.id.webviewLaporanLaba);
        linearLayoutBackgroundLabaRugi = view.findViewById(R.id.linearLayoutBackgroundLabaRugi);

        /*TEXT VIEW*/
        txtIncome = (TextView) view.findViewById(R.id.txtIncome);
        txtExpense = (TextView) view.findViewById(R.id.txtExpense);
        txtNetIncome = (TextView) view.findViewById(R.id.txtNetIncome);
        txtPendapatan = (TextView) view.findViewById(R.id.txtPendapatan);
        txtLabaKotor = (TextView) view.findViewById(R.id.txtLabaKotor);
        txtJumlahPengeluaranLainnya = (TextView) view.findViewById(R.id.txtJumlahPengeluaranLainnya);
        txtTotalBiaya = (TextView) view.findViewById(R.id.txtTotalBiaya);
        txtLabaBersih = (TextView) view.findViewById(R.id.txtLabaBersih);
        txtBulanLabaRugi = (TextView) view.findViewById(R.id.txtBulanLabaRugi);

        /*DATE PICKER*/
        dateFormatter = new SimpleDateFormat("MMMM yyyy", Locale.US);

        refreshLabaRugi.setOnRefreshListener( new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadLabaRugi(selectedMonthV, selectedYearV);
            }
        } );

        txtBulanLabaRugi.setText(dateFormatter.format(newCalendar.getTime()));

        return view;
    }

        @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
            selectedMonthV=newCalendar.get(Calendar.MONTH);
            selectedYearV=newCalendar.get(Calendar.YEAR);
        getActivity().setTitle("Laporan Laba Rugi");
        setHasOptionsMenu(true);
        loadLabaRugi(selectedMonthV,selectedYearV);
    }

    private void showDateDialog() {
        MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(getContext(), new MonthPickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(int selectedMonth, int selectedYear) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(selectedYear, selectedMonth, Calendar.DAY_OF_MONTH);
                txtBulanLabaRugi.setText(dateFormatter.format(newDate.getTime()));
                selectedMonthV=selectedMonth;
                selectedYearV=selectedYear;
                loadLabaRugi(selectedMonth, selectedYear);
            }
        }, selectedYearV, selectedMonthV);

        builder.setMinYear(1990)
                .setMaxYear(2030)
                .setTitle("Pilih Bulan : ")
                .build()
                .show();
    }

    private void loadLabaRugi(int bulan, int tahun){
        myWebView.loadUrl(baseUrl+"print_laba_rugi?bulan="+(bulan+1)+"&tahun="+tahun);
        Log.d("BULAN", "loadLabaRugi: "+bulan);
        refreshLabaRugi.setRefreshing( true );
        listItemBiayas = new ArrayList<>();
        StringRequest stringRequest = new StringRequest( Request.Method.GET, baseUrl+"api/transaksi?api=laporan&bulan="+(bulan+1)+"&tahun="+tahun+"&lap=laplabarugi",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            JSONObject dataObject = jsonObject.getJSONObject("data");
                            DecimalFormat formatter = new DecimalFormat("#,###,###");

                            if(dataObject.getInt("net_income")<0){
                                linearLayoutBackgroundLabaRugi.setBackgroundResource(R.color.red_500);
                            }else{
                                linearLayoutBackgroundLabaRugi.setBackgroundResource(R.color.light_blue_A100);
                            }

                            txtIncome.setText("Rp. "+formatter.format(Double.parseDouble(dataObject.getString("income"))));
                            txtExpense.setText(String.valueOf("Rp. "+formatter.format(Double.parseDouble(dataObject.getString("totalbiaya")))));
                            txtJumlahPengeluaranLainnya.setText("Rp. "+formatter.format(Double.parseDouble(dataObject.getString("expense"))));
                            txtPendapatan.setText("Rp. "+formatter.format(Double.parseDouble(dataObject.getString("income"))));
                            txtLabaKotor.setText("Rp. "+formatter.format(Double.parseDouble(dataObject.getString("income"))));
                            txtNetIncome.setText("Rp. "+formatter.format(Double.parseDouble(dataObject.getString("net_income"))));
                            txtLabaBersih.setText("Rp. "+formatter.format(Double.parseDouble(dataObject.getString("net_income"))));
                            txtTotalBiaya.setText("Rp. "+formatter.format(Double.parseDouble(dataObject.getString("totalbiaya"))));

                            JSONArray bebanBiaya = dataObject.getJSONArray("data_biaya");
                            for (int i = 0; i<bebanBiaya.length(); i++){
                                JSONObject biayaPengeluaran = bebanBiaya.getJSONObject( i );

                                ListItemBiaya listItemBiaya = new ListItemBiaya(
                                        biayaPengeluaran.getString( "kd_biaya"),
                                        biayaPengeluaran.getString( "nama_biaya" ),
                                        "Rp. "+formatter.format(Double.parseDouble(biayaPengeluaran.getString( "jumlah_biaya" ))),
                                        biayaPengeluaran.getString( "tgl_biaya" )
                                );

                                listItemBiayas.add( listItemBiaya );
                            }

                            refreshLabaRugi.setRefreshing( false );
                            setUpRecycleView();
                        }catch (JSONException e){
                            e.printStackTrace();
                            refreshLabaRugi.setRefreshing( false );
                            setUpRecycleView();
                            listItemBiayas.clear();
                            adapterRecycleViewBiayaPengeluaran.notifyDataSetChanged();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        refreshLabaRugi.setRefreshing( false );
                        setUpRecycleView();
                        listItemBiayas.clear();
                        adapterRecycleViewBiayaPengeluaran.notifyDataSetChanged();
                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue( getContext() );
        requestQueue.add( stringRequest );

    }

    private void setUpRecycleView(){
        recycleViewBiayaPengeluaran.setHasFixedSize(true);
        recycleViewBiayaPengeluaran.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterRecycleViewBiayaPengeluaran = new AdapterRecycleViewBiayaPengeluaran( listItemBiayas, getContext());
        recycleViewBiayaPengeluaran.setAdapter( adapterRecycleViewBiayaPengeluaran );
        adapterRecycleViewBiayaPengeluaran.notifyDataSetChanged();
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

        PrintDocumentAdapter printAdapter =
                webView.createPrintDocumentAdapter("Laporan_Laba_Rugi_Periode_"+txtBulanLabaRugi.getText());

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
            file =  new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "Laporan_Laba_Rugi_Periode_"+txtBulanLabaRugi.getText()+"_"+System.currentTimeMillis()+".jpg");
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
}




