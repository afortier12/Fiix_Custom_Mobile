package ITM.maint.fiix_custom_mobile.di;

import android.content.Context;

import java.util.concurrent.Executors;

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

        @Provides
        @Singleton
        public static RepositoryExecutor provideRepositoryExecutor() {
                return new RepositoryExecutor(Executors.newFixedThreadPool(10));
        }


        @Component(modules={RepositoryModule.class})
        @Singleton
        public interface RepositoryComponent {
                AppExecutor appExecutor();

                void inject(BaseRepository baseRepository);

        }

}
