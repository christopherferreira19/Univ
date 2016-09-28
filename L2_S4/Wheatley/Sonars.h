#ifndef __SONARS_H__
#define __SONARS_H__

#include "Arduino.h"

namespace Sonars {
   void setup();

   word frontLeft();
   word frontRight();
   word sideLeft();
   word sideRight();
}

#endif
