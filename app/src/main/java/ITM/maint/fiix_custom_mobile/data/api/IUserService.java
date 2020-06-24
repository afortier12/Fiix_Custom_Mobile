package ITM.maint.fiix_custom_mobile.data.api;

import java.util.List;

import ITM.maint.fiix_custom_mobile.data.api.requests.FindRequest;
import ITM.maint.fiix_custom_mobile.data.api.responses.FindResponse;
import ITM.maint.fiix_custom_mobile.data.model.entity.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface IUserService {

    //FindRequest, Assets (Parts and Supplies)
    @POST("/api/")
    Call<List<User>> findUser(@Body FindRequest UserRequest);


}
