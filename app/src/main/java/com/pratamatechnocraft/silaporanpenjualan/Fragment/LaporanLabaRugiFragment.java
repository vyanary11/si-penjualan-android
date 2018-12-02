package com.pratamatechnocraft.silaporanpenjualan.Fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class LaporanLabaRugiFragment extends Fragment {
    private Button buttontgl;
    private SimpleDateFormat dateFormatter;
    private RecyclerView recycleViewBiayaPengeluaran;
    private AdapterRecycleViewBiayaPengeluaran adapterRecycleViewBiayaPengeluaran;
    private SwipeRefreshLayout refreshLabaRugi;
    private SessionManager sessionManager;
    private ProgressDialog progress;
    private AlertDialog dialog;
    private TextView txtIncome, txtExpense, txtNetIncome, txtPendapatan, txtLabaKotor, txtJumlahPengeluaranLainnya, txtTotalBiaya, txtLabaBersih;

    private List<ListItemBiaya> listItemBiayas;

    BaseUrlApiModel baseUrlApiModel = new BaseUrlApiModel();
    private String baseUrl=baseUrlApiModel.getBaseURL();
    private static final String API_URL = "api/kategori?api=kategoriall";
    Calendar newCalendar = Calendar.getInstance();
    int selectedMonthV;
    int selectedYearV;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_laporan_laba_rugi, container, false);
        refreshLabaRugi = (SwipeRefreshLayout) view.findViewById(R.id.refreshLabaRugi);
        recycleViewBiayaPengeluaran = (RecyclerView) view.findViewById(R.id.recycleViewBiayaPengeluaran);
        selectedMonthV=newCalendar.get(Calendar.MONTH);
        selectedYearV=newCalendar.get(Calendar.YEAR);

        /*TEXT VIEW*/
        txtIncome = (TextView) view.findViewById(R.id.txtIncome);
        txtExpense = (TextView) view.findViewById(R.id.txtExpense);
        txtNetIncome = (TextView) view.findViewById(R.id.txtNetIncome);
        txtPendapatan = (TextView) view.findViewById(R.id.txtPendapatan);
        txtLabaKotor = (TextView) view.findViewById(R.id.txtLabaKotor);
        txtJumlahPengeluaranLainnya = (TextView) view.findViewById(R.id.txtJumlahPengeluaranLainnya);
        txtTotalBiaya = (TextView) view.findViewById(R.id.txtTotalBiaya);
        txtLabaBersih = (TextView) view.findViewById(R.id.txtLabaBersih);

        /*DATE PICKER*/
        dateFormatter = new SimpleDateFormat("MMMM yyyy", Locale.US);
        buttontgl = (Button) view.findViewById(R.id.buttonbulan);
        buttontgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog();
            }
        });

        refreshLabaRugi.setOnRefreshListener( new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadLabaRugi(selectedMonthV, selectedYearV);
            }
        } );

        return view;
    }

        @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Laporan Laba Rugi");
        loadLabaRugi(newCalendar.get(Calendar.MONTH),newCalendar.get(Calendar.YEAR));
    }

    private void showDateDialog() {
        MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(getContext(), new MonthPickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(int selectedMonth, int selectedYear) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(selectedYear, selectedMonth, Calendar.DAY_OF_MONTH);
                buttontgl.setText(dateFormatter.format(newDate.getTime()));
                selectedMonthV=selectedMonth;
                selectedYearV=selectedYear;
                loadLabaRugi(selectedMonth, selectedYear);
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH));

        builder.setMinYear(1990)
                .setMaxYear(2030)
                .setTitle("Pilih Bulan : ")
                .build()
                .show();
    }

    private void loadLabaRugi(int bulan, int tahun){
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
}




