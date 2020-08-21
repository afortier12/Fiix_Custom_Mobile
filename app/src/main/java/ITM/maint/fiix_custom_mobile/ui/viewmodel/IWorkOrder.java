package ITM.maint.fiix_custom_mobile.ui.viewmodel;

import java.util.List;

import ITM.maint.fiix_custom_mobile.data.api.requests.ChangeRequest;
import ITM.maint.fiix_custom_mobile.data.api.requests.FindRequest;
import ITM.maint.fiix_custom_mobile.data.model.entity.Asset;
import ITM.maint.fiix_custom_mobile.data.model.entity.Asset.AssetDepartmentPlant;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrder;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrderTask;

public interface IWorkOrder {

    public void addTask(WorkOrderTask task);

    public interface IWorkOrderList {

        public void getWorkOrders(String username, int userId);

        public void findWorkOrderTasks(String username, int userId, int workOrderId);

        public void getWorkOrderDetails(FindRequest.Filter filter);

        public void changeWorkOrderStatus(ChangeRequest changeRequest);

        public void updateTask(ChangeRequest changeRequest);

        public void getDepartmentsPlants(List<Integer> assetIds);

        public List<WorkOrder> updateWorkOrders(List<WorkOrder> workOrders, List<AssetDepartmentPlant> departmentPlants);

    }


    public interface IWorkOrderTasks {

        public void getWorkOrderTasks(String username, int userId, int workOrderId);

        public void updateWorkOrderTasks(List<WorkOrderTask> taskList);

        public void updateWorkOrderTask(WorkOrderTask task);

        public void addTask(WorkOrderTask task);

        public void deleteTask(WorkOrderTask task);

        public void addTaskToDB(String description, String estTime, int userId, int workOrderId);
    }

    public interface IWorkOrderDetails {

        public void getWorkOrderEstTime(int workOrderId, int userId);

        public void getMaintenanceTypeDetails();

        public void getWorkOrderStatusDetails();
    }

    public interface IWorkOrderRCA{

        public void getAssets(WorkOrder workOrder);

        public void getAssetCategory(int assetCategoryId);

        public void getCategoryList();

        public void getSourceList(String categoryName);

        public void getProblemsCausesList(String sourceName);

        public void getActionList();

    }

}
