package ITM.maint.fiix_custom_mobile.data.model.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import ITM.maint.fiix_custom_mobile.data.model.entity.Action;
import ITM.maint.fiix_custom_mobile.data.model.entity.Cause;
import ITM.maint.fiix_custom_mobile.data.model.entity.RCACategorySource;
import ITM.maint.fiix_custom_mobile.data.model.entity.RCACategorySource.SourceJoinProblemCause;
import ITM.maint.fiix_custom_mobile.data.model.entity.RCACategorySource.CategoryJoinSource;
import ITM.maint.fiix_custom_mobile.data.model.entity.Problem;
import ITM.maint.fiix_custom_mobile.data.model.entity.Source;
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
    Completable insertFailureCodeNesting(List<RCACategorySource> nestings);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Completable insertSourceNesting(List<Source> sources);

    @Query("SELECT DISTINCT name FROM nesting_table ORDER BY name ASC")
    Single<List<String>> getCategories();

    @Transaction
    @Query("SELECT * FROM nesting_table inner join rca_source_table " +
            "on nesting_table.source_id = rca_source_table.id " +
            "where nesting_table.name = :category")
    Single<List<CategoryJoinSource>> getSourcesForCategory(String category);

    @Transaction
    @Query("SELECT * FROM rca_source_table inner join problem_table " +
            "on rca_source_table.problem_id = problem_table.id " +
            "inner join cause_table " +
            "on rca_source_table.cause_id = cause_table.id " +
            "where rca_source_table.name = :source")
    Single<List<SourceJoinProblemCause>> getProblemsCausesForSource(String source);

    @Query("SELECT * FROM action_table where id !=0")
    Single<List<Action>> getActions();
}
