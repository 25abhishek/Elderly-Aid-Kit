from sys import argv
import gps
import requests
import RPi.GPIO as GPIO                  
import time
import firebase_admin
import google.cloud
from firebase_admin import credentials, firestore
from math import atan2,sin,cos,sqrt,radians

TRIG = 23                                  
ECHO = 24                                  

GPIO.setup(TRIG,GPIO.OUT)                  
GPIO.setup(ECHO,GPIO.IN)    

GPIO.setwarnings(False) 
GPIO.setmode(GPIO.BOARD) 
GPIO.setup(11, GPIO.IN, pull_up_down=GPIO.PUD_DOWN)
cred = credentials.Certificate("./ServiceAccountKey.json")
app = firebase_admin.initialize_app(cred)
store = firestore.client()
doc_ref = store.collection(u'data')

#GPIO.setmode(GPIO.BCM)    #Set GPIO pin numbering 
GPIO.setwarnings(False)
buzzer=7
GPIO.setup(buzzer,GPIO.OUT)
GPIO.output(buzzer,GPIO.LOW)
     
R=6371.0
lat1=0.0
lon1=0.0
i=1
push=1

def ultrasonic():
    GPIO.output(TRIG, False)        
	time.sleep(0.5)                            
  
    GPIO.output(TRIG, True)                  #Set TRIG as HIGH
    time.sleep(0.00001)                      #Delay of 0.00001 seconds
    GPIO.output(TRIG, False)                 #Set TRIG as LOW

    while GPIO.input(ECHO)==0:               #Check whether the ECHO is LOW
        pulse_start = time.time()              #Saves the last known time of LOW pulse

    while GPIO.input(ECHO)==1:               #Check whether the ECHO is HIGH
        pulse_end = time.time()                #Saves the last known time of HIGH pulse 

    pulse_duration = pulse_end - pulse_start #Get pulse duration to a variable

    distance = pulse_duration * 171.50        #Multiply pulse duration by 17150 to get distance
    distance = round(distance, 2)            #Round to two decimal points

    if distance < 1.2: 
        GPIO.output(buzzer,GPIO.HIGH)
        sleep(0.5) #Print distance with 0.5 cm calibration
    else:
        GPIO.output(buzzer,GPIO.LOW)
        sleep(0.5)                 #display out of range

def button_callback(channel):
    global push
    doc_ref.document(u'1').set({u'toggle': push})
    print(push)
    if push==0:
        push=1
    else:
        push = 0
def dist(lat2,lon2):
    global i
    global lon1
    global lat1
    if i==1:
        lon1 = radians(lon2)
        lat1 = radians(lat2)
        i=i+1
    lat2 = radians(lat2)
    lon2 = radians(lon2)
    dlon = lon2-lon1
    dlat = lat2-lat1
    a = sin(dlat/2)**2+cos(lat1)*cos(lat2)*sin(dlon/2)**2
    c = 2*atan2(sqrt(a),sqrt(1-a))
    dist=R*c
    return dist*1000
#Listen on port 2947 of gpsd
session = gps.gps("localhost", "2947")
session.stream(gps.WATCH_ENABLE | gps.WATCH_NEWSTYLE)
#a=23.8161612
count=1
#b=86.4390703
while True :
        rep = session.next()
        try :
            ultrasonic()
            if GPIO.input(11) == GPIO.HIGH:
                    GPIO.add_event_detect(11,GPIO.RISING,callback=button_callback)
            if (rep["class"] == "TPV") :
                print(str(rep.lat) + "," + str(rep.lon))
                #if (dist(rep.lat,rep.lon)>10) :

                print(dist(rep.lat,rep.lon))
                if (dist(rep.lat,rep.lon)>10) :
                    GPIO.output(buzzer,GPIO.HIGH)
                    doc_ref.document(u'0').set({u'latitude': rep.lat, u'longitude': rep.lon})
                #count=count+1
                
                    
                      
              
        except Exception as e :
            print("Got exception " + str(e))



