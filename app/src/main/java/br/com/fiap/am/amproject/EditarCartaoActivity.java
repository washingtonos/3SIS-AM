package br.com.fiap.am.amproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
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
import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.fiap.am.model.MaskWatcher;

public class EditarCartaoActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etNomeCartao;
    private EditText etNumeroCartao;
    private EditText etCodigoSeg;
    private EditText etDataVenc;
    private Spinner spBandeira;
    private RadioGroup rgSimNao;
    private RadioButton rbSim;
    private RadioButton rbNao;

    private EditText etRuaEnderecoCartao;
    private EditText etNumeroEnderecoCartao;
    private EditText etComplementoEnderecoCartao;
    private EditText etBairroEnderecoCartao;
    private EditText etCidadeEnderecoCartao;
    private EditText etEstadoEndereco;
    private EditText etCep;

    private Button btConfirmaEdicao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_cartao);

        Toolbar toolbar = (Toolbar)findViewById(R.id.tb_editar_cartao);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadFields();
        rbSim.setOnClickListener(this);
        rbNao.setOnClickListener(this);
        String params = cardId();


        GetAllInformationsAboutCard gaic = new GetAllInformationsAboutCard();
        gaic.execute(params);



    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.bt_confirma_edicao_cartao:
                SendAllInformationsToUpdate saitu = new SendAllInformationsToUpdate();
                String date []= etDataVenc.getText().toString().split("/");
                String correctDate = date[2]+"-"+date[1]+"-"+date[0];

               EditText allEditText [] = {etNumeroCartao
                       ,etNomeCartao,
                       etDataVenc,
                       etCodigoSeg,
                        etRuaEnderecoCartao,
                        etNumeroEnderecoCartao,
                        etBairroEnderecoCartao,
                        etComplementoEnderecoCartao,
                        etEstadoEndereco,
                        etCep,
                        etCidadeEnderecoCartao};

                for(int i = 0;i<allEditText.length;i++){
                    if(allEditText[i].getText().toString().trim().length()==0){
                        Toast.makeText(getApplicationContext(),"Ha campos vazios, preencha-os",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                String param = cardId();
                saitu.execute(
                        param,
                        etNumeroCartao.getText().toString(),
                        etNomeCartao.getText().toString(),
                        correctDate,
                        spBandeira.getSelectedItem().toString(),
                        etCodigoSeg.getText().toString(),
                        etRuaEnderecoCartao.getText().toString(),
                        etNumeroEnderecoCartao.getText().toString(),
                        etBairroEnderecoCartao.getText().toString(),
                        etComplementoEnderecoCartao.getText().toString(),
                        etEstadoEndereco.getText().toString(),
                        etCep.getText().toString().replace("-",""),
                        etCidadeEnderecoCartao.getText().toString()
                        );
                break;
            case R.id.rb_sim_editar_cartao:
                controllTextFields(true);
                break;
            case R.id.rb_nao_editar_cartao:
                controllTextFields(false);
                break;
        }

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
                url = new URL("http://paguefacilbinatron.azurewebsites.net/api/CadastroCartaoWeb/?"+strings[0]);
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
                }else if(connection.getResponseCode()==500) {
                    return "500";
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
            if(s!=null){
                if(!s.equals("500")){
                    try {

                        boolean deuErro = true;
                        JSONObject jsonObjectInfos = new JSONObject(s);
                        String mensagemRetorno = jsonObjectInfos.getString("MensagemRetorno");
                        deuErro = jsonObjectInfos.getBoolean("DeuErro");

                        if(!deuErro){


                            JSONObject jsonObjectCartao = jsonObjectInfos.getJSONObject("CartaoCredito");
                            String idCartao = jsonObjectCartao.getString("Id");
                            String usuarioId = jsonObjectCartao.getString("UsuarioId");
                            String numeroCartao = jsonObjectCartao.getString("Numero");
                            String nomeTitular = jsonObjectCartao.getString("NomeTitular");
                            String validade = jsonObjectCartao.getString("Validade");
                            String bandeira = jsonObjectCartao.getString("Bandeira");
                            String codSeg = jsonObjectCartao.getString("CodigoSeguranca");

                            JSONObject jsonObjectEnderecoCartao = jsonObjectInfos.getJSONObject("EnderecoCartao");
                            String ruaEndereco = jsonObjectEnderecoCartao.getString("Rua");
                            String numeroEndereco = jsonObjectEnderecoCartao.getString("Numero");
                            String bairroEndereco = jsonObjectEnderecoCartao.getString("Bairro");
                            String complementoEndereco = jsonObjectEnderecoCartao.getString("Complemento");
                            String estado = jsonObjectEnderecoCartao.getString("Estado");
                            String cidade = jsonObjectEnderecoCartao.getString("Cidade");
                            String cep = jsonObjectEnderecoCartao.getString("Cep");

                            etNomeCartao.setText(nomeTitular);
                            etNumeroCartao.setText(numeroCartao);
                            etCodigoSeg.setText(codSeg);
                            etDataVenc.addTextChangedListener(new MaskWatcher("##/##/####"));
                            String validadeCartao[]=validade.split("(?<=\\G.{10})");
                            String dataInversa [] = validadeCartao[0].split("-");
                            etDataVenc.setText(dataInversa[2]+"/"+dataInversa[1]+"/"+dataInversa[0]);
                            spBandeira.setSelection(bandeira.equals("Visa")?1:2);
                            //rgSimNao;

                            etRuaEnderecoCartao.setText(ruaEndereco);
                            etNumeroEnderecoCartao.setText(numeroEndereco);
                            etComplementoEnderecoCartao.setText(complementoEndereco);
                            etBairroEnderecoCartao.setText(bairroEndereco);
                            etEstadoEndereco.setText(estado);
                            etCidadeEnderecoCartao.setText(cidade);
                            cep = new StringBuilder(cep).insert(cep.length()-3,"-").toString();
                            etCep.setText(cep);




                        }else{
                            Toast.makeText(getApplicationContext(),mensagemRetorno,Toast.LENGTH_SHORT).show();
                        }



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else{
                    Toast.makeText(getApplicationContext(),"Ocorreu um erro ao buscar os seus dados",Toast.LENGTH_SHORT).show();
                }

            }else {
                Toast.makeText(getApplicationContext(),"Ocorreu um erro inesperado",Toast.LENGTH_SHORT).show();
            }



        }
    }


    private class SendAllInformationsToUpdate extends AsyncTask<String,Void,Integer>{

        ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(EditarCartaoActivity.this,"Aguarde","Atualizando dados do cartäo");
        }

        @Override
        protected Integer doInBackground(String... params) {

            URL url;

            try {
                url = new URL("http://paguefacilbinatron.azurewebsites.net/api/CadastroCartaoWeb/?"+params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("PUT");
                connection.setRequestProperty("Content-Type","application/json");

                JSONObject jsonParamsCartao = new JSONObject();
                jsonParamsCartao.put("Numero",params[1]);
                jsonParamsCartao.put("NomeTitular",params[2]);
                jsonParamsCartao.put("Validade",params[3]);
                jsonParamsCartao.put("Bandeira",params[4]);
                jsonParamsCartao.put("CodigoSeguranca",params[5]);

                JSONObject jsonUsuarioObject = new JSONObject();
                jsonUsuarioObject.put("CartaoCredito",jsonParamsCartao);

                JSONObject jsonParamsEndereco = new JSONObject();
                jsonParamsEndereco.put("Rua",params[6]);
                jsonParamsEndereco.put("Numero",params[7]);
                jsonParamsEndereco.put("Bairro",params[8]);
                jsonParamsEndereco.put("Complemento",params[9]);
                jsonParamsEndereco.put("Estado",params[10]);
                jsonParamsEndereco.put("Cep",params[11]);
                jsonParamsEndereco.put("Cidade",params[12]);

                jsonUsuarioObject.put("EnderecoCartao",jsonParamsEndereco);

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

                Toast.makeText(getApplicationContext(),"Dados gravados com sucesso",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EditarCartaoActivity.this,MenuActivity.class);
                startActivity(intent);
                finish();

            }else{
                Toast.makeText(getApplicationContext(),"Ocorreu um erro ao gravar seus dados",Toast.LENGTH_SHORT).show();
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
        rbSim = (RadioButton)findViewById(R.id.rb_sim_editar_cartao);
        rbNao = (RadioButton)findViewById(R.id.rb_nao_editar_cartao);

        etRuaEnderecoCartao = (EditText)findViewById(R.id.et_rua_editar_cartao);
        etNumeroEnderecoCartao = (EditText)findViewById(R.id.et_numero_editar_cartao);
        etComplementoEnderecoCartao = (EditText)findViewById(R.id.et_complemento_editar_cartao);
        etBairroEnderecoCartao = (EditText)findViewById(R.id.et_bairro_editar_cartao);
        etCidadeEnderecoCartao = (EditText)findViewById(R.id.et_cidade_editar_cartao);
        etEstadoEndereco = (EditText)findViewById(R.id.et_estado_editar_cartao);
        etCep = (EditText)findViewById(R.id.et_cep_editar_cartao);



        btConfirmaEdicao = (Button)findViewById(R.id.bt_confirma_edicao_cartao);
        btConfirmaEdicao.setOnClickListener(this);

        rbSim.setChecked(true);
        controllTextFields(rbSim.isChecked()?true:false);




    }

    private String cardId() {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


        return "cartaoId="+sp.getString("idCartao",null)+"&usuarioId="+sp.getString("id",null);

    }

    public void controllTextFields(boolean trueOrFalse){

        if(trueOrFalse==true){
            etRuaEnderecoCartao.setEnabled(false);
            etRuaEnderecoCartao.setEnabled(false);
            etNumeroEnderecoCartao.setEnabled(false);
            etComplementoEnderecoCartao.setEnabled(false);
            etBairroEnderecoCartao.setEnabled(false);
            etCidadeEnderecoCartao.setEnabled(false);
            etEstadoEndereco.setEnabled(false);
            etCep.setEnabled(false);

        }else{
            etRuaEnderecoCartao.setEnabled(true);
            etNumeroEnderecoCartao.setEnabled(true);
            etComplementoEnderecoCartao.setEnabled(true);
            etBairroEnderecoCartao.setEnabled(true);
            etCidadeEnderecoCartao.setEnabled(true);
            etEstadoEndereco.setEnabled(true);
            etCep.setEnabled(true);
            etCep.addTextChangedListener(new MaskWatcher("#####-###"));

        }


    }
}
