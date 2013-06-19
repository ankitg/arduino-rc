// Dependencies //
var HID = require('node-hid')
,   serialport = require('serialport')
,   SerialPort = serialport.SerialPort
;


// Constants //
// Calibrated for the left joystick.
var L_LOWERX = 113;
var L_UPPERX = 143;
var L_LOWERY = 116;
var L_UPPERY = 139;

// Calibrated for the right joystick.
var R_LOWERX = 109;
var R_UPPERX = 140;
var R_LOWERY = 115;
var R_UPPERY = 138;


// Setup the serial port (for output) //
// var sp1 = new SerialPort('/dev/tty.usbmodemfa131', { // Leonardo
// var sp1 = new SerialPort('/dev/tty.usbmodemfd121', { // UNO
// var sp1 = new SerialPort('/dev/tty.usbserial-A900cfap', { // Duemilanove
var sp1 = new SerialPort('/dev/tty.FireFly-CC01-SPP', { // Firefly over Bluetooth
 parser: serialport.parsers.readline("\n")
});

// Serial port debugging //
 sp1.on('data', function(data) {
   console.log(data);
 });

// First find the PS3 Controller //
var devicePath;
var devices = HID.devices();
devices.forEach(function(device) {
  if(device.manufacturer === 'Sony') {
    devicePath = device.path;
  }
});

// Start reading the controller data //
var conrtoller = new HID.HID(devicePath);
conrtoller.read(dataRead);


var rJoyX, rJoyY, rJoyDirection, lJoyX, lJoyY, lJoyDirection;
function dataRead(err, data) {
  if(err) throw err;
  else { 
    lJoyX = data[6]; // Left joystick x-axis
    lJoyY = data[7]; // Left joystick y-axis
    rJoyX = data[8]; // Right joystick x-axis
    rJoyY = data[9]; // Right joystick y-axis


//    console.log(rJoyX + ", " + rJoyY); // For debugging and calibration.

    if(rJoyX > R_LOWERX && rJoyX < R_UPPERX && rJoyY > R_LOWERY && rJoyY < R_UPPERY)
    {
      sp1.write("0"); // 48
      //console.log("Centered");
      rJoyDirection = "Centered";
    }
    else if(rJoyX < R_LOWERX && rJoyY > R_LOWERY && rJoyY < R_UPPERY)
    {
      sp1.write("1"); // 49
      //console.log("Left");
      rJoyDirection = "Left";
    }
    else if(rJoyX > R_UPPERX && rJoyY > R_LOWERY && rJoyY < R_UPPERY)
    {
      sp1.write("2"); // 50
      //console.log("Right");
      rJoyDirection = "Right";
    }
    else if(rJoyX > R_LOWERX && rJoyX < R_UPPERX && rJoyY < R_LOWERY)
    {
      sp1.write("3"); // 51
      // console.log("Up");
      rJoyDirection = "Up";
    }
    else if(rJoyX > R_LOWERX && rJoyX < R_UPPERX && rJoyY > R_UPPERY)
    {
      sp1.write("4"); // 52
      // console.log("Down");
      rJoyDirection = "Down";
    }
    else if(rJoyX > R_UPPERX && rJoyY > R_UPPERY)
    {
      sp1.write("5"); // 53
      // console.log("Down + Right");
      rJoyDirection = "Down + Right";
    }
    else if(rJoyX > R_UPPERX && rJoyY < R_LOWERY)
    {
      sp1.write("6"); // 54
      // console.log("Up + Right");
      rJoyDirection = "Up + Right";
    }
    else if(rJoyX < R_LOWERX && rJoyY > R_UPPERY)
    {
      sp1.write("7"); // 55
      // console.log("Down + Left");
      rJoyDirection = "Down + Left";
    }
    else if(rJoyX < R_LOWERX && rJoyY < R_LOWERY)
    {
      sp1.write("8"); // 56
      // console.log("Up + Left");
      rJoyDirection = "Up + Left";
    }


    if(lJoyX > L_LOWERX && lJoyX < L_UPPERX && lJoyY > L_LOWERY && lJoyY < L_UPPERY)
    {
      //sp1.write("0");
      // console.log("Centered");
      lJoyDirection = "Centered";
    }
    else if(lJoyX < L_LOWERX && lJoyY > L_LOWERY && lJoyY < L_UPPERY)
    {
      // console.log("Left");
      lJoyDirection = "Left";
    }
    else if(lJoyX > L_UPPERX && lJoyY > L_LOWERY && lJoyY < L_UPPERY)
    {
      // console.log("Right");
      lJoyDirection = "Right";
    }
    else if(lJoyX > L_LOWERX && lJoyX < L_UPPERX && lJoyY < L_LOWERY)
    {
      // console.log("Up");
      lJoyDirection = "Up";
    }
    else if(lJoyX > L_LOWERX && lJoyX < L_UPPERX && lJoyY > L_UPPERY)
    {
      // console.log("Down");
      lJoyDirection = "Down";
    }
    else if(lJoyX > L_UPPERX && lJoyY > L_UPPERY)
    {
      // console.log("Down + Right");
      lJoyDirection = "Down + Right";
    }
    else if(lJoyX > L_UPPERX && lJoyY < L_LOWERY)
    {
      // console.log("Up + Right");
      lJoyDirection = "Up + Right";
    }
    else if(lJoyX < L_LOWERX && lJoyY > L_UPPERY)
    {
      // console.log("Down + Left");
      lJoyDirection = "Down + Left";
    }
    else if(lJoyX < L_LOWERX && lJoyY < L_LOWERY)
    {
      // console.log("Up + Left");
      lJoyDirection = "Up + Left";
    }

//    console.log("LeftJoy: " + lJoyDirection + " || RightJoy: " + rJoyDirection);

    // keep reading!
    conrtoller.read(dataRead);
  }
}
