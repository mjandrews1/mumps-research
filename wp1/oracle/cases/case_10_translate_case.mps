	; case_10_translate_case — $Translate, case conversion
	Write $Translate("aBcD","abcd","ABCD"),!
	Write $Translate("MUMPS ii","ABCDEFGHIJKLMNOPQRSTUVWXYZ","abcdefghijklmnopqrstuvwxyz"),!
	Write $Translate("strip-me","-",""),!
	Set s="Hello"
	Write $ZConvert(s,"U"),!,$ZConvert(s,"L"),!
	Quit
