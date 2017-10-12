package br.com.fiap.am.amproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import br.com.fiap.am.model.Usuario;

public class LoginActivity extends AppCompatActivity {

    private EditText userEt;
    private EditText passwordEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar)findViewById(R.id.tb_login);
        setSupportActionBar(toolbar);

       getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userEt = (EditText) findViewById(R.id.et_user);
        passwordEt = (EditText) findViewById(R.id.et_password);
    }

    public void doLogin(View view) {

        ValidateLogin validateLogin = new ValidateLogin();
        Usuario usuario = new Usuario();
        usuario.setCpf(userEt.getText().toString());
        usuario.setSenha(passwordEt.getText().toString());
        validateLogin.execute(usuario);
    }

    public void recuperarSenha(View view) {
        Toast.makeText(getApplicationContext(),"Recuperar Senha",Toast.LENGTH_SHORT).show();
    }

    private class ValidateLogin extends AsyncTask<Usuario,Void,String>{
        ProgressDialog progress;
        @Override
        protected void onPreExecute() {
             progress = ProgressDialog.show(LoginActivity.this,"Aguarde","Realizando Login");

        }

        @Override
        protected String doInBackground(Usuario...usuarios) {
            URL url = null;

            try{
                //url = new URL("http://192.168.1.36:52993/api/LoginWeb");
                url = new URL("http://paguefacilbinatron.azurewebsites.net/api/LoginWeb");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("Cpf",usuarios[0].getCpf());
                jsonObject.put("Senha",usuarios[0].getSenha());

                OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream());
                osw.write(jsonObject.toString());
                osw.close();
                if(connection.getResponseCode()==200){

                    String linha = "";
                    StringBuilder builder = new StringBuilder();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    while ((linha=reader.readLine())!=null){
                        builder.append(linha);
                    }

                    connection.disconnect();

                    return builder.toString();
                }

            }catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            progress.dismiss();
            JSONObject jsonResponse = null;
            boolean deuErro=false;
            String mensagemErro="";
            try {
                jsonResponse = new JSONObject(s);
                JSONObject jsonObjectUsuario = jsonResponse.getJSONObject("Usuario");
                String idUsuario = jsonObjectUsuario.getString("Id");
                String nomeUsuario = jsonObjectUsuario.getString("Nome");
                String senhaUsuario = jsonObjectUsuario.getString("Senha");
                String cpfUsuario = jsonObjectUsuario.getString("Cpf");

                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("id",idUsuario);
                editor.putString("nome",nomeUsuario);
                editor.putString("senha",senhaUsuario);
                editor.putString("cpf",cpfUsuario);
                editor.commit();

                deuErro = jsonResponse.getBoolean("DeuErro");
                mensagemErro = jsonResponse.getString("MensagemRetorno");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(deuErro==false){
                Intent intent = new Intent(LoginActivity.this,MenuActivity.class);
                startActivity(intent);
            }else {
                Toast.makeText(getApplicationContext(),mensagemErro,Toast.LENGTH_SHORT).show();
            }
        }


    }
}
