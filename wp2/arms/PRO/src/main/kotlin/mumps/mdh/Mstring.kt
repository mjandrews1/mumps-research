package mumps.mdh

/**
 * PRO-arm minimal carrier for the vendor C++ class mstring
 * (libmpscpp machinery). Not itself a corpus unit; members are added per
 * translated unit as needed (one-owner policy, cf. rules_v0.pl h004).
 */
class Mstring(var value: String = "") {

    /** c_str() equivalent for translated call sites. */
    fun c_str(): String = value

    /**
     * s_str() equivalent (B1 unit 31: the vendor Hash::operator()
     * (mstring) reads via s_str() while the global::operator() family
     * uses c_str(); kept distinct to preserve the vendor call shape).
     */
    fun s_str(): String = value

    // --- added by B4 unit 97 (libmpscpp.cpp::<misc-9>) ---

    /**
     * (u97) Vendor operator const char*() (libmpscpp.cpp:9840):
     * return data.c_str(); toString serves as the Kotlin implicit
     * conversion point (the explicit c_str() above predates this
     * unit and remains).
     */
    override fun toString(): String = value

    /**
     * (u97) The vendor == / != overload families (libmpscpp.cpp:
     * 9726-9837) ride Kotlin's equals: C++ compares data against
     * the rhs - atol for Int/Long rhs, atof for Double/Float,
     * value equality for string-ish rhs (vendor char-star and
     * const-char-star, plus std::string - all map to String), extraction (i >> t1) for
     * global rhs, data equality for mstring rhs. The != families
     * fold into Kotlin's negated equals (h015); the <= family is
     * translated as compareTo extensions in LibmpscppMisc9.kt.
     */
    override fun equals(other: Any?): Boolean = when (other) {
        is Int -> atol_(value) == other.toLong()
        is Long -> atol_(value) == other
        is Float -> atof_(value) == other.toDouble()
        is Double -> atof_(value) == other
        is String -> value == other
        is Mstring -> value == other.value
        is Global -> {
            val t1 = CharArray(STR_MAX_)
            other.readInto(t1) // vendor: i >> t1 (h014 stub, unit 97)
            value == mumps.interp.cstringAt(t1)
        }
        else -> false // no vendor fallback overload
    }

    /**
     * (u97) Required companion to the equals override (Kotlin
     * equals/hashCode consistency); the vendor has no hashCode -
     * adaptation (h015).
     */
    override fun hashCode(): Int = value.hashCode()

    // ------------------------------------------------------------
    // --- B5 unit 131 completion (include/mumpsc/mstringClass.h::
    //     <misc>): the full header declaration surface, rendered
    //     on the carrier. Identity: units.csv row 130 - c, loc
    //     331, comment_ratio 0.006, pointer_marks 0,
    //     control_marks 2, score 51.1, rank 234 (the 389-line
    //     physical header minus blanks/comments). The existing
    //     carrier members (value/data, c_str, s_str, toString,
    //     equals/hashCode - u31/u97 seats) are this unit's
    //     FRONT-RUN, verified against the header, no
    //     redeclaration (u124 precedent).
    //
    // INTERFACE LEDGER (vendor mstringClass.h anchors - each
    // family's seat disposition):
    //  - ctors :39-47 - the const-char-star/string/default seats
    //    are the primary constructor; mstring(const mstring&) has
    //    no Kotlin seat (reference copies, h015 note); the
    //    numeric ctors render below as secondary constructors.
    //  - friends :51-57 - class global (DONE, GlobalCarrier/
    //    GlobalClassH), Hash::operator=(mstring) (DONE, u31),
    //    global::Piece(mstring&,int,int) (pending owner),
    //    SQL_Table(mstring,int) (DONE, SQLTable.kt u34).
    //  - Eval() :59 - DONE (MstringEval.kt extension seat).
    //  - Translate x2/Soundex/replace/decorate/ends/begins :61-67
    //    - bodies owned by libmpscpp.cpp chunks not yet rendered;
    //    they raise with their first caller (h014; note: the
    //    mstring members are distinct from the global-class
    //    siblings of the same names).
    //  - Assign x7 :69-76 - assign(Mstring)/assign(String) DONE
    //    (LibmpscppMisc1), assign(global) via GlobalCarrier.
    //    assign(int/long/double) pending (h014).
    //  - SQL_Connect/SQL_Format/SQL_Command friends :78-80 -
    //    pending; the reference build resolves the SQL arm to
    //    rdbms-if.c (u125) - empty, neither SQL nor POSTGRES.
    //  - Concat x7 :82-88 - Concat(global) DONE (GlobalConcat);
    //    the mstring/string/int/long/double/char-star seats
    //    pending (h014).
    //  - Reverse/ReadLine x3 :90-93 - pending (h014; ReadLine's
    //    FILE*/istream edges ride h001 when the owner renders).
    //  - conversion operators :95-104 - seats below (Kotlin has
    //    no implicit-conversion operator point; explicit member
    //    functions, h015 fold like the u97 operator const
    //    char-star -> toString).
    //  - Pattern x4 :106-109 - pending (h014; the interp-side
    //    Patrn units are different vendor functions).
    //  - Query/Qlength/Qsubscript/FixGblRef :111-114 - pending;
    //    note the interp-sym.cpp::Qlength stub (u133) is a
    //    DIFFERENT vendor function, not this member.
    //  - Justify :116 - pending (h014).
    //  - Fnumber x2 :118-119 - Global.Fnumber DONE (u?);
    //    mstring members pending (h014).
    //  - Length x5/Ascii x2/zBlanks/Extract/Find x3/Piece x4
    //    :121-145 - the global-class siblings are DONE
    //    (GlobalExtract/GlobalFind etc.); the mstring members
    //    pending (h014).
    //  - operator families :147-327 - ==/!=/<= families DONE
    //    (u97 equals + LibmpscppMisc9 compareTo extensions);
    //    operator const char* :331 DONE (u97 toString); the
    //    remaining families (concat-op &, subscript [], ^=,
    //    += -= *= /= %=, binary + - * / %, ++/--, unary ! + -,
    //    friend reversed forms, and the stream <</>>) have no
    //    direct Kotlin operator points or pending owner seats -
    //    they raise with their first caller (h014; folds per
    //    h015 - named extension members, cf. plusAssignInt etc.
    //    in LibmpscppMisc1; stream edges h001).
    // ------------------------------------------------------------

