package both

// Unit 179 (BOTH B6 T3, complex): server.cpp::main (65 LOC).
// Batch_plan ordinal 179: complex, LOC 65, rank 235. The DBMS
// client/server demo server: the SysV message queue listener
// that serves Mglobal transactions to socket clients (the demo
// client/server family of the source distribution; the SQLITE
// reference build never routes it - the server TU is the
// CLIENT/NATIVE-queue arm companion; the live corpus render
// keeps the unit whole).
//
// Vendor behavior: the journal option (-j argv[i+i] - the
// vendor i+i INDEX BUG kept byte-faithful: the argument copied
// is argv[2i], which is argv[0] at i=0 and past-end at i>0
// quantized to the empty string; journaled); jflag opens the
// append-mode journal (fopen NULL prints "Journal error" and
// CONTINUES - the vendor keeps the NULL file in play; the
// render keeps the nullable Writer seat). logtime() is the
// same-TU helper (server.cpp:62 - static tm[64], ctime strip
// of the trailing newline) rendered REAL below. The queue:
// msgkey = ftok("/tmp/mumps.tmp",'A'); qid = msgget(msgkey,
// IPC_CREAT|0777); the AF_UNIX SOCK_DGRAM socket; msgrcv
// blocked read of struct mymsgbuf (the struct carrier:
// cid/buff0[SERVERBUF+1]/buff1[SERVERBUF]/socketinfo{socket,
// socketname[108]} - server.cpp:52-59; SERVERBUF 1024 at
// server.cpp:47). The served transaction: the journal arms
// test buf0[0] (the vendor's memcpy family is commented out -
// the journal reads the never-filled buf0 scratch, whose
// zeroed seat never matches STORE/KILL: the dead journal arms
// are journaled, the shapes kept); the LIVE call routes
// clientMsg.buff0[0] = Mglobal(buff0[0], &buff0[1], buff1,
// svPtr) (the SQLITE-routing Mglobal of the T2 flip; the int
// return FOLDS TO THE BYTE - the clientMsg.buff0[0] byte-
// truncating assignment kept); the reply sendto pair (buff0 then
// buff1) on the nm sockaddr (sun_family AF_UNIX, sun_path from
// clientMsg.socketinfo.socketname) with perror+exit on failure.
// The loop never exits in the vendor (while(1); the shutdown-
// signal comment is dead); close(ssock) + GlobalClose (the
// Mglobal(CLOSE) macro fold) seat the unreachable tail.
//
// Quirks kept (ledgered):
//   - the argv[i+i] journal index bug (byte-faithful).
//   - the dead buf0/buf1 journal arms (the memcpy family is
//     commented out in the vendor; the zero scratch never fires
//     STORE/KILL - shapes kept, effect journaled).
//   - type/sck/nbytes/cid/result/Flush/sigint-decl are dead
//     locals (h013); the int socket local shadows the socket() call in
//     the vendor - dropped as dead (h013) with the rename note.
//   - int type passes uninitialized to msgrcv in the vendor
//     (UB) - the render seats 0 (the quantized fold).
//   - perror("server socket")/exit(1) and the two sendto
//     perror("Sending Response")/exit tails keep exact strings.
//
// BOTH consults (find_rule before render, events logged):
//   h001 io_stream_param - reused: the printf seats are
//     System.out; Journal is the append Writer edge
//     (java.io.FileWriter(append) seat); perror the stderr
//     print seat; exit folds exitProcess.
//   h004 globals_ext - reused: the svPtr singleton routes the
//     Mglobal call (the T2 SQLITE flip).
//   h009 format_string - reused: the journal fprintf shapes
//     (%d space key newline data newline / the %d-only KILL
//     arm) and the started/listening print shapes.
//   h013 dead_store - hit: the dead locals and the shadowing
//     socket name dropped; the commented-out memcpy family
//     stays ledger notes.
//   h014 out_of_scope_call - reused: Mglobal REAL (T2 flip);
//     the POSIX IPC/socket family seats as platform stubs
//     (ftok/msgget/msgrcv/socket/sendto/close - non-corpus
//     libc edges, the selectStdinC precedent); GlobalClose
//     folds to Mglobal(CLOSE).
//   h015 wrap - hit: this file.
//   h017 naming - hit: the vendor names kept (msgrcv family
//     camel- seat names carry the C-edge provenance notes).
//   h019 class_member_carrier - reused: the MyMsgBuf carrier
//     below (the struct field set exact).
//   h020 pointer_handle - reused: the char arrays and the
//     sockaddr_un sun_path memcpy seat through array copies.
//   h021 struct_carrier - reused: struct mymsgbuf and the
//     nested struct socketdata seat at server.cpp:52-59.
//   h011 goto - not consulted (no goto family in this chunk).

internal const val SERVERBUF = 1024 // server.cpp:47

