### DAF B4 T1 gate close (2026-11-21, m1-build, Mark)

Splice note: master restored byte-exact at bc3b366 (the ARM/PILOT
clobber from the 9fc534f5 mojibake-add repair; both git blob SHAs
verified pre-push), DOC B7T3 journal spliced at b909dc3 (fragment
106 lines, journal 4356). DAF m1-build gate: dafny 4.11.0+fcb2042
matches; gradle --no-daemon build BUILD SUCCESSFUL in 10s; dafny
verify initially 12 verified / 0 errors but 2 warnings (exit
nonzero) - fixed in 847c5d2 (parenthesized tableWellFormed
implication chain in Escan.dfy; {:axiom} attribute on the declared
asin-range assumption in mps_asin.dfy; the ARM/PILOT restore +
splice + gate-close sequence recorded here per the INFRA
append-locally precedent). Post-fix verify: 12 verified, 0 errors,
0 warnings, exit 0, both spec blob SHAs byte-verified on master.
DAF B4 tranche 1 is CLOSED: 10/10 units rendered, gate green.
