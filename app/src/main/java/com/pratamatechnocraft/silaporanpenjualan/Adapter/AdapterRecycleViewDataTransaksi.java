package com.pratamatechnocraft.silaporanpenjualan.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.pratamatechnocraft.silaporanpenjualan.InvoiceActivity;
import com.pratamatechnocraft.silaporanpenjualan.Model.ListItemTransaksi;
import com.pratamatechnocraft.silaporanpenjualan.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterRecycleViewDataTransaksi extends RecyclerView.Adapter<AdapterRecycleViewDataTransaksi.ViewHolder> implements Filterable {

    private List<ListItemTransaksi> listItemTransaksis;
    private List<ListItemTransaksi> listItemTransaksiFull;
    private Context context;
    private int type;


    public AdapterRecycleViewDataTransaksi(List<ListItemTransaksi> listItemTransaksis, Context context, int type) {
        this.listItemTransaksis = listItemTransaksis;
        listItemTransaksiFull = new ArrayList<>( listItemTransaksis );
        this.context = context;
        this.type=type;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate( R.layout.list_item_transaksi,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final ListItemTransaksi listItemTransaksi = listItemTransaksis.get(position);

        holder.txtNoInvoiceTransaksi.setText(listItemTransaksi.getNoInvoice());
        holder.txtTanggalTransaksi.setText(listItemTransaksi.getTanggalTransaksi());
        holder.txtTotalHargaTransaksi.setText(listItemTransaksi.getTotalHarga());

        holder.cardViewDataTransaksi.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String replace;
                if (type==0){
                    replace ="#PL";
                }else{
                    replace ="#PB";
                }
                Intent i = new Intent(context, InvoiceActivity.class);
                i.putExtra("done", false);
                i.putExtra("kdTransaksi", listItemTransaksi.getNoInvoice().replace( replace,"" ));
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItemTransaksis.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView txtNoInvoiceTransaksi, txtTanggalTransaksi, txtTotalHargaTransaksi;
        public CardView cardViewDataTransaksi;

        public ViewHolder(View itemView) {
            super(itemView);
            txtNoInvoiceTransaksi = (TextView) itemView.findViewById(R.id.txtNoInvoiceTransaksi);
            txtTanggalTransaksi = (TextView) itemView.findViewById(R.id.txtTanggalTransaksi);
            txtTotalHargaTransaksi = (TextView) itemView.findViewById(R.id.txtTotalHargaTransaksi);
            cardViewDataTransaksi = (CardView) itemView.findViewById(R.id.cardViewDataTransaksi);
        }
    }

    public Filter getFilter() {
        return listItemFilter;
    }

    private Filter listItemFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<ListItemTransaksi> filteredList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll( listItemTransaksiFull );
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (ListItemTransaksi itemTransaksi : listItemTransaksiFull) {
                    if (itemTransaksi.getNoInvoice().toLowerCase().contains( filterPattern ) || itemTransaksi.getCatatan().toLowerCase().contains( filterPattern ) || itemTransaksi.getCatatan().toLowerCase().contains( filterPattern )) {
                        filteredList.add( itemTransaksi );
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            listItemTransaksis.clear();
            listItemTransaksis.addAll((List) filterResults.values );
            notifyDataSetChanged();
        }
    };

}
