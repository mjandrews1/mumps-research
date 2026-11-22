package both

// BOTH-arm carrier seats for the not-yet-rendered vendor classes touched
// by the batch-5 tranche-1 units. Every seat names its roster owner and
// delegates when the owner unit lands (h014 action). The vendor class
// layouts live in pristine headers only; no other arm's Kotlin is
// consulted.
//
// Consults:
//   h014 out_of_scope_call - reused (TODO seats below name owners:
//     global-class layout include/mumpsc/globalClass.h::<misc> roster
//     89; the global-node Avg() service libmpscpp.cpp::global::Avg
//     roster 301; the mstring-class layout
//     include/mumpsc/mstringClass.h::<misc> roster 149).
//   MISS class_member_carrier (units 129/131/133): units 129 (mstring::
//     Length), 131 (global::Translate) and 133 (global::Length) are
//     member functions of classes whose roster owners are not yet
//     translated in this arm; the base has no rule for representing a
//     member unit of an unrendered class, so the renders pass the
//     receiver state explicitly (data/ref) as package-level functions.
//     Capture candidate at batch close (T1 recurrence 3).
//   h017 naming - reused: vendor member/property names kept (ref,
//     avg -> Avg on the node seat); snake->camel only for multiword
//     identifiers.

// Minimal `global` class carrier (vendor: class global, char
// ref[STR_MAX] inside globalClass.h:536 area; owner roster 89). Only
// the surface the batch-5 tranche-1 units touch: the ref key. The
// constructor shape mirrors `global a(...)` / `global needle` from
// the vendor signatures.
internal class GlobalRef(val ref: String)

// The subscripted global-node proxy (vendor: global::operator()
// result usable as a global). The avg() service member is a TODO seat
// (owner libmpscpp.cpp::global::Avg, roster 301).
internal class GlobalNode {
    fun avg(): Double {
        TODO("stub: owner libmpscpp.cpp::global::Avg (roster 301)")
    }
}

internal operator fun GlobalRef.invoke(sub: String): GlobalNode {
    // vendor appends the coded subscript when invoked; the concrete
    // key construction is only exercised by the avg() owner seat, so
    // it stays with the owner unit when it lands.
    return GlobalNode()
}

// -------------------------------------------------------------------------
// Batch-5 tranche-2 seats (core units 134-143), added 2026-11-21.
//
// T2 consults (rule base wp2/arms/BOTH/rules_v0.pl):
//   h014 out_of_scope_call - reused: the transpose/multiply member
//     seats and the Transpose multiply both name their owners
//     (libmpscpp.cpp::global::Multiply roster 213 batch 2; the
//     global-class Transpose member is not itself a corpus unit - its
//     file-level wrappers are roster 75 batch 4 and roster 136 batch 5,
//     both pending in this arm).
//   MISS struct_carrier (unit 142): the struct stab chain
//     (include/mumpsc/sym.h:43-51) has no base rule for typed heap
//     chains; the Stab carrier below is filed as a new miss family
//     (capture candidate at batch close, T1 recurrence 1).
//   MISS class_member_carrier recurrence: units 137/138 ride the
//     GlobalRef/GlobalNode member seats below; owners not yet in
//     this arm (T1 recurrence grows: see events CSV).
//   h017 naming - reused: stab/next/OrigName fields keep vendor case.

// struct stab - include/mumpsc/sym.h:43-51. Layout owner:
// include/mumpsc/sym.h::<misc>, a batch-5 complex unit pending in
// this arm. Only the fields the batch-5 tranche-2 units touch carry
// TODO bodies; the rest are the vendor declaration order (name,
// next, data, OrigName, size, copy, exec).
internal class Stab {
    var name: String? = null
    var next: Stab? = null
    var data: String? = null
    var OrigName: String? = null
    var size: Int = 0
    var copy: Int = 0
    var exec: Int = 0
}

