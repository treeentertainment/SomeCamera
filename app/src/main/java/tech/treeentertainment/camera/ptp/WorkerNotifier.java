package tech.treeentertainment.camera.ptp;

import android.app.NotificationManager;
import android.content.Context;
import android.app.Notification;
import android.os.Build;
import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.app.NotificationCompat;
import android.annotation.SuppressLint;
import androidx.core.content.ContextCompat;

import tech.treeentertainment.camera.R;
import tech.treeentertainment.camera.util.NotificationIds;

public class WorkerNotifier implements Camera.WorkerListener {

    private final NotificationManager notificationManager;
    private final Notification notification;
    private final int uniqueId;
    private final Context appContext;
    private static final String CHANNEL_ID = "worker_channel";

    public WorkerNotifier(Context context) {
        appContext = context.getApplicationContext();
        notificationManager = (NotificationManager) appContext.getSystemService(Context.NOTIFICATION_SERVICE);
        
        // You should create the notification channel elsewhere if targeting API 26+
        notification = new NotificationCompat.Builder(context.getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.icon)
                .setContentTitle(context.getString(R.string.worker_content_title))
                .setContentText(context.getString(R.string.worker_content_text))
                .setTicker(context.getString(R.string.worker_ticker))
                .setAutoCancel(false)
                .build();
        uniqueId = NotificationIds.getInstance().getUniqueIdentifier(WorkerNotifier.class.getName() + ":running");
    }

    @Override
    @SuppressLint("NotificationPermission")
    public void onWorkerStarted() {
        notification.flags |= Notification.FLAG_NO_CLEAR;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(appContext, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                notificationManager.notify(uniqueId, notification);
            }
        } else {
            notificationManager.notify(uniqueId, notification);
        }
    }

    @Override
    public void onWorkerEnded() {
        notificationManager.cancel(uniqueId);
    }

}
