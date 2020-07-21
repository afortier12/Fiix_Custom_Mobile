package ITM.maint.fiix_custom_mobile.utils.Workers;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ITM.maint.fiix_custom_mobile.constants.Priorities;
import ITM.maint.fiix_custom_mobile.constants.StatusCodes;
import ITM.maint.fiix_custom_mobile.data.api.ErrorUtils;
import ITM.maint.fiix_custom_mobile.data.api.ILookupTableService;
import ITM.maint.fiix_custom_mobile.data.api.IWorkOrderService;
import ITM.maint.fiix_custom_mobile.data.api.ServiceGenerator;
import ITM.maint.fiix_custom_mobile.data.api.requests.FindRequest;
import ITM.maint.fiix_custom_mobile.data.api.responses.APIError;
import ITM.maint.fiix_custom_mobile.data.model.FiixDatabase;
import ITM.maint.fiix_custom_mobile.data.model.dao.ILookupTablesDao;
import ITM.maint.fiix_custom_mobile.data.model.entity.MaintenanceType;
import ITM.maint.fiix_custom_mobile.data.model.entity.Priority;
import ITM.maint.fiix_custom_mobile.utils.Box;
import ITM.maint.fiix_custom_mobile.utils.Status;
import ITM.maint.fiix_custom_mobile.utils.Utils;
import io.reactivex.Completable;
import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MaintenanceTypeSyncWorker extends Worker {

    private static final String TAG = "SyncMaintenanceType";
    private ILookupTableService lookupTableService;
    private ILookupTablesDao lookupTablesDao;
    private FiixDatabase fiixDatabase;

    private static final String RESPONSE_KEY = "Maintenance_Type";

    public MaintenanceTypeSyncWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        FindRequest.ClientVersion clientVersion = new FindRequest.ClientVersion(
                2, 8, 1);

        List<String> typeFields = new ArrayList<>(Arrays.asList(
                Priorities.id.getField(),
                Priorities.order.getField(),
                Priorities.name.getField()
        ));

        String fields = TextUtils.join(",", typeFields);

        FindRequest request = new FindRequest("FindRequest", clientVersion, "MaintenanceType", fields, null);
        lookupTableService = ServiceGenerator.createService(ILookupTableService.class);
        Call<List<MaintenanceType>> call = lookupTableService.getMaintenanceTypeList(request);
        try{
            Response<List<MaintenanceType>> response = call.execute();
            if (response.isSuccessful()) {
                if (response.body() != null) {
                    if (response.body().isEmpty()) {
                        Log.d(TAG, "Maintenance type request returned empty list");
                    } else {

                        fiixDatabase = FiixDatabase.getDatabase(getApplicationContext());
                        lookupTablesDao = fiixDatabase.lookupTablesDao();
                        CompositeDisposable compositeDisposable = new CompositeDisposable();

                        Completable completable = fiixDatabase.lookupTablesDao().insertTypes(response.body());
                        DisposableCompletableObserver disposableCompletableObserver = new DisposableCompletableObserver() {
                            @Override
                            public void onComplete() {
                                Log.d(TAG, "priorities added to DB");
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d(TAG, "Error adding priorities to DB");
                            }
                        };
                        completable.subscribeOn(Schedulers.io())
                                .subscribe(disposableCompletableObserver);
                        compositeDisposable.add(disposableCompletableObserver);
                        return Result.success();
                    }

                } else {
                    Log.d(TAG, "Maintenance type response is empty");
                }
            } else {
                Log.d(TAG, "Maintenance type response error");
            }

        } catch (Exception e) {
            if (e instanceof Exception) {
                Log.d(TAG, "this is an actual network failure: " + e.getMessage());
                // logging probably not necessary
            } else {
                Log.d(TAG, "conversion issue! big problems: " + e.getMessage());
            }
        }
        return Result.failure();
    }
}
