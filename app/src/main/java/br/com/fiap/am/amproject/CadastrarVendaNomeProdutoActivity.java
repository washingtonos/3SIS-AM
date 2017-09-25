package br.com.fiap.am.amproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class CadastrarVendaNomeProdutoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_venda_nome_produto);

        Toolbar toolbar = (Toolbar)findViewById(R.id.tb_cadastrarVenda);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void callSetSellPrice(View view) {

        Intent intent = new Intent(CadastrarVendaNomeProdutoActivity.this,DefinirPrecoVendaActivity.class);
        startActivity(intent);

    }
}
