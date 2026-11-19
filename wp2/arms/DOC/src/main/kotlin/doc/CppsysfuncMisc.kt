package doc

// Unit 76 (B3 tranche 2): include/mumpsc/cppsysfunc.h::<misc> (batch
// 3, core stratum; LOC 23, score 12.9). The compiler run-time support
// header: the LGPL banner (O'Kane 2001-2003 copyright residue, kept
// as this ledger) plus the extern "C" declaration roster of the Mumps
// C-ABI service layer. Declaration-only ledger, no executable code
// (CopyrightMisc unit-1 and EpiMisc unit-63 precedents). Vendor
// quirks kept and noted: LocalName is declared TWICE (a legal C
// duplicate declaration, kept verbatim in the roster below); the
// C-ABI names GlobalData/GlobalOrder collide textually with the
// libmpscpp.cpp class-global members of the same names but are
// DISTINCT entities (the C fns of the interpreter core, not corpus
// units) - DOC's unit-70 GlobalData renders the class-global member,
// not this C prototype; the commented-out getstr1 line is vendor
// residue (kept in the ledger as it appears). Roster owners are
// noted per entry; the two declared names that are also DOC corpus
// units elsewhere (none of these C fns are) are so marked. Naming
// per H-017 (no code rendered, names cited verbatim).

private const val CPPSYSFUNC_BANNER_YEARS = "2001, 2002, 2003"

// extern "C" prototype roster (vendor residue, verbatim order):
//   TimeOut_getstr1(int, unsigned char[], char*, int) - timeout reader
//   DirectRead(int, unsigned char*) - direct console reader
//   VariableNotFound(long, char*) - variable miss reporter
//   ErrorMessage(const char*, long) - error reporter
//   sigint(int) - SIGINT shutdown path
//   Merge(unsigned char*, unsigned char*, MSV*) - global merge
//   GlobalData(unsigned char*, unsigned char*, MSV*) - C-ABI data
//   LocalData(unsigned char*, unsigned char*, MSV*) - local data
//   GlobalOrder(unsigned char*, unsigned char*, unsigned char*, MSV*)
//   LocalOrder(unsigned char*, unsigned char*, unsigned char*, MSV*)
//   LocalNext / GlobalNext(unsigned char*, unsigned char*, MSV*)
//   LocalName / GlobalName(..., MSV*) - name walkers
//   LocalName - declared a SECOND time (vendor duplicate, kept)
//   LocalGet / GlobalGet(..., MSV*) - value getters
//   GlobalQuery(unsigned char*, unsigned char*, char, MSV*)
//   LocalQuery(unsigned char*, unsigned char*, MSV*)
//   fullUnpad(unsigned char*, MSV*) - full unpad
//   getstr1 - commented out in the vendor (above the roster)
//
// None of these C-ABI definitions is a corpus unit: each stays a
// named owner in this ledger (the ErrorMessage/DirectRead Stubs seats
// of the arm already name two of them with their landing owners).

private val cppsysfuncRoster76 = listOf(
    "TimeOut_getstr1",
    "DirectRead",
    "VariableNotFound",
    "ErrorMessage",
    "sigint",
    "Merge",
    "GlobalData",
    "LocalData",
    "GlobalOrder",
    "LocalOrder",
    "LocalNext",
    "GlobalNext",
    "LocalName",
    "GlobalName",
    "LocalGet",
    "GlobalGet",
    "GlobalQuery",
    "LocalQuery",
    "fullUnpad",
)
