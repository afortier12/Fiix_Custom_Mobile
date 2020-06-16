package ITM.maint.fiix_custom_mobile.ui.viewmodel;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ITM.maint.fiix_custom_mobile.constants.Assets;
import ITM.maint.fiix_custom_mobile.data.api.PartService;
import ITM.maint.fiix_custom_mobile.data.api.requests.PartRequest;
import ITM.maint.fiix_custom_mobile.data.api.responses.PartResponse;
import ITM.maint.fiix_custom_mobile.data.model.entity.Part;
import ITM.maint.fiix_custom_mobile.data.repository.remote.PartRepository;

public class PartAddViewModel extends AndroidViewModel {

    private PartRepository partRepository;
    private PartService partService;
    private LiveData<List<Part>> partResponseLiveData;
    private MutableLiveData<Integer> httpHeaderStatus;
    private MutableLiveData<Integer> httpQueryStatus;
    private static List<String> assetFields;



    public PartAddViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        partRepository = new PartRepository();
        partService = partRepository.getPartService();
        partResponseLiveData = partRepository.getPartResponseMutableLiveData();

        //httpHeaderStatus = partRepository.getHttpHeaderStatus();
        //httpQueryStatus = partRepository.getHttpQueryStatus();
    }

    public void findParts() {

        PartRequest.ClientVersion clientVersion = new PartRequest.ClientVersion(
                2, 8, 1);

        List<String> assetFields = new ArrayList<>(Arrays.asList(
                Assets.id.getField(),
                Assets.name.getField(),
                Assets.description.getField()
        ));
        String fields = TextUtils.join(",",assetFields);

        partRepository.findParts(new PartRequest("FindRequest", clientVersion, "Asset", fields));
    }

    public LiveData<List<Part>> getPartResponseLiveData() {
        return partResponseLiveData;
    }

    public MutableLiveData<Integer> getHttpQueryStatus() {
        return httpQueryStatus;
    }

    public MutableLiveData<Integer> getHttpHeaderStatus() {
        return httpHeaderStatus;
    }
}
