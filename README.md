# Elderly-Aid-Kit

## Description
Developed a technology-based solution for Senior citizens, especially abled
individual on any one who need supervision which can alarm the concerned person in
case person who need supervision is crossing a specified boundary for eg 10 meters
outside a specified boundary. We also think about more innovative ideas to see
what more value you can bring to these individuals for eg : devices which can apart
from keep tracking an individual can also collect heath parameters such as heart rate
and alert concerned person in case of any issue. This health data can be further used for
research and development of these individuals.

## PROPOSAL
A wearable device wore by the target person (here an old / blind person ) that gives a
warning to the target person himself as well as transmits an alert to the phone of the
person-in-charge ( person responsible for the target person) on encountering threats
such as theft or target person going out of the specified boundary , etc.

## TECHNOLOGY STACK
### Machine Learnng
- Object Recognition using YOLO model
- Face Recognition using Siamese Network
### Networking
- BOTO3 API to access AWS
### Hardware
- GPS Module
- Raspberry Pi 3B+
- Buzzer Module
- Pi-cam
### Android Application
- Android Studio
- Google Maps API

## IMPLEMENTATION
The device wore by the target person will contain Raspberry Pi 3 that receives live
video from the Picam and coordinates from the gps module which is transmitted to the
processing unit .This unit will run object recognition and face recognition on the input
stream and generates an output " threat" message with its details or "No threat"
message as well as generates the distance of the device from a fixed reference .If this
distance turns out to be more than desired result (i.e person is out of the desired
boundary ) ,then it will generate an appropriate message. These messages will get
transmitted back to the raspberry pi that is itself connected to the AWS Server.
Raspberry Pi will not only trigger the buzzer on receiving threat messages ,but also
transmit an appropriate alert (and its details) to the mobile application of the
concerned person via AWS Server.
![alt text](https://github.com/25abhishek/Elderly-Aid-Kit/blob/main/Images/implementation%20flow.png)
