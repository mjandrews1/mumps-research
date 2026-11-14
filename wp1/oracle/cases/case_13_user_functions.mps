	; case_13_user_functions — labels, Do with args, $$ extrinsic, Quit value
	; (revised pre-freeze 2026-11-14: main code must precede labels in interpreted mode)
	Write $$square(6),!
	Do report("test")
	Quit
square(n)	Quit n*n
report(msg)	Write "report: ",msg,!
	Quit
