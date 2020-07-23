package ITM.maint.fiix_custom_mobile.ui.view;

import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import ITM.maint.fiix_custom_mobile.R;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrder;
import ITM.maint.fiix_custom_mobile.ui.adapter.WorkOrderViewPagerAdapter;
import ITM.maint.fiix_custom_mobile.ui.viewmodel.SharedViewModel;
import ITM.maint.fiix_custom_mobile.utils.Utils;

public class WorkOrderFragment extends Fragment {

    private static final String TAG = "WorkOrderFragment";
    private WorkOrderViewPagerAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private FloatingActionButton detailFAB;
    private FloatingActionButton noteFAB;
    private FloatingActionButton rcaFAB;
    private FloatingActionButton updateFAB;
    private ConstraintLayout subFABContainer;

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


        detailFAB = view.findViewById(R.id.work_order_FAB);
        subFABContainer = view.findViewById(R.id.work_order_sub_FAB_container);
        detailFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (subFABContainer.getVisibility() == View.INVISIBLE)
                    subFABContainer.setVisibility(View.VISIBLE);
                else
                    subFABContainer.setVisibility(View.INVISIBLE);
            }
        });

        noteFAB = view.findViewById(R.id.work_order_FAB_note);
        noteFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "note clicked");
            }
        });

        rcaFAB = view.findViewById(R.id.work_order_FAB_RCA);
        rcaFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "RCA clicked");
            }
        });

        updateFAB = view.findViewById(R.id.work_order_FAB_Send_Fiix);
        updateFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "update clicked");
            }
        });

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
                if (tab.getPosition() == 0){
                    detailFAB.setVisibility(View.VISIBLE);
                } else {
                    detailFAB.setVisibility(View.INVISIBLE);
                }
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
