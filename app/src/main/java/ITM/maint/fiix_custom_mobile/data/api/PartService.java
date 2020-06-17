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
    Call<PartResponse> addPart(@Body PartRequest partRequest);

    @FormUrlEncoded
    @POST("/api/")
    Call<PartResponse> changePart(@Body PartRequest partRequest);

    @FormUrlEncoded
    @POST("/api/")
    Call<PartResponse> removePart(@Body PartRequest partRequest);

}
