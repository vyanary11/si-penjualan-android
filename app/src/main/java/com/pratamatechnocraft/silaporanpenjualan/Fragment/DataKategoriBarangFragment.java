package com.pratamatechnocraft.silaporanpenjualan.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pratamatechnocraft.silaporanpenjualan.Adapter.AdapterRecycleViewDataKategoriBarang;
import com.pratamatechnocraft.silaporanpenjualan.Model.BaseUrlApiModel;
import com.pratamatechnocraft.silaporanpenjualan.Model.ListItemDataKategoriBarang;
import com.pratamatechnocraft.silaporanpenjualan.R;
import com.pratamatechnocraft.silaporanpenjualan.Service.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DataKategoriBarangFragment extends Fragment {

    private RecyclerView recyclerViewDataKategoriBarang;
    private AdapterRecycleViewDataKategoriBarang adapterDataKategoriBarang;
    LinearLayout noDataKategoriBarang, koneksiDataKategoriBarang;
    SwipeRefreshLayout refreshDataKategoriBarang;
    ImageButton imageButtonTambahKategoriBarang;
    ProgressBar progressBarDataKategoriBarang;
    Button cobaLagiDataKategoriBarang;
    SessionManager sessionManager;
    private Boolean statusFragment = false,statusBtn = false;
    private ProgressDialog progress;
    private TextInputLayout inputLayoutNamaKategori;
    private EditText inputNamaKategori;
    AlertDialog dialog;

    private List<ListItemDataKategoriBarang> listItemDataKategorisBarangs;

    BaseUrlApiModel baseUrlApiModel = new BaseUrlApiModel();
    private String baseUrl=baseUrlApiModel.getBaseURL();
    private static final String API_URL = "api/kategori?api=kategoriall";
    TextView textView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_data_kategori_barang, container, false);
        progress = new ProgressDialog(getContext());
        noDataKategoriBarang = view.findViewById( R.id.noDataKategoriBarang );
        refreshDataKategoriBarang = (SwipeRefreshLayout) view.findViewById(R.id.refreshDataKategoriBarang);
        imageButtonTambahKategoriBarang = view.findViewById( R.id.imageButtonTambahKategoriBarang );
        cobaLagiDataKategoriBarang = view.findViewById( R.id.cobaLagiKategoriBarang );
        koneksiDataKategoriBarang = view.findViewById( R.id.koneksiDataKategoriBarang );
        progressBarDataKategoriBarang = view.findViewById( R.id.progressBarDataKategoriBarang );
        recyclerViewDataKategoriBarang = (RecyclerView) view.findViewById(R.id.recycleViewDataKategoriBarang);

        inputLayoutNamaKategori = (TextInputLayout) view.findViewById(R.id.inputLayoutNamaKategori);
        inputNamaKategori = (EditText) view.findViewById(R.id.inputNamaKategori);

        inputNamaKategori.addTextChangedListener( new DataKategoriBarangFragment.MyTextWatcher( inputNamaKategori ) );

        sessionManager = new SessionManager( getContext() );
        HashMap<String, String> kategori = sessionManager.getUserDetail();

        refreshDataKategoriBarang.setOnRefreshListener( new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listItemDataKategorisBarangs.clear();
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

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.ic_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener( new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapterDataKategoriBarang.getFilter().filter(s);
                return false;
            }
        } );
        searchView.setQueryHint("Search");

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Data Kategori");
        setHasOptionsMenu( true );
        loadKategoriBarang();
    }

    @Override
    public void onPause() {
        super.onPause();
        statusFragment=true;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(statusFragment) {
            loadKategoriBarang();
        }
    }

    private void loadKategoriBarang(){
        listItemDataKategorisBarangs = new ArrayList<>();
        StringRequest stringRequest = new StringRequest( Request.Method.GET, baseUrl+API_URL,
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
                                }
                            }
                            refreshDataKategoriBarang.setRefreshing( false );
                            progressBarDataKategoriBarang.setVisibility( View.GONE );
                            koneksiDataKategoriBarang.setVisibility( View.GONE);
                            setUpRecycleView();
                        }catch (JSONException e){
                            e.printStackTrace();
                            refreshDataKategoriBarang.setRefreshing( false );
                            progressBarDataKategoriBarang.setVisibility( View.GONE );
                            noDataKategoriBarang.setVisibility( View.GONE );
                            setUpRecycleView();
                            listItemDataKategorisBarangs.clear();
                            adapterDataKategoriBarang.notifyDataSetChanged();
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
                        setUpRecycleView();
                        listItemDataKategorisBarangs.clear();
                        adapterDataKategoriBarang.notifyDataSetChanged();
                        koneksiDataKategoriBarang.setVisibility( View.VISIBLE );
                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue( getContext() );
        requestQueue.add( stringRequest );

    }

    private void prosesTambahKategori(final String nama_kategori) {
        progress.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, baseUrl+API_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String kode = jsonObject.getString("kode");
                    if (kode.equals("1")) {
                        Toast.makeText(getContext(), "Berhasil Menambahkan Kategori Barang", Toast.LENGTH_SHORT).show();
                        inputNamaKategori.setText( "" );
                        statusBtn=false;
                        loadKategoriBarang();
                    }else{
                        Toast.makeText(getContext(), "Gagal Menambahkan Kategori Barang", Toast.LENGTH_SHORT).show();
                    }
                    progress.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d( "TAG", e.toString() );
                    Toast.makeText(getContext(), "Periksa koneksi & coba lagi", Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                }
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.d( "TAG", error.toString() );
                /*Log.d(TAG, error.printStackTrace() );*/
                Toast.makeText(getContext(), "Periksa koneksi & coba lagi1", Toast.LENGTH_SHORT).show();
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

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }


    private void setUpRecycleView(){
        recyclerViewDataKategoriBarang.setHasFixedSize(true);
        recyclerViewDataKategoriBarang.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterDataKategoriBarang = new AdapterRecycleViewDataKategoriBarang( listItemDataKategorisBarangs, getContext(), "biasa", null, null, null);
        recyclerViewDataKategoriBarang.setAdapter( adapterDataKategoriBarang );
        adapterDataKategoriBarang.notifyDataSetChanged();
    }

    /*INPUT*/
    private class MyTextWatcher implements TextWatcher {

        private View view;

        public MyTextWatcher(View view) {
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
            getActivity().getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
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
}
