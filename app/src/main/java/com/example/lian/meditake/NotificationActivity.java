package com.example.lian.meditake;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class NotificationActivity extends AppCompatActivity {
    private Button btnSnooze;
    private Button btnTake;
    private TextView title;
    private String name;
    private double doses;
    private int id;
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        name = savedInstanceState.getString("name");
        doses = savedInstanceState.getDouble("doses");
        id = savedInstanceState.getInt("id_notif");

        btnSnooze = (Button)findViewById(R.id.snooze_btn);
        btnTake = (Button)findViewById(R.id.take_btn);
        title = (TextView)findViewById(R.id.text_msg_notification);

        title.setText(getResources().getString(R.string.notification_msg, doses, name));

//        btnSnooze.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //set alarm again to snooze in 10 minutes
//                alarmMgr = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
//                Intent intent = new Intent(getApplicationContext(), NotificationReceiver.class);
//               // intent.putExtra("id_alarm", allReminders.get(i).getId());
//                intent.putExtra("name_notif", name);
//                intent.putExtra("doses_notif", doses);
//                alarmIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
//
//                alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
//                        SystemClock.elapsedRealtime() +
//                                10 * 60 * 1000, alarmIntent);
//            }
//        });

        btnTake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Awesome!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
