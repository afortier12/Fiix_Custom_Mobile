package ITM.maint.fiix_custom_mobile.ui.viewmodel;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ITM.maint.fiix_custom_mobile.constants.Asset;
import ITM.maint.fiix_custom_mobile.constants.Assets;
import ITM.maint.fiix_custom_mobile.data.api.PartService;
import ITM.maint.fiix_custom_mobile.data.api.requests.PartRequest;
import ITM.maint.fiix_custom_mobile.data.api.responses.PartResponse;
import ITM.maint.fiix_custom_mobile.data.repository.remote.PartRepository;

public class PartAddViewModel extends AndroidViewModel {

    private PartRepository partRepository;
    private PartService partService;
    private LiveData<PartResponse> partResponseLiveData;
    private static List<String> assetFields;

    public PartAddViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        partRepository = new PartRepository();
        partResponseLiveData = partRepository.getPartResponseMutableLiveData();
    }

    public void findParts() {
        PartRequest.ClientVersion clientVersion = new PartRequest.ClientVersion(
                2, 8, 1);

        List<String> assetFields = new ArrayList<>(Arrays.asList(
                Assets.id.name(),
                Assets.name.name(),
                Assets.description.name()
        ));
        String fields = TextUtils.join(",",assetFields);

        partRepository.findParts(new PartRequest("FindRequest", clientVersion, "Asset", fields));
    }

    public LiveData<PartResponse> getPartResponseLiveData() {
        return partResponseLiveData;
    }


}
