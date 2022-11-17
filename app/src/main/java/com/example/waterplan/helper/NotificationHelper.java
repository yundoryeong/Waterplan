package com.example.waterplan.helper;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.waterplan.R;
import com.example.waterplan.activity.LitterActivity;

public class NotificationHelper extends ContextWrapper {
    public static final String channelId = "water_plan";
    public static final String channelName = "WATER";

    // 선언
    private NotificationManager notificationManager;

    private Intent intent;

    public NotificationHelper(Context base) {
        super(base);

        // 안드로이드 버전이 오레오 이상이면 채널 생성
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            // 플래그 설정 (백그라운드에서 알람을 클릭했을 시 해당 액티비티 실행)
            intent = new Intent(this, LitterActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            createNotificationChannel();
        }
    }

    // RequiresApi 어노테이션은 지정한 버전보다 낮을 경우 컴파일 에러 발생
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createNotificationChannel(){
        NotificationChannel channel = new NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
        );
        /* 채널 세팅 */
        channel.enableLights(true);
        channel.enableVibration(true);
        channel.setLightColor(com.google.android.material.R.color.design_default_color_on_primary);
        channel.setLockscreenVisibility((Notification.VISIBILITY_PUBLIC));

        /* 채널을 시스템과 연결 */
        getManager().createNotificationChannel(channel);
    }

    /* 알람 채널을 시스템과 연동 */
    public NotificationManager getManager(){
        // 노티피케이션이 존재하지 않는다면
        if(notificationManager == null){
            // 시스템 서비스에 알람 채널 추가
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return notificationManager;
    }

    /* 알람창 꾸미기 - Compat */
    public NotificationCompat.Builder getChannelNotification(){
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_MUTABLE );
        return new NotificationCompat.Builder(getApplicationContext(), channelId)
                .setSmallIcon(R.drawable.water) // 알람 아이콘 설정
                .setContentTitle("알람") // 알람의 타이틀
                .setContentText("약 먹을 시간이에오 :)") // 알람의 내용
                .setContentIntent(pendingIntent) // 알람 클릭시 해당 pendingIntent 실행
                .setAutoCancel(true) // 알람 클릭 시 해당 알람 삭제
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
    }

    /* 알림 채널 삭제 */
    private void destroyNotification(int id){
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(id);
    }
}
