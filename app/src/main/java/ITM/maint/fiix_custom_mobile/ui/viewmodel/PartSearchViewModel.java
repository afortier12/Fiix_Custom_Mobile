package ITM.maint.fiix_custom_mobile.ui.viewmodel;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ITM.maint.fiix_custom_mobile.constants.Assets;
import ITM.maint.fiix_custom_mobile.data.api.PartService;
import ITM.maint.fiix_custom_mobile.data.api.requests.PartRequest;
import ITM.maint.fiix_custom_mobile.data.model.entity.Part;
import ITM.maint.fiix_custom_mobile.data.repository.remote.PartRepository;

public class PartSearchViewModel extends AndroidViewModel {

    private PartRepository partRepository;
    private PartService partService;
    private LiveData<List<Part>> partResponseLiveData;

    public PartSearchViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        partRepository = new PartRepository();
        partService = partRepository.getPartService();
        partResponseLiveData = partRepository.getPartResponseMutableLiveData();

    }

    public void findParts(PartRequest.Filter filter) {

        PartRequest.ClientVersion clientVersion = new PartRequest.ClientVersion(
                2, 8, 1);

        List<String> assetFields = new ArrayList<>(Arrays.asList(
                Assets.id.getField(),
                Assets.name.getField(),
                Assets.description.getField(),
                Assets.make.getField(),
                Assets.model.getField(),
                Assets.barcode.getField(),
                Assets.partNumber.getField()
        ));
        String fields = TextUtils.join(",",assetFields);

       List list = Stream.of(13455594).collect(Collectors.toList());

        PartRequest.Filter filterP = new PartRequest.Filter(
                "id = ?",
                list
        );

        List<PartRequest.Filter> filters = new ArrayList<>();
        filters.add(filterP);

        partRepository.findParts(new PartRequest("FindRequest", clientVersion, "Asset", fields, filters));
    }

    public LiveData<List<Part>> getPartResponseLiveData() {
        return partResponseLiveData;
    }

}
