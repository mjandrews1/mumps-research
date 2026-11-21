package daf

// DAF T3 seat file: constants, exception carrier and named-owner stubs
// for the batch-4 complex-stratum renders (units 114-123). Stub seats
// follow the BASE-log row 37 prototype discipline (named owners, out of
// scope until their roster cells); const values are pinned verbatim from
// the vendor headers (defines.h / compiler-macros.h / libmpscpp.h);
// carrier folds follow rows 18 (struct->class) and 6/22 (globals->
// singleton).
//
// Named-owner roster homes (units.csv batches in parentheses):
//   xindexDaf          <- interp-strmanip.cpp::xindex (b9) /
//                         compiler-sys1.c::xindex (b10)
//   MglobalDaf         <- interp-globalb.cpp::Mglobal (b6)
//   parseDaf           <- interp-parse.cpp::parse_ (b9)
//   symRetDaf          <- interp-sym.cpp::sym_ (no roster unit for the
//                         dispatcher; permanent named-owner stub, nullable
//                         seat for the BASE-log row 92 retrieve-then-throw
//                         pattern)
//   InterpretDaf       <- interp-interp.cpp::Interpret (b6)
//   scanParseDaf       <- interp-sym.cpp::ScanParse (b8)
//   allocSVDaf         <- interp-sysfunc.cpp::AllocSV (b8)
//   buildLocalDaf      <- interp-sym.cpp::BuildLocal (b8)
//   buildGlobalDaf     <- interp-parse.cpp::BuildGlobal (b8)
//   globalOrderDaf     <- libmpscpp.cpp::GlobalOrder (b2/b3/b9; the
//                         include/mumpsc/globalOrder.h::<misc> unit b9)
//   globalQueryDaf flip: the unit-119 render in this same tranche
//   globalNameDaf      <- interp-sysfunc.cpp::GlobalName (b1)
//   qsubDaf            <- interp-sym.cpp::Qsub (b7)
//   qlengthDaf         <- interp-sym.cpp::Qlength (b5)
//   evalDaf            <- interp-sym.cpp::Eval (b9)
//   mpsJustifyDaf      <- interp-arith.cpp::mps_justify (b3)
//   sqlDaf             <- interp-sql.cpp::sql (b9) / rdbms-if.c::sql (b5)
//   extract2Daf        <- compiler-parse.c::Extract2 (b1)
//   extract1Daf        <- compiler-parse.c::Extract1 (kernel of the
//                         Extract2 family; same TU owner)
//   localArrayDaf      <- compiler-localarray.c::LocalArray (b1)
//   globalArrayDaf     <- compiler-globalarray.c::GlobalArray (b2)
// Non-roster permanent stubs (interpreter/library TU helpers):
//   errorMessageDaf    <- interp misc diagnostic (prototyped by the
//                         sysfunc.h residue unit 107)
//   keyfixDaf          <- mumpsc/keyfix.h family
//   makeLockDaf, cleanLocksDaf <-lock service helpers
//   levCheckDaf        <- compiler dotted-line helper (LevCheck)
//   cvarDaf            <- compiler-parse.c::cvar (declared-variable test)
//   atCopyDaf          <- compiler at-text copy helper
//   memcheckDaf        <- compiler temp-var budget check helper
//   bsv wrappers        <- libmpscpp bio library (StopLookup/SYN/Shred/
//                         ShredQuery/bmg_fullsearch): non-roster library
//                         functions reachable via the unit-120 wrappers
// Modeled libc seats (manual-decision, DafCRuntimeT3):
//   strfmon is NOT modeled on the DAF arm (named stub); rand/time/
//   getpid modeled as divergent seats in DafCRuntimeT3.

// vendor defines.h / compiler-macros.h / libmpscpp.h values
const val PARSE_ERROR_D = 1
const val ARG_LIST_ERROR_D = 2
const val NO_GLOBAL_PERMITTED_D = 3
const val MEM_ERROR_D = 4
const val TOO_MANY_LHS_D = 5
const val ABRTX_D = 6
const val BAD_LOCK_D = 9
const val SET_CODE_SET_D = 0        // compiler-macros.h #define SET 0
const val SET_STORE_D = 1           // #define SET_STORE 1
const val CMD_SET_D = 's'.code      // #define SET 's'
const val CMD_NEW_D = 'n'.code      // #define NEW 'n'
const val CMD_NEW1_D = 'N'.code     // #define NEW1 'N'
const val CMD_READ_D = 'r'.code     // #define READ 'r'
const val CMD_KILL_D = 'k'.code     // #define KILL 'k'
const val CMD_LOCK_D = 'l'.code     // #define LOCK 'l'

