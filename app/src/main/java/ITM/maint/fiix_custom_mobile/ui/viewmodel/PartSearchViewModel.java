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
import ITM.maint.fiix_custom_mobile.data.api.IPartService;
import ITM.maint.fiix_custom_mobile.data.api.requests.FindRequest;
import ITM.maint.fiix_custom_mobile.data.model.entity.Part;
import ITM.maint.fiix_custom_mobile.data.repository.remote.PartRepository;

public class PartSearchViewModel extends AndroidViewModel {

    private PartRepository partRepository;
    private IPartService partService;
    private LiveData<List<Part>> partResponseLiveData;

    public PartSearchViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        partRepository = new PartRepository();
        partService = partRepository.getPartService();
        partResponseLiveData = partRepository.getPartResponseMutableLiveData();

    }

    public void findParts(FindRequest.Filter filter) {

        FindRequest.ClientVersion clientVersion = new FindRequest.ClientVersion(
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

        FindRequest.Filter filterP = new FindRequest.Filter(
                "id = ?",
                list
        );

        List<FindRequest.Filter> filters = new ArrayList<>();
        filters.add(filterP);

        partRepository.findParts(new FindRequest("FindRequest", clientVersion, "Asset", fields, filters));
    }

    public LiveData<List<Part>> getPartResponseLiveData() {
        return partResponseLiveData;
    }

}
