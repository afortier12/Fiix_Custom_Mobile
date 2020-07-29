package ITM.maint.fiix_custom_mobile.data.repository;

import java.util.List;

import ITM.maint.fiix_custom_mobile.data.api.requests.FindRequest;
import ITM.maint.fiix_custom_mobile.data.model.entity.Part;

public interface IAssetRepository {

    //get specific group/single asset
    public void findAsset(List<Integer> assetIds);

    //retrieve asset categories
    public void findAssetCategory(List<Integer> assetCategoryIds);


}
