package com.example.lian.meditake;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GetStartedActivity extends AppCompatActivity {

    private Button btnGetStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);

        btnGetStarted = (Button) findViewById(R.id.get_started_btn);

        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getStartedButtonClicked();
            }
        });
    }

    public void getStartedButtonClicked() {


        Intent intent = new Intent(getApplicationContext(), AddMedicationActivity.class);
        startActivity(intent);
        finish();
    }
}
