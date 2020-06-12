package ITM.maint.fiix_custom_mobile.data.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import ITM.maint.fiix_custom_mobile.data.api.PartRequest;

@Dao
public interface PartDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(PartRequest part);

    @Query("SELECT * from PartRequest ORDER BY id ASC")
    LiveData<List<PartRequest>> getParts();

    @Query("DELETE FROM PartRequest")
    void deleteAll();

}
