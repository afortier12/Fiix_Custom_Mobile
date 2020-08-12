package ITM.maint.fiix_custom_mobile;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import ITM.maint.fiix_custom_mobile.data.model.entity.AssetCategory;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrder;
import ITM.maint.fiix_custom_mobile.data.model.entity.WorkOrderTask;
import ITM.maint.fiix_custom_mobile.data.repository.RefreshRepository;
import ITM.maint.fiix_custom_mobile.di.AppExecutor;
import ITM.maint.fiix_custom_mobile.ui.view.WorkOrderFragmentArgs;
import ITM.maint.fiix_custom_mobile.utils.Utils;
import ITM.maint.fiix_custom_mobile.utils.Workers.ActionWorker;
import ITM.maint.fiix_custom_mobile.utils.Workers.AssetCategoryWorker;
import ITM.maint.fiix_custom_mobile.utils.Workers.AssetFacilityWorker;
import ITM.maint.fiix_custom_mobile.utils.Workers.CauseWorker;
import ITM.maint.fiix_custom_mobile.utils.Workers.MaintenanceTypeSyncWorker;
import ITM.maint.fiix_custom_mobile.utils.Workers.ProblemWorker;
import ITM.maint.fiix_custom_mobile.utils.Workers.RCANestingWorker;
import ITM.maint.fiix_custom_mobile.utils.Workers.RCASourceWorker;
import ITM.maint.fiix_custom_mobile.utils.Workers.WorkOrderStatusSyncWorker;
import dagger.android.support.DaggerAppCompatActivity;

import static android.graphics.Color.RED;
import static android.media.AudioAttributes.CONTENT_TYPE_SONIFICATION;
import static android.media.AudioAttributes.USAGE_NOTIFICATION_RINGTONE;

public class MainActivity extends DaggerAppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback{

    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private static final String CHANNEL_ID = "Notification_Service_Channel";
    private static final int TASK_NOTIFICATION_ID = 100;
    private static final int WORK_ORDER_NOTIFICATION_ID = 200;
    private static final String TAG = "MainActivity";
    protected static final String PREFERENCE_KEY = "MAIN_KEY";
    protected static final String SET_KEY = "TASK_LIST_SET";
    protected static final String WORK_ORDER_TASK_SYNC = "TASK_SYNC";
    protected static final String USER_KEY = "USER_KEY";
    private ArrayList<Integer> workOrderTaskList;

    protected static SharedPreferences sharedPreferences;
    private RefreshRepository refreshRepository;
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

        refreshRepository = new RefreshRepository(this.getApplication(), id, username);

        refreshRepository.getTaskListChangedLiveData().observe(this, new Observer<RefreshRepository.WorkOrderTaskMessage>() {
            @Override
            public void onChanged(RefreshRepository.WorkOrderTaskMessage workOrderTaskMessage) {

                String title = "";
                String message = "";
                if (workOrderTaskMessage.getOrderList().size() > 3){
                    title = "Multiple work orders added!";
                    message = String.valueOf(workOrderTaskMessage.getOrderList().size()) + " new work orders have been assigned to you";
                    sendNotification(WORK_ORDER_NOTIFICATION_ID, title, message);
                } else {
                    int index = 0;
                    title = "New work order added!";
                    for (int orderNumber : workOrderTaskMessage.getOrderList()) {
                        message = "Work order: " + String.valueOf(orderNumber);
                        sendNotification(WORK_ORDER_NOTIFICATION_ID + index, title, message);
                        index++;
                    }
                    index = 0;
                    title = "New task added!";
                    for (WorkOrderTask task : workOrderTaskMessage.getTaskList()) {
                        message = "Task added to work order: " + String.valueOf(task.getWorkOrderId());
                        sendNotification(TASK_NOTIFICATION_ID + index, title, message);
                        index++;
                    }
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

        OneTimeWorkRequest actionRequest = new OneTimeWorkRequest.Builder(ActionWorker.class)
                .setConstraints(constraints)
                .build();

        OneTimeWorkRequest rcaSourceRequest = new OneTimeWorkRequest.Builder(RCASourceWorker.class)
                .setConstraints(constraints)
                .build();

        OneTimeWorkRequest rcaNestingRequest = new OneTimeWorkRequest.Builder(RCANestingWorker.class)
                .setConstraints(constraints)
                .build();

        OneTimeWorkRequest assetFacilityRequest = new OneTimeWorkRequest.Builder(AssetFacilityWorker.class)
                .setConstraints(constraints)
                .build();

        workManager.beginWith(problemRequest)
                .then(causeRequest)
                .then(actionRequest)
                .then(rcaSourceRequest)
                .then(rcaNestingRequest)
                .then(maintenanceTypeRequest)
                .then(workOrderStatusRequest)
                .then(assetFacilityRequest)
                .enqueue();

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

    private void sendNotification(int notificationID, String title, String message){
        createNotificationChannel();

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        AudioAttributes audioAttributes = new AudioAttributes.Builder().setUsage(USAGE_NOTIFICATION_RINGTONE)
                .setContentType(CONTENT_TYPE_SONIFICATION).build();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification_icon)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setLights(RED, 300, 1000)
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            builder.setSound(alarmSound);
        }

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        notificationManager.notify(notificationID, builder.build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    name,
                    NotificationManager.IMPORTANCE_DEFAULT);
            serviceChannel.setDescription(description);

            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            AudioAttributes audioAttributes = new AudioAttributes.Builder().setUsage(USAGE_NOTIFICATION_RINGTONE)
                    .setContentType(CONTENT_TYPE_SONIFICATION).build();

            serviceChannel.enableLights(true);
            serviceChannel.setLightColor(RED);
            serviceChannel.enableVibration(true);
            serviceChannel.setVibrationPattern( new long [] {100, 200, 300, 400, 500, 400, 300, 200, 400});
            serviceChannel.setSound(alarmSound, audioAttributes);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
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

    @Override
    protected void onStart() {
        super.onStart();

    }


    @Override
    protected void onResume() {
        super.onResume();

        if (refreshRepository.checkIfDisposed()) {
            refreshRepository.createDisposable();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        refreshRepository.dispose();
    }

    @Override
    protected void onStop() {
        super.onStop();

        refreshRepository.dispose();
    }
}
