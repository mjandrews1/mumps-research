package doc

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Unit 101 (B4 tranche 1): server.cpp::<misc> (batch 4, simple
// stratum; LOC 60, score 9.2). The file-residue chunk that survived the
// min-loc cut: the July-2012-era GPL banner (Sockets implementation by
// Ben Fain, kept verbatim), the include block, the SERVERBUF constant,
// the message-queue struct, and logtime(). The file's large server
// bodies (mumps_open etc.) are separate corpus units in later batches
// and ride their owner files.
//
// include / platform ledger (vendor residual chunk):
//   stdio, unistd, sys/socket, sys/un, string, sys/types, sys/stat,
//   fcntl, poll, errno, stdlib, sys/ipc, sys/msg   POSIX socket and
//   SysV message-queue machinery with no JVM analog inside this arm's
//   translation scope; the corpus units that use them carry their own
//   IO edges. mumpsc/defines.h rides the arm constants; mumpsc/
//   libmpscpp.h rides the libmpscpp carriers.
//
// H-004: the file-scope struct mymsgbuf (server.cpp:30) renders as the
// Mymsgbuf data class with the nested Socketdata carrier, fields in the
// vendor's declaration order, CharArray for the fixed byte buffers.
//
// logtime (server.cpp:55): H-008 - the C static char tm[64] shared
// scratch returned to callers folds to a fresh String per call (no
// caller can alias the old buffer in this arm's render). H-009: the
// ctime shape renders via SimpleDateFormat with explicit Locale.US;
// the vendor ctm[strlen(tm)-1]=0 newline strip is folded into the
// format (no newline is emitted), noted here because it changes the
// emitted-byte path, not the emitted bytes.

private const val SERVERBUF = 1024

data class Socketdata(
    val socket: Int,           // server.cpp struct socketdata int socket
    val socketname: CharArray, // server.cpp char socketname[108]
)

data class Mymsgbuf(
    val cid: Long,                 /* Client ID */
    val buff0: CharArray,          /* Buf0 sent from client (SERVERBUF+1) */
    val buff1: CharArray,          /* Buf1 sent from client (SERVERBUF) */
    val socketinfo: Socketdata,    /* Socket Data needed to send data back */
)

fun logtime(): String {
    val f = SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy", Locale.US)
    return f.format(Date())
}
