package ITM.maint.fiix_custom_mobile.ui.viewmodel;

import java.util.List;

import ITM.maint.fiix_custom_mobile.data.api.requests.ChangeRequest;
import ITM.maint.fiix_custom_mobile.data.api.requests.FindRequest;

public interface IWorkOrder {

    public interface IWorkOrderList {

        public void getWorkOrders(String username, int userId);

        public void findWorkOrderTasks(String username, int userId, int workOrderId);

        public void findWorkOrders(List<Integer> ids);

        public void getWorkOrderDetails(FindRequest.Filter filter);

        public void changeWorkOrderStatus(ChangeRequest changeRequest);

        public void updateTask(ChangeRequest changeRequest);

    }


    public interface IWorkOrderTasks {

        public void getWorkOrderTasks(String username, int userId, int workOrderId);

    }

}
