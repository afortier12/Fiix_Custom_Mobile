package ITM.maint.fiix_custom_mobile.data.api;

import java.util.List;

import ITM.maint.fiix_custom_mobile.data.api.requests.FindRequest;
import ITM.maint.fiix_custom_mobile.data.model.entity.MaintenanceType;
import ITM.maint.fiix_custom_mobile.data.model.entity.Priority;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrder;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrderStatus;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrderTask;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ILookupTableService {

    // Get priority list
    @POST("/api/")
    Call<List<Priority>> getPriorityList(@Body FindRequest priorityRequest);

    // Get maintenance type list
    @POST("/api/")
    Call<List<MaintenanceType>> getMaintenanceTypeList(@Body FindRequest maintenanceTypeRequest);

    // Get work order status list
    @POST("/api/")
    Call<List<WorkOrderStatus>> getWorkOrderStatusList(@Body FindRequest workOrderStatusRequest);

}
