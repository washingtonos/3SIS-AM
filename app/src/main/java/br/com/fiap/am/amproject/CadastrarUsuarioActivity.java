package br.com.fiap.am.amproject;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import br.com.fiap.am.model.MaskWatcher;

public class CadastrarUsuarioActivity extends AppCompatActivity implements TextWatcher {

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
        etCpf.setBackgroundResource(R.drawable.edit_text_shape);

        etnome = (EditText) findViewById(R.id.et_Nome);
        etnome.setBackgroundResource(R.drawable.edit_text_shape);

        etSenha = (EditText) findViewById(R.id.et_Senha);
        etSenha.setBackgroundResource(R.drawable.edit_text_shape);

        etConfirmaSenha = (EditText) findViewById(R.id.et_ConfirmaSenha);
        etConfirmaSenha.setBackgroundResource(R.drawable.edit_text_shape);

        etRua = (EditText) findViewById(R.id.et_Rua);
        etRua.setBackgroundResource(R.drawable.edit_text_shape);

        etNumero = (EditText) findViewById(R.id.et_Numero);
        etNumero.setBackgroundResource(R.drawable.edit_text_shape);

        etComplemneto = (EditText) findViewById(R.id.et_Complemento);
        etComplemneto.setBackgroundResource(R.drawable.edit_text_shape);

        etBairro = (EditText) findViewById(R.id.et_Bairro);
        etBairro.setBackgroundResource(R.drawable.edit_text_shape);

        etCidade = (EditText) findViewById(R.id.et_Cidade);
        etCidade.setBackgroundResource(R.drawable.edit_text_shape);

        etEstado = (EditText) findViewById(R.id.et_Estado);
        etEstado.setBackgroundResource(R.drawable.edit_text_shape);

        etCep = (EditText) findViewById(R.id.et_Cep);
        etCep.setBackgroundResource(R.drawable.edit_text_shape);


        etConfirmaSenha.addTextChangedListener(this);
        etCep.addTextChangedListener(new MaskWatcher("#####-##"));
        etCpf.addTextChangedListener(new MaskWatcher("###.###.###-##"));


    }

    public void Cadastrar(View view){


        String senha[] = etSenha.getText().toString().split("");
        String confirmaSenha[] = etConfirmaSenha.getText().toString().split("");

        for (int i = 0;i<senha.length;i++){

            if (!senha[i].equals(confirmaSenha[i])){
                Toast.makeText(getApplicationContext(),"A senha nao esta igual",Toast.LENGTH_SHORT).show();
                etSenha.setBackgroundResource(R.drawable.edit_text_shape_change_line_color);
                etConfirmaSenha.setBackgroundResource(R.drawable.edit_text_shape_change_line_color);
                return;
            }

        }

        String cpf = etCpf.getText().toString();




        CadastroUsuarioTask task = new CadastroUsuarioTask();
        task.execute(cpf.replace(".","").replace("-",""), etnome.getText().toString(), etSenha.getText().toString(),
                     etConfirmaSenha.getText().toString(),etRua.getText().toString(),etNumero.getText().toString(),
                     etComplemneto.getText().toString(),etBairro.getText().toString(),etCidade.getText().toString(),
                     etEstado.getText().toString(),etCep.getText().toString().replace("-",""));

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

        String value = editable.toString();

        if(value.equals(etSenha.getText().toString())){

            Toast.makeText(getApplicationContext(),"Senha Confirmada",Toast.LENGTH_SHORT).show();
        }

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
                //Toast.makeText(CadastrarUsuarioActivity.this, "Cadastro Realizado!", Toast.LENGTH_LONG).show();

                final AlertDialog.Builder alert = new AlertDialog.Builder(CadastrarUsuarioActivity.this);
                alert.setTitle("Cadastro realizado");
                alert.setMessage("Deseja gravar o login e a senha para o acesso?");
                alert.setCancelable(true);


                alert.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Intent intent = new Intent(CadastrarUsuarioActivity.this,LoginActivity.class);

                        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editor = sp.edit();

                        editor.putString("cpf",etCpf.getText().toString());
                        editor.putString("senha",etSenha.getText().toString());
                        editor.commit();
                        startActivity(intent);
                        finish();

                    }
                });

                alert.setNegativeButton("Nao", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(CadastrarUsuarioActivity.this,LoginActivity.class);
                        startActivity(intent);
                        finish();

                    }
                });

                AlertDialog alertDialog = alert.create();
                alertDialog.show();

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
