package doc

// Unit 206 (B7 tranche 3): compiler-mumps.c::<misc> (batch 7,
// complex stratum; units.csv row 206: loc 57, score 29.3). The
// file-residual chunk: the GPL banner and the include set are
// the ledger preamble (CopyrightMisc unit-1 declaration-only
// precedent), and the two sub-roster functions of the residual
// are EP (compiler-mumps.c:1978, the subfunction entry-point
// router; the compiler-globals-ext ledger :96 seat) and
// LevCheck (the :>1 Do-return exit-probe). EP renders: flg
// non-zero emits the _epilogue goto and _eps router label, walks
// the LabelsStart TxtEntry table emitting one if-goto per label
// (the strcmp against the labeled entry-point offset), and the
// entry-point-not-found ErrorMessage emission; the vendor quirk
// kept verbatim: the epi(ExecName,pid) call and MAIN_flg=0
// execute UNCONDITIONALLY - the closing brace of the if (flg)
// block precedes them, so every EP call terminates the previous
// function regardless of flg (H-013 shape note, not revived).
// epi is the unit-213 owner (Epi.kt, this tranche); Out is the
// CompilerState seat (H-004); MAIN_flg the abrtx extern seat.
// H-010: flg int; the short ExecName param and long pid render
// Int/Long; LevCheck's short params render Int with the
// .toInt() reads on the ShortArray DoRet carrier. The EP
// prototype is consumed by this render (the :96 ledger flips to
// LANDED). Naming per H-017 (EP, LevCheck verbatim).

// include ledger (vendor residual chunk):
//   stdio.h/fcntl.h/stdlib.h/string.h/ctype.h/time.h/sys/stat.h
//                    JVM console/file IO and runtime, per-unit
//   mumpsc/memsize.h  the memsize constants ride their owning
//                    units (stateVector sizes, CompilerState
//                    seats; the EpiMisc unit-63 precedent)
//   mumpsc/compiler-macros.h  macro expansions inline at their
//                    owners (Sysout -> CompilerState.emit, H-002)
//   mumpsc/compiler-globals.h the CompilerState object carrier
//                    (H-004)
private val includeLedger206 = listOf(
    "stdio", "fcntl", "stdlib", "string", "ctype", "time", "sys/stat",
    "memsize.h", "compiler-macros.h", "compiler-globals.h",
)

// void EP(int flg, short ExecName, long pid) - subfunction entry
// point router; the epi call and MAIN_flg reset run on every
// call (vendor brace shape, quirk ledger above).
fun EP(flg: Int, ExecName: Int, pid: Long) {
    if (flg != 0) {
        CompilerState.Out.write("\tgoto _epilogue;\n")
        CompilerState.Out.write("_eps:\tif (strlen((const char *) offset)==0) goto _fcn;\n")
        if (CompilerState.LabelsStart != null) {
            var p1 = CompilerState.LabelsStart
            while (p1 != null) {
                CompilerState.Out.write(
                    "\tif (strcmp((const char *) offset,(const char *) \"" +
                        p1.name + "\")==0) goto " + p1.name + ";\n")
                p1 = p1.next
            }
        }
        CompilerState.Out.write(
            "\tErrorMessage(\"Error - entry point label not found\", svPtr->LineNumber);\n")
    }
    epi(ExecName, pid) //* terminate prev function
    CompilerState.MAIN_flg = 0 //* not in main function
}

// int LevCheck(short ExecName, short DoRetX, short DoRet[]) -
// returns 1 unless the ExecName is already in the Do-return set.
fun LevCheck(ExecName: Int, DoRetX: Int, DoRet: ShortArray): Int {
    var i = 0
    while (i < DoRetX) {
        if (DoRet[i].toInt() == ExecName) return 0
        i++
    }
    return 1
}
