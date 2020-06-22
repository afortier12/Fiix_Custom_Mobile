package ITM.maint.fiix_custom_mobile.di;

import android.content.Context;

import javax.inject.Singleton;

import ITM.maint.fiix_custom_mobile.BaseApplication;
import ITM.maint.fiix_custom_mobile.data.model.FiixDatabase;

import ITM.maint.fiix_custom_mobile.data.repository.BaseRepository;
import dagger.Component;
import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class RepositoryModule {
        private final Context context;

        public RepositoryModule (Context context) {
                this.context = context;
        }

        @Provides //scope is not necessary for parameters stored within the module
        public Context context() {
                return context;
        }

        @Component(modules={RepositoryModule.class})
        @Singleton
        public interface RepositoryComponent {
                Context context();
                AppExecutor appExecutor();

                void inject(BaseRepository baseRepository);

        }

}
