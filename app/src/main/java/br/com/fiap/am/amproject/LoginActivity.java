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
        validateLogin.execute(userEt.getText().toString(),passwordEt.getText().toString());
    }

    public void recuperarSenha(View view) {
        Toast.makeText(getApplicationContext(),"Recuperar Senha",Toast.LENGTH_SHORT).show();
    }

    private class ValidateLogin extends AsyncTask<String,Void,Boolean>{
        ProgressDialog progress;
        @Override
        protected void onPreExecute() {
             progress = ProgressDialog.show(LoginActivity.this,"Aguarde","Realizando Login");

        }

        @Override
        protected Boolean doInBackground(String...params) {
            try{
                URL url = new URL("");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept", "application/json");
            }catch (Exception e){
                e.printStackTrace();
            }
            if(params[0].equals("fiap")&&params[1].equals("fiap")){
                return true;
            }else{
                return false;
            }

        }

        @Override
        protected void onPostExecute(Boolean b) {
            progress.dismiss();

            if(b==true){
                Intent intent = new Intent(LoginActivity.this,MenuActivity.class);
                startActivity(intent);
            }else {
                Toast.makeText(getApplicationContext(),"Usuário ou senha inválidos",Toast.LENGTH_SHORT).show();
            }
        }


    }
}
