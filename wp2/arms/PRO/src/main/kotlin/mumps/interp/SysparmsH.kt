package mumps.interp

// ----------------------------------------------------------------------------
// (B10 unit 294) include/mumpsc/sysparms.h::<misc>.
// Identity: units.csv row 294 - include/mumpsc/sysparms.h::<misc>, c,
// loc 39, comment_ratio 0.645, pointer_marks 0, control_marks 14,
// score 18.7 - exact match.
// ----------------------------------------------------------------------------

/**
 * PRO-arm translation of the sysparms.h whole-header residual (the
 * Mumps Compiler General Parameters header; the header has NO
 * extracted functions, so the misc chunk IS the whole file - u168 /
 * u171 header-residue precedent, MemsizeH / DefinesH KDoc-ledger
 * precedent from the same batch family).
 *
 * h018 reference_build resolution (the OS-config ifdef cascade,
 * vendor 74-133):
 *  - PS / PS1 (74-75): live on every arm. PS 32000 and PS1 PS+1
 *    are the live pd1 sizing twins ALREADY rendered as private
 *    per-file copies (Interpret.kt, Prnterr.kt, MemsizeH's
 *    memsize.h twins kept per-file); this seat re-renders them
 *    per the one-owner file-private convention.
 *  - __APPLE__ GCVT pair (77-79): EXCLUDED arm (reference build
 *    Linux gcc amd64; the GCVT seat lives in the build config,
 *    MemsizeH/u31 precedent) - journaled, not rendered.
 *  - _MSC_VER / MSVisualC vs Linux (81-87): _MSC_VER is not
 *    defined -> the Linux define chain is IN; the MSVisualC arm
 *    is EXCLUDED - journaled, not rendered.
 *  - SERVER / SINGLE (89-93): SERVER is not defined -> SINGLE is
 *    IN ("define if globals are to be used in a server" is off;
 *    single-user mode for Globals is the compiled shape).
 *  - Of the four trailing platform arms (100-133), only the
 *    Linux arm compiles: UNIX 1, USYSTEM LINUX, DOS 2. The
 *    MSVisualC / Solaris / GnuDos arms are journaled, not
 *    rendered. The commented // #define SYSTEM lines are dead
 *    preprocessor text in every arm (SYSTEM resolves through
 *    USYSTEM per the vendor cascade) - not rendered, MemsizeH's
 *    private SYSTEM="UNIX" twin already carries the resolved value.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h010 numeric_radix        : PS 32000 decimal kept; PS1 as PS+1.
 *  - h013 dead_code            : the commented-out SYSTEM defines,
 *    the MSVisualC/Solaris/GnuDos/GCVT excluded arms, and the
 *    "STR_MAX now dfined in strmax.h" note are ledger notes only.
 *  - h018 preprocessor_conditional: the ifdef cascade resolves for
 *    the reference build exactly as journaled above.
 *  - h019 duplicate_definition  : the UNIX/USYSTEM/DOS define family
 *    is the h013 duplicate note; MemsizeH (memsize.h residual,
 *    B6 unit 177) renders the same-named twins - both kept
 *    file-private, one-owner rule intact.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The header exists to set compile-configuration knobs (the
 *    READ THIS block: BUF_PARSE, MNORMAL, STEM, GCVT, SINGLE,
 *    STR_MAX, SERVER, NEW_TREE, DELIM, SUB, STRMAX) - the knobs
 *    themselves are either resolved by the reference build
 *    (SINGLE, GCVT) or owned by their consumer headers (STR_MAX
 *    by strmax.h, DELIM/DBL_STR/BLOCK_SPLIT by globalx.h, MBLOCK
 *    by global.h); this seat is the disposition ledger.
 *  - The LSB license banner and the Fortran-origin note are kept
 *    as this KDoc's shape, not rendered as code.
 */

// sysparms.h:74 - #define PS 32000
private const val PS = 32000

// sysparms.h:75 - #define PS1 PS+1
private const val PS1 = PS + 1

// sysparms.h:93 - #define SINGLE (SERVER not defined in the
// reference build; single-user mode for Globals is the compiled shape)
private const val SINGLE = true

// sysparms.h:117-122 - the compiled Linux platform arm:
// #define UNIX 1 / #define USYSTEM LINUX / #define DOS 2
private const val UNIX = 1
private const val USYSTEM = "LINUX"
private const val DOS = 2
