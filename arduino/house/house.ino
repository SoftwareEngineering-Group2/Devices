#include <Servo.h>
#include <Wire.h>
#include <LiquidCrystal_I2C.h> // LiquidCrystal_I2C library
 
LiquidCrystal_I2C lcd(0x27, 16, 2); // 0x27 is the i2c address of the LCM1602 IIC v1 module (might differ)

int input_array[8];  // state array for sending from java to arduino

int output_array[8] = {0, 0, 0, 0, 0, 0, 0, 0};

Servo DOOR;
Servo WINDOW;

#define BUZZER 3
#define YELLOW_LED 5
#define FAN_BACKWARD 6
#define FAN_FORWARD 7
//#define DOOR 9
//#define WINDOW 10
#define WHITE_LED 13

#define GAS_SENSOR A0
#define LIGHT_SENSOR A1
#define STEAM_SENSOR A2
#define SOIL_SENSOR A3
#define MOTION_SENSOR 2
#define LEFT_BUTTON 4
#define RIGHT_BUTTON 8

int gas_sensor_val = 0;
int light_sensor_val = 0;
int steam_sensor_val = 0;
int soil_sensor_val = 0;
int motion_sensor_val = 0;

int ultimate_flag = 0;

byte sensor_byte = 0;

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
  pinMode(FAN_BACKWARD, OUTPUT);
  pinMode(FAN_FORWARD, OUTPUT);
  pinMode(WHITE_LED, OUTPUT);

  pinMode(GAS_SENSOR, INPUT);
  pinMode(LIGHT_SENSOR, INPUT);
  pinMode(STEAM_SENSOR, INPUT);
  pinMode(SOIL_SENSOR, INPUT);
  pinMode(MOTION_SENSOR, INPUT);



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
      ByteToArray(incomingByte, input_array);
      
      for (i=0; i < 8; ++i) {
        lcd.print(input_array[i]); // print out the retrieved value to the second row
        lcd.setCursor(i + 1, 1);
      }
      
    }
  }

  check_sensors();

}

void ByteToArray( byte b, int arr[8] )
{
  byte i;
  
  for ( i=0; i < 8; ++i )
  {
    arr[i] = b & 1;
    b = b >> 1;
  }

  bit_parser(arr);

}

byte convertToByte(int data[8]) {
  byte resultByte = 0;
  for (int i = 0; i < 8; i++) {
    resultByte |= (data[i] & 0x01) << (7 - i);
  }
  return resultByte;
}

void bit_parser(int arr[8]) {
  if(arr[0] == 1) {
    digitalWrite(BUZZER, HIGH);
  } else {
    digitalWrite(BUZZER, LOW);
  }
  if(arr[1] == 1) {
    digitalWrite(YELLOW_LED, HIGH);
  } else {
    digitalWrite(YELLOW_LED, LOW);
  }
  if(arr[2] == 1) {
    digitalWrite(FAN_FORWARD, HIGH);
  } else {
    digitalWrite(FAN_FORWARD, LOW);
  }
  if(arr[3] == 1) {
    digitalWrite(FAN_BACKWARD, HIGH);
  } else {
    digitalWrite(FAN_BACKWARD, LOW);
  }
  if(arr[4] == 1) {
    DOOR.write(360);
  } else {
    DOOR.write(0);
  }
  if(arr[5] == 1) {
   WINDOW.write(360);
  } else {
    WINDOW.write(0);
  }
  if(arr[6] == 1) {
    digitalWrite(WHITE_LED, HIGH);
  } else {
    digitalWrite(WHITE_LED, LOW);
  }

}

void check_sensors() {

  ultimate_flag = 0;

  gas_sensor_val = analogRead(GAS_SENSOR);

  if(gas_sensor_val > 100 && output_array[0] == 0) {
    output_array[0] = 1;
    ultimate_flag = 1;
  }
    if(gas_sensor_val < 100 && output_array[0] == 1) {
    output_array[0] = 0;
    ultimate_flag = 1;
  }

  light_sensor_val = analogRead(LIGHT_SENSOR);

  if(light_sensor_val <= 100 && output_array[1] == 0) {
    output_array[1] = 1;
    ultimate_flag = 1;
  }
    if(light_sensor_val > 100 && output_array[1] == 1) {
    output_array[1] = 0;
    ultimate_flag = 1;
  }

  steam_sensor_val = analogRead(STEAM_SENSOR);

  if(steam_sensor_val >= 200 && output_array[2] == 0) {
    output_array[2] = 1;
    ultimate_flag = 1;
  }
    if(steam_sensor_val < 200 && output_array[2] == 1) {
    output_array[2] = 0;
    ultimate_flag = 1;
  }


  soil_sensor_val = analogRead(SOIL_SENSOR);

  if(soil_sensor_val >= 200 && output_array[3] == 0) {
    output_array[3] = 1;
    ultimate_flag = 1;
  }
    if(soil_sensor_val < 200 && output_array[3] == 1) {
    output_array[3] = 0;
    ultimate_flag = 1;
  }

  motion_sensor_val = digitalRead(MOTION_SENSOR);

  if(motion_sensor_val == 1 && output_array[4] == 0) {
    output_array[4] = 1;
    ultimate_flag = 1;
  }
    if(motion_sensor_val == 0 && output_array[4] == 1) {
    output_array[4] = 0;
    ultimate_flag = 1;
  }


  if(ultimate_flag == 1) {

    sensor_byte = convertToByte(output_array);
    Serial.write(sensor_byte);
    ultimate_flag = 0;

  }


}