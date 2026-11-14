	; case_13_user_functions — labels, Do with args, Quit
	; (revised pre-freeze 2026-11-14: $$ in this dialect is an external
	; routine-file call (observed E26 File error), not a local extrinsic;
	; the case now covers Do-based subroutines only)
	Do report("test")
	Quit
report(msg)	Write "report: ",msg,!
	Quit
