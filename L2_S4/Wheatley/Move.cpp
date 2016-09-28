#include "Move.h"
#include "Arduino.h"

#define PIN_CTRL1_LEFT 7
#define PIN_CTRL1_RIGHT 4
#define PIN_CTRL2_LEFT 8
#define PIN_CTRL2_RIGHT 9
#define PIN_SPEED_LEFT 6
#define PIN_SPEED_RIGHT 13

namespace Move {

   void setup() {
      pinMode(PIN_CTRL1_LEFT, OUTPUT);
      pinMode(PIN_CTRL1_RIGHT, OUTPUT);
      pinMode(PIN_CTRL2_LEFT, OUTPUT);
      pinMode(PIN_CTRL2_RIGHT, OUTPUT);
      pinMode(PIN_SPEED_LEFT, OUTPUT);
      pinMode(PIN_SPEED_RIGHT, OUTPUT);

      stop();
      speed(0);
   }

   void speed(int speed) {
      analogWrite(PIN_SPEED_LEFT, speed);
      analogWrite(PIN_SPEED_RIGHT, speed);
   }

   void forward() {
      digitalWrite(PIN_CTRL1_LEFT, HIGH);
      digitalWrite(PIN_CTRL1_RIGHT, LOW);
      digitalWrite(PIN_CTRL2_LEFT, LOW);
      digitalWrite(PIN_CTRL2_RIGHT, HIGH);
   }

   void left() {
      digitalWrite(PIN_CTRL1_LEFT, LOW);
      digitalWrite(PIN_CTRL1_RIGHT, HIGH);
      digitalWrite(PIN_CTRL2_LEFT, LOW);
      digitalWrite(PIN_CTRL2_RIGHT, HIGH);
   }

   void right() {
      digitalWrite(PIN_CTRL1_LEFT, HIGH);
      digitalWrite(PIN_CTRL1_RIGHT, LOW);
      digitalWrite(PIN_CTRL2_LEFT, HIGH);
      digitalWrite(PIN_CTRL2_RIGHT, LOW);
   }

   void stop() {
      digitalWrite(PIN_CTRL1_LEFT, LOW);
      digitalWrite(PIN_CTRL1_RIGHT, LOW);
      digitalWrite(PIN_CTRL2_LEFT, LOW);
      digitalWrite(PIN_CTRL2_RIGHT, LOW);
   }
}
