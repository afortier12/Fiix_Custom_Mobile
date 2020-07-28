package ITM.maint.fiix_custom_mobile;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavArgument;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavGraph;
import androidx.navigation.NavInflater;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrder;
import ITM.maint.fiix_custom_mobile.di.AppExecutor;
import ITM.maint.fiix_custom_mobile.ui.view.WorkOrderFragmentArgs;
import ITM.maint.fiix_custom_mobile.utils.Utils;
import ITM.maint.fiix_custom_mobile.utils.Workers.CauseWorker;
import ITM.maint.fiix_custom_mobile.utils.Workers.MaintenanceTypeSyncWorker;
import ITM.maint.fiix_custom_mobile.utils.Workers.ProblemWorker;
import ITM.maint.fiix_custom_mobile.utils.Workers.RCANestingWorker;
import ITM.maint.fiix_custom_mobile.utils.Workers.RCASourceWorker;
import ITM.maint.fiix_custom_mobile.utils.Workers.WorkOrderStatusSyncWorker;
import dagger.android.support.DaggerAppCompatActivity;

public class MainActivity extends DaggerAppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback{

    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private static final String TAG = "MainActivity";
    protected static final String PREFERENCE_KEY = "MAIN_KEY";
    protected static SharedPreferences sharedPreferences;
    private String username;
    private int id;
    private NavArgument idArg;
    private NavArgument usernameArg;

    private WorkManager workManager;
    private DateFormat dateFormat;

    @Inject
    AppExecutor appExecutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        username = intent.getStringExtra("User");
        id = intent.getIntExtra("id", 0);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        workManager = WorkManager.getInstance(this.getApplication());

        BottomNavigationView navView = findViewById(R.id.nav_view);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        usernameArg = new NavArgument.Builder().setDefaultValue(username).build();
        idArg = new NavArgument.Builder().setDefaultValue(id).build();
        NavInflater navInflator = navController.getNavInflater();
        NavGraph navGraph = navInflator.inflate(R.navigation.mobile_navigation);
        navGraph.addArgument("User", usernameArg);
        navGraph.addArgument("id", idArg);
        navController.setGraph(navGraph);
        NavigationUI.setupWithNavController(navView, navController);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                switch (destination.getId()) {
                    case R.id.navigation_home:
                        setTitle("Assigned Work Orders");
                        break;
                    case R.id.navigation_workOrder:
                        WorkOrder workOrder = WorkOrderFragmentArgs.fromBundle(arguments).getWorkOrder();
                        setTitle("Work Order: " + workOrder.getCode());
                        Drawable img = Utils.getPriorityIcon(workOrder.getPriorityOrder(), getApplicationContext());
                        getSupportActionBar().setIcon(img);
                        break;
                    case R.id.navigation_partAdd:
                        setTitle("Add Part");
                    case R.id.navigation_partSearch:
                        setTitle("Part Search");
                    default:

                }
            }
        });

        sharedPreferences = getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE);
        dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
        Date updatedDate = compareDates();
        if (updatedDate != null){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("LastUpdate", dateFormat.format(updatedDate));
            editor.apply();
            updateTables();
        }

        checkCameraPermissions();
    }

    private Date compareDates(){
        Date currentDate = new Date();
        String lastUpdate = sharedPreferences.getString("LastUpdate", null);
        if (lastUpdate != null){
            try {
                Date lastDate = dateFormat.parse(lastUpdate);
                long difference = currentDate.getTime() - lastDate.getTime();
                if ((difference/(1000*60*60*25)) > 1 )
                    return currentDate;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            return currentDate;
        }
        return null;
    }

    private void updateTables(){

        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .build();

        OneTimeWorkRequest maintenanceTypeRequest = new OneTimeWorkRequest.Builder(MaintenanceTypeSyncWorker.class)
                .setConstraints(constraints)
                .build();

        OneTimeWorkRequest workOrderStatusRequest = new OneTimeWorkRequest.Builder(WorkOrderStatusSyncWorker.class)
                .setConstraints(constraints)
                .build();

        OneTimeWorkRequest problemRequest = new OneTimeWorkRequest.Builder(ProblemWorker.class)
                .setConstraints(constraints)
                .build();

        OneTimeWorkRequest causeRequest = new OneTimeWorkRequest.Builder(CauseWorker.class)
                .setConstraints(constraints)
                .build();

        OneTimeWorkRequest rcaSourceRequest = new OneTimeWorkRequest.Builder(RCASourceWorker.class)
                .setConstraints(constraints)
                .build();
        OneTimeWorkRequest rcaNestingRequest = new OneTimeWorkRequest.Builder(RCANestingWorker.class)
                .setConstraints(constraints)
                .build();

        workManager.beginWith(problemRequest)
                .then(causeRequest)
                .then(rcaSourceRequest)
                .then(rcaNestingRequest)
                .enqueue();

        //                .then(workOrderStatusRequest)
        //                .then(rcaRequest)

    }


    private void checkCameraPermissions() {
        if (!isCameraPermissionGranted()) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA_PERMISSION);
        }
    }

    private boolean isCameraPermissionGranted() {
        return ContextCompat.checkSelfPermission(
                this, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.CAMERA)) {
                        Toast.makeText(this, "Permission Denied By User.", Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(this, "Permission Disabled. Check Settings...Apps & notifications...App Permissions", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        if (navController.getCurrentDestination() != null)
            navController.popBackStack();
        else
            navController.navigate(navController.getGraph().getStartDestination());
    }

}
