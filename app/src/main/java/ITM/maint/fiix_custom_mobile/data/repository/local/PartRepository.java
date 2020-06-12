package ITM.maint.fiix_custom_mobile.data.repository.local;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import ITM.maint.fiix_custom_mobile.data.model.FiixDatabase;
import ITM.maint.fiix_custom_mobile.data.model.dao.PartDao;
import ITM.maint.fiix_custom_mobile.data.model.entity.Part;

public class PartRepository {

    private PartDao partDao;
    private LiveData<List<Part>> allParts;

    PartRepository(Application application){
        FiixDatabase db = FiixDatabase.getDatabase(application);
        partDao = db.partDao();
        allParts = partDao.getParts();
    }

    LiveData<List<Part>> getAllParts(){
        return allParts;
    }

    /*
    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    void insert(Word word) {
        WordRoomDatabase.databaseWriteExecutor.execute(() -> {
            mWordDao.insert(word);
        });
    }
     */

}
