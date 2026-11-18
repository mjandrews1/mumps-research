package mumps.interp

import mumps.mdh.Mltoa
import java.io.BufferedReader
import java.io.File
import java.io.FileReader

/**
 * PRO-arm translation of interp-sym.cpp::SysLoad (batch 10 unit,
 * roster row 300, file rank 62). Translated independently from the
 * pristine vendor source (mumps-14-may-2026.tgz, sha256
 * 621deb2586f27e741ce6db1a2a86fbddf4e4da40c9c6bb363f84c814f53ffdbe),
 * reference source lines 945-962.
 *
 * Identity verified against the study tool
 * (tools/select_study_subset.py): SysLoad, cpp, loc 18,
 * comment_ratio 0.053, pointer_marks 1, control_marks 2, score 8.0 -
 * exact match to wp1/study-subset/units.csv row 300 (batch 10).
 *
 * SysLoad(pid, svPtr): the symbol-table load - the twin of the DONE
 * SysDump seat (SysDump.kt, u137): the file NAME is the pid in
 * decimal (Mltoa into the 2048 carrier), the file is open-for-read,
 * and every name-line/data-line pair is sym_(0,...)-stored back into
 * the CURRENT runtime symbol table; fclose and return 1 on success,
 * return 0 when the dump file does not exist.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h001 io_edge            : FILE * renders BufferedReader at the
 *    edge only (SysDump PrintWriter precedent; fopen "r" failure
 *    maps to the null carrier and the vendor return-0 path).
 *  - h012 unsigned_byte_domain: inbuf/inbuf2 are C byte-domain
 *    fgets buffers (2048); the "\n" strip is the fgets trailing
 *    newline removal (see the vendor note below); Mltoa NUL-
 *    terminated pid carrier is the file NAME (cstringAt edge).
 *  - h014 out_of_scope_call  : Mltoa is DONE (Mltoa.kt, batch-10
 *    row 292); sym_ keeps its raised InterpStubs h014 stub (owner
 *    interp-sym.cpp::sym_, batch-10 corpus unit, kept per the
 *    stubs ledger) - direct call, compiles clean.
 *  - h015 grammar_collision  : new top-level SysLoad - SysDump's
 *    DONE seat is a distinct name; no collisions.
 *  - h017 naming             : SysLoad keeps the vendor name.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - fgets keeps the trailing newline; the vendor strips the LAST
 *    byte of each line unconditionally (inbuf[strlen-1]=0 with the
 *    "new line" comment). readLine() edges exclude the newline, so
 *    the strip is absorbed by the reader EXCEPT for a final line
 *    without a trailing newline, where the vendor would strip a
 *    real data byte - quantized to the unstripped line (u128
 *    deterministic-UB precedent, journaled).
 *  - The inner fgets(inbuf2,...) return value is NOT checked: on a
 *    truncated (odd-line) file the vendor sym_-stores stale/
 *    uninitialized inbuf2 content. Quantized to the previously
 *    read inbuf2 line (deterministic at the same seat, u128) and
 *    journaled here - the odds of a real dump file are moot, it is
 *    paired with its own SysDump writer.
 *  - The sym_ store uses flag 0 (the "store" arm) with the line's
 *    terminating NUL at the strlen seat - kept verbatim as the
 *    stub call shape.
 */
fun SysLoad(pid: Long, svPtr: Msv): Int {
    val inbuf = CharArray(2048) // vendor unsigned char inbuf[2048]
    Mltoa(pid, inbuf) // file name = pid in decimal
    val indump: BufferedReader? = try {
        BufferedReader(FileReader(File(cstringAt(inbuf)))) // fopen(inbuf, "r")
    } catch (e: java.io.IOException) {
        null
    }
    if (indump == null) return 0 // vendor: return 0

    var stale2: String = "" // vendor UB quantization: stale inbuf2 carrier
    while (true) {
        val l1 = indump.readLine() ?: break // fgets(inbuf,...) == NULL
        val l2 = indump.readLine() // fgets(inbuf2,...) - vendor return NOT checked
        val in2: CharArray
        if (l2 == null) {
            // vendor UB quantized (see KDoc): stale inbuf2 content
            in2 = stale2.toCharArray()
        } else {
            in2 = l2.toCharArray()
            stale2 = l2
        }
        // vendor: sym_(0, inbuf, inbuf2, svPtr) /* store */
        sym_(0, l1.toCharArray(), 0, in2, 0, svPtr)
    }
    indump.close() // fclose(indump)
    return 1
}
