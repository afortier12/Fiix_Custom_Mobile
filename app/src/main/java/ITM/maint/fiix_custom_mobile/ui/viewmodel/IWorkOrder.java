package ITM.maint.fiix_custom_mobile.ui.viewmodel;

import ITM.maint.fiix_custom_mobile.data.api.requests.ChangeRequest;
import ITM.maint.fiix_custom_mobile.data.api.requests.FindRequest;

public interface IWorkOrder {

    public void findWorkOrders(FindRequest.Filter filter);

    public void changeWorkOrderStatus(ChangeRequest changeRequest);

    public void updateTask(ChangeRequest changeRequest);

}
