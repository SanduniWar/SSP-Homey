package com.example.test01;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MainActivity3 extends AppCompatActivity {
    Button bkh2button;
    TextView txvWaterLevel;
    private static final String broker = "tcp://test.mosquitto.org:1883";
    private static final String topicForWaterLevel = "ssp_homey/checkWaterLevel";  // Topic for Plant Water Level
    private static final String clientId = MqttClient.generateClientId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        bkh2button = findViewById(R.id.bkh2button);
        txvWaterLevel = findViewById(R.id.txtWaterLevel);
        connectToMQTT(); //Connection setup

        bkh2button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity3.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void connectToMQTT() {
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

                    String waterLevelMsg = new String(message.getPayload());
                    System.out.println("Water Level Message: " + waterLevelMsg);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            updateTextView(waterLevelMsg); // Update the textView of plan moisture level
                        }
                    });
                }
                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                }
            });

            mqttClient.subscribe(topicForWaterLevel, 0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateTextView(String waterLevel) {

        txvWaterLevel.setText(waterLevel);
    }
}
