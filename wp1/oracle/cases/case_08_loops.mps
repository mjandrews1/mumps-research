	; case_08_loops — For forms, Quit, $Order over local array
	For i=1:1:5 Write i," "
	Write !
	For j=10:-2:0 Write j," "
	Write !
	Set k=0
	For  Set k=k+1 Quit:k>4  Write k," "
	Write !
	Kill l
	Set l(1)="a",l(3)="c",l(7)="g"
	Set sub=""
	For  Set sub=$Order(l(sub)) Quit:sub=""  Write sub,"=",l(sub)," "
	Write !
	Quit
