package ITM.maint.fiix_custom_mobile.data.repository;


import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ITM.maint.fiix_custom_mobile.R;
import ITM.maint.fiix_custom_mobile.constants.AssetCategories;
import ITM.maint.fiix_custom_mobile.constants.AssetTypes;
import ITM.maint.fiix_custom_mobile.constants.Assets;
import ITM.maint.fiix_custom_mobile.constants.StatusCodes;
import ITM.maint.fiix_custom_mobile.data.api.IAssetService;
import ITM.maint.fiix_custom_mobile.data.api.ServiceGenerator;
import ITM.maint.fiix_custom_mobile.data.api.requests.FindRequest;
import ITM.maint.fiix_custom_mobile.data.model.FiixDatabase;
import ITM.maint.fiix_custom_mobile.data.model.dao.IAssetDao;
import ITM.maint.fiix_custom_mobile.data.model.entity.Asset;
import ITM.maint.fiix_custom_mobile.data.model.entity.AssetCategory;
import ITM.maint.fiix_custom_mobile.utils.Status;
import io.reactivex.Completable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;

public class AssetRepository extends BaseRepository implements IAssetRepository{

    private static final String TAG = "AssetRepository";
    private IAssetService assetService;
    private MutableLiveData<List<Asset>> assetMutableLiveData;
    private MutableLiveData<List<AssetCategory>> assetCategoryListMutableLiveData;
    private MutableLiveData<Status> status;

    private IAssetDao assetDao;
    private FiixDatabase fiixDatabase;
    private CompositeDisposable compositeDisposable;

    private List<Integer> assetIds;
    private List<Integer> assetCategoryIds;
    private int assetType;
    private int assetCategoryId;

    public AssetRepository(Application application) {
        super(application);
        assetMutableLiveData = new MutableLiveData<List<Asset>>();
        assetCategoryListMutableLiveData = new MutableLiveData<List<AssetCategory>>();
        assetService = ServiceGenerator.createService(IAssetService.class);

        fiixDatabase = FiixDatabase.getDatabase(application);
        assetDao = fiixDatabase.assetDao();
        compositeDisposable = new CompositeDisposable();

        assetIds = new ArrayList<>();
        assetCategoryIds = new ArrayList<>();
        assetType = AssetTypes.ALL.getValue();
    }

