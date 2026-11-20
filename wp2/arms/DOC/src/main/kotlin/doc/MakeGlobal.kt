package doc

// Unit 199 (B7 tranche 2): libmpscpp.cpp::global::MakeGlobal (batch
// 7, core stratum; LOC 38, score 14.6, rank 140; vendor
// libmpscpp.cpp:6963, physically verified this session; nonblank 38
// matches the roster row) - the class-global varargs reference
// builder named by the Stubs.makeGlobal H-014 seat since batch 1's
// unit-31 chunk (the seat flips to the delegate on this render; the
// unit-31 invoke-family call sites bind unchanged). Consult H-014
// hit-adapt: the flip needs the member-state adapter seats and one
// NEW stub (count, below), the first non-reuse outcome of this
// tranche.
//
// SIGNATURE FOLD (seat adapter): the vendor member is
// MakeGlobal(int flg, char * gblnme, char * key, va_list ap). The
// batch-1 seat folded gblnme onto the receiver (the vendor callers
// pass the object's own myname; the arm GlobalRef name carrier is
// the idem seat), key is the a,b,c letter list and the va_list
// renders as the vararg parameter. The batch-1 call sites pass
// decimal-string subscripts in BOTH arms (the long-invoke variants
// convert through the decimal string first, the unit-31 ledger),
// so the flg==1 numeric arm keeps its va_arg(long) semantics over
// the Any seats (Long -> the %ld arm; String -> the char* arm);
// in-arm call sites today are all flg=0 char* subscripts.
//
// MEMBER-STATE LEDGER: the vendor writes the private members Sref
// (the quoted visible-reference form), Sname, ArgCount and the char
// ref[STR_MAX] buffer. The unit-199 render opens the member seats
// on the GlobalRef carrier (globalClass.h:534/:537-540 anchors, the
// Carriers.kt edit; ref widens val -> var for the strcpy(ref,buf)
// store - the divergence note lives there). The vendor sprintf
// %ld builds through the H-009 Locale.US fold; strcatx is the
// real inline.h:46 helper (the unit-23 Input.kt render, module
// internal); va_end has no Kotlin analog (comment ledger). The
// vendor's two commented-out //strcat(Sref,...) fossil lines are
// kept verbatim as comments per H-013. The static char
// buf[STR_MAX] is the function-static shared scratch: the H-004
// owner object MakeGlobalState holds it. count(key) is NOT a roster
// unit (libmpscpp.cpp:2932 tally helper): the new Stubs.count
// H-014 seat names the owner. Naming per H-017.

object MakeGlobalState {
    // static char buf[STR_MAX] (vendor libmpscpp.cpp:6967)
    val buf = CharArray(STR_MAX)
}

fun GlobalRef.makeGlobal(
    flgParam: Int,
    gblnme: String,
    key: String,
    vararg args: Any
): GlobalRef {
    var flg = flgParam
    val tmp = CharArray(32)
    Sref = gblnme
    Sname = gblnme
    Sref = Sref + "("
    // va_start (ap, key): the Kotlin vararg parameter is the ap
    // seat; j = count(key)
    val j = Stubs.count(key)
    ArgCount = j
    CstringOps.strcpyS(MakeGlobalState.buf, "")
    strcatx(MakeGlobalState.buf, gblnme.toCharArray())

    if (key == "\"\"") { // strcmp(key,"\"\"") == 0
        flg = 0
    }

    for (i in 0 until j) {
        if (flg == 1) {
            val ix = args[i] as Long // va_arg(ap,long)
            CstringOps.strcpyS(tmp, String.format(java.util.Locale.US, "%d", ix))
            strcatx(MakeGlobalState.buf, tmp)
            //strcat(Sref,tmp);
            Sref = Sref + tmp
            //strcat(Sref,",");
            Sref = Sref + ","
        } else {
            val x = args[i] as String // va_arg(ap,char *)
            strcatx(MakeGlobalState.buf, x.toCharArray())
            Sref = Sref + "\""
            Sref = Sref + x
            Sref = Sref + "\""
            Sref = Sref + ","
        }
    }

    // va_end(ap); - no Kotlin analog (comment ledger)
    // strcpy(ref, buf); - the member ref store (see the carrier
    // note): the pristine name is REPLACED by the built reference
    ref = CstringOps.cstring(MakeGlobalState.buf)
    // Sref[Sref.length()-1] = ')'; - the index-store renders over
    // the String content (same observable effect, H-007 String
    // classification of the builder)
    Sref = Sref.substring(0, Sref.length - 1) + ")"
    return this
}
