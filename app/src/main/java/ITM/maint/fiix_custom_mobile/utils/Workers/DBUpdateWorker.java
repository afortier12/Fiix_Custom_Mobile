package ITM.maint.fiix_custom_mobile.utils.Workers;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import ITM.maint.fiix_custom_mobile.data.model.FiixDatabase;
import ITM.maint.fiix_custom_mobile.data.model.dao.ILookupTablesDao;
import ITM.maint.fiix_custom_mobile.data.model.entity.MaintenanceType;
import ITM.maint.fiix_custom_mobile.utils.Box;
import ITM.maint.fiix_custom_mobile.utils.Utils;
import io.reactivex.Completable;
import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;

public class DBUpdateWorker extends Worker {

    private static final String TAG = "DBUpdateWorker";
    private ILookupTablesDao lookupTablesDao;
    private FiixDatabase fiixDatabase;

    private static final String RESPONSE_KEY = "DB_Update";

    public DBUpdateWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        fiixDatabase = FiixDatabase.getDatabase(getApplicationContext()));
        lookupTablesDao = fiixDatabase.lookupTablesDao();
        CompositeDisposable compositeDisposable = new CompositeDisposable();

        Type complexType = new TypeToken<Box<MaintenanceType>>(){}.getType();
        String inputData = getInputData().getString(RESPONSE_KEY);

        Box<MaintenanceType> boxData = Utils.deserializeFromJson(inputData, complexType);

        MaintenanceType types = boxData.getBoxContent();

        Completable completable = fiixDatabase.lookupTablesDao().insertTypes(types);
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
    }
}
