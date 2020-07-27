package ITM.maint.fiix_custom_mobile.ui.view;

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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import ITM.maint.fiix_custom_mobile.R;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrder;
import ITM.maint.fiix_custom_mobile.ui.adapter.WorkOrderViewPagerAdapter;
import ITM.maint.fiix_custom_mobile.ui.viewmodel.SharedViewModel;

public class WorkOrderFragment extends Fragment implements WorkOrderRCADialog.OnRCAListener {

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

    private static final int DETAIL_TAB_POSITION = 0;
    private static final int TASK_TAB_POSITION = 1;
    private int visibleTab;

    private static final int RCA_FRAGMENT_REQUEST_CODE = 22;




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
                    if (visibleTab == DETAIL_TAB_POSITION) {
                        subFABContainer.setVisibility(View.VISIBLE);
                    } else {
                        //add code to add task to work Order (dialog, view model, snackbar)
                    }
                else
                    subFABContainer.setVisibility(View.INVISIBLE);
            }
        });

        noteFAB = view.findViewById(R.id.work_order_FAB_note);
        noteFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //add code to add note (dialog, view model, snackbar)
                Log.d(TAG, "note clicked");
            }
        });

        rcaFAB = view.findViewById(R.id.work_order_FAB_RCA);
        rcaFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //add code to update RCA (dialog, view model, snackbar)
                WorkOrderRCADialog dialog = new WorkOrderRCADialog();
                dialog.setTargetFragment(WorkOrderFragment.this, RCA_FRAGMENT_REQUEST_CODE);
                dialog.show(getChildFragmentManager(), "RCA");
                Log.d(TAG, "RCA clicked");
            }
        });

        updateFAB = view.findViewById(R.id.work_order_FAB_Send_Fiix);
        updateFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //add code to add update
                //if status changed to closed -> check if RCA selected
                //  if not, display a banner notifying user to fill in RCA or change status
                Log.d(TAG, "update clicked");
            }
        });

        adapter = new WorkOrderViewPagerAdapter(getChildFragmentManager(), getLifecycle(), username, userId, workOrder);

        viewPager = (ViewPager2) view.findViewById(R.id.fragment_work_order_viewpager);
        viewPager.setAdapter(adapter);

        tabLayout = (TabLayout) view.findViewById(R.id.fragment_work_order_tab_layout);
        visibleTab = DETAIL_TAB_POSITION;

        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                if (position == DETAIL_TAB_POSITION)
                    tab.setText(R.string.detail);
                else
                    tab.setText(R.string.tasks);
            }
        }).attach();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                visibleTab = tab.getPosition();
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

    @Override
    public void sendRCA(String category, String source, String problem, String cause, String action) {

    }
}
