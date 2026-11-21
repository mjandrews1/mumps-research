package doc

import java.util.Locale

// Unit 258 (B9 tranche 2): interp-sym.cpp::<misc> (batch 9, core
// stratum; units.csv row 259: loc 75, ptr 0, ctl 3, score 24.9,
// rank 118; physLOC 75 physically verified this session). The
// FILE-SCOPE LEDGER unit of the symbol-table runtime file
// (sym.c line banner family, the H-004 V ledger pattern):
// banners, build defines, includes, and the resident
// prototype/extern roster - the bodies of the prototype family
// live in their own unit files (landed or batch-tagged below).
// THE ONE BODY IN SCOPE is _ascii (the unsigned-char code
// point of a 1-based position), rendered inline per the dump.
//
// BUILD CONFIG LEDGER (defines at the banner, resolved against
// the reference build the H-018 way): #define SQLITE (live,
// the unit-202 sql family ruling); SYMDEBUG commented out in
// the vendor (debug instrumentation off on the reference
// build). #define _INTERP_ (the interp-family build tag); CSTR/
// USTR cast macros fold to the Kotlin type domain (H-012);
// #define intLong (the 64-bit long family build tag, the
// vendor "long will be defined is 32 bit wanted" comment kept
// in spirit - the H-010 Int/Long promotions ride the Msv
// carrier fields).
//
// INCLUDE LEDGER (H-004): stdio/stdlib/string/string/sys.types/
// monetary/locale/math (libc families carried by the Kotlin
// carriers - monetary.h is the strfmon seat precedent);
// mumpsc/defines.h (Carriers.kt consts - XNEXT 8 ORDERNEXT 12
// ORDERPREV 13 this batch, CRTN/CTRLZ, GBLROOT seats); sym.h;
// btree.h; globalOrder.h (the unit-265 batch-9 header inline
// roster row); keyfix.h (the throwing Stubs.keyfix seat);
// inline.h; unistd/time/sys.types ("time" family the unit
// time() extern note); fcns.h (ErrorMessage); builtin.h;
// interp.h (intInterp). global.h last (the GlobalRef carrier,
// Carriers.kt). wl, wait.h commented out.
//
// PROTOTYPE ROSTER with unit dispositions:
//   int Interpret(const char *, MSV *)             - the
//     Interpret implementation is interp-if.cpp (ServiceInter-
//     face.kt unit-119 family seats; vendor-file boundary kept,
//     not this file's roster line body);
//   int ScanParse(char *) - ScanParse rendered in Scan2Parse.kt;
//   int Eval(unsigned char *, unsigned char *, struct MSV *) -
//     "evaluate expression": THE UNIT-260 BATCH-9 BODY (this
//     tranche, Eval.kt); this roster line is the prototype that
//     file's render resolves;
//   extern int (*__label_lookup)(char *) - function pointer
//     into the label namespace binding; opaque carrier seat;
//   extern char* (*__text_function)(int) - the fcn text
//     pointer; opaque carrier seat (both extern carriers stay
//     unattributed pending an owner unit, H-013 ledger).
//
// SECTION BANNER LEDGER (physLOC-family evidence rows; the
// bodies are NOT in this file - the vendor scrolls each body
// in elsewhere or the section is a home for other-file bodies):
//   BuildLocal (the Kotlin BuildLocal.kt family landing, the
//     vendor banner here is the sym-family fork point);
//   sym_ (unit-93 parse_ family);
//   _SymFree (SymFree family, landed);
//   "evaluate an expression - non zero return is error" (the
//     _ascii/_extract neighbor banners);
//   _ascii, _extract, _find, _horolog (unit-218 batch-7 landing),
//     _justify "may be dead" (vendor comment verbatim), _length
//     (batch-10 roster row), _random (unit 247 batch-9 T1);
//   the _text_function/_label_lookup init block is COMMENTED
//     OUT in the vendor (the /* char * _text_function... */
//     fossil) - documentary only (H-013 fossil, kept as note).
//
// VENDOR FOSSIL corrected in the ledger (H-004 V header-comment
// pull): the "Dec 13, 2025" banner date kept verbatim in this
// note; the "specal case" typo family belongs to the unit-262
// FixGblRef header; the banner copyright 2003-2025 kept in situ.
//
// Consult H-004 hit-reuse: ledger construction from the header
// comments/defines/includes; the _ascii body rides the atoi/
// strmove/sprintf seats (Find.kt atoi, Stubs.strmove, sprintf
// "%d" Locale.US per H-009).

// void _ascii(unsigned char out[], unsigned char in[],
//             unsigned char begin[]) - the position-to-code-
// point intrinsic body: begin is numeric (atoi the Find.kt
// C-contract helper), position is 1-based as all Mumps string
// indexing (the "odd array and label usage" banner lineage),
// bounds miss writes the literal "-1" (strmove), and the hit
// writes sprintf "%d" of the unsigned char (ASCII domain - no
// sign bit on the reference plane, the H-012 domain fold).
fun _ascii(out: CharArray, in_: CharArray, begin: CharArray) {
    var i = atoi(CstringOps.cstring(begin))
    if (i < 1) {
        i = 1
    }

    if (i > CstringOps.strlenC(in_)) {
        CstringOps.strcpyS(out, "-1")
        return
    }

    i = in_[i - 1].code
    // sprintf(out, "%d", i) - Locale.US per H-009
    CstringOps.strcpyS(out, String.format(Locale.US, "%d", i))
    return
}
