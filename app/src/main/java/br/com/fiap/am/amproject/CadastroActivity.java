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

import org.json.JSONStringer;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class CadastroActivity extends AppCompatActivity {

    private EditText etCpf, etnome, etSenha, etConfirmaSenha, etRua, etBairro, etComplemneto;
    private ProgressDialog progress;

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

    public void Cadastrar(View view){
        CadastroUsuarioTask task = new CadastroUsuarioTask();
        task.execute(etCpf.getText().toString(), etnome.getText().toString(), etSenha.getText().toString(),
                     etConfirmaSenha.getText().toString(),etRua.getText().toString(),etBairro.getText().toString(),etComplemneto.getText().toString());

    }

    private class CadastroUsuarioTask extends AsyncTask<String, Void, Integer>{

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(CadastroActivity.this, "Aguarde...", "Realizando cadastro");
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(String... params) {
            URL url = null;

            try {
                url = new URL("");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");

                JSONStringer json = new JSONStringer();
                json.object();
                json.key("cpf").value(params[0]);
                json.key("nome").value(params[1]);
                json.key("senha").value(params[2]);
                json.key("confSenha").value(params[3]);
                json.key("rua").value(params[4]);
                json.key("bairro").value(params[5]);
                json.key("complemento").value(params[6]);
                json.endObject();

                OutputStreamWriter stream = new OutputStreamWriter(connection.getOutputStream());
                stream.write(json.toString());
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

            if (s == 201){
                Toast.makeText(CadastroActivity.this, "Cadastro Realizado!", Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(CadastroActivity.this, "Erro ao realizar cadastro", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void haveAnAccount(View view) {
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
    }
}
