package ITM.maint.fiix_custom_mobile.utils.Workers;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.stream.Collectors;

import ITM.maint.fiix_custom_mobile.data.model.FiixDatabase;
import ITM.maint.fiix_custom_mobile.data.model.entity.FailureCodeNesting;
import io.reactivex.Completable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;

public class RCANestingWorker extends Worker {

    private static final String TAG = "RCANestingWorker";
    private static final String RESPONSE_KEY = "RCA_NESTING";

    private static final String fileName = "asset_category_tree.json";

    public RCANestingWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
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
            Type listType = new TypeToken<ArrayList<FailureCodeNesting>>() {}.getType();
            ArrayList<FailureCodeNesting> failureCodeNesting =gson.fromJson(jsonString, listType);

            FiixDatabase fiixDatabase = FiixDatabase.getDatabase(getApplicationContext());
            CompositeDisposable compositeDisposable = new CompositeDisposable();

            Completable completable = fiixDatabase.rcaDao().insertFailureCodeNesting(failureCodeNesting);
            DisposableCompletableObserver disposableCompletableObserver = new DisposableCompletableObserver() {
                @Override
                public void onComplete() {
                    Log.d(TAG, "RCA nesting added to DB");
                }

                @Override
                public void onError(Throwable e) {
                    Log.d(TAG, e.getMessage());
                    Log.d(TAG, "Error adding RCA nesting to DB");
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
