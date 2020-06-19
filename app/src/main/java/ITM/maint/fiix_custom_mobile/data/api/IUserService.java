package ITM.maint.fiix_custom_mobile.data.api;

import ITM.maint.fiix_custom_mobile.data.api.requests.FindRequest;
import ITM.maint.fiix_custom_mobile.data.api.responses.PartFindResponse;
import ITM.maint.fiix_custom_mobile.data.api.responses.UserFindResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface IUserService {

    //FindRequest, Assets (Parts and Supplies)
    @POST("/api/")
    Call<UserFindResponse> findUser(@Body FindRequest UserRequest);


}
