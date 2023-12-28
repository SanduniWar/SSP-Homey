package com.example.ssp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

public class MainActivity2 extends AppCompatActivity {
    Button bkh1button;
    TextView txv_temp_indoor = null;
    Switch lightToggle = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

            bkh1button = findViewById(R.id.bkh1button);

            txv_temp_indoor = (TextView) findViewById(R.id.indoortemp);
            txv_temp_indoor.setText("24");
            lightToggle = (Switch) findViewById(R.id.btnToggle1);
            lightToggle = (Switch) findViewById(R.id.btnToggle2);


            bkh1button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity2.this, MainActivity.class);
                    startActivity(intent);
                }
            });

    }

}


