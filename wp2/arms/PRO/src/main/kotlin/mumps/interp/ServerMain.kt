package mumps.interp

import java.io.FileWriter
import java.io.IOException

/**
 * PRO-arm translation of server.cpp::main (B6 unit 161).
 * Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference
 * source lines 71-211.
 *
 * The transaction server: opens the SysV message queue on
 * /tmp/mumps.tmp ('A'), binds an AF_UNIX datagram socket, then
 * loops forever reading client transactions, journaling
 * STORE/KILL commands when -j was given, dispatching each
 * transaction to Mglobal, and sendto()-ing the reply buffers
 * back to the client's socket path.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h003 state_macro    : GlobalClose (vendor 208) rides the
 *    post-loop tail the vendor can never reach (while(1)); the
 *    splice stays owned by the existing globalClose InterpStubs
 *    stub (u130 precedent) and the tail is dropped under h013
 *    below (Kotlin would flag the unreachable code as a
 *    warning). The implicit file-scope svPtr folds to
 *    InterpState.svPtr.
 *  - h004 globals_ext    : svPtr is the single state vector
 *    (InterpState.svPtr, u130 precedent); AllocSV not needed.
 *  - h012 unsigned_byte_domain : buf0/buf1/clientMsg buffers
 *    are C-string/byte domains (Char 0..255); the STORE/KILL
 *    opcodes ride buff0[0] as coded bytes (defines.h:83-86).
 *  - h013 dead_store     : the declaration-only local
 *    "int sigint(int);", the unused Flush, cid/socket/sck/
 *    nbytes/result locals (live only in vendor commented text;
 *    type IS read uninitialized at the msgrcv call and is
 *    quantized to 0 in place - h012 note), and the unreachable
 *    post-loop tail (close(ssock)/GlobalClose/return 0, dead
 *    past while(1)) are dropped with this note, not translated;
 *    free() is GC.
 *  - h014 out_of_scope_call : Mglobal (InterpStubs stub raised
 *    by this unit, owner interp-globalb.cpp::Mglobal, batch-6
 *    corpus unit); logtime (InterpStubs stub raised by this
 *    unit, owner server.cpp::logtime:62, not a corpus unit);
 *    the OS syscall layer - ftok/msgget/msgrcv/socket/sendto/
 *    perror/exit - as per-file private stub carriers (u158
 *    strxcat per-file-stub precedent; process-level OS surface,
 *    never arm-owned).
 *  - h015 grammar_collision: new top-level serverMain plus the
 *    private carriers (MyMsgBuf/SocketData/SockAddrUn/the
 *    syscall stubs) - no package collisions; the raw vendor
 *    net-init banner comment (a run of dollar chars) is
 *    reworded here per the no-raw-dollar scan rule.
 *  - h017 naming         : main -> serverMain (u130 one-main-
 *    per-package precedent; vendor name in this header).
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The -j argument scan uses argv[i+i] (NOT argv[i+1]) when
 *    copying the journal filename - a vendor typo kept verbatim
 *    (it reads argv[2*i], in bounds only by luck for -j at
 *    argv[1] with the filename at argv[2]).
 *  - The journal fprintf arms read buf0/buf1, which the LIVE
 *    code path never fills (the buf copies are vendor commented
 *    text; the live message path uses clientMsg) - C reads
 *    uninitialized stack. Quantized to fresh zero-filled
 *    buffers (u128 deterministic UB quantization precedent),
 *    so the journal arms see opcode 0 and empty strings.
 *  - fopen failure only prints "Journal error" and CONTINUES -
 *    the later journal fprintf on the NULL FILE pointer is
 *    quantized with an error() carrier (u137/u158 precedent),
 *    not a crash.
 *  - length is sizeof(struct mymsgbuf)-sizeof(long): the C
 *    struct layout (x86-64 gcc: 8-byte cid + 1025 buff0 + 1024
 *    buff1 + pad-to-4 socketinfo at 2060 + 112 socketinfo,
 *    struct padded to 2176) gives 2168 - carried as a named
 *    constant with this computation, since sizeof is a
 *    compile-time C property.
 *  - The vendor's many commented-out alternate forms (the
 *    msgrcv result capture, the memcpy buf copies, the
 *    strcpy(nm.sun_path) variant, the second sendto comment)
 *    are kept as roster comments at their flow positions.
 *  - The vendor's "net initialization goes here" banner (line
 *    109-111, with its dollar-run filler) is kept as a comment
 *    with the filler reworded.
 */

