package com.example.test01;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MainActivity2 extends AppCompatActivity {
    Button bkh1button;
    TextView txv_temp;
    private static final String BROKER = "tcp://test.mosquitto.org:1883";  // change
    private static final String TOPIC = "ssp_homey/checkTemp";  //  // change
    private static final String CLIENT_ID = MqttClient.generateClientId(); //  // change

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        bkh1button = findViewById(R.id.bkh1button);
        txv_temp = findViewById(R.id.indoortemp);
        connectToMQTTBroker();

        bkh1button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity2.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void connectToMQTTBroker() { // change
        try {
            MqttClient mqttClient = new MqttClient(BROKER, CLIENT_ID, new MemoryPersistence()); // change
            MqttConnectOptions options = new MqttConnectOptions(); //change
            options.setCleanSession(true);

            mqttClient.connect(options);

            // Set the callback for message handling
            mqttClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    // Handle connection lost
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception { // change
                    // Update the TextView with the received message
                    String payload = new String(message.getPayload());  //cnahe
                    System.out.println("Incoming message: " + payload); // cnahge
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateTextView(payload);  //change
                        }
                    });
                }
                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    // Delivery complete
                }
            });

            // Subscribe to the topic
            mqttClient.subscribe(TOPIC, 0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateTextView(String payload) {  //cnahge
        txv_temp.setText(payload);
    }
}