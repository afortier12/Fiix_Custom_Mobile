package ITM.maint.fiix_custom_mobile.ui.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import ITM.maint.fiix_custom_mobile.R;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrder;
import ITM.maint.fiix_custom_mobile.ui.adapter.WorkOrderViewPagerAdapter;
import ITM.maint.fiix_custom_mobile.ui.viewmodel.SharedViewModel;

public class WorkOrderFragment extends Fragment {

    private static final String TAG = "WorkOrderFragment";
    private WorkOrderViewPagerAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    private String username;
    private int userId;
    private WorkOrder workOrder;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_work_order, container, false);

        SavedStateViewModelFactory factory = new SavedStateViewModelFactory(getActivity().getApplication(), this);
        SharedViewModel sharedViewModel = new ViewModelProvider(requireActivity(), factory).get(SharedViewModel.class);

        String usernameArg = getArguments().getString("User");
        if (usernameArg != null) {
            sharedViewModel.setUsername(usernameArg);
            sharedViewModel.setUserId(getArguments().getInt("id"));
        } else if (savedInstanceState != null){
            sharedViewModel.setUsername(savedInstanceState.getString("USERNAME_KEY"));
            sharedViewModel.setUserId(savedInstanceState.getInt("USERID_KEY"));
        }

        username = sharedViewModel.getUsername().getValue();
        userId = sharedViewModel.getUserId().getValue();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        WorkOrderFragmentArgs args = WorkOrderFragmentArgs.fromBundle(getArguments());
        workOrder = args.getWorkOrder();

        adapter = new WorkOrderViewPagerAdapter(getChildFragmentManager(), getLifecycle(), username, userId, workOrder);

        viewPager = (ViewPager2) view.findViewById(R.id.fragment_work_order_viewpager);
        viewPager.setAdapter(adapter);

        tabLayout = (TabLayout) view.findViewById(R.id.fragment_work_order_tab_layout);
        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                if (position == 0)
                    tab.setText(R.string.detail);
                else
                    tab.setText(R.string.tasks);
            }
        }).attach();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });



    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
