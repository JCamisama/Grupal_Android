package com.example.grupal_android.managers;

import android.content.Context;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.example.grupal_android.models.Franchise;
import com.example.grupal_android.workers.GetFrachiseNames;
import com.example.grupal_android.workers.GetFranchisesWorker;

import java.util.ArrayList;

public class FranchiseManager extends AppCompatActivity {
    private static FranchiseManager instance = null;
    private Context myContext = null;
    public static final String defaultLanguage = "en";
    private String names[];
    private ArrayList<Franchise> franchises = new ArrayList<>();

    /*** Singleton Pattern ***/
    private FranchiseManager(Context pContext) {
        this.myContext = pContext;
    }

    public static synchronized FranchiseManager getInstance(Context pContext) {
        if (FranchiseManager.instance == null) {
            FranchiseManager.instance = new FranchiseManager(pContext);
        }

        return FranchiseManager.instance;
    }

    private void  getFranchisesNames(){
        OneTimeWorkRequest otwr = new OneTimeWorkRequest.Builder(GetFrachiseNames.class).build();
        WorkManager.getInstance(myContext).getWorkInfoByIdLiveData(otwr.getId())
                .observe(this, new Observer<WorkInfo>() { //LifecycleOwner cuidado
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        Log.d("pre","ifffffff");
                        if(workInfo != null && workInfo.getState().isFinished()){
                            Log.d("des","if");
                            String datos = workInfo.getOutputData().getString("datos");
                            datos = datos.replace("[","").replace("]","");
                            names = datos.split(", ");
                            Log.d("for","antes");
                            for (int i=0; i<names.length;i++) {
                                Log.d("for","despues");
                                Data datos1 = new Data.Builder().putString("name", names[i]).build();
                                OneTimeWorkRequest otwr = new OneTimeWorkRequest.Builder(GetFranchisesWorker.class).setInputData(datos1).build();
                                WorkManager.getInstance(myContext.getApplicationContext()).enqueue(otwr);
                            }


                        }
                    }
                });
        WorkManager.getInstance(myContext.getApplicationContext()).enqueue(otwr);
    }
    public ArrayList<Franchise> getFranchises(){
        getFranchisesNames();
        return franchises;
    }
    public void addFranquise(Franchise franchise){
        this.franchises.add(franchise);
    }
}
