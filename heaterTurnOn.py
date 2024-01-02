import paho.mqtt.client as mqtt
import paho.mqtt.publish as publish
import subprocess
import paho.mqtt.client as mqtt
import paho.mqtt.publish as publish

# Set MQTT broker and topic
broker = "test.mosquitto.org"   # Broker
pub_topic = "ssp_homey/heaterTurnOnOff"       # send messages to this topic


# when connecting to mqtt do this;
def on_connect(client, userdata, flags, rc):
    if rc==0:
       print ("Connection established. Code: "+str(rc))
    else:
       print ("Connection failed. Code: " +str(rc))

def on_message(client, userdata, msg):
    payload = str(msg.payload.decode('utf-8'))
    print(f"Received message from Android: {payload}")

    if(payload == 'on'):
        subprocess.run(['tdtool', '--on', '2'])
    elif(payload == 'off'):
        subprocess.run(['tdtool', '--off', '2'])

def on_log(client, userdata, level, buf):               # Message is in buf
    print ("MQTT Log: " + str(buf))


# Connect functions for MQTT
client = mqtt.Client()
client.on_connect = on_connect
#client.on_disconnect = on_disconnect
client.on_message = on_message
client.on_log = on_log
client.subscribe(pub_topic)

# Connect to MQTT
print("Attempting to connect to broker " + broker)
client.connect(broker)  


# Start the MQTT loop
client.loop_forever()

