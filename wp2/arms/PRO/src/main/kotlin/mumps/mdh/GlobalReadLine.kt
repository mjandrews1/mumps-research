package mumps.mdh

import java.io.BufferedReader
import java.io.InputStream
import java.io.Reader
import java.util.Scanner
import mumps.interp.Msv
import mumps.interp.cstringAt
import mumps.interp.mglobal
import mumps.interp.writeCstring

/**
 * PRO-arm translation of libmpscpp.cpp::global::ReadLine
 * (batch-8 unit 236, units.csv data line 236, rank 106).
 * Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference
 * source lines 3003-3069 (the three global::ReadLine
 * overloads; the mstring::ReadLine family at 3071-3111 was
 * already rendered as the unit-188 residue and named THIS row
 * as the deferred owner - the deferral is now resolved).
 *
 * Identity verified against the study tool (tools/select_study_
 * subset.py): libmpscpp.cpp::global::ReadLine, cpp, loc 21,
 * comment_ratio 0.045, pointer_marks 1, control_marks 4,
 * score 10.7, rank 106, batch 8 - exact match to
 * wp1/study-subset/units.csv line 236.
 *
 * global::ReadLine(): read one line from stdin / a FILE* /
 * an istream and STORE it into this global (Mglobal(STORE,
 * ref, tmp, svPtr)); returns whether the read succeeded.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h001 io_stream_param: FILE* -> Reader, istream ->
 *    Scanner (unit-188 mstring::ReadLine shapes, same
 *    tranche family); stdin via the reflective System.in
 *    fetch (u158 precedent).
 *  - h004 globals_ext: file-scope svPtr -> MdhState.svPtr.
 *  - h007 char_buffer : the fgets/getline line rides the
 *    String edge (readLine excludes the terminator - the
 *    built-in strip matches the vendor's tmp[strlen-1]=0).
 *  - h012 unsigned_byte_domain: the STORE key/value buffers
 *    are Char(0..255) domains.
 *  - h014 out_of_scope_call: Mglobal (RETRIEVE=0/STORE=1)
 *    reused unmodified.
 *  - h015 grammar_collision: new top-level ReadLine
 *    extensions on Global - no collisions (the Mstring
 *    family is receiver-distinct; the unit-188 KDoc
 *    anticipated this seat).
 *  - h017 naming      : ReadLine keeps the vendor name.
 *  - h018 preprocessor_conditional: none in this body.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The no-arg and FILE* forms strip the trailing newline
 *    UNCONDITIONALLY (tmp[strlen(tmp)-1]=0 before the
 *    null-read check: an empty read indexes tmp[-1] - the
 *    String fold makes the strip safe, kept as note), then
 *    strip a second char when < ' ' (CR-LF form).
 *  - The istream form checks eof BEFORE the CR-LF strip -
 *    eof writes the empty string, stores it, and returns
 *    false (kept; the String fold reports eof via null).
 *  - All three return i != 0 / true - the read flag, not
 *    the STORE flag (kept).
 */

private const val STR_MAX = 32000 // vendor strmax.h:3 (file-private, cf. unit 22/97)
private const val STORE = 1 // vendor defines.h:84
private const val GLOBAL_NOT_FOUND = 2 // vendor libmpscpp.h:71

/** Per-file C-string bridge (h007; Misc-6 precedent). */
private fun toCbuf(s: String): CharArray {
    val b = CharArray(STR_MAX)
    writeCstring(b, s)
    return b
}

/** Shared STORE edge: line -> this global; throws when absent. */
private fun Global.storeLine(line: String, svPtr: Msv) {
    val tmp = CharArray(STR_MAX)
    writeCstring(tmp, line)
    val f = mglobal(STORE, toCbuf(ref), tmp, svPtr)
    if (f == 0) {
        MdhState.globalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }
}

/** vendor 3003: bool global::ReadLine() - the stdin form. */
fun Global.ReadLine(): Boolean {
    val stdin = System::class.java.getField("in").get(null) as InputStream
    val line = BufferedReader(java.io.InputStreamReader(stdin)).readLine() ?: return false
    var t = line // h001: readLine excludes the terminator (the vendor's unconditional strip)
    if (t.isNotEmpty() && t[t.length - 1] < ' ') t = t.dropLast(1) // CR-LF form
    storeLine(t, MdhState.svPtr)
    return true
}

/** vendor 3021: bool global::ReadLine(FILE *u1) - the file-twin. */
fun Global.ReadLine(u1: Reader): Boolean {
    val line = (u1 as? BufferedReader ?: BufferedReader(u1)).readLine() ?: return false
    var t = line
    if (t.isNotEmpty() && t[t.length - 1] < ' ') t = t.dropLast(1) // CR-LF form
    storeLine(t, MdhState.svPtr)
    return true
}

/** vendor 3039: bool global::ReadLine(istream &u1) - the Scanner form. */
fun Global.ReadLine(u1: Scanner): Boolean {
    if (!u1.hasNextLine()) { // vendor: if (u1.eof()) { tmp[0]=0; ... return false; }
        storeLine("", MdhState.svPtr)
        return false
    }
    var t = u1.nextLine()
    if (t.isNotEmpty() && t[t.length - 1] < ' ') t = t.dropLast(1) // CR-LF form
    storeLine(t, MdhState.svPtr)
    return true
}
