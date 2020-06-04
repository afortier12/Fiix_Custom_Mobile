package ITM.maint.fiix_custom_mobile.di;

import android.app.Application;

import javax.inject.Singleton;

import ITM.maint.fiix_custom_mobile.BaseApplication;
import ITM.maint.fiix_custom_mobile.CodeAnalyzer;
import ITM.maint.fiix_custom_mobile.MainActivity;
import ITM.maint.fiix_custom_mobile.ui.view.BarcodeFragment;
import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

/**
 * Application level component.
 */
@Singleton
@Component(
        modules = {
                AndroidSupportInjectionModule.class,
                ITM.maint.barcodescan.di.AppModule.class,
                ActivityModule.class
        }
)
public interface AppComponent extends AndroidInjector<BaseApplication> {

    void inject(BarcodeFragment barcodeFragment);
    void inject(CodeAnalyzer codeAnalyzer);


    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }

}