// struct mymsgbuf - server.cpp:52 (with the nested struct
// socketdata at :56-59; the long cid head the vendor subsumes
// in the msg traffic).
internal class MyMsgBuf {
    val cid = LongArray(1) // long cid (the msg header seat)
    val buff0 = CharArray(SERVERBUF + 1) // char buff0[SERVERBUF+1]
    val buff1 = CharArray(SERVERBUF) // char buff1[SERVERBUF]
    var socket: Int = 0 // socketinfo.socket
    val socketname = CharArray(108) // socketinfo.socketname[108]
}

// struct sockaddr_un fold (sun_family + sun_path[108]).
internal class SockaddrUn {
    var sunFamily: Int = 0
    val sunPath = CharArray(108)
}

// char * logtime(void) - server.cpp:62, the same-TU helper:
// ctime + the trailing-newline strip. Rendered REAL (same-file
// rationale of the keyfix precedent; the static tm[64] seat is
// the fresh-string fold - the vendor's static buffer returns
// the same pointer each call; Kotlin strings are immutable so
// the aliasing dead-ends, journaled).
internal fun logtime(): String {
    val t = java.time.Instant.now()
    // ctime shape: "Www Mmm dd hh:mm:ss yyyy\n" - the strip
    // removes the newline; the render seats the same shape
    // through toString + substring 0..24 (24 fixed-width).
    val c = java.util.Date.from(t).toString() // EEE pattern area
    val sb = StringBuilder()
    // Day-of-week and month abbreviations fold through the
    // three-letter ASCII calendar names (ctime locale C).
    val days = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
    val months = listOf(
        "Jan", "Feb", "Mar", "Apr", "May", "Jun",
        "Jul", "Aug", "Sep", "Oct", "Nov", "Dec",
    )
    val cal = java.util.Calendar.getInstance()
    cal.timeInMillis = t.toEpochMilli()
    val two: (Int) -> String = { v -> if (v < 10) "0" + v else v.toString() }
    sb.append(days[cal.get(java.util.Calendar.DAY_OF_WEEK) - 1])
    sb.append(' ')
    sb.append(months[cal.get(java.util.Calendar.MONTH)])
    sb.append(' ')
    sb.append(two(cal.get(java.util.Calendar.DAY_OF_MONTH)))
    sb.append(' ')
    sb.append(two(cal.get(java.util.Calendar.HOUR_OF_DAY)))
    sb.append(':')
    sb.append(two(cal.get(java.util.Calendar.MINUTE)))
    sb.append(':')
    sb.append(two(cal.get(java.util.Calendar.SECOND)))
    sb.append(' ')
    sb.append(cal.get(java.util.Calendar.YEAR))
    return sb.toString() // tm[strlen(tm)-1]=0 fold (no newline)
}

internal fun serverMain(args: Array<String>): Int {
    val argc = args.size
    val buf0 = CharArray(SERVERBUF + 1) // char buf0[SERVERBUF+1]
    val buf1 = CharArray(SERVERBUF) // char buf1[SERVERBUF]
    var journal = "" // char journal[1024]
    var jflag = 0
    var Journal: java.io.Writer? = null // FILE *Journal

    System.out.print("Server started\n")

    for (i in 0 until argc) {
        if (args[i] == "-j") {
            if (argc >= i + 2) {
                // strcpy(journal, argv[i+i]) - the i+i index
                // bug byte-faithful (quantized to the empty
                // string past the argv end; journaled).
                journal = if (i + i < argc) args[i + i] else ""
                jflag = 1
            } else {
                System.out.print("incorrectly specified journal\n")
                return 0
            }
        }
    }

    if (jflag != 0) {
        Journal = try {
            java.io.FileWriter(journal, true) // fopen(a)
        } catch (e: java.io.IOException) {
            null // fopen NULL
        }
        if (Journal == null) {
            System.out.print("Journal error\n")
        }
        System.out.print(journal + " journal started...\n")
    }

    // msgkey = ftok("/tmp/mumps.tmp", 'A')
    val msgkey = ftokC("/tmp/mumps.tmp", 'A')

    // qid = msgget(msgkey, IPC_CREAT | 0777)
    val qid = msggetC(msgkey, 0x1ff) // IPC_CREAT|0777 octal 077

    System.out.print(logtime() + " Begin listening....\n")

    // int ssock = socket(AF_UNIX, SOCK_DGRAM, 0)
    val ssock = socketAFUnixDgram()

    if (ssock < 0) {
        perrorC("server socket")
        kotlin.system.exitProcess(1)
    }

    val clientMsg = MyMsgBuf() // struct mymsgbuf clientMsg
    val nm = SockaddrUn() // struct sockaddr_un nm
    val length = SERVERBUF + 1 + SERVERBUF + 108 // sizeof fold
    var type = 0 // uninitialized int in the vendor (UB->0 seat)

    while (true) { // repeat as necessary
        if (msgrcvC(qid, clientMsg, length, type, 0) != -1) {
            if (jflag != 0 && Journal != null) {
                // the journal arms read the never-filled buf0
                // scratch (the vendor memcpy family commented
                // out; the zeroed seat never matches - the
                // shapes kept, journaled).
                if (buf0[0].code == STORE) {
                    try {
                        Journal.write(
                            buf0[0].code.toString() + " " +
                                cStringAt(buf0, 1) + "\n" +
                                cStringOf(buf1) + "\n",
                        )
                    } catch (e: java.io.IOException) {
                        // the vendor fprintf return is unread
                    }
                } else if (buf0[0].code == KILL) {
                    try {
                        Journal.write(
                            buf0[0].code.toString() + " " +
                                cStringAt(buf0, 1) + "\n",
                        )
                    } catch (e: java.io.IOException) {
                        // unread fprintf return
                    }
                }
            }

            // clientMsg.buff0[0] = Mglobal((int)buff0[0],
            //   (unsigned char *)&buff0[1], (unsigned char
            //   *)buff1, svPtr) - the int return folds to the
            // byte (the truncating assignment kept).
            val e = Mglobal(
                clientMsg.buff0[0].code,
                keyOffset(clientMsg.buff0, 1),
                clientMsg.buff1,
                svPtr,
            )
            clientMsg.buff0[0] = (e and 0xff).toChar()

            // nm.sun_family = AF_UNIX;
            nm.sunFamily = 1 // AF_UNIX
            // memcpy(nm.sun_path, clientMsg.socketinfo.socketname,
            //   sizeof(name)) - the 108-byte copy shape.
            System.arraycopy(clientMsg.socketname, 0, nm.sunPath, 0, 108)

            if (sendtoC(ssock, clientMsg.buff0, SERVERBUF + 1, nm) < 0) {
                perrorC("Sending Response")
                kotlin.system.exitProcess(1)
            }

            if (sendtoC(ssock, clientMsg.buff1, SERVERBUF, nm) < 0) {
                perrorC("Sending Response")
                kotlin.system.exitProcess(1)
            }
        }
    }

    // unreachable vendor tail (while(1)): close(ssock);
    // GlobalClose; return 0 - the shapes seat below (h011 dead
    // back-edge journal; the loop never exits).
    // close(ssock); Mglobal(CLOSE,...); return 0
}

