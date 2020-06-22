package ITM.maint.fiix_custom_mobile.di;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AppExecutor  {

    public static final String TAG = AppExecutor.class.getSimpleName();
    private final Executor analyzerThread;
    private final Executor databaseThread;
    private final Executor previewThread;
    private final Executor mainThread;

    public AppExecutor(Executor analyzerThread,
                       Executor databaseThread,
                       Executor previewThread,
                       Executor mainThread) {
        this.analyzerThread = analyzerThread;
        this.databaseThread = databaseThread;
        this.previewThread = previewThread;
        this.mainThread = mainThread;
    }

    @Inject
    public AppExecutor() {
        this(Executors.newSingleThreadExecutor(), Executors.newFixedThreadPool(10), new PreviewThreadExecutor(),
                new MainThreadExecutor());
    }



    public Executor analyzerThread() {
        return analyzerThread;
    }

    public Executor databaseThread() {
        return databaseThread;
    }

    public Executor previewThread() { return previewThread; }

    public Executor mainThread() {
        return mainThread;
    }

    public static class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }

    public static class PreviewThreadExecutor implements Executor {
        private Handler previewThread = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            previewThread.post(command);
        }
    }


}
