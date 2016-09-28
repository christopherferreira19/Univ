#include <EEPROM.h>
#include <Servo.h>
#include "Pince.h"
#include "Arduino.h"

#define PIN_SERVO 5
#define SERVO_OPEN 0
#define SERVO_CLOSED 90

namespace Pince {
   uint8_t pos;
   Servo servo;
  
   void setup() {
      servo.attach(PIN_SERVO);
      open();
   }

   void open() {
      servo.write(SERVO_OPEN);
   }

   void close() {
      servo.write(SERVO_CLOSED);
   }
}
