package com.pratamatechnocraft.silaporanpenjualan.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.pratamatechnocraft.silaporanpenjualan.R;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class LaporanTahunanFragment extends Fragment {

    private Button buttontahun;
    private SimpleDateFormat dateFormatter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_laporan_tahunan, container, false);


        /*DATE PICKER*/
        dateFormatter = new SimpleDateFormat("yyyy", Locale.US);
        buttontahun = (Button) view.findViewById(R.id.buttonThn);
        buttontahun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Laporan Tahunan");
    }

    private void showDateDialog() {
        Calendar newCalendar = Calendar.getInstance();
        MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(getContext(), new MonthPickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(int selectedMonth, int selectedYear) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(selectedYear, selectedMonth, Calendar.DAY_OF_YEAR);
                buttontahun.setText(dateFormatter.format(newDate.getTime()));
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH));

        builder.showYearOnly()
                .setYearRange(1990, 2030)
                .build()
                .show();
    }

    }

