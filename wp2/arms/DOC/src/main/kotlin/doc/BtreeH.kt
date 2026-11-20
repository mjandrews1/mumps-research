package doc

// Unit 55: include/mumpsc/btree.h::<misc> (batch 2, complex stratum;
// row 40: loc 215, score 57.3, rank 246). The whole-header residual of
// the native btree global array handler - a declaration/constant
// ledger, no executable code. Preprocessor pairs resolve against the
// vendor reference build: ADSIZE 8 (the btree.h:51 constant itself is
// owned by BtreeExterns.kt from unit 50, not re-declared here);
// MSVC twin arms excluded (USYSTEM/SYSTEM = LINUX, SINGLE defined as
// a presence marker); GMP/MPFR not on the reference path. Vendor
// quirks kept as ledger notes: the ADZIZE spelling typo (the #if tests
// ADZIZE, not ADSIZE - both arms define MBLOCK 2048 anyway); the
// MBLOCK ladder's default 2048; the read-only gp1->gp3 jump (gp1/gp2
// pointers are declared but the handler uses gp0/gp3/gp4/gp5/gproot
// - the dead gp1/gp2 seats kept declared); the trailing static
// global-buffer externs (gblfd/gbldat -1 sentinels, trx, ReadOnly 1).
// The commented UDAT/UKEY data/key filename defines are kept as
// comment fossils. Numeric literals named per their define lines.

const val MBLOCK = 2048            // btree.h:169/172 (both arms)
const val DBL_STR = MBLOCK * 2     // btree.h:173
const val BLOCK_SPLIT = MBLOCK - 64 // btree.h:174
const val PAGE_SHIFT = 11          // btree.h:201 ladder arm for MBLOCK 2048
const val GBLBUF = 65537           // btree.h:229
const val AMASK = 0xffff           // btree.h:265 ladder arm for 65537
const val GBLPERMIT = 438          // btree.h:100 0666 octal (H-010 note)
const val MAX_SQL_TRAN_COUNT = 250 // btree.h:107
const val LINUX_OS = 1             // btree.h:77
const val DOS_OS = 2               // btree.h:78
val USYSTEM = LINUX_OS             // btree.h:82 (Linux arm)
val SYSTEM_OS = LINUX_OS           // btree.h:83
// SINGLE is a presence marker in the vendor (#define SINGLE); modelled
// as a const flag rather than a macro.
const val SINGLE = 1

// btree.h:57 extern int sql(...) - declaration-only roster entry; the
// owner is the SQL corpus unit of a later batch (H-014 note).
// btree.h:123 int btree(...): same treatment (owner: the native btree
// corpus unit; scanblocks' caller family).

// btree.h:287-290 struct buffers: the 2048-byte block image plus the
// in-buffer index (gblbfx) and the modulo scan index (gblmod).
class BtreeBuffers {
    val gblbf = ByteArray(MBLOCK)
    var gblbfx: Int = 0
    var gblmod: Int = 0
}

// btree.h:293-306 static struct buffers *gp0..gp5, *gproot and the
// file descriptors / transaction counter / read-only flag. The
// commented bfr[GBLBUF] table is a fossil (kept as a comment above in
// spirit); gp1/gp2 are declared but unused by the handler (kept
// declared, dead-seat note).
object BtreeStatic {
    var gp0: BtreeBuffers? = null
    var gp1: BtreeBuffers? = null
    var gp2: BtreeBuffers? = null
    var gp3: BtreeBuffers? = null
    var gp4: BtreeBuffers? = null
    var gp5: BtreeBuffers? = null
    var gproot: BtreeBuffers? = null
    var gblfd: Int = -1
    // gblfd's RandomAccessFile carrier (H-001 fd to RandomAccessFile;
    // unit-209 freadx EDIT-ONLY seat: the readers/writers of the
    // btree block file ride the null sentinel until the opening
    // unit of a later batch assigns it).
    var gblFile: java.io.RandomAccessFile? = null
    var gbldat: Int = -1
    var trx: Int = 0
    var ReadOnly: Int = 1
}
