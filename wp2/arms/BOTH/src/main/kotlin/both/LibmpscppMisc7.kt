package both

// Unit 217 render: libmpscpp.cpp::<misc-7> (roster 217, batch 8,
// 600 LOC chunk; the 7th libmpscpp misc residue). The chunk:
// the global::Concat / mstring::Concat overload families, the
// mstring::Soundex and the ONE-ARG mstring::Translate (the
// B7 renders are the GlobalRef.Soundex and two-arg Translate
// twins - distinct overloads, no fold), Reverse x2, Qsubscript
// x2, Qlength x2, Query x2, the global cast-operator block
// (VERIFICATION FOLD), the operator* family, the operator> /
// operator< ladders, and the operator<< store family head.
//
// COVERAGE FINDINGS (disclosed to the PI at close, the record
// 43 embed family): record 91 (libmpscpp.cpp::global::Concat,
// batch 3) and record 77 (libmpscpp.cpp::mstring::Eval, batch
// 3) have NO render on BOTH master and NO events-CSV rows
// (verified this session). The Concat(double) body rides this
// chunk's head (rendered here); mstring::Eval is raised as a
// TODO stub below.
//
// Consults (rule base wp2/arms/BOTH/rules_v0.pl):
//   h013 dead_store - hit: the Concat(string) arm declares an
//     xtmp[STR_MAX] it never uses (kept as the ledger note);
//     the Qlength bodies' int i folds into the return.
//   h014 out_of_scope_call - reused and hit: Mglobal RETRIEVE/
//     STORE direct (the macro folds below); zfcn direct (the
//     record-156 render); fcn direct (the fcn stub seat);
//     mstring::Eval is a NEW TODO stub (record 77 disclosure);
//     mstring::FixGblRef is a NEW TODO stub (owner record 262,
//     batch 9 pending); fnCpyAtZb/fnBdStrAt1Zb direct (the
//     Fnumber helpers).
//   h015 grammar-collision - reused: ASCII only, no backticks,
//     no raw comment dollars, trailing newline; the intrinsic
//     dollars ride the \\u0024 escapes (the MstringTranslate
//     precedent).
//   h017 naming - reused: the members keep the house carrier
//     names (concatG / mstringConcat / mstringSoundex /
//     mstringTranslate1 / mstringReverse / reverseG /
//     mstringQsubscript / qsubscriptG / mstringQlength /
//     qlengthG / mstringQuery / queryG / globalGt / globalLt /
//     shlG - the globalFindM/readLineG/mstringTranslate
//     precedents); the C operator spellings stay in the header
//     ledger.
//   h019 class_member_carrier - reused: mstring members are
//     String extensions (receiver = data); global members are
//     GlobalRef extensions (receiver = ref); the TU-static
//     svPtr is the BothCRuntime singleton.
//   h021 struct_carrier - reused and ADAPTED: the C unsigned
//     int/short/float operator arms are same-body modulo the
//     C type width - folded onto the Int/Long/Double seats
//     (the LibmpscppMisc5 mstringEquals/mstringLessEqual fold
//     precedent); the fold ledgered per family below.
//   h012 unsigned_byte_domain - consulted: no byte-domain work
//     in this chunk (the fold note above covers the unsigned
//     TYPES, not bytes).
//
// Quirks kept (ledgered):
//   - mstring::Soundex puts a LEADING BLANK before the fcn
//     name (" \u0024zzsoundex") and runs the zfcn path - the B7
//     GlobalRef.Soundex has no blank and runs the fcn path
//     (both kept exact; the twin note).
//   - the one-arg Translate keeps the single \\u00d0 (the B7
//     two-arg render's a2 slot is absent).
//   - global::Reverse retrieves INTO &gtmp[9] (the pointer
//     offset fold - the prefix "\u0024reverse(" occupies 0..8).
//   - global::Qsubscript/Qlength/Query embed Sref (the
//     subscripted-ref member); the GlobalRef carrier seats it
//     at ref (the MakeGlobal carrier note: Sref = gblnme+"("
//     folded onto ref - ledgered).
//   - the << (unsigned int) arm formats "%ud" (a %u followed
//     by a LITERAL 'd' - the vendor typo; kept exact via
//     toString()+"d").
//   - operator*(global,global) parses BOTH operands with
//     strtod (no Int() path - kept).
//   - the comparison ladders return Int 0/1 (not Boolean -
//     kept exact); the friend forms are rendered as the
//     globalGtR/globalLtR top-level seats.
//   - the cast-operator block folds onto the seated
//     Int()/Double()/Mstring()/Char() faces (LibmpscppMisc5:
//     193-205); the unsigned short/short/float/long arms are
//     same-body folds (the >> extraction idiom).

