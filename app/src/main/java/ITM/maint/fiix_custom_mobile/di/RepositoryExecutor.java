package ITM.maint.fiix_custom_mobile.di;

import android.content.Context;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Inject;
import javax.inject.Singleton;

import ITM.maint.fiix_custom_mobile.BaseApplication;

@Singleton
public class RepositoryExecutor {

    public static final String TAG = AppExecutor.class.getSimpleName();
    private final Executor databaseThread;

    public RepositoryExecutor(Executor databaseThread) {
        this.databaseThread = databaseThread;
    }

    @Inject
    public RepositoryExecutor(){
        this(Executors.newFixedThreadPool(10));
    }

    public Executor databaseThread() {
        return databaseThread;
    }

}
