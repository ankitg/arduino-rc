/*
References:
Adafruit Arduino - Lesson 15. Bi-directional Motor
*/

// First motor for forward and backward movement.
int in1Pin = 3; // pin 2 on L293D
int in2Pin = 4; // pin 7 on L293D
int enablePin = 9; // pin 1 on L293D

// Second (direction) motor. Left and right.
int in1DPin = 11; // pin 15 on L293D
int in2DPin = 12; // pin 10 on L293D
int enableDPin = 10; // pin 9 on L293D

// Variables for input from HID.
int steering = 1; // steering input from HID device (PS3 controller).
int speed = 0; // starting speed
boolean reverse = 0; // start out in neutral

boolean isTurning = 0; // start out not turning
boolean direction = 0; // turning direction

void setup()
{
  pinMode(in1Pin, OUTPUT);
  pinMode(in2Pin, OUTPUT);
  pinMode(enablePin, OUTPUT);
  pinMode(in1DPin, OUTPUT);
  pinMode(in2DPin, OUTPUT);
  pinMode(enableDPin, OUTPUT);
  Serial.begin(115200); // Serial to node for HID input.
}

void setMotor(int speed, boolean reverse)
{
  analogWrite(enablePin, speed);
  digitalWrite(in1Pin, ! reverse);
  digitalWrite(in2Pin, reverse);
}

void setDirectionMotor(boolean direction)
{
  analogWrite(enableDPin, 255);
  digitalWrite(in1DPin, ! direction);
  digitalWrite(in2DPin, direction);
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
      isTurning = 1; // Set to 1, so turning direction is also set to center.
    }
    
    if(steering == 51) // 3 - up
    {
      speed = 255;
      reverse = 0;
      isTurning = 0;
    }
    
    if(steering == 52) // 4 - down
    {
      speed = 255;
      reverse = 1;
      isTurning = 0;
    }
    
    if(steering == 54) // 6 - up+right
    {
      speed = 200;
      reverse = 0;
      isTurning = 1;
      direction = 1;
    }
    
    if(steering == 56) // 8 - up+left
    {
      speed = 200;
      reverse = 0;
      isTurning = 1;
      direction = 0;
    }

    setMotor(speed, reverse);
    if(isTurning)
    { setDirectionMotor(direction); }
  }
}