// key_t ftok(const char *, int) - POSIX message-queue key
// (sys/ipc.h). Platform edge, non-corpus. TODO-stub (h014).
internal fun ftokC(path: String, c: Char): Int {
    TODO("stub: POSIX ftok, sys/ipc.h (platform edge, non-corpus)")
}

// int msgget(key_t, int) - POSIX queue open (sys/msg.h).
// Platform edge, non-corpus. TODO-stub (h014).
internal fun msggetC(key: Int, msgflg: Int): Int {
    TODO("stub: POSIX msgget, sys/msg.h (platform edge, non-corpus)")
}

// ssize_t msgrcv(int, void *, size_t, long, int) - POSIX queue
// read (sys/msg.h); the mymsgbuf carrier fills in place.
// Platform edge, non-corpus. TODO-stub (h014).
internal fun msgrcvC(
    msqid: Int,
    msgp: MyMsgBuf,
    msgsz: Int,
    msgtyp: Int,
    msgflg: Int,
): Int {
    TODO("stub: POSIX msgrcv, sys/msg.h (platform edge, non-corpus)")
}

// int socket(int, int, int) - AF_UNIX SOCK_DGRAM edge
// (sys/socket.h). Platform edge, non-corpus. TODO-stub (h014).
internal fun socketAFUnixDgram(): Int {
    TODO("stub: POSIX socket AF_UNIX SOCK_DGRAM, sys/socket.h (platform edge, non-corpus)")
}

// ssize_t sendto(int, const void *, size_t, int, const struct
// sockaddr *, socklen_t) - POSIX datagram send. Platform edge,
// non-corpus. TODO-stub (h014).
internal fun sendtoC(
    s: Int,
    buf: CharArray,
    len: Int,
    nm: SockaddrUn,
): Int {
    TODO("stub: POSIX sendto, sys/socket.h (platform edge, non-corpus)")
}

// void perror(const char *) - the C stderr perror edge
// (stdio.h). Platform edge, non-corpus. TODO-stub (h014).
internal fun perrorC(s: String) {
    TODO("stub: POSIX perror, stdio.h (platform edge, non-corpus)")
}

// KILL - defines.h GKILL 3 (compiler-macros.h:56; the vendor
// server.cpp includes mumpsc/defines.h; the kill op seat).
internal const val KILL = 3

// The &buf[off] string seat (walks to NUL).
private fun cStringAt(buf: CharArray, off: Int): String {
    val sb = StringBuilder()
    var p = off
    while (buf[p] != '\u0000') {
        sb.append(buf[p])
        p++
    }
    return sb.toString()
}

// The key CharArray seat of &buff0[1] (the array-pair fold the
// Mglobal key parameter takes; the offset-1 name zone rides the
// buff0 array itself and Mglobal reads from index 1 - the
// same-offset fold of the T2 call sites; the copy keeps the
// NUL-terminated span).
private fun keyOffset(buf: CharArray, off: Int): CharArray {
    val len = off + cStringAt(buf, off).length
    val out = CharArray(len + 1)
    System.arraycopy(buf, 0, out, 0, len + 1)
    return out
}
