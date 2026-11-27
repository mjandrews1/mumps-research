package both

// Unit 300 (BOTH B10, complex): interp-globalb.cpp::<misc>
// (112 LOC). Batch_plan ordinal 300; units.csv row 286. The
// globalb TU residue: the banner/license, the June 21 2025
// date line, the config gate (#define SQLITE - LIVE in the
// reference build), the include block, and the NATIVE
// (#else) arm of Mglobal. The SQLITE arm of Mglobal is the
// LIVE arm (rendered at its roster-167 seat, the B6 unit -
// interp-globalb.cpp::Mglobal); the native arm below is
// VERBATIM-DEAD under the reference build config (h018: the
// reference build config is the only resolution authority;
// translate only the live arm - the excluded arm documented).
// The banner is NOT emitted (residue precedent).
//
// EXCLUDED NATIVE ARM (translated for the record only):
//   RETRIEVE/STORE -> keyfix(key) then btree(g,key,bd,svPtr).
//   NEXT/ORDERNEXT/ORDERPREV/PREVIOUS prep: j=strlen(key);
//   the \\u0024next -1 sentinel (key tail bytes 1,'1','-',1,
//   for non-ORDER ops) rewrites the tail to 2,1,0 and empties
//   bd; else NEXT/ORDERNEXT appends byte 2 and zeros past
//   j..j+3 and empties bd; else ORDERPREV with tail bytes
//   1,1 rewrites the last to '~'. gx remap: NEXT/ORDERNEXT ->
//   XNEXT, ORDERPREV -> PREVIOUS. keyfix(key); hold=key;
//   b=btree(gx,key,bd); for NEXT/ORDERNEXT/ORDERPREV the
//   result check walks hold back to its byte-1 marker j and
//   requires strncmp(hold,bd,j)==0 AND hold[j]==1 AND
//   bd[j]==1 - else NEXT yields "-1" and the ORDER ops ""
//   (return 0). On match, the subscripts after bd[j+1] shift
//   down (the in-place STRCPY), the scan stops at the next
//   byte-1 or NUL and NULs it, unpad(bd,svPtr) decodes the
//   gpad form (unit 298, this batch), and an empty result
//   yields "-1"/"" (return 0); otherwise return 1.
//   GKILL/XNEXT -> keyfix (XNEXT only) then btree. CLOSE ->
//   btree then EXIT_SUCCESS.
//
// BOTH consults (find_rule before render, events logged):
//   h018 preprocessor_conditional - reused and hit: SQLITE
//     is defined in the reference build config; the native
//     #else arm is VERBATIM-DEAD and documented above only
//     (the DAF-arm unit-286 precedent, the same TU twin).
//   h014 out_of_scope_call - reused: the include block
//     ledgers to the arm seats (defines.h roster 297 this
//     batch, stateVector.h the Msv seat, btree.h roster 55);
//     keyfix rides its seat (BothCRuntime, the leading-caret
//     strip); btree rides its roster-4 stub family; unpad
//     ledgers to the unit-298 render (this batch).
//   h017 naming - reused: no symbol is emitted; the ledger
//     keeps the TU name (InterpGlobalbMisc).
//   Dafny: EXEMPT (config-dead arm; journal row).

// (no Kotlin emitted: the native arm is VERBATIM-DEAD under
// the SQLITE reference build config - the live SQL arm rides
// its roster-167 seat; this file is the residue ledger)
