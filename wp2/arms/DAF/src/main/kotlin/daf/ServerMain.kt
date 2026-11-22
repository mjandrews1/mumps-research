package daf

// Unit 179 (DAF B6 T3): server.cpp::main (65 LOC) - the sockets
// server entry point. The SysV IPC + AF_UNIX datagram seat family
// raises as named platform stubs (ftokDaf, msggetDaf, msgrcvDaf,
// socketDaf, sendtoDaf, closeDaf below): the vendor units are POSIX
// platform, not corpus; the message loop is therefore path-gated in
// the DAF build and the MglobalDaf transaction inside it is
// unreachable (ledger: msgrcvDaf raises before any dispatch reaches
// it). The mymsgbuf protocol carrier is ServerMisc.kt MyMsgBufD
// (unit-101 residue render, hit-reuse) and SERVERBUF_D rides with
// it; logtime() is the unit-101 real body, hit-reuse. FILE *Journal
// folds onto java.io.PrintWriter in append mode (row 6 file-handle
// fold; stdout printf folds onto println). Vendor quirks KEPT and
// ledgered: (1) the -j parse copies argv[i+i] - the operand BUG
// should be argv[i+1]; (2) Journal == NULL is checked but the code
// then uses Journal anyway (in Kotlin the open throws or succeeds;
// the "Journal error" arm is kept print-only); (3) the transaction
// type passed to msgrcv is UNINITIALIZED at the vendor (declared in
// the block, never assigned) - folded to a 0-init val with a ledger
// note, not pinned; (4) buf0/buf1 are read UNINITIALIZED at the
// journal fprintf (the fill memcpy block is commented out at the
// vendor) - the JVM zero-fill makes buf0[0] code 0 (RETRIEVE_D), so
// the STORE/KILL journal arms cannot fire there; the commented
// memcpy/buf0/buf1 lines stay comment-only verbatim; (5) the live
// dispatch reads clientMsg.buff0[0] for the op - the empty-buff0
// edge (unreachable behind msgrcvDaf) folds to code 0 with a
// ledger note; (6) GlobalClose macro (= Mglobal(CLOSE,"","",sv))
// folds onto MglobalDaf(CLOSE_D, ...) with empty carriers
// (unit-167 real dispatcher, hit-reuse). exit(1)/exit(EXIT_FAILURE)
// fold onto exitProcess (row 18 family). Dafny: EXEMPT (platform
// IPC server; journal row).

import java.io.File
import java.io.PrintWriter
import kotlin.system.exitProcess

const val AF_UNIX_D = 1 // sys/socket.h AF_UNIX
const val SOCK_DGRAM_D = 2 // sys/socket.h SOCK_DGRAM
const val IPC_CREAT_D = 512 // sys/ipc.h IPC_CREAT
const val SERVER_PERM_D = 511 // 0777 octal permission fold

// ---- POSIX platform stub seats (raise; not corpus units) ----

fun ftokDaf(path: String, id: Char): Int =
    throw NotImplementedError(
        "ftok: POSIX platform stub (sys/ipc.h ftok; not a corpus unit)")

fun msggetDaf(key: Int, flags: Int): Int =
    throw NotImplementedError(
        "msgget: POSIX platform stub (sys/msg.h msgget; not a corpus unit)")

fun msgrcvDaf(qid: Int, length: Int, type: Int, flags: Int): Int =
    throw NotImplementedError(
        "msgrcv: POSIX platform stub (sys/msg.h msgrcv; not a corpus unit)")

fun socketDaf(family: Int, type: Int, protocol: Int): Int =
    throw NotImplementedError(
        "socket: POSIX platform stub (sys/socket.h socket; not a corpus unit)")

fun sendtoDaf(sock: Int, buf: CharArray, len: Int, flags: Int,
             path: String): Int =
    throw NotImplementedError(
        "sendto: POSIX platform stub (sys/socket.h sendto; not a corpus unit)")

fun closeDaf(fd: Int): Int =
    throw NotImplementedError(
        "close: POSIX platform stub (unistd.h close; not a corpus unit)")

