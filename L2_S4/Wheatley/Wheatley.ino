#include <EEPROM.h>
#include <Servo.h>
#include "Sonars.h"
#include "Move.h"
#include "Pos.h"
#include "Pince.h"

#define OBSTACLE_THRESHOLD 30

void setup() {
   Sonars::setup();
   Move::setup();
   Pos::setup();
   Pince::setup();

   Move::speed(80);
}

void loop() {
   bool leftClear = Sonars::frontLeft() > OBSTACLE_THRESHOLD;
   bool rightClear = Sonars::frontRight() > OBSTACLE_THRESHOLD;

   if (leftClear && rightClear) {
      Move::forward();
   }
   else if (leftClear) {
      Move::left();
   }
   else {
      Move::right();
   }
}
