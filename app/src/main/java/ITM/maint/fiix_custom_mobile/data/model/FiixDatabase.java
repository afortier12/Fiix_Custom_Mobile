package ITM.maint.fiix_custom_mobile.data.model;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import javax.inject.Inject;

import ITM.maint.fiix_custom_mobile.data.model.dao.PartDao;
import ITM.maint.fiix_custom_mobile.data.model.entity.Part;
import ITM.maint.fiix_custom_mobile.di.AppExecutor;

@Database(entities = {Part.class},version = 1)
@TypeConverters({Converters.class})
public abstract class FiixDatabase extends RoomDatabase {

    @Inject
    AppExecutor appExecutor;

    public abstract PartDao partDao();
    private static volatile FiixDatabase INSTANCE;

    private static Callback fiixDatabaseCallback = new Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
        }
    };

    public static FiixDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (FiixDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            FiixDatabase.class, "fiix_database")
                            .addCallback(fiixDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final PartDao partDao;

        PopulateDbAsync(FiixDatabase db) {
            partDao = db.partDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            partDao.deleteAll();
            return null;
        }
    }

}
