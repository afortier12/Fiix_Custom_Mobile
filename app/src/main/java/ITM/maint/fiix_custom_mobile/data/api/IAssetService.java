package ITM.maint.fiix_custom_mobile.data.api;

import java.util.List;

import ITM.maint.fiix_custom_mobile.data.api.requests.FindRequest;
import ITM.maint.fiix_custom_mobile.data.model.entity.Asset;
import ITM.maint.fiix_custom_mobile.data.model.entity.AssetCategory;
import ITM.maint.fiix_custom_mobile.data.model.entity.Part;
import io.reactivex.Completable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


public interface IAssetService {

    //FindRequest, Assets
    @POST("/api/")
    Call<List<Asset>> findAssets(@Body FindRequest assetRequest);

    @POST("/api/")
    Call<List<AssetCategory>> findAssetCategories(@Body FindRequest assetCategoriesRequest);

    @POST("/api/")
    Call<AssetCategory> findAssetCategory(@Body FindRequest assetCategoriesRequest);

    @POST("/api/")
    Call<List<AssetCategory>> getAllAssetCategories(@Body FindRequest assetCategoriesRequest);

}
