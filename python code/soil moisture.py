import serial
import time
import time
import paho.mqtt.client as mqtt
import paho.mqtt.publish as publish

# Set MQTT broker and topic
broker = "test.mosquitto.org"   # Broker
pub_topic = "ssp_homey/checkWaterLevel"       # send messages to this topic

############### MQTT section ##################


# when connecting to mqtt do this;
def on_connect(client, userdata, flags, rc):
    if rc==0:
       print ("Connection established. Code: "+str(rc))
    else:
       print ("Connection failed. Code: " +str(rc))

def on_publish(client, userdata, mid):
    print ("Published: " + str(mid))
   

def on_log(client, userdata, level, buf):               # Message is in buf
    print ("MQTT Log: " + str(buf))

# Connect functions for MQTT
client = mqtt.Client()
client.on_connect = on_connect
#client.on_disconnect = on_disconnect
client.on_publish = on_publish
client.on_log = on_log

# Connect to MQTT
print("Attempting to connect to broker " + broker)
client.connect(broker)  # Broker address, port and keepalive (maximum period in$
client.loop_start()


ser = serial.Serial('/dev/ttyACM0', 9600)  # Adjust the port and baud rate

while True:
    data = ser.readline().decode('utf-8').strip()
    print(f'Received from Arduino: {data}')
    client.publish(pub_topic, str(data))
    time.sleep(1)
