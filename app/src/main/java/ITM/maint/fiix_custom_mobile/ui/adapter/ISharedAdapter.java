package ITM.maint.fiix_custom_mobile.ui.adapter;

import ITM.maint.fiix_custom_mobile.data.model.entity.MaintenanceType;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrder;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrderStatus;

public interface ISharedAdapter {

    public interface OnItemClickListener {

        void onTypeItemClick(MaintenanceType maintenanceType);

        void onStatusItemClick(WorkOrderStatus status);
    }

}
