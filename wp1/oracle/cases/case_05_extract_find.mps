	; case_05_extract_find — $Extract, $Find (revised pre-freeze 2026-11-14 for MDH dialect:
	; indeterminate * subscripts unsupported; single-position $Extract returns one char)
	Set s="Interpreter"
	Write $Extract(s),!
	Write $Extract(s,1,5),!
	Write $Extract(s,4,8),!
	Write $Extract(s,4),!
	Write $Extract(s,32000),!
	Write $Find(s,"r"),!
	Write $Find(s,"e",5),!
	Quit
