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

import ITM.maint.fiix_custom_mobile.constants.Assets;
import ITM.maint.fiix_custom_mobile.data.api.requests.FindRequest;
import ITM.maint.fiix_custom_mobile.data.api.IPartService;
import ITM.maint.fiix_custom_mobile.data.api.responses.PartFindResponse;
import ITM.maint.fiix_custom_mobile.data.model.FiixDatabase;
import ITM.maint.fiix_custom_mobile.data.model.dao.IPartDao;
import ITM.maint.fiix_custom_mobile.data.model.entity.Part;
import ITM.maint.fiix_custom_mobile.data.api.ServiceGenerator;
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

public class PartRepository extends BaseRepository{

    private static int FRESH_TIMEOUT_IN_MINUTES = 3;

    private static final String TAG ="PartRepository";
    private IPartService partService;
    private MutableLiveData<List<Part>> partResponseMutableLiveData;

    private IPartDao IPartDao;
    private FiixDatabase fiixDatabase;
    private MutableLiveData<Part> partDBMutableLiveData;
    private CompositeDisposable compositeDisposable;
    private DisposableCompletableObserver disposableCompletableObserver;

    public PartRepository(Application application) {
        super(application);
        partDBMutableLiveData = new MutableLiveData<Part>();
        partResponseMutableLiveData = new MutableLiveData<List<Part>>();
        partService = ServiceGenerator.createService(IPartService.class);

        fiixDatabase = FiixDatabase.getDatabase(application);
        IPartDao = fiixDatabase.partDao();

    }

    public void addPart(Part part){
        Completable completable = fiixDatabase.partDao().insert(part);
        Scheduler scheduler = Schedulers.from(getRepositoryExecutor().databaseThread());
        completable.subscribeOn(scheduler)
                .subscribe(disposableCompletableObserver);
        compositeDisposable.add(new DisposableCompletableObserver() {
            @Override
            public void onComplete() {
                Log.d(TAG, "Part added to DB");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "Error adding part to DB");
            }
        });
    }

    public void findPartFromDB(String barcode){
        Single<Part> single = fiixDatabase.partDao().hasPart(barcode, new Date());
        Scheduler scheduler = Schedulers.from(getRepositoryExecutor().databaseThread());

        single.subscribeOn(scheduler);
        single.subscribe(new SingleObserver<Part>() {
            @Override
            public void onSubscribe(Disposable d) {
                // add it to a CompositeDisposable
                compositeDisposable.add(d);
            }

            @Override
            public void onSuccess(Part part) {
                partDBMutableLiveData.postValue(part);
            }

            @Override
            public void onError(Throwable e) {
                // show an error message
                Log.d(TAG, "Error finding part from DB");
            }
        });

    }

    public void findPartsFromDB(String category, String type, String Make){
        Single<List<Part>> single = fiixDatabase.partDao().getParts();
        Scheduler scheduler = Schedulers.from(getRepositoryExecutor().databaseThread());
        single.subscribeOn(scheduler)
                .subscribe(new SingleObserver<List<Part>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        // add it to a CompositeDisposable
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(List<Part> part) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        // show an error
                        Log.d(TAG, "Error finding multiple parts in DB");
                    }
                });

    }

    public void findPartFromFiix(String barcode){
        FindRequest.ClientVersion clientVersion = new FindRequest.ClientVersion(
                2, 8, 1);

        List<String> assetFields = new ArrayList<>(Arrays.asList(
                Assets.id.getField(),
                Assets.name.getField(),
                Assets.description.getField(),
                Assets.make.getField(),
                Assets.model.getField(),
                Assets.barcode.getField(),
                Assets.partNumber.getField()
        ));
        String fields = TextUtils.join(",",assetFields);

        List list = Stream.of(barcode).collect(Collectors.toList());

        FindRequest.Filter filter = new FindRequest.Filter(
                "strBarcode = ?",
                list
        );

        List<FindRequest.Filter> filters = new ArrayList<>();
        filters.add(filter);

        FindRequest partRequest = new FindRequest("FindRequest", clientVersion, "Asset", fields, filters);
        requestPartsFromFiix(partRequest);
    }

    private void requestPartsFromFiix(FindRequest partRequest){
        partService.findParts(partRequest)
                .enqueue(new Callback<PartFindResponse>() {

                    @Override
                    public void onResponse(Call<PartFindResponse> call, retrofit2.Response<PartFindResponse> response) {
                        if (response.body() != null){
                            partResponseMutableLiveData.postValue(response.body().getObjects());
                        }
                    }

                    @Override
                    public void onFailure(Call<PartFindResponse> call, Throwable t) {
                        partResponseMutableLiveData.postValue(null);

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



    private Date getMaxRefreshTime(Date currentDate){
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        cal.add(Calendar.MINUTE, -FRESH_TIMEOUT_IN_MINUTES);
        return cal.getTime();
    }

    public MutableLiveData<List<Part>> getPartResponseMutableLiveData() {
        return partResponseMutableLiveData;
    }

    public void dispose(){
        if (compositeDisposable != null)
            compositeDisposable.dispose();
    }

    public MutableLiveData<Part> getPartDBMutableLiveData() {
        return partDBMutableLiveData;
    }

    public IPartService getPartService() {
        return partService;
    }


}
