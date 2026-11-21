package doc

// Unit 240 (B8 tranche 3): compiler-parse.c::<misc> (batch 8,
// complex stratum; LOC 210, score 108.8, rank 277; units.csv row
// 220). The file-head residue chunk of the compiler's parser:
// banner pair + June 5 2024 date line, the include ladder, the
// #define ladders, the prototype roster, the extern roster, and
// SIX SMALL BODIES the chunker folded in (ConCheck, strcasecmp,
// strncasecmp, memcheck, cvar, Extract1). Consult H-014
// hit-reuse: every define and prototype names a landed seat; the
// bodies render with the seats flipping to delegates.
//
// Include ledger: memsize (Carriers consts), stdio/sys.stat/fcntl/
// time/stdlib/string/ctype (per-unit Kotlin imports per H-001);
// the #if UNIX==0 #include conio.h/io.h/dos.h/errno.h arm is
// INERT on the reference build (UNIX != 0, H-018 fossil ledger).
//
// Define ladders, per seat:
// - SymStore 0 / SymRetrieve 1 / SymDeleteExplicit 2 / SymNext 3
//   / SymOrder 33 / SymDeleteAll 4 / SymDeleteAllExcept 5 /
//   SymData 6 / SymName 10 - the sym_ code literals already ride
//   the landed callers (Fcn.kt sym_(0/1/11) etc.); no new consts
//   opened.
// - RETRIEVE 0 / STORE 1 - Carriers (the CLOSE-4 third-same-
//   value define note applies: this header re-declares the
//   defines.h values the Carriers consts carry).
// - op codes FOLLOW 4? no - the CONCAT 7 .. EXPONEN 29 operator
//   ladder - the InterpH.kt consts (interp.h twin defines,
//   identical values; the u186 shared-value note).
// - PREDICATEVAR '~' - the InterpH.kt / Parse_.kt char seats.
// - CodedOpen 206 / CodedClose 207 / CodedComma 208 /
//   CodedColon 209 - the Carriers Coded* consts (the ExpandText
//   unit-228 emission family).
// - GBLDEFERED 100 / LCLDEFERED 101 / NAME 102 / GET 103 /
//   ORDER 104 - the deferred-binding codes of the Expand*
//   emitters; the ExpandNext/ExpandQS landed renders carry them
//   as their internal walk labels (no package consts; the
//   vendor-name ledger note).
// - CVARMAX 100 - the CompilerState.cvars bound (below).
//
// Extern roster: MaxTmp (CompilerState.MaxTmp, the Epi unit
// reset), iiitmp (CompilerState.iiitmp), ErrChar
// (Msv.ErrChar carrier), FILE *Out (the CompilerState.Out
// emitter), SSS/TTT - file-scope scratch pointers of the vendor;
// each use site folds them into a loop index or names its own
// carrier (the ConCheck shift here folds SSS into the loop; the
// H-010 pointer-fold precedent). struct dcls cvars + cvarx -
// the CompilerState.cvars Dcs table (the unit-151
// compiler-globals-ext.h carrier raise; CVARMAX bound).
//
// Prototype roster, per owner: ExpandQS/patrn/patrn1 (landed),
// cvar/xindex/errmod_/memcheck/add/sub/mult/divx (Stubs seats;
// memcheck/cvar flip to delegates below), numcomp (Numcomp.kt),
// cannon (the unit-84 render; the batch-8 interp-parse.cpp
// twin is the verification row-note on Cannon.kt), MyFunc
// (MyFunc.kt), sym_ (Parse_.kt), keyfix (Stubs), fixstr (the
// unit-16 render), Extract2/Extract2a/Extract (landed renders
// and seats), Extract1 (this chunk's body below), ExpandOrder/
// Data/Qlength/Next/Get/Name/Text/Qsub (landed renders),
// MyFunc(unsigned char*, short) (landed).
//
// #if SYSTEM==DOS / #endif - empty matched arm, INERT (H-018).

