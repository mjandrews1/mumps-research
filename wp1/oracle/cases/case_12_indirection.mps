	; case_12_indirection — @ indirection, Xecute, argumentless Do
	Set vn="target"
	Set @vn=17
	Write @vn,!
	Set cmd="Write ""via xecute"",!"
	Xecute cmd
	Set e="1+2*3"
	Write @e,!
	Quit
