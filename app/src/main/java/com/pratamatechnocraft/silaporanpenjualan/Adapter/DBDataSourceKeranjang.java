package com.pratamatechnocraft.silaporanpenjualan.Adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pratamatechnocraft.silaporanpenjualan.Model.ModelKeranjang;

import java.util.ArrayList;

public class DBDataSourceKeranjang {
    private SQLiteDatabase database;
    private DBHelperSqlLiteKeranjang dbHelperSqlLiteKeranjang;
    private String[] allColumns = {
            dbHelperSqlLiteKeranjang.KD_KERANJANG,
            dbHelperSqlLiteKeranjang.KD_BARANG,
            dbHelperSqlLiteKeranjang.NAMA_BARANG,
            dbHelperSqlLiteKeranjang.HARGA_BARANG,
            dbHelperSqlLiteKeranjang.URL_GAMBAR_BARANG,
            dbHelperSqlLiteKeranjang.QTY
    };

    public DBDataSourceKeranjang(Context context){ dbHelperSqlLiteKeranjang = new DBHelperSqlLiteKeranjang(context); }

    public void open() throws SQLException { database = dbHelperSqlLiteKeranjang.getWritableDatabase(); }

    public void close() {
        dbHelperSqlLiteKeranjang.close();
    }

    public ModelKeranjang createModelKeranjang(String kd_barang,String nama_barang,String harga_barang,String url_gambar_barang, String qty) {

        ContentValues values = new ContentValues();
        values.put(dbHelperSqlLiteKeranjang.KD_BARANG, kd_barang);
        values.put(dbHelperSqlLiteKeranjang.NAMA_BARANG, nama_barang);
        values.put(dbHelperSqlLiteKeranjang.HARGA_BARANG, harga_barang);
        values.put(dbHelperSqlLiteKeranjang.URL_GAMBAR_BARANG, url_gambar_barang);
        values.put(dbHelperSqlLiteKeranjang.QTY, qty);

        long insertId = database.insert(dbHelperSqlLiteKeranjang.TABLE_NAME, null,
                values);

        Cursor cursor = database.query(dbHelperSqlLiteKeranjang.TABLE_NAME,
                allColumns, dbHelperSqlLiteKeranjang.KD_KERANJANG + " = " + insertId, null,
                null, null, null);

        cursor.moveToFirst();

        ModelKeranjang newKeranjang = cursorToKeranjang(cursor);

        cursor.close();

        return newKeranjang;
    }

    private ModelKeranjang cursorToKeranjang(Cursor cursor)
    {
        ModelKeranjang modelKeranjang = new ModelKeranjang();
        // debug LOGCAT
        Log.v("info", "The getLONG "+cursor.getLong(0));
        Log.v("info", "The setLatLng "+cursor.getString(1)+","+cursor.getString(2));

        modelKeranjang.setKdKeranjang(cursor.getLong(0));
        modelKeranjang.setKdBarang(cursor.getString(1));
        modelKeranjang.setNamaBrang(cursor.getString(2));
        modelKeranjang.setHargaBarang(cursor.getString(3));
        modelKeranjang.setUrlGambarBarang(cursor.getString(4));
        modelKeranjang.setQty(cursor.getString(5));

        return modelKeranjang;
    }

    public ArrayList<ModelKeranjang> getAllKeranjang() {
        ArrayList<ModelKeranjang> modelKeranjangs = new ArrayList<ModelKeranjang>();

        Cursor cursor = database.query(DBHelperSqlLiteKeranjang.TABLE_NAME,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ModelKeranjang modelKeranjang = cursorToKeranjang(cursor);
            modelKeranjangs.add(modelKeranjang);
            cursor.moveToNext();
        }
        cursor.close();
        return modelKeranjangs;
    }

    //ambil satu barang sesuai id
    public ModelKeranjang getKeranjang(long id)
    {
        ModelKeranjang modelKeranjang = new ModelKeranjang(); //inisialisasi barang
        //select query
        Cursor cursor = database.query(DBHelperSqlLiteKeranjang.TABLE_NAME, allColumns, "_kd_keranjang ="+id, null, null, null, null);
        //ambil data yang pertama
        cursor.moveToFirst();
        //masukkan data cursor ke objek barang
        modelKeranjang = cursorToKeranjang(cursor);
        //tutup sambungan
        cursor.close();
        //return barang
        return modelKeranjang;
    }

    public Boolean cekKeranjang(String kdBarang)
    {
        ModelKeranjang modelKeranjang = new ModelKeranjang();
        //select query
        Cursor cursor = database.rawQuery("select count(*) from data_keranjang where kd_barang='" + kdBarang + "'", null);
        //ambil data yang pertama
        cursor.moveToFirst();
        int count= cursor.getInt(0);
        //tutup sambungan
        cursor.close();
        if (count>0){
            return true;
        }else{
            return false;
        }
    }

    //update barang yang diedit
    public void updateBarang(ModelKeranjang b)
    {
        //ambil id barang
        String strFilter = "kd_barang=" + b.getKdBarang();
        //memasukkan ke content values
        ContentValues args = new ContentValues();
        //masukkan data sesuai dengan kolom pada database
        args.put(DBHelperSqlLiteKeranjang.QTY, DBHelperSqlLiteKeranjang.QTY+1);
        //update query
        database.update(DBHelperSqlLiteKeranjang.TABLE_NAME, args, strFilter, null);
    }

    // delete barang sesuai ID
    public void deleteBarang(long id)
    {
        String strFilter = "_kd_keranjang=" + id;
        database.delete(DBHelperSqlLiteKeranjang.TABLE_NAME, strFilter, null);
    }
}
