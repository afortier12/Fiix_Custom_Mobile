package ITM.maint.fiix_custom_mobile.utils.Workers;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;
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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ITM.maint.fiix_custom_mobile.R;
import ITM.maint.fiix_custom_mobile.constants.AssetCategories;
import ITM.maint.fiix_custom_mobile.constants.StatusCodes;
import ITM.maint.fiix_custom_mobile.data.api.IAssetService;
import ITM.maint.fiix_custom_mobile.data.api.ILookupTableService;
import ITM.maint.fiix_custom_mobile.data.api.ServiceGenerator;
import ITM.maint.fiix_custom_mobile.data.api.requests.FindRequest;
import ITM.maint.fiix_custom_mobile.data.model.FiixDatabase;
import ITM.maint.fiix_custom_mobile.data.model.entity.AssetCategory;
import ITM.maint.fiix_custom_mobile.data.model.entity.Source;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrderStatus;
import ITM.maint.fiix_custom_mobile.utils.Status;
import io.reactivex.Completable;
import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AssetCategoryWorker extends Worker {

    private static final String TAG = "AssetCategorySync";
    private static final String RESPONSE_KEY = "AssetCategory";

    public AssetCategoryWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        FindRequest assetCategoryRequest = setupGetAllAssetCategoriesAPICall();
        IAssetService assetService = ServiceGenerator.createService(IAssetService.class);
        Call<List<AssetCategory>> call = assetService.getAllAssetCategories(assetCategoryRequest);
        try{
            Response<List<AssetCategory>> response = call.execute();
            if (response.isSuccessful()) {
                if (response.body() != null) {
                    if (response.body().isEmpty()) {
                        Log.d(TAG, "Work order status request returned empty list");
                    } else {

                        FiixDatabase fiixDatabase = FiixDatabase.getDatabase(getApplicationContext());
                        CompositeDisposable compositeDisposable = new CompositeDisposable();

                        Completable completable = fiixDatabase.assetDao().insertAssetCategories(response.body());
                        DisposableCompletableObserver disposableCompletableObserver = new DisposableCompletableObserver() {
                            @Override
                            public void onComplete() {
                                Log.d(TAG, "Asset categories added to DB");
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d(TAG, "Error adding asset categories to DB");
                            }
                        };
                        completable.subscribeOn(Schedulers.io())
                                .subscribe(disposableCompletableObserver);
                        compositeDisposable.add(disposableCompletableObserver);
                        return Result.success();
                    }

                } else {
                    Log.d(TAG, "Asset categories response is empty");
                }
            } else {
                Log.d(TAG, "Asset categories response error");
            }

        } catch (Exception e) {
            if (e instanceof IOException) {
                Log.d(TAG, "this is an actual network failure: " + e.getMessage());
                // logging probably not necessary
            } else {
                Log.d(TAG, "conversion issue! big problems: " + e.getMessage());
            }
        }
        return Result.failure();

    }

    private FindRequest setupGetAllAssetCategoriesAPICall() {

        FindRequest.ClientVersion clientVersion = new FindRequest.ClientVersion(
                2, 8, 1);

        List<String> assetFields = new ArrayList<>(Arrays.asList(
                AssetCategories.id.getField(),
                AssetCategories.name.getField(),
                AssetCategories.sysCode.getField(),
                AssetCategories.parentId.getField()
        ));
        String fields = TextUtils.join(",", assetFields);

        List<FindRequest.Filter> filters = null;

        FindRequest assetRequest = new FindRequest("FindRequest", clientVersion, "AssetCategory", fields, filters);
        return assetRequest;
    }

}
