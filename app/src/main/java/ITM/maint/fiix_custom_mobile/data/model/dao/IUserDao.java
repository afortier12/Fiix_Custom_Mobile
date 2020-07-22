package ITM.maint.fiix_custom_mobile.data.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import ITM.maint.fiix_custom_mobile.data.model.entity.User;
import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface IUserDao {

    @Query("SELECT * FROM user_table where username = :username")
    Single<User> getUser(String username);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Completable insert(User user);
}
