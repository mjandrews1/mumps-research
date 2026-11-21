package doc

// Unit 286 (B10 tranche 1): libmpscpp.cpp::global::Pattern
// (batch 10, simple stratum; units.csv row 307: loc 15, cr
// 0.062, ptr 0, ctl 2, score 5.1, rank 12; physically verified
// this session at vendor libmpscpp.cpp:7121-7141, physLOC 15)
// - the GLOBAL-key member overload (the four Pattern
// overloads: the :7142 string / :7156 mstring-ref / :7169
// char-star siblings are below min_loc, not roster units - the
// unit-89 GlobalClassMisc "no owner unit in the corpus" ledger
// row for Pattern is SUPERSEDED here: this roster row owns the
// global-key form). Double Mglobal(RETRIEVE) walk (this
// global's value, then the argument global's) with the two
// GLOBAL_NOT_FOUND throw arms (the MumpsGlobalException
// carrier, the unit-133 double-retrieve template); the return
// is the pm() pattern-match verdict over the retrieved pair -
// the Stubs.pm H-014 seat (interp-pm.cpp::pm, not a corpus
// unit; raised by the unit-154 pmw wrappers; the rpm/perl_pm
// renders cite the same owner). Naming per H-017 (Pattern ->
// pattern).

fun GlobalRef.pattern(p: GlobalRef): Int {
    val tmp = CharArray(STR_MAX)
    val tmp1 = CharArray(STR_MAX)

    var f = Stubs.Mglobal(RETRIEVE, ref, tmp, Globals.svPtr)

    if (f == 0) {
        Globals.GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    f = Stubs.Mglobal(RETRIEVE, p.ref, tmp1, Globals.svPtr)

    if (f == 0) {
        Globals.GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    return Stubs.pm(tmp, tmp1, Globals.svPtr)
}
