package both

// Unit 169 (BOTH B6 T2, core): include/mumpsc/interp.h::<misc>
// (69 LOC residue). Batch_plan ordinal 169: core, LOC 69,
// score 13.3, complexity rank 130. The interpretation-side
// lattice header: the interpreter command-char macros
// (interp.h:37-61), the QUOTE/PREDICATEVAR seats (63, 95), the
// parser token codes (interp.h:64-94), the TSTACK bound (:35),
// the function-prototype ledger (interp.h:97-101) and the
// symbasic.h include tail (:103). The banner and include guard
// are residue-comment lines (the KDoc-only ledger precedents:
// units 144/155/164).
//
// Prototype ledger (the declarations seat no bodies):
//   int Interpret(char*, struct MSV*) - interp.h:97; owner:
//     interp-interp.cpp::Interpret, roster 305 (the batch-6 big-
//     three PI session, pending).
//   int parse_(struct MSV*) - interp.h:98; owner: interp-parse::
//     parse_, roster 303, batch 3 pending (the parse_ stub seat).
//   void prnterr(int, struct MSV*, int) - interp.h:99; non-unit
//     provenance (the interpreter error-print family).
//   int BuildGlobal(...) / int BuildLocal(...) - interp.h:100-101;
//     the indirection builders (non-unit provenance ledger).
//   include <mumpsc/symbasic.h> - interp.h:103: the symbasic TU
//     residue seats nothing this arm touches yet (ledger).
//
// h015 collision scans (journalseated):
//   - FCN is the function-indicator byte: seated as the
//     "\\u0024" char constant (the H-015 no-raw-dollar hygiene;
//     the ASCII-210 style domain seats are the DOUBLE_QUOTE
//     precedent).
//   - TAB is the '\\t' char (\\u0009 seat).
//   - NOT_NOT aliases the same value 18 as NOTEQ (the vendor
//     double-define kept: both names seat).
//   - the three Kotlin hard-keyword macro names fold with the
//     trailing-underscore seats (ELSE/FOR/IF/DO -> ELSE_K/FOR_K/
//     IF_K/DO_K); the remaining names scanned clean at package
//     scope (the compiler-side globals seat separately).
//
// BOTH consults (find_rule before render, events logged):
//   h015 grammar_collision - reused and hit: the collision scans
//     above; every emitted const names its interp.h pin.
//   h017 naming - hit: the vendor macro names keep case and the
//     underscore shapes verbatim (TSTACK, NOT_NOT, PREDICATEVAR).

internal const val TSTACK = 10          // interp.h:35
internal const val BREAK = 'b'          // interp.h:37
internal const val CMMNT = ';'          // interp.h:38
internal const val DATAKILL = 'k'       // interp.h:39
internal const val DO_K = 'd'           // interp.h:40 (do is a Kotlin
                                         //   hard keyword; the trailing-
                                         //   underscore fold (h015))
internal const val ELSE_K = 'e'         // interp.h:41 (else is a Kotlin
                                         //   hard keyword; the trailing-
                                         //   underscore fold (h015))
internal const val FCN = '\u0024'         // interp.h:42 (the 0x24 byte seat)
internal const val FILECLOSE = 'c'      // interp.h:43
internal const val FILELOCK = 'l'       // interp.h:44
internal const val FILEOPEN = 'o'       // interp.h:45 (no collision; the
                                         //   open command byte kept)
internal const val FOR_K = 'f'          // interp.h:46 (for: same fold)
internal const val GOTO = 'g'          // interp.h:47
internal const val HALT = 'h'          // interp.h:48
internal const val HTML = '<'          // interp.h:49
internal const val IF_K = 'i'          // interp.h:50 (if: same fold)
internal const val JOB = 'j'           // interp.h:51
internal const val QUIT = 'q'          // interp.h:52
internal const val READ = 'r'          // interp.h:53
internal const val SET = 's'           // interp.h:54
internal const val MERGE = 'm'         // interp.h:55
internal const val TAB = '\u0009'          // interp.h:56 ('\t' seat)
internal const val USE = 'u'           // interp.h:57
internal const val WRITE_C = 'w'       // interp.h:58 (renamed: the vendor
                                         //   WRITE command byte; the C
                                         //   suffix avoids the io-side
                                         //   write seat family (h015))
internal const val XECUTE = 'x'        // interp.h:59
internal const val ZCMDS = 'z'         // interp.h:60
internal const val NEW = 'n'           // interp.h:61
internal const val QUOTE = '\''        // interp.h:63
internal const val POPEN = 0           // interp.h:64
internal const val DIVIDE = 1          // interp.h:65
internal const val MULTIPLY = 2        // interp.h:66
internal const val MINUS = 3           // interp.h:67
internal const val PLUS = 4            // interp.h:68
internal const val OPERAND = 5         // interp.h:69
internal const val OPENC = 6           // interp.h:70
internal const val CONCAT = 7          // interp.h:71
internal const val EQUALS = 8          // interp.h:72
internal const val GREATER = 9         // interp.h:73
internal const val PEMPTY = 10          // interp.h:74
internal const val LESSTHAN = 11       // interp.h:75
internal const val NOT = 12             // interp.h:76
internal const val INTDIVIDE = 13      // interp.h:77
internal const val MODULO = 14         // interp.h:78
internal const val CONTAINS = 15       // interp.h:79
internal const val FOLLOWS = 16         // interp.h:80
internal const val PATTERN = 17        // interp.h:81
internal const val NOTEQ = 18          // interp.h:82
internal const val NOT_NOT = 18        // interp.h:83 (the alias kept)
internal const val NOTGREATER = 19     // interp.h:84
internal const val NOTLESS = 20        // interp.h:85
internal const val NOTCONTAINS = 21    // interp.h:86
internal const val NOTFOLLOWS = 22      // interp.h:87
internal const val NOTPATTERN = 23     // interp.h:88
internal const val AND = 24            // interp.h:89
internal const val OR_K = 25           // interp.h:90 (renamed: the token
                                         //   code; the suffix avoids the
                                         //   arm's range-fold helper
                                         //   namespace (h015 scan))
internal const val NOTAND = 26         // interp.h:91
internal const val NOTOR = 27          // interp.h:92
internal const val INDIRECT = 28      // interp.h:93
internal const val EXPONEN = 29        // interp.h:94
internal const val PREDICATEVAR = '~'  // interp.h:95