// global-class member Transpose(b) - vendor body:
// libmpscpp.cpp::global::Transpose (landed by the unit 154
// render, the batch-5 tranche-3 complex chunk; the T2 seat above
// flipped per the standing h014 seat protocol - this edit). The
// real loop body rides the order/kill/valuation carrier seats
// below (layout owner roster 259, pending).
internal fun GlobalNode.transpose(other: GlobalNode) {
    other.kill() // B().Kill()

    var aa = ""
    while (true) {
        aa = invoke(aa).order(1) // aa = (string) (*this)(aa).Order(1)
        if (aa == "") break

        var bb = ""
        while (true) {
            bb = invoke(aa, bb).order(1) // (*this)(aa,bb).Order(1)
            if (bb == "") break
            // B(bb,aa) = (*this)(aa,bb)
            other.invoke(bb, aa).setValue(invoke(aa, bb).value())
        }
    }
}

// global-class member Multiply(b,c) - owner: libmpscpp.cpp::global::
// Multiply, roster unit 213, batch 2. TODO-stub (h014); the caller
// passes the three file-level wrappers' carriers.
internal fun GlobalRef.multiply(b: GlobalRef, c: GlobalRef) {
    TODO("stub: owner libmpscpp.cpp::global::Multiply (roster unit 213, batch 2)")
}

// -------------------------------------------------------------------------
// Batch-5 tranche-3 seats (complex units 144-154), added 2026-11-22.
//
// T3 consults (rule base wp2/arms/BOTH/rules_v0.pl):
//   h014 out_of_scope_call - reused: every seat below names its
//     roster owner; the global-class member seats name the
//     include/mumpsc/globalClass.h::<misc> layout owner.
//   h017 naming - reused: vendor names kept (SYNMAX, wstem, pm,
//     parse_, AllocSV, Indirect.reference per struct indirect).
//   PIN CORRECTION (journaled): the T1 header above cites the
//     global-class layout owner as roster 89; units.csv pins it
//     include/mumpsc/globalClass.h::<misc> rank 259 batch 3. The
//     new seats cite 259; the stale note above stays untouched
//     for history.
//   MISS class_member_carrier recurrence: the GlobalNode
//     order/kill/valuation seats below extend the carrier
//     pattern (T1/T2 recurrence; see events CSV).

// const SYNMAX - libmpscpp.h:385; the SynInit syn[] table cap
// (unit 146 render seat; TU-dimension pin, no owner unit).
internal const val SYNMAX = 20000 // libmpscpp.h:385

// struct MSV *AllocSV() - owner: interp-sysfunc.cpp::AllocSV,
// roster 285, batch 8. TODO-stub (h014); the Qlength push seat.
internal fun allocSV(): Msv {
    TODO("stub: owner interp-sysfunc.cpp::AllocSV (roster 285, batch 8)")
}

// int parse_(struct MSV *) - owner: interp-parse.cpp::parse_, roster
// 303, batch 3. TODO-stub (h014); the Qlength parse seat (vendor
// name parse_ kept; h015 note: trailing-underscore identifiers do
// not collide with Kotlin keywords, no rename needed).
internal fun parse_(sv: Msv): Int {
    TODO("stub: owner interp-parse.cpp::parse_ (roster 303, batch 3)")
}

// int wstem(char *, int) - owner: interp-stem.cpp::wstem, roster
// 164, batch 10. TODO-stub (h014); the Stem/stem family seats.
internal fun wstem(buf: CharArray, len: Int): Int {
    TODO("stub: owner interp-stem.cpp::wstem (roster 164, batch 10)")
}

// int pm(const unsigned char *, const unsigned char *, struct MSV *)
// - include/mumpsc/libmpscpp.h:237; the body sits in interp-pm.cpp
// (non-unit provenance). TODO-stub (h014); the pmw wrappers seat.
internal fun pm(a: CharArray, b: CharArray, sv: Msv): Int {
    TODO("stub: owner interp-pm.cpp::pm (libmpscpp.h:237; not a corpus unit)")
}

// struct indirect (include/mumpsc/globalClass.h): the operator*
// result carrier. MISS class_member_carrier (recurrence): the
// layout owner is not yet in this arm; only the reference field
// the misc-5 render touches is carried.
internal class Indirect(var reference: String)

