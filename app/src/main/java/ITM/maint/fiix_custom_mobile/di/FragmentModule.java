package ITM.maint.fiix_custom_mobile.di;

import ITM.maint.fiix_custom_mobile.CodeAnalyzer;
import ITM.maint.fiix_custom_mobile.ui.view.BarcodeFragment;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentModule {

    @ContributesAndroidInjector
    public abstract BarcodeFragment barcodeFragment();

    @ContributesAndroidInjector
    public abstract CodeAnalyzer codeAnalyzer();
}
