import subprocess
import json
import re
import time
import paho.mqtt.client as mqtt
import paho.mqtt.publish as publish

# Set MQTT broker and topic
broker = "test.mosquitto.org"   # Broker
pub_topic = "ssp_homey/checkTemp"       # send messages to this topic

############### MQTT section ##################


# when connecting to mqtt do this;
def on_connect(client, userdata, flags, rc):
    if rc==0:
       print ("Connection established. Code: "+str(rc))
    else:
       print ("Connection failed. Code: " +str(rc))

def on_publish(client, userdata, mid):
    print ("Published: " + str(mid))

#def on_disconnect(client, userdata, rc):
 #   if rc != 0:
#       print ("Unexpected disonnection. Code: " +str(rc))
 #   else:
  #      print ("Disconnected. Code: " + str(rc))
    

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
client.connect(broker)  # Broker address, port and keepalive 
client.loop_start()

while True:
    try:
        # Run the subprocess to get temperature data
        temp_process = subprocess.run(['tdtool', '--list-sensors', '199'], capture_output=True, text=True)
        temp = temp_process.stdout

        # Split the data into individual sensor readings
        sensor_readings = temp.split('\n')


        # Use regular expression to extract key-value pairs
        temp_data = dict(re.findall(r'(\S+)=\s*([^=\s]+)', sensor_readings[0]))

        # Convert the dictionary to JSON
        json_data = json.dumps(temp_data)

        # Parse the JSON
        data = json.loads(json_data)
        temperature_value = str(data["temperature"])
        print ("data"+ str(temperature_value))
        #temperature_value = str(data["temperature"])
        client.publish(pub_topic, str(temperature_value))

    except Exception as e:
        print("Error:", e)

    # Add a delay before the next iteration 
    time.sleep(2.0)


