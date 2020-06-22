package ITM.maint.fiix_custom_mobile.data.repository;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import ITM.maint.fiix_custom_mobile.data.api.IUserService;
import ITM.maint.fiix_custom_mobile.data.model.entity.User;

public class WorkOrderRepository extends BaseRepository{

    private static final String TAG ="WorkOrderRepository";
    private IUserService workOrderService;

    private MutableLiveData<List<User>> userResponseMutableLiveData;

    private ITM.maint.fiix_custom_mobile.data.model.dao.IPartDao IPartDao;

    public WorkOrderRepository(Application application) {
        super(application);
    }
}