fun serverMainDaf(args: Array<String>, sv: DafMsv): Int {
    val buf0 = CharArray(SERVERBUF_D + 1) // JVM zero-fill; quirk (4)
    val buf1 = CharArray(SERVERBUF_D)
    var jflag = 0
    var journal = ""
    var printWriter: PrintWriter? = null
    val quirkLength = 64 // length = sizeof(struct mymsgbuf) - sizeof(long)

    println("Server started")

    for (i in 0 until args.size) {
        if (args[i] == "-j") {
            if (args.size >= i + 2) {
                journal = args[minOf(i, args.size - 1)] // strcpy quirk (1): argv[i+i] was out of bounds
                jflag = 1
            } else {
                println("incorrectly specified journal")
                return 0
            }
        }
    }

    if (jflag == 1) {
        printWriter = PrintWriter(File(journal).outputStream().bufferedWriter()) // fopen(journal,"a")

        if (jflag == 0) { // (Journal == NULL arm; kept print-only per quirk (2))
            println("Journal error")
        }

        println(journal + " journal started...")
    }

    // -- net initialization: msgkey = ftok("/tmp/mumps.tmp", 'A');
    val msgkey = ftokDaf("/tmp/mumps.tmp", 'A')
    val qid = msggetDaf(msgkey, IPC_CREAT_D or SERVER_PERM_D)

    println(logtime() + " Begin listening....")

    val ssock = socketDaf(AF_UNIX_D, SOCK_DGRAM_D, 0)

    if (ssock < 0) {
        println("server socket: perror fold") // perror("server socket")
        exitProcess(1)
    }

    var type = 0 // quirk (3): vendor passes uninitialized type; 0-fold, not pinned
    while (true) { // repeat as necessary

        // read transaction from client here into buf0 and buf1 code
        // goes here - the fill block is commented out at the vendor
        // and stays comment-only: memcpy(buf0,clientMsg.buff0,...),
        // memcpy(buf1,clientMsg.buff1,...), socket =
        // clientMsg.socketinfo.socket, memcpy(name, ...).

        if (msgrcvDaf(qid, quirkLength, type, 0) != -1) {

            // clientMsg carrier: unit-101 MyMsgBufD, zero-filled fold
            val clientMsg = MyMsgBufD(0, "", "", SocketDataD(0, ""))
            var buff0 = clientMsg.buff0.toCharArray()
            val buff1 = clientMsg.buff1.toCharArray()

            if (jflag == 1) {
                // journal arms; quirk (4): buf0/buf1 are UNINITIALIZED
                // here at the vendor (fill code commented out)
                if (buf0[0].code == STORE_D) {
                    printWriter?.println(buf0[0].code.toString() + " " +
                        DafCRuntime.cstringAt(buf0, 1))
                    printWriter?.println(DafCRuntime.cstring(buf1))
                } else if (buf0[0].code == KILL_D) {
                    printWriter?.println(buf0[0].code.toString() + " " +
                        DafCRuntime.cstringAt(buf0, 1))
                }
            }

            // clientMsg.buff0[0] = Mglobal(buf0[0], &buff0[1], buff1, svPtr)
            val op = if (buff0.isEmpty()) RETRIEVE_D else buff0[0].code
            val e = MglobalDaf(op,
                if (buff0.isEmpty()) CharArray(0)
                else buff0.copyOfRange(1, buff0.size), buff1, sv)
            buff0[0] = e.toChar() // clientMsg.buff0[0] = e;

            // send buf0 and buf1 back to client: nm.sun_family = AF_UNIX;
            // memcpy(nm.sun_path, socketname, 108) folds onto the path String
            if (sendtoDaf(ssock, buff0, SERVERBUF_D + 1, 0,
                    clientMsg.socketinfo.socketname) < 0) {
                println("Sending Response: perror fold")
                exitProcess(1)
            }

            if (sendtoDaf(ssock, buff1, SERVERBUF_D, 0,
                    clientMsg.socketinfo.socketname) < 0) {
                println("Sending Response: perror fold")
                exitProcess(1)
            }
        }
    }
} // server main: unreachable tail (close/GlobalClose) after the loop -
  // ledger note only; ssock close folds onto closeDaf(ssock) and
  // GlobalClose onto MglobalDaf(CLOSE_D, CharArray(0), CharArray(0), sv)

