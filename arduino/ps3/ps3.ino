/*
Based on Adafruit Arduino - Lesson 15. Bi-directional Motor
*/

int switchPin = 0;
int in1Pin = 3; // pin 2 on L293D
int in2Pin = 4; // pin 7 on L293D
int enablePin = 9; // pin 1 on L293D

int steering = 1; // steering input from HID device (PS3 controller).
int speed = 0; // starting speed
boolean reverse = 0; // start out in neutral

void setup()
{
  pinMode(in1Pin, OUTPUT);
  pinMode(in2Pin, OUTPUT);
  pinMode(enablePin, OUTPUT);
  Serial.begin(9600); // Serial to node for HID input.
}

void setMotor(int speed, boolean reverse)
{
  analogWrite(enablePin, speed);
  digitalWrite(in1Pin, ! reverse);
  digitalWrite(in2Pin, reverse);
}
 
void loop()
{
  if(Serial.available() > 0) //Only move the car if a serial connection (from node) is available.
  {
    steering = Serial.read();
    Serial.println(steering);
    
    if(steering == 48) // 0 - centered
    {
      speed = 0;
      reverse = 0;
    }
    
    if(steering == 51) // 3 - up
    {
      speed = 100;
      reverse = 0;
    }
    
    if(steering == 52) // 4 - down
    {
      speed = 100;
      reverse = 1;
    }
  
    setMotor(speed, reverse);
//    delay(2000);
  }
}
 

