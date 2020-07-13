package ITM.maint.fiix_custom_mobile.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class WorkOrderTaskViewModel extends AndroidViewModel implements IWorkOrder.IWorkOrderTasks{

    public WorkOrderTaskViewModel(@NonNull Application application) {
        super(application);
    }

    public void init(){

    }

    @Override
    public void findWorkOrderDetails(String username, int userId, int workOrderId) {

    }
}
