package br.com.fiap.am.amproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import br.com.fiap.am.model.Produto;

/**
 * Created by renan on 10/12/17.
 */

public class CustomAdapter extends ArrayAdapter<Produto> implements View.OnClickListener {



    private Context context;
    private List<Produto> listaDeProdutos;



    public CustomAdapter(Context context, List<Produto> listaDeProdutos) {
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
    public Produto getItem(int position) {
        return listaDeProdutos.get(position);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        Produto ppv = getItem(i);

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
        holder.tvPreco.setText("R$ "+ppv.getPreco());
        String path = ppv.getImagemUrl().replace('-','/');
        Bitmap bitmap = BitmapFactory.decodeFile(path);

        if(bitmap!=null){

            int bitmapWidth = bitmap.getWidth();
            int bitmapHeight = bitmap.getHeight();
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap,bitmap.getWidth()/30,bitmap.getHeight()/30,true);
            holder.imagem.setImageBitmap(resizedBitmap);
        }else {

            holder.imagem.setImageResource(R.drawable.ic_image_black_24dp);
        }


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
