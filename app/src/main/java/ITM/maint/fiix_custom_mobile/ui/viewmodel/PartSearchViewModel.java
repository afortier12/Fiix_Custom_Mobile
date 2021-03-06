package ITM.maint.fiix_custom_mobile.ui.viewmodel;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import ITM.maint.fiix_custom_mobile.data.api.IPartService;

import ITM.maint.fiix_custom_mobile.data.repository.PartRepository;

public class PartSearchViewModel extends AndroidViewModel implements IPartFind{

    private PartRepository partRepository;
    private IPartService partService;
    private MutableLiveData<List<?>> partResponseLiveData;

    public PartSearchViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        partRepository = new PartRepository(this.getApplication());
        partService = partRepository.getPartService();
        partResponseLiveData = partRepository.getPartResponseMutableLiveData();

    }

    @Override
    public void findParts(String category, String type, String make) {

        partRepository.findPartsFromDB(category, type, make);
    }

    public LiveData<List<?>> getPartResponseLiveData() {
        return partResponseLiveData;
    }

}
