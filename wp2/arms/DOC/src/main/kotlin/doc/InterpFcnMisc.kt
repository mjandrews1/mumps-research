package doc

// Unit 200 (B7 tranche 2): interp-fcn.cpp::<misc> (batch 7, core
// stratum; LOC 60, score 14.8, rank 143) - the LEADING RESIDUE chunk
// of interp-fcn.cpp (the fcn unit-123 dispatcher owns the function
// bodies; the misc chunk is the file banner and the define/
// include/prototype roster, physically verified this session;
// nonblank 60 matches the roster row). Consult H-018 hit-reuse.
// Ledger shape per the unit-19 interp-interp precedent; declaration
// and define entries render as named constants and comment
// ledgers, no executable code of its own:
//
//   - banner: Mumps Bioinformatics Software Library GPL block,
//     Dec 13, 2025 runtime date (comment ledger)
//   - :29 #define SQLITE - the reference-build config authority
//     (cookbook H-018: SQLITE is LIVE; the arm renders translate
//     only the SQLITE arms of this TU, the unit-198 render of this
//     same tranche is the worked example)
//   - :30-31 the commented // #define SYMDEBUG - off in the
//     reference build, kept as a comment (H-013 fossil)
//   - the include roster (stdio/stdlib/string/string/sys/
//     monetary/locale/math, mumpsc defines/sym/btree/globalOrder/
//     keyfix/inline, unistd/time/sys, mumpsc fcns/builtin/interp/
//     global) - comment ledger, the seats live in their owner
//     renders
//   - :50 #define _INTERP_ - unit-typing define; reference ledger
//     with no Kotlin analog (unit-19/48 precedent comments)
//   - :51-52 #define CSTR (char *) / #define USTR (unsigned char *)
//     - cast macros with no Kotlin analog (comment ledger; the
//     unit-19 wording)
//   - :78 #define intLong - the 32-bit int arm selector, ledger
//     (the unit-90/74 Modulo precedent comments)
//   - :80-82 prototypes: int Interpret(const char *, MSV *) - the
//     landed batch-6 unit-185 real (Stubs.Interpret delegate
//     seat); int ScanParse(char *) - batch-8 unit-234, the
//     Stubs.ScanParse H-014 seat names the owner; int Eval
//     (unsigned char *, unsigned char *, struct MSV *) - batch-9
//     row 261, the Stubs.Eval seat names the owner (the Fcn.kt
//     unit-123 SEAT LEDGER rows bind them)
//   - :84-85 extern int (*__label_lookup)(char *); extern char *
//     (*__text_function)(int); - roster notes only (the unit-114/
//     150 precedent: the pointer pair carries no in-arm call site)
//   - :92-93 #define SYMSTORE 0 / #define SYMRETRIEVE 1 - the
//     sym_ opcode constants rendered below (const seats; symmetric
//     private per-file consts exist at the MpstimeMain unit-150
//     render - H-015 no-redeployment: these are NEW top-level
//     seats, no prior top-level const duplicated)
//
// Naming per H-017 (vendor define names kept verbatim).

// interp-fcn.cpp:92 #define SYMSTORE 0
const val SYMSTORE = 0

// interp-fcn.cpp:93 #define SYMRETRIEVE 1
const val SYMRETRIEVE = 1