// ---------------------------------------------------------------------------
// Stubs raised (h014)

// mstring mstring::Eval() - owner: libmpscpp.cpp::mstring::Eval
// (roster 77, batch 3 - COVERAGE FINDING: no render on master,
// no events rows; PI disclosure at close). TODO-stub.
internal fun mstringEvalZb(m: String): String {
    TODO("stub: owner libmpscpp.cpp::mstring::Eval (roster 77, batch 3; unrendered on master, PI disclosure)")
}

// mstring mstring::FixGblRef() - owner: libmpscpp.cpp::mstring::
// FixGblRef (roster 262, batch 9 pending). TODO-stub.
internal fun mstringFixGblRefZb(m: String): String {
    TODO("stub: owner libmpscpp.cpp::mstring::FixGblRef (roster 262, batch 9)")
}

// ---------------------------------------------------------------------------
// Concat families

// mstring global::Concat(double) - the record-91 body (chunk
// head; the coverage disclosure above). The long/int arms are
// same-body modulo the mstring conversion type (fold ledger).
internal fun GlobalRef.concatG(b: Double): String {
    val gtmp = CharArray(STR_MAX)
    val f = Mglobal(RETRIEVE, ref.toCharArray(), gtmp, svPtr)
    if (f == 0) {
        GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }
    // rslt = b; strcat(gtmp, rslt.data.c_str()); rslt = gtmp;
    // - the mstring(double) conversion folds to toString.
    return cStringOf(gtmp) + b
}

internal fun GlobalRef.concatG(b: Long): String {
    val gtmp = CharArray(STR_MAX)
    val f = Mglobal(RETRIEVE, ref.toCharArray(), gtmp, svPtr)
    if (f == 0) {
        GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }
    return cStringOf(gtmp) + b
}

internal fun GlobalRef.concatG(b: Int): String {
    val gtmp = CharArray(STR_MAX)
    val f = Mglobal(RETRIEVE, ref.toCharArray(), gtmp, svPtr)
    if (f == 0) {
        GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }
    return cStringOf(gtmp) + b
}

// mstring global::Concat(char *) / Concat(string) / Concat(mstring)
// - the three arms share the strcat-direct body (fold; the
// Concat(string) arm's unused xtmp[STR_MAX] is the h013 note).
internal fun GlobalRef.concatG(b: String): String {
    val gtmp = CharArray(STR_MAX)
    val f = Mglobal(RETRIEVE, ref.toCharArray(), gtmp, svPtr)
    if (f == 0) {
        GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }
    return cStringOf(gtmp) + b
}

// mstring mstring::Concat(global b) - retrieve b, prepend data.
internal fun String.mstringConcat(b: GlobalRef): String {
    val gtmp = CharArray(STR_MAX)
    val f = Mglobal(RETRIEVE, b.ref.toCharArray(), gtmp, svPtr)
    if (f == 0) {
        GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }
    return this + cStringOf(gtmp)
}

// mstring mstring::Concat(double/long/int) - the mstring(b)
// conversions fold to toString.
internal fun String.mstringConcat(b: Double): String = this + b
internal fun String.mstringConcat(b: Long): String = this + b
internal fun String.mstringConcat(b: Int): String = this + b

// mstring mstring::Concat(char *) / Concat(string) /
// Concat(mstring) - same-body folds (data + b).
internal fun String.mstringConcat(b: String): String = this + b

// ---------------------------------------------------------------------------
// Soundex (the mstring face; the B7 GlobalRef.Soundex is the
// distinct global face)

// mstring mstring::Soundex() - note the LEADING BLANK before the
// fcn name (vendor strcpy " \u0024zzsoundex"); zfcn path.
internal fun String.mstringSoundex(): String {
    val tmp = CharArray(STR_MAX)
    val tmp2 = CharArray(STR_MAX)
    writeC(tmp, " \u0024zzsoundex") // note blank before fcn name
    tmp[cStringOf(tmp).length] = '\u00CE' // strcat "\xce"
    val s = cStringOf(tmp)
    writeC(tmp, s + this + '\u00CF')

    zfcn(tmp, tmp2, svPtr)

    return cStringOf(tmp2)
}