const val RETRIEVE_D = 0
const val STORE_D = 1
const val NEXT_D = 2
const val KILL_D = 3
const val XNEXT_D = 8
const val FLUSH_D = 21
const val ORDERNEXT_D = 12
const val ORDERPREV_D = 13
const val SYMSTORE_D = 0
const val SYMRETRIEVE_D = 1
const val SYMTAB_ERROR_D = 9        // libmpscpp.h
const val SYM_MAX_D = 200           // defines.h

const val LINEFEED_D: Char = '\u000A' // '\012'
const val CRTN_D: Char = '\u000D'      // '\15'
const val CTRLZ_D: Char = '\u001A'     // '\032'
const val TAB_D: Char = '\u0009'       // defines-h family TAB 9

// GlobalExceptionCode + MumpsGlobalException carrier (BASE-log row 92:
// the Mglobal/sym_ retrieve-then-throw pattern; the exception type is
// the vendor class-object fold per row 18).
class MumpsGlobalExceptionD(msg: String) : RuntimeException(msg)

var GlobalExceptionCodeD = 0

// ---- named-owner stubs (throw; owners in the header above) ----

fun xindexDaf(s: CharArray, sOff: Int, pat: CharArray, patOff: Int, from: Int): Int =
    throw NotImplementedError(
        "xindex: out-of-scope owner (interp-strmanip.cpp::xindex, roster b9; compiler-sys1.c::xindex, roster b10)")

fun MglobalDaf(op: Int, name: CharArray, res: CharArray, sv: DafMsv): Int =
    throw NotImplementedError(
        "Mglobal: out-of-scope owner (interp-globalb.cpp::Mglobal, roster b6)")

fun parseDaf(sv: DafMsv): Int =
    throw NotImplementedError(
        "parse_: out-of-scope owner (interp-parse.cpp::parse_, roster b9)")

// char *sym_(int,...): nullable return is the C NULL of the dispatcher;
// the caller checks (BASE-log row 92 retrieve-then-throw fold).
fun symRetDaf(symflg: Int, a: CharArray, aOff: Int, out: CharArray, outOff: Int, sv: DafMsv): String? =
    throw NotImplementedError(
        "sym_: out-of-scope owner (interp-sym.cpp::sym_, not a roster unit; permanent named-owner stub)")

fun InterpretDaf(text: String, sv: DafMsv): Int =
    throw NotImplementedError(
        "Interpret: out-of-scope owner (interp-interp.cpp::Interpret, roster b6)")

fun scanParseDaf(xd: CharArray): Int =
    throw NotImplementedError(
        "ScanParse: out-of-scope owner (interp-sym.cpp::ScanParse, roster b8)")

fun allocSVDaf(): DafMsv =
    throw NotImplementedError(
        "AllocSV: out-of-scope owner (interp-sysfunc.cpp::AllocSV, roster b8)")

fun buildLocalDaf(flga: Int, op: Int, a: CharArray, out: CharArray, sv: DafMsv): Int =
    throw NotImplementedError(
        "BuildLocal: out-of-scope owner (interp-sym.cpp::BuildLocal, roster b8)")

fun buildGlobalDaf(flga: Int, op: Int, a: CharArray, out: CharArray, sv: DafMsv): Int =
    throw NotImplementedError(
        "BuildGlobal: out-of-scope owner (interp-parse.cpp::BuildGlobal, roster b8)")

// unsigned char *GlobalOrder(gb, prior, dir, svPtr) - roster b2/b3/b9
fun globalOrderDaf(gb: CharArray, prior: CharArray, dir: CharArray, sv: DafMsv): Int =
    throw NotImplementedError(
        "GlobalOrder: out-of-scope owner (libmpscpp.cpp::GlobalOrder, roster b2/b3/b9)")

fun globalNameDaf(name: CharArray, op: Int, sv: DafMsv): Unit =
    throw NotImplementedError(
        "GlobalName: out-of-scope owner (interp-sysfunc.cpp::GlobalName, roster b1)")

fun qsubDaf(name: CharArray, res: CharArray, sub: CharArray, sv: DafMsv): Unit =
    throw NotImplementedError(
        "Qsub: out-of-scope owner (interp-sym.cpp::Qsub, roster b7)")

fun qlengthDaf(name: CharArray, res: CharArray, sv: DafMsv): Unit =
    throw NotImplementedError(
        "Qlength: out-of-scope owner (interp-sym.cpp::Qlength, roster b5)")

fun evalDaf(a: CharArray, out: CharArray, sv: DafMsv): Int =
    throw NotImplementedError(
        "Eval: out-of-scope owner (interp-sym.cpp::Eval, roster b9)")

fun mpsJustifyDaf(fmt: String, res: CharArray, valS: CharArray): Unit =
    throw NotImplementedError(
        "mps_justify: out-of-scope owner (interp-arith.cpp::mps_justify, roster b3)")

