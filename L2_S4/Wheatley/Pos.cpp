#include "Constants.h"
#include "Pos.h"
#include "Arduino.h"

#define PIN_A_LEFT  0
#define PIN_B_LEFT  1
#define PIN_A_RIGHT 3
#define PIN_B_RIGHT 2

#define BIT_A_LEFT  0b00000100
#define BIT_B_LEFT  0b00001000
#define BIT_A_RIGHT 0b00000001
#define BIT_B_RIGHT 0b00000010

namespace Pos {

   volatile int pos_left;
   volatile int pos_right;
   volatile boolean pos_update;

   float pos_x;
   float pos_y;
   float pos_angle;

   #define interrupt_encoder(pos, bitA, bitB) \
      if (PIND & bitA) { \
         if (PIND & bitB) { pos--; } \
         else { pos++; } \
      } \
      else if (PIND & bitB) { pos++; } \
      else { pos--; } \
      pos_update = true;


   void interrupt_left() {
      interrupt_encoder(pos_left, BIT_A_LEFT, BIT_B_LEFT);
   }

   void interrupt_right() {
      interrupt_encoder(pos_right, BIT_A_RIGHT, BIT_B_RIGHT);
   }

   void setup() {
      pos_left = 0;
      pos_right = 0;
      pos_update = false;
      pos_x = 0;
      pos_y = 0;
      pos_angle = M_PI / 2;

      pinMode(PIN_A_LEFT, INPUT_PULLUP);
      pinMode(PIN_A_RIGHT, INPUT_PULLUP);
      pinMode(PIN_B_LEFT, INPUT_PULLUP);
      pinMode(PIN_B_RIGHT, INPUT_PULLUP);

      attachInterrupt(digitalPinToInterrupt(PIN_A_LEFT), interrupt_left, CHANGE);
      attachInterrupt(digitalPinToInterrupt(PIN_A_RIGHT), interrupt_right, CHANGE);
   }

   void update() {
      cli();
      long left = pos_left;
      long right = pos_right;
      pos_left = 0;
      pos_right = 0;
      pos_update = false;
      sei();

      // Reference : http://rossum.sourceforge.net/papers/DiffSteer/
      float diff = right - left;
      if (diff == 0) {
         pos_x += left * cos(pos_angle);
         pos_y += left * sin(pos_angle);
      }
      else {
         float pos_angle0 = pos_angle;
         pos_angle += diff / (WHEELS_AXIS_RADIUS << 1);
         float turn_radius = WHEELS_AXIS_RADIUS * (left + right) / diff;
         float component = diff / (WHEELS_AXIS_RADIUS << 1) + pos_angle0;
         pos_x += turn_radius * (sin(component) - sin(pos_angle0));
         pos_y -= turn_radius * (cos(component) - cos(pos_angle0));
      }
   }

   float x() {
      while (pos_update) update();
      return pos_x;
   }

   float y() {
      while (pos_update) update();
      return pos_y;
   }

   float angle() {
      while (pos_update) update();
      return pos_angle;
   }
}
