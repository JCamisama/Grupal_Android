package com.example.grupal_android;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.grupal_android.workers.InsertarFotoWorker;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ShopActivity extends MainActivity {

    private TextView titleTV;
    private TextView latTV;
    private TextView lngTV;
    private ImageButton botonImagen;
    private static final int CODIGO_FOTO_ARCHIVO = 1;
    private Uri uriimagen = null;
    private Bitmap bitmapredimensionado = null;
    private String nameFranchise = null;
    private String lat = null;
    private String lng = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        super.initializeMenuBar();
        this.initializeElements();
        this.handleExtras();
        //Conseguir imagen para el ImageButton en caso de girar la pantalla por ejemplo
        if (savedInstanceState != null){
            bitmapredimensionado = savedInstanceState.getParcelable("bitmap");
            botonImagen.setImageBitmap(bitmapredimensionado);
        }


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (bitmapredimensionado != null) {
            //Para que no se pierda la imagen al girar la pantalla, por ejemplo
            outState.putParcelable("bitmap", bitmapredimensionado);
        }
    }

    //Para obtener el bitmap que nos viene del intent para sacar la foto
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CODIGO_FOTO_ARCHIVO && resultCode == RESULT_OK) {
            Bitmap bitmapFoto = null;
            try {
                //Obtener el bitmap de la uri
                bitmapFoto = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uriimagen);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //Recortar
            int anchoDestino = botonImagen.getWidth();
            int altoDestino = botonImagen.getHeight();
            int anchoImagen = bitmapFoto.getWidth();
            int altoImagen = bitmapFoto.getHeight();
            float ratioImagen = (float) anchoImagen / (float) altoImagen;
            float ratioDestino = (float) anchoDestino / (float) altoDestino;
            int anchoFinal = anchoDestino;
            int altoFinal = altoDestino;
            if (ratioDestino > ratioImagen) {
                anchoFinal = (int) ((float)altoDestino * ratioImagen);
            } else {
                altoFinal = (int) ((float)anchoDestino / ratioImagen);
            }
            bitmapredimensionado = Bitmap.createScaledBitmap(bitmapFoto,anchoFinal,altoFinal,true);
            //Poner la imagen en el ButtonImage
            botonImagen.setImageBitmap(bitmapredimensionado);
            //Llamada a MiBD para que inserte la foto en la base de datos
            Data datos = new Data.Builder().putString("uriString",uriimagen.toString())
                    .putInt("ancho",anchoDestino).putInt("alto",altoDestino)
                    .putString("nameFranchise",this.nameFranchise).putString("lat",this.lat)
                    .putString("lng",this.lng).build();
            OneTimeWorkRequest otwr = new OneTimeWorkRequest.Builder(InsertarFotoWorker.class).setInputData(datos).build();
            WorkManager.getInstance(this).enqueue(otwr);

        }
    }

    /**
     * Toma las instancias de los inputs de texto disponibles en la ventana.
     */
    private void initializeElements() {
        this.titleTV = (TextView) findViewById(R.id.shopTitleLabel);
        this.latTV = (TextView) findViewById(R.id.latShopLabel);
        this.lngTV = (TextView) findViewById(R.id.lngShopLabel);
        this.botonImagen = (ImageButton) findViewById(R.id.imageButton);
    }

    /**
     * Se obtienen los extras y se actualizan los elementos del layout
     */
    private void handleExtras(){
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            nameFranchise = extras.getString("nameFranchise");
            lat = extras.getString("lat");
            lng = extras.getString("lng");
            titleTV.setText(nameFranchise);
            latTV.setText(lat);
            lngTV.setText(lng);
        }
    }

    /**
     * Click en el botón para poder sacar la foto
     */
    public void onClickImagen(View v){

        //MIRAR QUE NO HAYA IMAGEN

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String nombrefich = "IMG_" + timeStamp + "_";
        File directorio=this.getFilesDir();
        File fichImg = null;
        uriimagen = null;

        try {
            fichImg = File.createTempFile(nombrefich, ".jpg",directorio);
        } catch (IOException e) {
            e.printStackTrace();
        }
        uriimagen = FileProvider.getUriForFile(this, "com.example.tema17ejercicio1.provider", fichImg);

        Intent elIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        elIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriimagen);
        startActivityForResult(elIntent, CODIGO_FOTO_ARCHIVO);

    }



}