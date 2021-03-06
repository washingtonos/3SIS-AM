package br.com.fiap.am.amproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import br.com.fiap.am.model.Produto;

public class InformacoesSobreVendaActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imvDeuCerto;
    private ImageView imvImagemProduto;

    private TextView tvDeuCerto;
    private TextView tvInformacoesSobreVenda;
    private TextView tvParaVenderBasta;
    private TextView tvNomeVenda;
    private TextView tvPrecoVenda;
    private TextView tvNomeVendedor;//sera capturado em uma determinada chamada do servico ainda a ser feito

    private Button btQueroRegistrarEsseProduto;
    private Button btDesistiDaIdeia;
    private Button btNaoQueroVenderAgora;
    private Button btQueroVenderAgora;
    private Button btVendaAvulsa;
    private Button btAdicionarQuantidade;
    private Button btRemoverQuantidade;

    private EditText etQtd;

    private LinearLayout llQtd;

    private ScrollView scInformacoesSobreVenda;

    private String valorUnitarioMecadoria;

    private String path;

    private String idProduto;

    private SharedPreferences sp;

    private boolean createMenuDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacoes_sobre_venda);

        Toolbar toolbar = (Toolbar)findViewById(R.id.tb_informacoes_sobre_venda);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Campos que sao fixos, usados apenas para layout-------------------
        tvDeuCerto = (TextView)findViewById(R.id.tv_deu_certo);
        tvDeuCerto.setVisibility(View.GONE);

        tvInformacoesSobreVenda = (TextView)findViewById(R.id.tv_informacoes_sobre_venda);
        tvInformacoesSobreVenda.setVisibility(View.GONE);

        imvDeuCerto = (ImageView)findViewById(R.id.imv_deu_certo);
        imvDeuCerto.setVisibility(View.GONE);

        tvParaVenderBasta = (TextView)findViewById(R.id.tv_para_vender_basta);
        // Fim de campos que sao fixos, usados apenas para layout

        //Captura de botoes
        btQueroRegistrarEsseProduto = (Button)findViewById(R.id.bt_quero_registrar_esse_produto);
        btDesistiDaIdeia = (Button)findViewById(R.id.bt_nao_desisti_da_ideia);
        btNaoQueroVenderAgora = (Button)findViewById(R.id.bt_nao_quero_vender_agora);
        btVendaAvulsa = (Button)findViewById(R.id.bt_venda_avulsa);
        btQueroVenderAgora = (Button)findViewById(R.id.bt_quero_vender_agora);
        btAdicionarQuantidade = (Button)findViewById(R.id.bt_adicionar_qtd);
        btRemoverQuantidade = (Button)findViewById(R.id.bt_remover_qtd);

        //Captura de campos a serem setados
        imvImagemProduto = (ImageView)findViewById(R.id.imv_imagem_produto);
        tvNomeVenda = (TextView)findViewById(R.id.tv_nome_produto_informacoessobrevenda);
        tvPrecoVenda = (TextView)findViewById(R.id.tv_preco_produto_informacoessobrevenda);
        tvNomeVendedor = (TextView)findViewById(R.id.tv_nome_vendedor_informacoessobrevenda);
        etQtd = (EditText)findViewById(R.id.et_qtd_produto_informacoessobrevenda);

        //Capturar linearLayout
        llQtd = (LinearLayout)findViewById(R.id.ll_qtd);
        llQtd.setVisibility(View.GONE);

        //capturar scrollview
        scInformacoesSobreVenda = (ScrollView)findViewById(R.id.sv_informacoessobrevenda);

        Bundle extras = getIntent().getExtras();
        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        /*Validando de qual activity e originado. Se for BuscarImagensActivity, enconde-se os outros
            botoes e deixa-se o btNaoqueroVenderAgora inativo. Isso significa que o usuario deve ter
            seguido o fluxo de vendas.*/
        if(extras!=null){
            if(extras.getString("classe").equalsIgnoreCase("buscarimagensactivity")){

                createMenuDelete = false;

                //pegar os extras
                String nomeProduto = extras.getString("nomeProduto");
                valorUnitarioMecadoria = extras.getString("precoProduto");
                path = extras.getString("path");
                Bitmap bitmap = BitmapFactory.decodeFile(path);

                //Setar visibilidade de elementos na tela
                btNaoQueroVenderAgora.setVisibility(View.GONE);
                btQueroVenderAgora.setVisibility(View.GONE);
                tvParaVenderBasta.setVisibility(View.GONE);
                btQueroRegistrarEsseProduto.setVisibility(View.VISIBLE);
                btDesistiDaIdeia.setVisibility(View.VISIBLE);
                tvInformacoesSobreVenda.setVisibility(View.VISIBLE);

                //Setar informacoes na tela
                tvNomeVendedor.setText("Vendedor: "+ sp.getString("nome",null));
                tvNomeVenda.setText("Nome: "+nomeProduto);
                tvPrecoVenda.setText("Preco: "+valorUnitarioMecadoria);

                if(!path.equals("")&&path!=null){
                    imvImagemProduto.setImageBitmap(Bitmap.createScaledBitmap
                            (bitmap,bitmap.getWidth()/6,bitmap.getHeight()/6,true));
                }


            }else if(extras.getString("classe").equalsIgnoreCase("MenuActivity")){

                createMenuDelete=true;

                //pegar os extras
                String nomeProduto = extras.getString("nomeProduto");
                String valor [] = extras.getString("precoProduto").split(" ");
                valorUnitarioMecadoria = valor[1];
                path = extras.getString("path");
                idProduto = extras.getString("id");
                Bitmap bitmap = BitmapFactory.decodeFile(path);

                //Setar visibilidade de elementos na tela
                btNaoQueroVenderAgora.setVisibility(View.VISIBLE);
                btQueroRegistrarEsseProduto.setVisibility(View.GONE);
                btDesistiDaIdeia.setVisibility(View.GONE);
                btNaoQueroVenderAgora.setVisibility(View.VISIBLE);
                btQueroVenderAgora.setVisibility(View.VISIBLE);
                tvParaVenderBasta.setVisibility(View.GONE);
                btQueroRegistrarEsseProduto.setVisibility(View.GONE);
                btDesistiDaIdeia.setVisibility(View.GONE);
                tvInformacoesSobreVenda.setVisibility(View.VISIBLE);
                llQtd.setVisibility(View.VISIBLE);
                btVendaAvulsa.setVisibility(View.GONE);


                //setar clickView para botoes
                btQueroVenderAgora.setOnClickListener(this);
                btAdicionarQuantidade.setOnClickListener(this);
                btRemoverQuantidade.setOnClickListener(this);

                //Setar informacoes na tela
                tvNomeVendedor.setText("Vendedor: "+ sp.getString("nome",null));
                tvNomeVenda.setText("Nome: "+nomeProduto);
                tvPrecoVenda.setText("Preço: "+valorUnitarioMecadoria);

                if(bitmap!=null){
                    imvImagemProduto.setImageBitmap(Bitmap.createScaledBitmap
                            (bitmap,bitmap.getWidth()/6,bitmap.getHeight()/6,true));
                }else{
                    imvImagemProduto.setImageResource(R.drawable.ic_image_black_128dp);
                }

            }
        }




    }

    public void callRecordOfSell(View view) {

        String nome[] = tvNomeVenda.getText().toString().split(" ");
        String preco[] = tvPrecoVenda.getText().toString().split(" ");
        String qtd = etQtd.getText().toString();
        VenderProdutoAgora vender = new VenderProdutoAgora();
        String pathForJSon = path.replace('/','-');
        String idUsuario = sp.getString("id",null);
        vender.execute(nome[1],preco[1],qtd,pathForJSon,idUsuario);
    }

    public void callReturnToMenu(View view) {
        Intent intent = new Intent(InformacoesSobreVendaActivity.this,MenuActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_delete,menu);

        return createMenuDelete;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.item_deletar_item:
                DeleteThisItem dti = new DeleteThisItem();
                dti.execute(idProduto);
                break;

        }


        return super.onOptionsItemSelected(item);
    }

    public void callGerarQrCode(View view) {

        String nomeProduto[] = tvNomeVenda.getText().toString().split(" ");
        String preco[] = tvPrecoVenda.getText().toString().split(" ");
        String descricao = "";
        String qtd = etQtd.getText().toString();
        String usuarioId = sp.getString("id",null);
        GerarQrCode gerarQrCode = new GerarQrCode();
        gerarQrCode.execute(nomeProduto[1],preco[1],qtd,usuarioId,idProduto);


    }

    private class DeleteThisItem extends AsyncTask<String,Void,String>{

        ProgressDialog progress;
        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(InformacoesSobreVendaActivity.this,"Aguarde","Deletando Item");
        }

        @Override
        protected String doInBackground(String... strings) {


            try {
                URL url = new URL("http://paguefacilbinatron.azurewebsites.net/api/ProdutoParaVenderWeb/"+strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(4000);
                connection.setReadTimeout(4000);
                connection.setRequestMethod("DELETE");
                connection.setRequestProperty("Content-Type","application/json");

                if(connection.getResponseCode()==200){
                    return "200";
                }else if(connection.getResponseCode()==500){
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
                    Intent intent = new Intent(InformacoesSobreVendaActivity.this,MenuActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(), R.string.ocorreu_um_erro_ao_deletar_item,Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(getApplicationContext(),R.string.ocorreu_um_erro_inesperado,Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class GerarQrCode extends AsyncTask<String, Void, String>{

        ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            progress= ProgressDialog.show(InformacoesSobreVendaActivity.this,"QR Code",getString(R.string.estamos_gerando_o_qr_code));
        }

        @Override
        protected String doInBackground(String... strings) {

            URL url = null;

            try {
                url= new URL("http://paguefacilbinatron.azurewebsites.net/api/QrCodeWeb");

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type","application/json; charset=UTF8");

                JSONObject jsonProduto = new JSONObject();
                jsonProduto.put("Nome",strings[0]);
                jsonProduto.put("Preco",strings[1]);
                jsonProduto.put("Descricao","");
                jsonProduto.put("Quantidade",strings[2]);
                jsonProduto.put("UsuarioId",strings[3]);
                jsonProduto.put("Id",strings[4]);




                JSONObject jsonQrCode = new JSONObject();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                Date today = Calendar.getInstance().getTime();
                String dataDeTransacao = format.format(today);

                jsonQrCode.put("DataHora",dataDeTransacao);
                jsonQrCode.put("ProdutoParaVender",jsonProduto);


                OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream());
                osw.write(jsonQrCode.toString());
                osw.close();

                if(connection.getResponseCode()==200){

                    String linha = "";
                    StringBuilder builder = new StringBuilder();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    while ((linha=reader.readLine())!=null){
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
            scInformacoesSobreVenda.pageScroll(View.FOCUS_UP);
            imvDeuCerto.setVisibility(View.GONE);
            tvDeuCerto.setVisibility(View.GONE);
            tvParaVenderBasta.setVisibility(View.GONE);
            tvInformacoesSobreVenda.setVisibility(View.VISIBLE);

            progress.dismiss();
            JSONObject jsonResponse = null;
            boolean deuErro=false;
            String mensagemErro="";
            try {
                jsonResponse = new JSONObject(s);
                String imagemCodificada = jsonResponse.getString("ImagemCodificada");
                byte[] imageDecode = Base64.decode(imagemCodificada,Base64.DEFAULT);
                Bitmap decodeImage = BitmapFactory.decodeByteArray(imageDecode,0,imageDecode.length);
                imvImagemProduto.setImageBitmap(Bitmap.createScaledBitmap(decodeImage,360,360,false));
                //Bitmap bitmap = BitmapFactory.

                /*String nomeProduto = jsonObjectProduto.getString("Nome");
                String precoProduto = jsonObjectProduto.getString("Preco");*/
                //usuarioId = jsonObjectProduto.getString("UsuarioId");
                /*
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("id",idUsuario);
                editor.putString("nome",nomeUsuario);
                editor.putString("senha",senhaUsuario);
                editor.putString("cpf",cpfUsuario);
                editor.commit();*/

                deuErro = jsonResponse.getBoolean("DeuErro");
                mensagemErro = jsonResponse.getString("MensagemRetorno");

                if(deuErro==false){
                    return;

                }else{
                    Toast.makeText(getApplicationContext(),mensagemErro,Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.bt_adicionar_qtd:
                etQtd.setText(somarQtd());
                tvPrecoVenda.setText("Preco: "+multiplicarPorQtd(etQtd.getText().toString()));
                break;
            case R.id.bt_remover_qtd:
                etQtd.setText(subtrairQtd());
                tvPrecoVenda.setText("Preco: "+multiplicarPorQtd(etQtd.getText().toString()));
                break;
            case R.id.bt_quero_vender_agora:
                String nomeProduto[] = tvNomeVenda.getText().toString().split(" ");
                String preco[] = tvPrecoVenda.getText().toString().split(" ");
                String descricao = "";
                String qtd = etQtd.getText().toString();
                String usuarioId = sp.getString("id",null);
                GerarQrCode gerarQrCode = new GerarQrCode();
                gerarQrCode.execute(nomeProduto[1],preco[1],qtd,usuarioId,idProduto);

                break;
        }

    }

    private String multiplicarPorQtd(String s) {

        double precoVenda = Double.parseDouble(valorUnitarioMecadoria);

        int qtd = Integer.parseInt(s);

        double valorFinal = precoVenda*qtd;

        return String.valueOf(valorFinal);
    }

    private String subtrairQtd() {
        int qtd = Integer.parseInt(etQtd.getText().toString());
        if(qtd>1){
            qtd--;
        }



        return String.valueOf(qtd);
    }


    private String somarQtd(){
        int qtd = Integer.parseInt(etQtd.getText().toString());
        qtd++;
        return String.valueOf(qtd);
    }


    private class VenderProdutoAgora extends AsyncTask<String, Void,String>{

        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(InformacoesSobreVendaActivity.this,"Gravar informacoes",getString(R.string.aguarde_guardando_info_sobre_venda));
        }

        @Override
        protected String doInBackground(String... strings) {

            URL url = null;

            try{
                url = new URL("http://paguefacilbinatron.azurewebsites.net/api/ProdutoParaVenderWeb/");
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type","application/json");
                Produto ppv = new Produto();
                ppv.setNome(strings[0]);
                ppv.setPreco(strings[1]);
                ppv.setDescricao("descricao");
                ppv.setQuantidade(strings[2]);
                ppv.setImagemUrl(strings[3]);
                ppv.setUsuarioId(strings[4]);

                JSONObject produtoObject = new JSONObject();
                produtoObject.put("Nome",ppv.getNome());
                produtoObject.put("Preco",ppv.getPreco());
                produtoObject.put("Descricao",ppv.getDescricao());
                produtoObject.put("Quantidade",ppv.getQuantidade());
                produtoObject.put("ImagemUrl",ppv.getImagemUrl());
                produtoObject.put("UsuarioId",ppv.getUsuarioId());

                JSONObject produtoParaVender = new JSONObject();
                produtoParaVender.put("ProdutoParaVender",produtoObject);


                OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream());
                osw.write(produtoParaVender.toString());
                osw.close();

                if(connection.getResponseCode()==200){

                    String linha = "";
                    StringBuilder builder = new StringBuilder();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    while ((linha=reader.readLine())!=null){
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
            JSONObject jsonResponse = null;
            boolean deuErro=false;
            String mensagemErro="";
            try {
                jsonResponse = new JSONObject(s);
                JSONObject jsonObjectProduto = jsonResponse.getJSONObject("ProdutoParaVender");
                idProduto = jsonObjectProduto.getString("Id");
                deuErro = jsonResponse.getBoolean("DeuErro");
                mensagemErro = jsonResponse.getString("MensagemRetorno");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(deuErro==false){
                scInformacoesSobreVenda.pageScroll(View.FOCUS_UP);

                Animation animImv = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in_animation);
                imvDeuCerto.startAnimation(animImv);
                imvDeuCerto.setVisibility(View.VISIBLE);

                Animation animTv = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in_text_animation);

                tvDeuCerto.startAnimation(animTv);
                tvDeuCerto.setVisibility(View.VISIBLE);

                tvInformacoesSobreVenda.setVisibility(View.GONE);

                tvParaVenderBasta.setVisibility(View.VISIBLE);

                llQtd.setVisibility(View.VISIBLE);

                btQueroRegistrarEsseProduto.setVisibility(View.GONE);

                btVendaAvulsa.setVisibility(View.GONE);
                btQueroVenderAgora.setVisibility(View.VISIBLE);

                btAdicionarQuantidade.setOnClickListener(InformacoesSobreVendaActivity.this);
                btRemoverQuantidade.setOnClickListener(InformacoesSobreVendaActivity.this);
                btQueroVenderAgora.setOnClickListener(InformacoesSobreVendaActivity.this);

                btDesistiDaIdeia.setVisibility(View.GONE);

                btNaoQueroVenderAgora.setVisibility(View.VISIBLE);
            }else {
                Toast.makeText(getApplicationContext(),mensagemErro,Toast.LENGTH_SHORT).show();
            }
        }
    }
}