    /** vendor :335 - private string name (the data field is the
     *  value carrier above, u97 binding). */
    var name: String = ""

    // --- vendor :40-45 numeric ctors (secondary constructor
    //     seats; the toString conversion is the minimal honest
    //     binding until the libmpscpp.cpp ctor-owner renders -
    //     h014 note, decimal string shape). ---
    constructor(v: Long) : this(v.toString())
    constructor(v: Int) : this(v.toString())
    constructor(v: Double) : this(v.toString())
    constructor(v: Float) : this(v.toString())
    constructor(v: Char) : this(v.toString())

    // --- vendor :95-104 conversion operators (h015 fold: no
    //     Kotlin implicit-conversion point; explicit member seats).
    //     B7 unit 188 (libmpscpp.cpp::<misc-6>, vendor 3113-3168)
    //     resolves the bodies: the vendor atoi()/atof() over the
    //     data string, with C implicit narrowing kept as explicit
    //     Kotlin conversions (the unsigned forms wrap negatives,
    //     as the C implicit conversions do). ---

    /** vendor :95 operator int() - i = atoi(data.c_str()). */
    fun toInt(): Int = atol_(value).toInt()

    /** vendor :96 operator float() - i = atof(data.c_str()). */
    fun toFloat(): Float = atof_(value).toFloat()

    /** vendor :97 operator double() - i = atof(data.c_str()). */
    fun toDouble(): Double = atof_(value)

    /** vendor :98 operator char() - i = atoi(data.c_str()); char i. */
    fun toChar(): Char = atol_(value).toInt().toChar()

    /** vendor :99 operator short() - i = atoi(data.c_str()); short i. */
    fun toShort(): Short = atol_(value).toInt().toShort()

    /** vendor :100 operator long() - i = atoi(data.c_str()); long i. */
    fun toLong(): Long = atol_(value)

    /** vendor :101 operator unsigned long() - atoi wrapped (C implicit). */
    fun toULong(): ULong = atol_(value).toULong()

    /** vendor :102 operator unsigned int() - atoi wrapped (C implicit). */
    fun toUInt(): UInt = atol_(value).toInt().toUInt()

    /** vendor :103 operator unsigned char() - atoi wrapped (C implicit). */
    fun toUByte(): UByte = atol_(value).toInt().toUByte()

    /** vendor :104 operator unsigned short() - atoi wrapped (C implicit). */
    fun toUShort(): UShort = atol_(value).toInt().toUShort()
}

/** strmax.h:3 STR_MAX 32000 (file-private, cf. Cosine unit 71). */
private const val STR_MAX_ = 32000

/** (u97) atol(data.c_str()) over the Kotlin String value (h009 local parser). */
private fun atol_(s: String): Long {
    var i = 0
    while (i < s.length && (s[i] == ' ' || s[i] == '\t')) i++
    var neg = false
    if (i < s.length && (s[i] == '+' || s[i] == '-')) {
        neg = s[i] == '-'
        i++
    }
    var v = 0L
    while (i < s.length && s[i] in '0'..'9') {
        v = v * 10 + (s[i] - '0')
        i++
    }
    return if (neg) -v else v
}

/** (u97) atof(data.c_str()) over the Kotlin String value (strtodC reuse, h014). */
private fun atof_(s: String): Double = mumps.interp.strtodC(toCbuf_(s))

/** (u97) String -> NUL-terminated C-string buffer (call-shape helper). */
private fun toCbuf_(s: String): CharArray {
    val b = CharArray(s.length + 1)
    for (k in s.indices) b[k] = s[k]
    b[s.length] = mumps.interp.NUL
    return b
}
