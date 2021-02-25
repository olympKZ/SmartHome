# MBTechWorks.com 2017
# Use an HC-SR501 PIR to detect motion (infrared)

#!/usr/bin/python

import RPi.GPIO as GPIO
import time


GPIO.setwarnings(False)
GPIO.setmode(GPIO.BOARD)


#MotionSensor Setup
pir = 8 #Assign pin 8 to PIR

GPIO.setup(pir, GPIO.IN) #Setup GPIO pin PIR as input



sound = 7
GPIO.setup(sound, GPIO.IN)

def callback(sound):
        if GPIO.input(sound):
                print ("Sound Detected!")
               
        else:
                print ("Sound Detected!")
                
                
GPIO.add_event_detect(sound, GPIO.BOTH, bouncetime=300)  # let us know when the pin goes HIGH or LOW
GPIO.add_event_callback(sound, callback)              


print ("Sensor initializing . . .")
time.sleep(60) #Give sensor time to startup
print ("Active")
n=1
try:
    while True:
        if GPIO.input(pir) == True: #If PIR pin goes high, motion is detected
              
                print ("Motion Detected!")
           
                print("Sensor is reloading")
                time.sleep(17) #Keep LED on for 4 seconds

        else:
                print("No motion")
                time.sleep(1)
                
except KeyboardInterrupt: #Ctrl+c
    pass #Do nothing, continue to finally


        