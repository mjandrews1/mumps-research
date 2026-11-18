package mumps.interp

/**
 * PRO-arm translation of interp-native-btree.cpp::<misc> (roster
 * row 252, batch 9; units.csv: loc 84, comment_ratio 0.475,
 * pointer_marks 35, control_marks 14, score 98.4, rank 272).
 * Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference
 * source lines 37-68, 71, 91-102, 158, 212 and 286 - the
 * residual chunk of interp-native-btree.cpp after the freadx
 * (u193, Freadx.kt), scanblocks (u50, Scanblocks.kt), pb
 * (u111, Pb.kt) and btreeX (u250, BtreeX.kt) units.
 *
 * Residue contents, each line mapped onto its seat:
 *  - :37 #define SQLITE; :39 CSTR; :41 OK=1; :42 BAD=0; the
 *    includes :48-52; :54-55 MODIFIED/UNMODIFIED; :57-59
 *    unistd/LSEEK/OFF_T - the SQLITE prologue and macro set ride
 *    the BtreeX.kt private constants (this batch); nothing new.
 *  - :63 struct buffers *bfr[GBLBUF] = {0} - the buffer pool:
 *    BtreeBufferState.bfr (u193 Freadx.kt carrier).
 *  - :65 #define DumpBlocks; :67-69 the pb() prototype - OUT
 *    per the reference build (h018; the u111 Pb.kt B4 seat is
 *    authoritative - the dumper body is that unit's own
 *    sample).
 *  - :71 sigint proto - Sigint.kt (B1) real seat.
 *  - :91-100 the BigKey globals (BigKey/BigKeyBlock/FoundBlock/
 *    Reinsert/BigKeyParent/BigKeyParentOffset/BigKeyOffset/
 *    BigKeyLower/FoundOffset/BigKeyData) - NativeBtreeState
 *    (InterpStubs.kt carrier; foundBlock/reinsert/foundOffset
 *    ADDED by rows 250/252 this batch, vendor :93/:94/:99).
 *  - :102 extern int DBSem - MumpsGlobals.DBSem (u130
 *    MumpsMain.kt h004 note; init 0 served there).
 *  - :158 "flush all internal buffers" / :212 "trace descendant
 *    blocks for highest key value" comment fossils - seats:
 *    flushC (BtreeX.kt private, this batch) and Scanblocks.
 *  - :286 int btreeX(...) - the prototype of the row-250 seat
 *    (BtreeX.kt, same batch; the InterpStubs stub resolved).
 *  - :1206-1238 the DumpBlocks pb() body - the pb seat is
 *    Pb.kt (B4 unit 111, roster row 112; DumpBlocks OUT for
 *    the reference build per that seat's h018 note).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h004 file_scope_static: the BigKey/Found globals ride the
 *    InterpStubs NativeBtreeState object (u50 precedent).
 *  - h013 dead_store      : the freadx(&block,Reinsert,
 *    MODIFIED) double-fetch inside btreeX KILL kept as comment
 *    at that seat.
 *  - h018 preprocessor_conditional: #define DumpBlocks is OUT
 *    for the reference build (u111 Pb.kt note); the pb call
 *    sites inside btreeX stay comments (fossils).
 *  - h017 naming          : every seat keeps the vendor names
 *    at the owner files.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The BigKey family, bfr pool and DBSem extern are
 *    initialized exactly once at program start - the Kotlin
 *    carriers' defaults preserve that (no extra init emitted).
 */

// Header residue: no Kotlin declarations emitted (see KDoc).
