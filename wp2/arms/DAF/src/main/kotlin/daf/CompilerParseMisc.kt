package daf

// Unit 243 (DAF B8 T3, complex): compiler-parse.c::<misc> (210
// LOC, score 108.8): the file-head residue chunk of the compiler
// parser - banner pair + June 5 2024 date line, the Fortran-
// heritage block note, the include ladder, the #define ladders,
// the prototype/extern rosters, and SIX SMALL BODIES the chunker
// folded in (ConCheck, strcasecmp, strncasecmp, memcheck, cvar,
// Extract1). Consult H-014 hit-reuse: every define and prototype
// names a landed seat; the three bodies that ARE DAF named-owner
// stubs (memcheckDaf, cvarDaf, extract1Daf) render here as real
// bodies and the stubs are RELEASED (the levCheck precedent); the
// other three render as this-file helpers.
// Consults (2026-11-24T21:23:30Z, nominal): memcheck/cvar/
// Extract1 releases verified against call sites (CompilerExtract,
// SetCode, ExpandText, ForCode); the Coded lattice flagged and
// adjudicated below.
// Dafny: EXEMPT (emit-side parser plumbing and fold-in helpers;
// journal row).

// INCLUDE LEDGER: memsize (the Carriers consts), stdio/sys.stat/
// fcntl/time/stdlib/string/ctype (per-unit Kotlin per H-001); the
// #if UNIX==0 #include conio.h/io.h/dos.h/errno.h arm is INERT on
// the reference build (UNIX != 0; H-018 fossil ledger note).

// DEFINE LADDERS, per seat:
// - SymStore 0 / SymRetrieve 1 / SymDeleteExplicit 2 / SymNext 3
//   / SymOrder 33 / SymDeleteAll 4 / SymDeleteAllExcept 5 /
//   SymData 6 / SymName 10 - the sym_ code literals already ride
//   the landed callers (Fcn.kt sym_(0/1/11) etc.); no new consts.
// - RETRIEVE 0 / STORE 1 / NEXT 2 / XNEXT 8 / NEXTX 20 / KILL1 30
//   / PREVIOUS 9 / ORDERNEXT 12 / ORDERPREV 13 / OPEN 0 - the
//   MglobalD code consts (MumpsMainD.kt:63 seat file); this
//   header's GBLDEFERED 100 / LCLDEFERED 101 / NAME 102 / GET 103
//   / ORDER 104 are the deferred-binding walk labels carried
//   internally by the landed ExpandText/ExpandOrder renders (no
//   package consts; vendor-name ledger note).
// - JOB 0 / SET 1 / GOTO 2 / WRITE 3 / IF 4 / ZCMDS 6 / READ 7 /
//   QUIT 8 / XECUTE 9 / TAB 9 / FOR 10 / HALT 11 / ELSE 12 /
//   ORDERNEXT 12 / ORDERPREV 13 / DO 13 / KILL 14 / CLOSE 15 /
//   USE 16 / LOCK 17 / COMMENT 18 / BREAK 19 / BADCMD 21 /
//   KILL2 20 / FCN 20 / LBL 21 / QUOTE 39 - the command-int
//   lattice; the landed walkers carry them as raw ints on the
//   NEXT_LINE_D=8-armed router (CompilerZcommandsCode) and the
//   Unit-120/InterpH seats; QUOTE 39 rides Doty.kt.
// - OPEN 0 / DIVIDE 1 / MULTIPLY 2 / MINUS 3 / PLUS 4 / OPERAND 5
//   / OPENC 6 / CONCAT 7 / EQUALS 8 / GREATER 9 / EMPTY 10 /
//   LESSTHAN 11 / NOT 12 / INTDIVIDE 13 / MODULO 14 / CONTAINS 15
//   / FOLLOWS 16 / PATTERN 17 / NOTEQ 18 / NOTGREATER 19 /
//   NOTLESS 20 / NOTCONTAINS 21 / NOTFOLLOWS 22 / NOTPATTERN 23 /
//   AND 24 / OR 25 / NOTAND 26 / NOTOR 27 / INDIRECT 28 /
//   EXPONEN 29 / PREDICATEVAR '~' - the operator lattice; the
//   InterpH.kt consts (interp.h twin defines, identical values;
//   the u186 shared-value note).
// - CodedOpen 206 / CodedClose 207 / CodedComma 208 / CodedColon
//   209 - ADJUDICATION NOTE: this header (and all four vendor
//   defines.h copies, verified this session at defines.h:38-41)
//   sets CodedClose 207 and CodedComma 208; the arm consts of the
//   unit-97 seat (MstringFnumber.kt:17-19, CODED_COMMA_D =
//   '\u00CF' 207, CODED_CLOSE_D = '\u00D0' 208) INVERT those two
//   names relative to vendor. The inversion is arm-internal and
//   symmetric (every encoder and decoder of the family - Fcn.kt,
//   GlobalFnumber.kt, InterpFnumber.kt, LocalName.kt, the
//   Expand* emitters - uses the same two consts in matching
//   pairs), so emitted and re-parsed text roundtrips correctly;
//   the Zfcns/ZfcnBifs seats (CLOSE_ZB '\u00CF' 207, COMMA_ZB
//   '\u00D0' 208, the zfcn family) ARE vendor-value aligned. No
//   code change rides this render; the name/value divergence is
//   disclosed for PI adjudication in the B8 fragment.

