package br.com.fiap.am.amproject;

import android.app.Activity;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity {

    private ListView listView;
    private ImageView imageViewQrCode;
    private FloatingActionButton fab;
    LinearLayout linearLayoutAccount;

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
                    //mTextMessage.setText(R.string.title_sell);
                    button.setVisibility(View.INVISIBLE);
                    imageViewQrCode.setVisibility(View.INVISIBLE);
                    fab.setVisibility(View.VISIBLE);
                    menuAsynTaskManager.execute("sell");
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
        listView.setVisibility(View.GONE);

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
                    case "sell":
                        List<String> listaItensVenda = new ArrayList<String>();
                        listaItensVenda.removeAll(listaItensVenda);
                        listaItensVenda.add("item 2");
                        listaItensVenda.add("item 3");
                        adapter = new ArrayAdapter(MenuActivity.this,android.R.layout.simple_list_item_1,listaItensVenda);
                        listView.setAdapter(adapter);
                }
            }

            super.onPostExecute(s);
        }
    }
}
