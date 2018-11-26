package com.pratamatechnocraft.silaporanpenjualan.Adapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelperSqlLiteKeranjang extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "data_keranjang";
    public static final String KD_KERANJANG = "_kd_keranjang";
    public static final String KD_BARANG = "kd_barang";
    public static final String NAMA_BARANG = "nama_barang";
    public static final String HARGA_BARANG = "harga_barang";
    public static final String URL_GAMBAR_BARANG = "url_gambar_barang";
    public static final String QTY = "qty";
    public static final String STOK = "stok";
    private static final String db_name ="penjualan.db";
    private static final int db_version=1;

    // Perintah SQL untuk membuat tabel database baru
    private static final String db_create = "create table "
            + TABLE_NAME + "("
            + KD_KERANJANG +" integer primary key autoincrement, "
            + KD_BARANG+ " varchar(100) not null, "
            + NAMA_BARANG+ " varchar(50) not null, "
            + HARGA_BARANG+ " varchar(100) not null, "
            + URL_GAMBAR_BARANG+ " varchar(255) not null, "
            + QTY+ " varchar(100) not null, "
            + STOK+ " varchar(100) not null);";

    public DBHelperSqlLiteKeranjang(Context context) {
        super(context, db_name, null, db_version);
        // Auto generated
    }

    //mengeksekusi perintah SQL di atas untuk membuat tabel database baru
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(db_create);
    }

    // dijalankan apabila ingin mengupgrade database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DBHelperSqlLiteKeranjang.class.getName(),"Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }
}
