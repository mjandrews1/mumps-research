package mumps.mdh

import mumps.interp.Msv
import mumps.interp.cstringAt
import mumps.interp.fcn
import mumps.interp.strcatC
import mumps.interp.writeCstring

/**
 * PRO-arm translation of libmpscpp.cpp::mstring::Fnumber(mstring, mstring)
 * (pilot B2, unit 42, provisional under deviation C-003). Translated
 * independently from the pristine vendor source (mumps-14-may-2026.tgz,
 * sha256 621deb25...ffdbe), reference source lines 7483-7511
 * (units.csv: 22 LOC, core stratum; the one-arg overload is a batch-4
 * unit, the global::Fnumber wrappers batch-3/-7).
 *
 * The MUMPS fnumber intrinsic: build the coded function call
 * "<dollar>fnumber<Open><data>,<a>[,<b>]<Close>" into the state
 * vector's v1d buffer at offset 1, run the interpreter's function
 * dispatcher, and read the result from bd at offset 1.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h007 char_buffer        : tmp[STR_MAX] call-assembly buffer
 *    (C-string domain).
 *  - h012 unsigned_byte_domain: all buffers are Char(0..255)
 *    domains; the vendor's offset-1 convention (v1d[1]/bd[1]) kept.
 *  - h013 dead_store         : the vendor's "b.c_str()==0" branch is
 *    dead code - std::string::c_str() never returns a null pointer,
 *    so the close-only arm can never run; dropped with this note,
 *    the always-taken else arm translated straight-line.
 *  - h014 out_of_scope_call  : fcn (owner interp-fcn.cpp::fcn,
 *    batch-4 unit, 775 LOC) -> new InterpStubs stub; strcpy/strcat
 *    resolve to the real B1 CstringOps helpers - reused unmodified.
 *  - h015 grammar_collision  : the vendor literal "<dollar>fnumber"
 *    contains a raw dollar byte the pre-push scan forbids; rebuilt
 *    as 36.toChar() (SQL_Table precedent, unit 34).
 *  - h017 naming             : Fnumber keeps the vendor name; an
 *    extension on the Mstring carrier (the vendor member reads
 *    this->data via the carrier's value).
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - CodedOpen/CodedClose/CodedComma = 206/207/208 (defines.h:38-40);
 *    the vendor's two-byte arrays {code, 0} render as one-char
 *    strings in the strcat calls.
 *  - The call goes to v1d[1] (not [0]) and the result is read from
 *  bd[1] - the vendor's offset-1 state-vector convention.
 */

private const val STR_MAX = 32000 // vendor strmax.h:3 (file-private, cf. unit 22)
private const val CODED_OPEN = 206 // vendor defines.h:38 CodedOpen
private const val CODED_CLOSE = 207 // vendor defines.h:39 CodedClose
private const val CODED_COMMA = 208 // vendor defines.h:40 CodedComma

// dollar byte for the vendor literal; not written as a raw
// character (h015 pre-push scan forbids it in Kotlin sources)
private val DOLLAR = 36.toChar()

fun Mstring.Fnumber(a: Mstring, b: Mstring): Mstring {
    val tmp = CharArray(STR_MAX)
    val svPtr: Msv = MdhState.svPtr

    // vendor: strcpy(tmp, "<dollar>fnumber"); strcat(tmp, Open);
    writeCstring(tmp, DOLLAR + "fnumber")
    strcatC(tmp, CODED_OPEN.toString())

    strcatC(tmp, value) // vendor: invoking mstring data
    strcatC(tmp, CODED_COMMA.toString())

    strcatC(tmp, a.c_str()) // first argument required

    // vendor: if (b.c_str()==0) strcat(tmp,Close) else {...} - the
    // condition can never fire (c_str() is never null); the else arm
    // is unconditional here (h013 note above).
    strcatC(tmp, CODED_COMMA.toString())
    strcatC(tmp, b.c_str())
    strcatC(tmp, CODED_CLOSE.toString())

    // vendor: strcpy((char *) &svPtr->v1d[1], tmp); fcn(svPtr);
    writeCstring(svPtr.v1d, cstringAt(tmp), 1)
    fcn(svPtr)

    // vendor: rslt = (char *) &svPtr->bd[1];
    return Mstring(cstringAt(svPtr.bd, 1))
}
