package ITM.maint.fiix_custom_mobile.data.model.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

import java.util.List;

import ITM.maint.fiix_custom_mobile.data.model.entity.Action;
import ITM.maint.fiix_custom_mobile.data.model.entity.Cause;
import ITM.maint.fiix_custom_mobile.data.model.entity.FailureCodeNesting;
import ITM.maint.fiix_custom_mobile.data.model.entity.Problem;
import io.reactivex.Completable;

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

}
