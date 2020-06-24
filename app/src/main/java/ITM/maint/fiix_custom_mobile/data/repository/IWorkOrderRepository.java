package ITM.maint.fiix_custom_mobile.data.repository;

import ITM.maint.fiix_custom_mobile.data.api.requests.FindRequest;

public interface IWorkOrderRepository {

    //get list of work order tasks from Fiix
    public void getWorkOrderTasks(String username, int userId, int workOrderId);

    //get list of work orders from Fiix
    public void getWorkOrders(FindRequest workOrderRequest);

    //get work order details from Fiix
    public void getWorkOrderDetails(FindRequest partRequest);


}
