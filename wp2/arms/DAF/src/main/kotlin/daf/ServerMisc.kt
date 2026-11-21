package daf

// Unit 101 (DAF B4 T1): server.cpp residue (<misc>, 60 LOC): the
// sockets-server banner include set (sockets/sys-msg/poll/defines),
// the SERVERBUF const, the mymsgbuf message struct (with the nested
// socketdata struct), and logtime(). The message struct renders as
// data classes (BASE-log row 68 struct-by-value->class-instance
// consulted, hit-reuse; the fixed char arrays buff0[SERVERBUF+1] /
// buff1[SERVERBUF] fold onto String carriers with the size contract
// noted - the queue protocol buffers them, the sizes are protocol
// constants). logtime(): ctime + strip-trailing-newline: BASE-log
// row 3 consulted (hit-rel to be logged as hit-adapt: the ctime
// format string EEE MMM dd HH:mm:ss yyyy + Locale.US is applied
// WITHOUT the trailing newline the vendor strips anyway); the static
// char tm[64] return folds to a fresh String (rows 56/112 family).
// Dafny: EXEMPT (clock + socket struct residue; journal row).

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

const val SERVERBUF_D = 1024 // server.cpp: define SERVERBUF 1024

data class SocketDataD(val socket: Int, val socketname: String)

data class MyMsgBufD(
    val cid: Long,            /* Client ID */
    val buff0: String,         /* Buf0 sent from client (SERVERBUF+1) */
    val buff1: String,        /* Buf1 sent from client (SERVERBUF) */
    val socketinfo: SocketDataD, /* socket data to send back to client */
)

fun logtime(): String {
    val fmt = SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy", Locale.US)
    val t = fmt.format(Date()) // ctime(&t); tm[strlen(tm)-1]=0 strips the \n
    return t
}
