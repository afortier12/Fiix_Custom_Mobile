package ITM.maint.fiix_custom_mobile.ui.viewmodel;

import java.util.List;

import ITM.maint.fiix_custom_mobile.data.api.requests.ChangeRequest;
import ITM.maint.fiix_custom_mobile.data.api.requests.FindRequest;

public interface IWorkOrder {

    public void findWorkOrderTasks(String username, int userId, int workOrderId);

    public void findWorkOrders(List<Integer> ids);

    public void getWorkOrderDetails(FindRequest.Filter filter);

    public void changeWorkOrderStatus(ChangeRequest changeRequest);

    public void updateTask(ChangeRequest changeRequest);

}
