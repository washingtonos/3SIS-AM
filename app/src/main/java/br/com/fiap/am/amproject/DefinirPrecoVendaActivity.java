package br.com.fiap.am.amproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class DefinirPrecoVendaActivity extends AppCompatActivity {

    private EditText etPrecoProduto;
    private String nomeProduto;
    private TextView porQuantoVoceVende;
    private TextView definaValorVenda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_definir_preco_venda);

        Toolbar toolbar = (Toolbar)findViewById(R.id.tb_definir_preco_venda);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        porQuantoVoceVende = (TextView)findViewById(R.id.tv_por_quanto_voce_vende);
        definaValorVenda = (TextView)findViewById(R.id.tv_defina_valor_venda);

        //Capturar nome de Produto de activity anterior

        Bundle extras = getIntent().getExtras();
        if(extras!=null){

            nomeProduto = extras.getString("nomeProduto");
            porQuantoVoceVende.setText(getString(R.string.por_quanto_voce_quer)+nomeProduto);
            definaValorVenda.setText("vender o "+nomeProduto);
        }


        etPrecoProduto = (EditText)findViewById(R.id.et_preco_produto_definirprecovenda);
    }

    public void callSetPictureForProduct(View view) {

        Intent intent = new Intent(DefinirPrecoVendaActivity.this,BuscarImagensActivity.class);
        intent.putExtra("nomeProduto",nomeProduto);
        intent.putExtra("precoProduto",etPrecoProduto.getText().toString());
        startActivity(intent);
    }
}
