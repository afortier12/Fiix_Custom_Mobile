package ITM.maint.fiix_custom_mobile.data.model.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import ITM.maint.fiix_custom_mobile.data.model.entity.Action;
import ITM.maint.fiix_custom_mobile.data.model.entity.Cause;
import ITM.maint.fiix_custom_mobile.data.model.entity.FailureCodeNesting;
import ITM.maint.fiix_custom_mobile.data.model.entity.Problem;
import ITM.maint.fiix_custom_mobile.data.model.entity.Source;
import ITM.maint.fiix_custom_mobile.data.model.entity.User;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrder;
import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface IRCADao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Completable insertProblems(List<Problem> problems);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Completable insertCauses(List<Cause> causes);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Completable insertActions(List<Action> causes);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Completable insertFailureCodeNesting(List<FailureCodeNesting> nestings);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Completable insertSourceNesting(List<Source> sources);

    @Query("SELECT DISTINCT name FROM nesting_table ORDER BY name ASC")
    Single<List<String>> getCategories();

    @Transaction
    @Query("SELECT * FROM nesting_table inner join rca_source_table " +
            "on nesting_table.source_id = rca_source_table.id " +
            "where nesting_table.name = :category")
    Single<List<FailureCodeNesting.FailureCodeNestingJoinSource>> getSourcesForCategory(String category);

}
