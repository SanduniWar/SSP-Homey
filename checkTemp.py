import subprocess
import json
import re
import time

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

        # Access the temperature value
        temperature_value = float(data["temperature"])
        # Check if temperature is greater than 22.5
        #if temperature_value > 21.5:
            # Turn on the device
            # Turn on the device
            #subprocess.run(['tdtool', '--on', '2'])
           # print("Device turned ON")

        #else:
            # Turn off the device
           # subprocess.run(['tdtool', '--off', '2'])
            #print("Device turned OFF")

        # Print the temperature value
        print("Temperature:", temperature_value)

    except Exception as e:
        print("Error:", e)

    # Add a delay before the next iteration (e.g., 10 seconds)
    time.sleep(10)


