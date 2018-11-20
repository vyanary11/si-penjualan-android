package com.pratamatechnocraft.silaporanpenjualan;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pratamatechnocraft.silaporanpenjualan.Model.BaseUrlApiModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class FormBiayaActivity extends AppCompatActivity {
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    private TextView tvDateResult;
    private ImageButton imgButton;
    private ProgressDialog progress;
    private TextInputLayout inputLayoutNamaBiaya,inputLayoutJumlahBiaya;
    private EditText inputNamaBiaya,inputJumlahBiaya;
    private SwipeRefreshLayout refreshFormBiaya;
    private Button buttonSimpanTambahBiaya,buttonBatalTambahBiaya;
    private BaseUrlApiModel baseUrlApiModel = new BaseUrlApiModel();
    private String baseUrl=baseUrlApiModel.getBaseURL();
    private static final String API_URL = "api/biaya";
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_biaya);

        i = getIntent();
        progress = new ProgressDialog(this);
        refreshFormBiaya = findViewById( R.id.refreshFormBiaya );
        Toolbar ToolBarAtas2 = (Toolbar)findViewById(R.id.toolbartambahbiaya);
        ToolBarAtas2.setSubtitleTextColor( ContextCompat.getColor(this, R.color.colorIcons) );
        this.setTitle("Data Biaya");
        setSupportActionBar(ToolBarAtas2);
        if (i.getStringExtra( "type" ).equals( "tambah" )){
            ToolBarAtas2.setSubtitle( "Tambah Biaya" );
        }else if(i.getStringExtra( "type" ).equals( "edit" )){
            ToolBarAtas2.setSubtitle( "Edit Biaya" );
        }
        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_black_24dp);
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.colorIcons), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*DATE PICKER*/
        dateFormatter = new SimpleDateFormat("dd MMMM yyyy HH:mm", Locale.US);

        tvDateResult = (TextView) findViewById(R.id.tv_dateresult);
        imgButton = (ImageButton) findViewById(R.id.imagebutton);
        imgButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                showDateDialog();
            }
        });

        /*BUTTON*/
        buttonSimpanTambahBiaya = findViewById( R.id.buttonSimpanTambahBiaya );
        buttonBatalTambahBiaya = findViewById( R.id.buttonBatalTambahBiaya);

        /*LAYOUT INPUT*/
        inputLayoutNamaBiaya = (TextInputLayout) findViewById(R.id.inputLayoutNamaBiaya);
        inputLayoutJumlahBiaya = (TextInputLayout) findViewById(R.id.inputLayoutJumlahBiaya);

        /*TEXT INPUT*/
        inputNamaBiaya = (EditText) findViewById(R.id.inputNamaBiaya);
        inputJumlahBiaya = (EditText) findViewById(R.id.inputJumlahBiaya);

        if (i.getStringExtra( "type" ).equals( "tambah" )){
            buttonSimpanTambahBiaya.setText("Tambah");
        }else if(i.getStringExtra( "type" ).equals( "edit" )){
            loadTampilEdit(i.getStringExtra( "kdBiaya" ));
            buttonSimpanTambahBiaya.setText("Simpan");
        }

        /*VALIDASI DATA*/
        inputNamaBiaya.addTextChangedListener( new MyTextWatcher(inputNamaBiaya));
        inputJumlahBiaya.addTextChangedListener( new MyTextWatcher(inputJumlahBiaya));

        if (i.getStringExtra( "type" ).equals("tambah")){
            buttonSimpanTambahBiaya.setText("Tambah");
            refreshFormBiaya.setEnabled( false );

        }else if(i.getStringExtra( "type" ).equals("edit")){
            buttonSimpanTambahBiaya.setText("Simpan");
        }

        refreshFormBiaya.setOnRefreshListener( new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadTampilEdit(i.getStringExtra( "kdBiaya" ));
            }
        } );


        /*FUNGSI KLIK*/
        buttonSimpanTambahBiaya.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (i.getStringExtra( "type" ).equals( "tambah" )){
                    if (!validateNamaBiaya() || !validateJumlahBiaya() ) {
                        return;
                    }else {
                        progress.setMessage("Mohon Ditunggu, Sedang diProses.....");
                        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progress.setIndeterminate(false);
                        progress.setCanceledOnTouchOutside(false);
                        prosesTambahBiaya(
                                inputNamaBiaya.getText().toString().trim(),
                                inputJumlahBiaya.getText().toString().trim(),
                                tvDateResult.getText().toString().trim()
                        );
                    }
                }else if(i.getStringExtra( "type" ).equals( "edit" )){
                    if (!validateNamaBiaya() || !validateJumlahBiaya() ){
                        return;
                    }else {
                        progress.setMessage("Mohon Ditunggu, Sedang diProses.....");
                        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progress.setIndeterminate(false);
                        progress.setCanceledOnTouchOutside(false);
                        prosesEditBiaya(
                                inputNamaBiaya.getText().toString().trim(),
                                inputJumlahBiaya.getText().toString().trim(),
                                tvDateResult.getText().toString().trim()
                        );
                    }
                }
            }
        } );

    }

    /*PROSES KE DATABASE*/
    private void prosesEditBiaya(final String namaBiaya, final String jumlahBiaya, final String tanggalBiaya) {
        progress.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, baseUrl+API_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String kode = jsonObject.getString("kode");
                    if (kode.equals("1")) {
                        finish();
                        Toast.makeText(FormBiayaActivity.this, "Berhasil Edit Biaya", Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(FormBiayaActivity.this, "Gagal Edit Biaya", Toast.LENGTH_SHORT).show();
                    }
                    progress.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d( "TAG", e.toString() );
                    Toast.makeText(FormBiayaActivity.this, "Periksa koneksi & coba lagi", Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                }
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(FormBiayaActivity.this, "Periksa koneksi & coba lagi", Toast.LENGTH_SHORT).show();
                progress.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("kd_biaya", i.getStringExtra( "kdBiaya" ));
                params.put("nama_biaya", namaBiaya);
                params.put("jumlah_biaya", jumlahBiaya);
                params.put("tgl_biaya", tanggalBiaya);
                params.put( "api", "edit" );
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void prosesTambahBiaya(final String namaBiaya, final String jumlahBiaya, final String tanggalBiaya) {
        progress.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, baseUrl+API_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String kode = jsonObject.getString("kode");
                    if (kode.equals("1")) {
                        finish();
                        Toast.makeText(FormBiayaActivity.this, "Berhasil Tambah Biaya", Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(FormBiayaActivity.this, "Gagal Tambah Biaya", Toast.LENGTH_SHORT).show();
                    }
                    progress.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d( "TAG", e.toString() );
                    Toast.makeText(FormBiayaActivity.this, "Periksa koneksi & coba lagi", Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                }
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(FormBiayaActivity.this, "Periksa koneksi & coba lagi1", Toast.LENGTH_SHORT).show();
                progress.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nama_biaya", namaBiaya);
                params.put("jumlah_biaya", jumlahBiaya);
                params.put("tgl_biaya", tanggalBiaya);
                params.put( "api", "tambah" );
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    private void loadTampilEdit(String kdBiaya){
        refreshFormBiaya.setRefreshing(true);
        StringRequest stringRequest = new StringRequest( Request.Method.GET, baseUrl+API_URL+"?api=biayadetail&kd_biaya="+kdBiaya,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            final JSONObject biayadetail = new JSONObject(response);
                            inputNamaBiaya.setText( biayadetail.getString( "nama_biaya" ) );
                            tvDateResult.setText( biayadetail.getString( "tgl_biaya" ) );
                            inputJumlahBiaya.setText( biayadetail.getString( "jumlah_biaya" ) );
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(FormBiayaActivity.this, "Periksa koneksi & coba lagi", Toast.LENGTH_SHORT).show();
                        }
                        refreshFormBiaya.setRefreshing( false );
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(FormBiayaActivity.this, "Periksa koneksi & coba lagi1", Toast.LENGTH_SHORT).show();
                        refreshFormBiaya.setRefreshing( false );
                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue( FormBiayaActivity.this );
        requestQueue.add( stringRequest );
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

    /*INPUT*/

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
}

