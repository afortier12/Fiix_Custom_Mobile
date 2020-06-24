package ITM.maint.fiix_custom_mobile.data.api;

import ITM.maint.fiix_custom_mobile.data.api.requests.FindRequest;
import ITM.maint.fiix_custom_mobile.data.api.responses.FindResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


public interface IPartService {

    //FindRequest, Assets (Parts and Supplies)
    @POST("/api/")
    Call<FindResponse> findParts(@Body FindRequest partRequest);

    @FormUrlEncoded
    @POST("/api/")
    Call<FindResponse> addPart(@Body FindRequest partRequest);

    @FormUrlEncoded
    @POST("/api/")
    Call<FindResponse> changePart(@Body FindRequest partRequest);

    @FormUrlEncoded
    @POST("/api/")
    Call<FindResponse> removePart(@Body FindRequest partRequest);

}
