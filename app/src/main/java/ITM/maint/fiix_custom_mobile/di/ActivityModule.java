package ITM.maint.fiix_custom_mobile.di;


import ITM.maint.fiix_custom_mobile.CodeAnalyzer;
import ITM.maint.fiix_custom_mobile.ui.view.BarcodeFragment;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;


/**
 * Module for injection dependencies into Android Framework clients.
 */
@Module
public abstract class ActivityModule {

    @ContributesAndroidInjector
    public abstract BarcodeFragment barcodeFragment();

    @ContributesAndroidInjector
    public abstract CodeAnalyzer codeAnalyzer();

}