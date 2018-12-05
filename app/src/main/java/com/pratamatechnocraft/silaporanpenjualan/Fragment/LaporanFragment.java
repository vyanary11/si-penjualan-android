package com.pratamatechnocraft.silaporanpenjualan.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pratamatechnocraft.silaporanpenjualan.DetailBarangActivity;
import com.pratamatechnocraft.silaporanpenjualan.FormBarangActivity;
import com.pratamatechnocraft.silaporanpenjualan.MainActivity;
import com.pratamatechnocraft.silaporanpenjualan.R;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

@SuppressLint("ValidFragment")
public class LaporanFragment extends Fragment implements DateRangePickerFragment.OnDateRangeSelectedListener{
    private TextView txtTanggalHarian, txtBulan, txtTahun;
    private LinearLayout LinearLayoutLapHarian,LinearLayoutLapBulanan,LinearLayoutLapTahunan;
    private SimpleDateFormat dateFormatter;
    private DatePickerDialog datePickerDialog;
    private Integer jenisLaporan;
    Context thiscontext;
    Calendar newCalendar = Calendar.getInstance();
    int selectedDayV;
    int selectedMonthV;
    int selectedYearV;
    DateRangePickerFragment dateRangePickerFragment;

    public LaporanFragment(Integer jenisLaporan) {this.jenisLaporan = jenisLaporan;}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        thiscontext = container.getContext();
        dateRangePickerFragment= DateRangePickerFragment.newInstance(,false);
        View view = inflater.inflate(R.layout.fragment_laporan_penjualan, container, false);
        selectedDayV=newCalendar.get(Calendar.DAY_OF_MONTH);
        selectedMonthV=newCalendar.get(Calendar.MONTH);
        selectedYearV=newCalendar.get(Calendar.YEAR);

        /*LINEAR LAYOUT*/
        LinearLayoutLapBulanan = view.findViewById(R.id.LinearLayoutLapBulanan);
        LinearLayoutLapHarian = view.findViewById(R.id.LinearLayoutLapHarian);
        LinearLayoutLapTahunan = view.findViewById(R.id.LinearLayoutLapTahunan);

        /*TEXT VIEW*/
        txtTanggalHarian=view.findViewById(R.id.txtTanggalHarian);
        txtBulan=view.findViewById(R.id.txtBulan);
        txtTahun=view.findViewById(R.id.txtTahun);

        if(jenisLaporan==0){
            LinearLayoutLapBulanan.setVisibility(View.GONE);
            LinearLayoutLapTahunan.setVisibility(View.GONE);
            LinearLayoutLapHarian.setVisibility(View.VISIBLE);
            dateFormatter = new SimpleDateFormat("dd MMMM yyyy ", Locale.US);
        }else if(jenisLaporan==1){
            LinearLayoutLapBulanan.setVisibility(View.VISIBLE);
            LinearLayoutLapTahunan.setVisibility(View.GONE);
            LinearLayoutLapHarian.setVisibility(View.GONE);
            dateFormatter = new SimpleDateFormat("MMMM yyyy ", Locale.US);
        }else{
            LinearLayoutLapBulanan.setVisibility(View.GONE);
            LinearLayoutLapTahunan.setVisibility(View.VISIBLE);
            LinearLayoutLapHarian.setVisibility(View.GONE);
            dateFormatter = new SimpleDateFormat("yyyy ", Locale.US);
        }

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        setHasOptionsMenu(true);
        if(jenisLaporan==0){
            getActivity().setTitle("Laporan Harian");
        }else if(jenisLaporan==1){
            getActivity().setTitle("Laporan Bulanan");
        }else{
            getActivity().setTitle("Laporan Tahunan");
        }
    }

    private void showDateDialog(){
        if(jenisLaporan==0){
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

                return true;
            case R.id.ic_datepicker:
                showDateDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDateRangeSelected(int startDay, int startMonth, int startYear, int endDay, int endMonth, int endYear) {

    }
}
