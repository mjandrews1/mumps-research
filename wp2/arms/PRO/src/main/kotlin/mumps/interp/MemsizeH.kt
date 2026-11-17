package mumps.interp

/**
 * PRO-arm translation of include/mumpsc/memsize.h::<misc> (B6
 * tranche C, unit 177, rank 98). Translated independently from
 * the pristine vendor source (mumps-14-may-2026.tgz, sha256
 * 621deb25...ffdbe), whole-header residual (units.csv: 50 LOC,
 * comment_ratio 0.485, ptr 0, ctrl 4, score 10.0 - exact match
 * via the study tool; the header has NO extracted functions, so
 * the misc chunk IS the whole file, residual by identity, u168
 * stateVector / u171 interp.h precedent): the memory-size and
 * OS-configuration macro roster.
 *
 * h018 reference_build resolution (the OS-config ifdef cascade,
 * vendor 62-110):
 *  - The __APPLE__ GCVT pair (62-64) resolves to the EXCLUDED
 *    arm (reference build: Linux gcc amd64; the GCVT seat lives
 *    in the build config, u31 unit-31 journal note).
 *  - _MSC_VER is not defined; GnuDos is not defined -> the
 *    Linux define chain (70-74) is IN.
 *  - Of the five platform arms, only the Linux arm (83-88) is
 *    compiled: USYSTEM LINUX, UNIX 1, DOS 2, SYSTEM UNIX. The
 *    Solaris/Watcom/GnuDos/MSVisualC arms are journaled, not
 *    rendered.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h013 dead_code          : the commented-out STEM 0 /
 *    UDAT "data.dat" / UKEY "key.dat" / RELEASE fossils
 *    (36, 38-39, 42) are roster notes; DDAT/DKEY (40-41) are
 *    the live defines.
 *  - h015 grammar_collision  : the macro constants render as
 *    file-private const vals (u57 FLT_PRECISION / u171 opcode
 *    precedent - the live seats live with their consumers, e.g.
 *    the pd1[PS1] carrier sized from sysparms.h's twin, see the
 *    vendor FIXME below).
 *  - h017 naming           : memsize.h -> MemsizeH wrapper file;
 *    every macro name keeps the vendor spelling.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The vendor's own FIXME (line 31): PS and PS1 ALSO appear in
 *    sysparms.h - and with DIFFERENT values (memsize.h: PS 4096,
 *    PS1 4097; sysparms.h:74: PS 32000, PS1 32001). Both kept;
 *    the arm's live pd1 carrier uses the sysparms.h twin (the
 *    include order in the vendor build decides; InterpStubs.kt
 *    note).
 *  - The banner copyright years (A.D. 2001, 2002, 2003, 2024)
 *    and the May 4 2024 date comment are kept as the identity
 *    ledger (u35 banner-ledger shape).
 *  - NOTABS (43) is a bare flag define (consumed by ifdef
 *    elsewhere) - rendered as a Boolean.
 */

// memsize.h:32-33 - #define PS 4096 / PS1 PS+1 (the sysparms.h
// FIXME twin, see header note; file-private ledger values).
private const val PS = 4096
private const val PS1 = PS + 1

// memsize.h:35 - #define SHOW_CODE 1
private const val SHOW_CODE = 1

// memsize.h:37 - #define VERSION 1000
private const val VERSION = 1000

// memsize.h:40-41 - the btree file-name defines (the commented
// UDAT/UKEY fossils above them are roster notes).
private const val DDAT = "data.dat"
private const val DKEY = "key.dat"

// memsize.h:43 - #define NOTABS (bare flag).
private const val NOTABS = true

// memsize.h:44 - #define FORPARM 32
private const val FORPARM = 32

// memsize.h:83-88 - the compiled Linux platform arm:
private const val USYSTEM = "LINUX" // #define USYSTEM LINUX
private const val UNIX = 1 // #define UNIX 1
private const val DOS = 2 // #define DOS 2
private const val SYSTEM = "UNIX" // #define SYSTEM UNIX
