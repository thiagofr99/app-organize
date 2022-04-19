package com.devthiagofurtado.organize.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.devthiagofurtado.organize.R;
import com.devthiagofurtado.organize.model.Lancamento;

import java.text.DecimalFormat;
import java.util.List;

public class LancamentoAdapter extends RecyclerView.Adapter<LancamentoAdapter.MyViewHolder> {

    List<Lancamento> lancamentosList;
    Context context;

    public LancamentoAdapter(List<Lancamento> lancamentosList, Context context) {
        this.lancamentosList = lancamentosList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.lancamentos_adapter, parent, false);
        return new MyViewHolder(itemLista);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Lancamento lancamento = lancamentosList.get(position);
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");

        holder.titulo.setText(lancamento.getDescricao());
        holder.valor.setText(decimalFormat.format(lancamento.getValorLanc()));
        holder.categoria.setText(lancamento.getCategoria());

        if (lancamento.getTipoLanc() == "despesa" || lancamento.getTipoLanc().equals("despesa")) {
            holder.valor.setTextColor(context.getResources().getColor(R.color.colorPrimaryDespesa));
            holder.valor.setText("-"+decimalFormat.format(lancamento.getValorLanc()));
        } else {
            holder.valor.setTextColor(context.getResources().getColor(R.color.colorAccentReceitas));
        }

    }


    @Override
    public int getItemCount() {
        return lancamentosList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView titulo, valor, categoria;

        public MyViewHolder(View itemView) {
            super(itemView);



            titulo = itemView.findViewById(R.id.textAdapterTitulo);
            valor = itemView.findViewById(R.id.textAdapterValor);
            categoria = itemView.findViewById(R.id.textAdapterCategoria);
        }

    }

}

