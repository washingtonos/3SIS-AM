package br.com.fiap.am.amproject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import br.com.fiap.am.model.ProdutoParaVender;

public class MenuActivity extends AppCompatActivity {

    private ListView listView;
    private ImageView imageViewQrCode;
    private FloatingActionButton fab;
    LinearLayout linearLayoutAccount;
    private ListView llProdutoCadastrado;
    private String nomes[];
    private String precos [];
    private String paths [];

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            MenuAsynTaskManager menuAsynTaskManager = new MenuAsynTaskManager();
            Button button = (Button)findViewById(R.id.button_ler_qr_code);
            switch (item.getItemId()) {
                case R.id.navigation_my_account:
                   // mTextMessage.setText(R.string.title_account);
                    button.setVisibility(View.INVISIBLE);
                    listView.setVisibility(View.VISIBLE);
                    imageViewQrCode.setVisibility(View.INVISIBLE);
                    fab.setVisibility(View.GONE);
                    menuAsynTaskManager.execute("account");
                    return true;
                case R.id.navigation_buy:
                    //mTextMessage.setText(R.string.title_buy);
                    button.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.INVISIBLE);
                    imageViewQrCode.setVisibility(View.VISIBLE);
                    fab.setVisibility(View.GONE);
                    return true;
                case R.id.navigation_sell:
                    button.setVisibility(View.INVISIBLE);
                    imageViewQrCode.setVisibility(View.INVISIBLE);
                    fab.setVisibility(View.VISIBLE);
                    ListAllItemsToSell laits = new ListAllItemsToSell();
                    String id = loadSharedPreferences();
                    laits.execute();
                    llProdutoCadastrado.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                    return true;

            }
            return false;
        }

    };


    private class ListAllItemsToSell extends AsyncTask<String,Void,String>{

        ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(MenuActivity.this,"Aguarde","Carregando Itens");
        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                URL url = new URL("http://paguefacilbinatron.azurewebsites.net/api/ProdutoParaVenderWeb/");
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

                    Toast.makeText(getApplicationContext(),"Ocorreu um erro ao buscar os produtos",Toast.LENGTH_SHORT).show();

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

                ArrayList<ProdutoParaVender> listaDeProdutos = new ArrayList<ProdutoParaVender>();
                int index=0;
                //JSONObject jsonObjectDadosProdutoParaVender = new JSONObject(s);
                JSONArray arrayOsItems = new JSONArray(s);
                for (int i = 0;i<arrayOsItems.length();i++){

                    JSONObject jsonObject = (JSONObject)arrayOsItems.get(i);



                    if(jsonObject.getString("UsuarioId").equals(loadSharedPreferences())){
                        ProdutoParaVender ppv = new ProdutoParaVender();
                        ppv.setNome(jsonObject.getString("Nome"));
                        ppv.setPreco(jsonObject.getString("Preco"));
                        ppv.setImagemUrl(jsonObject.getString("ImagemUrl"));
                        listaDeProdutos.add(ppv);

                    }
                }


                llProdutoCadastrado.setAdapter(new CustomAdapter(MenuActivity.this,listaDeProdutos));




            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(s);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);



        //Capturar Listview
        listView = (ListView) findViewById(R.id.lv_historico);
        listView.setVisibility(View.GONE);

        llProdutoCadastrado = (ListView)findViewById(R.id.ll_itens_cadastrados);
        llProdutoCadastrado.setVisibility(View.GONE);

        //Capturar Imagem de QrCode
        imageViewQrCode = (ImageView)findViewById(R.id.imv_qrcode);
        imageViewQrCode.setVisibility(View.INVISIBLE);

        //Coloca botao invisivel
        Button button = (Button)findViewById(R.id.button_ler_qr_code);
        button.setVisibility(View.INVISIBLE);

        //set Toolbar
        Toolbar mToolBar = (Toolbar)findViewById(R.id.toolbar_menuactivity);
        setSupportActionBar(mToolBar);

        MenuAsynTaskManager menuAsynTaskManager = new MenuAsynTaskManager();
        menuAsynTaskManager.execute("account");

        fab = (FloatingActionButton) findViewById(R.id.fbt_add);
        fab.setVisibility(View.GONE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(MenuActivity.this,CadastrarVendaNomeProdutoActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent;
        switch (item.getItemId()){

            case R.id.item_editar_perfil:
                intent = new Intent(MenuActivity.this,EditarPerfilActivity.class);
                startActivity(intent);
                break;
            case R.id.item_editar_cartao:
                intent = new Intent(MenuActivity.this,EditarCartaoActivity.class);
                startActivity(intent);
                break;
            case R.id.item_cadastrar_cartao:
                intent = new Intent(MenuActivity.this,CadastrarCartaoActivity.class);
                startActivity(intent);
                break;
            case R.id.item_sair:
                intent = new Intent(MenuActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
                break;

        }


        return super.onOptionsItemSelected(item);
    }

    public void callQrCodeReader(View view) {
        final Activity activity= this;
        IntentIntegrator intentIntegrator = new IntentIntegrator(activity);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES)
                .setPrompt("Scan")
                .setCameraId(0)
                .setBeepEnabled(false)
                .setBarcodeImageEnabled(false)
                .initiateScan();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);

        if(intentResult.getContents()!=null){
            Intent intent = new Intent(MenuActivity.this,ConfirmarCompraActivity.class);
            startActivity(intent);
            finish();

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private class MenuAsynTaskManager extends AsyncTask<String,Void,String>{

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(MenuActivity.this,"Carregando Lista","Aguarde um instante");
        }

        @Override
        protected String doInBackground(String... strings) {
            return strings[0];
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            ArrayAdapter adapter;
            if(!s.equals("")){
                switch (s){
                    case "account":
                        List<String> listaItensConta = new ArrayList<String>();
                        listaItensConta.removeAll(listaItensConta);
                        listaItensConta.add("item 1");
                        listaItensConta.add("item 2");
                        adapter = new ArrayAdapter(MenuActivity.this,android.R.layout.simple_list_item_1,listaItensConta);

                        listView.setAdapter(adapter);
                        break;
                }
            }

            super.onPostExecute(s);
        }
    }

    private String loadSharedPreferences(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        return sp.getString("id",null);
    }


}
