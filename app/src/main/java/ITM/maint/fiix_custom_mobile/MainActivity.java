package ITM.maint.fiix_custom_mobile;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
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

import javax.inject.Inject;

import ITM.maint.fiix_custom_mobile.di.AppExecutor;
import ITM.maint.fiix_custom_mobile.ui.view.WorkOrderFragmentArgs;
import dagger.android.support.DaggerAppCompatActivity;

public class MainActivity extends DaggerAppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback{

    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private static final String TAG = "MainActivity";
    private String username;
    private int id;
    private NavArgument idArg;
    private NavArgument usernameArg;

    @Inject
    AppExecutor appExecutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        username = intent.getStringExtra("User");
        id = intent.getIntExtra("id", 0);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //getSupportActionBar().hide();

        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        usernameArg = new NavArgument.Builder().setDefaultValue(username).build();
        idArg = new NavArgument.Builder().setDefaultValue(id).build();
        NavInflater navInflator = navController.getNavInflater();
        NavGraph navGraph = navInflator.inflate(R.navigation.mobile_navigation);
        navGraph.addArgument("User",usernameArg);
        navGraph.addArgument("id", idArg);
        navController.setGraph(navGraph);
        NavigationUI.setupWithNavController(navView, navController);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                switch (destination.getId()){
                    case R.id.navigation_home:
                        setTitle("Assigned Work Orders");
                        break;
                    case R.id.navigation_workOrder:
                        String code = WorkOrderFragmentArgs.fromBundle(arguments).getWorkOrder().getCode();
                        setTitle("Work Order: " + code);
                        break;
                    case R.id.navigation_partAdd:
                        setTitle("Add Part");
                    case R.id.navigation_partSearch:
                        setTitle("Part Search");
                    default:

                }
            }
        });

        checkCameraPermissions();
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

    private void update
}
