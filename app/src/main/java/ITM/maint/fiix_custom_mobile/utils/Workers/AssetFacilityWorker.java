package ITM.maint.fiix_custom_mobile.utils.Workers;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ITM.maint.fiix_custom_mobile.constants.AssetCategories;
import ITM.maint.fiix_custom_mobile.constants.Assets;
import ITM.maint.fiix_custom_mobile.data.api.IAssetService;
import ITM.maint.fiix_custom_mobile.data.api.ServiceGenerator;
import ITM.maint.fiix_custom_mobile.data.api.requests.FindRequest;
import ITM.maint.fiix_custom_mobile.data.model.FiixDatabase;
import ITM.maint.fiix_custom_mobile.data.model.entity.Asset;
import ITM.maint.fiix_custom_mobile.data.model.entity.AssetCategory;
import io.reactivex.Completable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Response;

public class AssetFacilityWorker extends Worker {

    private static final String TAG = "AssetFacilitySync";
    private static final String RESPONSE_KEY = "AssetFacilityWorker";

    public AssetFacilityWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        FindRequest assetFacilityRequest = setupGetAllAssetFacilitiesAPICall();
        IAssetService assetService = ServiceGenerator.createService(IAssetService.class);
        Call<List<Asset>> call = assetService.findAssets(assetFacilityRequest);
        try{
            Response<List<Asset>> response = call.execute();
            if (response.isSuccessful()) {
                if (response.body() != null) {
                    if (response.body().isEmpty()) {
                        Log.d(TAG, "Asset facility request returned empty list");
                    } else {

                        FiixDatabase fiixDatabase = FiixDatabase.getDatabase(getApplicationContext());
                        CompositeDisposable compositeDisposable = new CompositeDisposable();

                        Completable completable = fiixDatabase.assetDao().insertAssets(response.body());
                        DisposableCompletableObserver disposableCompletableObserver = new DisposableCompletableObserver() {
                            @Override
                            public void onComplete() {
                                Log.d(TAG, "Asset facilities added to DB");
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d(TAG, "Error adding asset facilities to DB");
                            }
                        };
                        completable.subscribeOn(Schedulers.io())
                                .subscribe(disposableCompletableObserver);
                        compositeDisposable.add(disposableCompletableObserver);
                        return Result.success();
                    }

                } else {
                    Log.d(TAG, "Asset facilities response is empty");
                }
            } else {
                Log.d(TAG, "Asset facilities response error");
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

    private FindRequest setupGetAllAssetFacilitiesAPICall() {

        FindRequest.ClientVersion clientVersion = new FindRequest.ClientVersion(
                2, 8, 1);

        List<String> assetFields = new ArrayList<>(Arrays.asList(
                Assets.id.getField(),
                Assets.name.getField(),
                Assets.categoryId.getField(),
                Assets.assetLocationID.getField(),
                Assets.assetType.getField(),
                Assets.description.getField(),
                Assets.displayCategoryId.getField(),
                Assets.make.getField(),
                Assets.model.getField()
        ));
        String fields = TextUtils.join(",", assetFields);

        List list = Stream.of(1).collect(Collectors.toList());

        FindRequest.Filter filter = new FindRequest.Filter(
                "intKind = ?",
                list
        );

        List<FindRequest.Filter> filters = new ArrayList<>();
        filters.add(filter);

        FindRequest assetRequest = new FindRequest("FindRequest", clientVersion, "Asset", fields, filters);
        return assetRequest;
    }

}
