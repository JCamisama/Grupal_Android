package com.example.grupal_android;

import androidx.core.content.FileProvider;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.grupal_android.managers.SessionManager;
import com.example.grupal_android.workers.ActualizarVotosWorker;
import com.example.grupal_android.workers.GetPuntuationShopWorker;
import com.example.grupal_android.workers.GetVotedWorker;
import com.example.grupal_android.workers.InsertarFotoWorker;
import com.example.grupal_android.workers.ActualizarPuntuacionWorker;
import com.example.grupal_android.workers.InsertarVotosWorker;
import com.example.grupal_android.workers.ShopPhotoWorker;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.stream.Collectors;

public class ShopActivity extends MainActivity {

    private TextView titleTV;
    private TextView latTV;
    private TextView lngTV;
    private ImageButton botonImagen;
    private static final int CODIGO_FOTO_ARCHIVO = 1;
    private Uri uriimagen = null;
    private Bitmap bitmapredimensionado = null;
    private String username = null;
    private String nameFranchise = null;
    private String lat = null;
    private String lng = null;
    private int voted = 0;
    private String startVote = null;
    private int puntos = 0;
    private int newPoints = 0;
    ImageButton likeBtn;
    ImageButton unlikeBtn;
    TextView votos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        super.initializeMenuBar();
        this.initializeElements();
        this.handleExtras();

        SessionManager sessionManager = SessionManager.getInstance(this);
        username = sessionManager.getCurrentUsername();
        votos = findViewById(R.id.votesLabel);
        likeBtn = findViewById(R.id.likeButton);
        likeBtn.getBackground().setAlpha(89);
        unlikeBtn = findViewById(R.id.unlikeButton);
        unlikeBtn.getBackground().setAlpha(89);

        //Conseguir imagen para el ImageButton en caso de girar la pantalla por ejemplo
        if (savedInstanceState != null){
            bitmapredimensionado = savedInstanceState.getParcelable("bitmap");
            botonImagen.setImageBitmap(bitmapredimensionado);
        }
        this.loadImage();
        this.getPuntuation();
        this.getVote();
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

    public void onClickLike(View v){
        voted = 1;
        newPoints = puntos + voted;
        votos.setText(Integer.toString(newPoints));
        this.insertVote();
        this.updateVote();
        this.updatePuntuation();


        this.like();
    }


    public void onClickDislike(View v){
        voted = -1;
        newPoints = puntos + voted;
        votos.setText(Integer.toString(newPoints));
        this.insertVote();
        this.updateVote();
        this.updatePuntuation();


        this.unlike();
    }

    private void like(){
        likeBtn.setEnabled(false);
        likeBtn.getBackground().setAlpha(255);
        unlikeBtn.setEnabled(true);
        unlikeBtn.getBackground().setAlpha(89);
    }

    private void unlike(){
        unlikeBtn.setEnabled(false);
        unlikeBtn.getBackground().setAlpha(255);
        likeBtn.setEnabled(true);
        likeBtn.getBackground().setAlpha(89);
    }

    private void loadImage(){
        Data datos = new Data.Builder()
                .putString("name", nameFranchise)
                .putString("lat", lat)
                .putString("lng", lng)
                .build();
        OneTimeWorkRequest otwr = new OneTimeWorkRequest.Builder(ShopPhotoWorker.class).setInputData(datos).build();
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(otwr.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        if (workInfo != null && workInfo.getState().isFinished()) {
                            try {
                                //Leemos el fichero donde esta guardado el base64 de la foto que ha sido escrita por la clase DownloadWorker con anterioridad.
                                BufferedReader ficherointerno = new BufferedReader(new InputStreamReader(
                                        openFileInput("foto.txt")));
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                    //Decodificar la foto de string para poder proyectar la imagen por pantalla
                                    String  linea =  ficherointerno.lines().collect(Collectors.joining());
                                    ficherointerno.close();
                                    if(!linea.equalsIgnoreCase("")){

                                        byte[] bytes = Base64.decode(linea, Base64.DEFAULT);
                                        Bitmap elBitMap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                        ImageView img  = findViewById(R.id.imageButton);
                                        img.setImageBitmap(elBitMap);
                                        img.setEnabled(false);
                                    }

                                }


                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                });
        WorkManager.getInstance(this).enqueue(otwr);
    }

    private void getPuntuation(){
        Data datos = new Data.Builder()
                .putString("nameFranchise", nameFranchise)
                .putString("lat", lat)
                .putString("lng", lng)
                .build();
        OneTimeWorkRequest otwr = new OneTimeWorkRequest.Builder(GetPuntuationShopWorker.class).setInputData(datos).build();
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(otwr.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        if (workInfo != null && workInfo.getState().isFinished()) {
                            TextView votos = findViewById(R.id.votesLabel);
                            puntos = Integer.parseInt(workInfo.getOutputData().getString("puntuation"));
                            votos.setText(Integer.toString(puntos));

                        }
                    }
                });
        WorkManager.getInstance(this).enqueue(otwr);
    }

    private void updatePuntuation() {
        Data datos = new Data.Builder()
                .putString("nameFranchise", nameFranchise)
                .putString("lat", lat)
                .putString("lng", lng)
                .putString("points", String.valueOf(newPoints))
                .build();
        OneTimeWorkRequest otwr = new OneTimeWorkRequest.Builder(ActualizarPuntuacionWorker.class).setInputData(datos).build();
        WorkManager.getInstance(this).enqueue(otwr);
    }

    private void insertVote(){
        Data datos = new Data.Builder()
                .putString("username", username)
                .putString("nameFranchise", nameFranchise)
                .putString("lat", lat)
                .putString("lng", lng)
                .putInt("voted", voted)
                .build();
        OneTimeWorkRequest otwr = new OneTimeWorkRequest.Builder(InsertarVotosWorker.class).setInputData(datos).build();
        WorkManager.getInstance(this).enqueue(otwr);
    }

    private void updateVote(){
        Data datos = new Data.Builder()
                .putString("username", username)
                .putString("nameFranchise", nameFranchise)
                .putString("lat", lat)
                .putString("lng", lng)
                .putInt("voted", voted)
                .build();
        OneTimeWorkRequest otwr = new OneTimeWorkRequest.Builder(ActualizarVotosWorker.class).setInputData(datos).build();
        WorkManager.getInstance(this).enqueue(otwr);
    }

    private void getVote() {
        Data datos = new Data.Builder()
                .putString("username", username)
                .putString("nameFranchise", nameFranchise)
                .putString("lat", lat)
                .putString("lng", lng)
                .build();
        OneTimeWorkRequest otwr = new OneTimeWorkRequest.Builder(GetVotedWorker.class).setInputData(datos).build();
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(otwr.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        if (workInfo != null && workInfo.getState().isFinished()) {
                                startVote = workInfo.getOutputData().getString("voted");
                                Log.d("ad", startVote);
                                if (startVote != null){
                                  /*  if(Integer.parseInt(startVote) > 0){
                                        like();
                                    }
                                    else if (Integer.parseInt(startVote) < 0){
                                        unlike();
                                    }*/
                                }

                        }
                    }
                });
        WorkManager.getInstance(this).enqueue(otwr);
    }


}