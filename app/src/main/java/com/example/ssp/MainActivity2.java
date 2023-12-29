package com.example.ssp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;


public class MainActivity2 extends AppCompatActivity {
    Button bkh1button;
    TextView txv_temp_indoor = null;
    Switch lightToggle = null;
    String outputValue = null;

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


        lightToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // below you write code to change switch status and action to take
                if (isChecked) {
                    new AsyncTask<Integer, Void, Void>() {
                        @Override
                        protected Void doInBackground(Integer... params) {
                            //your code to fetch results via SSH
                            run("python3 /ssp_homey/checkTemp.py/turnOnLight.py");
                            return null;
                        }
                    }.execute(1);


                } else {
                    new AsyncTask<Integer, Void, Void>() {
                        @Override
                        protected Void doInBackground(Integer... params) {
                            //your code to fetch results via SSH
                            run("python3 /ssp_homey/checkTemp.py/turnOffLight.py");
                            return null;
                        }
                    }.execute(1);
// to do something if not checked
                }
            }
        });

    }
    public void run (String command) {
        String hostname = "192.168.0.100";
        String username = "pi";
        String password = "pi";
        try
        {
            ch.ethz.ssh2.Connection conn = new Connection(hostname); //init connection
            conn.connect(); //start connection to the hostname
            boolean isAuthenticated = conn.authenticateWithPassword(username,
                    password);
            if (isAuthenticated == false)
                throw new IOException("Authentication failed.");
            Session sess = conn.openSession();
            sess.execCommand(command);
            InputStream stdout = new StreamGobbler(sess.getStdout());
            BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
            StringBuilder output = new StringBuilder();

//reads text
            while (true){
                String line = br.readLine(); // read line
                output.append(line);
                if (line == null)
                    break;
                System.out.println(line);
                outputValue = output.toString();
            }
            System.out.println(output);
            System.out.println(outputValue);

            /* Show exit status, if available (otherwise "null") */
            System.out.println("ExitCode: " + sess.getExitStatus());
            sess.close(); // Close this session
            conn.close();
        }
        catch (IOException e)
        { e.printStackTrace(System.err);
            System.exit(2); }
    }

}






