package ITM.maint.fiix_custom_mobile.data.repository;

import android.app.Application;

import javax.inject.Inject;

import ITM.maint.fiix_custom_mobile.data.model.FiixDatabase;
import ITM.maint.fiix_custom_mobile.di.DaggerRepositoryModule_RepositoryComponent;
import ITM.maint.fiix_custom_mobile.di.RepositoryExecutor;
import ITM.maint.fiix_custom_mobile.di.RepositoryModule;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;

public class BaseRepository {

    @Inject
    RepositoryExecutor repositoryExecutor;

    private RepositoryModule.RepositoryComponent component;

    protected static final String FIIX_URL = "https://integritytool.macmms.com";
    protected static final String API_key = "macmmsackp3848fbda83ce8bfff2fe692e700e40d392049fcc1c6928619403d94";
    protected static final String Access_key = "macmmsaakp3844fa3e6d75a198199ec20f727518bad4dc4f798531d5427225c";
    protected static final String API_secret = "macmmsaskp38410245f872c82bf62de179360f648957ac37ea162a95cecebbe91e94f8ec45084";
    protected String requestUrl = FIIX_URL +"/api/?action=FindResponse&appKey="+API_key+"&accessKey="+Access_key+"&signatureMethod=HmacSHA256&signatureVersion=1";

    protected FiixDatabase fiixDatabase;
    protected CompositeDisposable compositeDisposable;
    protected DisposableCompletableObserver disposableCompletableObserver;

    public BaseRepository(Application application) {

        fiixDatabase = FiixDatabase.getDatabase(application);
        compositeDisposable = new CompositeDisposable();

        component = DaggerRepositoryModule_RepositoryComponent.builder().build();
        component.inject(this);

    }

    public RepositoryExecutor getRepositoryExecutor() {
        return repositoryExecutor;
    }
}
