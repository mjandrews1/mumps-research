package doc

// Unit 179 (B6 tranche 3): server.cpp::main (batch 6, complex
// stratum; roster row 179: loc 65, score 51.5, batch_plan strata
// order). The transaction server: the -j journal scan, the
// SysV message queue and AF_UNIX datagram socket setup, and
// the while(1) transaction loop - receive, journal-log the
// STORE/KILL arms, dispatch the Mglobal call, send the pair of
// datagrams back. One main per package: the MumpsMainUnit150
// render owns the mumps.cpp main; this render is the entry
// serverMain (vendor-name-in-header precedent, H-017). Seat
// table: Mglobal is the unit-167 REAL render (the CharArray
// seat); the message-queue struct and logtime ride the unit-101
// ServerMisc carriers (SERVERBUF is file-private there, so this
// file carries its own SERVERBUF pin; Mymsgbuf reused); STORE
// and GKILL ride the Carriers defines (defines.h: STORE 1,
// GKILL 3); the OS syscall layer (ftok, msgget, msgrcv, socket,
// sendto, perror and the exit codes) renders as PER-FILE
// PRIVATE STUB CARRIERS that throw NotImplementedError -
// process-level OS surface is never arm-owned (the TimeOut_
// getstr1 unit-170 and u161 server precedents). h012: the
// vendor reads the UNINITIALIZED local type at msgrcv and the
// never-filled buf0/buf1 locals in the journal arms (the
// actual client data copies are vendor commented text) - the
// uninitialized reads quantize to deterministic zero values
// (type 0, fresh zero CharArrays; the stale-read precedent).
// h013: the int sigint(int) declaration, the unused Flush/e/_
// cid/socket/sck/nbytes/result locals and the commented
// fossil lines are dropped or noted; the post-while(1) tail
// (close(ssock), GlobalClose, return 0) is unreachable vendor
// text - the GlobalClose splice stays owned by its macro
// render (LibmpscppMisc5), the tail dropped per h013.
// VENDOR TYPO kept and noted: strcpy(journal, argv[i+i]) - the
// second subscript is i+i, not i+1 (the C reads an adjacent
// or out-of-range argv slot; the out-of-range read quantizes
// to the empty string, the deterministic-UB precedent). The
// vendor net-init banner comment (a run of dollar chars in the
// source) is reworded here per the arm hygiene rule. Naming
// per H-017.

private const val SERVERBUF_MAIN = 1024 // server.cpp:36 define SERVERBUF

// vendor length = sizeof(struct mymsgbuf) - sizeof(long): the
// x86-64 gcc layout is 8 (long cid) + 1025 (buff0) + 1024
// (buff1) + int socket (4-aligned to 2060) + 108 socketname =
// 2168, struct alignment 8 rounds sizeof to 2176, minus the
// long = 2168. sizeof is a compile-time C property carried as
// this constant (the u161 MSG_LENGTH precedent).
private const val MSG_LENGTH_MAIN = 2168

// include file-scope defines (jitless-file-private carriers)
private const val AF_UNIX_MAIN = 1 // sys/socket.h AF_UNIX
private const val SOCK_DGRAM_MAIN = 2 // sys/socket.h SOCK_DGRAM
private const val IPC_CREAT_MAIN = 512 // sys/ipc.h IPC_CREAT (0o1000)
private const val QPERM_MAIN = 0 // msgget 0777 permission bits (carried)

// PER-FILE PRIVATE OS STUB CARRIERS (never arm-owned)
private fun ftokC(path: String, id: Char): Int =
    throw NotImplementedError("ftok: process-level OS surface")

private fun msggetC(key: Int, flags: Int, perm: Int): Int =
    throw NotImplementedError("msgget: process-level OS surface")

private fun msgrcvC(qid: Int, buf: Mymsgbuf, length: Int, type: Int, flags: Int): Int =
    throw NotImplementedError("msgrcv: process-level OS surface")

private fun socketC(domain: Int, typ: Int, protocol: Int): Int =
    throw NotImplementedError("socket: process-level OS surface")

private fun sendtoC(sock: Int, buf: CharArray, len: Int, flags: Int, sunPath: CharArray, addrLen: Int): Int =
    throw NotImplementedError("sendto: process-level OS surface")

private fun perrorExit(msg: String): Nothing {
    println(msg + ": process-level OS surface")
    kotlin.system.exitProcess(1)
}

