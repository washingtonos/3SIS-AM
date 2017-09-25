package br.com.fiap.am.amproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

public class BuscarImagensActivity extends AppCompatActivity {

    static final int TAKE_PICTURE_CODE = 1;
    static final int OPEN_LOCAL_FOLDER=2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_imagens);

        Toolbar toolbar = (Toolbar)findViewById(R.id.tb_buscar_imagens);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    public void callCamera(View view) {

        Intent intentTakePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        startActivityForResult(intentTakePicture,TAKE_PICTURE_CODE);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode==TAKE_PICTURE_CODE&&resultCode==RESULT_OK){
            Bundle extra = data.getExtras();
            if(extra!=null){
            //Bitmap bitmap = (Bitmap)extra.get("data");

            //ImageView imageView = (ImageView) findViewById(R.id.image_picture);
            //imageView.setImageBitmap(bitmap);
            }

            //Metodo para capturar imagem
        }else if(requestCode==OPEN_LOCAL_FOLDER&&resultCode==RESULT_OK){
            //Metodo para capturar uri de imagem

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void callOpenFolder(View view) {
        Intent intentOpenFolder = new Intent(Intent.ACTION_GET_CONTENT);
        intentOpenFolder.setType("*/*");
        intentOpenFolder.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intentOpenFolder,"Selecione um Gerenciador de arquivos"),OPEN_LOCAL_FOLDER);
    }

    public void callInformacoesSobreVendaActivity(View view) {
        Intent intent = new Intent(BuscarImagensActivity.this,InformacoesSobreVendaActivity.class);
        intent.putExtra("classe",this.getLocalClassName());
        startActivity(intent);
    }
}
