package mumps.mdh

import java.util.Date
import java.util.Locale
import mumps.interp.NUL

/**
 * PRO-arm translation of server.cpp::<misc> (B4 unit 116, rank
 * 83). Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe); the
 * study-tool residual chunk is the file head: the GPL banner
 * (with the vendor's "Sockets implementation by Ben Fain"
 * credit), the socket/system include set, the SERVERBUF define,
 * the struct mymsgbuf message-queue carrier, and the logtime()
 * function (misc physical loc 60, comment_ratio 0.032,
 * pointer_marks 0, control_marks 2, score 9.2 - exact match to
 * wp1/study-subset/units.csv row 117; every other function in
 * server.cpp exceeds min_loc and is claimed by its own
 * units.csv row - the server functions are not corpus units of
 * this batch).
 *
 * server.cpp - the Mumps sockets/IPC server: message-queue and
 * unix-socket plumbing. The residue carries the message layout
 * and the timestamp helper.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h004 globals_ext       : the SERVERBUF pin and the
 *    mymsgbuf layout are the file-scope data carriers; the
 *    SERVERBUF pin rides this unit (file-private const), and the
 *    struct maps to the Mymsgbuf class (each field registered
 *    with its vendor source line).
 *  - h007 char_buffer       : the msgbuf fields are fixed C
 *    arrays - CharArray(SERVERBUF+1)/CharArray(SERVERBUF)/
 *    CharArray(108) carriers, fresh per instance (C struct
 *    copies are value copies; the class is the reference form of
 *    the same layout, instantiation notes it).
 *  - h008 static_scratch    : logtime's "static char tm[64]"
 *    persists across calls - carried on the LogtimeScratch
 *    object (MaxScratch precedent, unit 88).
 *  - h009 format_string     : ctime(&t) rendered via the
 *    C-locale formatter (CopyrightNotice precedent, B1 unit 1):
 *    "Www Mmm dd hh:mm:ss yyyy\n", day-of-month space-padded;
 *    the trailing newline is stripped by the vendor's
 *    tm[strlen(tm)-1]=0 - kept.
 *  - h014 out_of_scope_call : none in the residue - time/ctime
 *    are C library edges (Date/Locale.US); the socket/msgque
 *    includes are reference-build visibility text (below).
 *  - h017 naming            : mymsgbuf -> Mymsgbuf; logtime keeps
 *    the vendor spelling; the struct socketdata nested name
 *    keeps vendor lowercase (nested inside the carrier).
 *  - h018 preprocessor_conditional: the include set resolves
 *    against the reference build (server.cpp is built by the
 *    vendor Makefile's library build; the socket/ipc/msg.h
 *    system headers are C library surfaces with no corpus
 *    translation - noted as roster text; errno/poll/fcntl ride
 *    the same note).
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - logtime() returns the STATIC tm - the caller's pointer
 *    aliases the next call's buffer; kept via the scratch object
 *    (the honest observable).
 *  - The queue message layout is byte-counted: buff0 is
 *    SERVERBUF+1 bytes, buff1 is SERVERBUF, and the socketname
 *    member is the unix-socket 108-name-length shape (socket.h
 *    sun_path) - kept verbatim.
 *  - The vendor's struct tag is mymsgbuf with a nested
 *    anonymous-style struct socketdata member named socketinfo -
 *    the class keeps the nested shape (SocketData inside
 *    Mymsgbuf).
 */
private const val SERVERBUF = 1024 // vendor server.cpp (define)

/**
 * vendor struct mymsgbuf - the message-queue message:
 * long cid (Client ID); char buff0[SERVERBUF+1] (Buf0 sent from
 * client); char buff1[SERVERBUF] (Buf1 sent from client); struct
 * socketdata socketinfo { int socket; char socketname[108]; }
 * (Socket Data needed to send data back to client).
 */
class Mymsgbuf {
    /** Client ID (vendor: long cid). */
    var cid: Long = 0
    /** Buf0 sent from client (char buff0[SERVERBUF+1]). */
    val buff0 = CharArray(SERVERBUF + 1)
    /** Buf1 sent from client (char buff1[SERVERBUF]). */
    val buff1 = CharArray(SERVERBUF)
    /** Socket Data needed to send data back to client. */
    val socketinfo = SocketData()

    /** vendor struct socketdata { int socket; char socketname[108]; }. */
    class SocketData {
        var socket: Int = 0
        val socketname = CharArray(108)
    }
}

/** vendor: static char tm[64] in logtime() - persists across calls (h008). */
object LogtimeScratch {
    val tm = CharArray(64)
}

/** vendor server.cpp - char * logtime(): ctime of now, newline stripped,
 *  returned as the static buffer. */
fun logtime(): CharArray {
    val tm = LogtimeScratch.tm

    // vendor: time(&t); strcpy(tm,ctime(&t)); tm[strlen(tm)-1]=0;
    val t = Date()
    val s = String.format(
        Locale.US, "%ta %tb %2te %tT %tY\n",
        t, t, t, t, t
    )
    var i = 0
    while (i < s.length) {
        tm[i] = s[i]
        i++
    }
    tm[s.length - 1] = NUL // vendor: tm[strlen(tm)-1]=0;

    return tm
}
