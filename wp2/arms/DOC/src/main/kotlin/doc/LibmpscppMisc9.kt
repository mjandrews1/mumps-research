package doc

// Unit 303 (B10 tranche 3): libmpscpp.cpp::<misc-4> (batch 10,
// complex stratum; units.csv row 283: loc 600). The mstring
// operator= (global) assignment with its tpx probe, the operator!
// not, the pre/post increment/decrement arms, the unary + / -,
// the full operator+ / - / % / / ladders (member side), the
// operator|| concat-OR family (the eight member arms and the
// seven friend commutes), the scalar friend % + - / sets, and
// the >= / <= comparison ladders. All derived from the pristine
// vendor source this session (sha256 621deb...ffdbe).
//
// FOLDS per the arm rules: the char*/const char* twins of every
// ladder collapse onto the String seat (H-015); the friend +
// arms are the pure vendor commutes (return k+i) and render as
// the members they call; the <= heads here render Int/Long/
// Double/Float/String only - the <=(global)/(mstring) tails are
// the landed LibmpscppMisc5.kt le seats (one-render, not
// duplicated); the scalar (int i, mstring k) friend crumbs ride
// the mstringCtorM10 unit-301 seats where vendor builds tmp=i.
// Consult H-014 hit-reuse, timestamp 2026-11-22T06:23:00Z.
//
// SEAT FLIPS this unit owns (the misc-chunk precedent): the
// mstringPlus/mstringMinus/mstringDiv (x7 each) and mstringRem
// (x5) Stubs seats flip to the member renders below - the
// unit-147 Misc8 addAssign/subAssign frontends and the Misc9
// era call sites bind through them unchanged; the NEW
// Stubs.add1/add3 seats are raised here (the interp-arith
// backend owners, throwing H-014 pending their own batch).

// cz twin of the M10 file (the per-file NUL-terminated buffer
// adapter, the czM7 precedent).
private fun czM9(s: String): CharArray {
    val b = CharArray(STR_MAX)
    CstringOps.copyAtS(b, 0, s)
    return b
}

// ------------------------------------------------------------------
// operator= (global) - the tpx-checked assignment
// ------------------------------------------------------------------

