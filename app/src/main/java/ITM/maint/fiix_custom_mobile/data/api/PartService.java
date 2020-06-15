package ITM.maint.fiix_custom_mobile.data.api;

import ITM.maint.fiix_custom_mobile.data.api.requests.PartRequest;
import ITM.maint.fiix_custom_mobile.data.api.responses.PartResponse;
import ITM.maint.fiix_custom_mobile.data.model.entity.Part;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface PartService {

    //FindRequest, Assets (Parts and Supplies)
    @POST("/api/")
    Call<PartResponse> findParts(@Body PartRequest partRequest);

    @FormUrlEncoded
    @POST("/api/")
    Call<ITM.maint.fiix_custom_mobile.data.api.requests.PartRequest> addPart(
            @Field("id") int id,
            @Field("strName") String name,
            @Field("strDescription") String description,
            @Field("strMake") String make,
            @Field("strModel") String model,
            @Field("strCode") String partNumber,
            @Field("strAisle") String tray,
            @Field("strRow") String row,
            @Field("strBin") String bin,
            @Field("strBarcode") String barcode,
            @Field("intCategoryID") int categoryID,
            @Field("intSiteID") int siteID
    );

}
