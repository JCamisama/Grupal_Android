package com.example.grupal_android;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class ActividadMapa extends FragmentActivity implements OnMapReadyCallback {
    private String tienda;
    private double latitudG;
    private double longitudG;
    private boolean ubication = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Al crear la actividad se vincula con  el layout del mapa. Así, enseñando el mapa mundi
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            tienda = (String) extras.get("tienda");
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {
            hacerToast(R.string.noPer);
        } else {
            FusedLocationProviderClient proveedordelocalizacion = LocationServices.getFusedLocationProviderClient(this);
            proveedordelocalizacion.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                ubication = true;
                                //Consigue tu latitud y longitud actual
                                latitudG = location.getLatitude();
                                longitudG = location.getLongitude();
                            } else {
                                hacerToast(R.string.falloUbi);
                            }
                        }
                    })
                    .addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            hacerToast(R.string.falloUbi);
                        }
                    });

        }
        setContentView(R.layout.activity_mapa);
        SupportMapFragment elfragmento =

                (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.fragmentoMapa);
        elfragmento.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        //Método que una vez que este cargado el mapa cargara todas las ubicaciones de todos los usuarios
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        Data datos = new Data.Builder().putString("tienda", tienda).build();
        OneTimeWorkRequest otwr = new OneTimeWorkRequest.Builder(MapaWorker.class).setInputData(datos).build();
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(otwr.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        if(workInfo != null && workInfo.getState().isFinished()){
                            Double latitud;
                            Double longitud;
                            String nombres = workInfo.getOutputData().getString("datosN");
                            nombres = nombres.substring(1, nombres.length()-1);
                            String[] arrN = nombres.split(", ");
                            String latitudes = workInfo.getOutputData().getString("datosLat");
                            latitudes = latitudes.substring(1, latitudes.length()-1);
                            String[] arrLat = latitudes.split(", ");
                            String longitudes = workInfo.getOutputData().getString("datosLon");
                            longitudes = longitudes.substring(1, longitudes.length()-1);
                            String[] arrLon = longitudes.split(", ");
                            for(int i =0; i<arrLat.length;i++){
                                latitud = Double.parseDouble(arrLat[i]);
                                longitud = Double.parseDouble(arrLon[i]);
                                if(ubication){
                                    if(latitud<latitudG+2 && latitud>latitudG-2 && longitud<latitudG+2 && longitud>latitudG-2){
                                        googleMap.addMarker(new MarkerOptions().position(new LatLng(latitud, longitud)).title(arrN[i]));
                                    }
                                } else {
                                    googleMap.addMarker(new MarkerOptions().position(new LatLng(latitud, longitud)).title(arrN[i]));
                                }
                            }

                        }
                    }
                });
        WorkManager.getInstance(this).enqueue(otwr);
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            public boolean onMarkerClick(Marker marker) {
                findShopWhithMarker(marker);
                return false;
            }
        });
    }
    private void findShopWhithMarker(Marker pMarker) {
        String tienda = pMarker.getTitle();
        LatLng latLng = pMarker.getPosition();
        Double lati = latLng.latitude;
        Double lon = latLng.longitude;
        Intent intent = new Intent(this, MainActivity.class);//Klasea aldatu behar da
        intent.putExtra("tienda", tienda);
        intent.putExtra("latitud", lati);
        intent.putExtra("longitud", lon);
        startActivity(intent);
        finish();
    }
    public void hacerToast(int s){
        //Metodo de apoyo para hacer notificaciones del tipo toast
        Toast toast= Toast.makeText(this,s, Toast.LENGTH_SHORT);
        toast.show();
    }
}