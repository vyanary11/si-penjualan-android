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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class TambahUserActivity extends AppCompatActivity {

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
    private ImageView fotoTambahUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_user);

        Toolbar ToolBarAtas2 = (Toolbar)findViewById(R.id.toolbartambahuser);
        setSupportActionBar(ToolBarAtas2);
        ToolBarAtas2.setLogoDescription("Tambah User");
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


        /*IMAGE*/
        fotoTambahUser = (ImageView) findViewById( R.id.fotoTambahUser );
        txtFotoTambahUser = (TextView) findViewById( R.id.txtFotoTambahUser );


        /*FUNGSI KLIK*/
        btnPilihFotoTambahUser.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                klikPilihFotoTambahUser();
            }
        } );

        /*VALIDASI DATA*/
        inputUsername.addTextChangedListener( new MyTextWatcher( inputUsername) );
        inputPassword.addTextChangedListener( new MyTextWatcher( inputPassword) );
        inputNamaDepan.addTextChangedListener( new MyTextWatcher( inputNamaDepan) );
        inputNamaBelakang.addTextChangedListener( new MyTextWatcher( inputNamaBelakang) );
        inputNoTelp.addTextChangedListener( new MyTextWatcher( inputNoTelp) );
        inputAlamat.addTextChangedListener( new MyTextWatcher( inputAlamat) );
    }

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
