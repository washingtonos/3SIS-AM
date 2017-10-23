package br.com.fiap.am.amproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

public class CadastrarVendaNomeProdutoActivity extends AppCompatActivity {

    private EditText etNomeProduto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_venda_nome_produto);

        Toolbar toolbar = (Toolbar)findViewById(R.id.tb_cadastrarVenda);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etNomeProduto = (EditText)findViewById(R.id.et_nome_produto_cadastrarvendanomeproduto);
    }

    public void callSetSellPrice(View view) {

        Intent intent = new Intent(CadastrarVendaNomeProdutoActivity.this,DefinirPrecoVendaActivity.class);
        intent.putExtra("nomeProduto",etNomeProduto.getText().toString());
        startActivity(intent);

    }
}