// mstring& mstring::operator=(global i) - the invoke2 = seat (the
// Hash.invoke2 precedent; the Misc8 scalar assignment family).
fun Mstring.invoke2(i: GlobalRef): Mstring {
    val tmp = CharArray(STR_MAX)

    Stubs.get_global(czM9(i.get()), tmp)

    if (Globals.svPtr.tpx == 0L) {
        Globals.GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    str = CstringOps.cstring(tmp)
    return this
}

// ------------------------------------------------------------------
// operator! / ++ / -- / unary + / unary -
// ------------------------------------------------------------------

// mstring mstring::operator!() - the null/zero test (Length()==0
// or the "0" literal; the eq(String) Misc5 seat for the latter).
fun Mstring.notOp(): Mstring {
    if (str.isEmpty() || str == "0") {
        return Mstring("1")
    }
    return Mstring("0")
}

// mstring mstring::operator++()
fun Mstring.incPre(): Mstring {
    val r = Stubs.mstringPlus(this, 1)
    str = r.str
    return this
}

// mstring mstring::operator--()
fun Mstring.decPre(): Mstring {
    val r = Stubs.mstringMinus(this, 1)
    str = r.str
    return this
}

// mstring mstring::operator++(int) - the copy BEFORE the bump.
fun Mstring.incPost(): Mstring {
    val tmp = Mstring(str)
    val r = Stubs.mstringPlus(this, 1)
    str = r.str
    return tmp
}

// mstring mstring::operator--(int)
fun Mstring.decPost(): Mstring {
    val tmp = Mstring(str)
    val r = Stubs.mstringMinus(this, 1)
    str = r.str
    return tmp
}

// mstring mstring::operator+() - unary plus: *this + 0.
fun Mstring.unaryPlus(): Mstring =
    Stubs.mstringPlus(this, 0)

// mstring mstring::operator-() - unary minus: 0 - *this (the
// friend int commute seat below).
fun Mstring.unaryMinus(): Mstring =
    mstringMinusM9(0, this)

// ------------------------------------------------------------------
// operator+ member ladder
// ------------------------------------------------------------------

// mstring mstring::operator+(int i) and +(long i): the add1
// backend (the int domain; the long arm casts (int)i, kept).
fun Mstring.plus(i: Int): Mstring {
    val t1 = CharArray(64)
    Stubs.add1(czM9(str), i.toLong(), t1)
    return Mstring(CstringOps.cstring(t1))
}

fun Mstring.plus(i: Long): Mstring {
    val t1 = CharArray(64)
    Stubs.add1(czM9(str), i, t1)
    return Mstring(CstringOps.cstring(t1))
}

// mstring mstring::operator+(double i) and +(float i): the add3
// backend (the double domain).
fun Mstring.plus(i: Double): Mstring {
    val t1 = CharArray(64)
    Stubs.add3(czM9(str), i, t1)
    return Mstring(CstringOps.cstring(t1))
}

fun Mstring.plus(i: Float): Mstring {
    val t1 = CharArray(64)
    Stubs.add3(czM9(str), i.toDouble(), t1)
    return Mstring(CstringOps.cstring(t1))
}

// mstring mstring::operator+(mstring i), +(string i) and the
// char*/const char* twin fold: the add backend.
fun Mstring.plus(i: Mstring): Mstring {
    val t1 = CharArray(64)
    Stubs.add(czM9(str), czM9(i.str), t1)
    return Mstring(CstringOps.cstring(t1))
}

fun Mstring.plus(i: String): Mstring {
    val t1 = CharArray(64)
    Stubs.add(czM9(str), czM9(i), t1)
    return Mstring(CstringOps.cstring(t1))
}

// mstring mstring::operator+(global i) - VENDOR QUIRK: NO tpx
// check between the get_global and the add; the read buffer is
// BOTH add operands (the vendor t1 aliasing, H-012), so the fold
// rides the STR_MAX product.
fun Mstring.plus(i: GlobalRef): Mstring {
    val t1 = CharArray(STR_MAX)

    Stubs.get_global(czM9(i.get()), t1)

    Stubs.add(czM9(str), t1, t1)

    return Mstring(CstringOps.cstring(t1))
}

// ------------------------------------------------------------------
// operator|| - the concat family
// ------------------------------------------------------------------

// mstring mstring::operator||(mstring i)
fun Mstring.concatOr(i: Mstring): Mstring = Mstring(str + i.str)

// mstring mstring::operator||(int i) / (long i) / (float i) /
// (double i): the tmp=i mstring ctor crumb then the plain string
// concat (the mstringCtorM10 seats, unit 301).
fun Mstring.concatOr(i: Int): Mstring = Mstring(str + mstringCtorM10(i).str)

fun Mstring.concatOr(i: Long): Mstring = Mstring(str + mstringCtorM10(i).str)

fun Mstring.concatOr(i: Float): Mstring = Mstring(str + mstringCtorM10(i).str)

fun Mstring.concatOr(i: Double): Mstring = Mstring(str + mstringCtorM10(i).str)

// mstring mstring::operator||(string i) and the char* twin fold.
fun Mstring.concatOr(i: String): Mstring = Mstring(str + i)

// mstring mstring::operator||(global i) - the i >> t1 read via
// the unit-301 shrStr seat, then the concat.
fun Mstring.concatOr(i: GlobalRef): Mstring = Mstring(str + i.shrStr())

// mstring operator||(string i, mstring s) - the const char* twin
// folds onto the String seat (H-015).
fun mstringOrM9(i: String, s: Mstring): Mstring = Mstring(i + s.str)

// mstring operator||(global i, mstring s)
fun mstringOrM9(i: GlobalRef, s: Mstring): Mstring = Mstring(i.shrStr() + s.str)

// mstring operator||(int i, mstring s) / (long i)
fun mstringOrM9(i: Int, s: Mstring): Mstring = Mstring(mstringCtorM10(i).str + s.str)

fun mstringOrM9(i: Long, s: Mstring): Mstring = Mstring(mstringCtorM10(i).str + s.str)

// mstring operator||(float i, mstring s) / (double i) - the
// vendor a=t1+s full-mstring plus, not the raw concat.
fun mstringOrM9(i: Float, s: Mstring): Mstring = mstringCtorM10(i).plus(s)

fun mstringOrM9(i: Double, s: Mstring): Mstring = mstringCtorM10(i).plus(s)

// ------------------------------------------------------------------
// operator- member ladder
// ------------------------------------------------------------------

// mstring mstring::operator-(int i) / (long i) / (double i) /
// (float i) - the sub over cvt crumbs.
fun Mstring.minus(i: Int): Mstring {
    val t1 = CharArray(64)
    Stubs.sub(czM9(str), czM9(cvt(i)), t1)
    return Mstring(CstringOps.cstring(t1))
}

fun Mstring.minus(i: Long): Mstring {
    val t1 = CharArray(64)
    Stubs.sub(czM9(str), czM9(cvt(i)), t1)
    return Mstring(CstringOps.cstring(t1))
}

fun Mstring.minus(i: Double): Mstring {
    val t1 = CharArray(64)
    Stubs.sub(czM9(str), czM9(cvt(i)), t1)
    return Mstring(CstringOps.cstring(t1))
}

fun Mstring.minus(i: Float): Mstring {
    val t1 = CharArray(64)
    Stubs.sub(czM9(str), czM9(cvt(i)), t1)
    return Mstring(CstringOps.cstring(t1))
}

// mstring mstring::operator-(mstring i) / (string i) and the
// char* twin folds.
fun Mstring.minus(i: Mstring): Mstring {
    val t1 = CharArray(64)
    Stubs.sub(czM9(str), czM9(i.str), t1)
    return Mstring(CstringOps.cstring(t1))
}

fun Mstring.minus(i: String): Mstring {
    val t1 = CharArray(64)
    Stubs.sub(czM9(str), czM9(i), t1)
    return Mstring(CstringOps.cstring(t1))
}

// mstring mstring::operator-(global i) - the UNCHECKED read and
// the t1 aliasing again (the plus(global) quirk twin).
fun Mstring.minus(i: GlobalRef): Mstring {
    val t1 = CharArray(STR_MAX)

    Stubs.get_global(czM9(i.get()), t1)

    Stubs.sub(czM9(str), t1, t1)

    return Mstring(CstringOps.cstring(t1))
}

// ------------------------------------------------------------------
// operator% member ladder - the atol integer domain (the long
// j int j asymmetry kept: the first two arms are the long domain,
// the rest the int domain, H-012 per-arm note).
// ------------------------------------------------------------------

// mstring mstring::operator%(int i) / (long i)
fun Mstring.rem(i: Int): Mstring {
    var j = atollShared(str)
    j = j % i
    return Mstring(j.toString())
}

fun Mstring.rem(i: Long): Mstring {
    var j = atollShared(str)
    j = j % i
    return Mstring(j.toString())
}

// mstring mstring::operator%(mstring i) / (string i) and the
// char* twin folds - the INT domain (j = int; atollShared is the
// widened seat, the int truncation ledgered).
fun Mstring.rem(i: Mstring): Mstring {
    val j = atollShared(str).toInt()
    val r = j % atollShared(i.str).toInt()
    return Mstring(r.toString())
}

fun Mstring.rem(i: String): Mstring {
    val j = atollShared(str).toInt()
    val r = j % atollShared(i).toInt()
    return Mstring(r.toString())
}

// mstring mstring::operator%(global i) - int k=i invokes the
// operator int() conversion (the Stubs.globalGetInt seat, raised;
// the Misc7 toInt idiom).
fun Mstring.rem(i: GlobalRef): Mstring {
    val j = atollShared(str).toInt()
    val k = Stubs.globalGetInt(i)
    val r = j % k
    return Mstring(r.toString())
}

// ------------------------------------------------------------------
// operator/ member ladder - the divx backend
// ------------------------------------------------------------------

// mstring mstring::operator/(int i) / (long i) / (double i) /
// (float i)
fun Mstring.div(i: Int): Mstring {
    val t1 = CharArray(64)
    Stubs.divx(czM9(str), czM9(cvt(i)), t1)
    return Mstring(CstringOps.cstring(t1))
}

fun Mstring.div(i: Long): Mstring {
    val t1 = CharArray(64)
    Stubs.divx(czM9(str), czM9(cvt(i)), t1)
    return Mstring(CstringOps.cstring(t1))
}

fun Mstring.div(i: Double): Mstring {
    val t1 = CharArray(64)
    Stubs.divx(czM9(str), czM9(cvt(i)), t1)
    return Mstring(CstringOps.cstring(t1))
}

fun Mstring.div(i: Float): Mstring {
    val t1 = CharArray(64)
    Stubs.divx(czM9(str), czM9(cvt(i)), t1)
    return Mstring(CstringOps.cstring(t1))
}

// mstring mstring::operator/(mstring i) / (string i) and the
// char* twin folds.
fun Mstring.div(i: Mstring): Mstring {
    val t1 = CharArray(64)
    Stubs.divx(czM9(str), czM9(i.str), t1)
    return Mstring(CstringOps.cstring(t1))
}

fun Mstring.div(i: String): Mstring {
    val t1 = CharArray(64)
    Stubs.divx(czM9(str), czM9(i), t1)
    return Mstring(CstringOps.cstring(t1))
}

// mstring mstring::operator/(global i) - the UNCHECKED read and
// t1 aliasing (the family quirk twin).
fun Mstring.div(i: GlobalRef): Mstring {
    val t1 = CharArray(STR_MAX)

    Stubs.get_global(czM9(i.get()), t1)

    Stubs.divx(czM9(str), t1, t1)

    return Mstring(CstringOps.cstring(t1))
}

// ------------------------------------------------------------------
// friend % / + / - / scalar families
// ------------------------------------------------------------------

// mstring operator%(int i, mstring k) / (long i)
fun mstringRemM9(i: Int, k: Mstring): Mstring {
    val r = i % atollShared(k.str).toInt()
    return Mstring(r.toString())
}

fun mstringRemM9(i: Long, k: Mstring): Mstring {
    val r = i % atollShared(k.str)
    return Mstring(r.toString())
}

// mstring operator%(string i, mstring k) and the char* twin fold
fun mstringRemM9(i: String, k: Mstring): Mstring {
    val r = atollShared(i).toInt() % atollShared(k.str).toInt()
    return Mstring(r.toString())
}

// mstring operator%(global i, mstring k) - the i >> m LONG
// out-param read (the ShrLongM10 holder, unit 301).
fun mstringRemM9(i: GlobalRef, k: Mstring): Mstring {
    val h = ShrLongM10()
    i.shr(h)
    val r = h.v % atollShared(k.str)
    return Mstring(r.toString())
}

// mstring operator+(scalar i, mstring k) - the VENDOR COMMUTE set
// (all eight arms are return k+i; the char* twins fold onto the
// String seat, H-015): int, long, double, float, string, global.
fun mstringPlusM9(i: Int, k: Mstring): Mstring = k.plus(i)

fun mstringPlusM9(i: Long, k: Mstring): Mstring = k.plus(i)

fun mstringPlusM9(i: Double, k: Mstring): Mstring = k.plus(i)

fun mstringPlusM9(i: Float, k: Mstring): Mstring = k.plus(i)

fun mstringPlusM9(i: String, k: Mstring): Mstring = k.plus(i)

fun mstringPlusM9(i: GlobalRef, k: Mstring): Mstring = k.plus(i)

// mstring operator-(int i, mstring k) / (long i) / (double i) /
// (float i) - the reversed sub (the scalar cvt crumb first).
fun mstringMinusM9(i: Int, k: Mstring): Mstring {
    val t1 = CharArray(64)
    Stubs.sub(czM9(cvt(i)), czM9(k.str), t1)
    return Mstring(CstringOps.cstring(t1))
}

fun mstringMinusM9(i: Long, k: Mstring): Mstring {
    val t1 = CharArray(64)
    Stubs.sub(czM9(cvt(i)), czM9(k.str), t1)
    return Mstring(CstringOps.cstring(t1))
}

fun mstringMinusM9(i: Double, k: Mstring): Mstring {
    val t1 = CharArray(64)
    Stubs.sub(czM9(cvt(i)), czM9(k.str), t1)
    return Mstring(CstringOps.cstring(t1))
}

fun mstringMinusM9(i: Float, k: Mstring): Mstring {
    val t1 = CharArray(64)
    Stubs.sub(czM9(cvt(i)), czM9(k.str), t1)
    return Mstring(CstringOps.cstring(t1))
}

// mstring operator-(string i, mstring k) and the char* twin folds
fun mstringMinusM9(i: String, k: Mstring): Mstring {
    val t1 = CharArray(64)
    Stubs.sub(czM9(i), czM9(k.str), t1)
    return Mstring(CstringOps.cstring(t1))
}

// mstring operator-(global i, mstring k) - the i >> t2 string
// read, reversed sub.
fun mstringMinusM9(i: GlobalRef, k: Mstring): Mstring {
    val t1 = CharArray(64)
    val t2 = i.shrStr()
    Stubs.sub(czM9(t2), czM9(k.str), t1)
    return Mstring(CstringOps.cstring(t1))
}

// mstring operator/(int i, mstring k) / (long i) / (double i) /
// (float i) - the reversed divx.
fun mstringDivM9(i: Int, k: Mstring): Mstring {
    val t1 = CharArray(64)
    Stubs.divx(czM9(cvt(i)), czM9(k.str), t1)
    return Mstring(CstringOps.cstring(t1))
}

fun mstringDivM9(i: Long, k: Mstring): Mstring {
    val t1 = CharArray(64)
    Stubs.divx(czM9(cvt(i)), czM9(k.str), t1)
    return Mstring(CstringOps.cstring(t1))
}

fun mstringDivM9(i: Double, k: Mstring): Mstring {
    val t1 = CharArray(64)
    Stubs.divx(czM9(cvt(i)), czM9(k.str), t1)
    return Mstring(CstringOps.cstring(t1))
}

fun mstringDivM9(i: Float, k: Mstring): Mstring {
    val t1 = CharArray(64)
    Stubs.divx(czM9(cvt(i)), czM9(k.str), t1)
    return Mstring(CstringOps.cstring(t1))
}

// mstring operator/(string i, mstring k) and the char* twin folds
fun mstringDivM9(i: String, k: Mstring): Mstring {
    val t1 = CharArray(64)
    Stubs.divx(czM9(i), czM9(k.str), t1)
    return Mstring(CstringOps.cstring(t1))
}

// mstring operator/(global i, mstring k) - the i >> t2 read,
// reversed divx.
fun mstringDivM9(i: GlobalRef, k: Mstring): Mstring {
    val t1 = CharArray(64)
    val t2 = i.shrStr()
    Stubs.divx(czM9(t2), czM9(k.str), t1)
    return Mstring(CstringOps.cstring(t1))
}

// ------------------------------------------------------------------
// >= / <= comparison ladders
// ------------------------------------------------------------------

// bool mstring::operator>=(int i) / (long i) - the atol domain.
fun Mstring.ge(i: Int): Boolean {
    val j = atollShared(str)
    return j >= i
}

fun Mstring.ge(i: Long): Boolean {
    val j = atollShared(str)
    return j >= i
}

// bool mstring::operator>=(double i) / (float i) - the atof
// domain (the long j widened to double per the C promotion).
fun Mstring.ge(i: Double): Boolean {
    val j = atollShared(str)
    return j.toDouble() >= i
}

fun Mstring.ge(i: Float): Boolean {
    val j = atollShared(str)
    return j.toDouble() >= i
}

// bool mstring::operator>=(string i) and the char* twin folds -
// the lexicographic string compare.
fun Mstring.ge(i: String): Boolean = str >= i

// bool mstring::operator>=(global i) - the i >> t1 read first.
fun Mstring.ge(i: GlobalRef): Boolean {
    val t1 = i.shrStr()
    return str >= t1
}

// bool mstring::operator>=(mstring i)
fun Mstring.ge(i: Mstring): Boolean = str >= i.str

// bool mstring::operator<=(int i) / (long i)
fun Mstring.le(i: Int): Boolean {
    val j = atollShared(str)
    return j <= i
}

fun Mstring.le(i: Long): Boolean {
    val j = atollShared(str)
    return j <= i
}

// bool mstring::operator<=(double i) / (float i)
fun Mstring.le(i: Double): Boolean {
    val j = atollShared(str)
    return j.toDouble() <= i
}

fun Mstring.le(i: Float): Boolean {
    val j = atollShared(str)
    return j.toDouble() <= i
}

// bool mstring::operator<=(string i) and the char* twin folds -
// the CHUNK TAIL arm: the dump cuts this arm after its first body
// line; rendered WHOLE (the one-render rule). The <=(global) and
// <=(mstring) tails of the ladder are the landed Misc5 le seats.
fun Mstring.le(i: String): Boolean = str <= i
