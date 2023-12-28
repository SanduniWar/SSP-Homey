import serial
import time

ser = serial.Serial('/dev/ttyACM0', 9600)  # Adjust the port and baud rate

while True:
    data = ser.readline().decode('utf-8').strip()
    print(f'Received from Arduino: {data}')
    time.sleep(1)
