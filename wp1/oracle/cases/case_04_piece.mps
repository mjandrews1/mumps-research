	; case_04_piece — $Piece with delimiters and counts
	Set r="a,b,c,d,e"
	Write $Piece(r,",",1),!,$Piece(r,",",3),!,$Piece(r,",",*),!
	Write $Length(r,","),!
	Set r2=$Piece(r,",",2,4)
	Write r2,!,$Length(r2,","),!
	Write $Piece("x;y;z",";",0),!
	Quit
