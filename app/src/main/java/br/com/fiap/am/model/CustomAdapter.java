package br.com.fiap.am.model;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

import br.com.fiap.am.amproject.R;
import br.com.fiap.am.bean.Item;


/**
 * Created by Luan Ianhes on 26/09/2017.
 */

public class CustomAdapter extends BaseAdapter implements ListAdapter{

    private final Activity activity;
    private final JSONArray jsonArray;
    //private final List<Item> listaItem;

    //Para Testes sem o serviço
    public CustomAdapter (JSONArray jsonArray, Activity activity) {

        this.activity = activity;
        this.jsonArray = jsonArray;
    }


    @Override
    public int getCount() {
        if(null==jsonArray)
            return 0;
        else
            return jsonArray.length();

       // return listaItem.size();
    }

    @Override
    public Object getItem(int position) {

        //return listaItem.get(position);
         if(null==jsonArray) return null;
        else
            return jsonArray.optJSONObject(position);
    }

    @Override
    public long getItemId(int position) {
        JSONObject jsonObject = (JSONObject) getItem(position);

        return jsonObject.optLong("id");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //View view = convertView;

           /* view = activity.getLayoutInflater().inflate(R.layout.activity_itens_lista, parent, false);
            Item item = listaItem.get(position);*/

        if (convertView == null)
            convertView = activity.getLayoutInflater().inflate(R.layout.activity_itens_lista, parent, false);

            TextView id = (TextView)
                convertView.findViewById(R.id.lista_id_item);
            TextView nome = (TextView)
                    convertView.findViewById(R.id.lista_nome_item);
            TextView descricao = (TextView)
                    convertView.findViewById(R.id.lista_descricao_item);
            TextView preco = (TextView)
                    convertView.findViewById(R.id.lista_preco_item);
            TextView quantidade = (TextView)
                    convertView.findViewById(R.id.lista_quantidade_item);
            TextView usuario = (TextView)
                convertView.findViewById(R.id.lista_usuario_item);
            ImageView imagem = (ImageView)
                    convertView.findViewById(R.id.imagemItem);


        JSONObject json_data = (JSONObject) getItem(position);

        try {


            if (null != json_data) {

                id.setText(json_data.getString("Id"));
                nome.setText(json_data.getString("Nome"));
                descricao.setText(json_data.getString("Preco"));
                preco.setText(json_data.getString("Descricao"));
                quantidade.setText(json_data.getString("Quantidade"));
                usuario.setText(json_data.getString("Usuario"));
                //imagem.setImageURI(json_data.getString("ImagemUrl"));
            }

        }catch (Exception e){
            e.printStackTrace();
        }


            /*nome.setText(item.getNome());
            descricao.setText(item.getDescricao());
            preco.setText(item.getPreco());
            quantidade.setText(item.getPreco());
            imagem.setImageResource(R.drawable.java);*/

        return convertView;
    }
}
