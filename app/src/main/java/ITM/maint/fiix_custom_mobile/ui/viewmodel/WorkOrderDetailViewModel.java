package ITM.maint.fiix_custom_mobile.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import ITM.maint.fiix_custom_mobile.data.repository.WorkOrderRepository;

public class WorkOrderDetailViewModel extends AndroidViewModel implements IWorkOrder.IWorkOrderDetail {


    public WorkOrderDetailViewModel(@NonNull Application application) {
        super(application);
    }

    public void init() {

    }

    @Override
    public void findWorkOrderDetails(String username, int userId, int workOrderId) {

    }
}
