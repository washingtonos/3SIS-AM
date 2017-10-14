package br.com.fiap.am.amproject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.common.StringUtils;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import br.com.fiap.am.model.Produto;

public class MenuActivity extends AppCompatActivity {

    private ListView listView;
    private ImageView imageViewQrCode;
    private FloatingActionButton fab;
    LinearLayout linearLayoutAccount;
    private ListView llProdutoCadastrado;
    private String nomes[];
    private String precos [];
    private String paths [];
    private RelativeLayout rlBotao;
    private RelativeLayout rlProduto;
    private RelativeLayout rlHistorico;
    private RelativeLayout rlCompra;
    private String mesesDoAno [] = {
            "Janeiro",
            "Fevereiro",
            "Marco",
            "Abril",
            "Maio",
            "Junho",
            "Julho",
            "Agosto",
            "Setembro",
            "Outubro",
            "Novembro",
            "Dezembro"};

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            ListAllTransactions listAllTransactions = new ListAllTransactions();
            Button button = (Button)findViewById(R.id.button_ler_qr_code);
            String id = loadSharedPreferences();
            switch (item.getItemId()) {
                case R.id.navigation_my_account:
                    getSupportActionBar().setTitle("Historico");
                    listAllTransactions.execute(id);
                    rlProduto.setVisibility(View.GONE);
                    rlHistorico.setVisibility(View.VISIBLE);
                    rlCompra.setVisibility(View.GONE);
                    rlBotao.setVisibility(View.GONE);
                    return true;
                case R.id.navigation_buy:
                    getSupportActionBar().setTitle("Escanear QR code");
                    rlCompra.setVisibility(View.VISIBLE);
                    rlHistorico.setVisibility(View.GONE);
                    rlProduto.setVisibility(View.GONE);
                    rlBotao.setVisibility(View.GONE);
                    return true;
                case R.id.navigation_sell:
                    getSupportActionBar().setTitle("Itens Cadastrados");
                    rlHistorico.setVisibility(View.GONE);
                    rlCompra.setVisibility(View.GONE);
                    ListAllItemsToSell laits = new ListAllItemsToSell();
                    laits.execute();
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
                connection.setConnectTimeout(4000);
                connection.setReadTimeout(4000);
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

                }else if(connection.getResponseCode()==500){
                    return"500";

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
                        ArrayList<Produto> listaDeProdutos = new ArrayList<Produto>();
                        int index=0;
                        //JSONObject jsonObjectDadosProdutoParaVender = new JSONObject(s);
                        JSONArray arrayOsItems = new JSONArray(s);
                        for (int i = 0;i<arrayOsItems.length();i++){

                            JSONObject jsonObject = (JSONObject)arrayOsItems.get(i);



                            if(jsonObject.getString("UsuarioId").equals(loadSharedPreferences())){
                                Produto ppv = new Produto();
                                ppv.getUsuario().setId(loadSharedPreferences());
                                ppv.setId(jsonObject.getString("Id"));
                                ppv.setNome(jsonObject.getString("Nome"));
                                ppv.setPreco(jsonObject.getString("Preco"));
                                ppv.setImagemUrl(jsonObject.getString("ImagemUrl"));
                                listaDeProdutos.add(ppv);

                            }
                        }


                        llProdutoCadastrado.setAdapter(new CustomAdapter(MenuActivity.this,listaDeProdutos));

                        rlProduto.setVisibility(View.VISIBLE);
                        rlBotao.setVisibility(View.VISIBLE);



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{

                    Toast.makeText(getApplicationContext(),"Ocorreu um erro ao lista",Toast.LENGTH_SHORT);

                }
            }else{
                Toast.makeText(getApplicationContext(),"Ocorreu um erro inesperado",Toast.LENGTH_SHORT);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        //capturar Relative Layout
        rlBotao = (RelativeLayout)findViewById(R.id.rl_botao);
        rlHistorico = (RelativeLayout)findViewById(R.id.rl_historico);
        rlProduto = (RelativeLayout)findViewById(R.id.rl_produtos);
        rlCompra = (RelativeLayout)findViewById(R.id.rl_compra);
        rlProduto.setVisibility(View.GONE);
        rlCompra.setVisibility(View.GONE);
        rlBotao.setVisibility(View.GONE);


        //Capturar Listview
        //listView = (ListView) findViewById(R.id.lv_historico);
        //listView.setVisibility(View.GONE);

        llProdutoCadastrado = (ListView)findViewById(R.id.ll_itens_cadastrados);
        //llProdutoCadastrado.setVisibility(View.GONE);

        //Capturar Imagem de QrCode
        imageViewQrCode = (ImageView)findViewById(R.id.imv_qrcode);
        //imageViewQrCode.setVisibility(View.INVISIBLE);

        //Coloca botao invisivel
        Button button = (Button)findViewById(R.id.button_ler_qr_code);
        //button.setVisibility(View.INVISIBLE);

        //set Toolbar
        Toolbar mToolBar = (Toolbar)findViewById(R.id.toolbar_menuactivity);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("Historico");

        rlHistorico.setVisibility(View.VISIBLE);
        ListAllTransactions listAllTransactions = new ListAllTransactions();
        listAllTransactions.execute(loadSharedPreferences());


        fab = (FloatingActionButton) findViewById(R.id.fbt_add);
        //fab.setVisibility(View.GONE);
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

            if(data.getExtras()!=null){
                String scan_result = data.getStringExtra("SCAN_RESULT");
                String dadosResult [] = scan_result.split("\\|");
                String result = intentResult.getContents();
                Intent intent = new Intent(MenuActivity.this,ConfirmarCompraActivity.class);
                intent.putExtra("DataHora",dadosResult[1]);
                intent.putExtra("Nome",dadosResult[2]);
                intent.putExtra("Preco",dadosResult[3]);
                intent.putExtra("Quantidade",dadosResult[5]);
                intent.putExtra("UsuarioId",dadosResult[6]);
                intent.putExtra("Id",dadosResult[7]);
                startActivity(intent);
                finish();
            }


        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private class ListAllTransactions extends AsyncTask<String,Void,String>{

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(MenuActivity.this,"Carregando Lista","Aguarde um instante");
        }

        @Override
        protected String doInBackground(String... strings) {



            try {
                URL url = new URL("http://paguefacilbinatron.azurewebsites.net/api/TransacionarWeb/"+strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(4000);
                connection.setReadTimeout(4000);
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
            progressDialog.dismiss();

            JSONArray arrayOfSoldItems = null;

            if(s!=null){
                if(!s.equals("500")){

                    try {
                        arrayOfSoldItems = new JSONArray(s);
                        createChildsProgrammatically(arrayOfSoldItems);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(getApplicationContext(),"Erro ao lista",Toast.LENGTH_SHORT).show();
                }

            }else{
                Toast.makeText(getApplicationContext(),"Erro inesperado tente novamente",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String loadSharedPreferences(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        return sp.getString("id",null);
    }


    private void createChildsProgrammatically(JSONArray jsonArray){


        TableLayout tblayout = (TableLayout)findViewById(R.id.ll_for_months);
        tblayout.removeAllViews();
        String mesValida="";
        int indexMes=0;
        TableRow.LayoutParams trParams = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,(float)1);

        for (int i = 0;i<jsonArray.length();i++){

            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String dataTransacao = jsonObject.getString("DataTransacao");
                String comprador = jsonObject.getString("CompradorId");
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                Calendar calendar = Calendar.getInstance();
                Date date = dateFormat.parse(dataTransacao);
                calendar.setTime(date);

                String month = new SimpleDateFormat("MMMM").format(calendar.getTime());

                if(!month.equalsIgnoreCase(mesValida)){

                    TableRow tableRow = new TableRow(this);

                    TextView texto = new TextView(this);
                    texto.setLayoutParams(trParams);
                    String primeiraLetra = month.substring(0,1).toUpperCase();
                    String mesComLetraCapital = primeiraLetra+month.substring(1);

                    texto.setText(mesComLetraCapital);
                    texto.setTextSize(18);
                    texto.setTypeface(Typeface.DEFAULT_BOLD);
                    texto.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark,null));
                    texto.setTextColor(getResources().getColor(R.color.colorSecondary,null));
                    texto.setGravity(Gravity.CENTER);
                    texto.setPadding(32,32,32,32);
                    tableRow.addView(texto);
                    tblayout.addView(tableRow);

                    mesValida = month;

                }

                TableRow tableRow = new TableRow(this);

                JSONObject produtoParaVenderObject = jsonObject.getJSONObject("ProdutoParaVender");
                TextView texto = new TextView(this);
                texto.setLayoutParams(trParams);
                texto.setText(produtoParaVenderObject.getString("Nome"));
                texto.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight,null));
                texto.setPadding(64,32,16,32);
                tableRow.addView(texto);

                TextView textoTeste = new TextView(this);
                textoTeste.setLayoutParams(trParams);
                textoTeste.setText(comprador.equals(loadSharedPreferences())?"--"+produtoParaVenderObject.getString("Preco"):"++"+produtoParaVenderObject.getString("Preco"));
                textoTeste.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight,null));
                textoTeste.setPadding(16,32,64,32);
                textoTeste.setGravity(Gravity.RIGHT);
                textoTeste.setTextColor(comprador.equals(loadSharedPreferences())?getResources().getColor(R.color.colorSecondaryDark,null):getResources().getColor(R.color.colorCheck,null));
                tableRow.addView(textoTeste);
                tblayout.addView(tableRow);


            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

    }


}
