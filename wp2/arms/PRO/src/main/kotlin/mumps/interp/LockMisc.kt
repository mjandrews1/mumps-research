package mumps.interp

import java.io.File
import java.io.RandomAccessFile
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.PosixFilePermissions

/**
 * PRO-arm translation of interp-lock.cpp::<misc> (pilot B2, unit 56,
 * provisional under deviation C-003). Translated independently from
 * the pristine vendor source (mumps-14-may-2026.tgz, sha256
 * 621deb25...ffdbe), whole-file residual (units.csv: 97 LOC, score
 * 57.5, rank 247): the Mumps file lock library - makeLock creates
 * or removes a global-array node lock and cleanLocks erases every
 * lock held by a process id. Locks live in fixed 528-byte records
 * (struct rec: char pid[16]; char ref[512]) in /tmp/Mumps.Locks.
 * The vendor warns this does not work over NFS (kept here too).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h002 io_primitives     : the raw POSIX open/read/lseek/write/
 *    close/chmod calls map to java.io.RandomAccessFile over the
 *    same path. Divergence note: the JVM cannot express the O_EXCL
 *    open dance - the vendor first opens O_EXCL|O_RDWR and only on
 *    failure retries O_CREAT|O_EXCL|O_RDWR, an atomic create-or-
 *    fail; RandomAccessFile(path, rw) is create-or-open, so the
 *    two opens collapse into one (documented divergence).
 *  - h007 char_buffer       : the struct rec record buffer renders
 *    as a fixed 528-byte ByteArray worked through local helpers
 *    (c-string field reads, space-blank field writes) that mirror
 *    the CstringOps semantics on this byte layout.
 *  - h010 numeric_promotion : off_t seek offsets map to Long;
 *    the octal permission literal 0666 has no Kotlin octal form
 *    and renders as the equivalent Posix permissions rw-rw-rw-
 *    (chmod666 helper, best effort).
 *  - h013 dead_branches     : the vendor's unused locals (int k in
 *    both functions) are dropped with this note; the makeLock
 *    parameters code and timeout are never read by the vendor body
 *    and are kept unused in the signature as there.
 *  - h014 out_of_scope_call : cleanLocks had an InterpStubs stub
 *    (declared mumps.cpp); this unit is the owner - the stub is
 *    REMOVED and the MumpsMain call site now resolves here.
 *  - h017 naming           : makeLock/cleanLocks keep their names;
 *    wrapper file follows the source (interp-lock.cpp -> LockMisc).
 *
 * Vendor quirks kept faithfully:
 *  - cleanLocks blanks a whole matching record; makeLock's '-' arm
 *    blanks only the FIRST character of pid and ref (asymmetric).
 *  - makeLock's '-' arm uses ftell on a fresh fdopen(i, w+) stream
 *    (equals the fd offset) and rewinds by one record size; the
 *    new-lock path instead scans for any record whose ref begins
 *    with five spaces (a freed slot) before appending at EOF.
 *  - the struct initializer pads only the first 15 chars of each
 *    field with spaces; the explicit blanking loops establish the
 *    real record layout, so the initializer quirk is subsumed.
 */

private const val LOCKFILE = "/tmp/Mumps.Locks" // interp-lock.cpp:28

// struct rec: char pid[16]; char ref[512] -> 528-byte fixed record.
private const val PID_OFF = 0
private const val PID_LEN = 16
private const val REF_OFF = 16
private const val REF_LEN = 512
private const val REC_SIZE = PID_LEN + REF_LEN

/** chmod(LOCKFILE, 0666) best effort (h010 octal -> rw-rw-rw-). */
private fun chmod666() {
    try {
        Files.setPosixFilePermissions(
            Path.of(LOCKFILE),
            PosixFilePermissions.fromString("rw-rw-rw-"),
        )
    } catch (e: Exception) {
        // non-POSIX filesystem or race: best effort, as the vendor
        // chmod's return value is also ignored there.
    }
}

/** Reads one 528-byte record; null at clean EOF (C read()==0). */
private fun readRec(raf: RandomAccessFile): ByteArray? {
    val b = ByteArray(REC_SIZE)
    var n = 0
    while (n < REC_SIZE) {
        val r = raf.read(b, n, REC_SIZE - n)
        if (r < 0) {
            // partial record at EOF: C read returns the short count
            // (nonzero) and the loop body runs on stale buf bytes;
            // the zero-padded remainder is the closest rendering.
            return if (n == 0) null else b
        }
        n += r
    }
    return b
}

