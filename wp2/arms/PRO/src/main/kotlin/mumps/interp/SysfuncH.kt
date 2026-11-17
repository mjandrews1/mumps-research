package mumps.interp

/**
 * PRO-arm translation of include/mumpsc/sysfunc.h::<misc> (B4
 * unit 110, rank 141). Translated independently from the pristine
 * vendor source (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe);
 * the whole header is the study-tool residual chunk (loc 26,
 * comment_ratio 0.519, pointer_marks 3, control_marks 2, score
 * 14.6 - exact match to wp1/study-subset/units.csv row 111; no
 * function definition lives in a declaration header, so nothing
 * reaches min_loc).
 *
 * sysfunc.h - "Mumps Runtime Library": the most operating-system
 * specific run-time support declarations. A pure declaration
 * surface translates to an owner roster (cppsysfunc.h precedent,
 * B3 unit 80) - no Kotlin declarations are emitted; each prototype
 * resolves as noted.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h013 dead_store         : the commented-out VariableNotFound
 *    prototype stays commented out, as in the vendor (roster
 *    text, not resurrected).
 *  - h014 out_of_scope_call  : the declaration roster resolves
 *    each prototype to its arm status - real reuse, future-owner
 *    note, or reference-build visibility note; NO stubs are
 *    raised for future owners (GlobalClassH precedent, B3 unit
 *    67 - a declaration alone has no call site to satisfy).
 *  - h015 grammar_collision  : pre-push scan clean (the include
 *    guard and prototypes contain no quoting hazards).
 *  - h017 naming             : the roster keeps vendor spellings
 *    (TimeOut_getstr1's mixed case is vendor-verbatim).
 *  - h018 preprocessor_conditional: the _INTERP_ include guard
 *    selects the interpreter-side roster (the reference build
 *    compiles the interpreter with _INTERP_ defined - the
 *    LocalNext/LocalOrder prototypes appear on BOTH arms, so
 *    the visible surface is identical either way; noted, not
 *    duplicated); SYSFUNC_H include guard is reference-build
 *    visibility text.
 *
 * Owner roster (vendor declaration order):
 *  - int getstr1(FILE *, unsigned char **):
 *    interp-sysfunc.cpp::getstr1 - batch-5 corpus unit (future
 *    owner; FILE* maps per h001 at that unit).
 *  - int TimeOut_getstr1(int, unsigned char [], char *, int):
 *    interp-sysfunc.cpp::TimeOut_getstr1 - batch-6 corpus unit
 *    (future owner).
 *  - int DirectRead(int, unsigned char *):
 *    REAL reuse - interp/DirectRead.kt (B1 unit 24).
 *  - void ErrorMessage(const char *, long):
 *    REAL reuse - InterpSysfuncMisc.kt (B1 unit 25).
 *  - void GlobalNext(unsigned char *, unsigned char *, struct MSV *):
 *    REAL reuse - InterpSysfuncMisc.kt (B1 unit 25).
 *  - void LocalNext(unsigned char *, unsigned char *, struct MSV *):
 *    REAL reuse - InterpSysfuncMisc.kt (B1 unit 25).
 *  - void LocalOrder(unsigned char *, unsigned char *, unsigned char *, struct MSV *):
 *    REAL reuse - InterpSysfuncMisc.kt (B1 unit 25).
 *  - unsigned char * VariableNotFound1(long, unsigned char *):
 *    REAL reuse - InterpSysfuncMisc.kt (B1 unit 25); the
 *    commented-out VariableNotFound prototype above it stays
 *    commented out (vendor-verbatim).
 *  - int Merge(unsigned char *, unsigned char *, struct MSV *):
 *    interp-sysfunc.cpp::Merge - batch-8 corpus unit (future
 *    owner).
 *  - void GlobalData(unsigned char *, unsigned char *, struct MSV *):
 *    the three-argument runtime form - a distinct vendor function
 *    from the intrinsic shapes in mdh/GlobalData.kt; owner
 *    interp-sysfunc.cpp::GlobalData - batch-7 corpus unit
 *    (future owner; distinct-shape note per the cppsysfunc.h
 *    precedent, B3 unit 80).
 *  - void GlobalName(unsigned char *, unsigned char, struct MSV *):
 *    REAL reuse - interp/GlobalName.kt (B1 unit 13).
 *  - void LocalGet(unsigned char *, unsigned char *, unsigned char *, struct MSV *):
 *    REAL reuse - InterpSysfuncMisc.kt (B1 unit 25).
 *  - void GlobalGet(unsigned char *, unsigned char *, unsigned char *, struct MSV *):
 *    REAL reuse - InterpSysfuncMisc.kt (B1 unit 25).
 *  - void LocalQuery(unsigned char *, unsigned char *, struct MSV *):
 *    REAL reuse - InterpSysfuncMisc.kt (B1 unit 25).
 *  - void fullUnpad(unsigned char *, struct MSV *):
 *    REAL reuse - InterpSysfuncMisc.kt (B1 unit 25).
 *  - void LocalName(unsigned char *, unsigned char *, unsigned char *, struct MSV *):
 *    REAL reuse as of THIS batch - interp/LocalName.kt (B4 unit
 *    112, declared here; the InterpStubs stub raised by B4 unit
 *    94 is resolved and removed).
 */
// (No Kotlin declarations emitted - this is a declarations-only
// header surface; every live prototype resolves per the roster
// above.)
