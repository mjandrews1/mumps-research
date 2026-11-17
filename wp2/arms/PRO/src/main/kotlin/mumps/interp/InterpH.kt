package mumps.interp

/**
 * PRO-arm translation of include/mumpsc/interp.h::<misc> (B6
 * tranche B, unit 171, rank 130). Translated independently from
 * the pristine vendor source (mumps-14-may-2026.tgz, sha256
 * 621deb25...ffdbe), whole-header residual (units.csv: 69 LOC,
 * comment_ratio 0.281, ptr 1, ctrl 3, score 13.3 - exact match
 * via the study tool; the header has NO extracted functions, so
 * the misc chunk IS the whole file, residual by identity, u168
 * stateVector precedent): the interpreter's command-character
 * and operator-opcode macro roster plus the five prototypes of
 * the indirection-callable runtime.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h014 out_of_scope_call : every prototype resolves to its
 *    owner roster (u57 arith.h ledger precedent): Interpret
 *    (InterpStubs interpret(cmd,svPtr) stub - roster row 156,
 *    batch-6 deferred unit u155, the u159-era mis-tag note),
 *    parse_ (real seat, compiler/Stubs.kt), prnterr (DONE, u58
 *    Prnterr.kt), BuildGlobal/BuildLocal (InterpStubs stubs,
 *    batch-8 owners). No new stubs are created here; the roster
 *    names the seats that exist (house convention, u57 note).
 *    The two includes map to existing carriers (defines.h ->
 *    per-unit consumers; symbasic.h -> the symbol-machinery
 *    roster, batch-8/10 seats).
 *  - h015 grammar_collision  : the macro constants render as
 *    file-private const vals (u57 FLT_PRECISION precedent - the
 *    parse machinery already carries per-file private opcode
 *    constants, Parse_.kt:71ff, kept as the live seats; this
 *    file is the header-faithful ledger, no cross-file binding
 *    changes).
 *  - h017 naming           : interp.h -> InterpH wrapper file;
 *    every macro name keeps the vendor spelling.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - NOT_NOT (18) duplicates NOTEQ's value 18 - two macro names,
 *    one opcode; both kept.
 *  - FCN is the dollar character; the scan gate forbids the raw
 *    dollar byte in arm source, so the value rides the DOLLAR
 *    carrier (36, ReadCode.kt/Match.kt precedent) - the runtime
 *    value is byte-identical to the vendor macro.
 *  - The banner copyright years differ from the stateVector.h
 *    twin (A.D. 2001, 2002, 2003 vs 2001 - 2215) - kept as
 *    written (u35 banner-ledger shape).
 */
private const val DOLLAR = 36.toChar() // scan-gate carrier (ReadCode precedent)

// interp.h:35 - #define TSTACK 10
private const val TSTACK = 10

// interp.h:37-61 - command characters
private const val BREAK = 'b'
private const val CMMNT = ';'
private const val DATAKILL = 'k'
private const val DO = 'd'
private const val ELSE_ = 'e' // Kotlin keyword name avoided, h015
private const val FCN = DOLLAR
private const val FILECLOSE = 'c'
private const val FILELOCK = 'l'
private const val FILEOPEN = 'o'
private const val FOR_ = 'f' // Kotlin keyword name avoided, h015
private const val GOTO_ = 'g' // Kotlin keyword name avoided, h015
private const val HALT = 'h'
private const val HTML = '<'
private const val IF_ = 'i' // Kotlin keyword name avoided, h015
private const val JOB = 'j'
private const val QUIT = 'q'
private const val READ = 'r'
private const val SET = 's' // Kotlin keyword name avoided, h015
private const val MERGE = 'm'
private const val TAB = '\t'
private const val USE = 'u'
private const val WRITE = 'w'
private const val XECUTE = 'x'
private const val ZCMDS = 'z'
private const val NEW_ = 'n' // Kotlin keyword name avoided, h015

// interp.h:63 - #define QUOTE '\''
private const val QUOTE = '\''

// interp.h:64-94 - operator opcodes
private const val POPEN = 0
private const val DIVIDE = 1
private const val MULTIPLY = 2
private const val MINUS = 3
private const val PLUS = 4
private const val OPERAND = 5
private const val OPENC = 6
private const val CONCAT = 7
private const val EQUALS = 8
private const val GREATER = 9
private const val PEMPTY = 10
private const val LESSTHAN = 11
private const val NOT = 12
private const val INTDIVIDE = 13
private const val MODULO = 14
private const val CONTAINS = 15
private const val FOLLOWS = 16
private const val PATTERN = 17
private const val NOTEQ = 18
private const val NOT_NOT = 18 // vendor duplicate of NOTEQ, kept
private const val NOTGREATER = 19
private const val NOTLESS = 20
private const val NOTCONTAINS = 21
private const val NOTFOLLOWS = 22
private const val NOTPATTERN = 23
private const val AND = 24
private const val OR = 25
private const val NOTAND = 26
private const val NOTOR = 27
private const val INDIRECT = 28
private const val EXPONEN = 29

// interp.h:95 - #define PREDICATEVAR '~'
private const val PREDICATEVAR = '~'

// interp.h:97-101 - the five prototypes (owner roster in KDoc;
// seats named, no new stubs raised - u57 arith.h convention):
//   int Interpret(char*, struct MSV*)        -> interpret stub (u155 deferred)
//   int parse_(struct MSV*)                  -> parse_ real seat (compiler/Stubs.kt)
//   void prnterr(int, struct MSV*, int)      -> prnterr DONE (u58 Prnterr.kt)
//   int BuildGlobal(int,int,uchar*,uchar*,struct MSV*) -> batch-8 stub
//   int BuildLocal(int,int,uchar*,uchar*,struct MSV*)   -> batch-8 stub
