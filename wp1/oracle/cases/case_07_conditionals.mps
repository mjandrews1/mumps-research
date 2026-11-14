	; case_07_conditionals — If with &, !, comparisons, $Data, $Get
	Set x=5
	If x>1&(x<10) Write "in range",!
	If x>1!(x>100) Write "or true",!
	If '(x<1) Write "not less",!
	Set (g,h)=0
	If $Data(g)=0 Write "g undefined",!
	Write $Get(undefined,99),!
	If x=5 Write "eq",!
	If x'=6 Write "neq",!
	Quit
