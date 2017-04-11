#include "syscall.h"

#define CMD_MAX_SIZE 128

int main () {
	char cmd[CMD_MAX_SIZE];
	npid_t npid;
	int exitStatus;

	for (;;) {
		PutChar('>'); PutChar(' ');
		GetString(cmd, CMD_MAX_SIZE);
		for (int i = 0; i < CMD_MAX_SIZE; i++) {
			if (cmd[i] == '\n') {
				cmd[i] = '\0';
				break;
			}
		}

		npid = ForkExec (cmd);
		if (npid == FORK_EXEC_ERROR) {
			PutString("Error while launching the program");
		}
		else {
			WaitProcess (npid, &exitStatus);

			PutInt(exitStatus);
			PutString(" exit code !");
		}
	}
}
