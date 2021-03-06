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

import br.com.fiap.am.model.MaskWatcher;
import br.com.fiap.am.model.Usuario;

public class LoginActivity extends AppCompatActivity {

    private EditText userEt;
    private EditText passwordEt;
    private boolean userEtIsEmpty;
    private boolean passwordEtIsEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar)findViewById(R.id.tb_login);
        setSupportActionBar(toolbar);

       getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userEt = (EditText) findViewById(R.id.et_user);
        passwordEt = (EditText) findViewById(R.id.et_password);

        loadSharedPreferences();

        userEt.addTextChangedListener(new MaskWatcher("###.###.###-##"));


    }

    public void doLogin(View view) {



        if(userEtIsEmpty==true&&passwordEtIsEmpty==true){



            final AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
            alert.setTitle("Login");
            alert.setMessage(R.string.deseja_guardar_login_para_acesso);
            alert.setCancelable(true);


            alert.setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    ValidateLogin validateLogin = new ValidateLogin();
                    Usuario usuario = new Usuario();
                    String cpf = userEt.getText().toString();
                    String newcpf = cpf.replace(".","").replace("-","");

                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = sp.edit();

                    editor.putString("cpf",userEt.getText().toString());
                    editor.putString("senha",passwordEt.getText().toString());
                    editor.commit();
                    usuario.setCpf(newcpf);
                    usuario.setSenha(passwordEt.getText().toString());
                    userEtIsEmpty=false;
                    passwordEtIsEmpty=false;

                    validateLogin.execute(usuario);

                }
            });

            alert.setNegativeButton(R.string.nao, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    dialogInterface.cancel();

                    ValidateLogin validateLogin = new ValidateLogin();
                    Usuario usuario = new Usuario();
                    String cpf = userEt.getText().toString();
                    String newcpf = cpf.replace(".","").replace("-","");

                    usuario.setCpf(newcpf);
                    usuario.setSenha(passwordEt.getText().toString());
                    validateLogin.execute(usuario);

                }
            });


            AlertDialog alertDialog = alert.create();
            alertDialog.show();

        }else {
            ValidateLogin validateLogin = new ValidateLogin();
            Usuario usuario = new Usuario();
            String cpf = userEt.getText().toString();
            String newcpf = cpf.replace(".","").replace("-","");

            usuario.setCpf(newcpf);
            usuario.setSenha(passwordEt.getText().toString());

            validateLogin.execute(usuario);

        }


    }

    public void recuperarSenha(View view) {
        Toast.makeText(getApplicationContext(), R.string.recuperar_senha,Toast.LENGTH_SHORT).show();
    }

    private class ValidateLogin extends AsyncTask<Usuario,Void,String>{
        ProgressDialog progress;
        @Override
        protected void onPreExecute() {
             progress = ProgressDialog.show(LoginActivity.this,getString(R.string.aguarde),getString(R.string.realizando_login));

        }

        @Override
        protected String doInBackground(Usuario...usuarios) {
            URL url = null;

            try{
                url = new URL("http://paguefacilbinatron.azurewebsites.net/api/LoginWeb");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(4000);
                connection.setReadTimeout(4000);
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
                }else if(connection.getResponseCode()==500){
                    return "500";

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
            boolean deuErro=true;
            String mensagemErro="";
            if(s!=null){
                if(!s.equals("500")){
                    try {
                        jsonResponse = new JSONObject(s);
                        deuErro = jsonResponse.getBoolean("DeuErro");
                        mensagemErro = jsonResponse.getString("MensagemRetorno");
                        if(deuErro==false){
                            JSONObject jsonObjectUsuario = jsonResponse.getJSONObject("Usuario");
                            String idUsuario = jsonObjectUsuario.getString("Id");
                            String nomeUsuario = jsonObjectUsuario.getString("Nome");
                            String senhaUsuario = jsonObjectUsuario.getString("Senha");
                            String cpfUsuario = jsonObjectUsuario.getString("Cpf");

                            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("id",idUsuario);
                            editor.putString("nome",nomeUsuario);
                            editor.commit();

                            Intent intent = new Intent(LoginActivity.this,MenuActivity.class);
                            startActivity(intent);
                        }else {
                            Toast.makeText(getApplicationContext(),mensagemErro,Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                }else {

                    Toast.makeText(getApplicationContext(), R.string.Ocorreu_erro_ao_validar_login,Toast.LENGTH_SHORT).show();
                }


            }else{
                Toast.makeText(getApplicationContext(), R.string.Ocorreu_um_erro_inesperado,Toast.LENGTH_SHORT).show();
            }



        }


    }

    private void loadSharedPreferences(){

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String cpf = sp.getString("cpf",null);
        String senha = sp.getString("senha",null);

        if(cpf!=null){
            userEt.setText(cpf);
            passwordEt.setText(senha);
        }else {
            userEtIsEmpty=true;
            passwordEtIsEmpty=true;
        }

    }
}
