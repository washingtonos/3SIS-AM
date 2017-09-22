package br.com.fiap.am.amproject;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MenuActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Button button = (Button)findViewById(R.id.button_ler_qr_code);
            switch (item.getItemId()) {
                case R.id.navigation_buy:
                    mTextMessage.setText(R.string.title_buy);
                    button = (Button)findViewById(R.id.button_ler_qr_code);
                    button.setVisibility(View.VISIBLE);
                    return true;
                case R.id.navigation_sell:
                    mTextMessage.setText(R.string.title_sell);
                    button.setVisibility(View.INVISIBLE);
                    return true;
                case R.id.navigation_transactions:
                    mTextMessage.setText(R.string.title_transactions);
                    button.setVisibility(View.INVISIBLE);
                    return true;
                case R.id.navigation_account:
                    mTextMessage.setText(R.string.title_account);
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

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //set Toolbar
        Toolbar mToolBar = (Toolbar)findViewById(R.id.toolbar_menuactivity);
        setSupportActionBar(mToolBar);
    }

    public void callQrCodeReader(View view) {


    }
}
