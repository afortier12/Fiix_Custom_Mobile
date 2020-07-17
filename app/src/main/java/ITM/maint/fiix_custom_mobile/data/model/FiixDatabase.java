package ITM.maint.fiix_custom_mobile.data.model;

import android.content.Context;
import android.net.CaptivePortal;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import ITM.maint.fiix_custom_mobile.constants.Fiix;
import ITM.maint.fiix_custom_mobile.data.model.dao.ILookupTablesDao;
import ITM.maint.fiix_custom_mobile.data.model.dao.IPartDao;
import ITM.maint.fiix_custom_mobile.data.model.dao.IRCADao;
import ITM.maint.fiix_custom_mobile.data.model.dao.IUserDao;
import ITM.maint.fiix_custom_mobile.data.model.dao.IWorkOrderDao;
import ITM.maint.fiix_custom_mobile.data.model.entity.Part;
import ITM.maint.fiix_custom_mobile.data.model.entity.Priority;
import ITM.maint.fiix_custom_mobile.data.model.entity.User;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrder;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrderTask;
import ITM.maint.fiix_custom_mobile.di.AppExecutor;

@Database(entities = {Part.class, User.class, WorkOrder.class, WorkOrderTask.class,
        Priority.class},version = 1)
@TypeConverters({Converters.class})
public abstract class FiixDatabase extends RoomDatabase {

    public abstract IPartDao partDao();
    public abstract IUserDao userDao();
    public abstract IWorkOrderDao workOrderDao();
    public abstract ILookupTablesDao lookupTablesDao();
    public abstract IRCADao rcaDao();

    // marking the instance as volatile to ensure atomic access to the variable
    private static volatile FiixDatabase INSTANCE;

    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseOpenExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static FiixDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (FiixDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            FiixDatabase.class, "Fiix_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }


    /**
     * Override the onOpen method to populate the database.
     * For this sample, we clear the database every time it is created or opened.
     *
     * If you want to populate the database only when the database is created for the 1st time,
     * override RoomDatabase.Callback()#onCreate
     */
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);

            // If you want to keep data through app restarts,
            // comment out the following block
            databaseOpenExecutor.execute(() -> {
                // Populate the database in the background.
                // If you want to start with more words, just add them.
                /*ILookupTablesDao lookupTablesDao= INSTANCE.lookupTablesDao();
                lookupTablesDao.deleteAll();

                List<Priority> priorities = new ArrayList<>();
                priorities.add(new Priority(2665,"High", 1));
                priorities.add(new Priority(2666,"High", 2));
                priorities.add(new Priority(2667,"Medium", 3));
                priorities.add(new Priority(2668,"Low", 4));
                priorities.add(new Priority(2669,"Low",5 ));

                lookupTablesDao.insert(priorities);*/

            });
        }
    };


}
