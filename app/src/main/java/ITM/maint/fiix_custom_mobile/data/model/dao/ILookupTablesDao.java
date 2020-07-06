package ITM.maint.fiix_custom_mobile.data.model.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.Date;
import java.util.List;


import ITM.maint.fiix_custom_mobile.data.model.entity.Priority;
import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface ILookupTablesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Completable insert(List<Priority> priority);

    @Query("SELECT * from priority_table ORDER BY id ASC")
    Single<List<Priority>> getPriorities();

    @Query("DELETE FROM priority_table")
    Completable deleteAll();
}
