#include "msg.h"

Msg_Type msg_type = NONE;

void init_msg(Msg_Type type) {
    msg_type = type;
}
