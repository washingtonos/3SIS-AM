package br.com.fiap.am.amproject;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MenuActivity extends AppCompatActivity {



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Button button = (Button)findViewById(R.id.button_ler_qr_code);
            switch (item.getItemId()) {
                case R.id.navigation_my_account:
                   // mTextMessage.setText(R.string.title_account);
                    button.setVisibility(View.INVISIBLE);
                    return true;
                case R.id.navigation_buy:
                    //mTextMessage.setText(R.string.title_buy);
                    button.setVisibility(View.VISIBLE);
                    return true;
                case R.id.navigation_sell:
                    //mTextMessage.setText(R.string.title_sell);
                    button.setVisibility(View.INVISIBLE);
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



        //Coloca botao invisivel
        Button button = (Button)findViewById(R.id.button_ler_qr_code);
        button.setVisibility(View.INVISIBLE);

        //set Toolbar
        Toolbar mToolBar = (Toolbar)findViewById(R.id.toolbar_menuactivity);
        setSupportActionBar(mToolBar);
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
            Intent intent = new Intent(MenuActivity.this,ConfirmarCompra.class);
            startActivity(intent);
            finish();

        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
