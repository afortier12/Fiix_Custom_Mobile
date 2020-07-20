package ITM.maint.fiix_custom_mobile.data.model.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.Date;
import java.util.List;


import ITM.maint.fiix_custom_mobile.data.model.entity.MaintenanceType;
import ITM.maint.fiix_custom_mobile.data.model.entity.Priority;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrderStatus;
import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface ILookupTablesDao {

    //maintenance type
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Completable insertTypes(List<MaintenanceType> maintenanceTypes);

    @Query("SELECT * from maintenance_type_table ORDER BY id ASC")
    Single<List<MaintenanceType>> getMaintenanceType();

    @Query("DELETE FROM maintenance_type_table")
    Completable deleteAllTypes();

    //work order status
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Completable insertStatuses(List<WorkOrderStatus> statuses);

    @Query("SELECT * from status_table ORDER BY id ASC")
    Single<List<WorkOrderStatus>> getWorkOrderStatus();

    @Query("DELETE FROM status_table")
    Completable deleteAllStatuses();


    //work order priority
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Completable insertPriorities(List<Priority> priorities);

    @Query("SELECT * from priority_table ORDER BY id ASC")
    Single<List<Priority>> getPriorities();

    @Query("DELETE FROM priority_table")
    Completable deleteAllPriorities();
}
