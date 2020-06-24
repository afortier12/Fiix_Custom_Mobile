package ITM.maint.fiix_custom_mobile.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import ITM.maint.fiix_custom_mobile.data.api.IPartService;
import ITM.maint.fiix_custom_mobile.data.model.entity.FiixObject;
import ITM.maint.fiix_custom_mobile.data.model.entity.Part;
import ITM.maint.fiix_custom_mobile.data.model.entity.Storage;
import ITM.maint.fiix_custom_mobile.data.repository.PartRepository;

public class PartAddViewModel extends AndroidViewModel implements IPartAdd {

    private PartRepository partRepository;
    private IPartService partService;
    private MutableLiveData<List<?>> partResponseLiveData;
    private LiveData<Part> partDBLiveData;

    public PartAddViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        partRepository = new PartRepository(this.getApplication());
        partService = partRepository.getPartService();
        partResponseLiveData = partRepository.getPartResponseMutableLiveData();
        partDBLiveData = partRepository.getPartDBMutableLiveData();
    }

    @Override
    public void findPart(String barcode, int fromDB) {
        if (fromDB == 0) {
            partRepository.findPartFromDB(barcode);
        } else {
            partRepository.findPartFromFiix(barcode);
        }
    }

    @Override
    public void findLocation(int partId) {

    }

    @Override
    public void addToStorage(int partId, int qty, Storage storage) {

    }

    @Override
    public void checkStorage(int partId, Storage storage) {

    }

    public void dispose(){
        partRepository.dispose();
    }


    public LiveData<List<?>> getPartResponseLiveData() {
        return partResponseLiveData;
    }

    public LiveData<Part> getPartDBLiveData() {
        return partDBLiveData;
    }

}
