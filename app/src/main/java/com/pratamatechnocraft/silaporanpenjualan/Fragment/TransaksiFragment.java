package com.pratamatechnocraft.silaporanpenjualan.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.pratamatechnocraft.silaporanpenjualan.Adapter.AdapterRecycleViewBarangTransaksiPembelian;
import com.pratamatechnocraft.silaporanpenjualan.Adapter.AdapterRecycleViewBarangTransaksiPenjualan;
import com.pratamatechnocraft.silaporanpenjualan.CheckoutActivity;
import com.pratamatechnocraft.silaporanpenjualan.Drawable.BadgeDrawable;

import com.pratamatechnocraft.silaporanpenjualan.Model.BaseUrlApiModel;
import com.pratamatechnocraft.silaporanpenjualan.Model.ListItemBarangTransaksi;
import com.pratamatechnocraft.silaporanpenjualan.R;
import com.pratamatechnocraft.silaporanpenjualan.Service.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TransaksiFragment extends Fragment {
    private RecyclerView recycleViewBarangTransaksi;
    private AdapterRecycleViewBarangTransaksiPenjualan adapterRecycleViewBarangTransaksiPenjualan;
    private AdapterRecycleViewBarangTransaksiPembelian adapterRecycleViewBarangTransaksiPembelian;
    private ProgressDialog progress;

    SwipeRefreshLayout refreshMendisposisikan;

    private List<ListItemBarangTransaksi> listItemBarangTransaksis;

    BaseUrlApiModel baseUrlApiModel = new BaseUrlApiModel();
    private String baseUrl=baseUrlApiModel.getBaseURL();

    NavigationView navigationView;
    SessionManager sessionManager;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.activity_transaksi_fragment, container, false);
        navigationView = getActivity().findViewById( R.id.nav_view );

        sessionManager = new SessionManager( getContext() );
        HashMap<String, String> user = sessionManager.getUserDetail();

        recycleViewBarangTransaksi = (RecyclerView) view.findViewById(R.id.recycleViewBarangTransaksi);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        recycleViewBarangTransaksi.setLayoutManager(mLayoutManager);
        recycleViewBarangTransaksi.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(0), true));
        recycleViewBarangTransaksi.setItemAnimator(new DefaultItemAnimator());
        recycleViewBarangTransaksi.setHasFixedSize(true);

        listItemBarangTransaksis = new ArrayList<>();
        if (navigationView.getMenu().findItem( R.id.nav_transaksi_penjualan ).isChecked()){
            adapterRecycleViewBarangTransaksiPenjualan = new AdapterRecycleViewBarangTransaksiPenjualan( listItemBarangTransaksis, getContext());
        }else{
            adapterRecycleViewBarangTransaksiPembelian = new AdapterRecycleViewBarangTransaksiPembelian( listItemBarangTransaksis, getContext());
        }


        for (int i=0;i<6;i++){
            ListItemBarangTransaksi listItemBarangTransaksi = new ListItemBarangTransaksi(
                    "1",
                    "Barang "+i,
                    "1000",
                    "mcackmskms"
            );

            listItemBarangTransaksis.add( listItemBarangTransaksi );
            if (navigationView.getMenu().findItem( R.id.nav_transaksi_penjualan ).isChecked()) {
                adapterRecycleViewBarangTransaksiPenjualan.notifyDataSetChanged();
            }else{
                adapterRecycleViewBarangTransaksiPembelian.notifyDataSetChanged();
            }
        }

        if (navigationView.getMenu().findItem( R.id.nav_transaksi_penjualan ).isChecked()) {
            recycleViewBarangTransaksi.setAdapter( adapterRecycleViewBarangTransaksiPenjualan );
        }else {
            recycleViewBarangTransaksi.setAdapter( adapterRecycleViewBarangTransaksiPembelian );
        }



        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        if (navigationView.getMenu().findItem( R.id.nav_transaksi_penjualan ).isChecked()) {
            getActivity().setTitle("Transaksi Penjualan");
        }else {
            getActivity().setTitle("Transaksi Pembelian");
        }

        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate( R.menu.icon_menu_checkout, menu );
        MenuItem itemCart = menu.findItem(R.id.icon_checkout );
        LayerDrawable icon = (LayerDrawable) itemCart.getIcon();
        setBadgeCount(getContext(), icon, "9");
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.icon_checkout:
                // do s.th.
                Intent i = new Intent(getContext(), CheckoutActivity.class );
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public static void setBadgeCount(Context context, LayerDrawable icon, String count) {

        BadgeDrawable badge;

        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_group_count);
        if (reuse != null && reuse instanceof BadgeDrawable) {
            badge = (BadgeDrawable) reuse;
        } else {
            badge = new BadgeDrawable(context);
        }

        badge.setCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_group_count, badge);
    }

    private class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        private GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition( view ); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round( TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics() ) );
    }
}

