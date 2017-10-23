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
import android.widget.Button;
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
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import br.com.fiap.am.model.MaskWatcher;

public class CadastrarCartaoActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText etNomeCartao;
    private EditText etNumeroCartao;
    private EditText etCodSegCartao;
    private EditText etDataVencCartao;

    private Spinner spBandeiraCartao;
    private RadioGroup rgSimNao;
    private RadioButton rbSim;
    private RadioButton rbNao;
    private EditText etRuaEndereco;
    private EditText etNumeroEndereco;
    private EditText etComplementoEndereco;
    private EditText etBairroEndereco;
    private EditText etCidadeEndereco;
    private EditText etEstadoEndereco;
    private EditText etCepEndereco;

    private Button btCadastrarCartao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_cartao);

        Toolbar toolbar = (Toolbar)findViewById(R.id.tb_cadastrarCartao);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        loadFields();
        rbSim.setOnClickListener(this);
        rbNao.setOnClickListener(this);

        btCadastrarCartao.setOnClickListener(this);
    }

    private void loadFields() {
        etNomeCartao = (EditText)findViewById(R.id.et_nome_cadastrar_cartao);
        etNumeroCartao = (EditText)findViewById(R.id.et_numerocartao_cadastrar_cartao);
        etCodSegCartao = (EditText)findViewById(R.id.et_cod_seg_cadastrar_cartao);
        etDataVencCartao = (EditText)findViewById(R.id.et_data_venc_cadastrar_cartao);
        etDataVencCartao.addTextChangedListener(new MaskWatcher("##/##/####"));


        spBandeiraCartao = (Spinner)findViewById(R.id.sp_bandeira_cadastrar_cartao);
        rgSimNao = (RadioGroup)findViewById(R.id.rg_sim_nao_cadastrar_cartao);
        rbSim = (RadioButton)findViewById(R.id.rb_sim_cadastrar_cartao);
        rbNao = (RadioButton)findViewById(R.id.rb_nao_cadastrar_cartao);
        etRuaEndereco = (EditText)findViewById(R.id.et_rua_cadastrar_cartao);
        etNumeroEndereco = (EditText)findViewById(R.id.et_numero_cadastrar_cartao);
        etComplementoEndereco = (EditText)findViewById(R.id.et_complemento_cadastrar_cartao);
        etBairroEndereco = (EditText)findViewById(R.id.et_bairro_cadastrar_cartao);
        etCidadeEndereco = (EditText)findViewById(R.id.et_cidade_cadastrar_cartao);
        etEstadoEndereco = (EditText)findViewById(R.id.et_estado_cadastrar_cartao);
        etCepEndereco = (EditText)findViewById(R.id.et_cep_cadastrar_cartao);
        btCadastrarCartao = (Button)findViewById(R.id.bt_cadastrar_cartao);
        rbSim.setChecked(true);
        controllTextFields(rbSim.isChecked()?true:false);
        etCepEndereco.addTextChangedListener(new MaskWatcher("#####-###"));



    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_cadastrar_cartao:
                CadastrarDadosCartao cdc = new CadastrarDadosCartao();
                String dataDeTransacao = etDataVencCartao.getText().toString();
                dataDeTransacao.replace("/","-");

                String id = loadSharedPreferences();

                cdc.execute
                        (
                                id,
                                etNumeroCartao.getText().toString(),
                                etNomeCartao.getText().toString(),
                                dataDeTransacao,
                                spBandeiraCartao.getSelectedItem().toString(),
                                etCodSegCartao.getText().toString(),
                                etRuaEndereco.getText().toString(),
                                etNumeroEndereco.getText().toString(),
                                etBairroEndereco.getText().toString(),
                                etComplementoEndereco.getText().toString(),
                                etEstadoEndereco.getText().toString(),
                                etCepEndereco.getText().toString().replace("-",""),
                                etCidadeEndereco.getText().toString()

                        );
                break;
            case R.id.rb_sim_cadastrar_cartao:
                controllTextFields(true);
                break;
            case R.id.rb_nao_cadastrar_cartao:
                controllTextFields(false);
                break;
        }
    }

    private class CadastrarDadosCartao extends AsyncTask<String,Void,String>{

        ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(CadastrarCartaoActivity.this,"Aguarde","Cadastrando Cartao");
        }

        @Override
        protected String doInBackground(String... strings) {
            URL url;


            try {
                url = new URL("http://paguefacilbinatron.azurewebsites.net/api/CadastroCartaoWeb/");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type","application/json");


                JSONObject jsonObjectDadosCartoes = new JSONObject();
                jsonObjectDadosCartoes.put("UsuarioId",strings[0])
                        .put("Numero",strings[1])
                        .put("NomeTitular",strings[2])
                        .put("Validade",strings[3])
                        .put("Bandeira",strings[4])
                        .put("CodigoSeguranca",strings[5]);

                JSONObject jsonInfos = new JSONObject();
                jsonInfos.put("CartaoCredito",jsonObjectDadosCartoes);

                JSONObject jsonObjectEnderecoCartoes = new JSONObject();
                jsonObjectEnderecoCartoes.put("Rua",strings[6])
                        .put("Numero",strings[7])
                        .put("Bairro",strings[8])
                        .put("Complemento",strings[9])
                        .put("Estado",strings[10])
                        .put("Cep",strings[11])
                        .put("Cidade",strings[12]);

                jsonInfos.put("EnderecoCartao",jsonObjectEnderecoCartoes);


                OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream());
                osw.write(jsonInfos.toString());
                osw.close();

                if(connection.getResponseCode()==200){

                    String linha = "";
                    StringBuilder builder = new StringBuilder();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    while((linha=reader.readLine())!=null){
                        builder.append(linha);
                    }

                    connection.disconnect();

                    return builder.toString();

                }

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
        protected void onPostExecute(String s) {
            progress.dismiss();


            try {
                boolean deuErro = true;

                JSONObject jsonMensagem = new JSONObject(s);
                deuErro = jsonMensagem.getBoolean("DeuErro");
                String mensagem = jsonMensagem.getString("MensagemRetorno");

                if(!deuErro){

                    JSONObject jsonObjectCartao = jsonMensagem.getJSONObject("CartaoCredito");
                    String id = jsonObjectCartao.getString("Id");

                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("idCartao",id);
                    editor.putBoolean("isCartaoRecorded",true);
                    editor.commit();

                    Toast.makeText(getApplicationContext(), R.string.cartao_cadastrado,Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CadastrarCartaoActivity.this,MenuActivity.class);
                    startActivity(intent);
                    finish();

                }else{
                    Toast.makeText(getApplicationContext(),mensagem,Toast.LENGTH_SHORT).show();
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }



        }



    }

    private String loadSharedPreferences(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        return sp.getString("id",null);
    }


    public void controllTextFields(boolean trueOrFalse){

        if(trueOrFalse==true){
            etRuaEndereco.setEnabled(false);
            etNumeroEndereco.setEnabled(false);
            etComplementoEndereco.setEnabled(false);
            etBairroEndereco.setEnabled(false);
            etCidadeEndereco.setEnabled(false);
            etEstadoEndereco.setEnabled(false);
            etCepEndereco.setEnabled(false);

        }else{
            etRuaEndereco.setEnabled(true);
            etNumeroEndereco.setEnabled(true);
            etComplementoEndereco.setEnabled(true);
            etBairroEndereco.setEnabled(true);
            etCidadeEndereco.setEnabled(true);
            etEstadoEndereco.setEnabled(true);
            etCepEndereco.setEnabled(true);
            etCepEndereco.addTextChangedListener(new MaskWatcher("#####-###"));

        }


    }
}
