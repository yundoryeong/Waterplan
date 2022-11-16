package com.example.waterplan.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.DialogFragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.waterplan.R;
import com.example.waterplan.fragment.TimePickerFragment;
import com.example.waterplan.helper.NotificationHelper;
import com.example.waterplan.receiver.AlertReceiver;

import java.text.DateFormat;
import java.util.Calendar;

/*
    알람을 만드는 절차
    1. 권한 설정 (!! 최우선 !!)
    2. 노티피케이션 설정 (NotificationHelper)
    3. 사용자가 시간을 선택할 수 있는 팝업창 띄우기 (TimePickerFragment)


 */

public class LitterActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    private TextView textView, alarmTextView;
    private Button alarmSetting, alarmCancel;

    // NotificationHelper 선언
    private NotificationHelper notificationHelper;
    // 알람 빌더
    private NotificationCompat.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_litter);
        Intent intent = getIntent();
        int temp = intent.getIntExtra("wantWater", 0);

        textView = (TextView) findViewById(R.id.textView);
        alarmTextView = (TextView) findViewById(R.id.alarmTextView);
        alarmSetting = (Button) findViewById(R.id.alarmSetting);
        alarmCancel = (Button) findViewById(R.id.alarmCancel);

        textView.setText(String.valueOf(temp));

        // 알람 도우미를 해당 LitterActivity에 지정
        notificationHelper = new NotificationHelper(getApplicationContext());

        // 알람 버튼 클릭 이벤트
        alarmSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });

        alarmCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelAlarm();
            }
        });
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        // 캘린터 객체 호출
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour); // 시간 설정
        calendar.set(Calendar.MINUTE, minute); // 분 설정
        calendar.set(Calendar.SECOND, 0); // 초 설정

        // 화면에 위에서 설정한 시간을 뿌리기
        updateTimeText(calendar);
        // !! 중요 : 알림 설정 (캘린더와 알람을 연결해주는 함수) !!
        startAlarm(calendar);
    }

    // 텍스트뷰에 알람 뿌려주기
    private void updateTimeText(Calendar calendar){
        String timeText = "알람 시간 : ";
        timeText += DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar.getTime());
        alarmTextView.setText(timeText);
    }

    // 알람 시작 (캘린더와 알람 연동)
    private  void startAlarm(Calendar calendar){
        // 알람 매니저 호출 - 시스템 서비스에 알람 추가
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), AlertReceiver.class);
         /*
        PendingIntent는 intent를 바로 수행하지 않고, 특정 시점에 수행시킨다.
        이때 특정 시점은 !! 앱이 구동되고 있지 않을 때 !!
        즉, 알람 매니저를 통해 어플리케이션이 구동되고 있지 않아도 getSystemService로 알람 서비스를 지정하였기에
        알람 매니저로 지정된 시간에 intent를 수행시킨다.
        PendingIntent.getBroadcast는 결론적으로 특정 시점에 브로드캐스트 실행
         */
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 1, intent, PendingIntent.FLAG_MUTABLE);
        // 캘린더 객체보다 매개변수로 넘어온 캘린더 객체가 먼저 생성되었다면
        if(calendar.before(Calendar.getInstance())){
            calendar.add(Calendar.DATE, 1); // 날짜에 하루 추가하기
        }
        // 알람 매니저로 설정한 시간에 기기의 절전모드를 해제(RTC_WAKEUP)시켜 대기중인 인텐트(PendingIntent)를 실행한다.
        // setExact 메서드는 지정된 시간에 알람이 전달되도록 예약한다.
        // setExact(int type, long triggerAtMillis, PendingIntent operation)
        // 알람 타입, 캘린더에서 설정한 시간을 ms로 변환한 값, 대기중인 인텐트를 인수로 넘긴다.
        // RTC_WAKEUP를 사용하기 위해서는 퍼미션을 추가해야한다. <uses-permission android:name="android.permission.SCHEDULE_EXACT_ALARM" />
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        Toast.makeText(getApplicationContext(), "알람이 설정되었습니다.", Toast.LENGTH_LONG).show();
    }

    // 알람 취소
    private void cancelAlarm(){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 1, intent, PendingIntent.FLAG_MUTABLE);
        alarmManager.cancel(pendingIntent);
        alarmTextView.setText("알람이 취소되었습니다.");
        Toast.makeText(getApplicationContext(), "알람이 취소되었습니다.", Toast.LENGTH_LONG).show();
    }
}