fun sqlDaf(op: Int, sv: DafMsv, a: String?, b: String?, c: String?, d: String?): Int =
    throw NotImplementedError(
        "sql: out-of-scope owner (interp-sql.cpp::sql, roster b9; rdbms-if.c::sql roster b5)")

// char *Extract2(char *) - roster b1 (compiler TU). The char* side of
// the compiler seat renders as String (row 56 family value fold).
fun extract2Daf(s: String): String =
    throw NotImplementedError(
        "Extract2: out-of-scope owner (compiler-parse.c::Extract2, roster b1)")

fun extract1Daf(a: CharArray, aOff: Int): String =
    throw NotImplementedError(
        "Extract1: out-of-scope owner (compiler-parse.c::Extract1, compiler TU kernel)")

// the &flag out-param carrier (row 30 pointer fold): LocalArray/
// GlobalArray write Set_flg/GBL_flg through the C pointer; the render
// passes the cell and reads it back after the seat call.
class IntCellD(var v: Int)

fun localArrayDaf(vd: String, flag: IntCellD): Unit =
    throw NotImplementedError(
        "LocalArray: out-of-scope owner (compiler-localarray.c::LocalArray, roster b1)")

fun globalArrayDaf(v1d: CharArray, flag: IntCellD, vd: String): Unit =
    throw NotImplementedError(
        "GlobalArray: out-of-scope owner (compiler-globalarray.c::GlobalArray, roster b2)")

fun errorMessageDaf(msg: String, line: Int): Unit =
    throw NotImplementedError(
        "ErrorMessage: out-of-scope owner (interpreter misc diagnostic; prototyped by the unit-107 residue)")

fun keyfixDaf(name: CharArray): Unit =
    throw NotImplementedError(
        "keyfix: out-of-scope owner (mumpsc/keyfix.h family, not a corpus unit)")

fun makeLockDaf(a: String, b: String, c: String, d: Char, e: String): String =
    throw NotImplementedError(
        "makeLock: out-of-scope owner (lock service helper, not a corpus unit)")

fun cleanLocksDaf(a: String): Unit =
    throw NotImplementedError(
        "cleanLocks: out-of-scope owner (lock service helper, not a corpus unit)")

fun levCheckDaf(execName: Int, doRetX: Int, doRet: IntArray): Boolean =
    throw NotImplementedError(
        "LevCheck: out-of-scope owner (compiler dotted-line helper, not a corpus unit)")

fun cvarDaf(s: String): Boolean =
    throw NotImplementedError(
        "cvar: out-of-scope owner (compiler-parse.c::cvar, not a corpus unit)")

fun atCopyDaf(atFlg: Int, tmp: CharArray): Unit =
    throw NotImplementedError(
        "atCopy: out-of-scope owner (compiler at-text copy helper, not a corpus unit)")

fun memcheckDaf(n: Int): Unit =
    throw NotImplementedError(
        "memcheck: out-of-scope owner (compiler temp-var budget helper, not a corpus unit)")

// strfmon(3): NOT modeled on the DAF arm (the \$fnumber monetary format
// path seat; named-owner stub over libc).
fun strfmonD(fmt: String, x: Double): String =
    throw NotImplementedError(
        "strfmon: out-of-scope owner (libc strfmon; not modeled on the DAF arm)")

// ---- T3 numeric/platform seats (raised by the unit-123 fcn render) ----

// getpid(): JVM ProcessHandle pid (DOCUMENTED DIVERGENCE: the C getpid
// of the reference build; the JVM process id is the honest analog -
// ledger, rows 33/46 family).
fun getpidD(): Long =
    ProcessHandle.current().pid()

// rand(): seeded like the libc sequence (DOCUMENTED DIVERGENCE: the
// libc rand() sequence is not reproducible; the seat models the
// rand()%j shape - ledger).
val randDSeed = java.util.Random()
fun randD(): Int = randDSeed.nextInt()

// time(&timex): epoch seconds (divergence ledger: JVM System clock).
fun timeD(): Long =
    System.currentTimeMillis() / 1000

// getcwd(): current working directory (divergence ledger: JVM user.dir).
fun getcwdD(): String? =
    System.getProperty("user.dir")

// lpad(unsigned char *, unsigned char *, short) - roster
// interp-arith.cpp family; named-owner stub.
fun lpadDaf(dst: CharArray, src: CharArray, l: Int): Unit =
    throw NotImplementedError(
        "lpad: out-of-scope owner (interp-arith.cpp lpad; not a roster unit of this batch)")

// isalpha() - ASCII-only fold of the libc ctype (BASE-log row 25 family)
fun isAlphaAscii(c: Char): Boolean =
    (c in 'a'..'z') || (c in 'A'..'Z')

// sql session opcode (defines.h:169 #define OPEN 206)
const val SQL_OPEN_D = 206