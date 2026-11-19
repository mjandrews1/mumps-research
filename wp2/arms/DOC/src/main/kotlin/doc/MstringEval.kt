package doc

// Unit 79 (B3 tranche 2): libmpscpp.cpp::mstring::Eval (batch 3, core
// stratum; LOC 18, score 18.2). The indirection operator twin: the
// evaluated mstring hands its content to the interpreter by packing
// the text into the state-vector xd buffer (stateVector.h:56 carrier,
// the strcpy idiom renders as the H-007 strcpyS carrier write),
// resetting xpx to 0, setting the t2 reentry flag (new carrier,
// stateVector.h:67) and pd1len to 0 (new carrier, stateVector.h:66),
// then calls the interpreter parse_ core. parse_ landed as unit 93 of
// this batch (the roster's last/hardest unit): the call site rebinds
// to the landed one-arg delegate Stubs.parse_(Globals.svPtr), the
// Extract2 seat-conversion precedent. On a
// parse_ error the GlobalExceptionCode is set to INTERPRETER_ERROR
// (libmpscpp.h:77, new Carriers const) and MumpsGlobalException is
// raised. Vendor quirks kept and noted: the "tmp = \"err\"; return
// tmp;" statements after the throw are unreachable dead code (H-013,
// kept as this ledger note, not translated); the local mstring tmp
// survives only on the success path where it wraps pd1[svPtr->sdlim]
// (the sdlim lower-bound carrier, stateVector.h:51, offset-1
// convention family) via the cstringAt carrier read (the &pd1[n]
// idiom). Naming per H-017 (Eval -> eval, the mstring member renders
// as the Mstring extension of the arm carrier class).

fun Mstring.eval(): Mstring {
    // vendor local: int parse_(struct MSV*) forward declaration -
    // rendered as the Stubs.parse_ seat call below.

    val tmp = Mstring()

    CstringOps.strcpyS(Globals.svPtr.xd, this.str)
    Globals.svPtr.xpx = 0
    Globals.svPtr.t2 = 1
    Globals.svPtr.pd1len = 0

    if (Stubs.parse_(Globals.svPtr) != 0) {
        Globals.GlobalExceptionCode = INTERPRETER_ERROR
        throw MumpsGlobalException()
        // vendor unreachable tail (H-013): tmp = "err"; return tmp;
    }

    tmp.str = CstringOps.cstringAt(Globals.svPtr.pd1, Globals.svPtr.sdlim)
    return tmp
}
