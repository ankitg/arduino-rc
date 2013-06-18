/*

Got this code from http://www.instructables.com/id/Arduino-Bluetooth-Serial-Connections-I-made-it-/step2/Arduino-Side-Setup/

Setup.
Upload this to board without the modem attached
-Make sure the baud rate is 115200 as that is what the bluetooth is (and serial port)
*/


int counter = 0;
int incomingByte;

void setup() {
  Serial.begin(115200); 
}

void loop() {
  // see if there's incoming serial data:
  if (Serial.available() > 0) {
    // read the oldest byte in the serial buffer:
    incomingByte = Serial.read();
    // if we recieve a capital R, reset the counter
    if (incomingByte == 'R') {
      Serial.println("RESET");
      counter=0;
    }
  }
 
  //only count to 100 then stop aka don't count to infinity
  if(counter<100){
  Serial.println(counter); //displays the ACSII number/letter
  //Serial.write(counter); //writes the data AS IS
  counter++;
  delay(250);
  } 
//  else {
//    counter = 0;
//  }

}
