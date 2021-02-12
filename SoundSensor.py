import RPi.GPIO as GPIO
import time

#GPIO SETUP
sound = 17
led = 27
GPIO.setwarnings(False)
GPIO.setmode(GPIO.BCM)
GPIO.setup(sound, GPIO.IN)
GPIO.setup(led,GPIO.OUT)

def callback(sound):
        if GPIO.input(sound):
                print ("Sound Detected!")
                GPIO.output(led,True)
        else:
                print ("Sound Detected!")
                GPIO.output(led,False)
                
GPIO.add_event_detect(sound, GPIO.BOTH, bouncetime=300)  # let us know when the pin goes HIGH or LOW
GPIO.add_event_callback(sound, callback)  # assign function to GPIO PIN, Run function on change
n=1
# infinite loop
while True:
        n+=1
        print(n)
        time.sleep(1)
        