package br.com.fiap.am.amproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.media.VolumeProviderCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.w3c.dom.Text;

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

        /*Validando de qual activity e originado. Se for BuscarImagensActivity, enconde-se os outros
            botoes e deixa-se o btNaoqueroVenderAgora inativo. Isso significa que o usuario deve ter
            seguido o fluxo de vendas.*/
        if(extras!=null){
            if(extras.getString("classe").equalsIgnoreCase("buscarimagensactivity")){


                //pegar os extras
                String nomeProduto = extras.getString("nomeProduto");
                valorUnitarioMecadoria = extras.getString("precoProduto");
                Bitmap bitmap = BitmapFactory.decodeFile(extras.getString("path"));

                //Setar visibilidade de elementos na tela
                btNaoQueroVenderAgora.setVisibility(View.GONE);
                btQueroVenderAgora.setVisibility(View.GONE);
                tvParaVenderBasta.setVisibility(View.GONE);
                btQueroRegistrarEsseProduto.setVisibility(View.VISIBLE);
                btDesistiDaIdeia.setVisibility(View.VISIBLE);
                tvInformacoesSobreVenda.setVisibility(View.VISIBLE);

                //Setar informacoes na tela
                tvNomeVendedor.setText("Vendedor: ");
                tvNomeVenda.setText("Nome: "+nomeProduto);
                tvPrecoVenda.setText("Preco: "+valorUnitarioMecadoria);
                imvImagemProduto.setImageBitmap(Bitmap.createScaledBitmap
                        (bitmap,bitmap.getWidth()/6,bitmap.getHeight()/6,true));

            }else{
                btNaoQueroVenderAgora.setVisibility(View.VISIBLE);
                btQueroRegistrarEsseProduto.setVisibility(View.GONE);
                btDesistiDaIdeia.setVisibility(View.GONE);

            }
        }


    }

    public void callRecordOfSell(View view) {

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

        btAdicionarQuantidade.setOnClickListener(this);
        btRemoverQuantidade.setOnClickListener(this);


        btDesistiDaIdeia.setVisibility(View.GONE);

        btNaoQueroVenderAgora.setVisibility(View.VISIBLE);






    }

    public void callReturnToMenu(View view) {
        Intent intent = new Intent(InformacoesSobreVendaActivity.this,MenuActivity.class);
        startActivity(intent);
        finish();
    }

    public void callGerarQrCode(View view) {


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
                VenderProdutoAgora vender = new VenderProdutoAgora();
                vender.execute();
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
        if(qtd>0){
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

        ProgressDialog progress = ProgressDialog.show(getApplicationContext(),"Gravar informacoes","Aguarde enquanto guardamos as informacoes da sua venda");

        @Override
        protected String doInBackground(String... strings) {




            return null;
        }
    }
}
