package doc

// Unit 290 (B10 tranche 2): include/mumpsc/defines.h::<misc>
// (batch 10, core stratum; units.csv row 290: loc 179, cr 0.164,
// ptr 0, ctl 3, score 21.5, rank 173; physically verified this
// session at vendor include/mumpsc/defines.h:1-247, the whole-
// file residual physLOC 179). The run-time macro header ledger:
// every define family is led to its landed seat or owner row;
// NO new body (banner + macro convention render, the
// compiler-globals.h u241 / builtin.h u244 precedent). Derived
// from the pristine vendor source this session (tarball sha256
// 621deb...ffdbe). Consult H-004 hit-reuse: file-scope ledger
// convention, each named seat citing its landed or future
// owner. Naming per H-017.
// Consult: unit 290, entry H-004, outcome hit-reuse,
// timestamp 2026-11-22T06:12:00Z.
//
// Macro family ledger (disposition per block, vendor order):
// - intLong / SQLITE feature defines: banner feature macros of
//   the sym.c/fnumber.c preambles (ledgered residue there);
//   no value seat. CodedOpen 206 / CodedClose 207 / CodedComma
//   208 / CodedColon 209: the coded-byte family - Open/Close/
//   Comma RIDE the Carriers.kt consts (landed raises);
//   CodedColon (the commerce-nine colon byte) had NO landed
//   consumer seat and stays a ledger row here (raised where a
//   roster owner cites it).
// - SymStore..SymRetNoLbls / SymCPPdata=SymMarkCopyFlag=102 and
//   the later duplicate SymStore..SymName block: the sym_
//   access codes, ledgered to the batch-10 row-280 interp-
//   sym.cpp::sym_ owner (the Stubs.sym_ seat tag).
// - PGSQL/POSTGRESQL/NO/YES/HAVE_* feature probes: build-
//   configuration macros, no runtime seat (banner).
// - RETRIEVE..GLOBAL, BEGIN..PRAGMA transaction codes: the
//   Mglobal code family - RETRIEVE 0/STORE 1/NEXT 2/KILL 3/
//   CLOSE 4/XNEXT 8/PREVIOUS 9 order codes LIVE in Carriers.kt
//   (NEXT const) and the SqlIsc/BtreeX renders (their local
//   seats cite this header); duplicate later block (PULSE 100,
//   LOCK 10, UNLOCK 11, INIT 6, ORDERNEXT 12/ORDERPREV 13 -
//   Carriers), SQLCMD 55/SQLREBUILD 56/DELETE 57 (the sql
//   block - SqlIsc ledger rows), the commented CLOSE 22 and
//   commented DATABASE "Mumps.DB" fossils kept (H-013).
// - SEED 777 / NBR_ITERATIONS 100000: the test-generator
//   constants, banner (no roster unit).
// - MMARK 0xff / TRXLIMIT 100000 / NEXTMARK 1: landed seats
//   (BtreeExterns MMARK; the BtreeX private TRXLIMIT_BX/
//   NEXTMARK_BX cites defines.h:121-122).
// - GLOBAL_SUCCESS 1 / GLOBAL_FAILURE 0: Mglobal return
//   codes; folded as literal compares in the landed Mglobal
//   renders (banner row here).
// - GBLPERMIT 0666: landed GBLPERMIT (BtreeH.kt, octal 438).
// - LINEFEED '\012' / CRTN '\15' / CTRLZ '\032': landed
//   seats (Getstr2 LINEFEED, Carriers CRTN=13/CTRLZ=26 Int).
// - SYMSTORE 0 / SYMGET 1 / SYMGET_NOLBL 11: the interp sym
//   padding codes, ledgered to the interp-sym owners (the
//   batch-10 row-280 owner seat).
// - GPADWIDTH 40 / GPADFMT "%c%+040.20lf": RAISED HERE - the
//   gpad pad-width/sign-format pair (the unpad row-289 render
//   is the landed consumer; GPADFMT is the pad-builder format
//   cited by unpad's decode side).
// - SYM_MAX 200: landed (Carriers.kt, defines.h:218).
// - PREVIOUS 9 re-defines: FOUR sites (defines.h:89/173/213/
//   219) of the same order code (duplicate-define fossil of
//   the order-code block, same value kept).
// - DELIM "\\" / BUF_PARSE / STEM / MNORMAL: the feature and
//   delimiter macros (STEM toggles the wstem wiring; BUF_PARSE
//   and MNORMAL the ZWN/ZWI/ZWP and ZA/ZN features) - banner
//   feature rows, owners the sysparms.h row-294 ledger (the
//   feature-switch cross-family).
// - COSINE 1..GBLNME 20 block (defines.h:226-245): the
//   similarity-function
//   opcode table (the DOCCORRELATE-style integer command
//   codes; the Cosine/Dice/Jaccard/Count/Max/Min/Concat
//   renders consume their codes at their call seats; the
//   numeric identities are their owner rows' KDoc ledgers).

const val GPADWIDTH = 40 // defines.h:215
const val GPADFMT = "%c%+040.20lf" // defines.h:216 (gpad pad format)