// mstring mstring::Translate(mstring a1) - the ONE-ARG overload
// (the B7 two-arg mstringTranslate is distinct).
internal fun String.mstringTranslate1(a1: String): String {
    // m = "\u0024translate" .Concat("\xce") .Concat(data)
    //     .Concat("\xd0") .Concat(a1) .Concat("\xcf")
    val m = "\u0024translate" + '\u00CE' + this + '\u00D0' +
        a1 + '\u00CF'

    fnCpyAtZb(svPtr.v1d, 1, m) // strcpy(&v1d[1], m.data)
    fcn(svPtr)
    return fnBdStrAt1Zb(svPtr.bd) // m = (char *) &bd[1]
}

// ---------------------------------------------------------------------------
// Reverse / Qsubscript / Qlength / Query

// mstring global::Reverse() - the "\u0024reverse(" prefix occupies
// gtmp[0..8]; the retrieve lands at &gtmp[9] (the pointer
// offset fold).
internal fun GlobalRef.reverseG(): String {
    val gtmp = CharArray(STR_MAX)
    writeC(gtmp, "\u0024reverse(") // char gtmp[STR_MAX]="\u0024reverse("
    val out = CharArray(STR_MAX)
    val f = Mglobal(RETRIEVE, ref.toCharArray(), out, svPtr)
    if (f == 0) {
        GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }
    // strcat(gtmp, ")"); m = gtmp; m = m.Eval();
    return mstringEvalZb("\u0024reverse(" + cStringOf(out) + ")")
}

// mstring mstring::Reverse()
internal fun String.mstringReverse(): String =
    mstringEvalZb("\u0024reverse(" + this + ")")

// mstring mstring::Qsubscript(int j)
internal fun String.mstringQsubscript(j: Int): String =
    mstringEvalZb("\u0024qsubscript(" + this + "," + j + ")")

// mstring global::Qsubscript(int j) - Sref seats at ref (the
// MakeGlobal carrier fold, ledgered).
internal fun GlobalRef.qsubscriptG(j: Int): String =
    mstringEvalZb("\u0024qsubscript(" + ref + "," + j + ")")

// int mstring::Qlength()
internal fun String.mstringQlength(): Int {
    // m = "\u0024qlength("; m = m & (*this).FixGblRef() & ")";
    val m = "\u0024qlength(" + mstringFixGblRefZb(this) + ")"
    return mstringEvalZb(m).let { atolC(it).toInt() }
}

// int global::Qlength() - the ^-prefixed Sref form.
internal fun GlobalRef.qlengthG(): Int {
    val m = "\u0024qlength(^" + ref + ")"
    return mstringEvalZb(m).let { atolC(it).toInt() }
}

// mstring global::Query()
internal fun GlobalRef.queryG(): String {
    val m = "\u0024query(^" + ref + ")"
    return mstringEvalZb(m)
}

// mstring mstring::Query()
internal fun String.mstringQuery(): String {
    val m = "\u0024query(" + mstringFixGblRefZb(this) + ")"
    return mstringEvalZb(m)
}

// ---------------------------------------------------------------------------
// The cast-operator block (global::operator T) - VERIFICATION
// FOLD onto the LibmpscppMisc5 faces: char* -> Mstring(), int /
// unsigned int / short / unsigned short -> Int(), long /
// unsigned long -> atolC(extractValue()), float / double ->
// Double(), mstring -> Mstring() (the >> extraction idiom; the
// unsigned arms are same-body folds, h021 ledger).

// ---------------------------------------------------------------------------
// operator* family

// int global::operator*(int s) - the unsigned/short arms are
// same-body folds onto the Int seat (h021).
internal operator fun GlobalRef.times(s: Int): Int = s * Int()
internal operator fun Int.times(a: GlobalRef): Int = this * a.Int()
internal operator fun GlobalRef.times(s: Long): Long = s * atolC(extractValue())
internal operator fun Long.times(a: GlobalRef): Long = this * atolC(a.extractValue())
internal operator fun GlobalRef.times(s: Float): Float = (s * Double()).toFloat()
internal operator fun Float.times(a: GlobalRef): Float = (this * a.Double()).toFloat()
internal operator fun GlobalRef.times(s: Double): Double = s * Double()
internal operator fun Double.times(a: GlobalRef): Double = this * a.Double()

