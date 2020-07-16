package ITM.maint.fiix_custom_mobile.data.repository;

import java.util.List;

import ITM.maint.fiix_custom_mobile.data.api.requests.FindRequest;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrder;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrderTask;

public interface IWorkOrderRepository {

        public interface WorkOrderList {
            //get list of work orders from DB
            void getWorkOrdersForUser(String username, int userId);

            //get priorities from DB
            void getPrioritiesFromDB();

            //get list of work orders with priorities for user
            void getWorkOrdersWithPriorities();

            //get list of work orders from DB/Fiix
            void getWorkOrders(List<Integer> workOrders);

            //insert work orders in DB
            void addWorkOrders(List<WorkOrder> workOrderList);

            //delete work orders
            void deleteWorkOrder(int workOrderId);
        }

        public interface WorkOrderDetail {
            //get list of work order tasks from Fiix
            void getWorkOrderTasks(String username, int userId, int workOrderId);

            //add work order tasks to Fiix
            void addWorkOrderTasks(List<WorkOrderTask> taskList);

            //update work order in Fiix
            void updateWorkOrder(WorkOrder workOrder);
        }

}
