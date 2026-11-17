package mumps.mdh

/**
 * PRO-arm translation of include/mumpsc/globalClass.h::<misc> (B3
 * unit 67, rank 259). Translated independently from the pristine
 * vendor source (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe).
 *
 * Identity verified against the study tool (tools/select_study_
 * subset.py): the misc unit is the header residue (the entire class
 * global declaration surface - the study extractor claims no
 * function bodies from a .h, so every non-blank line rides the
 * residue) - include/mumpsc/globalClass.h::<misc>, c, loc 485,
 * comment_ratio 0.01, pointer_marks 0, control_marks 2, score 76.7,
 * batch 3 - exact match to wp1/study-subset/units.csv row 259.
 *
 * Unit scope: globalClass.h declares the vendor C++ class global -
 * constructor, MakeGlobal, ~200 method/operator/overload signatures,
 * the data members, and the friend surface. This file is the
 * declaration-side translation. Kotlin has no forward declarations
 * and the arm models class global as the Global carrier
 * (GlobalCarrier.kt, unit 14 + additions), so per the arith.h
 * precedent (unit 57, ArithH.kt):
 *   - the DATA MEMBERS owned by this header (Gtmp, tmp, ArgCount,
 *     Sref, Sname; myname and static Eflg already carried from unit
 *     31) are added to the Global carrier class, owner-tagged (u67);
 *   - NO stubs are raised for future owning units (one-owner policy,
 *     rules_v0.pl h004/h014: bodies belong to their .cpp units).
 *
 * OWNER ROSTER (declaration group -> owning corpus unit):
 *   - global(const char*) ctor, unary operator+/-, invoke(vararg)/
 *     invoke(indirect), Eflg/atexit: DONE (B1 unit 31,
 *     libmpscpp.cpp::<misc-1>, GlobalCarrier.kt).
 *   - MakeGlobal: batch-7 (makeGlobal h014 stub in GlobalCarrier).
 *   - Translate/Soundex/Extract/Find/Ascii/zBlanks/Query/Qlength/
 *     Reverse/Qsubscript/Pattern/Justify/Length/Piece/ReadLine/
 *     Flush/Transpose/Centroid/Sum/DocCorrelate/Avg/IDF/Count/
 *     TablePrint/Merge/Lock/UnLock/get/Set/Get/C_Order/Order_Next/
 *     Order_Prior/Name: libmpscpp.cpp method machinery (later
 *     batches / not corpus units).
 *   - Concat overloads: B3 unit 91 (global::Concat).
 *   - Fnumber overloads: B3 unit 84 (global::Fnumber).
 *   - Max/Min (double): Min DONE (Min.kt, B1); Max is B3 unit 88.
 *   - TermCorrelate: DONE (TermCorrelate.kt, B1).
 *   - TreePrint: DONE (unit 60); TablePrint: libmpscpp.cpp (later).
 *   - operator= family: MdhStubs.assignInt/... (owner libmpscpp.cpp
 *     class global::operator= machinery, stubs raised by unit 62).
 *   - operator<< family / operator>> family: MdhStubs.insertLong/
 *     insertDouble/extractInt/... stubs (owner libmpscpp.cpp
 *     <misc-3>, raised by unit 62).
 *   - Arithmetic operator families (+ - * / compound assigns, ++/--,
 *     >= <= families): DONE (B2 unit 62, GlobalOps.kt).
 *   - == / != / > / < families and their friends: libmpscpp.cpp
 *     later misc chunks (no stubs raised yet).
 *   - Casts (operator int, double, char-ptr, mstring): GlobalRef stubs
 *     (toDouble, stringValue; owner libmpscpp.cpp machinery).
 *   - friends Cosine/Sim1/Jaccard/Dice: Cosine is B3 unit 71;
 *     Sim1/Jaccard/Dice are libmpscpp.cpp (later batches).
 *   - friend char* __Name(global, MSV*): libmpscpp.cpp machinery.
 *   - operator()(const long int) and the string/mstring/char*
 *     subscript overloads (arities 0..10): folded into the carrier's
 *     vararg invoke (unit 31; see GlobalCarrier.kt).
 *   - friend ostream << global: operator mstring/const char*
 *     (GlobalRef.stringValue stub, unit 60).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h004 globals_ext          : static Eflg already carried in the
 *    Global companion (unit 31); the remaining file-scope state is
 *    per-instance data members, added to the carrier.
 *  - h012 unsigned_byte_domain: Gtmp/tmp are STR_MAX (32000,
 *    strmax.h:3) char buffers in the carrier.
 *  - h014 out_of_scope_call    : declaration-only header - the
 *    roster above delegates every method body to its owning unit;
 *    no stubs raised here (u57 precedent).
 *  - h015 grammar_collision    : the header's char ref[STR_MAX]
 *    member collides with the carrier's existing 'val ref: String'
 *    (unit-14 adaptation, the MakeGlobal working buffer is carried
 *    by GlobalRef) - kept as the existing adaptation, documented in
 *    the carrier; the commented-out vendor members (char Sref /
 *    char Sname array forms, lines 537/539) are the superseded
 *    declarations of the string members that ARE carried.
 *  - h017 naming               : Gtmp, tmp, ArgCount, Sref, Sname
 *    keep vendor names in the carrier.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The header's commented-out members (// char Sref[STR_MAX]; and
 *    //char Sname[STR_MAX];) sit immediately above their live
 *    'string' replacements - the port carries the live string forms.
 *  - operator<(mstring) and friend operator<(mstring,global) have no
 *    operator<=(mstring) counterpart (the <= family lacks the
 *    mstring member) - vendor asymmetry, noted; the == / != families
 *    likewise omit mstring entirely.
 *  - friend operator+=(int&,global) takes its left operand by
 *    reference while every other friend compound assign takes it by
 *    value - vendor asymmetry kept in the roster (the u62 GlobalOps
 *    compound-assign family already mirrors it).
 */
