package br.com.fiap.am.amproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class DefinirPrecoVendaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_definir_preco_venda);

        Toolbar toolbar = (Toolbar)findViewById(R.id.tb_definir_preco_venda);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void callSetPictureForProduct(View view) {

        Intent intent = new Intent(DefinirPrecoVendaActivity.this,BuscarImagensActivity.class);
        startActivity(intent);
    }
}
