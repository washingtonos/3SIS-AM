package br.com.fiap.am.amproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class CadastrarUsuarioActivity extends AppCompatActivity {

    private EditText etCpf, etnome, etSenha, etConfirmaSenha,
                etRua, etNumero,etComplemneto, etBairro, etCidade, etEstado, etCep;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_usuario);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_cadastro);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etCpf = (EditText) findViewById(R.id.et_Cpf);
        etnome = (EditText) findViewById(R.id.et_Nome);
        etSenha = (EditText) findViewById(R.id.et_Senha);
        etConfirmaSenha = (EditText) findViewById(R.id.et_ConfirmaSenha);
        etRua = (EditText) findViewById(R.id.et_Rua);
        etNumero = (EditText) findViewById(R.id.et_Numero);
        etComplemneto = (EditText) findViewById(R.id.et_Complemento);
        etBairro = (EditText) findViewById(R.id.et_Bairro);
        etCidade = (EditText) findViewById(R.id.et_Cidade);
        etEstado = (EditText) findViewById(R.id.et_Estado);
        etCep = (EditText) findViewById(R.id.et_Cep);


    }

    public void Cadastrar(View view){
        CadastroUsuarioTask task = new CadastroUsuarioTask();
        task.execute(etCpf.getText().toString(), etnome.getText().toString(), etSenha.getText().toString(),
                     etConfirmaSenha.getText().toString(),etRua.getText().toString(),etNumero.getText().toString(),
                     etComplemneto.getText().toString(),etBairro.getText().toString(),etCidade.getText().toString(),
                     etEstado.getText().toString(),etCep.getText().toString());

    }

    private class CadastroUsuarioTask extends AsyncTask<String, Void, Integer>{

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(CadastrarUsuarioActivity.this, "Aguarde...", "Realizando cadastro");
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(String... params) {
            try {
                URL url = new URL("http://paguefacilbinatron.azurewebsites.net/api/CadastroUsuarioWeb");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");


                JSONObject jsonParamsUsuario = new JSONObject();
                jsonParamsUsuario.put("Nome",params[1]);
                jsonParamsUsuario.put("Cpf",params[0]);
                jsonParamsUsuario.put("Senha",params[2]);

                JSONObject jsonUsuarioObject = new JSONObject();
                jsonUsuarioObject.put("Usuario",jsonParamsUsuario);

                JSONObject jsonParamsEndereco = new JSONObject();
                jsonParamsEndereco.put("Rua",params[3]);
                jsonParamsEndereco.put("Numero",params[4]);
                jsonParamsEndereco.put("Bairro",params[6]);
                jsonParamsEndereco.put("Complemento",params[5]);
                jsonParamsEndereco.put("Estado",params[8]);
                jsonParamsEndereco.put("Cidade",params[7]);
                jsonParamsEndereco.put("Cep",params[9]);

                jsonUsuarioObject.put("EnderecoUsuario",jsonParamsEndereco);

                OutputStreamWriter stream = new OutputStreamWriter(connection.getOutputStream());
                stream.write(jsonUsuarioObject.toString());
                stream.close();

                return connection.getResponseCode();
            } catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer s) {
            progress.dismiss();

            if (s == 200){
                Toast.makeText(CadastrarUsuarioActivity.this, "Cadastro Realizado!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(CadastrarUsuarioActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
            else{
                Toast.makeText(CadastrarUsuarioActivity.this, "Erro ao realizar cadastro", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void haveAnAccount(View view) {
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