private const val SERVERBUF = 1024 // server.cpp:47

// defines.h:83-86 (RETRIEVE 0, STORE 1, KILL 3, CLOSE 4) - the
// journal arms test STORE/KILL on buff0[0].
private const val STORE = 1 // defines.h:84
private const val KILL = 3 // defines.h:86

// msgget flags: IPC_CREAT | 0777 (server.cpp:119)
private const val IPC_CREAT = 512 // 01000
private const val MSG_PERM = 511 // 0777

// AF_UNIX datagram (server.cpp:125)
private const val AF_UNIX = 1
private const val SOCK_DGRAM = 2 // sys/socket.h

/**
 * sizeof(struct mymsgbuf) - sizeof(long) per the x86-64 gcc
 * struct layout (see KDoc); vendor server.cpp:81.
 */
private const val MSG_LENGTH = 2168

/** struct socketdata (server.cpp:56-59). */
private class SocketData(val socket: Int, val socketname: CharArray)

/** struct mymsgbuf (server.cpp:52-60). */
private class MyMsgBuf(val cid: Long, val buff0: CharArray, val buff1: CharArray,
                       val socketinfo: SocketData)

/** struct sockaddr_un (sun_family + sun_path[108]). */
private class SockAddrUn(var sun_family: Int, val sun_path: CharArray)

// --- per-file OS-surface carriers (h014, u158 strxcat precedent) ---

/** ftok(path, id): SysV IPC key from file+char (server.cpp:115). */
private fun ftok(path: String, id: Char): Long =
    TODO("h014 stub: OS syscall layer - SysV IPC ftok, not a corpus unit")

/** msgget(key, flags): create/attach the message queue (server.cpp:119). */
private fun msgget(key: Long, flags: Int): Int =
    TODO("h014 stub: OS syscall layer - SysV msgget, not a corpus unit")

/** msgrcv(qid, msg, length, type, flags) (server.cpp:155). */
private fun msgrcv(qid: Int, msg: MyMsgBuf, length: Int, type: Int, flags: Int): Int =
    TODO("h014 stub: OS syscall layer - SysV msgrcv, not a corpus unit")

/** socket(domain, type, protocol): AF_UNIX datagram (server.cpp:125). */
private fun socket(domain: Int, type: Int, protocol: Int): Int =
    TODO("h014 stub: OS syscall layer - AF_UNIX datagram socket, not a corpus unit")

/** sendto(fd, buf, len, flags, addr, addrlen) (server.cpp:192,198). */
private fun sendto(fd: Int, buf: CharArray, len: Int, flags: Int,
                   addr: SockAddrUn, addrlen: Int): Int =
    TODO("h014 stub: OS syscall layer - sendto, not a corpus unit")

/** perror(s): stderr errno message (server.cpp:128,193,199). */
private fun perror(s: String): Unit =
    TODO("h014 stub: OS/libc perror, not a corpus unit")

/** exit(status): process termination (server.cpp:129,194,200). */
private fun exit(status: Int): Nothing =
    TODO("h014 stub: OS exit, not a corpus unit")

