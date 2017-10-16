package br.com.fiap.am.amproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
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
import java.util.Locale;

public class ConfirmarCompraActivity extends AppCompatActivity {


    private String dataHora;
    private String nome;
    private String preco;
    private String quantidade;
    private String usuarioId;
    private String idProduto;

    private TextView tvNomeProduto;
    private TextView tvPrecoProduto;
    private TextView tvQuantidadeProduto;
    private TextView tvUsuarioId;
    private TextView tvNomeVendedor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmar_compra);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        loadFields();



        if(getIntent().getExtras()!=null){
            Bundle extras = getIntent().getExtras();




                String dataHora = extras.getString("DataHora");
                String nome = extras.getString("Nome");
                String preco = extras.getString("Preco");
                String quantidade = extras.getString("Quantidade");
                usuarioId=extras.getString("UsuarioId");
                idProduto = extras.getString("Id");


                tvNomeProduto.setText("Estou comprando "+quantidade+" "+nome+"(s)");
                tvPrecoProduto.setText("Preco: R$ "+preco);

        }





    }

    private void loadFields() {

        tvNomeProduto = (TextView)findViewById(R.id.tv_produto_que_compra_confirma_compra);
        tvPrecoProduto = (TextView)findViewById(R.id.tv_preco_produto_confirmar_compra);




        //tvQuantidadeProduto = (TextView)findViewById(R.id.tv);
        //tvUsuarioId = (TextView)findViewById();
        //tvProdutoId = (TextView)findViewById();

    }

    public void callReturnToMenuActivity(View view) {

        Intent intent = new Intent(ConfirmarCompraActivity.this,MenuActivity.class);
        startActivity(intent);

    }

    public void completeTransaction(View view) {


        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date today = Calendar.getInstance().getTime();
        dataHora = format.format(today);
        SendAllInfosToTransaction satt = new SendAllInfosToTransaction();
        satt.execute(loadSharedPreferences(),usuarioId,idProduto,dataHora);


    }


    private class SendAllInfosToTransaction extends AsyncTask<String,Void,String>{

        ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(ConfirmarCompraActivity.this,"Aguarde","Estamos finalizando a transacao");
        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                URL url = new URL("http://paguefacilbinatron.azurewebsites.net/api/TransacionarWeb/");
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.setConnectTimeout(4000);
                connection.setReadTimeout(4000);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type","application/json");

                JSONObject jsonDadosTransacao = new JSONObject();
                jsonDadosTransacao.put("CompradorId",strings[0]);
                jsonDadosTransacao.put("VendedorId",strings[1]);
                jsonDadosTransacao.put("ProdutoParaVenderId",strings[2]);
                jsonDadosTransacao.put("DataTransacao",strings[3]);

                JSONObject transacaoObject = new JSONObject();
                transacaoObject.put("Transacao",jsonDadosTransacao);

                OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream());
                osw.write(transacaoObject.toString());
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

                }if(connection.getResponseCode()==500){
                    return "500";
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

            if(s!=null){
                if(!s.equals("500")){
                    try {
                        JSONObject responseObject = new JSONObject(s);
                        String mensagem = responseObject.getString("MensagemRetorno");
                        boolean deuErro = responseObject.getBoolean("DeuErro");

                        if(!deuErro){
                            Toast.makeText(getApplicationContext(),"Sua compra foi realizada!",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ConfirmarCompraActivity.this,MenuActivity.class);
                            startActivity(intent);
                            finish();
                        }else {
                            Toast.makeText(getApplicationContext(),mensagem,Toast.LENGTH_SHORT).show();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"Ocorreu um erro ao finalizar a transacao",Toast.LENGTH_SHORT).show();
                }

            }else{
                Toast.makeText(getApplicationContext(),"Ocorreu um erro inesperado",Toast.LENGTH_SHORT).show();
            }



        }
    }

    private String loadSharedPreferences(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        return sp.getString("id",null);
    }

}
