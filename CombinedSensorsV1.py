# MBTechWorks.com 2017
# Use an HC-SR501 PIR to detect motion (infrared)

#!/usr/bin/python

import RPi.GPIO as GPIO
import time
import Adafruit_DHT
import time

# Set sensor type : Options are DHT11,DHT22 or AM2302
tempHumSensor=Adafruit_DHT.DHT11

# Set GPIO sensor is connected to
gpio=22

GPIO.setwarnings(False)
GPIO.setmode(GPIO.BOARD)


#MotionSensor Setup
pir = 8 #Assign pin 8 to PIR

GPIO.setup(pir, GPIO.IN) #Setup GPIO pin PIR as input



sound = 7
GPIO.setup(sound, GPIO.IN)

print ("Sensor initializing . . .")
time.sleep(60) #Give sensor time to startup

def callback(sound):
        if GPIO.input(sound):
                print ("Sound Detected!")
               
        else:
                print ("Sound Detected!")
                
                
GPIO.add_event_detect(sound, GPIO.BOTH, bouncetime=300)  # let us know when the pin goes HIGH or LOW
GPIO.add_event_callback(sound, callback)              

print ("Active")
n=1
try:
    while True:
        
        humidity, temperature = Adafruit_DHT.read_retry(tempHumSensor, gpio)
 
        # Reading the DHT11 is very sensitive to timings and occasionally
        # the Pi might fail to get a valid reading. So check if readings are valid.
        if humidity is not None and temperature is not None:
            print('Temperature and Humidity sensor readings:')
            print('Temp={0:0.1f}*C  Humidity={1:0.1f}%'.format(temperature, humidity))
        else:
            print('Failed to get reading. Try again!')
        time.sleep(3)
        
        if GPIO.input(pir) == True: #If PIR pin goes high, motion is detected
              
                print ("Motion Detected!")
           
                print("Sensor is reloading")
                time.sleep(17) #Keep LED on for 4 seconds

        else:
                print("No motion")
                time.sleep(1)
                
except KeyboardInterrupt: #Ctrl+c
    pass #Do nothing, continue to finally