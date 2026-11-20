package doc

// Unit 203 (B7 tranche 2): compiler-parse.c::Extract (batch 7, core
// stratum; LOC 30, score 24.2, rank 186; vendor
// compiler-parse.c:2466, physically verified this session; nonblank
// 30 matches the roster row) - the function-argument list emitter of
// the compiler's abutment (the a,b,c arg parser that follows unit
// names). Consult H-007 hit-adapt: the vendor's FILE-scope pointer
// pair SSS/TTT (compiler-parse.c:180 unsigned char *SSS,*TTT;) is
// only ever used as the shift pointers of THIS squeeze - the
// pointer-into-buffer role renders as the local index walk below
// (no H-004 holder raised; the seat decision ledgered here, the
// SSS sdlim walk of compiler-parse.c:198/:418 is the OTHER user of
// the pair and renders with its own unit when it lands). v1d is
// the caller's carrier (the parse buffer), bp the out-parameter of
// the tmp-temp name (H-007 mutable scratch). Dead local kept per
// H-013: the vendor int r is never read.
//
// SEAT REUSE (H-014 notes): memcheck is the compiler-parse.c:2068
// stub (below-roster, not a corpus unit); Out is the CompilerState
// writer with the fprintf char-count return through the H-003
// emit member (return ignored at this vendor call site, ledger
// note); iiitmp is the CompilerState counter seat; Extract2 is the
// RESOLVED batch-1 unit-15 real (Stubs.Extract2 delegate, the
// &v1d[j] offset idiom passes j). The emitted C text keeps the
// vendor bytes verbatim including the p2493 comment tag and the
// adjacent-literal fold "\t_extract(" " (UNS) tmp%d,..." (the C
// adjacent-string concatenation, quoted per H-015); the -1
// padding arms kept verbatim (a==1 pads two -1 seats, a==2 pads
// one). Naming per H-017.

fun Extract(v1d: CharArray, bp: CharArray): Int {
    var i: Int
    var a: Int
    // char tmp[1024]; int i, j, r, a; - r is dead (H-013, kept as
    // this comment)
    val tmp = CharArray(1024)
    var j: Int

    i = 1
    while (v1d[i] >= ' ' && v1d[i] <= Char(127)) i++ // (blank) fcn nme

    // for (SSS=v1d,TTT=&v1d[i+1]; *TTT != 0; SSS++,TTT++)
    //       *SSS = *TTT;
    // *SSS=0;
    // the squeeze drops the source fcn name; pointer pair walk:
    var sss = 0
    var ttt = i + 1
    while (v1d[ttt] != NUL) {
        v1d[sss] = v1d[ttt]
        sss++
        ttt++
    }
    v1d[sss] = NUL
    i = 0
    a = 0
    CstringOps.strcpyS(tmp, "")

    while (true) {
        j = i

        while (v1d[i] >= ' ' && v1d[i] <= Char(127)) i++

        if (v1d[i] == NUL) {
            if (a == 0 || a > 3) return a
            if (a == 1) {
                CstringOps.strcatS(tmp, ", (UNS) \"-1\", (UNS) \"-1\"")
            } else if (a == 2) {
                CstringOps.strcatS(tmp, ", (UNS) \"-1\"")
            }
            Stubs.memcheck(CompilerState.iiitmp)

            // fprintf (Out, "\t_extract(" " (UNS) tmp%d,\n "
            //          "\t\t(UNS) %s); // p2493\n", iiitmp, tmp)
            CompilerState.emit(
                "\t_extract( (UNS) tmp" + CompilerState.iiitmp +
                ",\n \t\t(UNS) " + CstringOps.cstring(tmp) +
                "); // p2493\n"
            )

            CstringOps.copyAtS(bp, 0, "tmp" + CompilerState.iiitmp)
            CompilerState.iiitmp++
            Stubs.memcheck(CompilerState.iiitmp)
            return 0
        }

        v1d[i] = NUL
        i++
        if (a > 0) CstringOps.strcatS(tmp, ", (UNS) ") // if (a++)
        a++
        CstringOps.catAt(tmp, 0, Stubs.Extract2(v1d, j))
    }
}
