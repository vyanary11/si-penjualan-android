package com.pratamatechnocraft.silaporanpenjualan.Fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pratamatechnocraft.silaporanpenjualan.R;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

@SuppressLint("ValidFragment")
public class LaporanFragment extends Fragment{
    private   TextView txtTanggalHarian;
    private TextView txtBulan, txtTahun;
    private LinearLayout LinearLayoutLapHarian,LinearLayoutLapBulanan,LinearLayoutLapTahunan;
    private SimpleDateFormat dateFormatter;
    private Integer jenisLaporan;
    Calendar newCalendar = Calendar.getInstance();
    int selectedDayV;
    int selectedMonthV;
    int selectedYearV;
    DateRangePickerFragment dateRangePickerFragment;

    public LaporanFragment(Integer jenisLaporan) {this.jenisLaporan = jenisLaporan;}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
            txtTanggalHarian.setText(dateFormatter.format(newCalendar.getTime())+" - "+dateFormatter.format(newCalendar.getTime()));
        }else if(jenisLaporan==1){
            LinearLayoutLapBulanan.setVisibility(View.VISIBLE);
            LinearLayoutLapTahunan.setVisibility(View.GONE);
            LinearLayoutLapHarian.setVisibility(View.GONE);
            dateFormatter = new SimpleDateFormat("MMMM yyyy ", Locale.US);
            txtBulan.setText(dateFormatter.format(newCalendar.getTime()));
        }else{
            LinearLayoutLapBulanan.setVisibility(View.GONE);
            LinearLayoutLapTahunan.setVisibility(View.VISIBLE);
            LinearLayoutLapHarian.setVisibility(View.GONE);
            dateFormatter = new SimpleDateFormat("yyyy ", Locale.US);
            txtTahun.setText(dateFormatter.format(newCalendar.getTime())
            );
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
            dateRangePickerFragment= DateRangePickerFragment.newInstance((DateRangePickerFragment.OnDateRangeSelectedListener) getContext(),false);
            dateRangePickerFragment.setOnDateRangeSelectedListener(new DateRangePickerFragment.OnDateRangeSelectedListener() {
                @Override
                public void onDateRangeSelected(int startDay, int startMonth, int startYear, int endDay, int endMonth, int endYear) {
                    Calendar start = Calendar.getInstance();
                    start.set(startYear, startMonth, startDay);
                    Calendar ends = Calendar.getInstance();
                    ends.set(endYear, endMonth, endDay);
                    txtTanggalHarian.setText(dateFormatter.format(start.getTime())+" - "+dateFormatter.format(ends.getTime()));
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
}