// global extract seat: the vendor i >> t / (*this) >> t family
// (globalClass.h operator>>). Layout owner:
// include/mumpsc/globalClass.h::<misc>, roster 259, batch 3
// (pending in this arm). TODO-stub (h014); the global::Int/
// Double/Mstring/Char members and the mstring global-comparison
// arms seat through it.
internal fun GlobalRef.extractValue(): String {
    TODO("stub: owner include/mumpsc/globalClass.h::<misc> (roster 259, batch 3)")
}

// global::DocCorrelate(global, string, double) - the string-arm
// member the misc-5 overloads delegate to; layout owner
// globalClass.h::<misc> roster 259 (pending). TODO-stub (h014).
internal fun GlobalRef.docCorrelate(B: GlobalRef, fcn: String, threshold: Double) {
    TODO("stub: owner globalClass.h::<misc> DocCorrelate string arm (roster 259, batch 3)")
}

// global-node carrier extensions: the global::Order / global::
// Kill / valuation and assignment members the Transpose member
// body and the wrappers ride. Layout owner:
// include/mumpsc/globalClass.h::<misc>, roster 259, batch 3
// (pending in this arm). TODO-stubs (h014); the Transpose member
// renders its real loop body over them (the T2 transpose stub
// flips in the unit 154 render - see there).
internal fun GlobalNode.order(dir: Int): String {
    TODO("stub: owner globalClass.h::<misc> global::Order (roster 259, batch 3)")
}

internal fun GlobalNode.kill() {
    TODO("stub: owner globalClass.h::<misc> global::Kill (roster 259, batch 3)")
}

// The valuation/assignment pair: B(bb,aa) = (*this)(aa,bb) reads
// the right node and stores into the left.
internal fun GlobalNode.value(): String {
    TODO("stub: owner globalClass.h::<misc> valuation (roster 259, batch 3)")
}

internal fun GlobalNode.setValue(v: String) {
    TODO("stub: owner globalClass.h::<misc> assignment (roster 259, batch 3)")
}

// Global-value assignment seat (Ztell global arm writes the
// caller's global object). Layout owner globalClass.h::<misc>
// roster 259 (pending). TODO-stub (h014).
internal fun GlobalRef.assignValue(v: String) {
    TODO("stub: owner globalClass.h::<misc> global assignment (roster 259, batch 3)")
}

// Node-invoke seats (further-subscript walk on the carrier):
// layout owner globalClass.h::<misc> roster 259 (pending).
internal operator fun GlobalNode.invoke(sub: String): GlobalNode {
    return GlobalNode()
}

internal operator fun GlobalNode.invoke(sub1: String, sub2: String): GlobalNode {
    return GlobalNode()
}

// The two-subscript invoke arm the Transpose member uses
// ((*this)(aa,bb)); the one-subscript T1 seat stays above.
internal operator fun GlobalRef.invoke(sub1: String, sub2: String): GlobalNode {
    // vendor appends both coded subscripts; concrete key build
    // stays with the layout owner (roster 259, pending).
    return GlobalNode()
}

// GTK platform seats (mdh_* family, libmpscpp.cpp TU glue): every
// non-corpus helper below rides the GTK platform edge; owners are
// the vendor GTK binding family (not corpus units). The mdh_dialog_
// new_with_buttons arm is REAL (zfcn-based) in the unit 154 render;
// the direct gtk_* wrappers stay stubs (h014 platform provenance).
internal fun mdh_toggle_button_get_active(b: String): Int {
    TODO("stub: GTK platform edge, mdh_toggle_button_get_active (libmpscpp.cpp glue; not a corpus unit)")
}

internal fun mdh_switch_get_active(b: String): Int {
    TODO("stub: GTK platform edge, mdh_switch_get_active (libmpscpp.cpp glue; not a corpus unit)")
}

internal fun mdh_toggle_button_set_active(b: String, v: Int) {
    TODO("stub: GTK platform edge, mdh_toggle_button_set_active (libmpscpp.cpp glue; not a corpus unit)")
}

