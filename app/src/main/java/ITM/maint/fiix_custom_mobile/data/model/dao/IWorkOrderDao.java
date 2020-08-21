package ITM.maint.fiix_custom_mobile.data.model.dao;

import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

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
public abstract class IWorkOrderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Completable insertTask(WorkOrderTask workOrderTask);

    @Delete
    public abstract Completable deleteTask(WorkOrderTask task);

    @Update
    public abstract Completable updateTasks(List<WorkOrderTask> workOrderTasks);

    @Update
    public abstract Completable updateTask(WorkOrderTask workOrderTasks);

    @Transaction
    public void deleteWorkOrdersandTasks(List<Integer> workOrderIds){
        deleteWorkOrders(workOrderIds);
        deleteWorkOrderTasks(workOrderIds);
    }

    @Query("DELETE FROM work_order_table")
    public abstract void deleteAllWorkOrders();

    @Query("DELETE FROM work_order_task_table")
    public abstract void deleteAllWorkOrderTasks();

    @Query("DELETE from work_order_table where id in (:idList) ")
    abstract void deleteWorkOrders(List<Integer> idList);

    @Query("DELETE from work_order_task_table where workOrderId in (:idList) ")
    abstract void deleteWorkOrderTasks(List<Integer> idList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Completable insertTasks(List<WorkOrderTask> workOrderTasks);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Completable insertWorkOrder(WorkOrder workOrder);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Completable insertWorkOrders(List<WorkOrder> workOrders);

    @Update
    public abstract Completable updateWorkOrders(List<WorkOrder> workOrders);

    @Query("SELECT * FROM work_order_task_table where assignedToId = :userId " +
            "and workOrderId = :workOrderId")
    public abstract Single<List<WorkOrderTask>> getWorkOrderTasks(int userId, int workOrderId);

    @Query("SELECT * FROM work_order_table where " +
            "dateCompleted is null")
    public abstract Single<List<WorkOrder>> getWorkOrders();

    @Query("SELECT * FROM work_order_task_table where assignedToId = :userId")
    public abstract Single<List<WorkOrderTask>> getAssignedWorkOrderTasks(int userId);


    @Query("SELECT COALESCE(sum(COALESCE(estimatedHours,0)), 0) as hours FROM work_order_task_table where id = :workOrderId")
    public abstract Single<Double> getWorkOrderEstimatedTime(int workOrderId);

    @Transaction
    @Query("SELECT * FROM work_order_table inner join priority_table " +
            "on priorityId = priority_table.id " +
            "where username = :userName and dateCompleted is null " +
            "GROUP BY priorityId")
    public abstract Single<List<WorkOrderJoinPriority>> getWorkOrdersforUserWithPriorities(String userName);

    @Query("SELECT * FROM priority_table")
    public abstract Single<List<Priority>> getPriorities();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Completable insertWorkOrderTask(WorkOrderTask task);

}
