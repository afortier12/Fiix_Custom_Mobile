package ITM.maint.fiix_custom_mobile.utils.Workers;

import android.content.Context;
import android.content.res.AssetManager;
import android.renderscript.ScriptGroup;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import ITM.maint.fiix_custom_mobile.data.model.FiixDatabase;
import ITM.maint.fiix_custom_mobile.data.model.entity.Cause;
import ITM.maint.fiix_custom_mobile.data.model.entity.FailureCodeNesting;
import ITM.maint.fiix_custom_mobile.data.model.entity.Problem;
import ITM.maint.fiix_custom_mobile.data.model.entity.Source;
import io.reactivex.Completable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;

public class RCASourceWorker extends Worker {

    private static final String TAG = "RCASourceWorker";
    private static final String RESPONSE_KEY = "RCA_SOURCE";

    private static final String fileName = "asset_tree.json";

    public RCASourceWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        AssetManager assetManager = getApplicationContext().getAssets();
        try {
            InputStream inputStream = assetManager.open(fileName);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String jsonString =bufferedReader
                    .lines()
                    .collect(Collectors.joining("\n"));

            try {
                JSONObject jsonObject = null;
                jsonObject = new JSONObject(jsonString);
            } catch (JSONException e){
                JSONArray jsonArray = null;
                jsonArray = new JSONArray(jsonString);
            }

            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<Source>>() {}.getType();
            ArrayList<Source> sourceNesting =gson.fromJson(jsonString, listType);

            FiixDatabase fiixDatabase = FiixDatabase.getDatabase(getApplicationContext());
            CompositeDisposable compositeDisposable = new CompositeDisposable();

            Completable completable = fiixDatabase.rcaDao().insertSourceNesting(sourceNesting);
            DisposableCompletableObserver disposableCompletableObserver = new DisposableCompletableObserver() {
                @Override
                public void onComplete() {
                    Log.d(TAG, "RCA sources added to DB");
                }

                @Override
                public void onError(Throwable e) {
                    Log.d(TAG, e.getMessage());
                    Log.d(TAG, "Error adding RCA sources to DB");
                }
            };
            completable.subscribeOn(Schedulers.io())
                    .subscribe(disposableCompletableObserver);
            compositeDisposable.add(disposableCompletableObserver);

            return Result.success();
        } catch (Exception e){
            Log.d(TAG, e.getMessage());
            return Result.failure();
        }
    }


}
