package br.com.fiap.am.amproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private EditText userEt;
    private EditText passwordEt;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userEt = (EditText) findViewById(R.id.et_user);
        passwordEt = (EditText) findViewById(R.id.et_password);
    }

    public void doLogin(View view) {

        ValidateLogin validateLogin = new ValidateLogin();
        validateLogin.execute(userEt.getText().toString(),passwordEt.getText().toString());
    }

    private class ValidateLogin extends AsyncTask<String,Void,Boolean>{
        ProgressDialog progress;
        @Override
        protected void onPreExecute() {
             progress = ProgressDialog.show(LoginActivity.this,"Aguarde","Realizando Login");

        }

        @Override
        protected Boolean doInBackground(String...params) {

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
