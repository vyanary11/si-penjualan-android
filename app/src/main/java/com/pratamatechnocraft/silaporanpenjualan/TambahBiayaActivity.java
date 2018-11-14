package com.pratamatechnocraft.silaporanpenjualan;

import android.app.DatePickerDialog;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TambahBiayaActivity extends AppCompatActivity {
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    private TextView tvDateResult;
    private ImageButton imgButton;
    private TextInputLayout inputLayoutNamaBiaya,inputLayoutJumlahBiaya;
    private EditText inputNamaBiaya,inputJumlahBiaya;
    private Button buttonSimpanTambahBiaya,buttonBatalTambahBiaya;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_biaya);

        Toolbar ToolBarAtas2 = (Toolbar)findViewById(R.id.toolbartambahbiaya);
        setSupportActionBar(ToolBarAtas2);
        ToolBarAtas2.setLogoDescription("Tambah Biaya");
        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_black_24dp);
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.colorIcons), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        buttonSimpanTambahBiaya = findViewById( R.id.buttonSimpanTambahBiaya );
        buttonBatalTambahBiaya = findViewById( R.id.buttonBatalTambahBiaya);

        /*LAYOUT INPUT*/
        inputLayoutNamaBiaya = (TextInputLayout) findViewById(R.id.inputLayoutNamaBiaya);
        inputLayoutJumlahBiaya = (TextInputLayout) findViewById(R.id.inputLayoutJumlahBiaya);

        /*TEXT INPUT*/
        inputNamaBiaya = (EditText) findViewById(R.id.inputNamaBiaya);
        inputJumlahBiaya = (EditText) findViewById(R.id.inputJumlahBiaya);

        /*VALIDASI DATA*/
        inputNamaBiaya.addTextChangedListener( new MyTextWatcher(inputNamaBiaya));
        inputJumlahBiaya.addTextChangedListener( new MyTextWatcher(inputJumlahBiaya));

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        tvDateResult = (TextView) findViewById(R.id.tv_dateresult);
        imgButton = (ImageButton) findViewById(R.id.imagebutton);
        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog();
            }
        });

    }

    private void showDateDialog(){
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                tvDateResult.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.inputNamaBiaya:
                    validateNamaBiaya();
                    break;
                case R.id.inputJumlahBiaya:
                    validateJumlahBiaya();
                    break;
            }
        }
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private boolean validateNamaBiaya() {
        if (inputNamaBiaya.getText().toString().trim().isEmpty()) {
            inputLayoutNamaBiaya.setError("Masukkan Nama Biaya");
            requestFocus(inputNamaBiaya);
            return false;
        } else {
            inputLayoutNamaBiaya.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateJumlahBiaya() {
        if (inputJumlahBiaya.getText().toString().trim().isEmpty()) {
            inputLayoutJumlahBiaya.setError("Masukkan Jumlah Biaya");
            requestFocus(inputJumlahBiaya);
            return false;
        } else {
            inputLayoutJumlahBiaya.setErrorEnabled(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

