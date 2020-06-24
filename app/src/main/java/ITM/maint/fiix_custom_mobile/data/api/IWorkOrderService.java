package ITM.maint.fiix_custom_mobile.data.api;

import ITM.maint.fiix_custom_mobile.data.api.requests.FindRequest;
import ITM.maint.fiix_custom_mobile.data.api.responses.FindResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface IWorkOrderService {

    //Find assigned work orders
    @POST("/api/")
    Call<FindResponse> findUser(@Body FindRequest WorkOrderRequest);
}
