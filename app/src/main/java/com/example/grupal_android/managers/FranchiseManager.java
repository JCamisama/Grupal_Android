package com.example.grupal_android.managers;

import static com.example.grupal_android.utils.GlobalVariablesUtil.FRANCHISE_NAME;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.example.grupal_android.models.Franchise;
import com.example.grupal_android.utils.StringManipulator;
import com.example.grupal_android.workers.GetFrachiseNames;
import com.example.grupal_android.workers.GetFranchiseByNameWorker;

import java.util.ArrayList;

public class FranchiseManager {
    private static FranchiseManager instance = null;
    private Context myContext;
    public static final String defaultLanguage = "en";
    private ArrayList<String> names;
    private ArrayList<Franchise> franchises = new ArrayList<>();

    /*** Singleton Pattern ***/
    private FranchiseManager(Context pContext) {
        this.myContext = pContext;
        this.names = new ArrayList<>();
        this.initializeFranchises();
    }

    public static synchronized FranchiseManager getInstance(Context pContext) {
        if (FranchiseManager.instance == null) {
            FranchiseManager.instance = new FranchiseManager(pContext);
        }

        return FranchiseManager.instance;
    }


    /**
     * Envía la orden asíncrona que recopila las franquicias que se van a presentar
     * en la aplicación.
     */
    private void initializeFranchises() {
        this.getFranchiseNamesFromServer();
    }


    /**
     * Obtiene los nombres de las franquicias desde el servidor de manera asíncrona.
     */
    private void getFranchiseNamesFromServer(){
        OneTimeWorkRequest otwr = new OneTimeWorkRequest.Builder(GetFrachiseNames.class).build();
        WorkManager.getInstance(myContext).getWorkInfoByIdLiveData(otwr.getId())
                .observe((LifecycleOwner) this.myContext, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        if(workInfo != null && workInfo.getState().isFinished()){
                            String datos = workInfo.getOutputData().getString("datos");
                            ArrayList<String> franchiseNames = StringManipulator.getArrayListFromStringifiedArray(datos);
                            names = franchiseNames;
                            obtainFranchisesFromNames();
                        }
                    }
                });
        WorkManager.getInstance(myContext.getApplicationContext()).enqueue(otwr);
    }


    private void obtainFranchisesFromNames(){
        for(String franchiseName : this.names) {
            this.getFranchiseFromServerByName(franchiseName);
        }
    }


    private void getFranchiseFromServerByName(String franchiseName) {
        Data datos = new Data.Builder().putString(FRANCHISE_NAME, franchiseName).build();
        OneTimeWorkRequest otwr =  new OneTimeWorkRequest.Builder(GetFranchiseByNameWorker.class)
                .setInputData(datos).build();
        WorkManager.getInstance(this.myContext).getWorkInfoByIdLiveData(otwr.getId())
                .observe((LifecycleOwner) this.myContext, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        if(workInfo != null && workInfo.getState().isFinished()){
                            Log.i("FranchiseManager", "SUCCESS!!!!");
                        }
                    }
                });
        WorkManager.getInstance(myContext.getApplicationContext()).enqueue(otwr);
    }


    public ArrayList<Franchise> getFranchises() {
        return franchises;
    }

    public Franchise getFranchise(int pos){
        return  franchises.get(pos);
    }

    public void addFranquise(Franchise franchise){
        this.franchises.add(franchise);
    }
}
