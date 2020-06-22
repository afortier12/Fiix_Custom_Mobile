package ITM.maint.fiix_custom_mobile.di;

import android.app.Application;
import android.content.Context;
import android.util.Log;


import androidx.room.Room;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Named;
import javax.inject.Singleton;

import ITM.maint.fiix_custom_mobile.BaseApplication;
import ITM.maint.fiix_custom_mobile.constants.Fiix;
import ITM.maint.fiix_custom_mobile.data.model.FiixDatabase;
import ITM.maint.fiix_custom_mobile.data.model.dao.IPartDao;
import ITM.maint.fiix_custom_mobile.data.model.dao.IUserDao;
import ITM.maint.fiix_custom_mobile.di.AppExecutor;
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
     * ITM.maint.fiix_custom_mobile.di.AppExecutor.
     */
    @Provides
    @Singleton
    public static AppExecutor provideAppExecutor() {
        return new AppExecutor(
                Executors.newSingleThreadExecutor(),
                Executors.newFixedThreadPool(10),
                new AppExecutor.PreviewThreadExecutor(),
                new AppExecutor.MainThreadExecutor());
    }

    @Provides
    @Singleton
    @Named("Single Thread")
    public Executor provideSingleThreadExecutor(){
        return Executors.newSingleThreadExecutor();
    }


}