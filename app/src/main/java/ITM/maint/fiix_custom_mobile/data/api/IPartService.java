package ITM.maint.fiix_custom_mobile.data.api;

import java.util.List;

import ITM.maint.fiix_custom_mobile.data.api.requests.FindRequest;
import ITM.maint.fiix_custom_mobile.data.api.responses.FindResponse;
import ITM.maint.fiix_custom_mobile.data.model.entity.Part;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


public interface IPartService {

    //FindRequest, Assets (Parts and Supplies)
    @POST("/api/")
    Call<List<Part>> findParts(@Body FindRequest partRequest);

    @FormUrlEncoded
    @POST("/api/")
    Call<Part> addPart(@Body FindRequest partRequest);

    @FormUrlEncoded
    @POST("/api/")
    Call<Part> changePart(@Body FindRequest partRequest);

    @FormUrlEncoded
    @POST("/api/")
    Call<Part> removePart(@Body FindRequest partRequest);

}
