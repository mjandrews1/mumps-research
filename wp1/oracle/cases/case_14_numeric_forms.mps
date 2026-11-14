	; case_14_numeric_forms — numeric canonicalization and edge cases
	Write 0,!,.5,!,007.250,!
	Set x="12abc"+0
	Write x,!
	Write ""+0,!
	Write $Justify(2/3,0,6),!
	Write 1>0,!,0>1,!
	Write "a"_""_""_"b",!
	Quit
