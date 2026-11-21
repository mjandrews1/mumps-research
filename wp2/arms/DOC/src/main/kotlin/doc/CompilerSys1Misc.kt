package doc

import java.util.Locale

// Unit 257 (B9 tranche 2): compiler-sys1.c::<misc> (batch 9,
// core stratum; units.csv row 258: loc 107, ptr 0, ctl 12,
// score 22.4, rank -; physLOC 107 physically verified this
// session). The compiler SYSTEM-1 file-scope ledger and its
// resident K&R helper family (the biggest unit of the tranche).
//
// BANNER LEDGER (H-004 V header-comment pulls, kept verbatim in
// note): the O'Kane GPL banner (copyright 2003-2025; the
// okane@uni.edu address form of this file's banner kept in situ
// per the banner-fossil rule); the SECOND banner block - the
// Fortran lineage note: "Some of this code was originally
// written in Fortran / which will explain the odd array and
// label usage, / especially arrays beginning at index 1." and
// the admission "Not all functions work at present." (the H-003
// capture-candidate domain: the vendor's own candor about the
// 1-based indexing lineage and the not-all-working state);
// "modified April 20, 2025" file date kept verbatim.
//
// MACRO/DEFINE LEDGER:
//   #define Sysout(x) fprintf(Out,x) - folds to the
//     CompilerState.emit helper calls (H-001 FILE* to Writer;
//     the vendor fprintf-count idiom is why emit returns the
//     char count, the Epi.kt linepad convention);
//   #define GBLROOT 30 - RAISED HERE as the per-file const (no
//     prior GBLROOT const exists in the arm; Getstr2 TAB/
//     LINEFEED per-file raise precedent, H-004);
//   #define CRTN '\\15' and CTRLZ '\\032' - CRTN 13 / CTRLZ 26
//     already seated as the Carriers.kt consts (defines.h:
//     197-198, the readf_ unit-78 byte filters; CITED, not
//     redeclared - package-wide redeclaration would fail);
//   #define TAB '\\011' and LINEFEED '\\012' - the Getstr2.kt
//     top-level consts (unit-48 raise; CITED, not redeclared -
//     they are package-wide public);
//   #define NO 0 / YES 1 - the vendor file-scope boolean tags;
//     per-file note only: NO/YES never collide with Kotlin
//     keywords but NO as a package const would shadow-free but
//     clutter the namespace - kept as this comment ledger, the
//     only uses in this file's bodies are none (the tags
//     serve the REST of compiler-sys1.c outside this ledger
//     band; cited for the file ledger, H-013);
//   #ifdef GCVT / #endif - EMPTY conditional (no body either
//     arm): the preprocessor fossil of the gcvt variant
//     build; documentary only (H-013 fossil, H-018 the
//     resolved build carries the plain gcvt body below).
//
// INCLUDE LEDGER (H-004): mumpsc/memsize.h (the STR_MAX family
// carrier consts, Carriers.kt); stdio; fcntl; math; sys/stat;
// stdlib; string (libc families carried by the Kotlin
// carriers); mumpsc/externs.h (the compiler extern family,
// Carriers/CompilerCarriers carrier objects).
//
// EXTERN LEDGER: extern FILE *Out - the H-001 fold:
// CompilerState.Out (Writer), all writes through
// CompilerState.emit for the fprintf-count idiom.
//
// PROTOTYPE ROSTER with dispositions:
//   void linepad(int, char *) - THE BODY BELOW (this unit);
//   void cannon(char []) - the landed Cannon.kt unit (the
//     vendor cannon is pointer-shift idiom central; roster row
//     honored by that render);
//   void mult(char [], char[], char[]) - the K&R prototype
//     whose body is below (this unit; the Stubs CharArray
//     overload seat flips to delegate);
//   the lpad K&R definition IS the body band below (see the
//     DUPLICATE-DEF note).
//
// LEADING-DOT LEDGER of the band (H-004 V banner-comment pull,
// sym-lineage): the "/*==== BuildLocal ====*/ ." family section
// scrolls of compiler-sys1.c are outside this physLOC band
// (BuildLocal the unit-93 family landing; _SymFree the landed
// SymFree family) - the band counted here runs banner to the
// mult tail only.
//
// Consult H-004 hit-reuse: ledger construction from banner/
// define/include/prototype comments; the bodies ride the emit/
// String.format/strtod seats (H-009, H-010).

// #define GBLROOT 30 - per-file raise (see header; the unique
// name collides with no existing arm const)
const val GBLROOT = 30

// void lpad(strin, strout, l) - DUPLICATE-DEF LEDGER NOTE (the
// strcatx precedent of StrmanipMisc.kt): the vendor body here
// is a BYTE-IDENTICAL local copy of the interp-strmanip.cpp
// lpad (the NULL-padding left-justify), which the arm renders
// ONCE as the StrmanipMisc.kt fun lpad(strin: CharArray,
// strout: CharArray, l: Short) - distinct vendor file, same
// Kotlin signature: a second top-level fun here would collide
// (H-015 same-name rule); NO SECOND DEFINITION is rendered.
// VERIFICATION (the three arms' identity check this batch
// close): K&R short int j/i/k locals, j=strlen(strin)-1,
// k=l-j-1 guard, the tail shift strout[i+k]=strin[i], the
// space fill - byte-identical to the landed body.

