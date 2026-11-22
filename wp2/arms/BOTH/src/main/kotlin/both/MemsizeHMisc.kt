package both

// Unit 164 (BOTH B6 T1, simple): include/mumpsc/memsize.h::<misc>
// (50 LOC residue). Batch_plan ordinal 164: simple, LOC 50,
// score 10.0, complexity rank 98. The config header of the
// compiler/runtime: the residual <misc> chunk (banner lines
// 1-22, the configuration lattice lines 30-108). Banner and
// comment-only lines seat as a KDoc-only ledger (the unit-144
// sym.h and unit-155 compiler-fcninit residuals precede this
// render); the live emitters land as consts below.
//
// h018 lattice resolution (the frozen reference build is the
// oracle build config; h018 declares GCVT defined):
//   - LIVE (MacOS/gcc reference arm): the __APPLE__ block
//     (lines 62-64) defines GCVT 1; GnuDos undefined and
//     _MSC_VER undefined, so the Linux block (lines 82-87) is
//     the platform arm: USYSTEM LINUX, UNIX 1, DOS 2,
//     SYSTEM UNIX.
//   - EXCLUDED ARMS (translated only for the record as
//     comments): the Solaris block (lines 76-80, Solaris
//     undefined), the Watcom block (lines 89-94), the GnuDos
//     block (lines 96-101), the MSVisualC block (lines 103-109,
//     _MSC_VER undefined in the reference build).
//   - DEAD defines (commented out in the vendor, kept dead
//     here): STEM, the UDAT/UKEY commented defines (lines 38-39),
//     RELEASE (the "@VERSION@ __DATE__" build substitution stays
//     unreferenced).
//   - NOTABS (line 43) is defined (a valueless define): the
//     arm seats it as the true constant NOTABS_ACTIVE; the
//     DOC unit-129 readf blast-radius edit does NOT apply to
//     this arm (no readf render exists in the BOTH tree, the
//     h015 collision scan verified).
//   - The vendor FIXME note (line 31): PS/PS1 also appear in
//     sysparms.h (duplicate-definition seam, ledgered).
//
// PS1 (memsize.h:33, PS 4096 + 1 = 4097) is ALREADY SEATED at
// the unit-151 render (CompilerGlobalsExtMisc.kt:51) with the
// same pins; verified equal, reused not re-declared (h015
// collision scan).
//
// BOTH consults (find_rule before render, events logged):
//   h018 preprocessor_conditional - reused and hit: every
//     emitted const below names its live arm and pin; the
//     excluded platform arms stay as comments.
//   h015 grammar_collision - reused and hit: PS/PS1 verified
//     against the seated CompilerGlobalsExtMisc consts (PS1
//     reused; PS itself had no prior seat); VERSION/SHOW_CODE/
//     DDAT/DKEY/FORPARM scanned against the arm namespace, no
//     collisions found.
//   h017 naming - reused and hit: vendor macro names kept
//     (h017 always-document: the valueless NOTABS define maps
//     to the boolean NOTABS_ACTIVE seat named in the ledger).

internal const val PS = 4096          // memsize.h:32 (#define PS 4096)
internal const val SHOW_CODE = 1       // memsize.h:35
internal const val VERSION = 1000      // memsize.h:37
internal const val DDAT = "data.dat"   // memsize.h:40
internal const val DKEY = "key.dat"    // memsize.h:41
internal const val NOTABS_ACTIVE = true // memsize.h:43 valueless define, live
internal const val FORPARM = 32        // memsize.h:44
internal const val GCVT = 1            // memsize.h:63 live via __APPLE__ (h018)
internal const val UNIX = 1            // memsize.h:85 (Linux platform arm)
internal const val DOS = 2             // memsize.h:86
internal const val SYSTEM = UNIX       // memsize.h:87
internal const val USYSTEM = "LINUX"   // memsize.h:84 (the vendor macro
                                       // expands unquoted; the seat keeps
                                       // the token form)
// PS1 = PS + 1 = 4097 (memsize.h:33) - reused from
// CompilerGlobalsExtMisc.kt (unit-151 seat), not re-declared.
