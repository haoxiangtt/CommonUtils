package cn.bfy.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

/**
 * Created by ouyangjinfu on 2016/7/12.
 * 设置定时任务的工具
 * //TODO 目前只是个半成品，有待扩展
 */
public class AlarmTaskController {

    public static void setRepeatAlarmTask(Context context, Intent intent){
        PendingIntent sender = PendingIntent.getBroadcast(
                context, 0, intent, 0);
        // We want the alarm to go off 10 seconds from now.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 60 * 60);
        // Schedule the alarm!
        AlarmManager am = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        System.out.println(">>>>>cancel old repeat alarm task<<<<<<<");
        am.cancel(sender);
        System.out.println(">>>>>set repeat alarm task<<<<<<<");
        am.setRepeating(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(), 60 * 60 * 1000, sender);
    }

}
