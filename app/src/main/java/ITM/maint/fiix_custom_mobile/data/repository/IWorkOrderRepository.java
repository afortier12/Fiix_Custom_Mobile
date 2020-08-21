package ITM.maint.fiix_custom_mobile.data.repository;

import java.util.List;

import ITM.maint.fiix_custom_mobile.data.api.requests.FindRequest;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrder;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrderTask;


public interface IWorkOrderRepository {

        public interface IWorkOrderList {
            //get list of work orders from DB
            void getWorkOrdersForUser(String username, int userId);

            //get priorities from DB
            void getPrioritiesFromDB();

            //get list of work orders with priorities for user
            void getWorkOrdersWithPrioritiesFromDb();

            //insert work orders in DB
            void addWorkOrders(List<WorkOrder> workOrderList);

            //delete work orders
            void deleteWorkOrder(int workOrderId);

            //update work orders
            void updateWorkOrders(List<WorkOrder> workOrders);
        }

        public interface IWorkOrderDetail {
            //get list of work order tasks from Fiix
            void getWorkOrderTasks(String username, int userId, int workOrderId);

            //add work order tasks to Fiix
            void addWorkOrderTasks(List<WorkOrderTask> taskList);

            //get list of maintenance types from DB
            void getMaintenanceTypesFromDb();

            //get list of work order statuses from DB
            void getWorkOrderStatusesFromDb();

            //update work order in Fiix
            void updateWorkOrder(WorkOrder workOrder);
        }

        public interface IWorkOrderRCA {

            void getRCACategories();

            void getSources(String categoryName);

            void getSourceProblemsCauses(String sourceName);

            void getActions();
        }

        public interface IWorkOrderTask {

            public void addTaskToDatabase(String description, String estTime, int userId, int workOrderId);

            public void updateTask(WorkOrderTask task);

            public void addTask(WorkOrderTask task);

            public void deleteTask(WorkOrderTask task);

            public void updateTasks(List<WorkOrderTask> taskList);
        }

}
