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

import ITM.maint.fiix_custom_mobile.constants.MaintenanceTypes;
import ITM.maint.fiix_custom_mobile.constants.WorkOrderStatuses;
import ITM.maint.fiix_custom_mobile.data.api.ILookupTableService;
import ITM.maint.fiix_custom_mobile.data.api.ServiceGenerator;
import ITM.maint.fiix_custom_mobile.data.api.requests.FindRequest;
import ITM.maint.fiix_custom_mobile.data.model.FiixDatabase;
import ITM.maint.fiix_custom_mobile.data.model.dao.ILookupTablesDao;
import ITM.maint.fiix_custom_mobile.data.model.entity.MaintenanceType;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrderStatus;
import io.reactivex.Completable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Response;

public class WorkOrderStatusSyncWorker extends Worker {

    private static final String TAG = "WorkOrderStatusSync";
    private static final String RESPONSE_KEY = "WorkOrder_Status";

    public WorkOrderStatusSyncWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        FindRequest.ClientVersion clientVersion = new FindRequest.ClientVersion(
                2, 8, 1);

        List<String> typeFields = new ArrayList<>(Arrays.asList(
                WorkOrderStatuses.id.getField(),
                WorkOrderStatuses.controlId.getField(),
                WorkOrderStatuses.name.getField(),
                WorkOrderStatuses.sysCode.getField()
        ));

        String fields = TextUtils.join(",", typeFields);

        FindRequest request = new FindRequest("FindRequest", clientVersion, "WorkOrderStatus", fields, null);
        ILookupTableService lookupTableService = ServiceGenerator.createService(ILookupTableService.class);
        Call<List<WorkOrderStatus>> call = lookupTableService.getWorkOrderStatusList(request);
        try{
            Response<List<WorkOrderStatus>> response = call.execute();
            if (response.isSuccessful()) {
                if (response.body() != null) {
                    if (response.body().isEmpty()) {
                        Log.d(TAG, "Work order status request returned empty list");
                    } else {

                        FiixDatabase fiixDatabase = FiixDatabase.getDatabase(getApplicationContext());
                        CompositeDisposable compositeDisposable = new CompositeDisposable();

                        Completable completable = fiixDatabase.lookupTablesDao().insertStatuses(response.body());
                        DisposableCompletableObserver disposableCompletableObserver = new DisposableCompletableObserver() {
                            @Override
                            public void onComplete() {
                                Log.d(TAG, "Work order statuses added to DB");
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d(TAG, "Error adding work order statuses to DB");
                            }
                        };
                        completable.subscribeOn(Schedulers.io())
                                .subscribe(disposableCompletableObserver);
                        compositeDisposable.add(disposableCompletableObserver);
                        return Result.success();
                    }

                } else {
                    Log.d(TAG, "Work order status response is empty");
                }
            } else {
                Log.d(TAG, "Work order status response error");
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
}
