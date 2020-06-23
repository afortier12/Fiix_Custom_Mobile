package ITM.maint.fiix_custom_mobile.data.repository;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import ITM.maint.fiix_custom_mobile.data.api.IPartService;
import ITM.maint.fiix_custom_mobile.data.api.IUserService;
import ITM.maint.fiix_custom_mobile.data.api.IWorkOrderService;
import ITM.maint.fiix_custom_mobile.data.api.requests.FindRequest;
import ITM.maint.fiix_custom_mobile.data.model.FiixDatabase;
import ITM.maint.fiix_custom_mobile.data.model.dao.IPartDao;
import ITM.maint.fiix_custom_mobile.data.model.entity.Part;
import ITM.maint.fiix_custom_mobile.data.model.entity.User;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrder;
import ITM.maint.fiix_custom_mobile.ui.viewmodel.IWorkOrder;

public class WorkOrderRepository extends BaseRepository implements IWorkOrderRepository{

    private static final String TAG ="WorkOrderRepository";
    private IWorkOrderService workOrderService;
    private MutableLiveData<List<WorkOrder>> workOrderResponseMutableLiveData;

    private ITM.maint.fiix_custom_mobile.data.model.dao.IWorkOrderDao IWorkOrderDao;
    private FiixDatabase fiixDatabase;
    private MutableLiveData<WorkOrder> workOrderDBMutableLiveData;


    public WorkOrderRepository(Application application) {
        super(application);
    }

    @Override
    public void findParts(FindRequest partRequest) {

    }

    @Override
    public void addPart(FindRequest partRequest) {

    }

    @Override
    public void changePart(FindRequest partRequest) {

    }

    @Override
    public void removePart(FindRequest partRequest) {

    }

    public void dispose(){
        if (compositeDisposable != null)
            compositeDisposable.dispose();
    }

    public IWorkOrderService getWorkOrderService() {
        return workOrderService;
    }

    public MutableLiveData<List<WorkOrder>> getWorkOrderResponseMutableLiveData() {
        return workOrderResponseMutableLiveData;
    }

    public MutableLiveData<WorkOrder> getWorkOrderDBMutableLiveData() {
        return workOrderDBMutableLiveData;
    }

}
