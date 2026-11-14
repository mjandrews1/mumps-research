	; case_09_globals — ^globals, $Order, $Query, $Data, Kill, Merge
	Kill ^orc
	Set ^orc("x")=1,^orc("y")=2,^orc("z")=3
	Set ^orc("sub","a")=10,^orc("sub","b")=20
	Set sub=""
	For  Set sub=$Order(^orc(sub)) Quit:sub=""  Write sub,"->",^orc(sub)," "
	Write !
	Write $Data(^orc("x"))," ",$Data(^orc("sub")),!
	Write $Query(^orc(""))," ",$Query(^orc("x")),!
	Kill ^orc("y")
	Write $Get(^orc("y"),"gone"),!
	Merge ^orc2("copy")=^orc("sub")
	Write ^orc2("copy","b"),!
	Kill ^orc,^orc2
	Quit
