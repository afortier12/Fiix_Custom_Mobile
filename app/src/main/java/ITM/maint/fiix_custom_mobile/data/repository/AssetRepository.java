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
import ITM.maint.fiix_custom_mobile.data.model.entity.Asset.AssetDepartmentPlant;
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
    private MutableLiveData<List<AssetDepartmentPlant>> deptPlantMutableLiveData;
    private MutableLiveData<Status> status;

    private IAssetDao assetDao;
    private FiixDatabase fiixDatabase;
    private CompositeDisposable compositeDisposable;

    private List<Integer> assetIds;
    private boolean addAssetForDeptPlantFlag = false;
    private List<Integer> assetCategoryIds;
    private int assetType;
    private int assetCategoryId;

    public AssetRepository(Application application) {
        super(application);
        assetMutableLiveData = new MutableLiveData<List<Asset>>();
        assetCategoryListMutableLiveData = new MutableLiveData<List<AssetCategory>>();
        deptPlantMutableLiveData = new MutableLiveData<List<AssetDepartmentPlant>>();
        status = new MutableLiveData<Status>();
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
        this.assetIds = new ArrayList<>(assetIds);
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
                    setupGetAssetsAPICall();
                } else {
                    assetMutableLiveData.postValue(assets);
                }
            }

            @Override
            public void onError(Throwable e) {
                // show an error message
                setupGetAssetsAPICall();
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
                    setupGetAssetsCategoryAPICall();
                } else {
                    List<AssetCategory> assetCategories = new ArrayList<>();
                    assetCategories.add(category);
                    assetCategoryListMutableLiveData.postValue(assetCategories);
                }
            }

            @Override
            public void onError(Throwable e) {
                // show an error message
                setupGetAssetsCategoryAPICall();
                Log.d(TAG, "Error finding assets from DB");
            }

        });

    }

    @Override
    public void getAllAssetCategories() {
        Single<List<AssetCategory>> single =  fiixDatabase.assetDao().getAssetCategories();
        Scheduler scheduler = Schedulers.from(getRepositoryExecutor().databaseThread());
        single.subscribeOn(scheduler).subscribe(new SingleObserver<List<AssetCategory>>() {
            @Override
            public void onSubscribe(Disposable d) {
                // add it to a CompositeDisposable
                compositeDisposable.add(d);
            }

            @Override
            public void onSuccess(List<AssetCategory> assetCategories) {
                if (assetCategories == null || assetCategories.size() == 0) {
                    //no tasks in database -> request from Fiix
                    setupGetAllAssetCategoriesAPICall();
                } else {
                    Log.d(TAG, "Asset categories retrieved from DB");
                }
            }

            @Override
            public void onError(Throwable e) {
                // show an error message
                setupGetAssetsAPICall();
                Log.d(TAG, "Error finding assets from DB");
            }

            //@Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void getAssetDepartmentPlant(List<Integer> assetIds) {
        this.assetIds = new ArrayList<>(assetIds);
        Single<List<Asset>> single =  fiixDatabase.assetDao().getAssetFromIds(assetIds);
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
                    //no assets in database -> request from Fiix
                    setupGetAssetsForDeptPlantAPICall();
                } else {
                    getDepartmentPlantFromDB();
                    Log.d(TAG, "Asset categories retrieved from DB");
                }
            }

            @Override
            public void onError(Throwable e) {
                // show an error message
                setupGetAssetsAPICall();
                Log.d(TAG, "Error finding assets from DB");
            }

        });
    }

    private void getDepartmentPlantFromDB(){
        Single<List<AssetDepartmentPlant>> single =  fiixDatabase.assetDao().getDepartmentPlants(assetIds);
        Scheduler scheduler = Schedulers.from(getRepositoryExecutor().databaseThread());
        single.subscribeOn(scheduler).subscribe(new SingleObserver<List<AssetDepartmentPlant>>() {
            @Override
            public void onSubscribe(Disposable d) {
                // add it to a CompositeDisposable
                compositeDisposable.add(d);
            }

            @Override
            public void onSuccess(List<AssetDepartmentPlant> departmentPlants) {
                if (departmentPlants == null || departmentPlants.size() == 0) {
                    //no tasks in database -> request from Fiix
                    deptPlantMutableLiveData.postValue(null);
                    Log.d(TAG, "Error finding departments and plants from DB");
                } else {
                    deptPlantMutableLiveData.postValue(departmentPlants);
                    Log.d(TAG, "Plants and departments retrieved from DB");
                }
            }

            @Override
            public void onError(Throwable e) {
                // show an error message
                deptPlantMutableLiveData.postValue(null);
                Log.d(TAG, "Error finding departments and plants from DB");
            }

        });
    }

    private void setupGetAssetsAPICall() {

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
        getAssetsFromFiix(assetRequest);
    }

    private void getAssetsFromFiix(FindRequest assetRequest){
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


    private void setupGetAssetsForDeptPlantAPICall() {

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

        List<String> placeHolders = new ArrayList<>();
        List<Integer> parameters = new ArrayList<>();
        for (Integer id : assetIds) {
            parameters.add(id);
            placeHolders.add("?");
        }
        parameters.add(1); // for intKind = 1 (facility, plant, department, building)

        String placeHolderList = TextUtils.join(",", placeHolders);

        FindRequest.Filter filter = new FindRequest.Filter(
                "id in (" + placeHolderList + ") OR intKind = ?",
                parameters
        );

        List<FindRequest.Filter> filters = new ArrayList<>();
        filters.add(filter);

        FindRequest assetRequest = new FindRequest("FindRequest", clientVersion, "Asset", fields, filters);
        getAssetsForDeptPlantFromFiix(assetRequest);
    }

    private void getAssetsForDeptPlantFromFiix(FindRequest assetRequest){
        assetService.findAssets(assetRequest)
                .enqueue(new Callback<List<Asset>>() {

                    @Override
                    public void onResponse(Call<List<Asset>> call, retrofit2.Response<List<Asset>> response) {
                        if (response.body() != null){
                            addAssetForDeptPlantFlag = true;
                            addAssets(response.body());
                        } else {
                            deptPlantMutableLiveData.postValue(null);
                            Log.d(TAG, "Error finding departments and plants from DB");

                        }
                    }

                    @Override
                    public void onFailure(Call<List<Asset>> call, Throwable t) {
                        deptPlantMutableLiveData.postValue(null);

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

    private void setupGetAssetsCategoryAPICall() {

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
        getAssetCategoryFromFiix(assetRequest);
    }

    private void getAssetCategoryFromFiix(FindRequest assetRequest){
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

    private void setupGetAllAssetCategoriesAPICall() {

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
        getAllAssetCategoriesFromFiix(assetRequest);
    }

    private void getAllAssetCategoriesFromFiix(FindRequest assetRequest){
        assetService.findAssetCategories(assetRequest)
                .enqueue(new Callback<List<AssetCategory>>() {

                    @Override
                    public void onResponse(Call<List<AssetCategory>> call, retrofit2.Response<List<AssetCategory>> response) {
                        if (response.body() != null){
                            Log.d(TAG, "retrieved all asset categories from Fiix");
                            addAssetCategories(response.body());
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
                String msg = "Assets added to database";
                Status newStatus = new Status(StatusCodes.addComplete, "Asset", msg);
                status.postValue(newStatus);
                Log.d(TAG, msg);
                Log.d(TAG, "assets added to DB");

                if (addAssetForDeptPlantFlag)
                    getDepartmentPlantFromDB();

                addAssetForDeptPlantFlag = false;
            }

            @Override
            public void onError(Throwable e) {
                addAssetForDeptPlantFlag = false;
                String msg = "error adding asset to DB";
                Log.d(TAG, "Error adding assets to DB");
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
                Log.d(TAG, "asset categories added to DB");
            }

            @Override
            public void onError(Throwable e) {
                String msg = application.getResources().getString(R.string.work_order_add_error);
                Log.d(TAG, "Error adding asset categories to DB");
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

    public MutableLiveData<List<AssetDepartmentPlant>> getDeptPlantMutableLiveData() {
        return deptPlantMutableLiveData;
    }

    public MutableLiveData<Status> getStatus() {
        return status;
    }
}
