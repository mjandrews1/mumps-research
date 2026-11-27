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
// The subscripted global-node proxy (vendor: global::operator()
// result usable as a global; the operator composes the coded
// reference name(subscript)). The avg() service member releases
// at the B9 unit-263 close (owner libmpscpp.cpp::global::Avg,
// units.csv row 261; the h014 delegate-when-owner-lands flip):
// the node now carries the composed-key contract and avg()
// delegates to the owner render (GlobalAvg.kt).
internal class GlobalNode(internal val base: GlobalRef, internal val sub: String) {
    fun avg(): Double =
        GlobalRef(base.ref + "(" + sub + ")").globalAvg()
}

internal operator fun GlobalRef.invoke(sub: String): GlobalNode {
    // vendor appends the coded subscript when invoked; the
    // composed key construction landed with the unit-263 owner
    // seat (see GlobalNode above).
    return GlobalNode(this, sub)
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
    TODO("stub: owner libmpscpp.cpp::global::Multiply (roster unit 53, batch 2 - the old pin said roster 213, a both_seq number, C-015)")
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

// struct MSV *AllocSV() - released: the batch-8 owner render
// now lives in AllocSv.kt (unit 218; the roster-285 pin above was
// the complexity-rank mislabel family, corrected at the owner).

// int parse_(struct MSV *) - owner: interp-parse.cpp::parse_, roster
// 303, batch 3. TODO-stub (h014); the Qlength parse seat (vendor
// name parse_ kept; h015 note: trailing-underscore identifiers do
// not collide with Kotlin keywords, no rename needed).
internal fun parse_(sv: Msv): Int {
    TODO("stub: owner interp-parse.cpp::parse_ (roster 93, batch 3 - the old pin said roster 303, a both_seq number, C-015)")
}

// int wstem(char *, int) - owner: interp-stem.cpp::wstem, roster
// 294, batch 10 - RELEASED at the BOTH B10 close (the h014
// delegate-when-owner-lands flip; the seat above was the TODO
// stub). The stem-endings dispatcher: check the LAST byte of
// the word and route to the matching endings family (the
// eleven escan scan tables seated at InterpStemMisc, units
// 177); any other tail byte returns wl unchanged.
//
// Consults (unit 294, events logged):
//   h007 char_buffer - reused: word stays the caller's
//     CharArray (scan-only here; escan mutates in place).
//   h014 out_of_scope_call - reused and hit: escan rides its
//     NEW named-owner TODO stub below (owner
//     interp-stem.cpp::escan, roster 122, batch 4 - the
//     BOTH-arm committed order has not reached the unit; the
//     stub seats it, h014); the endings tables ride their
//     unit-177 seats (InterpStemMisc).
//   h017 naming - reused: wstem keeps the vendor name; i/wl
//     keep vendor names.
//   Dafny: EXEMPT (dispatch-only face over the escan stub; no
//     decidable core - the unit-177 family record).
internal fun wstem(buf: CharArray, len: Int): Int {
    // i = wl - 1; switch (word[i]) { ... default: return wl; }
    val c = buf[len - 1]
    return when (c) {
        'd' -> escan(buf, len, dEndings)
        's' -> escan(buf, len, sEndings)
        'g' -> escan(buf, len, gEndings)
        'n' -> escan(buf, len, nEndings)
        'e' -> escan(buf, len, eEndings)
        'l' -> escan(buf, len, lEndings)
        'y' -> escan(buf, len, yEndings)
        'v' -> escan(buf, len, vEndings)
        't' -> escan(buf, len, tEndings)
        'r' -> escan(buf, len, rEndings)
        else -> len
    }
}

// int escan(char *, int, struct endings *) - interp-stem.cpp:472.
// Owner: interp-stem.cpp::escan, roster 122, batch 4. TODO-stub
// (h014, raised at the B10 unit-294 close); the wstem
// dispatcher routes the ten endings families here.
internal fun escan(word: CharArray, wl: Int, endings: List<StemEnds>): Int {
    TODO("stub: owner interp-stem.cpp::escan (roster 122, batch 4)")
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
// (globalClass.h operator>>). BODY OWNER FLIP (B10, roster 306,
// the h014 delegate-when-owner-lands precedent): the operator>>
// BODIES are the libmpscpp.cpp::<misc-8> residue (vendor 6699
// char*/6859 string/6872 mstring variants, all the get_global +
// GLOBAL_NOT_FOUND fail-throw shape); the old pin named
// globalClass.h::<misc> roster 89 - the LAYOUT-owner mislabel,
// C-015 family - and the old body was a TODO-stub. The real
// render lands in LibmpscppMisc8.kt (the typed globalShr*
// readers share this kernel); this seat keeps the i >> t1 call
// shape the arm's global-comparison and lattice faces use.
internal fun GlobalRef.extractValue(): String {
    val s = CharArray(STR_MAX)
    val f = getGlobalC(ref, s, svPtr)
    if (f == 0) {
        GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }
    return cStringOf(s)
}

// global::DocCorrelate(global, string, double) - the string-arm
// member the misc-5 overloads delegate to. Owner:
// libmpscpp.cpp::global::DocCorrelate, roster 299, batch 10 -
// RELEASED at the BOTH B10 close (the h014
// delegate-when-owner-lands flip; the pin above named
// globalClass.h::<misc> roster 89, the layout-owner mislabel -
// the BODY is the libmpscpp.cpp member, C-015-family
// correction). The double walk: order the own global's first
// subscript repeatedly (doc1), and for each doc1 order the
// second (doc2); compute the requested similarity measure
// (Cosine/Sim1/Dice/Jaccard, default Cosine) over the two
// subscripted nodes and, above the threshold, store the
// score into the B global under BOTH (doc1,doc2) and
// (doc2,doc1).
//
// Consults (unit 299, events logged):
//   h007 char_buffer - reused: doc1/doc2 fold to the String
//     order-walk carriers (the GlobalOrderH precedent).
//   h014 out_of_scope_call - reused: Order/Kill/setValue ride
//     the GlobalNode seats above (layout owner roster 89
//     pending, h014); Sim1/Dice/Jaccard ride their unit seats
//     (Sim1.kt 197, Dice.kt 175, Jaccard.kt 269); Cosine rides
//     the NEW named-owner TODO stub below (owner
//     libmpscpp.cpp::Cosine, roster 85, batch 3 - COVERAGE
//     FINDING disclosed for PI: the unit is ahead of the
//     BOTH-arm committed order and unrendered).
//   h017 naming - reused: the member face keeps the docCorrelate
//     seat name; doc1/doc2/c/s/fcn/threshold keep vendor
//     names.
//   h019 class_member_carrier - reused: member of the global
//     class (layout owner roster 89, untranslated); the
//     receiver state passes explicitly (the GlobalRef ref).
//   Dafny: EXEMPT (the Mglobal order/store walk and the
//     measure-stub seats; no decidable core - the Jaccard
//     family record).
//
// Vendor quirks kept (ledgered):
//   - the B(doc1,doc2)=c double assignment renders through the
//     %g seat (fmtG) on the setValue stub (the vendor
//     operator=(double) format; the seat is a String carrier).
//   - the vendor switch leaves c UNINITIALIZED if s missed
//     every case (unreachable: s is 1-4 by construction);
//     the fold initializes 0.0 - journaled quantization.
internal fun GlobalRef.docCorrelate(B: GlobalRef, fcn: String, threshold: Double) {
    // if (fcn=="Cosine") s=1; else if Sim1 2; Dice 3; Jaccard 4; else 1
    val s = when (fcn) {
        "Cosine" -> 1
        "Sim1" -> 2
        "Dice" -> 3
        "Jaccard" -> 4
        else -> 1
    }

    // B().Kill();
    B.invoke("").kill()

    // while (1) { doc1 = (*this)(doc1).Order(1); if empty break; ... }
    var doc1 = ""
    while (true) {
        doc1 = this.invoke(doc1).order(1)
        if (doc1 == "") break

        var doc2 = doc1
        while (true) {
            doc2 = this.invoke(doc2).order(1)
            if (doc2 == "") break

            var c = 0.0 // vendor: uninitialized on switch miss (unreachable)
            // (*this)(doc1) composes the coded reference name(sub)
            val g1 = GlobalRef(this.ref + "(" + doc1 + ")")
            val g2 = GlobalRef(this.ref + "(" + doc2 + ")")
            when (s) {
                1 -> c = Cosine(g1, g2)
                2 -> c = Sim1(g1, g2)
                3 -> c = Dice(g1, g2)
                4 -> c = Jaccard(g1, g2)
            }

            if (c > threshold) {
                // B(doc1,doc2)=c; B(doc2,doc1)=c;
                B.invoke(doc1, doc2).setValue(fmtG(c))
                B.invoke(doc2, doc1).setValue(fmtG(c))
            }
        }
    }
}

// double Cosine(global, global) - libmpscpp.cpp. Owner:
// libmpscpp.cpp::Cosine, roster 85, batch 3 (the B3 coverage
// finding disclosed at the B10 close; the BOTH-arm committed
// order has not reached it). TODO-stub (h014, raised at the
// unit-299 close).
internal fun Cosine(A: GlobalRef, B: GlobalRef): Double {
    TODO("stub: owner libmpscpp.cpp::Cosine (roster 85, batch 3)")
}

// global-node carrier extensions: the global::Order / global::
// Kill / valuation and assignment members the Transpose member
// body and the wrappers ride. Layout owner:
// include/mumpsc/globalClass.h::<misc>, roster 259, batch 3
// (pending in this arm). TODO-stubs (h014); the Transpose member
// renders its real loop body over them (the T2 transpose stub
// flips in the unit 154 render - see there).
internal fun GlobalNode.order(dir: Int): String {
    TODO("stub: owner globalClass.h::<misc> global::Order (roster 89, batch 3 - C-015 renumber)")
}

internal fun GlobalNode.kill() {
    TODO("stub: owner globalClass.h::<misc> global::Kill (roster 89, batch 3 - C-015 renumber)")
}

// The valuation/assignment pair: B(bb,aa) = (*this)(aa,bb) reads
// the right node and stores into the left.
internal fun GlobalNode.value(): String {
    TODO("stub: owner globalClass.h::<misc> valuation (roster 89, batch 3 - C-015 renumber)")
}

internal fun GlobalNode.setValue(v: String) {
    TODO("stub: owner globalClass.h::<misc> assignment (roster 89, batch 3 - C-015 renumber)")
}

// Global-value assignment seat (Ztell global arm writes the
// caller's global object). Layout owner globalClass.h::<misc>
// roster 259 (pending). TODO-stub (h014).
internal fun GlobalRef.assignValue(v: String) {
    TODO("stub: owner globalClass.h::<misc> global assignment (roster 89, batch 3 - C-015 renumber)")
}

// Node-invoke seats (further-subscript walk on the carrier):
// layout owner globalClass.h::<misc> roster 259 (pending). The
// B9 unit-263 close gives the node carrier state (base, sub);
// these seats now COMPOSE the subscript list per the vendor
// operator() chain (s1)(s2) = name(s1,s2), with the final
// composed-key semantics owned by the pending layout unit.
internal operator fun GlobalNode.invoke(sub: String): GlobalNode {
    return GlobalNode(this.base, this.sub + "," + sub)
}

internal operator fun GlobalNode.invoke(sub1: String, sub2: String): GlobalNode {
    return GlobalNode(this.base, this.sub + "," + sub1 + "," + sub2)
}

// The two-subscript invoke arm the Transpose member uses
// ((*this)(aa,bb)); the one-subscript T1 seat stays above.
internal operator fun GlobalRef.invoke(sub1: String, sub2: String): GlobalNode {
    // vendor appends both coded subscripts; concrete key build
    // stays with the layout owner (roster 259, pending).
    return GlobalNode(this, sub1 + "," + sub2)
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
    TODO("stub: owner libmpscpp.cpp::sw (roster 61, batch 2 - the old pin said roster 287, C-015)")
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
    TODO("stub: owner interp-native-btree.cpp::btree (roster 4, batch 1 - the old pin said roster 29, C-015)")
}

// char * Extract2(char * s) - owner: compiler-parse.c::Extract2,
// roster 171 (batch 1, pending; unit 173 ExpandOrder's coded
// third-argument expander). The slice-index adaptation is
// journaled: the vendor callee receives the address past the
// CodedComma separator, which the Kotlin seat passes as the
// CharArray plus the scan index. TODO-stub (h014).
internal fun extract2C(v1d: CharArray, i: Int): String {
    TODO("stub: owner compiler-parse.c::Extract2 (roster 15, batch 1 - the old pin said roster 171, a rank number, C-015)")
}

// int globalDataC(String, Msv) - RELEASED at the roster-203
// render (GlobalData.kt, interp-sysfunc.cpp::GlobalData :733;
// the global::Data() member engine fold: GlobalData(ref,tmp,
// svPtr) then atol(tmp) - the Dice/Sim1/Multiply existence
// gates ride it). The old stub pin cited "roster 76/78/79" -
// RANKS of the libmpscpp.cpp::GlobalData Interpret wrappers
// (units 86/237/208); the engine row is unit 203.
internal fun globalDataC(key: String, sv: Msv): Int {
    val tmp0 = CharArray(STR_MAX)
    writeC(tmp0, key)
    val tmp2 = CharArray(STR_MAX)
    GlobalData(tmp0, tmp2, sv)
    return atolC(cStringOf(tmp2)).toInt()
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