// PROTOTYPE/EXTERN ROSTER, per seat:
// - ExpandQS/patrn/patrn1/xindex/errmod_ - patrnDaf/xindexDaf
//   (DafInterpStubsT3:114), Doty.kt patrn seats; errmod_ folds to
//   the emitInterpretDaf error ladder (SetCode).
// - add/sub/mult/divx/numcomp/cannon - addDaf (b1), the multDaf
//   stub raised at unit-240 (owner compiler-sys1.c::mult, not
//   corpus), divx/numcomp named stubs, numcompDaf (stubs file),
//   cannonD rendered at unit-240 (Cannon.kt; DOC's mirror carries
//   the 71-LOC compiler-sys1 twin, statement-identical).
// - MyFunc/sym_/keyfix/fixstr - the Doty.kt/Sym* seats; keyfixDaf
//   stub (Merge.kt cite); fixstr rides Doty.kt.
// - MaxTmp/iiitmp/ErrChar/Out - maxTmpD; iiitmp/ErrChar and the
//   Out emitter on DafCompilerState; Out emit-side folded
//   (Sysout seat, CompilerFcninit).
// - SSS/TTT (unsigned char * scratch) - file-scope pointers of
//   the vendor; each use site folds them into a loop index (the
//   ConCheck shift below folds SSS into the loop; H-010
//   pointer-fold precedent). TTT unused in this chunk.
// - struct dcls cvars/cvarx, CVARMAX 100 - cvarsD Array<DclsEntryD>
//   + CVARMAX_D (CompilerEpi.kt:57-62), cvarxD
//   (CompilerZcommandsCode.kt:55).

// -----------------------------------------------------------------
// Residual body 1: ConCheck (compiler-parse.c:181). Tests a
// constant-fold string (digit run in double quotes); on success
// shifts the buffer left one (drops the opening quote) and
// NUL-trims the closing quote before returning 1. No DAF caller
// sits above it yet (the pattern walkers ride patrn); the body
// lands as the residual cell of THIS chunk so the cell is not a
// tractor seat.
fun conCheckD(b: CharArray): Int {
    var i: Int
    if (b[0] != '"') return 0
    if (b[strlenC(b) - 1] != '"') return 0
    i = 1
    while (b[i] != '"') {          // for (i=1; b[i]!='"'; i++)
        if (!isDigit(b[i])) return 0
        i++
    }
    // for (SSS=b; *SSS !=0; SSS++) *SSS = *(SSS+1);  (SSS folded)
    var s = 0
    while (b[s] != NUL_D) { b[s] = b[s + 1]; s++ }
    b[s - 1] = NUL_D               // b[strlen(b)-1]=0
    return 1
}

// Residual body 2: strcasecmp (compiler-parse.c:190) - the C file
// defines its own ASCII-fold compare (locale-free). Positive
// return = differ; 0 = equal (vendor convention kept: returns 0 on
// equality or on exhausting a).
fun strcasecmpD(a: CharArray, b: CharArray): Int {
    var i = 0
    while (a[i].uppercaseChar() == b[i].uppercaseChar() && a[i] != NUL_D) {
        i++; if (i >= a.size || i >= b.size) break
    }
    return if (a[i] == NUL_D) 0 else 1
}

// Residual body 3: strncasecmp (compiler-parse.c:198) - the
// bounded twin; stops after n chars.
fun strncasecmpD(a: CharArray, b: CharArray, n: Int): Int {
    var i = 0
    while (a[i].uppercaseChar() == b[i].uppercaseChar() &&
            a[i] != NUL_D && n - i > 0) {
        i++; if (i >= a.size || i >= b.size) break
    }
    return if (n - i == 0 || a[i] == NUL_D) 0 else 1
}

// Residual body 4: memcheck (compiler-parse.c:213) - SEAT
// RELEASED: the memcheckDaf named-owner stub (DafInterpStubsT3,
// raised b1 with the temp-var emit family) is deleted with a
// release note; the call sites (CompilerExtract x2, SetCode,
// ExpandText x2) resolve here unchanged.
fun memcheckDaf(iiitmp: Int) {
    if (iiitmp > maxTmpD) maxTmpD = iiitmp // if (iiitmp > MaxTmp) MaxTmp=iiitmp;
}

// Residual body 5: cvar (compiler-parse.c:218) - SEAT RELEASED:
// the cvarDaf named-owner stub is deleted with a release note;
// the declared-variable test scans the cvarsD table to cvarxD.
// ForCode x7 / SetCode call sites resolve here unchanged.
fun cvarDaf(str: String): Boolean {
    for (i in 0 until cvarxD) {
        if (str == cvarsD[i].name) return true // strcmp fold
    }
    return false
}

// Residual body 6: Extract1 (compiler-parse.c:227) - SEAT
// RELEASED: the extract1Daf named-owner stub is deleted with a
// release note; SetCode 327/363 resolve here unchanged. The
// signature (CharArray + off) keeps the SetCode carrier; the
// vendor returns str when unmarked. tmp2 is the vendor fossil
// (declared static, never written in this body); not carried.
fun extract1Daf(str: CharArray, strOff: Int): String {
    if (str[strOff] != '#') {
        return buildString { for (k in strOff until str.size) append(str[k]) }
    }
    var i = strOff + 3
    while (str[i] != '#' && str[i] != NUL_D && i < str.size) i++
    val len = i - (strOff + 3)
    val tmp1 = CharArray(64)
    for (k in 0 until len) tmp1[k] = str[strOff + 3 + k] // memcpy
    return buildString { for (k in 0 until len) append(tmp1[k]) }
}

// file-local strlen twin for the CharArray-NUL convention (the
// b1 CRuntime precedent; NUL-terminated in-bounds carriers only).
private fun strlenC(a: CharArray): Int {
    var n = 0
    while (a[n] != NUL_D) n++
    return n
}

private fun isDigit(c: Char): Boolean = c in '0'..'9'
