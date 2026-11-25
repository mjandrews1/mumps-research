package daf

// Unit 289 (DAF B10 T3, ledger): include/mumpsc/defines.h::<misc>
// (units.csv row 289: 179 physical LOC): the macro ledger of the
// defines.h residue - the weekend patchwork of KIND blocks. This
// render is a LEDGER + the NEW PIN set: every macro either pins
// here (if no seat), or LEDGERS to its prior seat per the
// single-owner rule. The GPL banner rides the residue precedent
// (not emitted).
// Consults (2026-11-25T15:06:00Z, nominal): row 6/22 hit-reuse
// (the macro arm is stateless), row 46/85 hit-adapt (config
// macros fold to consts), row 96 hit-reuse - every macro is a
// name/number pair.
// ALREADY SEATED (LEDGER, not re-pinned - single-owner):
// - RETRIEVE/STORE/NEXT/KILL/CLOSE/XNEXT/ORDERNEXT/ORDERPREV/
//   PREVIOUS/POSTGRES-family opcodes: DafInterpStubsT3.kt
//   (RETRIEVE_D..ORDERPREV_D) + BtreeX.kt (BTR_PREVIOUS_D 9,
//   RESTORE 20 as BTR_RESTORE_D) + MumpsMainD.kt CLOSE_D 4;
// - SymStore/SymRetrieve opcodes: SYMSTORE_D/SYMRETRIEVE_D
//   (DafInterpStubsT3.kt);
// - CodedOpen/CodedComma/CodedClose 206-208: MstringFnumber.kt
//   CODED_OPEN_D/CODED_COMMA_D/CODED_CLOSE_D;
// - NEXTMARK/MMARK/TRXLIMIT: BtreeX.kt BTR_NEXTMARK_D, Pb.kt
//   MMARK_D, BtreeX.kt TRXLIMIT_D;
// - LINEFEED/CTRLZ/CRTN: DafInterpStubsT3.kt LINEFEED_D/CTRLZ_D
//   CRTN already seated;
// - SYM_MAX 200: DafInterpStubsT3.kt SYM_MAX_D;
// - SYMGET_NOLBL 11: MpstimeMain.kt SYMGET_NOLBL_D;
// - GPADWIDTH 40: InterpSysfuncUnpad.kt GPADWIDTH_D (row 288);
// - HAVE_PGSQL_H==0: RdbmsIfMisc.kt fold;
// - TERMCORRELATE 11 / DOCCORRELATE 17: InterpService.kt and the
//   Zfcns.kt/ZfcnBifs.kt private twins (the remainders of the
//   K-corr ladder pin here);
// - SQLITE: InterpBifsMisc.kt SQLITE_D; intLong: comment-only
//   (64-bit int fold, per DafCRuntime).
// VENDOR QUIRKS: (1) SymStore..SymName block appears TWICE
// (defines.h:54-63 and :131-139, identical - preprocessor happily
// re-defines; the Kotlin ledger pins once); (2) HAVE_PCRE_H and
// HAVE_HTTPD_H also duplicated; (3) NO/YES/HAVE_* are the
// configure-artifact lattice; (4) GPADFMT is the %c%+040.20lf
// format arm, used only by the C-kernel sort coercion - LEDGER
// note only; (5) DELIM is the "\\" two-char literal.
// Dafny: EXEMPT (macro ledger; journal row).

// ---- new pins (no prior seat found by file sweep) ----

const val CODED_COLON_D: Char = '\u00D1' // defines.h:44 CodedColon 209

// SymStore-family opcode ledger seats (the sym_ dispatcher of the
// row-279 InterpSym render references these by value; pinned here
// as the owner):
const val SYM_DELETE_EXPLICIT_D = 2 // defines.h:56 SymDeleteExplicit
const val SYM_NEXT_D = 3            // defines.h:57 SymNext
const val SYM_DELETE_ALL_D = 4      // defines.h:58 SymDeleteAll
const val SYM_DELETE_ALL_EXCEPT_D = 5 // defines.h:59 SymDeleteAllExcept
const val SYM_DATA_D = 6           // defines.h:60 SymData
const val SYM_NAME_D = 10          // defines.h:61 SymName
const val SYM_RET_NO_LBLS_D = 11   // defines.h:62 SymRetNoLbls
const val SYM_CPP_DATA_D = 102     // defines.h:63 SymCPPdata 102
// (SymMarkCopyFlag 102 - same value as SymCPPdata, vendor quirk
// shared-pin; fold to SYM_CPP_DATA_D)

