package ITM.maint.fiix_custom_mobile.ui.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

import ITM.maint.fiix_custom_mobile.ui.view.WorkOrderDetailFragment;
import ITM.maint.fiix_custom_mobile.ui.view.WorkOrderTaskFragment;

public class WorkOrderViewPagerAdapter extends FragmentStateAdapter {

    private static final int totalTabs = 2;

    private List<Fragment> fragmentList  = new ArrayList<>();

    private String username;
    private int userId;
    private int workOrderId;

    public WorkOrderViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, String username, int userId, int workOrderId) {
        super(fragmentManager, lifecycle);
         this.username = username;
         this.userId = userId;
         this.workOrderId = workOrderId;

         fragmentList.add(new WorkOrderDetailFragment(username, userId, workOrderId));
         fragmentList.add(new WorkOrderTaskFragment(username, userId, workOrderId));
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
       return fragmentList.get(position);
    }

    @Override
    public int getItemCount() {
        return totalTabs;
    }
}
