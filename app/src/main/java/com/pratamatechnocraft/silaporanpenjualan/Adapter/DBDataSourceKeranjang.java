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
    //inisialiasi SQLite Database
    private SQLiteDatabase database;

    //inisialisasi kelas DBHelper
    private DBHelperSqlLiteKeranjang dbHelperSqlLiteKeranjang;

    //ambil semua nama kolom
    private String[] allColumns = { dbHelperSqlLiteKeranjang.KD_KERANJANG,
            dbHelperSqlLiteKeranjang.KD_BARANG, dbHelperSqlLiteKeranjang.QTY};

    //DBHelper diinstantiasi pada constructor
    public DBDataSourceKeranjang(Context context)
    {
        dbHelperSqlLiteKeranjang = new DBHelperSqlLiteKeranjang(context);
    }

    //membuka/membuat sambungan baru ke database
    public void open() throws SQLException {
        database = dbHelperSqlLiteKeranjang.getWritableDatabase();
    }

    //menutup sambungan ke database
    public void close() {
        dbHelperSqlLiteKeranjang.close();
    }

    //method untuk create/insert barang ke database
    public ModelKeranjang createModelKeranjang(String kd_barang, String qty) {

        // membuat sebuah ContentValues, yang berfungsi
        // untuk memasangkan data dengan nama-nama
        // kolom pada database
        ContentValues values = new ContentValues();
        values.put(dbHelperSqlLiteKeranjang.KD_BARANG, kd_barang);
        values.put(dbHelperSqlLiteKeranjang.QTY, qty);

        // mengeksekusi perintah SQL insert data
        // yang akan mengembalikan sebuah insert ID
        long insertId = database.insert(dbHelperSqlLiteKeranjang.TABLE_NAME, null,
                values);

        // setelah data dimasukkan, memanggil
        // perintah SQL Select menggunakan Cursor untuk
        // melihat apakah data tadi benar2 sudah masuk
        // dengan menyesuaikan ID = insertID
        Cursor cursor = database.query(dbHelperSqlLiteKeranjang.TABLE_NAME,
                allColumns, dbHelperSqlLiteKeranjang.KD_KERANJANG + " = " + insertId, null,
                null, null, null);

        // pindah ke data paling pertama
        cursor.moveToFirst();

        // mengubah objek pada kursor pertama tadi
        // ke dalam objek barang
        ModelKeranjang newKeranjang = cursorToKeranjang(cursor);

        // close cursor
        cursor.close();

        // mengembalikan barang baru
        return newKeranjang;
    }

    private ModelKeranjang cursorToKeranjang(Cursor cursor)
    {
        // buat objek barang baru
        ModelKeranjang modelKeranjang = new ModelKeranjang();
        // debug LOGCAT
        Log.v("info", "The getLONG "+cursor.getLong(0));
        Log.v("info", "The setLatLng "+cursor.getString(1)+","+cursor.getString(2));

        /* Set atribut pada objek barang dengan
         * data kursor yang diambil dari database*/
        modelKeranjang.setKdKeranjang(cursor.getLong(0));
        modelKeranjang.setKdBarang(cursor.getString(1));
        modelKeranjang.setQty(cursor.getString(2));

        //kembalikan sebagai objek barang
        return modelKeranjang;
    }

    //mengambil semua data barang
    public ArrayList<ModelKeranjang> getAllKeranjang() {
        ArrayList<ModelKeranjang> modelKeranjangs = new ArrayList<ModelKeranjang>();

        // select all SQL query
        Cursor cursor = database.query(DBHelperSqlLiteKeranjang.TABLE_NAME,
                allColumns, null, null, null, null, null);

        // pindah ke data paling pertama
        cursor.moveToFirst();
        // jika masih ada data, masukkan data barang ke
        // daftar barang
        while (!cursor.isAfterLast()) {
            ModelKeranjang modelKeranjang = cursorToKeranjang(cursor);
            modelKeranjangs.add(modelKeranjang);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
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
