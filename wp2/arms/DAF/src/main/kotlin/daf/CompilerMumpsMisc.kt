package daf

// Unit 206 (DAF B7 T3, complex): compiler-mumps.c::<misc> (57
// physical LOC, score 29.3): the compiler-TU residue - the GPL
// banner, the config date line, the include set (stdio, fcntl,
// stdlib, string, ctype, time, sys/stat, mumpsc memsize/
// compiler-macros/compiler-globals), and the two sub-min-loc
// function shapes the chunker folds into this residual unit:
// EP (the subfunction entry-point router) and LevCheck (the
// dotted-line ExecName level check). Unlike the banner-only
// residues of units 187/200, this row's functions have live
// call-shapes and consume TU state, so BOTH are materialized
// here: epDaf as the EP body raise (owner this row; the vendor
// operates on compiler-globals state), and LevCheck as the
// RELEASE of the levCheckDaf seat (DafInterpStubsT3:203 - the
// stub's "not a corpus unit" blame was WRONG; the body is this
// row's residual cell; the stub body is deleted with this
// release note and the call sites (Doty.kt) rebind to
// levCheckD, same shape; the node class is public file-scope
// (the private-in-file exposure resolved).
// The nmes label chain (LabelsStart/p1 of the EP router) folds
// onto the NmesNodeD chain raised here (rows 6/22; the
// Interpret.kt private-struct precedent; the ExternsHMisc 197
// declaration-only carrier note resolves at this consuming row).
// MAIN_flg is consumed here: mainFlgD raised (the ExternsHMisc
// pending-carrier ledger row). errorMessageDaf seat called
// (DafInterpStubsT3:187). epiDaf raised as a named-owner stub
// below: owner compiler-epi.c::epi = roster unit 213 of THIS
// tranche - the stub releases at that render (the substrDaf
// release pattern). Out is the house DafCompilerState.emit fold
// (rows 2/26; emitted text verbatim). offset is the vendor's
// runtime label-cursor global; the router's strcmp compares fold
// onto the String name compare (row 56 family).
// Consults (2026-11-24T20:10:00Z, nominal): BASE-log rows 6/22
// hit-reuse (the compiled-file-scope globals fold: LabelsStart
// chain the row-62 fold, MAIN_flg the top-level state raise),
// row 2/26 hit-reuse (Out -> emit; the emitted C text kept
// verbatim), row 37 hit-adapt (the residue discipline of units
// 95/187 extended: the sub-min shapes materialize because their
// seats/call sites are live), row 56 hit-adapt (the chain-walk
// pointer cursors fold onto the node class), rows 14/15
// hit-reuse (the strcmp == 0 compare family),
// manual-decision (no BASE row: LevCheck's int-return 0/1 maps
// onto the Boolean seat the stub already declared - the caller
// lattice is preserved; EP's flg gate keeps the vendor no-else
// structure).
// Dafny: EXEMPT (compile-side emit + residue shapes over seats;
// journal row).

// CORRECTED at the unit-213 epi render (same push): the vendor
// chain heads are struct TXT {char *name; int lnbr; struct txt
// *next} (compiler-globals.h:61) - NOT struct nmes (the nmes
// name-chain is the nstart row-family); the node gained the
// lnbr field and the struct-name fold note.
class TxtNodeD(val name: String, val lnbr: Int, val next: TxtNodeD?)

// struct txt *LabelsStart - file-scope head (rows 6/22/62 fold)
var labelsStartD: TxtNodeD? = null
    private set

// struct txt *TextStart - the text-function chain head (unit 213)
var textStartD: TxtNodeD? = null
    private set

// the unit-213 epi walk frees both chains (owner body there):
// the free()/p3 walk folds onto GC; the heads clear here.
fun clearLabelChainsD() {
    labelsStartD = null
    textStartD = null
}

// short MAIN_flg - the pending-carrier ledger row resolves here
var mainFlgD = 0

// void epi(short ExecName, long pid) - named-owner stub; owner
// compiler-epi.c::epi = roster unit 213 of THIS tranche (the
// render releases the stub per the substrDaf pattern).
// epiDaf SEAT RELEASED (unit 213, same batch): delegates to the
// real body at CompilerEpi.kt (epiD); the 206 raise note retires.
fun epiDaf(execName: Int, pid: Long): Unit = epiD(execName, pid)

// void EP(int flg, short ExecName, long pid) - the subfunction
// entry-point router (this row's residual cell; owner named here)
fun epDaf(flg: Int, execName: Int, pid: Long) {

    if (flg != 0) {
        DafCompilerState.emit("\tgoto _epilogue;\n")
        DafCompilerState.emit("_eps:\tif (strlen((const char *) offset)==0) goto _fcn;\n")
        var p1: TxtNodeD? = labelsStartD
        while (p1 != null) { // while (p1 != NULL)
            DafCompilerState.emit("\tif (strcmp((const char *) offset,(const char *) \"" +
                    p1.name + "\")==0) goto " + p1.name + ";\n")
            p1 = p1.next
        }
        errorMessageDaf("Error - entry point label not found",
            DafInterpState.svPtr.LineNumber)
        // (the vendor dereferences svPtr unconditionally at the
        // emit site; the runtime sv singleton drive is the row-6/22
        // fold of the MumpsMainD precedent - the singleton is
        // non-null in this arm's model, ledgered)
    }

    epiDaf(execName, pid)            //* terminate prev function
    mainFlgD = 0                     //* not in main function
}

// The levCheckDaf SEAT RELEASE (DafInterpStubsT3:203): the real
// body. int LevCheck(short ExecName, short DoRetX, short DoRet[])
fun levCheckD(execName: Int, doRetX: Int, doRet: IntArray): Boolean {
    for (i in 0 until doRetX) {     // for (i=0; i<DoRetX; i++)
        if (doRet[i] == execName) return false // return 0
    }
    return true // return 1
}
