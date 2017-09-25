package br.com.fiap.am.amproject;

import android.content.Intent;
import android.support.v4.media.VolumeProviderCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class InformacoesSobreVendaActivity extends AppCompatActivity {

    ImageView imvDeuCerto;
    TextView tvDeuCerto;
    Button btQueroRegistrarEsseProduto;
    Button btDesistiDaIdeia;
    Button btNaoQueroVenderAgora;
    TextView tvInformacoesSobreVenda;
    TextView tvParaVenderBasta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacoes_sobre_venda);

        Toolbar toolbar = (Toolbar)findViewById(R.id.tb_informacoes_sobre_venda);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvDeuCerto = (TextView)findViewById(R.id.tv_deu_certo);
        tvDeuCerto.setVisibility(View.GONE);

        tvInformacoesSobreVenda = (TextView)findViewById(R.id.tv_informacoes_sobre_venda);
        tvInformacoesSobreVenda.setVisibility(View.GONE);

        tvParaVenderBasta = (TextView)findViewById(R.id.tv_para_vender_basta);

        imvDeuCerto = (ImageView)findViewById(R.id.imv_deu_certo);
        imvDeuCerto.setVisibility(View.GONE);

        btQueroRegistrarEsseProduto = (Button)findViewById(R.id.bt_quero_registrar_esse_produto);
        btDesistiDaIdeia = (Button)findViewById(R.id.bt_nao_desisti_da_ideia);
        btNaoQueroVenderAgora = (Button)findViewById(R.id.bt_nao_quero_vender_agora);


        Bundle extras = getIntent().getExtras();

        /*Validando de qual activity e originado. Se for BuscarImagensActivity, enconde-se os outros
            botoes e deixa-se o btNaoqueroVenderAgora inativo. Isso significa que o usuario deve ter
            seguido o fluxo de vendas.*/
        if(extras!=null){
            if(extras.getString("classe").equalsIgnoreCase("buscarimagensactivity")){

                btNaoQueroVenderAgora.setVisibility(View.GONE);
                btQueroRegistrarEsseProduto.setVisibility(View.VISIBLE);
                btDesistiDaIdeia.setVisibility(View.VISIBLE);
                tvInformacoesSobreVenda.setVisibility(View.VISIBLE);
                tvParaVenderBasta.setVisibility(View.GONE);

            }else{
                btNaoQueroVenderAgora.setVisibility(View.VISIBLE);
                btQueroRegistrarEsseProduto.setVisibility(View.GONE);
                btDesistiDaIdeia.setVisibility(View.GONE);

            }
        }


    }

    public void callRecordOfSell(View view) {

        Animation animImv = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in_animation);
        imvDeuCerto.startAnimation(animImv);
        imvDeuCerto.setVisibility(View.VISIBLE);

        Animation animTv = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in_text_animation);

        tvDeuCerto.startAnimation(animTv);
        tvDeuCerto.setVisibility(View.VISIBLE);

        tvInformacoesSobreVenda.setVisibility(View.GONE);

        tvParaVenderBasta.setVisibility(View.VISIBLE);

        btQueroRegistrarEsseProduto.setVisibility(View.GONE);


        btDesistiDaIdeia.setVisibility(View.GONE);

        btNaoQueroVenderAgora.setVisibility(View.VISIBLE);






    }

    public void callReturnToMenu(View view) {
        Intent intent = new Intent(InformacoesSobreVendaActivity.this,MenuActivity.class);
        startActivity(intent);
        finish();
    }
}
