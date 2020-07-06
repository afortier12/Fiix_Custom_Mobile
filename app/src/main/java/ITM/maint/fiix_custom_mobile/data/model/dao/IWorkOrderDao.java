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

    //@Query("SELECT id, workOrderId FROM work_order_task_table where assignedToId = :userId and completedDate is null")
    //LiveData<List<WorkOrderTask>> getWorkOrderTasksForUser(int userId, int workOrderId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertTask(WorkOrderTask workOrderTask);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertWorkOrder(WorkOrder workOrder);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertWorkOrders(List<WorkOrder> workOrder);

    @Query("SELECT * FROM work_order_task_table where assignedToId = :userId " +
            "and workOrderId = :workOrderId and completedDate is null")
    Single<List<WorkOrderTask>> getWorkOrderTasks(int userId, int workOrderId);

    @Query("SELECT * FROM work_order_table where " +
            "username = :userName and dateCompleted is null")
    Single<List<WorkOrder>> getWorkOrdersforUser(String userName);

    @Query("SELECT * FROM work_order_table where assignedUsers LIKE :username")
    Single<List<WorkOrder>> getAssignedWorkOrder(String username);



    @Transaction
    @Query("SELECT * FROM work_order_table inner join priority_table " +
            "on priorityId = priority_table.id " +
            "where username = :userName and dateCompleted is null " +
            "GROUP BY priorityId")
    Single<List<WorkOrderJoinPriority>> getWorkOrdersforUserWithPriorities(String userName);

    @Query("SELECT * FROM priority_table")
    Single<List<Priority>> getPriorities();


}