    @Override
    public void findAsset(List<Integer> assetIds) {

        this.assetIds = assetIds;
        Single<List<Asset>> single =  fiixDatabase.assetDao().getAssetFromIds(assetIds);;
        Scheduler scheduler = Schedulers.from(getRepositoryExecutor().databaseThread());
        single.subscribeOn(scheduler).subscribe(new SingleObserver<List<Asset>>() {
            @Override
            public void onSubscribe(Disposable d) {
                // add it to a CompositeDisposable
                compositeDisposable.add(d);
            }

            @Override
            public void onSuccess(List<Asset> assets) {
                if (assets == null || assets.size() == 0) {
                    //no tasks in database -> request from Fiix
                    getAssetsFromFiix();
                } else {
                    assetMutableLiveData.postValue(assets);
                }
            }

            @Override
            public void onError(Throwable e) {
                // show an error message
                getAssetsFromFiix();
                Log.d(TAG, "Error finding assets from DB");
            }

            //@Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void findAssetsByType(int assetType) {

        this.assetType = assetType;
        Single<List<Asset>> single;
        if (assetType == AssetTypes.ALL.getValue()) {
            single = fiixDatabase.assetDao().getAssets();
        } else {
            single = fiixDatabase.assetDao().getAssetsByType(assetType);
        }
    }

    @Override
    public void findAssetCategory(int assetCategoryId) {

        this.assetCategoryId = assetCategoryId;
        Single<AssetCategory> single = fiixDatabase.assetDao().getAssetCategory(assetCategoryId);
        Scheduler scheduler = Schedulers.from(getRepositoryExecutor().databaseThread());
        single.subscribeOn(scheduler).subscribe(new SingleObserver<AssetCategory>() {
            @Override
            public void onSubscribe(Disposable d) {
                // add it to a CompositeDisposable
                compositeDisposable.add(d);
            }

            @Override
            public void onSuccess(AssetCategory category) {
                if (category == null) {
                    //no tasks in database -> request from Fiix
                    getAssetsCategoryFromFiix();
                } else {
                    List<AssetCategory> assetCategories = new ArrayList<>();
                    assetCategories.add(category);
                    assetCategoryListMutableLiveData.postValue(assetCategories);
                }
            }

            @Override
            public void onError(Throwable e) {
                // show an error message
                getAssetsCategoryFromFiix();
                Log.d(TAG, "Error finding assets from DB");
            }

        });

    }


    private void getAssetsFromFiix() {

        FindRequest.ClientVersion clientVersion = new FindRequest.ClientVersion(
                2, 8, 1);

        List<String> assetFields = new ArrayList<>(Arrays.asList(
                Assets.id.getField(),
                Assets.name.getField(),
                Assets.description.getField(),
                Assets.categoryId.getField()
        ));
        String fields = TextUtils.join(",", assetFields);

        List<String> placeHolders = new ArrayList<>();
        List<Integer> parameters = new ArrayList<>();
        for (Integer id : assetIds) {
            parameters.add(id);
            placeHolders.add("?");
        }
        String placeHolderList = TextUtils.join(",", placeHolders);

        FindRequest.Filter filter = new FindRequest.Filter(
                "id in (" + placeHolderList + ")",
                parameters
        );

        List<FindRequest.Filter> filters = new ArrayList<>();
        filters.add(filter);

        FindRequest assetRequest = new FindRequest("FindRequest", clientVersion, "Asset", fields, filters);
        requestAssetsFromFiix(assetRequest);
    }

    private void requestAssetsFromFiix(FindRequest assetRequest){
        assetService.findAssets(assetRequest)
                .enqueue(new Callback<List<Asset>>() {

                    @Override
                    public void onResponse(Call<List<Asset>> call, retrofit2.Response<List<Asset>> response) {
                        if (response.body() != null){
                            assetMutableLiveData.postValue(response.body());
                            addAssets(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Asset>> call, Throwable t) {
                        assetMutableLiveData.postValue(null);

                        if (t instanceof IOException) {
                            Log.d(TAG, "this is an actual network failure: " + t.getMessage());
                            // logging probably not necessary
                        }
                        else {
                            Log.d(TAG, "conversion issue! big problems: " + t.getMessage());
                        }
                    }
                });
    }


    private void getAssetsCategoryFromFiix() {

        FindRequest.ClientVersion clientVersion = new FindRequest.ClientVersion(
                2, 8, 1);

        List<String> assetFields = new ArrayList<>(Arrays.asList(
                AssetCategories.id.getField(),
                AssetCategories.name.getField(),
                AssetCategories.sysCode.getField(),
                AssetCategories.parentId.getField()
        ));
        String fields = TextUtils.join(",", assetFields);

        List list = Stream.of(assetCategoryId).collect(Collectors.toList());

        FindRequest.Filter filter = new FindRequest.Filter(
                "id = ?",
                list
        );

        List<FindRequest.Filter> filters = new ArrayList<>();
        filters.add(filter);

        FindRequest assetRequest = new FindRequest("FindRequest", clientVersion, "AssetCategory", fields, filters);
        requestAssetCategoryFromFiix(assetRequest);
    }

    private void requestAssetCategoryFromFiix(FindRequest assetRequest){
        assetService.findAssetCategories(assetRequest)
                .enqueue(new Callback<List<AssetCategory>>() {

                    @Override
                    public void onResponse(Call<List<AssetCategory>> call, retrofit2.Response<List<AssetCategory>> response) {
                        if (response.body() != null){
                            assetCategoryListMutableLiveData.postValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<AssetCategory>> call, Throwable t) {
                        assetCategoryListMutableLiveData.postValue(null);

                        if (t instanceof IOException) {
                            Log.d(TAG, "this is an actual network failure: " + t.getMessage());
                            // logging probably not necessary
                        }
                        else {
                            Log.d(TAG, "conversion issue! big problems: " + t.getMessage());
                        }
                    }
                });
    }

    private void requestAssetCategoriesFromFiix(FindRequest assetRequest){
        assetService.findAssetCategories(assetRequest)
                .enqueue(new Callback<List<AssetCategory>>() {

                    @Override
                    public void onResponse(Call<List<AssetCategory>> call, retrofit2.Response<List<AssetCategory>> response) {
                        if (response.body() != null){
                            assetCategoryListMutableLiveData.postValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<AssetCategory>> call, Throwable t) {
                        assetCategoryListMutableLiveData.postValue(null);

                        if (t instanceof IOException) {
                            Log.d(TAG, "this is an actual network failure: " + t.getMessage());
                            // logging probably not necessary
                        }
                        else {
                            Log.d(TAG, "conversion issue! big problems: " + t.getMessage());
                        }
                    }
                });
    }


    private void addAssets(List<Asset> assets){
        Completable completable = fiixDatabase.assetDao().insertAssets(assets);
        Scheduler scheduler = Schedulers.from(getRepositoryExecutor().databaseThread());
        disposableCompletableObserver = new DisposableCompletableObserver() {
            @Override
            public void onComplete() {
                String msg = application.getResources().getString(R.string.work_orders_added);
                Status newStatus = new Status(StatusCodes.addComplete, "Asset", msg);
                status.postValue(newStatus);
                Log.d(TAG, msg);
                Log.d(TAG, "priorities added to DB");
            }

            @Override
            public void onError(Throwable e) {
                String msg = application.getResources().getString(R.string.work_order_add_error);
                Log.d(TAG, "Error adding priorities to DB");
            }
        };
        completable.subscribeOn(scheduler)
                .subscribe(disposableCompletableObserver);
        compositeDisposable.add(disposableCompletableObserver);
    }

    private void addAssetCategories(List<AssetCategory> assetCategories){
        Completable completable = fiixDatabase.assetDao().insertAssetCategories(assetCategories);
        Scheduler scheduler = Schedulers.from(getRepositoryExecutor().databaseThread());
        disposableCompletableObserver = new DisposableCompletableObserver() {
            @Override
            public void onComplete() {
                String msg = application.getResources().getString(R.string.work_orders_added);
                Status newStatus = new Status(StatusCodes.addComplete, "Asset", msg);
                status.postValue(newStatus);
                Log.d(TAG, msg);
                Log.d(TAG, "priorities added to DB");
            }

            @Override
            public void onError(Throwable e) {
                String msg = application.getResources().getString(R.string.work_order_add_error);
                Log.d(TAG, "Error adding priorities to DB");
            }
        };
        completable.subscribeOn(scheduler)
                .subscribe(disposableCompletableObserver);
        compositeDisposable.add(disposableCompletableObserver);
    }


    public void dispose(){
        if (compositeDisposable != null)
            compositeDisposable.dispose();
    }

    public MutableLiveData<List<Asset>> getAssetMutableLiveData() {
        return assetMutableLiveData;
    }

    public void setAssetMutableLiveData(MutableLiveData<List<Asset>> assetMutableLiveData) {
        this.assetMutableLiveData = assetMutableLiveData;
    }


    public MutableLiveData<List<AssetCategory>> getAssetCategoryListMutableLiveData() {
        return assetCategoryListMutableLiveData;
    }

    public void setAssetCategoryListMutableLiveData(MutableLiveData<List<AssetCategory>> assetCategoryListMutableLiveData) {
        this.assetCategoryListMutableLiveData = assetCategoryListMutableLiveData;
    }

    public MutableLiveData<Status> getStatus() {
        return status;
    }
}