// int ConCheck(char *b): the quoted-digit-string test of the
// compiler parser: b must open and close with '" with only
// isdigit chars between; on hit the string shifts left one
// (dropping the open quote via the SSS walk) and drops the close
// quote, returning 1. quirk: the digit walk b[i] loop has NO
// bound test past the closing quote (vendor assumes well-formed
// input; the H-007 carrier guard below bounds the walk).
fun ConCheck(b: CharArray): Int {
    var i: Int
    if (b[0] != '"') return 0
    if (b[CstringOps.strlenC(b) - 1] != '"') return 0
    i = 1
    while (b[i] != '"') {
        if (i >= b.size) return 0 // carrier guard (vendor none)
        if (!Character.isDigit(b[i])) return 0
        i++
    }
    // for (SSS=b; *SSS !=0; SSS++) *SSS = *(SSS+1);
    var sss = 0
    while (b[sss] != NUL) {
        b[sss] = b[sss + 1]
        sss++
    }
    b[CstringOps.strlenC(b) - 1] = NUL
    return 1
}

// int strcasecmp(char*, char*): the vendor variant is NOT the
// POSIX contract: it walks while case-folded chars match and *a
// is non-NUL, then returns 0 ONLY when *a == 0 (a full-prefix
// equal), 1 otherwise (no sign domain, no difference value).
// Kept verbatim (H-013 vendor semantic quirk, noted).
private fun strcasecmpCpm(a: CharArray, b: CharArray): Int {
    var ai = 0
    var bi = 0
    while (Character.toUpperCase(a[ai]) == Character.toUpperCase(b[bi]) && a[ai] != NUL) {
        ai++
        bi++
    }
    return if (a[ai] == NUL) 0 else 1
}

// int strncasecmp(char*, char*, int): same non-POSIX contract with
// the count bound: 0 when the count runs out OR *a hits NUL.
private fun strncasecmpCpm(a: CharArray, b: CharArray, n: Int): Int {
    var ai = 0
    var bi = 0
    var i = n
    while (Character.toUpperCase(a[ai]) == Character.toUpperCase(b[bi]) && a[ai] != NUL && i > 0) {
        i--
        ai++
        bi++
    }
    return if (i == 0 || a[ai] == NUL) 0 else 1
}

// void memcheck(int iiitmp): the temp-string high-water mark of
// the statement emitter (extern MaxTmp = the CompilerState seat
// the Epi render resets to 2).
fun memcheck(iiitmp: Int) {
    if (iiitmp > CompilerState.MaxTmp) CompilerState.MaxTmp = iiitmp
}

// int cvar(char *str): the named-C-variable test over the
// compilerState.cvars declared-local table.
fun cvar(str: String): Int {
    for (i in 0 until CompilerState.cvarx) {
        if (str == CstringOps.cstring(CompilerState.cvars[i].name)) return 1
    }
    return 0
}

// char *Extract1(char *str): the tmp1 half of the Extract2a
// payload split: gathers the #?name component and returns the
// function-static tmp1 (same static-buffer stale-value quirk as
// the Extract2a family - returns the LAST gathered name after
// any subsequent call; noted in Extract2a.kt). The static
// tmp2[64] declared beside tmp1 is unused in this body (kept as
// the vendor fossil comment below).
private object Extract1Static {
    // compiler-parse.c Extract1: static char tmp1[64]
    val tmp1 = CharArray(64)
    // static char tmp2[64] - unused in this body (vendor fossil)
    val tmp2 = CharArray(64)
}

fun Extract1(buf: CharArray): CharArray {
    val str = buf
    if (str[0] != '#') return str
    var i = 3
    while (str[i] != '#' && str[i] != NUL) i++
    val n = if (i - 3 > 63) 63 else i - 3 // silent clamp: the vendor
    // memcpy overflows the 64-char static for long names; the H-007
    // bounds-walk precedent (Cannon.kt tmp[25]) bounds silently
    System.arraycopy(str, 3, Extract1Static.tmp1, 0, n)
    Extract1Static.tmp1[n] = NUL
    return Extract1Static.tmp1
}
