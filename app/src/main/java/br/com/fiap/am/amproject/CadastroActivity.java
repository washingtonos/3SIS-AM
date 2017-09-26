package br.com.fiap.am.amproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

public class CadastroActivity extends AppCompatActivity {

    private EditText etCpf, etnome, etSenha, etConfirmaSenha, etRua, etBairro, etComplemneto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_cadastro);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etCpf = (EditText) findViewById(R.id.et_Cpf);
        etnome = (EditText) findViewById(R.id.et_Nome);
        etSenha = (EditText) findViewById(R.id.et_Senha);
        etConfirmaSenha = (EditText) findViewById(R.id.et_ConfirmaSenha);
        etRua = (EditText) findViewById(R.id.et_Rua);
        etBairro = (EditText) findViewById(R.id.et_Bairro);
        etComplemneto = (EditText) findViewById(R.id.et_Complemento);

    }

    public void haveAnAccount(View view) {
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
    }
}
