package ITM.maint.fiix_custom_mobile.data.api;

import java.util.Map;

import ITM.maint.fiix_custom_mobile.data.api.requests.PartAddRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


public interface PartService {

    @POST("/api/")
    Call<PartAddRequest> addPart(@Body PartAddRequest partAddRequest);

    @FormUrlEncoded
    @POST("/api/")
    Call<PartAddRequest> addPart(
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

    @FormUrlEncoded
    @POST("/api/")
    Call<PartAddRequest> addPart(@FieldMap Map<String, String> fields);


}
