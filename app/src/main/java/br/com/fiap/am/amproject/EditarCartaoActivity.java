package br.com.fiap.am.amproject;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class EditarCartaoActivity extends AppCompatActivity {

    private EditText etNomeCartao;
    private EditText etNumeroCartao;
    private EditText etCodigoSeg;
    private EditText etDataVenc;
    private Spinner spBandeira;
    private RadioGroup rgSimNao;

    private EditText etRuaEnderecoCartao;
    private EditText etNumeroEnderecoCartao;
    private EditText etComplementoEnderecoCartao;
    private EditText etBairroEnderecoCartao;
    private EditText etCidadeEnderecoCartao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_cartao);

        Toolbar toolbar = (Toolbar)findViewById(R.id.tb_editar_cartao);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadFields();
        String params = cardId();

        GetAllInformationsAboutCard gaic = new GetAllInformationsAboutCard();
        gaic.execute(params);

    }



    private class GetAllInformationsAboutCard extends AsyncTask<String, Void, String>{

        ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(EditarCartaoActivity.this,"Aguarde","Coletando seus dados");
        }

        @Override
        protected String doInBackground(String... strings) {
            URL url;

            try {
                url = new URL("http://paguefacilbinatron.azurewebsites.net/api/CadastroCartaoWeb/"+strings[0]);
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

                boolean deuErro = true;
                JSONObject jsonObjectInfos = new JSONObject(s);
                String mensagemRetorno = jsonObjectInfos.getString("MensagemRetorno");
                deuErro = jsonObjectInfos.getBoolean("DeuCerto");

                if(!deuErro){

                    Toast.makeText(getApplicationContext(),"Dados atualizados",Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(getApplicationContext(),mensagemRetorno,Toast.LENGTH_SHORT).show();
                }



            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }


    private void loadFields() {
        etNomeCartao = (EditText)findViewById(R.id.et_nome_editar_cartao);
        etNumeroCartao = (EditText)findViewById(R.id.et_numerocartao_editar_cartao);
        etCodigoSeg = (EditText)findViewById(R.id.et_cod_seg_editar_cartao);
        etDataVenc = (EditText)findViewById(R.id.et_data_venc_editar_cartao);
        spBandeira = (Spinner)findViewById(R.id.sp_bandeira_editar_cartao);
        rgSimNao = (RadioGroup)findViewById(R.id.rg_sim_nao_editar_cartao);

        etRuaEnderecoCartao = (EditText)findViewById(R.id.et_rua_editar_cartao);
        etNumeroEnderecoCartao = (EditText)findViewById(R.id.et_numero_editar_cartao);
        etComplementoEnderecoCartao = (EditText)findViewById(R.id.et_complemento_editar_cartao);
        etBairroEnderecoCartao = (EditText)findViewById(R.id.et_bairro_editar_cartao);
        etCidadeEnderecoCartao = (EditText)findViewById(R.id.et_cidade_editar_cartao);
    }

    private String cardId() {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


        return "cartaoId"+sp.getString("idCartao",null)+"&usuarioId="+sp.getString("id",null);

    }
}
