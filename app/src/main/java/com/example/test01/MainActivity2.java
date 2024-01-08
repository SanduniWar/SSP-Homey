package com.example.test01;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;



public class MainActivity2 extends AppCompatActivity {
    Button bkh1button;
    //Switch ToggleButton = null;
    TextView txvTemp;

    String heaterStatus;

    String lightStatus;


    private static final String broker = "tcp://test.mosquitto.org:1883";
    private static final String topicForTemp = "ssp_homey/checkTemp";  // Topic for temp

    private static final String topicForHeater = "ssp_homey/heaterTurnOnOff";  // Topic for heater

    private static final String topicForLight = "ssp_homey/lightTurnOnOff";  // Topic for light
    private static final String clientId = MqttClient.generateClientId();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        bkh1button = findViewById(R.id.bkh1button);
        txvTemp = findViewById(R.id.txtIndoorTemp);
        Switch heaterToggleButton = findViewById(R.id.btnToggle2);
        Switch lightToggleButton = findViewById(R.id.btnToggle1);
       // String heaterStatus = "off";
        connectToMQTTBroker("off");
//        heaterTurnonOff(heaterStatus);

        heaterToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    // The toggle button is ON
                    heaterStatus = "on";
                   heaterTurnonOff(heaterStatus);
                } else {
                    // The toggle button is OFF
                    heaterStatus = "off";
                    heaterTurnonOff(heaterStatus);
                }

            }
        });

        lightToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    // The toggle button is ON
                    lightStatus = "on";
                    lightTurnonOff(lightStatus);
                } else {
                    // The toggle button is OFF
                    lightStatus = "off";
                    lightTurnonOff(lightStatus);
                }

            }
        });



        bkh1button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity2.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }


    private void connectToMQTTBroker(String HStatus) {
        try {

            MqttClient mqttClient = new MqttClient(broker, clientId, new MemoryPersistence());
            MqttConnectOptions connectOptions = new MqttConnectOptions();
            connectOptions.setCleanSession(true);

            mqttClient.connect(connectOptions);


            mqttClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    String tempValue = new String(message.getPayload());
                    System.out.println("Temp Value from Sensor: " + tempValue);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateTextView(tempValue);
                        }
                    });

                    float temperatureValue;
                    String heaterStatus ;

                    temperatureValue = Float.parseFloat(tempValue);


                   if (temperatureValue < 1.0) {

                        heaterStatus = "on";
                       heaterTurnonOff(heaterStatus);
                    }


                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                }
            });

            mqttClient.subscribe(topicForTemp, 0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateTextView(String temp) {
        txvTemp.setText(temp);
    }

    private void heaterTurnonOff(String hstatus){
        try {
            MqttClient mqttClient = new MqttClient(broker, clientId, new MemoryPersistence());
            MqttConnectOptions connectOptions = new MqttConnectOptions();
            connectOptions.setCleanSession(true);

            mqttClient.connect(connectOptions);
            mqttClient.publish(topicForHeater, hstatus.getBytes(),0, false);
        }catch(Exception ex){
            ex.printStackTrace();
        }

    }

    private void lightTurnonOff(String lstatus){
        try {
            MqttClient mqttClient = new MqttClient(broker, clientId, new MemoryPersistence());
            MqttConnectOptions connectOptions = new MqttConnectOptions();
            connectOptions.setCleanSession(true);

            mqttClient.connect(connectOptions);
            mqttClient.publish(topicForLight, lstatus.getBytes(),0, false);
        }catch(Exception ex){
            ex.printStackTrace();
        }

    }


}