package doc

// UNIT 307 RENDER - batch 10 DOC tranche T3 (complex).
// interp-globalb.cpp::<misc> - roster row 287, loc 112.
// The misc pool body is the NATIVE arm of Mglobal (the
// #else branch behind the SQLITE define): the btree-backed
// RETRIEVE/STORE pass-throughs, the NEXT/ORDERPREV/PREVIOUS
// preparation ladder with the \u0024next -1 coded-key probe and
// the ORDERPREV tilde marker, the XNEXT/PREVIOUS rewrite, the
// hold-copy postwalk with the prefix/unpad result discipline,
// the GKILL/XNEXT/CLOSE arms and the scalar return codes.
//
// ADJUDICATION - EXCLUDED ARM LEDGER (H-018):
// - The reference DOC configuration compiles the SQLITE arm
//   (the #define SQLITE banner at the head of this dump; the
//   Carriers.kt unit-123 seat note and the landed Mglobal.kt
//   render carry the live arm). The native btree arm is
//   #define-EXCLUDED at build time: no executable seat
//   exists in the translated corpus, and this render does
//   NOT raise one (the btree() calls would bind the
//   BtreeX/BtreeExterns landed seats only under NATIVE).
// - Boundary pointer: the landed owners are Mglobal.kt (the
//   Mglobal entry render, which never reaches this #else
//   region) and the btree family (BtreeX.kt, BtreeExterns,
//   SqlIsc/BtreeDb renders for the SQLITE twin). The
//   btree-order codes (NEXT/XNEXT/PREVIOUS/GKILL/CLOSE/
//   ORDERNEXT/ORDERPREV) ride the Carriers.kt landed
//   consts (the defines.h Mglobal code family ledger).
// - Quirks preserved as documentation rows (verbatim, they
//   would be code rows in a NATIVE build):
//   . the -1-probe writes key[j-3]=2 then key[j-2]=1 and
//     immediately key[j-2]=0 (the 1-store is a dead store,
//     H-013);
//   . the NEXT/ORDERNEXT result walk unpads and tests the
//     empty result to rewrite "" or "-1" (the same tail as
//     the sym_ opcode-3/33 exit_processing family);
//   . the hold[j]==1 back-off loop and the STRCPY-style
//     in-place bd left shift are the &bd[j+1] idiom
//     (CstringOps.copyAt seat twins in live arms);
//   . keyfix(key) precedes every btree call (the keyfix.h
//     inline; Stubs.keyfix H-014 seat).
// - The copyright/banners (LGPL, sockets credit Ben Fain,
//   the Fortran-origin note, the June 21 2025 date and the
//   astyle banner) are file-scope comment rows (H-013
//   banner family); the #include block rides the landed
//   header ledgers (DefinesH, StateVectorH/Carriers,
//   GlobalH, BtreeH, Keyfix).
//
// Consult row logged 2026-11-22T06:27:00Z (H-018 hit-reuse,
// the excluded-arm ledger: the live-arm owners were consulted
// and reused as the boundary citations).
