package br.com.fiap.am.amproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.UnicodeSetSpanner;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class EditarPerfilActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText cpfUsuario;
    private EditText nomeUsuario;
    private EditText ruaUsuario;
    private EditText numeroUsuario;
    private EditText complementoUsuario;
    private EditText bairroUsuario;
    private EditText cidadeUsuario;
    private EditText estadoUsuario;
    private EditText cepUsuario;

    Button btSendCadastro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        Toolbar toolbar = (Toolbar)findViewById(R.id.tb_editar_perfil);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btSendCadastro = (Button)findViewById(R.id.bt_confirma_edicao_perfil);
        btSendCadastro.setOnClickListener(this);

        loadFields();
        String infos = getSharedPreferences();
        String splitParaPegarId[] = infos.split(",");

        GetAllInformationsAboutUser gainf = new GetAllInformationsAboutUser();
        gainf.execute(splitParaPegarId[0]);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_confirma_edicao_perfil:
                SendAllInformationsToUpdate satb = new SendAllInformationsToUpdate();
                String dados = getSharedPreferences();
                String [] infos = dados.split(",");

                satb.execute(infos[0],nomeUsuario.getText().toString(),
                        infos[1],
                        cpfUsuario.getText().toString(),
                        ruaUsuario.getText().toString(),
                        numeroUsuario.getText().toString(),
                        bairroUsuario.getText().toString(),
                        complementoUsuario.getText().toString(),
                        estadoUsuario.getText().toString(),
                        cidadeUsuario.getText().toString(),
                        cepUsuario.getText().toString()
                        );
                break;
        }
    }

    private class SendAllInformationsToUpdate extends AsyncTask<String,Void,Integer>{

        ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(EditarPerfilActivity.this,"Aguarde","Estamos atualizando seus dados");
        }

        @Override
        protected Integer doInBackground(String... params) {

            URL url;

            try {
                url = new URL("http://paguefacilbinatron.azurewebsites.net/api/CadastroUsuarioWeb/"+params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("PUT");
                connection.setRequestProperty("Content-type","application/json");

                JSONObject jsonParamsUsuario = new JSONObject();
                jsonParamsUsuario.put("Nome",params[1]);
                jsonParamsUsuario.put("Senha",params[2]);
                jsonParamsUsuario.put("Cpf",params[3]);

                JSONObject jsonUsuarioObject = new JSONObject();
                jsonUsuarioObject.put("Usuario",jsonParamsUsuario);

                JSONObject jsonParamsEndereco = new JSONObject();
                jsonParamsEndereco.put("Rua",params[4]);
                jsonParamsEndereco.put("Numero",params[5]);
                jsonParamsEndereco.put("Bairro",params[6]);
                jsonParamsEndereco.put("Complemento",params[7]);
                jsonParamsEndereco.put("Estado",params[8]);
                jsonParamsEndereco.put("Cidade",params[9]);
                jsonParamsEndereco.put("Cep",params[10]);

                jsonUsuarioObject.put("EnderecoUsuario",jsonParamsEndereco);

                OutputStreamWriter stream = new OutputStreamWriter(connection.getOutputStream());
                stream.write(jsonUsuarioObject.toString());
                stream.close();

                return connection.getResponseCode();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            progress.dismiss();

            if(integer==200){
                Toast.makeText(EditarPerfilActivity.this,"Dados atualizados",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(EditarPerfilActivity.this,MenuActivity.class);
                startActivity(intent);
                finish();
            }
            else{
                Toast.makeText(EditarPerfilActivity.this,"Algo deu errado",Toast.LENGTH_LONG).show();
            }
        }
    }


    private class GetAllInformationsAboutUser extends AsyncTask<String,Void,String>{
        ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(EditarPerfilActivity.this,"Aguarde","Coletando seus dados");

        }

        @Override
        protected String doInBackground(String... params) {
            URL url;

            try {
                url = new URL("http://paguefacilbinatron.azurewebsites.net/api/CadastroUsuarioWeb/"+params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept","application/json");

                if(connection.getResponseCode()==200){
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder builder = new StringBuilder();
                    String linha = "";

                    while((linha=reader.readLine())!=null){
                        builder.append(linha);
                    }

                    connection.disconnect();

                    return builder.toString();
                }else {
                    Toast.makeText(getApplicationContext(),"Ocorreu um erro ao buscar",Toast.LENGTH_SHORT).show();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            progress.dismiss();
            try {
                JSONObject jsonUsuarioObject = new JSONObject(s);
                JSONObject jsonDadosUsuarioObject = jsonUsuarioObject.getJSONObject("Usuario");
                String nome = jsonDadosUsuarioObject.getString("Nome");
                String senha = jsonDadosUsuarioObject.getString("Senha");
                String cpf = jsonDadosUsuarioObject.getString("Cpf");

                JSONObject jsonEnderecoObject = jsonUsuarioObject.getJSONObject("EnderecoUsuario");
                String rua = jsonEnderecoObject.getString("Rua");
                String numero = jsonEnderecoObject.getString("Numero");
                String bairro = jsonEnderecoObject.getString("Bairro");
                String complemento = jsonEnderecoObject.getString("Complemento");
                String estado = jsonEnderecoObject.getString("Estado");
                String cep = jsonEnderecoObject.getString("Cep");
                String cidade = jsonEnderecoObject.getString("Cidade");


                cpfUsuario.setText(cpf);
                nomeUsuario.setText(nome);
                ruaUsuario.setText(rua);
                numeroUsuario.setText(numero);
                complementoUsuario.setText(complemento);
                bairroUsuario.setText(bairro);
                cidadeUsuario.setText(cidade);
                estadoUsuario.setText(estado);
                cepUsuario.setText(cep);

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }


    private void loadFields(){
        cpfUsuario = (EditText)findViewById(R.id.et_cpf_editar_perfil);
        nomeUsuario = (EditText)findViewById(R.id.et_nome_editar_perfil);
        ruaUsuario = (EditText)findViewById(R.id.et_rua_editar_perfil);
        numeroUsuario = (EditText)findViewById(R.id.et_numero_editar_perfil);
        complementoUsuario = (EditText)findViewById(R.id.et_complemento_editar_perfil);
        bairroUsuario = (EditText)findViewById(R.id.et_bairro_editar_perfil);
        cidadeUsuario = (EditText)findViewById(R.id.et_cidade_editar_perfil);
        estadoUsuario = (EditText)findViewById(R.id.et_estado_editar_perfil);
        cepUsuario = (EditText)findViewById(R.id.et_cep_editar_perfil);
    }

    private String getSharedPreferences(){

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                StringBuilder builder = new StringBuilder();
                        builder.append(sp.getString("id",null));
                        builder.append(",");
                        builder.append(sp.getString("senha",null));

                return builder.toString();

    }

}
