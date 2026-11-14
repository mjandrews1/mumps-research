	; case_05_extract_find — $Extract, $Find
	Set s="Interpreter"
	Write $Extract(s),!,$Extract(s,1,5),!,$Extract(s,4,8),!,$Extract(s,*),!,$Extract(s,*-3,*),!
	Write $Find(s,"r"),!,$Find(s,"e",5),!
	Quit
