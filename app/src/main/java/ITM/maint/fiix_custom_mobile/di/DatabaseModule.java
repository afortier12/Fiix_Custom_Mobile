package ITM.maint.fiix_custom_mobile.di;

import ITM.maint.fiix_custom_mobile.data.model.FiixDatabase;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class DatabaseModule {

        @ContributesAndroidInjector
        public abstract FiixDatabase fiixDatabase();

}
