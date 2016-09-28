#include "Sonars.h"
#include <avr/interrupt.h>

// {Pin{Trigger,Echo},BitEcho}_{Front,Side}{Left,Right}
#define PT_FL 5
#define PT_FR 12
#define PT_SL A2
#define PT_SR A0

#define PE_FL 10
#define PE_FR 11
#define PE_SL 15
#define PE_SR 14

#define BE_FL 0b01000000
#define BE_FR 0b10000000
#define BE_SL 0b00000010
#define BE_SR 0b00001000

// L'ATmega32u4 tourne à une fréquence de 16MHz, un timer avec un prescaler de
// 64 compte donc toutes les 4uS (= 1/(16*10^6 / 64)).
#define TIMER_CONFIG_A 0b00000000
#define TIMER_CONFIG_B 0b00000011 // Prescaler 64

// Le compteur étant sur 16bits on peut compter jusqu'à 65536, soit une période
// de 262,144 millisecondes.
// On veut une impulsion d'au minimum 10uS sur le pin trigger des sonars et
// ensuite attendre entre 200mS et 500mS avant de recommencer pour éviter que
// le signal de la mesure interfère avec la mesure suivante.
// On active donc en mode de timer normal les interruptions d'overflow
// & de comparaison avec A :
#define TIMER_MASK 0b00000011

// On divise ainsi notre période en 2 :
//  - 4 comptes (~16uS) durant lesquelles le signal du trigger sera haut
//  - 65532 comptes (~262mS) pour lesquelles il sera bas
#define TIMER_TRIGGER_COUNT 4

namespace Sonars {

   // Les quatres variables contenants les dernières mesures.
   volatile word front_left = 0;
   volatile word front_right = 0;
   volatile word side_left = 0;
   volatile word side_right = 0;

   // On alterne les mesures entre les capteurs de devant pour éviter qu'ils
   // interfèrent entre eux
   volatile boolean front = 1;

   ISR(TIMER3_COMPA_vect) {
      front = !front;
      digitalWrite(front ? PT_FL : PT_FR, LOW);
      digitalWrite(PT_SL, LOW);
      digitalWrite(PT_SR, LOW);
   }

   ISR(TIMER3_OVF_vect) {
      digitalWrite(front ? PT_FL : PT_FR, HIGH);
      digitalWrite(PT_SL, HIGH);
      digitalWrite(PT_SR, HIGH);
   }

   // Les pin change interrupts n'indiquent pas quelles pin ont changées, on
   // est donc obligé de sauvegarder l'état précédent des pins pour pouvoir le
   // determiner.
   uint8_t old_PINB = 0xFF;

   // Les variables qui contiennent les différents débuts d'echo pour faire la
   // mesure de la différence de temps.
   long front_echo_start = 0;
   long side_left_echo_start = 0;
   long side_right_echo_start = 0;


   ISR(PCINT0_vect) {
      uint8_t diff = PINB ^ old_PINB;
      old_PINB = PINB;

      #define pc_interrupt_sonar(bit, pin, echo_start, distance) \
         if (diff & bit) { \
            if (old_PINB & bit) { \
               echo_start = micros(); \
            } \
            else if (echo_start > 0) { \
               distance = (micros() - echo_start) / 58; \
               echo_start = 0; \
            } \
         }

      if (front) {
         pc_interrupt_sonar(BE_FL, PE_FL, front_echo_start, front_left);
      }
      else {
         pc_interrupt_sonar(BE_FR, PE_FR, front_echo_start, front_right);
      }
      pc_interrupt_sonar(BE_SL, PE_SL, side_left_echo_start, side_left)
      pc_interrupt_sonar(BE_SR, PE_SR, side_right_echo_start, side_right)
   }

   void setup() {
      pinMode(PT_FL, OUTPUT);
      pinMode(PT_FR, OUTPUT);
      pinMode(PT_SL, OUTPUT);
      pinMode(PT_SR, OUTPUT);
      pinMode(PE_FL, INPUT_PULLUP);
      pinMode(PE_FR, INPUT_PULLUP);
      pinMode(PE_SL, INPUT_PULLUP);
      pinMode(PE_SR, INPUT_PULLUP);

      cli();
      // Configuration du timer
      TCCR3A = TIMER_CONFIG_A;
      TCCR3B = TIMER_CONFIG_B;
      TIMSK3 = TIMER_MASK;
      OCR3A = TIMER_TRIGGER_COUNT;
      // Configuration des pin change interrupts
      PCMSK0 = BE_FL | BE_FR | BE_SL | BE_SR;
      PCICR =  0b00000001;
      sei();
   }

   word frontLeft() {
      return front_left;
   }

   word frontRight() {
      return front_right;
   }

   word sideLeft() {
      return side_left;
   }

   word sideRightDistance() {
      return side_right;
   }
}
