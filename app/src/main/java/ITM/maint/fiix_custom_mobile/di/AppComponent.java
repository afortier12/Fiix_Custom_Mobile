package ITM.maint.fiix_custom_mobile.di;

import android.app.Application;

import javax.inject.Singleton;

import ITM.maint.fiix_custom_mobile.BaseApplication;
import ITM.maint.fiix_custom_mobile.data.model.FiixDatabase;
import ITM.maint.fiix_custom_mobile.data.repository.BaseRepository;
import ITM.maint.fiix_custom_mobile.firebase.CodeAnalyzer;
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
                ITM.maint.fiix_custom_mobile.di.AppModule.class,
                ActivityModule.class,
                FragmentModule.class,
        }
)
public interface AppComponent extends AndroidInjector<BaseApplication> {

    void inject(MainActivity mainActivity);
    void inject(CodeAnalyzer codeAnalyzer);
    void inject(BarcodeFragment barcodeFragment);


    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }

}