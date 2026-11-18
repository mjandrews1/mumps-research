package mumps.interp

/**
 * PRO-arm translation of include/mumpsc/defines.h::<misc> (roster
 * row 290, batch 10; units.csv: loc 179, comment_ratio 0.164,
 * pointer_marks 0, control_marks 3, score 21.5, rank 173).
 * Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference
 * source: the full header residue (the license banner, the DEFINES_
 * H include guard, and every #define block through the #endif
 * tail). The chunk reproduces the study tool's misc residue exactly
 * (loc 179, cr 0.164, ptr 0, ctrl 3, score 21.5 - load_units
 * repro verified).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h004 one_owner        : each constant renders here once with
 *    its earlier per-file seat named (the BtreeH.a precedent -
 *    per-file private copies stay; no redeclaration conflicts
 *    because every seat here is private to this file).
 *  - h013 dead_code        : the header repeats whole define blocks
 *    verbatim (the SymStore family, the HAVE_* family, the PGSQL/
 *    POSTGRESQL family, RETRIEVE/STORE/NEXT/KILL/etc., PREVIOUS)
 *    - C overwrites macros harmlessly; Kotlin cannot redeclare,
 *    so the repeats render once each with the duplicate noted in
 *    this header ledger.
 *  - h015 grammar_collision: GPADFMT is a format STRING ("%c%
 *    +040.20lf") carried as a const String; GBLPERMIT 0666 rides
 *    438 (Kotlin has no octal literals, btree.h BtreeH precedent
 *    for the same constant); LINEFEED/CRTN/CTRLZ ride Char octal
 *    escapes; DELIM is a two-char string "\\".
 *  - h017 naming           : defines.h -> DefinesH wrapper file
 *    (cppsysfunc.h -> CppsysfuncH precedent); C macro names keep
 *    their spelling.
 *  - h018 preprocessor_conditional: the #ifndef DEFINES_H guard,
 *    the #ifndef LOCK 10 guard, the intLong/SQLITE/BUF_PARSE/STEM/
 *    MNORMAL feature switches (the reference build's arm: SQLITE
 *    defined, per the Mglobal.kt u173 note) and the commented
 *    CLOSE 22 / DATABASE / apache.h residue are fossil text,
 *    carried as this ledger note.
 *
 * Disposition ledger (the constants that already have seats):
 *  - SQLITE: the reference-build DBMS switch (Mglobal.kt u173
 *    note; the arm renders, the native arm is GlobalbMisc.kt).
 *  - CodedOpen/CodedClose/CodedComma: compiler/Parse_.kt (the
 *    TU-local 206/207/208 decode; CodedColon 209 rides here).
 *  - RETRIEVE/STORE/NEXT/KILL/CLOSE/XNEXT/PREVIOUS/GKILL/INIT/
 *    ORDERNEXT/ORDERPREV/LOCK/UNLOCK: the global-array dispatch
 *    codes (Mglobal.kt, GlobalbMisc.kt, NativeBtree.kt,
 *    BuildGlobal.kt, BuildLocal.kt per-file seats).
 *  - SymStore..SymMarkCopyFlag / SYMSTORE..SYMGET_NOLBL: the
 *    symbol-table op codes (SymInit.kt/SymFree seats).
 *  - NEXTMARK 1 / MMARK 0xff / SEED / NBR_ITERATIONS / TRXLIMIT:
 *    machine-marker and test knobs (BmgSearch/Gotamatch NEXTMARK
 *    seats).
 *  - GPADWIDTH 40 / GPADFMT: the gpad pair (Unpad.kt,
 *    InterpSysfuncMisc.kt seats).
 *  - COSINE..GBLNME: the z-function dispatch codes (Zfcn.kt /
 *    ZfcnBifs.kt switch seats, u60 note).
 *  - HAVE_* / PGSQL / POSTGRESQL / NO / YES: feature switches
 *    fossil-boolean after configure (vendor 0/1/YES), rendered
 *    below as written.
 *  - SYM_MAX 200: symbol table stack depth (Msv.start carrier,
 *    InterpStubs.u28 note).
 *  - GLOBAL_SUCCESS / GLOBAL_FAILURE: the global() return pair
 *    (MdhState tpx gate conventions).
 *  - LINEFEED / CRTN / CTRLZ: control characters.
 *  - SQLCMD / SQLREBUILD / DELETE / BEGIN_TRANSACTION..PRAGMA /
 *    RESTORE / FLUSH / GLOBAL / PULSE / PCLOSE / CCLOSE / OPEN:
 *    the remaining dispatch codes named below.
 */

