import paho.mqtt.client as mqtt
import paho.mqtt.publish as publish
import subprocess
import paho.mqtt.client as mqtt


def on_connect(client, userdata, flags, rc):
    print("Connected with result code "+str(rc))
    client.subscribe("ssp_homey/lightTurnOnOff")

def on_message(client, userdata, msg):
    message = msg.payload.decode()
    print("Received message: " + message)
    if(message == 'on'):
        subprocess.run(['tdtool', '--on', '1']) # light Turn On
    elif(message == 'off'):
        subprocess.run(['tdtool', '--off', '1'])# light Turn On 

# Create an MQTT client instance
client = mqtt.Client()
client.on_connect = on_connect
client.on_message = on_message

# Connect to the MQTT broker
client.connect("test.mosquitto.org", 1883, 60)

# Start the MQTT client loop
client.loop_forever()
