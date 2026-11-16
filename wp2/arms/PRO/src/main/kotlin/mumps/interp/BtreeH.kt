package mumps.interp

/**
 * PRO-arm translation of include/mumpsc/btree.h::<misc> (pilot B2,
 * unit 55, provisional under deviation C-003). Translated
 * independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe). Whole-header
 * residual (units.csv: 215 LOC, score 57.3, rank 246): the header has
 * no function bodies - only the sql/btree prototypes, configuration
 * macros, struct buffers, and the file-scope static buffer globals
 * of the native B-tree global array handler.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h004 globals_ext        : the header statics gp0/gp1/gp3/gp4/
 *    gp5/gproot and gblfd/gbldat/trx/ReadOnly become the
 *    BtreeHeaderState singleton, registered with source lines.
 *    Divergence note: the C header's static gives each including
 *    translation unit its own copy of these; Kotlin renders one
 *    shared singleton (the reference build has a single live
 *    instance anyway). The vendor list jumps gp1 -> gp3 (no gp2) and
 *    the commented-out bfr[GBLBUF] array - both kept verbatim.
 *  - h010 numeric_promotion  : int file descriptors map to Int;
 *    the octal permission literal 0666 has no Kotlin octal form and
 *    is written as decimal 438 (= 0o666) with this note.
 *  - h012 unsigned_byte_domain: the buffers.gblbf[MBLOCK] byte
 *    domain maps to CharArray (Scanblocks precedent).
 *  - h014 out_of_scope_call  : the btree prototype resolves to the
 *    real B1 translation NativeBtree.btree (unit 4, same signature
 *    modulo the struct MSV* -> Msv carrier); the sql prototype has
 *    no translation yet and becomes an InterpStubs stub, owner
 *    interp-sql.cpp::sql (batch-9 corpus unit).
 *  - h017 naming             : btree.h -> BtreeH wrapper file.
 *
 * MISSED TRIGGER (preprocessor_conditional, journal-logged): the
 * macro cascade is resolved at the reference configuration, as in
 * the numSetup.h unit 29: the vendor's ADZIZE==4 conditional tests the
 * misspelled macro ADZIZE (ADSIZE is the real name), so the
 * condition is always false and both arms set MBLOCK 2048 - the
 * resolved constants are rendered directly with the selection
 * tables noted: MBLOCK 2048 -> PAGE_SHIFT 11; GBLBUF 65537 ->
 * AMASK 0xffff; ADSIZE>=8 -> large-file support.
 */

private const val ADSIZE = 8 // btree.h:51 (Scanblocks.kt keeps its own copy)

private const val GBLPERMIT = 438 // 0666 octal; Kotlin has no octal literal (h010)

// Maximum database operations per transaction (btree.h:109).
private const val MAX_SQL_TRAN_COUNT = 250

// Basic page size; must be a power of 2 (btree.h:170-173).
private const val MBLOCK = 2048

private const val DBL_STR = MBLOCK * 2 // btree.h:175

private const val BLOCK_SPLIT = MBLOCK - 64 // btree.h:176

// 11 corresponds to MBLOCK 2048 and a max file size of 4 TB.
private const val PAGE_SHIFT = 11

// Number of internal buffers; power of 2 plus 1.
private const val GBLBUF = 65537

// AMASK blocks the low bits: GBLBUF 65537 -> 0xffff.
private const val AMASK = 0xffff

/**
 * The header's struct buffers (btree.h:296-300): one cached block.
 */
class Buffers {
    var gblbf: CharArray = CharArray(MBLOCK) // h012 byte domain
    var gblbfx: Int = 0
    var gblmod: Int = 0
}

/**
 * h004: the btree.h file-scope statics (btree.h:302-312), one
 * shared singleton. The commented-out
 * the commented-out bfr[GBLBUF] array declaration is kept as this comment.
 * The vendor list has no gp2 (gp0, gp1, gp3, gp4, gp5, gproot).
 */
object BtreeHeaderState {
    var gp0: Buffers? = null
    var gp1: Buffers? = null
    var gp3: Buffers? = null // vendor jumps gp1 -> gp3 (no gp2)
    var gp4: Buffers? = null
    var gp5: Buffers? = null
    var gproot: Buffers? = null
    var gblfd: Int = -1 // btree.h:310
    var gbldat: Int = -1 // btree.h:310
    var trx: Int = 0 // btree.h:311
    var readOnly: Int = 1 // btree.h:312
}
