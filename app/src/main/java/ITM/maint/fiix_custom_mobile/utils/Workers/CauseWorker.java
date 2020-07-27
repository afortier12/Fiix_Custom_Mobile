package ITM.maint.fiix_custom_mobile.utils.Workers;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.stream.Collectors;

import ITM.maint.fiix_custom_mobile.data.model.FiixDatabase;
import ITM.maint.fiix_custom_mobile.data.model.entity.Cause;
import io.reactivex.Completable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;

public class CauseWorker extends Worker {

    private static final String TAG = "CauseWorker";
    private static final String RESPONSE_KEY = "Cause";

    private static final String cause_filename = "cause.json";

    public CauseWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        AssetManager assetManager = getApplicationContext().getAssets();
        try {
            InputStream inputStream = assetManager.open(cause_filename);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String jsonString =bufferedReader
                    .lines()
                    .collect(Collectors.joining("\n"));

            JSONObject jsonObject = null;
            jsonObject = new JSONObject(jsonString);

            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<Cause>>() {}.getType();
            ArrayList<Cause> causes =gson.fromJson(jsonString, listType);

            FiixDatabase fiixDatabase = FiixDatabase.getDatabase(getApplicationContext());
            CompositeDisposable compositeDisposable = new CompositeDisposable();

            Completable completable = fiixDatabase.rcaDao().insertCauses(causes);
            DisposableCompletableObserver disposableCompletableObserver = new DisposableCompletableObserver() {
                @Override
                public void onComplete() {
                    Log.d(TAG, "Causes added to DB");
                }

                @Override
                public void onError(Throwable e) {
                    Log.d(TAG, "Error adding causes to DB");
                }
            };
            completable.subscribeOn(Schedulers.io())
                    .subscribe(disposableCompletableObserver);
            compositeDisposable.add(disposableCompletableObserver);

            return Result.success();
        } catch (Exception e){
            return Result.failure();
        }
    }
}