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
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ITM.maint.fiix_custom_mobile.constants.Assets;
import ITM.maint.fiix_custom_mobile.data.api.PartService;
import ITM.maint.fiix_custom_mobile.data.api.requests.PartRequest;
import ITM.maint.fiix_custom_mobile.data.model.entity.Part;
import ITM.maint.fiix_custom_mobile.data.model.entity.Storage;
import ITM.maint.fiix_custom_mobile.data.repository.remote.PartRepository;

public class PartAddViewModel extends AndroidViewModel implements IPartAdd {

    private PartRepository partRepository;
    private PartService partService;
    private LiveData<List<Part>> partResponseLiveData;

    public PartAddViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {
        partRepository = new PartRepository();
        partService = partRepository.getPartService();
        partResponseLiveData = partRepository.getPartResponseMutableLiveData();

    }


    public LiveData<List<Part>> getPartResponseLiveData() {
        return partResponseLiveData;
    }

    @Override
    public void findPart(String barcode) {
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

        List list = Stream.of(barcode).collect(Collectors.toList());

        PartRequest.Filter filter = new PartRequest.Filter(
                "strBarcode = ?",
                list
        );

        List<PartRequest.Filter> filters = new ArrayList<>();
        filters.add(filter);

        partRepository.findParts(new PartRequest("FindRequest", clientVersion, "Asset", fields, filters));
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
}
