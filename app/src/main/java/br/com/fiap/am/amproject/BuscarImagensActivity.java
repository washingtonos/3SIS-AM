package br.com.fiap.am.amproject;

import android.Manifest;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.FileProvider;
import android.support.v4.os.EnvironmentCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BuscarImagensActivity extends AppCompatActivity {

    static final int TAKE_PICTURE_CODE = 1;
    static final int OPEN_LOCAL_FOLDER=2;
    private static final int MY_PERMISSION_WRITE_EXTERNAL_STORAGE = 1;
    private static final int MY_PERMISSION_CAMERA = 2;
    private static final int MY_PERMISSION_READ_EXTERNAL_STORAGE = 3;
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

        if(ContextCompat.checkSelfPermission(BuscarImagensActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED&&
                ContextCompat.checkSelfPermission(BuscarImagensActivity.this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(BuscarImagensActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,   Manifest.permission.CAMERA},MY_PERMISSION_WRITE_EXTERNAL_STORAGE);
        }else{

            callCamera();

        }




    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){

            case MY_PERMISSION_WRITE_EXTERNAL_STORAGE:
                if(grantResults.length>0
                        &&grantResults[0]==PackageManager.PERMISSION_GRANTED){

                    callCamera();
                }
                break;
            case MY_PERMISSION_READ_EXTERNAL_STORAGE:
                if(grantResults.length>0
                        &&grantResults[0]==PackageManager.PERMISSION_GRANTED){

                    Intent intentOpenFolder = new Intent(Intent.ACTION_GET_CONTENT);
                    intentOpenFolder.setType("*/*");
                    intentOpenFolder.addCategory(Intent.CATEGORY_OPENABLE);
                    startActivityForResult(Intent.createChooser(intentOpenFolder,getString(R.string.selecionar_gerenciador)),OPEN_LOCAL_FOLDER);
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


            //Metodo para capturar imagem
        }else if(requestCode==OPEN_LOCAL_FOLDER&&resultCode==RESULT_OK){

            Uri uri = data.getData();
            InputStream is = null;
            try {
                imagePhotoPath  = getPath(uri);
                is = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(is);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


            /*imagePhotoPath = uri.getPath();

            getImageUri(this,);*/






            Intent intent = new Intent(BuscarImagensActivity.this,InformacoesSobreVendaActivity.class);
            intent.putExtra("classe",this.getLocalClassName());
            intent.putExtra("path",imagePhotoPath);
            intent.putExtra("nomeProduto",nomeProduto);
            intent.putExtra("precoProduto",precoProduto);
            startActivity(intent);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String getPathFromUri(Context context,Uri tempUri) {

        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(context, tempUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    private Uri getImageUri(Context applicationContext, Bitmap bitmap) {

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        String path = MediaStore.Images.Media.insertImage(applicationContext.getContentResolver(),bitmap,"Imagem",null);
        return Uri.parse(path);
    }

    public void callOpenFolder(View view) {


        if(ContextCompat.checkSelfPermission(BuscarImagensActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(BuscarImagensActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,   Manifest.permission.READ_EXTERNAL_STORAGE},MY_PERMISSION_READ_EXTERNAL_STORAGE);
        }else{


            Intent intentOpenFolder = new Intent(Intent.ACTION_GET_CONTENT);
            intentOpenFolder.setType("*/*");
            intentOpenFolder.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(Intent.createChooser(intentOpenFolder,getString(R.string.selecione_gerenciador_de_arquivo)),OPEN_LOCAL_FOLDER);

        }





    }

    public void callInformacoesSobreVendaActivity(View view) {

        imagePhotoPath = "";
        Intent intent = new Intent(BuscarImagensActivity.this,InformacoesSobreVendaActivity.class);
        intent.putExtra("classe",this.getLocalClassName());
        intent.putExtra("path",imagePhotoPath);
        intent.putExtra("nomeProduto",nomeProduto);
        intent.putExtra("precoProduto",precoProduto);
        startActivity(intent);
    }


    public String getPath(Uri contentUri) {// Will return "image:x*"

        String wholeID = DocumentsContract.getDocumentId(contentUri);

        // Split at colon, use second item in the array
        String id = wholeID.split(":")[1];

        String[] column = { MediaStore.Images.Media.DATA };

        // Where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, column, sel,
                new String[] { id }, null);

        String filePath = "";

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }

        cursor.close();
        return filePath;
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
