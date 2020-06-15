package ITM.maint.fiix_custom_mobile.data.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import ITM.maint.fiix_custom_mobile.data.api.requests.PartRequest;
import ITM.maint.fiix_custom_mobile.data.model.entity.Part;

@Dao
public interface PartDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Part part);

    @Query("SELECT * from part_table ORDER BY id ASC")
    LiveData<List<Part>> getParts();

    @Query("DELETE FROM part_table")
    void deleteAll();

}
