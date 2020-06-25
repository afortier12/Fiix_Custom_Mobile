package ITM.maint.fiix_custom_mobile.data.api;

import org.json.JSONObject;

import java.util.List;


import ITM.maint.fiix_custom_mobile.data.api.requests.FindRequest;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrder;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrderTask;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface IWorkOrderService {

    // Get work order tasks that are assigned to the current user and work order id(optional)
    @POST("/api/")
    Call<List<WorkOrderTask>> getWorkOrderTasks(@Body FindRequest workOrderTaskRequest);

    // Get work order list by ids
    @POST("/api/")
    Call<List<WorkOrder>> getWorkOrderList(@Body FindRequest workOrderListRequest);

    // Get work order details by id
    @POST("/api/")
    Call<WorkOrder> getWorkOrderDetail(@Body FindRequest workOrderDetailRequest);

}
