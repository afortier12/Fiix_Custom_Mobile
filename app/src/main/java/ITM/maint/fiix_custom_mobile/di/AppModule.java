package ITM.maint.barcodescan.di;

import android.app.Application;
import android.content.Context;


import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Named;
import javax.inject.Singleton;

import ITM.maint.fiix_custom_mobile.AppExecutor;
import dagger.Module;
import dagger.Provides;


/**
 * Application level module.
 */
@Module
public class AppModule {

    /**
     * Application context.
     */
    @Provides
    @Singleton
    public static Context getApplicationContext(Application application) {
        return application.getApplicationContext();
    }

    /**
     * ITM.maint.fiix_custom_mobile.AppExecutor.
     */
    @Provides
    @Singleton
    public static AppExecutor provideAppExecutor() {
        return new AppExecutor(
                Executors.newSingleThreadExecutor(),
                Executors.newSingleThreadExecutor(),
                new AppExecutor.AnimationThreadExecutor(),
                new AppExecutor.MainThreadExecutor());
    }

    @Provides
    @Singleton
    @Named("Single Thread")
    public Executor provideSingleThreadExecutor(){
        return Executors.newSingleThreadExecutor();
    }

}