package ITM.maint.fiix_custom_mobile.data.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface PartService {

    @POST("/api/")
    Call<ITM.maint.fiix_custom_mobile.data.api.requests.PartRequest> addPart(@Body ITM.maint.fiix_custom_mobile.data.api.requests.PartRequest partAddRequest);

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


    @POST("/api/")
    Call<PartRequest> findParts(
            @Query("id") int id,
            @Query("strName") String name,
            @Query("strMake") String make
    );


}