internal fun mdh_entry_get_text(e: String, txt: CharArray): CharArray {
    TODO("stub: GTK platform edge, mdh_entry_get_text (libmpscpp.cpp glue; not a corpus unit)")
}

internal fun mdh_range_get_adjustment(e: String): Double {
    TODO("stub: GTK platform edge, mdh_range_get_adjustment (libmpscpp.cpp glue; not a corpus unit)")
}

internal fun mdh_calendar_get_date(e: String): String {
    // static char tmp[256] in the vendor folds per h008 (fresh).
    TODO("stub: GTK platform edge, mdh_calendar_get_date (libmpscpp.cpp glue; not a corpus unit)")
}

internal fun mdh_entry_set_text(e: String, txt: String) {
    TODO("stub: GTK platform edge, mdh_entry_set_text (libmpscpp.cpp glue; not a corpus unit)")
}

internal fun mdh_text_buffer_set_text(t: String, txt: String) {
    TODO("stub: GTK platform edge, mdh_text_buffer_set_text (libmpscpp.cpp glue; not a corpus unit)")
}

internal fun mdh_font_chooser_get_font(b: String): String {
    TODO("stub: GTK platform edge, mdh_font_chooser_get_font (libmpscpp.cpp glue; not a corpus unit)")
}

internal fun mdh_color_chooser_get_rgba(c: String): String {
    TODO("stub: GTK platform edge, mdh_color_chooser_get_rgba (libmpscpp.cpp glue; not a corpus unit)")
}

internal fun mdh_label_set_text(l: String, txt: String) {
    TODO("stub: GTK platform edge, mdh_label_set_text (libmpscpp.cpp glue; not a corpus unit)")
}

internal fun mdh_widget_hide(w: String) {
    TODO("stub: GTK platform edge, mdh_widget_hide (libmpscpp.cpp glue; not a corpus unit)")
}

internal fun mdh_widget_show(w: String) {
    TODO("stub: GTK platform edge, mdh_widget_show (libmpscpp.cpp glue; not a corpus unit)")
}

internal fun mdh_tree_selection_get_selected(t: String, col: Int, txt: CharArray): CharArray {
    TODO("stub: GTK platform edge, mdh_tree_selection_get_selected (libmpscpp.cpp glue; not a corpus unit)")
}

internal fun mdh_tree_store_clear(t: String) {
    TODO("stub: GTK platform edge, mdh_tree_store_clear (libmpscpp.cpp glue; not a corpus unit)")
}

internal fun mdh_spin_button_get_value(s: String): Double {
    TODO("stub: GTK platform edge, mdh_spin_button_get_value (libmpscpp.cpp glue; not a corpus unit)")
}

internal fun mdh_spin_button_set_value(s: String, v: Double) {
    TODO("stub: GTK platform edge, mdh_spin_button_set_value (libmpscpp.cpp glue; not a corpus unit)")
}

// -------------------------------------------------------------------------
// B6 T1 seat additions (2026-11-22, units 155-164).
//
// Consults (rule base wp2/arms/BOTH/rules_v0.pl, 21 rules):
//   h014 out_of_scope_call - reused and hit: both seats below name
//     their units.csv owners and release per the contract when the
//     owner lands.
//   h019 class_member_carrier - first consults-as-rule land with the
//     unit-156/159/160 renders (promotion of 2026-11-22, B5 close);
//     this file adds no carriers in T1.
//   h020 pointer_handle / h021 struct_carrier - no T1 carrier hits;
//     unit-161 consults h020 against the Reader handle seat (the
//     ScanAlnum.kt render), not here.

// int sw(char *s, char *t, int ShowAlign, int ShowMatrix, int gap,
// int mismatch, int match) - owner: libmpscpp.cpp::sw, roster 287
// (batch 2, pending; the alignment engine behind unit 158). The
// parameter names are the vendor's (the SmithWaterman caller passes
// its five parsed values by position; the seat-name mismatch is
// journaled at the unit-158 render). TODO-stub (h014).
internal fun sw(s: String, t: String, showAlign: Int, showMatrix: Int,
                gap: Int, mismatch: Int, match: Int): Int {
    TODO("stub: owner libmpscpp.cpp::sw (roster 287, batch 2)")
}

