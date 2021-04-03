import sys
import RPi.GPIO as GPIO
import time

from datetime import datetime


import pyrebase

config = {
    # authentication details
  }
firebase = pyrebase.initialize_app(config)
db = firebase.database()

now = datetime.now()
date_time_on = now.strftime("%d/%m/%Y %H:%M:%S")

# now_off = datetime.now()
# date_time_off = now_off.strftime("%d/%m/%Y %H:%M:%S")


GPIO.setwarnings(False)
BuzzerPin = 14

CL = [0, 131, 147, 165, 175, 196, 211, 248] # Low C Note Frequency
CM = [0, 262, 294, 330, 350, 393, 441, 495] # Middle C Note Frequency
CH = [0, 525, 589, 661, 700, 786, 882, 990] # High C Note Frequency

song_1 = [ CM[3], CM[5], CM[6], CM[3], CM[2], CM[3], CM[5], CM[6], # Sound Notes 1
CH[1], CM[6], CM[5], CM[1], CM[3], CM[2], CM[2], CM[3],
CM[5], CM[2], CM[3], CM[3], CL[6], CL[6], CL[6], CM[1],
CM[2], CM[3], CM[2], CL[7], CL[6], CM[1], CL[5] ]

beat_1 = [ 1, 1, 3, 1, 1, 3, 1, 1, # Beats of song 1, 1 means 1/8 beats
1, 1, 1, 1, 1, 1, 3, 1,
1, 3, 1, 1, 1, 1, 1, 1,
1, 2, 1, 1, 1, 1, 1, 1,
1, 1, 3 ]

song_2 = [ CM[1], CM[1], CM[1], CL[5], CM[3], CM[3], CM[3], CM[1], # Sound Notes 2
CM[1], CM[3], CM[5], CM[5], CM[4], CM[3], CM[2], CM[2],
CM[3], CM[4], CM[4], CM[3], CM[2], CM[3], CM[1], CM[1],
CM[3], CM[2], CL[5], CL[7], CM[2], CM[1] ]

beat_2 = [ 1, 1, 2, 2, 1, 1, 2, 2, # Beats of song 2, 1 means 1/8 beats
1, 1, 2, 2, 1, 1, 3, 1,
1, 2, 2, 1, 1, 2, 2, 1,
1, 2, 2, 1, 1, 3 ]

GPIO.setmode(GPIO.BCM) # Numbers GPIOs by physical location
GPIO.setup(BuzzerPin, GPIO.OUT) # Set pins' mode is output

def setup():
    GPIO.setmode(GPIO.BCM) # Numbers GPIOs by physical location
    GPIO.setup(BuzzerPin, GPIO.OUT) # Set pins' mode is output
global Buzz # Assign a global variable to replace GPIO.PWM
Buzz = GPIO.PWM(BuzzerPin, 440) # 440 is initial frequency.
#Buzz.start(50) # Start BuzzerPin pin with 50% duty ration

#db.update({"buzzer": "on"})

def loop():
    #if(db.child("alarm").get()=="on")
    while True:
        alarm = db.child("alarm").get()
        motion = db.child("motion").get()
        sound = db.child("sound").get()
        while alarm.val()=="on":
            
            #if (db.child("motion").get().val()!= motion.val()):
            if (db.child("motion").get().val()!= motion.val() or db.child("sound").get().val()!= sound.val()):
                db.update({"notification":"on"})
                db.child("buzzer").update({"status": "on"})
                db.child("buzzer").update({"lastOn": date_time_on})
                Buzz.start(50)
                print ('\n Playing song 1...')                
                for i in range(1, len(song_1)): # Play song 1
                    Buzz.ChangeFrequency(song_1[i]) # Change the frequency along the song note
                    time.sleep(beat_1[i] * 0.5) # delay a note for beat * 0.5s
                    time.sleep(1) # Wait a second for next song.
                    alarm = db.child("alarm").get()
                    if alarm.val()=="off":
                        destroy()
                        break
                #print ('\n\n Playing song 2...')
                #for i in range(1, len(song_2)): # Play song 1
                    #Buzz.ChangeFrequency(song_2[i]) # Change the frequency along the song note
                    #time.sleep(beat_2[i] * 0.5) # delay a note for beat * 0.5s
            

def destroy():
    Buzz.stop() # Stop the BuzzerPin
    #print(date_time_off)
    db.child("buzzer").update({"status": "off"})
    now = datetime.now()
    date_time_off = now.strftime("%d/%m/%Y %H:%M:%S")
    db.child("buzzer").update({"offSince": date_time_off})
    end_time = time.time()
    db.child("buzzer").update({"lastOnDuration": end_time-start_time})
GPIO.output(BuzzerPin, 1) # Set BuzzerPin pin to High
GPIO.cleanup() # Release resource


if __name__ == '__main__': # Program start from here
    #setup()
    db.child("buzzer").update({"status": "on"})
    #db.child("buzzer").update({"lastOn": date_time_on})
        #GPIO.setmode(GPIO.BCM) # Numbers GPIOs by physical location
        #GPIO.setup(BuzzerPin, GPIO.OUT) # Set pins' mode is output
try:
    setup()
    start_time=time.time()
    loop()
except KeyboardInterrupt: # When 'Ctrl+C' is pressed, the child program destroy() will be executed.
    destroy()
    
#if sys.exit()
    #db.child("buzzer").update({"status": "off"})
    #db.child("buzzer").update({"offSince": date_time_string})

