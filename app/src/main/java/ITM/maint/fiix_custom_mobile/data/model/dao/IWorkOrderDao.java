package ITM.maint.fiix_custom_mobile.data.model.dao;

import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ITM.maint.fiix_custom_mobile.constants.WorkOrders;
import ITM.maint.fiix_custom_mobile.data.model.entity.Part;
import ITM.maint.fiix_custom_mobile.data.model.entity.Priority;
import ITM.maint.fiix_custom_mobile.data.model.entity.User;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrder;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrder.WorkOrderJoinPriority;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrderTask;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public interface IWorkOrderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertTask(WorkOrderTask workOrderTask);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertTasks(List<WorkOrderTask> workOrderTasks);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertWorkOrder(WorkOrder workOrder);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertWorkOrders(List<WorkOrder> workOrders);

    @Query("SELECT * FROM work_order_task_table where assignedToId = :userId " +
            "and workOrderId = :workOrderId")
    Single<List<WorkOrderTask>> getWorkOrderTasks(int userId, int workOrderId);

    @Query("SELECT * FROM work_order_table where " +
            "username = :userName and dateCompleted is null")
    Single<List<WorkOrder>> getWorkOrdersforUser(String userName);

    @Query("SELECT * FROM work_order_table where assignedUsers LIKE :username")
    Single<List<WorkOrder>> getAssignedWorkOrder(String username);


    @Query("SELECT COALESCE(sum(COALESCE(estimatedHours,0)), 0) as hours FROM work_order_task_table where id = :workOrderId")
    Single<Double> getWorkOrderEstimatedTime(int workOrderId);

    @Transaction
    @Query("SELECT * FROM work_order_table inner join priority_table " +
            "on priorityId = priority_table.id " +
            "where username = :userName and dateCompleted is null " +
            "GROUP BY priorityId")
    Single<List<WorkOrderJoinPriority>> getWorkOrdersforUserWithPriorities(String userName);

    @Query("SELECT * FROM priority_table")
    Single<List<Priority>> getPriorities();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertWorkOrderTask(WorkOrderTask task);

}
