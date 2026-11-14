	; case_11_pattern — ? pattern matching operator
	Set s="Mumps II"
	If s?1"Mumps".1" ".1"II" Write "ok1",!
	Set n="12345"
	If n?5N Write "numeric5",!
	If "abc"?3L Write "lower3",!
	If "a1b2"?4UN Write "mixed-UN pattern test (vendor semantics)",!
	If "  x"?1" ".1E Write "ok2",!
	Quit