const val SYMGET_D = 1 // defines.h SYMGET

// CRTN '\15' ALREADY SEATED as CRTN_D (DafInterpStubsT3.kt:117)
// - LEDGER only.

const val LOCK_D = 10     // defines.h LOCK 10 (the #ifndef guard)
const val UNLOCK_D = 11   // defines.h UNLOCK
const val INIT_D = 6      // defines.h INIT
const val GKILL_D = 3     // defines.h GKILL (== KILL 3, shared pin)
const val PCLOSE_D = 7    // defines.h PCLOSE
const val CCLOSE_D = 15   // defines.h CCLOSE
const val GLOBAL_D = 23   // defines.h GLOBAL
// FLUSH 21 ALREADY SEATED as FLUSH_D (DafInterpStubsT3.kt:108)
// - LEDGER only.
const val PULSE_D = 100   // defines.h PULSE
const val SEED_D = 777    // defines.h SEED (test seed)
const val NBR_ITERATIONS_D = 100000 // defines.h NBR_ITERATIONS
const val GBLPERMIT_D = 438 // defines.h GBLPERMIT 0666 (decimal)
const val GBLNME_D = 20    // the K-corr ladder 20 GBLNME
const val DELIM_D = "\\\\"  // defines.h DELIM "\\" two-char literal

// ---- the K-corr ladder (defines.h:270-289; the DocCorrelate /
// SysCorrelate selector voice numbers; 11/17 ledger above) ----
const val COSINE_D = 1        // COSINE 1
const val TRANSPOSE_D = 2     // TRANSPOSE 2
const val INVDF_D = 3         // INVDF 3
const val DICE_D = 4          // DICE 4
const val SCANALNUM_D = 5     // SCANALNUM 5
const val SCAN_D = 6          // SCAN 6
const val AVG_D = 7           // AVG 7 (note: AVG 7 vs INIT 6 -
                              // separate ladders, distinct pins)
const val SIM1_D = 8          // SIM1 8
const val JACCARD_D = 9       // JACCARD 9
const val CENTROID_D = 10     // CENTROID 10
const val COUNT_D = 12        // COUNT 12
const val MAX_D = 13          // MAX 13
const val MIN_D = 14          // MIN 14
const val MMULTIPLY_D = 15    // MMULTIPLY 15
const val SUM_D = 16          // SUM 16
const val GETENV_D = 18       // GETENV 18
const val INPUT_D = 19        // INPUT 19

// transaction opcodes (defines.h:78-82)
const val BEGIN_TRANSACTION_D = 25
const val COMMIT_TRANSACTION_D = 26
const val ROLLBACK_TRANSACTION_D = 27
const val SAVEPOINT_D = 28
const val PRAGMA_D = 29
const val SQLCMD_D = 55   // defines.h SQLCMD 55
const val SQLREBUILD_D = 56
const val DELETE_D = 57   // defines.h DELETE 57

// open/close file opcodes (defines.h OPEN 206 == CodedOpen 206,
// shared vendor pin; fold to CODED_OPEN_D - ledger note)

// SUCCESS/FAILURE of global() (defines.h)
const val GLOBAL_SUCCESS_D = 1
const val GLOBAL_FAILURE_D = 0

// the configure lattice (defines.h:66-100; the reference config
// values as consts - the YES picks render true, the 0 picks false)
const val PGSQL_D = 2
const val POSTGRESQL_D = 3
const val HAVE_PCRE_D = 1        // #define HAVE_PCRE 1
const val HAVE_GCVT_D = YES_D    // #define HAVE_GCVT YES
const val HAVE_UNISTD_H_D = YES_D
const val HAVE_SSL_D = NO_D
const val HAVE_SOCKET_H_D = NO_D
const val HAVE_INET_H_D = NO_D
const val HAVE_SIGNAL_H_D = NO_D
const val HAVE_DB_H_D = NO_D
const val HAVE_SEM_H_D = NO_D
const val HAVE_SHM_H_D = NO_D
const val HAVE_BDB_D = NO_D
const val HAVE_APPLE_D = NO_D
const val HAVE_GMP_D = NO_D
const val HAVE_MPFR_D = NO_D
