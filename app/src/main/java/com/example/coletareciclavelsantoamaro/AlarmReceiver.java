package com.example.coletareciclavelsantoamaro;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.*;
import androidx.core.app.NotificationCompat;
import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {
    @Override public void onReceive(Context context, Intent intent) {
        String daysCsv = intent.getStringExtra("days_csv");
        int hour = intent.getIntExtra("hour", 19);
        int minute = intent.getIntExtra("minute", 0);
        int[] days = NotificationUtils.parseDays(daysCsv);

        Notification n = new NotificationCompat.Builder(context, NotificationUtils.CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_popup_reminder)
                .setContentTitle("Reciclado amanhã")
                .setContentText("Separe o reciclado: a coleta acontece amanhã no seu bairro.")
                .setAutoCancel(true)
                .build();
        ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE))
                .notify((hour==7? 3007:3019), n);

        Calendar next = NotificationUtils.nextReminderTime(Calendar.getInstance(), days, hour, minute);
        Intent i = new Intent(context, AlarmReceiver.class);
        i.putExtra("days_csv", daysCsv);
        i.putExtra("hour", hour);
        i.putExtra("minute", minute);

        int reqCode = (hour==7 ? NotificationUtils.REQ_MORNING : NotificationUtils.REQ_EVENING);
        PendingIntent pi = PendingIntent.getBroadcast(context, reqCode, i,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        ((AlarmManager) context.getSystemService(Context.ALARM_SERVICE))
                .setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, next.getTimeInMillis(), pi);
    }
}
