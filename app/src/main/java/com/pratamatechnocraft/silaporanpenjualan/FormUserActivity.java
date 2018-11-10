package com.pratamatechnocraft.silaporanpenjualan;

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
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class FormUserActivity extends AppCompatActivity {

    private Button btnPilihFotoTambahUser,buttonSimpanTambahUser,buttonBatalTambahUser;
    private TextInputLayout inputLayoutUsername,inputLayoutPassword,inputLayoutNamaDepan,inputLayoutNamaBelakang,inputLayoutNoTelp,inputLayoutAlamat;
    private EditText inputUsername,inputPassword,inputNamaDepan,inputNamaBelakang,inputNoTelp,inputAlamat;
    private BottomSheetDialog bottomSheetDialog;
    private Bitmap bitmap;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Bitmap mImageBitmap;
    private Button galeri,kamera;
    private TextView txtFotoTambahUser;
    private ProgressDialog progress;
    private CircleImageView fotoTambahUser;
    private SwipeRefreshLayout refreshFormUser;
    private BaseUrlApiModel baseUrlApiModel = new BaseUrlApiModel();
    private String baseUrl=baseUrlApiModel.getBaseURL();
    private static final String API_URL = "api/user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_user );

        final Intent i = getIntent();
        progress = new ProgressDialog(this);
        refreshFormUser = findViewById( R.id.refreshFormUser );
        Toolbar ToolBarAtas2 = (Toolbar)findViewById(R.id.toolbartambahuser);
        setSupportActionBar(ToolBarAtas2);
        if (i.getStringExtra( "type" ).equals( "tambah" )){
            this.setTitle("Tambah User");
        }else if(i.getStringExtra( "type" ).equals( "edit" )){
            this.setTitle("Edit User");
        }
        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_black_24dp);
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.colorIcons), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*BUTTON*/
        btnPilihFotoTambahUser = findViewById( R.id.buttonPilihFotoTambahUser );
        buttonSimpanTambahUser = findViewById( R.id.buttonSimpanTambahUser );
        buttonBatalTambahUser = findViewById( R.id.buttonBatalTambahUser);

        /*LAYOUT INPUT*/
        inputLayoutUsername = (TextInputLayout) findViewById(R.id.inputLayoutUsername);
        inputLayoutPassword = (TextInputLayout) findViewById(R.id.inputLayoutPassword);
        inputLayoutNamaDepan = (TextInputLayout) findViewById(R.id.inputLayoutNamaDepan);
        inputLayoutNamaBelakang = (TextInputLayout) findViewById(R.id.inputLayoutNamaBelakang);
        inputLayoutNoTelp = (TextInputLayout) findViewById(R.id.inputLayoutNoTelp);
        inputLayoutAlamat = (TextInputLayout) findViewById(R.id.inputLayoutAlamat);

        /*TEXT INPUT*/
        inputUsername = (EditText) findViewById(R.id.inputUsername);
        inputPassword = (EditText) findViewById(R.id.inputPassword);
        inputNamaDepan = (EditText) findViewById(R.id.inputNamaDepan);
        inputNamaBelakang = (EditText) findViewById(R.id.inputNamaBelakang);
        inputNoTelp = (EditText) findViewById(R.id.inputNoTelp);
        inputAlamat = (EditText) findViewById(R.id.inputAlamat);

        if (i.getStringExtra( "type" ).equals( "tambah" )){
            buttonSimpanTambahUser.setText("Tambah");
        }else if(i.getStringExtra( "type" ).equals( "edit" )){
            buttonSimpanTambahUser.setText("Simpan");
            inputLayoutUsername.setVisibility( View.GONE );
            inputLayoutPassword.setVisibility( View.GONE );
        }


        /*IMAGE*/
        fotoTambahUser = (CircleImageView) findViewById( R.id.fotoTambahUser );
        txtFotoTambahUser = (TextView) findViewById( R.id.txtFotoTambahUser );

        /*VALIDASI DATA*/
        inputUsername.addTextChangedListener( new MyTextWatcher( inputUsername) );
        inputPassword.addTextChangedListener( new MyTextWatcher( inputPassword) );
        inputNamaDepan.addTextChangedListener( new MyTextWatcher( inputNamaDepan) );
        inputNamaBelakang.addTextChangedListener( new MyTextWatcher( inputNamaBelakang) );
        inputNoTelp.addTextChangedListener( new MyTextWatcher( inputNoTelp) );
        inputAlamat.addTextChangedListener( new MyTextWatcher( inputAlamat) );

        if (i.getStringExtra( "type" ).equals("tambah")){
            buttonSimpanTambahUser.setText("Tambah");
            refreshFormUser.setEnabled( false );

        }else if(i.getStringExtra( "type" ).equals("edit")){
            buttonSimpanTambahUser.setText("Simpan");
        }

        refreshFormUser.setOnRefreshListener( new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (i.getStringExtra( "type" ).equals("tambah")){
                    refreshFormUser.setRefreshing( false );
                }else if(i.getStringExtra( "type" ).equals("edit")){

                }
            }
        } );


        /*FUNGSI KLIK*/
        btnPilihFotoTambahUser.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                klikPilihFotoTambahUser();
            }
        } );
        buttonSimpanTambahUser.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (i.getStringExtra( "type" ).equals( "tambah" )){
                    if (!validateUsername() || !validatePassword() || !validateNamaDepan() || !validateNamaBelakang() || !validateNamaBelakang() || !validateNotelp() || !validateAlamat()) {
                        return;
                    }else {
                        progress.setMessage("Mohon Ditunggu, Sedang diProses.....");
                        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progress.setIndeterminate(false);
                        progress.setCanceledOnTouchOutside(false);
                        prosesTambahUser(
                            inputUsername.getText().toString().trim(),
                            inputPassword.getText().toString().trim(),
                            inputNamaDepan.getText().toString().trim(),
                            inputNamaBelakang.getText().toString().trim(),
                            inputNoTelp.getText().toString().trim(),
                            inputAlamat.getText().toString().trim(),
                            "",
                            txtFotoTambahUser.getText().toString().trim()
                        );
                    }
                }else if(i.getStringExtra( "type" ).equals( "edit" )){
                    if (!validateNamaDepan() || !validateNamaBelakang() || !validateNamaBelakang() || !validateNotelp() || !validateAlamat()) {
                        return;
                    }else {
                        progress.setMessage("Mohon Ditunggu, Sedang diProses.....");
                        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progress.setIndeterminate(false);
                        progress.setCanceledOnTouchOutside(false);
                        prosesEditUser(
                                inputNamaDepan.getText().toString().trim(),
                                inputNamaBelakang.getText().toString().trim(),
                                inputNoTelp.getText().toString().trim(),
                                inputAlamat.getText().toString().trim(),
                                "",
                                txtFotoTambahUser.getText().toString().trim()
                        );
                    }
                }
            }
        } );

    }

    /*PROSES KE DATABASE*/
    private void prosesEditUser(final String namaDepan, final String namaBelakang, final String noTelp, final String alamat, final String levelUser, final String fotoUser) {
        progress.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, baseUrl+API_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String kode = jsonObject.getString("kode");
                    if (kode.equals("1")) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        String idUser = data.getString("kd_user").trim();

                        Intent i = new Intent(FormUserActivity.this, DetailUserActivity.class);
                        i.putExtra( "idUser", idUser );
                        startActivity(i);
                        Toast.makeText(FormUserActivity.this, "Berhasil Edit User", Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(FormUserActivity.this, "Gagal Edit User", Toast.LENGTH_SHORT).show();
                    }
                    progress.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d( "TAG", e.toString() );
                    Toast.makeText(FormUserActivity.this, "Periksa koneksi & coba lagi", Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                }
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(FormUserActivity.this, "Periksa koneksi & coba lagi1", Toast.LENGTH_SHORT).show();
                progress.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nama_depan", namaDepan);
                params.put("nama_belakang", namaBelakang);
                params.put("no_telp", noTelp);
                params.put("alamat", alamat);
                params.put("level_user", levelUser);
                params.put("foto_user", fotoUser);
                params.put("api", "edit");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private void prosesTambahUser(final String username, final String password, final String namaDepan, final String namaBelakang, final String noTelp, final String alamat, final String levelUser, final String fotoUser) {
        progress.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, baseUrl+API_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String kode = jsonObject.getString("kode");
                    if (kode.equals("1")) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        String idUser = data.getString("kd_user").trim();

                        Intent i = new Intent(FormUserActivity.this, DetailUserActivity.class);
                        i.putExtra( "idUser", idUser );
                        startActivity(i);
                        Toast.makeText(FormUserActivity.this, "Berhasil Menambahkan User", Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(FormUserActivity.this, "Gagal Menambahkan User", Toast.LENGTH_SHORT).show();
                    }
                    progress.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d( "TAG", e.toString() );
                    Toast.makeText(FormUserActivity.this, "Periksa koneksi & coba lagi", Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                }
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(FormUserActivity.this, "Periksa koneksi & coba lagi1", Toast.LENGTH_SHORT).show();
                progress.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                params.put("nama_depan", namaDepan);
                params.put("nama_belakang", namaBelakang);
                params.put("no_telp", noTelp);
                params.put("alamat", alamat);
                params.put("level_user", levelUser);
                params.put("foto_user", fotoUser);
                params.put("api", "tambah");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    /*PROSES KE DATABASE*/

    /*FOTO*/
    private void klikPilihFotoTambahUser() {
        View view = getLayoutInflater().inflate(R.layout.fragment_bottom_sheet_dialog_tambah_foto, null);
        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(view);

        galeri = view.findViewById( R.id.galeri1 );
        kamera = view.findViewById( R.id.kamera1 );
        galeri.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
                pilihFotoTambahUser();
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
    private void pilihFotoTambahUser(){
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
                fotoTambahUser.setImageBitmap( bitmap );
                txtFotoTambahUser.setText( getStringImage( bitmap ) );
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            mImageBitmap = (Bitmap) extras.get("data");
            fotoTambahUser.setImageBitmap(mImageBitmap);
            txtFotoTambahUser.setText( getStringImage( mImageBitmap ) );
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
                case R.id.inputUsername:
                    validateUsername();
                    break;
                case R.id.inputPassword:
                    validatePassword();
                    break;
                case R.id.inputNamaDepan:
                    validateNamaDepan();
                    break;
                case R.id.inputNamaBelakang:
                    validateNamaBelakang();
                    break;
                case R.id.inputNoTelp:
                    validateNotelp();
                    break;
                case R.id.inputAlamat:
                    validateAlamat();
                    break;
            }
        }
    }
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
    private boolean validatePassword() {
        if (inputPassword.getText().toString().trim().isEmpty()) {
            inputLayoutPassword.setError("Masukkan Password");
            requestFocus(inputPassword);
            return false;
        } else {
            inputLayoutPassword.setErrorEnabled(false);
        }
        return true;
    }
    private boolean validateNotelp() {
        if (inputNoTelp.getText().toString().trim().isEmpty()) {
            inputLayoutNoTelp.setError("Masukkan No Telepon");
            requestFocus(inputNoTelp);
            return false;
        } else {
            inputLayoutNoTelp.setErrorEnabled(false);
        }
        return true;
    }
    private boolean validateNamaDepan() {
        if (inputNamaDepan.getText().toString().trim().isEmpty()) {
            inputLayoutNamaDepan.setError("Masukkan Nama Depan");
            requestFocus(inputNamaDepan);
            return false;
        } else {
            inputLayoutNamaDepan.setErrorEnabled(false);
        }
        return true;
    }
    private boolean validateNamaBelakang() {
        if (inputNamaBelakang.getText().toString().trim().isEmpty()) {
            inputLayoutNamaBelakang.setError("Masukkan Nama Belakang");
            requestFocus(inputNamaBelakang);
            return false;
        } else {
            inputLayoutNamaBelakang.setErrorEnabled(false);
        }
        return true;
    }
    private boolean validateAlamat() {
        if (inputAlamat.getText().toString().trim().isEmpty()) {
            inputLayoutAlamat.setError("Masukkan Alamat");
            requestFocus(inputAlamat);
            return false;
        } else {
            inputLayoutAlamat.setErrorEnabled(false);
        }
        return true;
    }
    private boolean validateUsername() {
        if (inputUsername.getText().toString().trim().isEmpty()) {
            inputLayoutUsername.setError("Masukkan Username");
            requestFocus(inputUsername);
            return false;
        } else {
            inputLayoutUsername.setErrorEnabled(false);
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
}
