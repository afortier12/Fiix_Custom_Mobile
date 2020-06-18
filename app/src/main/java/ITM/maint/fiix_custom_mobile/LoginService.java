package ITM.maint.fiix_custom_mobile;

import ITM.maint.fiix_custom_mobile.data.api.requests.PartRequest;
import ITM.maint.fiix_custom_mobile.data.api.responses.PartResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


public interface LoginService {

    //FindRequest, Assets (Parts and Supplies)
    @POST("/login/")
    Call<PartResponse> login(@Body PartRequest partRequest);


}
