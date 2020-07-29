package ITM.maint.fiix_custom_mobile.data.repository;


import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ITM.maint.fiix_custom_mobile.constants.AssetCategories;
import ITM.maint.fiix_custom_mobile.constants.Assets;
import ITM.maint.fiix_custom_mobile.data.api.IAssetService;
import ITM.maint.fiix_custom_mobile.data.api.IPartService;
import ITM.maint.fiix_custom_mobile.data.api.IWorkOrderService;
import ITM.maint.fiix_custom_mobile.data.api.ServiceGenerator;
import ITM.maint.fiix_custom_mobile.data.api.requests.FindRequest;
import ITM.maint.fiix_custom_mobile.data.model.FiixDatabase;
import ITM.maint.fiix_custom_mobile.data.model.dao.IAssetDao;
import ITM.maint.fiix_custom_mobile.data.model.dao.IPartDao;
import ITM.maint.fiix_custom_mobile.data.model.entity.Asset;
import ITM.maint.fiix_custom_mobile.data.model.entity.AssetCategory;
import ITM.maint.fiix_custom_mobile.data.model.entity.Part;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrder;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrderTask;
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
    private MutableLiveData<List<AssetCategory>> assetCategoryMutableLiveData;

    private IAssetDao assetDao;
    private FiixDatabase fiixDatabase;
    private CompositeDisposable compositeDisposable;

    List<Integer> assetIds;
    List<Integer> assetCategoryIds;

    public AssetRepository(Application application) {
        super(application);
        assetMutableLiveData = new MutableLiveData<List<Asset>>();
        assetCategoryMutableLiveData = new MutableLiveData<List<AssetCategory>>();
        assetService = ServiceGenerator.createService(IAssetService.class);

        fiixDatabase = FiixDatabase.getDatabase(application);
        assetDao = fiixDatabase.assetDao();
        compositeDisposable = new CompositeDisposable();

        assetIds = new ArrayList<>();
        assetCategoryIds = new ArrayList<>();
    }

    @Override
    public void findAsset(List<Integer> assetIds) {
        this.assetIds = new ArrayList<Integer>(assetIds);
        Single<List<Asset>> single = fiixDatabase.assetDao().getAssetFromIds(assetIds);
        Scheduler scheduler = Schedulers.from(getRepositoryExecutor().databaseThread());
        single.subscribeOn(scheduler).subscribe(new SingleObserver<List<Asset>>() {
            @Override
            public void onSubscribe(Disposable d) {
                // add it to a CompositeDisposable
                compositeDisposable.add(d);
            }

            @Override
            public void onSuccess(List<Asset> assets) {
                if (assets == null) {
                    //no tasks in database -> request from Fiix
                    getAssetsByIdsFromFiix();
                } else {
                    assetMutableLiveData.postValue(assets);
                }
            }



            @Override
            public void onError(Throwable e) {
                // show an error message
                getAssetsByIdsFromFiix();
                Log.d(TAG, "Error finding assets from DB");
            }

            //@Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void findAssetCategory(List<Integer> assetCategoryIds) {
        this.assetCategoryIds = new ArrayList<Integer>(assetCategoryIds);
    }

/*    @Override
    public void findAssets() {
        Single<List<Asset>> single = fiixDatabase.assetDao().getAssets();
        Scheduler scheduler = Schedulers.from(getRepositoryExecutor().databaseThread());
        single.subscribeOn(scheduler).subscribe(new SingleObserver<List<Asset>>() {
            @Override
            public void onSubscribe(Disposable d) {
                // add it to a CompositeDisposable
                compositeDisposable.add(d);
            }

            @Override
            public void onSuccess(List<Asset> assets) {
                if (assets == null) {
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
    public void findAssetCategories() {
        Single<List<AssetCategory>> single = fiixDatabase.assetDao().getAssetCategories();
        Scheduler scheduler = Schedulers.from(getRepositoryExecutor().databaseThread());
        single.subscribeOn(scheduler).subscribe(new SingleObserver<List<AssetCategory>>() {
            @Override
            public void onSubscribe(Disposable d) {
                // add it to a CompositeDisposable
                compositeDisposable.add(d);
            }

            @Override
            public void onSuccess(List<AssetCategory> assets) {
                if (assets == null) {
                    //no tasks in database -> request from Fiix
                    getAssetsCategoriesFromFiix();
                } else {
                    assetCategoryMutableLiveData.postValue(assets);
                }
            }



            @Override
            public void onError(Throwable e) {
                // show an error message
                getAssetsCategoriesFromFiix();
                Log.d(TAG, "Error finding assets from DB");
            }


        });
    }*/


    public void getAssetsFromFiix() {

        FindRequest.ClientVersion clientVersion = new FindRequest.ClientVersion(
                2, 8, 1);

        List<String> assetFields = new ArrayList<>(Arrays.asList(
                Assets.id.getField(),
                Assets.name.getField(),
                Assets.description.getField(),
                Assets.categoryId.getField()
        ));
        String fields = TextUtils.join(",", assetFields);


        FindRequest assetRequest = new FindRequest("FindRequest", clientVersion, "Asset", fields, null);
        requestAssetsFromFiix(assetRequest);
    }

    public void getAssetsByIdsFromFiix() {

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

    public void getAssetCategoriesByIdsFromFiix() {

        FindRequest.ClientVersion clientVersion = new FindRequest.ClientVersion(
                2, 8, 1);

        List<String> assetFields = new ArrayList<>(Arrays.asList(
                AssetCategories.id.getField(),
                AssetCategories.name.getField(),
                AssetCategories.sysCode.getField(),
                AssetCategories.parentId.getField()
                ));
        String fields = TextUtils.join(",", assetFields);
        List<String> placeHolders = new ArrayList<>();
        List<Integer> parameters = new ArrayList<>();
        for (Integer id : assetCategoryIds) {
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

        FindRequest assetRequest = new FindRequest("FindRequest", clientVersion, "AssetCategory", fields, filters);
        requestAssetCategoriesFromFiix(assetRequest);
    }

    public void getAssetsCategoriesFromFiix() {

        FindRequest.ClientVersion clientVersion = new FindRequest.ClientVersion(
                2, 8, 1);

        List<String> assetFields = new ArrayList<>(Arrays.asList(
                AssetCategories.id.getField(),
                AssetCategories.name.getField(),
                AssetCategories.sysCode.getField(),
                AssetCategories.parentId.getField()
        ));
        String fields = TextUtils.join(",", assetFields);


        FindRequest assetRequest = new FindRequest("FindRequest", clientVersion, "AssetCategory", fields, null);
        requestAssetsFromFiix(assetRequest);
    }

    private void requestAssetCategoriesFromFiix(FindRequest assetRequest){
        assetService.findAssetCategories(assetRequest)
                .enqueue(new Callback<List<AssetCategory>>() {

                    @Override
                    public void onResponse(Call<List<AssetCategory>> call, retrofit2.Response<List<AssetCategory>> response) {
                        if (response.body() != null){
                            assetCategoryMutableLiveData.postValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<AssetCategory>> call, Throwable t) {
                        assetCategoryMutableLiveData.postValue(null);

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

    public MutableLiveData<List<AssetCategory>> getAssetCategoryMutableLiveData() {
        return assetCategoryMutableLiveData;
    }

    public void setAssetCategoryMutableLiveData(MutableLiveData<List<AssetCategory>> assetCategoryMutableLiveData) {
        this.assetCategoryMutableLiveData = assetCategoryMutableLiveData;
    }



}
