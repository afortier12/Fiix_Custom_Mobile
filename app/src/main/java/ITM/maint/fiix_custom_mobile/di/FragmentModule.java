package ITM.maint.fiix_custom_mobile.di;

import ITM.maint.fiix_custom_mobile.firebase.CodeAnalyzer;
import ITM.maint.fiix_custom_mobile.ui.view.BarcodeFragment;
import ITM.maint.fiix_custom_mobile.ui.view.WorkOrderFragment;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentModule {

    @ContributesAndroidInjector
    public abstract BarcodeFragment barcodeFragment();

    @ContributesAndroidInjector
    public abstract CodeAnalyzer codeAnalyzer();

    @ContributesAndroidInjector
    public abstract WorkOrderFragment workOrderFragment();
}
