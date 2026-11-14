	; case_03_strings — concatenation, comparison operators
	Set s="Mumps"
	Set t=s_" II"
	Write t,!
	Write "len=",$Length(t),!
	If s]"" Write "s non-empty",!
	If s'="" Write "s ne empty",!
	If "Mumps"[s Write "contains",!
	If s?1"Mumps" Write "pattern follows",!
	If t']"" Write "t not follows",!
	Quit