private const val CodedOpen = 206   // defines.h:29
private const val CodedClose = 207 // defines.h:30
private const val CodedComma = 208  // defines.h:31
private const val CodedColon = 209  // defines.h:32

private const val SymStore = 0            // defines.h SymStore family
private const val SymRetrieve = 1
private const val SymDeleteExplicit = 2
private const val SymNext = 3
private const val SymDeleteAll = 4
private const val SymDeleteAllExcept = 5
private const val SymData = 6
private const val SymName = 10
private const val SymRetNoLbls = 11   // the second family's SymRetNoLbls
private const val SymCPPdata = 102
private const val SymMarkCopyFlag = 102 // vendor: aliases 102 on purpose

private const val PGSQL = 2
private const val POSTGRESQL = 3
private const val NO = 0
private const val YES = 1
private const val HAVE_SSL_H = 0
private const val HAVE_SOCKET_H = 0
private const val HAVE_INET_H = 0
private const val HAVE_SIGNAL_H = 0
private const val HAVE_DB_H = 0
private const val HAVE_SEM_H = 0
private const val HAVE_SHM_H = 0
private const val HAVE_PCRE_H = YES
private const val HAVE_BDB = 0
private const val HAVE_APPLE = 0
private const val HAVE_GCVT = YES
private const val HAVE_HTTPD_H = 0
private const val HAVE_AP_CONFIG_H = 0
private const val HAVE_UTIL_CFGTREE_H = 0
private const val HAVE_UNISTD_H = YES
private const val HAVE_PCRE = 1
private const val HAVE_PGSQL_H = 0
private const val HAVE_POSTGRESQL = 0
private const val HAVE_SSL = 0
private const val HAVE_GMP = 0
private const val HAVE_MPFR = 0

private const val RETRIEVE = 0     // the global() dispatch block
private const val STORE = 1
private const val NEXT = 2
private const val KILL = 3
private const val CLOSE = 4
private const val XNEXT = 8
private const val PREVIOUS = 9
private const val RESTORE = 20
private const val FLUSH = 21
private const val GLOBAL = 23

private const val BEGIN_TRANSACTION = 25
private const val COMMIT_TRANSACTION = 26
private const val ROLLBACK_TRANSACTION = 27
private const val SAVEPOINT = 28
private const val PRAGMA = 29

private const val SEED = 777         // the test-code prime
private const val NBR_ITERATIONS = 100000

private const val MMARK = 0xff
private const val TRXLIMIT = 100000
private const val NEXTMARK = 1

private const val LOCK = 10 // the #ifndef LOCK guarded block
private const val INIT = 6
private const val GKILL = 3
private const val OPEN = 206 // defines.h (the interp macro-switch OPEN; the
// compiler TU-local opcode OPEN 0 is distinct, Parse_.kt)
private const val ORDERNEXT = 12
private const val ORDERPREV = 13
private const val PCLOSE = 7
private const val UNLOCK = 11
private const val CCLOSE = 15
private const val SQLCMD = 55
private const val SQLREBUILD = 56
private const val DELETE = 57

private const val GLOBAL_SUCCESS = 1  // Success/failure values returned by global().
private const val GLOBAL_FAILURE = 0

private const val GBLPERMIT = 438 // 0666; Kotlin has no octal literal (h010)

private const val LINEFEED = '\n' // vendor '\012' octal = 0x0a line feed
private const val CRTN = '\r'         // '\15' -> carriage return
private const val CTRLZ = '\u001a'    // '\032' -> SUB

private const val SYMSTORE = 0
private const val SYMGET = 1
private const val SYMGET_NOLBL = 11

private const val PULSE = 100

private const val GPADWIDTH = 40
private const val GPADFMT = "%c%+040.20lf" // defines.h (the gpad format)

private const val SYM_MAX = 200 // depth of symbol table stack

private const val DELIM = "\\\\" // vendor: "\\" - a single backslash string

private const val COSINE = 1
private const val TRANSPOSE = 2
private const val INVDF = 3
private const val DICE = 4
private const val SCANALNUM = 5
private const val SCAN = 6
private const val AVG = 7
private const val SIM1 = 8
private const val JACCARD = 9
private const val CENTROID = 10
private const val TERMCORRELATE = 11
private const val COUNT = 12
private const val MAX = 13
private const val MIN = 14
private const val MMULTIPLY = 15
private const val SUM = 16
private const val DOCCORRELATE = 17
private const val GETENV = 18
private const val INPUT = 19
private const val GBLNME = 20
