#include <Servo.h>
#include <Wire.h>
#include <LiquidCrystal_I2C.h> // LiquidCrystal_I2C library
 
LiquidCrystal_I2C lcd(0x27, 16, 2); // 0x27 is the i2c address of the LCM1602 IIC v1 module (might differ)

int variable[8];

Servo DOOR;
Servo WINDOW;

#define BUZZER 3
#define YELLOW_LED 5
#define FAN_BACKWARD 6
#define FAN_FORWARD 7
//#define DOOR 9
//#define WINDOW 10
#define WHITE_LED 13

void setup() {
  //byte b = 13;
  lcd.init();
  lcd.backlight(); // turns on the backlight
  //lcd.clear(); 

  Serial.begin(9600);
  while (!Serial) {
    ; // wait for serial port to connect.
  }

  lcd.setCursor(0, 0); // set cursor to first row
  lcd.print("Init done"); // print to lcd
 
  DOOR.attach(9);
  WINDOW.attach(10);

  pinMode(BUZZER, OUTPUT);
  pinMode(YELLOW_LED, OUTPUT);
  //pinMode(WHITE_LED, OUTPUT);
  //pinMode(WHITE_LED, OUTPUT);
  //pinMode(DOOR, OUTPUT);
  //pinMode(WINDOW, OUTPUT);
  pinMode(WHITE_LED, OUTPUT);
}

void loop() {
  int i;
  if (Serial.available() > 0) {    
    byte incomingByte = 0;
    incomingByte = Serial.read(); // read the incoming byte:
    if (incomingByte != -1) { // -1 means no data is available

      lcd.setCursor(0, 0); // set cursor to first row
      lcd.print("I received: "); // print out to LCD
      lcd.setCursor(0, 1); // set cursor to secon row
      for (i=0; i < 8; ++i) {
        //lcd.print(variable[i]); // print out the retrieved value to the second row
        //lcd.setCursor(i + 1, 0);
      }
      ByteToArray(incomingByte, variable);
    }
  }
}

void ByteToArray( byte b, int variable[8] )
{
  byte i;
  
  for ( i=0; i < 8; ++i )
  {
    variable[i] = b & 1;
    b = b >> 1;
  }

  bit_parser(variable);

  /*if(variable[6] == 1) {
    thing_on(13);
  }*/
}

void bit_parser(int variable[8]) {
  if(variable[0] == 1) {
    digitalWrite(BUZZER, HIGH);
  } else {
    digitalWrite(BUZZER, LOW);
  }
  if(variable[1] == 1) {
    digitalWrite(YELLOW_LED, HIGH);
  } else {
    digitalWrite(YELLOW_LED, LOW);
  }
  /*if(variable[2] == 1) {
    digitalWrite(YELLOW_LED, HIGH)
  } else {
    digitalWrite(YELLOW_LED, LOW)
  }
  if(variable[3] == 1) {
    digitalWrite(YELLOW_LED, HIGH)
  } else {
    digitalWrite(YELLOW_LED, LOW)
  }*/
  if(variable[4] == 1) {
    DOOR.write(360);
  } else {
    DOOR.write(0);
  }
  if(variable[5] == 1) {
   WINDOW.write(360);
  } else {
    WINDOW.write(0);
  }
  if(variable[6] == 1) {
    digitalWrite(WHITE_LED, HIGH);
  } else {
    digitalWrite(WHITE_LED, LOW);
  }

}

void thing_on(byte pin) {

  digitalWrite(pin, HIGH);
}