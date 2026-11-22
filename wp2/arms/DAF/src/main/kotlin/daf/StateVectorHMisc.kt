package daf

// Unit 172 (DAF B6 T2, core): include/mumpsc/stateVector.h::<misc>
// (76 LOC, score 18.4, rank 162): the state-vector header of the
// interpreter run time: the banner, the June 16 2025 date line, the
// include guard, struct MSV (the full runtime state: buffers, flags,
// io tables, symbol-table stacks, counters, sql/pg carriers), and
// the AllocSV prototype + the StateVector define.
// Consults (2026-11-22T16:03:30Z, nominal): BASE-log rows 18/22
// hit-reuse (the struct fold: the FULL MSV lattice seats into the
// DafMsv carrier of DafCRuntime.kt - the existing carrier holds
// the b4-T3/b5 seat fields; this render ADDS the residual field
// roster below so the carrier reaches the full vendor struct; the
// row-22 file-scope state fold), rows 39/78 hit-adapt (the header
// is unconditional; the struct field ORDER is kept), row 37
// hit-reuse (the residue ledger KDoc).
// SEAT ADDITIONS (the residual roster, in vendor order; the
// DafMsv fields already seated at b4-T3/b5 are unchanged):
//   cfgdata[256] / cfgkey[256] - the config-file name pair
//   LockDone, kflg, symflg, ITRAP, ctrlc, iorg, ERROR, gpad,
//   ctrlc-kin counters and flags
//   in_file[10] / out_file[10] - the FILE* device tables (the row
//   54 object-reference fold: the Int handle domain, file
//   descriptors not opened at DAF)
//   RefParm[10][16] / Rargs[10][16] / Rargsx / _SymPush /
//   order - the call-by-ref name registers
//   hit / count - the btree cache hit ratio (ULong)
//   restoredata - the restore blob cursor (ULong)
//   ScanMinWordSize / ScanMaxWordSize - the ScanAlnum window
//   UKEY[1024] / UDAT[1024] - the native data/key file names
//   Table[256] / TabSize / ConOpen - the sql table carriers
//   pgsql_msg / Connection / Col_Names - the sql message triplet
//   fdes[2] / exec - the exec pipe pair
//   reference[1024] - the call-by-ref underlying name
//   ErrChar - the error-echo char
//   program[512] - the current program name
// Vendor quirks ledgered: the banner year pair reads "2001 - 2215"
// (a vendor typo kept verbatim in the source note only); the
// commented-out grslts[22] block is source-dead (comment residue,
// no seat); the StateVector #define (struct MSV * alias) folds onto
// the DafMsv type name directly; AllocSV keeps its existing
// allocSVDaf seat (roster b8, the GlobalQuery consult note).
// Dafny: EXEMPT (struct carrier residue; journal row).

// (the residual field roster seats into DafMsv in DafCRuntime.kt -
// the carrier edit of this render; no separate Kotlin in this file)
