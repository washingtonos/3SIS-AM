package br.com.fiap.am.amproject;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import br.com.fiap.am.model.ProdutoParaVender;

/**
 * Created by renan on 10/12/17.
 */

public class CustomAdapter extends ArrayAdapter<ProdutoParaVender> implements View.OnClickListener {



    private Context context;
    private List<ProdutoParaVender> listaDeProdutos;



    public CustomAdapter(Context context, List<ProdutoParaVender> listaDeProdutos) {
        super(context, R.layout.content_listview_produtos_cadastrados,listaDeProdutos);
        this.context = context;
        this.listaDeProdutos = listaDeProdutos;
    }


    @Override
    public int getCount() {
        return listaDeProdutos.size();
    }

    @Nullable
    @Override
    public ProdutoParaVender getItem(int position) {
        return listaDeProdutos.get(position);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ProdutoParaVender ppv = getItem(i);

        ViewHolder holder;

        final View result;

        if(view ==null){
            holder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.content_listview_produtos_cadastrados,viewGroup,false);
            holder.tvNome = ((TextView)view.findViewById(R.id.tv_ll_nome_produto_para_vender));
            holder.tvPreco = (TextView)view.findViewById(R.id.tv_ll_preco_produto_para_vender);
            holder.imagem = (ImageView)view.findViewById(R.id.imv_ll_produto_para_vender);

            result = view;
            view.setTag(holder);
        }else{
            holder = (ViewHolder)view.getTag();
            result = view;
        }

        holder.tvNome.setText(ppv.getNome());
        holder.tvPreco.setText(ppv.getPreco());
        return view;
    }

    @Override
    public void onClick(View view) {

        Toast.makeText(context,"Ok",Toast.LENGTH_SHORT).show();

    }


    private static class ViewHolder{
        TextView tvNome;
        TextView tvPreco;
        ImageView imagem;
    }
}
