package ITM.maint.fiix_custom_mobile.di;

import android.content.Context;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class RepositoryExecutor {

    private final Context context;
    public static final String TAG = AppExecutor.class.getSimpleName();
    private final Executor databaseThread;

    @Inject
    public RepositoryExecutor(Context context, Executor databaseThread) {
        this.databaseThread = databaseThread;
        this.context = context;
    }

}
