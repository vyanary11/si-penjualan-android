package com.pratamatechnocraft.silaporanpenjualan.Fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import com.pratamatechnocraft.silaporanpenjualan.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;



public class LaporanHarianFragment extends Fragment {

    private Button buttondari, buttonsampai;
    private SimpleDateFormat dateFormatter;
    private DatePickerDialog datePickerDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_laporan_harian, container, false);

        /*DATE PICKER*/
        dateFormatter = new SimpleDateFormat("dd MMMM yyyy ", Locale.US);
        buttondari = (Button) view.findViewById(R.id.buttonDari);
        buttonsampai = (Button) view.findViewById(R.id.buttonSampai);
        buttonsampai.setOnClickListener(new View.OnClickListener(){

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
        getActivity().setTitle("Laporan Harian");
    }

    private void showDateDialog(){
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                buttonsampai.setText(dateFormatter.format(newDate.getTime()));
                buttondari.setText(dateFormatter.format(newDate.getTime()));
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    }
