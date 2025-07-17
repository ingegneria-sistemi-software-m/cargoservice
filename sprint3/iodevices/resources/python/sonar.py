# File: sonar.py
import RPi.GPIO as GPIO
import time
import sys

GPIO.setmode(GPIO.BCM)
GPIO.setwarnings(False)
TRIG = 17
ECHO = 27

GPIO.setup(TRIG, GPIO.OUT)
GPIO.setup(ECHO, GPIO.IN)
GPIO.output(TRIG, False)   # TRIG parte LOW

# print ('Waiting a few seconds for the sensor to settle')
time.sleep(2)

while True:
    GPIO.output(TRIG, True)    #invia impulso TRIG
    time.sleep(0.00001)
    GPIO.output(TRIG, False)

    pulse_start = time.time()
    #attendi che ECHO parta e memorizza tempo
    while GPIO.input(ECHO)==0:
        pulse_start = time.time()
    # register the last timestamp at which the receiver detects the signal.
    while GPIO.input(ECHO)==1:
        pulse_end = time.time()
    pulse_duration = pulse_end - pulse_start

    # velocità del suono ~= 340m/s 
    # distanza = v*t 
    # il tempo ottenuto misura un roundtrip -> distanza = v*t/2
    distance = pulse_duration * 17165
    distance = round(distance, 1)
    # print ('Distance:', distance,'cm')
    print (distance)
    sys.stdout.flush()
    time.sleep(1)
