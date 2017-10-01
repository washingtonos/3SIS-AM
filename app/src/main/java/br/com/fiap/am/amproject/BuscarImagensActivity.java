package br.com.fiap.am.amproject;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.os.EnvironmentCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BuscarImagensActivity extends AppCompatActivity {

    static final int TAKE_PICTURE_CODE = 1;
    static final int OPEN_LOCAL_FOLDER=2;
    private static final int MY_PERMISSION_WRITE_EXTERNAL_STORAGE = 1;
    private String imagePhotoPath;
    private File image;
    private String nomeProduto;
    private String precoProduto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_imagens);

        Toolbar toolbar = (Toolbar)findViewById(R.id.tb_buscar_imagens);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //capturar extras de CadastrarVendaNomeProduto E DefinirPrecoVenda

        Bundle extras = getIntent().getExtras();

        if(extras!=null){

            nomeProduto = extras.getString("nomeProduto");
            precoProduto = extras.getString("precoProduto");


        }
    }


    public void callCamera(View view) {

        if(ContextCompat.checkSelfPermission(BuscarImagensActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(BuscarImagensActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},MY_PERMISSION_WRITE_EXTERNAL_STORAGE);
        }else{

            /*Intent intentTakePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            startActivityForResult(intentTakePicture,TAKE_PICTURE_CODE);*/

            callCamera();

        }




    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){

            case MY_PERMISSION_WRITE_EXTERNAL_STORAGE:
                if(grantResults.length>0
                        &&grantResults[0]==PackageManager.PERMISSION_GRANTED){

                    /*Intent intentTakePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    File file = createImageFromFile();

                    Uri photoUri = FileProvider.getUriForFile(this,"br.com.fiap.am.amproject.fileprovider",file);

                    intentTakePicture.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);

                    startActivityForResult(intentTakePicture,TAKE_PICTURE_CODE);*/

                    callCamera();
                }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode==TAKE_PICTURE_CODE&&resultCode==RESULT_OK){


            Intent intent = new Intent(BuscarImagensActivity.this,InformacoesSobreVendaActivity.class);
            intent.putExtra("classe",this.getLocalClassName());
            intent.putExtra("path",imagePhotoPath);
            intent.putExtra("nomeProduto",nomeProduto);
            intent.putExtra("precoProduto",precoProduto);
            startActivity(intent);


            /*Bundle extra = data.getExtras();
            if(extra!=null){
                Bitmap bitmap = (Bitmap)extra.get("data");

                /*Uri tempUri = getImageUri(getApplicationContext(),bitmap);

                File file = new File(getPathFromUri(tempUri));
                File file = createImageFromFile();

                Uri photoUri = FileProvider.getUriForFile(this,"br.com.fiap.am.amproject.fileprovider",file);

                Intent intent = new Intent(BuscarImagensActivity.this,InformacoesSobreVendaActivity.class);
                intent.putExtra("classe",this.getLocalClassName());
                intent.putExtra("path",file.getPath());
                startActivity(intent);

            }*/

            //Metodo para capturar imagem
        }else if(requestCode==OPEN_LOCAL_FOLDER&&resultCode==RESULT_OK){
            //Metodo para capturar uri de imagem

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String getPathFromUri(Uri tempUri) {

        Cursor cursor = getContentResolver().query(tempUri,null,null,null,null);
        cursor.moveToFirst();
        int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(index);
    }

    private Uri getImageUri(Context applicationContext, Bitmap bitmap) {

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        String path = MediaStore.Images.Media.insertImage(applicationContext.getContentResolver(),bitmap,"Imagem",null);
        return Uri.parse(path);
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



    private void callCamera(){

        Intent intentTakePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File file = createImageFromFile();

        Uri photoUri = FileProvider.getUriForFile(this,"br.com.fiap.am.amproject.fileprovider",file);

        intentTakePicture.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);

        startActivityForResult(intentTakePicture,TAKE_PICTURE_CODE);
    }

    private File createImageFromFile(){
        try {
            String timestamp = new SimpleDateFormat("yyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timestamp + "_";
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

            image = File.createTempFile(imageFileName,".jpg",storageDir);

            imagePhotoPath = image.getAbsolutePath();

            return image;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
