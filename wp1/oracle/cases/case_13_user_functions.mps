	; case_13_user_functions — labels, Do with args, $$ extrinsic, Quit value
square(n)
	Quit n*n
sum3(a,b,c)
	Quit a+b+c
main
	Set r=$$square(6)
	Write r,!
	Write $$sum3(1,2,3),!
	Do report("test")
	Quit
report(msg)
	Write "report: ",msg,!
	Quit
