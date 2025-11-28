package com.example.coletareciclavelsantoamaro;

import android.Manifest;
import android.app.*;
import android.content.*;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.activity.ComponentActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.util.Calendar;
import android.widget.Toast;

public class NotificationUtils {

    public static final String CHANNEL_ID = "coleta_reciclado";
    public static final int REQ_MORNING = 700;   // 07:00
    public static final int REQ_EVENING = 1900;  // 19:00

    public static void createChannel(Context ctx){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel ch = new NotificationChannel(
                    CHANNEL_ID, "Lembretes de Reciclado",
                    NotificationManager.IMPORTANCE_DEFAULT);
            ctx.getSystemService(NotificationManager.class).createNotificationChannel(ch);
        }
    }

    public static boolean hasPostNotifPermission(Context ctx){
        if (Build.VERSION.SDK_INT < 33) return true;
        return ContextCompat.checkSelfPermission(ctx, Manifest.permission.POST_NOTIFICATIONS)
                == PackageManager.PERMISSION_GRANTED;
    }
    public static void requestPostNotifPermission(ComponentActivity act){
        if (Build.VERSION.SDK_INT >= 33) {
            ActivityCompat.requestPermissions(act,
                    new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1234);
        }
    }

    public static void scheduleWeeklyReminder(Context ctx, int[] coletaWeekdays, int hour, int minute, int reqCode){
        if (coletaWeekdays == null || coletaWeekdays.length == 0) return;

        Calendar next = nextReminderTime(Calendar.getInstance(), coletaWeekdays, hour, minute);

        Intent i = new Intent(ctx, AlarmReceiver.class);
        i.putExtra("days_csv", join(coletaWeekdays));
        i.putExtra("hour", hour);
        i.putExtra("minute", minute);

        PendingIntent pi = PendingIntent.getBroadcast(ctx, reqCode, i,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        AlarmManager am = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= 23) {
            am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, next.getTimeInMillis(), pi);
        } else {
            am.setExact(AlarmManager.RTC_WAKEUP, next.getTimeInMillis(), pi);
        }
    }



    public static void cancelAll(Context ctx){
        AlarmManager am = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
        am.cancel(pending(ctx, REQ_MORNING));
        am.cancel(pending(ctx, REQ_EVENING));
    }

    private static PendingIntent pending(Context ctx, int req){
        Intent i = new Intent(ctx, AlarmReceiver.class);
        return PendingIntent.getBroadcast(ctx, req, i,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
    }

    static Calendar nextReminderTime(Calendar base, int[] coletaWeekdays, int hour, int minute){
        Calendar c = (Calendar) base.clone();
        c.set(Calendar.SECOND, 0); c.set(Calendar.MILLISECOND, 0);

        for (int i = 0; i < 14; i++) {
            Calendar tomorrow = (Calendar) c.clone();
            tomorrow.add(Calendar.DAY_OF_YEAR, 1);
            int dowTomorrow = tomorrow.get(Calendar.DAY_OF_WEEK);

            if (contains(coletaWeekdays, dowTomorrow)) {
                c.set(Calendar.HOUR_OF_DAY, hour);
                c.set(Calendar.MINUTE, minute);
                if (c.getTimeInMillis() > base.getTimeInMillis()) return c;
            }
            c.add(Calendar.DAY_OF_YEAR, 1);
        }
        return c;
    }
    private static boolean contains(int[] arr, int v){ for (int x: arr) if (x==v) return true; return false; }
    private static String join(int[] days){
        StringBuilder sb = new StringBuilder();
        for (int i=0;i<days.length;i++){ if (i>0) sb.append(','); sb.append(days[i]); }
        return sb.toString();
    }
    static int[] parseDays(String csv){
        if (csv==null || csv.isEmpty()) return new int[]{};
        String[] p = csv.split(",");
        int[] r = new int[p.length];
        for (int i=0;i<p.length;i++) r[i] = Integer.parseInt(p[i]);
        return r;
    }
}
