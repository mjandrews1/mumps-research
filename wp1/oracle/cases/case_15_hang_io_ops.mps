	; case_15_hang_io_ops — $Horolog-free deterministic IO ops (Open/Use/Close omitted: env-dependent)
	Set (a,b)=100
	Write a," ",b,!
	New a
	Set a=5
	Write "new a=",a,!
	Quit
