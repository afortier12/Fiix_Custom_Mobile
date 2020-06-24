package ITM.maint.fiix_custom_mobile.data.repository;

import java.util.List;

import ITM.maint.fiix_custom_mobile.data.api.requests.FindRequest;

public interface IWorkOrderRepository {

    //get list of work order tasks from Fiix
    public void getWorkOrderTasks(String username, int userId, int workOrderId);


    //get list of work orders from Fiix
    public void getWorkOrders(List<Integer> workOrders);

    //get work order details from Fiix
    public void getWorkOrderDetails(FindRequest partRequest);


}