// void _find(unsigned char * out, unsigned char * in,
// unsigned char * key, unsigned char * begin) - owner:
// interp-sym.cpp::_find, roster 6 (batch 1, pending; the word find
// service behind unit 156). TODO-stub (h014).
internal fun _find(out: CharArray, in_: CharArray, key: CharArray,
                   begin: CharArray) {
    TODO("stub: owner interp-sym.cpp::_find (roster 6, batch 1)")
}

// -------------------------------------------------------------------------
// B6 T2 seat additions (2026-11-22, units 165-175).
//
// Consults (rule base wp2/arms/BOTH/rules_v0.pl, 21 rules):
//   h014 out_of_scope_call - reused and hit: each seat below names
//     its units.csv owner and releases per the contract when the
//     owner lands.
//   h001 io_stream_param - reused and hit: the selectStdinC seat
//     is the stdin readiness edge (POSIX fd 0, the unit-170
//     platform fold).
//   h015 hygiene - hit: all seats ASCII, no backtick escapes, no
//     tab bytes, trailing newline.

// int btree(int g, unsigned char key[], unsigned char bd[],
// struct MSV * svPtr) - owner: interp-native-btree.cpp::btree,
// roster 29 (batch 1, pending; the 18-LOC wrapper over btreeX,
// the Mglobal SQLITE routing's only callee). TODO-stub (h014).
// The btreeX engine rewrites the key buffer in place on the
// XNEXT walk (the unit-136/171 renders document the carrier
// contract: the caller's CharArray is the mutated scratch).
internal fun btreeC(g: Int, key: CharArray, bd: CharArray, sv: Msv): Int {
    TODO("stub: owner interp-native-btree.cpp::btree (roster 29, batch 1)")
}

// char * Extract2(char * s) - owner: compiler-parse.c::Extract2,
// roster 171 (batch 1, pending; unit 173 ExpandOrder's coded
// third-argument expander). The slice-index adaptation is
// journaled: the vendor callee receives the address past the
// CodedComma separator, which the Kotlin seat passes as the
// CharArray plus the scan index. TODO-stub (h014).
internal fun extract2C(v1d: CharArray, i: Int): String {
    TODO("stub: owner compiler-parse.c::Extract2 (roster 171, batch 1)")
}

// int GlobalData(unsigned char *, unsigned char *, struct MSV *) -
// owner: libmpscpp.cpp::GlobalData, roster rows 76 (batch 3),
// 78 (batch 7), 79 (batch 8) all pending; the global-class
// Data() member's engine (vendor libmpscpp.cpp:3669 folds
// GlobalData(ref,tmp,svPtr) then atol(tmp)). The seat returns
// the atol-folded data value, 0 when the node carries no data
// (the unit-175 Dice B.Data() existence gate rides it).
// TODO-stub (h014).
internal fun globalDataC(key: String, sv: Msv): Int {
    TODO("stub: owner libmpscpp.cpp::GlobalData (roster 76/78/79, batches 3/7/8)")
}

// int selectStdinC(int seconds) - POSIX platform edge, NOT a
// corpus unit (the vendor unit 170 calls select(1,&rfds,NULL,
// NULL,&tv) on fd 0). The Int return contract is select's:
// 0 = timeout expired, positive = data ready, negative = error.
// Platform stub (h001/h014).
internal fun selectStdinC(seconds: Int): Int {
    TODO("stub: POSIX select on fd 0 platform edge (unit 170 fold)")
}

// void sttyRaw() / void sttyRawOff() - POSIX platform edges,
// NOT corpus units (the vendor unit 170 calls system("stty
// raw") / system("stty -raw")). Platform stubs (h014).
internal fun sttyRaw() {
    TODO("stub: POSIX stty raw platform edge (unit 170 fold)")
}

internal fun sttyRawOff() {
    TODO("stub: POSIX stty -raw platform edge (unit 170 fold)")
}
