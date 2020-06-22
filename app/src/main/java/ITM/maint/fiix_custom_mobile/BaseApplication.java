package ITM.maint.fiix_custom_mobile;

import ITM.maint.fiix_custom_mobile.di.DaggerAppComponent;
import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;

public class BaseApplication extends DaggerApplication  {

    @Override
    public void onCreate() {
        super.onCreate();

    }


    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder().application(this).build();
    }
}