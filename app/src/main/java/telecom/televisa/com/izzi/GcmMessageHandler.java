package telecom.televisa.com.izzi;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.util.Calendar;
import java.util.List;

import televisa.telecom.com.model.Push;
import televisa.telecom.com.model.Usuario;

/**
 * Created by cevelez on 05/06/2015.
 */
public class GcmMessageHandler  extends IntentService {

    String mes;
    private Handler handler;
    GcmMessageHandler papa=this;
    public GcmMessageHandler() {
        super("GcmMessageHandler");
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        handler = new Handler();
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();

        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        mes = extras.getString("title");
        showToast();
        Log.i("GCM", "Received : (" + messageType + ")  " + extras.getString("title"));

        GcmBroadcastReceiver.completeWakefulIntent(intent);

    }

    public void showToast(){
        handler.post(new Runnable() {
            public void run() {
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(getApplicationContext()).setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle("izzi")
                                .setContentText(mes);
                Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                Uri alarmSound = Uri.parse("android.resource://" + papa.getPackageName() + "izziSound.mp3");
                mBuilder.setSound(alarmSound);
                mBuilder.setAutoCancel(true);
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
                stackBuilder.addParentStack(MainActivity.class);

                stackBuilder.addNextIntent(resultIntent);
                PendingIntent resultPendingIntent =
                        stackBuilder.getPendingIntent(
                                0,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );
                mBuilder.setContentIntent(resultPendingIntent);
                NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                mNotificationManager.notify(257, mBuilder.build());
                List<Usuario> usr= new Select().from(Usuario.class).execute();

                if(usr!=null) {
                    if (usr.size() > 0) {
                        Usuario currentUser = usr.get(0);
                        currentUser.getUserName();
                        Push p=new Push();
                        p.setCorreo(currentUser.getUserName());
                        p.setFecha(Calendar.getInstance().getTime().getTime()+"");
                        p.setMessage(mes);
                        p.setStatus("1");
                        p.save();
                    }
                }


            }
        });

    }
}
