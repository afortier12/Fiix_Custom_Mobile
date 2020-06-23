package ITM.maint.fiix_custom_mobile.data.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import ITM.maint.fiix_custom_mobile.data.model.entity.User;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrder;
import io.reactivex.Completable;

public interface IWorkOrderDao {

    @Query("SELECT * FROM work_order_table where id = :id")
    LiveData<WorkOrder> getWorkOrder(int id);

    @Query("SELECT * FROM work_order_table where assignedUsers LIKE '%:username%'")
    LiveData<List<WorkOrder>> getAssignedWorkOrder(String username);

}
