package both

// Unit 297 (BOTH B10, core): include/mumpsc/defines.h::<misc>
// (179 LOC). Batch_plan ordinal 297; units.csv row 289. The
// defines.h macro ledger: the banner/license residue is not
// carried; the model is the macro lattice. Per the
// single-owner rule every macro either PINS here (no prior
// seat) or LEDGERS to its prior seat. The vendor re-declares
// several macros in later blocks (RETRIEVE/STORE/NEXT/KILL/
// SymStore/SymRetrieve/HAVE_*): the duplicate re-declarations
// are consistent and ledger to the same seats (journaled).
//
// ALREADY SEATED (LEDGER, not re-pinned - single-owner):
//   CodedComma 208 (ExpandOrder.kt, the Char carrier) and
//   ORDERNEXT 12 / ORDERPREV 13 (LibmpscppMisc6.kt) and
//   SYM_MAX 200 (BothCRuntime.kt) are LEDGERED to their
//   top-level seats (single-owner; the vendor duplicate
//   blocks re-declare them consistently);
//   RETRIEVE 0 / STORE 1 / NEXT 2 / CLOSE 4 / XNEXT 8 /
//   SYMSTORE 0 / SYMGET 1 / SYMGET_NOLBL 11 (the Mglobal and
//   sym opcode seats, InterpStubs/BothStubs/MpstimeMain);
//   MMARK 0xff (Btree.kt);
//   NEXTMARK 1 / TRXLIMIT 100000 (the
//   BtreeX private NEXTMARK_BX/TRXLIMIT_BX seats); GPADWIDTH
//   40 (the unit-298 render, this batch); SQLITE live config
//   (the reference build, h018); DEFINES_H guard, intLong,
//   BUF_PARSE, STEM, MNORMAL - feature macros with no value,
//   documented only (the config lattice rides the unit-293
//   sysparms record).
//
// BOTH consults (find_rule before render, events logged):
//   h014 out_of_scope_call - reused: the ledgered macros name
//     their seats above (no new mechanism; the pins below
//     carry no call).
//   h017 naming - reused: the pins keep vendor names; the
//     collision-free set is verified against the arm's
//     top-level declarations (the object-scoped twins
//     (BifsZfcn CodedOpen member, the sym-object PREVIOUS)
//     keep their scopes - no shadow at the bare-name pins).
//   h018 preprocessor_conditional - reused: the HAVE_* config
//     lattice resolves against the reference build config
//     (the oracle build values below); SQLITE live.
//   Dafny: EXEMPT (constant macro lattice; journal row).

// --- coded-reference byte markers (defines.h Coded* block) ---
internal const val CodedOpen = 206
internal const val CodedClose = 207
internal const val CodedColon = 209

// --- sym_ opcode block (the lowercase sym.h-facing set) ---
internal const val SymStore = 0
internal const val SymRetrieve = 1
internal const val SymDeleteExplicit = 2
internal const val SymNext = 3
internal const val SymDeleteAll = 4
internal const val SymDeleteAllExcept = 5
internal const val SymData = 6
internal const val SymName = 10
internal const val SymRetNoLbls = 11
internal const val SymCPPdata = 102
internal const val SymMarkCopyFlag = 102

// --- Mglobal/global opcode block (unseated members) ---
internal const val INIT = 6
internal const val LOCK = 10
internal const val UNLOCK = 11
internal const val GKILL = 3
internal const val OPEN = 206
internal const val PCLOSE = 7
internal const val CCLOSE = 15
internal const val RESTORE = 20
internal const val FLUSH = 21
internal const val SQLCMD = 55
internal const val SQLREBUILD = 56
internal const val DELETE = 57
internal const val GLOBAL = 23
internal const val GLOBAL_SUCCESS = 1
internal const val GLOBAL_FAILURE = 0
internal const val GBLPERMIT = 438 // 0666 (octal file-permission mask)

// --- transaction/SQL block ---
internal const val BEGIN_TRANSACTION = 25
internal const val COMMIT_TRANSACTION = 26
internal const val ROLLBACK_TRANSACTION = 27
internal const val SAVEPOINT = 28
internal const val PRAGMA = 29

// --- byte constants ---
internal const val LINEFEED = '\n' // '\012'
internal const val CRTN = '\r' // '\15'
internal const val CTRLZ = 26.toChar() // '\032'

// --- service-function code block (the correlation family) ---
internal const val COSINE = 1
internal const val TRANSPOSE = 2
internal const val INVDF = 3
internal const val DICE = 4
internal const val SCANALNUM = 5
internal const val SCAN = 6
internal const val AVG = 7
internal const val SIM1 = 8
internal const val JACCARD = 9
internal const val CENTROID = 10
internal const val TERMCORRELATE = 11
internal const val COUNT = 12
internal const val MAX = 13
internal const val MIN = 14
internal const val MMULTIPLY = 15
internal const val SUM = 16
internal const val DOCCORRELATE = 17
internal const val GETENV = 18
internal const val INPUT = 19
internal const val GBLNME = 20

// --- misc constants ---
internal const val PGSQL = 2
internal const val POSTGRESQL = 3
internal const val NO = 0
internal const val YES = 1
internal const val SEED = 777
internal const val NBR_ITERATIONS = 100000
internal const val PULSE = 100
internal const val DELIM = "\\" // the vendor backslash literal
internal const val GPADFMT = "%c%+040.20lf" // the gpad packed format

// --- HAVE_* config lattice (the reference build values) ---
internal const val HAVE_SSL_H = 0
internal const val HAVE_SOCKET_H = 0
internal const val HAVE_INET_H = 0
internal const val HAVE_SIGNAL_H = 0
internal const val HAVE_DB_H = 0
internal const val HAVE_SEM_H = 0
internal const val HAVE_SHM_H = 0
internal const val HAVE_PCRE_H = 1
internal const val HAVE_BDB = 0
internal const val HAVE_APPLE = 0
internal const val HAVE_GCVT = 1
internal const val HAVE_HTTPD_H = 0
internal const val HAVE_AP_CONFIG_H = 0
internal const val HAVE_UTIL_CFGTREE_H = 0
internal const val HAVE_UNISTD_H = 1
internal const val HAVE_PCRE = 1
internal const val HAVE_PGSQL_H = 0
internal const val HAVE_POSTGRESQL = 0
internal const val HAVE_SSL = 0
internal const val HAVE_GMP = 0
internal const val HAVE_MPFR = 0
