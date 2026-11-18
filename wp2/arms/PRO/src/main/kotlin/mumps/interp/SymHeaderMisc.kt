package mumps.interp

/**
 * PRO-arm translation of include/mumpsc/sym.h::<misc> (B5 unit 134,
 * rank 215). Translated independently from the pristine vendor
 * source (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference
 * source lines 1-89 - the WHOLE header.
 *
 * Identity verified against the study tool (tools/select_study_
 * subset.py): sym.h yields no function chunks to the extractor; the
 * residual is the full file, physLOC 39, comment_ratio 0.466,
 * pointer_marks 11, control_marks 3, score 34.3 - exact match to
 * wp1/study-subset/units.csv row 135 (batch 5).
 *
 * sym.h is the runtime symbol-table header: the struct stab carrier,
 * the strsep/sigint prototypes, and the declaration surface for the
 * interp-sym.cpp / interp-service.cpp families. This unit renders
 * the header as a declaration ledger on the existing seats and
 * raises h014 stub seats for the three prototypes that had none.
 *
 * Declaration ledger (vendor line -> disposition):
 *  :39 char strsep(char p, char delim)  -> NEW stub (this unit);
 *     libc-style helper declared but never defined in the vendor
 *     tree (also declared in zfcns.h:51) - not a corpus unit.
 *  :41 void sigint(int)                 -> DONE: InterpStubs stub
 *     (owner interp-sysfunc.cpp::sigint, batch-7 unit).
 *  :43-51 struct stab                   -> DONE: class Stab in
 *     SymFree.kt (u28 carrier, front-run verification here; the
 *     machinery owner is interp-sym.cpp::sym_, batch-10).
 *  :59 GlobalName                       -> DONE: GlobalName.kt
 *     (u33, owner interp-sysfunc.cpp).
 *  :60 fullUnpad                        -> DONE: InterpSysfuncMisc.kt
 *     (B3 group, owner interp-sysfunc.cpp).
 *  :62-66 the _INTERP_ ifdef pair       -> h018 trichotomy; BOTH arms
 *     are commented-out keyfix prototypes, so the pair resolves
 *     EMPTY (the seat already exists as the MdhStubs keyfix stub,
 *     owner keyfix.h inline, not a corpus unit).
 *  :68 sym_                             -> DONE: InterpStubs stub
 *     (batch-10 owner) + the separate mdh MdhStubs sym_ stub.
 *  :69 _SymFree                         -> DONE: SymFree.kt (u28).
 *  :70 SysDump                          -> RESOLVED: SysDump.kt
 *     (u137, vendor 925-943; the u134 stub seat was removed
 *     there).
 *  :71 Mltoa (commented prototype)      -> dead commented prototype,
 *     nothing to render.
 *  :72 include arith.h                  -> DONE: arith.h was its own
 *     batch-2 corpus unit (units.csv row 38).
 *  :73 ErrorMessage                     -> DONE:
 *     InterpSysfuncMisc.kt (B3 group).
 *  :75 BuildGlobal                      -> DONE: InterpStubs stub
 *     (batch-8 owner).
 *  :76 BuildLocal (commented prototype) -> dead commented prototype.
 *  :78 Qsub                             -> DONE: InterpStubs stub
 *     (batch-7 owner, interp-sym.cpp:1126).
 *  :79 GlobalQuery                      -> DONE: GlobalQuery.kt
 *     (u98).
 *  :80-81 ServiceInterface              -> NEW stub (this unit);
 *     owner interp-service.cpp::ServiceInterface (vendor 87; corpus
 *     unit rank 299, batch 7 - no earlier seat existed).
 *  :82 Qlength                          -> DONE: Qlength.kt (u133;
 *     the u94 stub was removed there).
 *  :83 LocalName                        -> DONE: LocalName.kt (u112;
 *     the u94 stub was removed there).
 *  :84 xindex                           -> DONE: InterpStubs stub
 *     (owner interp-strmanip.cpp, not a corpus unit).
 *  :85 substr                           -> DONE: InterpStubs stub
 *     (batch-7 owner interp-strmanip.cpp); the compiler-side
 *     compiler/Stubs.kt substr stub is a separate seat (h015).
 *  :86 lpad                             -> DONE: InterpStubs stub
 *     (owner interp-strmanip.cpp:135, not a corpus unit).
 *  :87 _fnumber                         -> DONE: Fnumber_.kt (u111,
 *     owner interp-fnumber.cpp).
 *  :30-31/89 include guard + :33-37 std includes -> no Kotlin analog
 *     (h013 dead scaffolding, ledgered for completeness).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h012 unsigned_byte_domain: the unsigned char pointer
 *    parameters of the new stubs are C byte domains rendered as
 *    CharArray (+ Int offset carriers per the u94/u98 call shapes).
 *  - h014 out_of_scope_call  : strsep, SysDump, ServiceInterface
 *    get h014 stub seats here; the other prototypes bind to their
 *    existing DONE seats (verification rows below).
 *  - h015 grammar_collision  : pre-push scan incl. cross-file
 *    top-level names: new top-levels strsep, SysDump,
 *    ServiceInterface - no collisions (the compiler/Stubs.kt substr
 *    is a different package; the mdh sym_ overload is a different
 *    package too).
 *  - h018 if_else_resolved  : the _INTERP_ ifdef pair at :62-66
 *    resolves EMPTY (both arms are commented-out prototypes).
 *
 * Vendor notes: struct stab's comment says the data/OrigName
 * strings must be new-allocated rather than malloc-ed - carried by
 * the Stab fields as String? (u28, GC per the SymFree render).
 */

/** sym.h:39 - char * strsep(char **p, char * delim). C library
 *  strsep (BSD): tokenizes p in place, advancing the char-double-
 *  star carrier past the delimiter; declared in sym.h and zfcns.h
 *  but never defined in the vendor tree - not a corpus unit. The
 *  char-double-star renders as buffer + offset carrier (in/out
 *  token position per u70 pointer-offset conventions). */
fun strsep(p: CharArray, pOff: Int, delim: CharArray): CharArray? {
    TODO("h014 stub: owner C library strsep (not a corpus unit)")
}

/** sym.h:70 - void SysDump(long pid, struct MSV * svPtr). Seat
 *  raised by u134; RESOLVED by SysDump.kt (unit 137, the real
 *  vendor 925-943 body); stub removed. */

/** sym.h:80-81 - void ServiceInterface(...). Seat raised by this
 * unit; RESOLVED by ServiceInterface.kt (B7 unit 187, the real
 * vendor interp-service.cpp:87-445 body); stub removed (the
 * x1/x2 CharArray? widening from the u156 call sites is kept on
 * the resolved signature). */
