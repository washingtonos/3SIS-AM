package br.com.fiap.am.amproject;

import android.app.Activity;
import android.app.LauncherActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.fiap.am.bean.Item;
import br.com.fiap.am.model.CustomAdapter;

public class MenuActivity extends AppCompatActivity {

    private ListView listView;
    private ListView listaVenda;
    private ImageView imageViewQrCode;
    private FloatingActionButton fab;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            MenuAsynTaskManager menuAsynTaskManager = new MenuAsynTaskManager();
            Button button = (Button)findViewById(R.id.button_ler_qr_code);
            switch (item.getItemId()) {
                case R.id.navigation_my_account:
                    // mTextMessage.setText(R.string.title_account);
                    button.setVisibility(View.GONE);
                    listaVenda.setVisibility(View.VISIBLE);
                    imageViewQrCode.setVisibility(View.GONE);
                    fab.setVisibility(View.GONE);
                    return true;
                case R.id.navigation_buy:
                    //mTextMessage.setText(R.string.title_buy);
                    button.setVisibility(View.VISIBLE);
                    listaVenda.setVisibility(View.GONE);
                    imageViewQrCode.setVisibility(View.VISIBLE);
                    fab.setVisibility(View.GONE);
                    return true;
                case R.id.navigation_sell:
                    //mTextMessage.setText(R.string.title_sell);
                    button.setVisibility(View.GONE);
                    listaVenda.setVisibility(View.VISIBLE);
                    imageViewQrCode.setVisibility(View.GONE);
                    fab.setVisibility(View.VISIBLE);
                    menuAsynTaskManager.execute();
                    return true;

            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);



        //Capturar Listview
        listView = (ListView) findViewById(R.id.lv_historico);
        listaVenda = (ListView) findViewById(R.id.listaCustom);

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
        menuAsynTaskManager.execute();

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
        switch (item.getItemId()){

            case R.id.item_configuracao:
                break;
            case R.id.item_sair:
                Intent intent = new Intent(MenuActivity.this,LoginActivity.class);
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



            try{

                StringBuilder sb;
                String linha;

                URL url = new URL("http://paguefacilbinatron.azurewebsites.net/api/ProdutoParaVenderWeb");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept", "application/json");

                int i = connection.getResponseCode();

                if(connection.getResponseCode() == 200){
                    BufferedReader stream =
                            new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    linha = "";
                    sb = new StringBuilder();
                    while((linha = stream.readLine()) != null){
                        sb.append(linha);
                    }

                    connection.disconnect();

                    return sb.toString();
                }else {
                    Log.i("Erro no http",String.valueOf(connection.getResponseCode()));
                }


            }catch (Exception e){
                e.printStackTrace();
            }



            return null;
        }

        @Override
        protected void onPostExecute(String s) {
           progressDialog.dismiss();
           // ArrayAdapter adapter;
            CustomAdapter adapter;
            if(s != null) {

                try {

                    JSONArray jsonArray = new JSONArray(s);
                    //JSONArray jsonArray = json.getJSONArray("ProdutoParaVender");

                   // List<Item> itens = new ArrayList<>();

                            /*Item item = new Item();
                            item.setNome("rosca");
                            item.setDescricao("redondo");
                            item.setPreco("2.50");
                            item.setQuantidade("1");
                            List<Item> listaItensConta = new ArrayList<>();
                            listaItensConta.removeAll(listaItensConta);
                            listaItensConta.add(item);
                            adapter = new CustomAdapter(listaItensConta, MenuActivity.this);
                            listaVenda.setAdapter(adapter);*/
                            /*Item itemVenda = new Item();
                            itemVenda.setNome("rosca");
                            itemVenda.setDescricao("redondo");
                            itemVenda.setPreco("2.50");
                            itemVenda.setQuantidade("1");
                            List<Item> listaItensVenda = new ArrayList<>();
                            listaItensVenda.removeAll(listaItensVenda);
                            listaItensVenda.add(itemVenda);*/


                            /*for(int i = 0; i < jsonArray.length(); i++){

                                JSONObject item = (JSONObject) jsonArray.get(i);

                                int id = item.getInt("Id");
                                String nome = item.getString("Nome");
                                String preco = item.getString("Preco");
                                String descricao = item.getString("Preco");
                                String quantidade = item.getString("Preco");
                                String imagem = item.getString("ImagemUrl");
                                int usuarioId = item.getInt("UsuarioId");
                                String usuario = item.getString("Usuario");

                                itens.add(new Item(id, nome, preco, descricao, quantidade, imagem, usuarioId, usuario));

                            }*/

                            adapter = new CustomAdapter(jsonArray, MenuActivity.this);

                            listaVenda.setAdapter(adapter);

                        /*List<String> listaItensVenda = new ArrayList<String>();
                        listaItensVenda.removeAll(listaItensVenda);
                        listaItensVenda.add("item 2");
                        listaItensVenda.add("item 3");
                        adapter = new ArrayAdapter(MenuActivity.this,android.R.layout.simple_list_item_1,listaItensVenda);
                        listView.setAdapter(adapter);*/




                }catch (Exception e){
                    e.printStackTrace();
                }

            }else{

                Toast.makeText(MenuActivity.this,"Errando ainda ", Toast.LENGTH_LONG).show();
            }
            super.onPostExecute(s);
        }

    }
}