// int sigc() - the SIGC handler body (compiler-sys1.c:804):
// ctrlc=1; return 1. The ctrlc extern of this TU is the PLAIN
// RUNTIME flag (NOT the Msv field of the Sigint unit-211
// ladder, whose handler writes svPtr->ctrlc; THIS body has no
// svPtr parameter and writes the file-scope extern int). The
// extern folds to the Globals.ctrlc carrier (the H-014
// per-file raise rides the existing Globals extern-carrier
// object of Carriers.kt - the GlobalExceptionCode twin
// precedent; the carrier field is EDIT-ADDED by this unit,
// Carriers.kt, with a sibling comment).
fun sigc(): Int {
    Globals.ctrlc = 1
    return 1
}

// void itoa(val, str, rad) - the K&R itoa: sprintf "%d" only
// (the rad parameter is DEAD in the vendor body, H-013 dead-
// param ledger kept in the signature); Locale.US per H-009.
// NAMING/H-015 NOTE: no prior itoa fun exists in the arm
// (verified this session); this is the sole definition.
fun itoa(val_: Int, str: CharArray, rad: Int) {
    CstringOps.strcpyS(str, String.format(Locale.US, "%d", val_))
    return
}

// void linepad(int sent, char *str) - the banner-comment
// emitter: fprintf(Out, "  /* %s */\n", str); the sent count
// and the i/j/f=0 locals are DEAD in the vendor body (H-013
// ledger: kept in the signature as the call sites pass the
// emit char count, the Epi.kt/ForCode.kt convention). Flips
// the Stubs.linepad seat to delegate this push.
fun linepad(sent: Int, s: String) {
    CompilerState.emit("  /* " + s + " */\n")
    return
}

// void linepad1(char * str) - the dotted banner rule emitter:
// i=fprintf(Out,"/* %s ",str); for (j=i; j<25; j++)
// fprintf(Out,"."); fprintf(Out,"*/ "); - the vendor QUITE of
// the RETURN COUNT idiom: i is the emit char count and the
// dot fill pads to column 25 of the RETURNED WIDTH (not the
// string length), kept verbatim (H-009 the count semantics;
// the trailing "*/ " has a space and NO newline - the caller
// concatenates). No prior seat exists (verified this session):
// this is the sole definition; the vendor linepad1 call sites
// in the compiler family bind here.
fun linepad1(s: String) {
    val i = CompilerState.emit("/* " + s + " ")
    for (j in i until 25) CompilerState.emit(".")
    CompilerState.emit("*/ ")
    return
}

// void linepad2(char * str) - fprintf(Out,"/*** %s */\n",str):
// the epigraph rule emitter. Flips the Stubs.linepad2 seat to
// delegate this push (the unit-207 fcninit render raised that
// seat; the H-004 fcninit header cites this owner).
fun linepad2(s: String) {
    CompilerState.emit("/*** " + s + " */\n")
    return
}

// void gcvt(x, i, a) - the K&R gcvt: sprintf(a, "%g", x); the
// i (precision) parameter is DEAD in the vendor body (the %g
// default precision, H-013 dead-param ledger); "%g" per H-009
// (Locale.US, the C shortest-form double render; the gcvtWrapper
// Stubs seat is the INTERPRETER twin of the same idiom - this
// is the compiler-side plain body, no seat flip, callers are
// the mult below and the compiler family).
// NAMING/H-015 NOTE: no prior gcvt fun exists in the arm
// (verified this session); this is the sole definition.
fun gcvt(x: Double, i: Int, a: CharArray) {
    CstringOps.strcpyS(a, String.format(Locale.US, "%g", x))
    return
}

// void mult(a, b, c) - the K&R decimal-string multiply of the
// exponent-expansion family: atof folds (strtod the MpsExp
// unit-40 C-contract helper, the //double atof(); vendor fossil
// comment kept as this note), the FLOAT precision of the vendor
// aa*bb product is kept per H-010 (float cc = aa*bb; the
// reference-config precision ruling - NOT double), and the
// result rides gcvt(cc, 7, c) above with the dead 7 precision
// arg (H-013). FLIPS the Stubs.mult CHAR-ARRAY overload to
// delegate this push: the String-arg Stubs.mult overload stays
// a stub for its own emission call sites (Cannon.kt "10"/".1"
// exponent loop seats), unless the interp-arith.cpp:624 batch-7
// landing resolves them (see the fragment ledger this push).
fun mult(a: CharArray, b: CharArray, c: CharArray) {
    val aa = strtod(CstringOps.cstring(a)).toFloat()
    val bb = strtod(CstringOps.cstring(b)).toFloat()
    val cc = aa * bb
    gcvt(cc.toDouble(), 7, c)
    return
}