// double global::operator*(global s) - BOTH operands parse via
// strtod (kept; no Int() path).
internal operator fun GlobalRef.times(s: GlobalRef): Double {
    val b = strtodC(s.extractValue())
    val a = strtodC(extractValue())
    return a * b
}

// ---------------------------------------------------------------------------
// operator> ladder (Int 0/1 returns kept exact; the friend
// forms render as the globalGtR top-level seats)

// global > global: strcmp of both values.
internal fun GlobalRef.globalGt(a: GlobalRef): Int =
    if (extractValue().compareTo(a.extractValue()) > 0) 1 else 0

// numeric arms (int/uint/short/ushort fold onto Int; long/ulong
// onto atolC; float/double onto Double - h021).
internal fun GlobalRef.globalGt(a: Int): Int = if (Int() > a) 1 else 0
internal fun GlobalRef.globalGt(a: Long): Int = if (atolC(extractValue()) > a) 1 else 0
internal fun GlobalRef.globalGt(a: Double): Int = if (Double() > a) 1 else 0

// char*/string/mstring arms: strcmp.
internal fun GlobalRef.globalGt(a: String): Int =
    if (extractValue().compareTo(a) > 0) 1 else 0

// friend forms: a > global.
internal fun globalGtR(a: Int, b: GlobalRef): Int = if (a > b.Int()) 1 else 0
internal fun globalGtR(a: Long, b: GlobalRef): Int = if (a > atolC(b.extractValue())) 1 else 0
internal fun globalGtR(a: Double, b: GlobalRef): Int = if (a > b.Double()) 1 else 0
internal fun globalGtR(a: String, b: GlobalRef): Int =
    if (a.compareTo(b.extractValue()) > 0) 1 else 0

// ---------------------------------------------------------------------------
// operator< ladder (the > twin)

internal fun GlobalRef.globalLt(a: GlobalRef): Int =
    if (extractValue().compareTo(a.extractValue()) < 0) 1 else 0

internal fun GlobalRef.globalLt(a: Int): Int = if (Int() < a) 1 else 0
internal fun GlobalRef.globalLt(a: Long): Int = if (atolC(extractValue()) < a) 1 else 0
internal fun GlobalRef.globalLt(a: Double): Int = if (Double() < a) 1 else 0

internal fun GlobalRef.globalLt(a: String): Int =
    if (extractValue().compareTo(a) < 0) 1 else 0

internal fun globalLtR(a: Int, b: GlobalRef): Int = if (a < b.Int()) 1 else 0
internal fun globalLtR(a: Long, b: GlobalRef): Int = if (a < atolC(b.extractValue())) 1 else 0
internal fun globalLtR(a: Double, b: GlobalRef): Int = if (a < b.Double()) 1 else 0
internal fun globalLtR(a: String, b: GlobalRef): Int =
    if (a.compareTo(b.extractValue()) < 0) 1 else 0

// ---------------------------------------------------------------------------
// operator<< store family head (set_global/get_global are the
// libmpscpp.h:117-118 MACRO folds: tpx = Mglobal(...), rslt)

// global & global::operator<<(char * s) / <<(string) /
// <<(mstring): set_global(ref, s).
internal fun GlobalRef.shlG(s: String): GlobalRef {
    val buf = CharArray(STR_MAX)
    writeC(buf, s)
    svPtr.tpx = Mglobal(STORE, ref.toCharArray(), buf, svPtr).toLong()
    return this
}

// global & global::operator<<(global g): get_global(g.get(), s)
// with the tpx check, then set_global(ref, s).
internal fun GlobalRef.shlG(g: GlobalRef): GlobalRef {
    val s = CharArray(STR_MAX)
    svPtr.tpx = Mglobal(RETRIEVE, g.ref.toCharArray(), s, svPtr).toLong()

    if (svPtr.tpx.toInt() == 0) {
        GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    svPtr.tpx = Mglobal(STORE, ref.toCharArray(), s, svPtr).toLong()
    return this
}

// global & global::operator<<(int i): sprintf(s, "%d", i).
internal fun GlobalRef.shlG(i: Int): GlobalRef = shlG(i.toString())

// global & global::operator<<(unsigned int i): sprintf(s,
// "%ud", i) - the %u PLUS LITERAL 'd' vendor typo (kept exact).
internal fun GlobalRef.shlGU(i: UInt): GlobalRef =
    shlG(i.toString() + "d")

// global & global::operator<<(short i): sprintf(s, "%hd", i).
internal fun GlobalRef.shlG(i: Short): GlobalRef = shlG(i.toString())
