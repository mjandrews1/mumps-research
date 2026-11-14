	; case_10_translate_case — $Translate, case conversion
	; (revised pre-freeze 2026-11-14: $ZConvert not present in this build;
	; case conversion expressed via $Translate)
	Write $Translate("aBcD","abcd","ABCD"),!
	Write $Translate("MUMPS ii","ABCDEFGHIJKLMNOPQRSTUVWXYZ","abcdefghijklmnopqrstuvwxyz"),!
	Write $Translate("strip-me","-",""),!
	Set s="Hello"
	Write $Translate(s,"abcdefghijklmnopqrstuvwxyz","ABCDEFGHIJKLMNOPQRSTUVWXYZ"),!
	Write $Translate(s,"ABCDEFGHIJKLMNOPQRSTUVWXYZ","abcdefghijklmnopqrstuvwxyz"),!
	Quit