/** C-string read of one field (up to the first NUL). */
private fun fieldCstring(rec: ByteArray, off: Int, len: Int): String {
    val sb = StringBuilder()
    var i = off
    while (i < off + len && rec[i] != 0.toByte()) {
        sb.append(rec[i].toInt().toChar())
        i++
    }
    return sb.toString()
}

/**
 * C-string write of one field: space-blank to len-1, NUL at len-1,
 * then strcpy(value) overlays the head (value + NUL).
 */
private fun setField(rec: ByteArray, off: Int, len: Int, value: String) {
    for (i in 0 until len - 1) rec[off + i] = ' '.code.toByte()
    rec[off + len - 1] = 0
    for (i in value.indices) rec[off + i] = value[i].code.toByte()
    if (value.length < len) rec[off + value.length] = 0
}

/** strncmp(rec+off, s, n) on the record bytes. */
private fun strncmpRec(rec: ByteArray, off: Int, s: String, n: Int): Int {
    for (i in 0 until n) {
        val a = if (off + i < rec.size) rec[off + i].toInt() else 0
        val b = if (i < s.length) s[i].code else 0
        if (a != b) return a - b
        if (a == 0) return 0
    }
    return 0
}

/** The makeLock match test: strcmp(ref) or either 1-short strncmp. */
private fun lockMatches(recRef: String, ref: String, rec: ByteArray): Boolean {
    if (recRef == ref) return true
    // Empty-string strlen()-1 underflows to a full compare in C,
    // which reduces to the strcmp case already handled above.
    if (recRef.isEmpty() || ref.isEmpty()) return false
    if (strncmpRec(rec, REF_OFF, ref, recRef.length - 1) == 0) return true
    return strncmpRec(rec, REF_OFF, ref, ref.length - 1) == 0
}

/**
 * Removes every lock held by this process id (vendor cleanLocks).
 * Returns "1" on success, "0" when the lock file cannot be opened.
 */
fun cleanLocks(pid: String): String {
    val raf = try {
        RandomAccessFile(File(LOCKFILE), "rw")
    } catch (e: Exception) {
        return "0"
    }
    raf.use {
        while (true) {
            val pos = it.filePointer
            val rec = readRec(it) ?: break
            if (fieldCstring(rec, PID_OFF, PID_LEN) == pid) {
                it.seek(pos)
                val blanked = ByteArray(REC_SIZE)
                for (j in 0 until PID_LEN - 1) blanked[PID_OFF + j] = ' '.code.toByte()
                blanked[PID_OFF + PID_LEN - 1] = 0
                for (j in 0 until REF_LEN - 1) blanked[REF_OFF + j] = ' '.code.toByte()
                blanked[REF_OFF + REF_LEN - 1] = 0
                it.write(blanked)
            }
        }
    }
    chmod666()
    return "1"
}

/**
 * Creates a lock (Lf not '-') or removes one (Lf '-') for the node
 * reference (vendor makeLock). Returns "1" on success, "0" when the
 * lock file cannot be opened, the slot is held by another pid, or
 * the lock already exists.
 */
fun makeLock(ref: String, code: String, pid: String, Lf: Char, timeout: String?): String {
    val raf = try {
        RandomAccessFile(File(LOCKFILE), "rw")
    } catch (e: Exception) {
        return "0"
    }
    raf.use {
        while (true) {
            val pos = it.filePointer
            val rec = readRec(it) ?: break
            val recRef = fieldCstring(rec, REF_OFF, REF_LEN)
            if (lockMatches(recRef, ref, rec)) {
                if (Lf == '-') {
                    if (fieldCstring(rec, PID_OFF, PID_LEN) != pid) {
                        chmod666()
                        return "0"
                    }
                    // ftell(fdopen fresh stream) == fd offset; rewind one record.
                    it.seek(pos - REC_SIZE)
                    rec[PID_OFF] = ' '.code.toByte() // only first chars blanked (vendor asymmetry)
                    rec[REF_OFF] = ' '.code.toByte()
                    it.write(rec)
                    chmod666()
                    return "1"
                }
                chmod666()
                return "0" // lock fails
            }
        }

        // Rewind and reuse any freed slot (ref starting with five spaces).
        it.seek(0)
        var slotPos = it.length() // else append at EOF
        while (true) {
            val pos = it.filePointer
            val rec = readRec(it) ?: break
            if (strncmpRec(rec, REF_OFF, "     ", 5) == 0) {
                slotPos = pos - REC_SIZE
                break
            }
        }

        val out = ByteArray(REC_SIZE)
        setField(out, PID_OFF, PID_LEN, pid)
        setField(out, REF_OFF, REF_LEN, ref)
        it.seek(slotPos)
        it.write(out)
    }
    chmod666()
    return "1"
}