fun serverMain(args: Array<String>): Int {
    val argc = args.size
    val buf0 = CharArray(SERVERBUF_MAIN + 1) // fresh zero (h012)
    val buf1 = CharArray(SERVERBUF_MAIN)     // fresh zero (h012)
    var journal = "" // char journal[1024] folds to String (H-008)
    var jflag = 0
    var journalWriter: java.io.FileWriter? = null // FILE *Journal

    // vendor: int length = sizeof(struct mymsgbuf) - sizeof(long)
    val length = MSG_LENGTH_MAIN

    println("Server started")

    var i = 0
    while (i < argc) {
        if (args[i] == "-j") {
            if (argc >= i + 2) {
                // VENDOR TYPO kept: argv[i+i], not argv[i+1]; the
                // out-of-range read quantizes to "" (deterministic
                // UB, stale-read precedent)
                journal = if (i + i < argc) args[i + i] else ""
                jflag = 1
            } else {
                println("incorrectly specified journal")
                return 0
            }
        }
        i++
    }

    if (jflag != 0) {
        // fopen(journal, "a") with the vendor print-and-continue
        // NULL path; the later fprintf on the NULL FILE rides the
        // nullable carrier below
        journalWriter = try {
            java.io.FileWriter(journal, true)
        } catch (e: java.io.IOException) {
            println("Journal error")
            null
        }
        println(journal + " journal started...")
    }

    // net initialization goes here (vendor banner reworded)

    // generate unique key for message queue using file and id char
    val msgkey = ftokC("/tmp/mumps.tmp", 'A')

    // create message queue and return queue id
    val qid = msggetC(msgkey, IPC_CREAT_MAIN or QPERM_MAIN, 0)

    println(logtime() + " Begin listening....")

    // open socket
    val ssock = socketC(AF_UNIX_MAIN, SOCK_DGRAM_MAIN, 0)

    if (ssock < 0) {
        perrorExit("server socket")
    }

    // message loop
    while (true) { // repeat as necessary
        // the transaction read: the msgrcv type read is the
        // UNINITIALIZED local of the vendor, quantized to 0
        // (h012); the client fields come in fresh zero state
        val type = 0
        var clientMsg = Mymsgbuf(
            cid = 0L,
            buff0 = CharArray(SERVERBUF_MAIN + 1),
            buff1 = CharArray(SERVERBUF_MAIN),
            socketinfo = Socketdata(socket = 0, socketname = CharArray(108))
        )

        if (msgrcvC(qid, clientMsg, length, type, 0) != -1) {
            // journal arms read the never-filled buf0/buf1
            // locals (h012 fresh-zero quantization)
            if (jflag != 0) {
                val w = journalWriter
                    ?: error("journal fprintf on a NULL FILE (vendor path)")
                if (buf0[0] == STORE.toChar()) {
                    w.write(
                        buf0[0].code.toString() + " " +
                                CstringOps.cstringAt(buf0, 1) + "\n" +
                                CstringOps.cstring(buf1) + "\n"
                    )
                } else if (buf0[0] == GKILL.toChar()) {
                    w.write(
                        buf0[0].code.toString() + " " +
                                CstringOps.cstringAt(buf0, 1) + "\n"
                    )
                }
            }

            // Mglobal dispatch on the live client message (the
            // real unit-167 render; the ampersand-offset key
            // rides the slice carrier)
            clientMsg.buff0[0] = Mglobal(
                clientMsg.buff0[0].code,
                sliceTail(clientMsg.buff0, 1),
                clientMsg.buff1,
                Globals.svPtr
            ).toChar()

            // send buff0 and buff1 back to the client (the vendor
            // two-sendto arms; nm.sun_family/sun_path carriers)
            val sunPath = CharArray(108)
            CstringOps.copyAt(sunPath, 0, clientMsg.socketinfo.socketname)

            if (sendtoC(ssock, clientMsg.buff0, SERVERBUF_MAIN + 1, 0, sunPath, 110) < 0) {
                perrorExit("Sending Response")
            }

            if (sendtoC(ssock, clientMsg.buff1, SERVERBUF_MAIN, 0, sunPath, 110) < 0) {
                perrorExit("Sending Response")
            }

            // vendor: the loop head re-declares clientMsg per
            // iteration (fresh zero state, kept)
        }
    }

    // unreachable vendor tail (close(ssock); GlobalClose;
    // return 0) dropped per h013 - the GlobalClose splice stays
    // owned by its macro render (LibmpscppMisc5)
}

// the &buf[offset] pointer carrier (H-012): a fresh buffer
// holding the tail of buf from offset (NUL kept in place)
private fun sliceTail(buf: CharArray, offset: Int): CharArray =
    buf.copyOfRange(offset, buf.size)
