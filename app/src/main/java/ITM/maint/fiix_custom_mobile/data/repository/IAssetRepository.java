package ITM.maint.fiix_custom_mobile.data.repository;

import java.util.List;

import ITM.maint.fiix_custom_mobile.data.api.requests.FindRequest;
import ITM.maint.fiix_custom_mobile.data.model.entity.Part;

public interface IAssetRepository {

    //get specific group/single asset
    public void findAsset(List<Integer> assetIds);

    //get list of assets by type
    public void findAssetsByType(int assetType);

    //retrieve asset category by Id
    public void findAssetCategory(int categoryId);

    //get asset all asset categories
    public void getAllAssetCategories();

    //get department and plant for work order assets
    public void getAssetDepartmentPlant(List<Integer> assetIds);
}
