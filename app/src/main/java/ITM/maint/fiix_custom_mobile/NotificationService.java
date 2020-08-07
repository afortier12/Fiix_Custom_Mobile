package ITM.maint.fiix_custom_mobile;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static android.graphics.Color.RED;
import static android.media.AudioAttributes.CONTENT_TYPE_SONIFICATION;
import static android.media.AudioAttributes.USAGE_NOTIFICATION_RINGTONE;

public class NotificationService extends Service {

    public static final String TAG = "NotificationService";
    public static final String CHANNEL_ID = "Notification_Service_Channel";

    private Messenger messenger;
    private ArrayList<String> workOrderTaskList;

    public class LocalBinder extends Binder {
        NotificationService getService() {
            // Return this instance of LocalService so clients can call public methods
            return NotificationService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        workOrderTaskList = new ArrayList<String>();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotificationChannel();

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        AudioAttributes audioAttributes = new AudioAttributes.Builder().setUsage(USAGE_NOTIFICATION_RINGTONE)
                .setContentType(CONTENT_TYPE_SONIFICATION).build();

        Intent notificationIntent = new Intent(this, MainActivity.class);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification_icon)
                .setContentTitle("New Assignment")
                .setContentText("Work order#: ")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setLights(RED, 300, 1000)
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            builder.setSound(alarmSound);
        }

        return START_NOT_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        messenger = new Messenger(new IncomingHandler(this));
        return messenger.getBinder();
    }

    static class IncomingHandler extends Handler {
        private Context applicationContext;

        IncomingHandler(Context context) {
            applicationContext = context.getApplicationContext();
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
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
}
