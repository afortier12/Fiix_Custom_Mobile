package ITM.maint.fiix_custom_mobile.data.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.Date;
import java.util.List;

import ITM.maint.fiix_custom_mobile.data.model.entity.Part;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface IPartDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Completable insert(Part part);

    @Query("SELECT * FROM part_table WHERE barcode = :barcode AND lastRefresh > :lastRefreshMax LIMIT 1")
    Single<Part> hasPart(String barcode, Date lastRefreshMax);

    @Query("SELECT * from part_table ORDER BY id ASC")
    Single<List<Part>> getParts();

    @Query("DELETE FROM part_table")
    Completable deleteAll();

}