fun serverMain(args: Array<String>) {
    val svPtr = InterpState.svPtr // h004: vendor implicit file-scope svPtr

    val buf0 = CharArray(SERVERBUF + 1) // h012: never filled by the live path - see KDoc UB note
    val buf1 = CharArray(SERVERBUF)
    val argc = args.size

    print("Server started\n") // printf("Server started\n")

    var jflag = false
    var journal: String? = null
    for (i in 0 until argc) {
        if (args[i] == "-j") { // strcmp(argv[i],"-j")==0
            if (argc >= i + 2) {
                // vendor typo kept: argv[i+i], not argv[i+1]
                journal = args[i + i]
                jflag = true
            } else {
                print("incorrectly specified journal\n")
                return
            }
        }
    }

    var Journal: FileWriter? = null
    if (jflag) {
        val jname = journal ?: error("-j given without a journal name")
        Journal = try {
            FileWriter(jname, true) // fopen(journal,"a")
        } catch (e: IOException) {
            null
        }

        if (Journal == null) {
            print("Journal error\n") // vendor continues after the error
        }

        print(jname + " journal started...\n") // printf("%s journal started...\n",journal)
    }

    // net initialization goes here (vendor banner, filler reworded)

    // generate unique key for message queue using file and arbitrary char
    val msgkey = ftok("/tmp/mumps.tmp", 'A')

    // create message queue and return queue id
    val qid = msgget(msgkey, IPC_CREAT or MSG_PERM)

    print(String(logtime()) + " Begin listening....\n") // printf("%s Begin listening...\n",logtime())

    // open socket
    val ssock = socket(AF_UNIX, SOCK_DGRAM, 0)
    if (ssock < 0) {
        perror("server socket")
        exit(1)
    }

    val nm = SockAddrUn(0, CharArray(108)) // struct sockaddr_un nm

    // message loop
    while (true) { // repeat as necessary
        // detect a shutdown signal and exit this loop to perform GlobalClose

        val clientMsg = MyMsgBuf(0L, CharArray(SERVERBUF + 1), CharArray(SERVERBUF),
            SocketData(0, CharArray(108))) // struct mymsgbuf clientMsg
        val type = 0 // vendor reads the UNINITIALIZED local type at the
        // msgrcv call site (UB, quantized to 0 - h012 note)

        // read transaction from client here into buf0 and buf1 code goes here
        // (vendor 149-152 commented declarations dropped - h013)

        if (msgrcv(qid, clientMsg, MSG_LENGTH, type, 0) != -1) {
            // result/msgrcv capture, the memcpy buf copies and the
            // socket/name copies are vendor commented text (156-162)

            if (jflag) {
                val j = Journal ?: error("journal FILE carrier") // fprintf(NULL) UB quantized (h012 note)
                // fprintf(Journal,"%d %s\n%s\n",(int)buf0[0],&buf0[1],buf1)
                if (buf0[0].code == STORE) {
                    j.write("" + buf0[0].code + " " + cstringAt(buf0, 1) + "\n" + cstringAt(buf1, 0) + "\n")
                } else if (buf0[0].code == KILL) {
                    // fprintf(Journal,"%d %s\n",(int)buf0[0],&buf0[1])
                    j.write("" + buf0[0].code + " " + cstringAt(buf0, 1) + "\n")
                }
            }

            // e=Mglobal((int)buf0[0], ...) is vendor commented text (170)

            // vendor 172: buff0[0] = Mglobal((int)buff0[0], &buff0[1], buff1, svPtr)
            clientMsg.buff0[0] = Mglobal(clientMsg.buff0[0].code, clientMsg.buff0, 1,
                clientMsg.buff1, svPtr).toChar()

            // buf0[0]=e / clientMsg.buff0[0]=e are vendor commented text (174-175)

            // --- leave this code alone

            // send buf0 and buf1 back to client code goes here
            // (the memcpy reply copies are vendor commented text, 183-184)

            nm.sun_family = AF_UNIX

            for (k in 0 until 108) { // memcpy(nm.sun_path, socketname, sizeof(name))
                nm.sun_path[k] = clientMsg.socketinfo.socketname[k]
            }
            // strcpy(nm.sun_path,name) variant is vendor commented text (189)

            if (sendto(ssock, clientMsg.buff0, SERVERBUF + 1, 0, nm, 110) < 0) {
                perror("Sending Response")
                exit(1) // exit(EXIT_FAILURE)
            }

            // the buf1 sendto arm is vendor commented text (197)

            if (sendto(ssock, clientMsg.buff1, SERVERBUF, 0, nm, 110) < 0) {
                perror("Sending Response")
                exit(1) // exit(EXIT_FAILURE)
            }
        }
    }

    // close(ssock) - unreachable past the vendor infinite loop;
    // the socket close, GlobalClose splice (globalClose stub,
    // h003) and the return 0 sit past while(1) and are dead -
    // kept as this comment per h013.
}
