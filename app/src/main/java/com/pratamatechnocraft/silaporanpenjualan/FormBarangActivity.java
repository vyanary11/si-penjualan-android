package com.pratamatechnocraft.silaporanpenjualan;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.pratamatechnocraft.silaporanpenjualan.Adapter.AdapterRecycleViewDataKategoriBarang;
import com.pratamatechnocraft.silaporanpenjualan.Adapter.DBDataSourceKeranjang;
import com.pratamatechnocraft.silaporanpenjualan.Fragment.DataKategoriBarangFragment;
import com.pratamatechnocraft.silaporanpenjualan.Model.BaseUrlApiModel;
import com.pratamatechnocraft.silaporanpenjualan.Model.ListItemDataKategoriBarang;
import com.pratamatechnocraft.silaporanpenjualan.Model.ModelKeranjang;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class FormBarangActivity extends AppCompatActivity {

    private Button btnPilihFotoTambahBarang,buttonSimpanTambahBarang,buttonBatalTambahBarang;
    private ImageButton imageButtonPilihKategori;
    private RelativeLayout adaGambar, tidakAdaGambar;
    private TextInputLayout inputLayoutNamaBarang,inputLayoutHargaJual,inputLayoutHargaBeli,inputLayoutStok;
    private EditText inputBarang,inputHargaJual,inputHargaBeli,inputStok;
    private TextView txtKdKategoriBarangForm,txtNamaKategoriBarangForm;
    private BottomSheetDialog bottomSheetDialog;
    private Bitmap bitmap;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Bitmap mImageBitmap;
    private Button galeri,kamera;
    private TextView txtFotoTambahBarang, txtHurufDepanBarangForm;
    private ProgressDialog progress;
    private CircleImageView fotoTambahBarang,fotoTambahBarang1;
    private SwipeRefreshLayout refreshFormBarang;
    private BaseUrlApiModel baseUrlApiModel = new BaseUrlApiModel();
    private String baseUrl=baseUrlApiModel.getBaseURL();
    private static final String API_URL = "api/barang";
    Intent i;
    private DBDataSourceKeranjang dbDataSourceKeranjang;
    private ModelKeranjang modelKeranjang;
    AlertDialog dialog;
    LayoutInflater inflater;
    View dialogView;

    private RecyclerView recyclerViewDataKategoriBarang;
    private AdapterRecycleViewDataKategoriBarang adapterDataKategoriBarang;
    LinearLayout noDataKategoriBarang, koneksiDataKategoriBarang;
    SwipeRefreshLayout refreshDataKategoriBarang;
    ImageButton imageButtonTambahKategoriBarang;
    ProgressBar progressBarDataKategoriBarang;
    Button cobaLagiDataKategoriBarang;
    private Boolean statusBtn = false;
    private TextInputLayout inputLayoutNamaKategori;
    private EditText inputNamaKategori;

    private List<ListItemDataKategoriBarang> listItemDataKategorisBarangs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_barang );

        i = getIntent();
        progress = new ProgressDialog(this);
        refreshFormBarang = findViewById( R.id.refreshFormBarang );
        Toolbar ToolBarAtas2 = (Toolbar)findViewById(R.id.toolbartambahbarang);
        ToolBarAtas2.setSubtitleTextColor( ContextCompat.getColor(this, R.color.colorIcons) );
        this.setTitle("Data Barang");
        setSupportActionBar(ToolBarAtas2);
        if (i.getStringExtra( "type" ).equals( "tambah" )){
            ToolBarAtas2.setSubtitle( "Tambah Barang" );
        }else if(i.getStringExtra( "type" ).equals( "edit" )){
            ToolBarAtas2.setSubtitle( "Edit Barang" );
        }
        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_black_24dp);
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.colorIcons), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        /*TEXTVIEW*/
        txtKdKategoriBarangForm =  (TextView) findViewById(R.id.txtKdKategoriBarangForm);
        txtNamaKategoriBarangForm =  (TextView) findViewById(R.id.txtNamaKategoriBarangForm);

        /*BUTTON*/
        btnPilihFotoTambahBarang = findViewById( R.id.buttonPilihFotoTambahBarang );
        buttonSimpanTambahBarang = findViewById( R.id.buttonSimpanTambahBarang );
        buttonBatalTambahBarang = findViewById( R.id.buttonBatalTambahBarang);

        /*IMAGEBUTTON*/
        imageButtonPilihKategori = findViewById( R.id.imageButtonPilihKategori);

        /*LAYOUT INPUT*/
        inputLayoutNamaBarang = (TextInputLayout) findViewById(R.id.inputLayoutNamaBarang);
        inputLayoutHargaJual = (TextInputLayout) findViewById(R.id.inputLayoutHargaJual);
        inputLayoutHargaBeli = (TextInputLayout) findViewById(R.id.inputLayoutHargaBeli);
        inputLayoutStok = (TextInputLayout) findViewById(R.id.inputLayoutStok);
        /*TEXT INPUT*/
        inputBarang = (EditText) findViewById(R.id.inputBarang);
        inputHargaJual = (EditText) findViewById(R.id.inputHargaJual);
        inputHargaBeli = (EditText) findViewById(R.id.inputHargaBeli);
        inputStok  = (EditText) findViewById(R.id.inputStok);

        if (i.getStringExtra( "type" ).equals( "tambah" )){
            buttonSimpanTambahBarang.setText("Tambah");
        }else if(i.getStringExtra( "type" ).equals( "edit" )){
            loadTampilEdit(i.getStringExtra( "kdBarang" ));
            buttonSimpanTambahBarang.setText("Simpan");
            inputLayoutStok.setVisibility( View.GONE );
        }

        /*IMAGE*/
        adaGambar=findViewById( R.id.adaGambarFormBarang );
        tidakAdaGambar=findViewById( R.id.tidakAdaGambarFormBarang );
        fotoTambahBarang = (CircleImageView) findViewById( R.id.fotoTambahBarang );
        fotoTambahBarang1 = (CircleImageView) findViewById( R.id.fotoTambahBarang1 );
        txtFotoTambahBarang = (TextView) findViewById( R.id.txtFotoTambahBarang );
        txtHurufDepanBarangForm = (TextView) findViewById( R.id.hurufDepanBarangDetail );
        txtFotoTambahBarang.setText( "" );

        /*VALIDASI DATA*/
        inputBarang.addTextChangedListener( new FormBarangActivity.MyTextWatcher( inputBarang) );
        inputHargaJual.addTextChangedListener( new FormBarangActivity.MyTextWatcher( inputHargaJual) );
        inputHargaBeli.addTextChangedListener( new FormBarangActivity.MyTextWatcher( inputHargaBeli) );
        inputStok.addTextChangedListener( new FormBarangActivity.MyTextWatcher( inputStok) );

        if (i.getStringExtra( "type" ).equals("tambah")){
            buttonSimpanTambahBarang.setText("Tambah");
            refreshFormBarang.setEnabled( false );
        }else if(i.getStringExtra( "type" ).equals("edit")){
            buttonSimpanTambahBarang.setText("Simpan");
        }

        refreshFormBarang.setOnRefreshListener( new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadTampilEdit(i.getStringExtra( "kdBarang" ));
            }
        } );


        /*FUNGSI KLIK*/
        btnPilihFotoTambahBarang.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                klikPilihFotoTambahBarang();
            }
        } );
        buttonSimpanTambahBarang.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (i.getStringExtra( "type" ).equals( "tambah" )){
                    if (!validateNamaBarang() || !validateHargaJual() || !validateHargaBeli() || !validateStok() ) {
                        return;
                    }else {
                        progress.setMessage("Mohon Ditunggu, Sedang diProses.....");
                        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progress.setIndeterminate(false);
                        progress.setCanceledOnTouchOutside(false);
                        prosesTambahBarang(
                                inputBarang.getText().toString().trim(),
                                inputHargaJual.getText().toString().trim(),
                                inputHargaBeli.getText().toString().trim(),
                                inputStok.getText().toString().trim(),
                                txtKdKategoriBarangForm.getText().toString().trim(),
                                txtFotoTambahBarang.getText().toString().trim()
                        );
                    }
                }else if(i.getStringExtra( "type" ).equals( "edit" )){
                    if (!validateNamaBarang() || !validateHargaJual() || !validateHargaBeli() ) {
                        return;
                    }else {
                        progress.setMessage("Mohon Ditunggu, Sedang diProses.....");
                        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progress.setIndeterminate(false);
                        progress.setCanceledOnTouchOutside(false);
                        prosesEditBarang(
                                inputBarang.getText().toString().trim(),
                                inputHargaJual.getText().toString().trim(),
                                inputHargaBeli.getText().toString().trim(),
                                inputStok.getText().toString().trim(),
                                txtKdKategoriBarangForm.getText().toString().trim(),
                                txtFotoTambahBarang.getText().toString().trim()
                        );
                    }
                }
            }
        } );

        imageButtonPilihKategori.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new AlertDialog.Builder(FormBarangActivity.this).create();
                inflater = dialog.getLayoutInflater();
                dialogView = inflater.inflate(R.layout.fragment_data_kategori_barang, null);
                dialog.setView(dialogView);
                dialog.setCancelable(true);
                dialog.setTitle("Pilih Kategori : ");

                inisialisasiFragmentKategori();

                dialog.show();
            }
        } );

    }

    /*PROSES KE DATABASE*/
    private void prosesEditBarang(final String namaBarang, final String hargaJual, final String hargaBeli, final String stokBarang, final String kategori, final String fotoBarang) {
        progress.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, baseUrl+API_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String kode = jsonObject.getString("kode");
                    if (kode.equals("1")) {
                        finish();
                        Toast.makeText(FormBarangActivity.this, "Berhasil Edit Barang", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(FormBarangActivity.this, "Gagal Edit Barang", Toast.LENGTH_SHORT).show();
                    }
                    progress.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d( "TAG", e.toString() );
                    Toast.makeText(FormBarangActivity.this, "Periksa koneksi & coba lagi", Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                }
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(FormBarangActivity.this, "Periksa koneksi & coba lagi1", Toast.LENGTH_SHORT).show();
                progress.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("kd_barang", i.getStringExtra( "kdBarang" ));
                params.put("nama_barang", namaBarang);
                params.put("harga_jual", hargaJual);
                params.put("harga_beli", hargaBeli);
                params.put("stok", stokBarang);
                params.put("kd_kategori", kategori);
                params.put("gambar_barang", fotoBarang);
                params.put("api", "edit");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void prosesTambahBarang(final String namaBarang, final String hargaJual, final String hargaBeli, final String stokBarang, final String kategori, final String fotoBarang) {
        progress.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, baseUrl+API_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String kode = jsonObject.getString("kode");
                    if (kode.equals("1")) {
                        if(i.getStringExtra( "typedua" ).equals( "keranjang" )){
                            dbDataSourceKeranjang = new DBDataSourceKeranjang( FormBarangActivity.this );
                            dbDataSourceKeranjang.open();
                            if (i.getStringExtra( "typetiga" ).equals( "penjualan" )){
                                modelKeranjang = dbDataSourceKeranjang.createModelKeranjang(
                                        "",
                                        namaBarang,
                                        hargaJual,
                                        fotoBarang,
                                        "1",
                                        stokBarang
                                );
                            }else{
                                modelKeranjang = dbDataSourceKeranjang.createModelKeranjang(
                                        "",
                                        namaBarang,
                                        hargaBeli,
                                        fotoBarang,
                                        "1",
                                        stokBarang
                                );
                            }
                        }
                        finish();
                        Toast.makeText(FormBarangActivity.this, "Berhasil Menambahkan Barang", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(FormBarangActivity.this, "Gagal Menambahkan Barang", Toast.LENGTH_SHORT).show();
                    }
                    progress.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d( "TAG", e.toString() );
                    Toast.makeText(FormBarangActivity.this, "Periksa koneksi & coba lagi", Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                }
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.d( "TAG", error.toString() );
                /*Log.d(TAG, error.printStackTrace() );*/
                Toast.makeText(FormBarangActivity.this, "Periksa koneksi & coba lagi1", Toast.LENGTH_SHORT).show();
                progress.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nama_barang", namaBarang);
                params.put("harga_jual", hargaJual);
                params.put("harga_beli", hargaBeli);
                params.put("stok", stokBarang);
                params.put("kd_kategori", kategori);
                params.put("gambar_barang", fotoBarang);
                params.put("api", "tambah");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void loadTampilEdit(String kdBarang){
        refreshFormBarang.setRefreshing(true);
        StringRequest stringRequest = new StringRequest( Request.Method.GET, baseUrl+API_URL+"?api=barangdetail&kd_barang="+kdBarang,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            final JSONObject barangdetail = new JSONObject(response);
                            inputBarang.setText( barangdetail.getString( "nama_barang" ) );
                            inputHargaJual.setText( barangdetail.getString( "harga_jual" ) );
                            inputHargaBeli.setText( barangdetail.getString( "harga_beli" ) );
                            inputStok.setText( barangdetail.getString( "stok" ) );
                            if (barangdetail.getString( "gambar_barang" ).equals( "" )){
                                adaGambar.setVisibility( View.GONE );
                                tidakAdaGambar.setVisibility( View.VISIBLE );
                                setTidakAdaGambar(barangdetail.getString( "nama_barang" ));
                            }else{
                                adaGambar.setVisibility( View.VISIBLE );
                                tidakAdaGambar.setVisibility( View.GONE );
                                Glide.with(FormBarangActivity.this)
                                        // LOAD URL DARI INTERNET
                                        .load(baseUrl+String.valueOf( barangdetail.getString( "gambar_barang" )  ))
                                        // LOAD GAMBAR AWAL SEBELUM GAMBAR UTAMA MUNCUL, BISA DARI LOKAL DAN INTERNET
                                        .into(fotoTambahBarang);
                                txtFotoTambahBarang.setText( "ada" );
                            }
                            txtKdKategoriBarangForm.setText( barangdetail.getString( "kd_kategori" ) );
                            txtNamaKategoriBarangForm.setText( barangdetail.getString( "nama_kategori" ) );
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(FormBarangActivity.this, "Periksa koneksi & coba lagi", Toast.LENGTH_SHORT).show();
                        }
                        refreshFormBarang.setRefreshing( false );
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(FormBarangActivity.this, "Periksa koneksi & coba lagi1", Toast.LENGTH_SHORT).show();
                        refreshFormBarang.setRefreshing( false );
                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue( FormBarangActivity.this );
        requestQueue.add( stringRequest );
    }
    /*PROSES KE DATABASE*/

    /*FOTO*/
    private void klikPilihFotoTambahBarang() {
        View view = getLayoutInflater().inflate(R.layout.fragment_bottom_sheet_dialog_tambah_foto, null);
        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(view);

        galeri = view.findViewById( R.id.galeri1 );
        kamera = view.findViewById( R.id.kamera1 );
        galeri.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
                pilihFotoTambahBarang();
            }
        } );
        kamera.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
                takePicture();
            }
        } );

        bottomSheetDialog.show();
    }
    private void pilihFotoTambahBarang(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent,"Pilih Foto"),1);
    }
    private void takePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult( requestCode, resultCode, data );
        if (requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData() !=null){
            Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap( getContentResolver(),filePath );
                fotoTambahBarang.setImageBitmap( bitmap );
                txtFotoTambahBarang.setText( getStringImage( bitmap ) );
                Log.d("COBA", "onActivityResult: "+txtFotoTambahBarang.getText());
                adaGambar.setVisibility( View.VISIBLE );
                tidakAdaGambar.setVisibility( View.GONE );
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            mImageBitmap = (Bitmap) extras.get("data");
            fotoTambahBarang.setImageBitmap(mImageBitmap);
            txtFotoTambahBarang.setText( getStringImage( mImageBitmap ) );
            Log.d("COBA", "onActivityResult: "+txtFotoTambahBarang.getText());
            adaGambar.setVisibility( View.VISIBLE );
            tidakAdaGambar.setVisibility( View.GONE );
        }
    }
    private String getStringImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(  );
        bitmap.compress( Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream );
        byte[] imageByteArray =  byteArrayOutputStream.toByteArray();
        String encodedImage = Base64.encodeToString( imageByteArray, Base64.DEFAULT );

        return encodedImage;
    }
    /*FOTO*/

    /*INPUT*/
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
                case R.id.inputBarang:
                    validateNamaBarang();
                    break;
                case R.id.inputHargaJual:
                    validateHargaJual();
                    break;
                case R.id.inputHargaBeli:
                    validateHargaBeli();
                    break;
                case R.id.inputStok:
                    validateStok();
                    break;
            }
        }
    }

    private class MyTextWatcher1 implements TextWatcher {

        private View view;

        public MyTextWatcher1(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.inputNamaKategori:
                    validateNamaKatgori();
                    break;
            }
        }
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
    private boolean validateHargaJual() {
        if (inputHargaJual.getText().toString().trim().isEmpty()) {
            inputLayoutHargaJual.setError("Masukkan Harga Jual");
            requestFocus(inputHargaJual);
            return false;
        } else {
            inputLayoutHargaJual.setErrorEnabled(false);
        }
        return true;
    }
    private boolean validateHargaBeli() {
        if (inputHargaBeli.getText().toString().trim().isEmpty()) {
            inputLayoutHargaBeli.setError("Masukkan Harga Beli");
            requestFocus(inputHargaBeli);
            return false;
        } else {
            inputLayoutHargaBeli.setErrorEnabled(false);
        }
        return true;
    }
    private boolean validateNamaBarang() {
        if (inputBarang.getText().toString().trim().isEmpty()) {
            inputLayoutNamaBarang.setError("Masukkan Nama Barang");
            requestFocus(inputBarang);
            return false;
        } else {
            inputLayoutNamaBarang.setErrorEnabled(false);
        }
        return true;
    }
    private boolean validateStok() {
        if (inputStok.getText().toString().trim().isEmpty()) {
            inputLayoutStok.setError("Masukkan Stok");
            requestFocus(inputStok);
            return false;
        } else {
            inputLayoutStok.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateNamaKatgori() {
        if (inputNamaKategori.getText().toString().trim().isEmpty() && statusBtn == false) {
            inputLayoutNamaKategori.setError("Masukkan Nama Kategori");
            requestFocus( inputNamaKategori );
            return false;
        } else {
            inputLayoutNamaKategori.setErrorEnabled(false);
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

    private void setTidakAdaGambar(String namaBarang){
        txtHurufDepanBarangForm.setText(namaBarang.substring( 0,1 ));

        int color=0;

        if (txtHurufDepanBarangForm.getText().equals( "A" ) || txtHurufDepanBarangForm.getText().equals( "a" )){
            color=R.color.amber_500;
        }else if(txtHurufDepanBarangForm.getText().equals( "B" ) || txtHurufDepanBarangForm.getText().equals( "b" )){
            color=R.color.blue_500;
        }else if(txtHurufDepanBarangForm.getText().equals( "C" ) || txtHurufDepanBarangForm.getText().equals( "c" )){
            color=R.color.blue_grey_500;
        }else if(txtHurufDepanBarangForm.getText().equals( "D" ) || txtHurufDepanBarangForm.getText().equals( "d" )){
            color=R.color.brown_500;
        }else if(txtHurufDepanBarangForm.getText().equals( "E" ) || txtHurufDepanBarangForm.getText().equals( "e" )){
            color=R.color.cyan_500;
        }else if(txtHurufDepanBarangForm.getText().equals( "F" ) || txtHurufDepanBarangForm.getText().equals( "f" )){
            color=R.color.deep_orange_500;
        }else if(txtHurufDepanBarangForm.getText().equals( "G" ) || txtHurufDepanBarangForm.getText().equals( "g" )){
            color=R.color.deep_purple_500;
        }else if(txtHurufDepanBarangForm.getText().equals( "H" ) || txtHurufDepanBarangForm.getText().equals( "h" )){
            color=R.color.green_500;
        }else if(txtHurufDepanBarangForm.getText().equals( "I" ) || txtHurufDepanBarangForm.getText().equals( "i" )){
            color=R.color.grey_500;
        }else if(txtHurufDepanBarangForm.getText().equals( "J" ) || txtHurufDepanBarangForm.getText().equals( "j" )){
            color=R.color.indigo_500;
        }else if(txtHurufDepanBarangForm.getText().equals( "K" ) || txtHurufDepanBarangForm.getText().equals( "k" )){
            color=R.color.teal_500;
        }else if(txtHurufDepanBarangForm.getText().equals( "L" ) || txtHurufDepanBarangForm.getText().equals( "l" )){
            color=R.color.lime_500;
        }else if(txtHurufDepanBarangForm.getText().equals( "M" ) || txtHurufDepanBarangForm.getText().equals( "m" )){
            color=R.color.red_500;
        }else if(txtHurufDepanBarangForm.getText().equals( "N" ) || txtHurufDepanBarangForm.getText().equals( "n" )){
            color=R.color.light_blue_500;
        }else if(txtHurufDepanBarangForm.getText().equals( "O" ) || txtHurufDepanBarangForm.getText().equals( "o" )){
            color=R.color.light_green_500;
        }else if(txtHurufDepanBarangForm.getText().equals( "P" ) || txtHurufDepanBarangForm.getText().equals( "p" )){
            color=R.color.orange_500;
        }else if(txtHurufDepanBarangForm.getText().equals( "Q" ) || txtHurufDepanBarangForm.getText().equals( "q" )){
            color=R.color.pink_500;
        }else if(txtHurufDepanBarangForm.getText().equals( "R" ) || txtHurufDepanBarangForm.getText().equals( "r" )){
            color=R.color.red_600;
        }else if(txtHurufDepanBarangForm.getText().equals( "S" ) || txtHurufDepanBarangForm.getText().equals( "s" )){
            color=R.color.yellow_600;
        }else if(txtHurufDepanBarangForm.getText().equals( "T" ) || txtHurufDepanBarangForm.getText().equals( "t" )){
            color=R.color.blue_600;
        }else if(txtHurufDepanBarangForm.getText().equals( "U" ) || txtHurufDepanBarangForm.getText().equals( "u" )){
            color=R.color.cyan_600;
        }else if(txtHurufDepanBarangForm.getText().equals( "V" ) || txtHurufDepanBarangForm.getText().equals( "v" )){
            color=R.color.green_600;
        }else if(txtHurufDepanBarangForm.getText().equals( "W" ) || txtHurufDepanBarangForm.getText().equals( "w" )){
            color=R.color.purple_600;
        }else if(txtHurufDepanBarangForm.getText().equals( "X" ) || txtHurufDepanBarangForm.getText().equals( "x" )){
            color=R.color.pink_600;
        }else if(txtHurufDepanBarangForm.getText().equals( "Y" ) || txtHurufDepanBarangForm.getText().equals( "y" )){
            color=R.color.lime_600;
        }else if(txtHurufDepanBarangForm.getText().equals( "Z" ) || txtHurufDepanBarangForm.getText().equals( "z" )){
            color=R.color.orange_600;
        }

        fotoTambahBarang1.setImageResource(color);
    }

    private void inisialisasiFragmentKategori (){
        progress = new ProgressDialog(this);
        noDataKategoriBarang = dialogView.findViewById( R.id.noDataKategoriBarang );
        refreshDataKategoriBarang = (SwipeRefreshLayout) dialogView.findViewById(R.id.refreshDataKategoriBarang);
        imageButtonTambahKategoriBarang = dialogView.findViewById( R.id.imageButtonTambahKategoriBarang );
        cobaLagiDataKategoriBarang = dialogView.findViewById( R.id.cobaLagiKategoriBarang );
        koneksiDataKategoriBarang = dialogView.findViewById( R.id.koneksiDataKategoriBarang );
        progressBarDataKategoriBarang = dialogView.findViewById( R.id.progressBarDataKategoriBarang );
        recyclerViewDataKategoriBarang = (RecyclerView) dialogView.findViewById(R.id.recycleViewDataKategoriBarang);

        inputLayoutNamaKategori = (TextInputLayout) dialogView.findViewById(R.id.inputLayoutNamaKategori);
        inputNamaKategori = (EditText) dialogView.findViewById(R.id.inputNamaKategori);

        inputNamaKategori.addTextChangedListener( new FormBarangActivity.MyTextWatcher1( inputNamaKategori ) );

        recyclerViewDataKategoriBarang.setHasFixedSize(true);
        recyclerViewDataKategoriBarang.setLayoutManager(new LinearLayoutManager(this));

        listItemDataKategorisBarangs = new ArrayList<>();
        adapterDataKategoriBarang = new AdapterRecycleViewDataKategoriBarang( listItemDataKategorisBarangs, this, "dialog", dialog, txtKdKategoriBarangForm, txtNamaKategoriBarangForm);

        loadKategoriBarang();

        refreshDataKategoriBarang.setOnRefreshListener( new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listItemDataKategorisBarangs.clear();
                adapterDataKategoriBarang.notifyDataSetChanged();
                loadKategoriBarang();
            }
        } );

        cobaLagiDataKategoriBarang.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                koneksiDataKategoriBarang.setVisibility( View.GONE );
                progressBarDataKategoriBarang.setVisibility( View.VISIBLE );
                loadKategoriBarang();
            }
        } );

        imageButtonTambahKategoriBarang.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateNamaKatgori() ) {
                    return;
                }else {
                    statusBtn=true;
                    progress.setMessage("Mohon Ditunggu, Sedang diProses.....");
                    progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progress.setIndeterminate(false);
                    progress.setCanceledOnTouchOutside(false);
                    prosesTambahKategori(
                            inputNamaKategori.getText().toString().trim()
                    );
                }
            }
        } );

        recyclerViewDataKategoriBarang.setAdapter( adapterDataKategoriBarang );
    }

    private void loadKategoriBarang(){
        listItemDataKategorisBarangs.clear();
        adapterDataKategoriBarang.notifyDataSetChanged();
        StringRequest stringRequest = new StringRequest( Request.Method.GET, baseUrl+"api/kategori?api=kategoriall",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getInt( "jml_data" )==0){
                                noDataKategoriBarang.setVisibility( View.VISIBLE );
                            }else{
                                noDataKategoriBarang.setVisibility( View.GONE );
                                JSONArray data = jsonObject.getJSONArray("data");
                                for (int i = 0; i<data.length(); i++){
                                    JSONObject kategoriobject = data.getJSONObject( i );

                                    ListItemDataKategoriBarang listItemDataKategoriBarang = new ListItemDataKategoriBarang(
                                            kategoriobject.getString( "kd_kategori"),
                                            kategoriobject.getString( "nama_kategori" )
                                    );

                                    listItemDataKategorisBarangs.add( listItemDataKategoriBarang );
                                    adapterDataKategoriBarang.notifyDataSetChanged();
                                }
                            }
                            refreshDataKategoriBarang.setRefreshing( false );
                            progressBarDataKategoriBarang.setVisibility( View.GONE );
                            koneksiDataKategoriBarang.setVisibility( View.GONE);
                        }catch (JSONException e){
                            e.printStackTrace();
                            refreshDataKategoriBarang.setRefreshing( false );
                            progressBarDataKategoriBarang.setVisibility( View.GONE );
                            noDataKategoriBarang.setVisibility( View.GONE );
                            listItemDataKategorisBarangs.clear();
                            koneksiDataKategoriBarang.setVisibility( View.VISIBLE );
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        refreshDataKategoriBarang.setRefreshing( false );
                        progressBarDataKategoriBarang.setVisibility( View.GONE );
                        noDataKategoriBarang.setVisibility( View.GONE );
                        listItemDataKategorisBarangs.clear();
                        koneksiDataKategoriBarang.setVisibility( View.VISIBLE );
                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue( FormBarangActivity.this );
        requestQueue.add( stringRequest );

    }

    private void prosesTambahKategori(final String nama_kategori) {
        progress.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, baseUrl+"api/kategori", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String kode = jsonObject.getString("kode");
                    if (kode.equals("1")) {
                        Toast.makeText(FormBarangActivity.this, "Berhasil Menambahkan Kategori Barang", Toast.LENGTH_SHORT).show();
                        inputNamaKategori.setText( "" );
                        statusBtn=false;
                        loadKategoriBarang();
                    }else{
                        Toast.makeText(FormBarangActivity.this, "Gagal Menambahkan Kategori Barang", Toast.LENGTH_SHORT).show();
                    }
                    progress.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d( "TAG", e.toString() );
                    Toast.makeText(FormBarangActivity.this, "Periksa koneksi & coba lagi", Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                }
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.d( "TAG", error.toString() );
                /*Log.d(TAG, error.printStackTrace() );*/
                Toast.makeText(FormBarangActivity.this, "Periksa koneksi & coba lagi1", Toast.LENGTH_SHORT).show();
                progress.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nama_kategori", nama_kategori);
                params.put("api", "tambah");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(FormBarangActivity.this);
        requestQueue.add(stringRequest);
    }
}
