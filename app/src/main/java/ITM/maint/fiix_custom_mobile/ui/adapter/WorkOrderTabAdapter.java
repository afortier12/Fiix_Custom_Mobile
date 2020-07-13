package ITM.maint.fiix_custom_mobile.ui.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import ITM.maint.fiix_custom_mobile.ui.view.WorkOrderDetailFragment;
import ITM.maint.fiix_custom_mobile.ui.view.WorkOrderTaskFragment;

public class WorkOrderTabAdapter extends FragmentStateAdapter {

    private static final int totalTabs = 2;

    private String username;
    private int userId;
    private int workOrderId;




    /*public WorkOrderTabAdapter(@NonNull FragmentManager fragmentManager, String username, int userId, int workOrderId) {
        super(fragmentManager);
         this.username = username;
         this.userId = userId;
         this.workOrderId = workOrderId;

    }*/


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                WorkOrderDetailFragment workOrderDetailFragment = new WorkOrderDetailFragment(username, userId, workOrderId);
                return workOrderDetailFragment;
            case 1:
                WorkOrderTaskFragment workOrderTaskFragment = new WorkOrderTaskFragment(username, userId, workOrderId);
                return workOrderTaskFragment;
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return totalTabs;
    }
}
