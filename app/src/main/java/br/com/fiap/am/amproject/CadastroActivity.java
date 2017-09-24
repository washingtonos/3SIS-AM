package br.com.fiap.am.amproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class CadastroActivity extends AppCompatActivity {

    private EditText etCpf, etnome, etSenha, etConfirmaSenha, etRua, etBairro, etComplemneto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        etCpf = (EditText) findViewById(R.id.et_Cpf);
        etnome = (EditText) findViewById(R.id.et_Nome);
        etSenha = (EditText) findViewById(R.id.et_Senha);
        etConfirmaSenha = (EditText) findViewById(R.id.et_ConfirmaSenha);
        etRua = (EditText) findViewById(R.id.et_Rua);
        etBairro = (EditText) findViewById(R.id.et_Bairro);
        etComplemneto = (EditText) findViewById(R.id.et_Complemento);

    }
}
