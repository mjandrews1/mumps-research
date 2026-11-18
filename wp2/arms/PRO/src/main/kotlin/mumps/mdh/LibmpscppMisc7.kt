package mumps.mdh

import mumps.interp.ErrorMessage
import mumps.interp.GlobalData
import mumps.interp.Merge
import mumps.interp.Msv
import mumps.interp.NUL
import mumps.interp.cleanLocks
import mumps.interp.cstringAt
import mumps.interp.interpret
import mumps.interp.makeLock
import mumps.interp.mglobal
import mumps.interp.writeCstring

/**
 * PRO-arm translation of libmpscpp.cpp::<misc-7>
 * (batch-8 unit 218, units.csv data line 218, rank 292).
 * Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference
 * source lines 3468-4148 (the chunk ends at the embed1 banner
 * at line 4151, the next unit's boundary).
 *
 * Identity verified against the study tool (tools/select_study_
 * subset.py): libmpscpp.cpp::<misc-7>, cpp, loc 600,
 * comment_ratio 0.065, pointer_marks 70, control_marks 53,
 * score 288.4, rank 292, batch 8 - exact match to
 * wp1/study-subset/units.csv line 218.
 *
 * Chunk contents (vendor lines):
 *  - 3464-3466 global::get() - the misc-6 chunk tail (u188
 *    boundary completion; rendered here, journaled).
 *  - 3468-3484 operator<<(ostream, global x).
 *  - 3486-3503 global::Merge(global rhs).
 *  - 3505-3513 global::Lock(); 3515-3523 global::UnLock().
 *  - 3525-3532 CleanLocks(void); 3534-3536 CleanAllLocks(void).
 *  - 3538-3567 global::Set x5 (char*, string, long, int, double).
 *  - 3569-3627 global::Get x5 (char*, string&, no-arg, double*,
 *    long*).
 *  - 3629-3639 global::Kill() - RESOLVES the GlobalCarrier
 *    Kill() h014 stub (body moved into the carrier member, the
 *    carrier call site at :146 binds unchanged).
 *  - 3641-3645 global::Order(int); 3647-3657 global::C_Order(int);
 *    3659-3662 Order_Next; 3664-3667 Order_Prior;
 *    3669-3677 global::Data().
 *  - 3679-3745 mstring::Assign x7 (global, mstring, int, double,
 *    long, char*, string - the string form delegates to char*).
 *  - 3747-3877 SQL_Disconnect / SQL_Connect x3 / SQL_Format x4 /
 *    SQL_Command x3 / SQL_Table x4 / SQL_Message / SQL_Open /
 *    SQL_Native.
 *  - 3880-3934 operator& x14 (7 global receivers + 7 mstring
 *    receivers, each a one-line delegate to Concat).
 *  - 3936-4076 global::Concat x7 - Concat(global) is B3 unit 91
 *    (u33 front-run: verification note only, its seat in
 *    GlobalConcat.kt binds unchanged); this unit renders the
 *    double/long/int/char-star/string/mstring members.
 *  - 4078-4148 mstring::Concat x7.
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h007 char_buffer: STR_MAX scratch buffers rebuilt per call
 *    (strcat/strcpy fold to string concatenation at the edges,
 *    GlobalConcat unit-91 precedent).
 *  - h004 globals_ext: file-scope svPtr -> MdhState.svPtr; the
 *    static Set/Data buffers -> Misc7State object.
 *  - h012 unsigned_byte_domain: buffers are Char(0..255) domains.
 *  - h014 out_of_scope_call: Mglobal/Merge/GlobalData/interpret/
 *    makeLock/cleanLocks/KiLL/Mltoa/ErrorMessage reused via
 *    their seats (Merge is the InterpStubs stub, owner row 226
 *    this batch; mglobal owner: global-array machinery;
 *    GlobalData is the u204 real seat; get_global/set_global
 *    are the libmpscpp.h:117-118 macros, RESOLVED in MdhStubs
 *    by this unit).
 *  - h015 grammar_collision: pre-push cross-file scan: new
 *    top-level Set/Get/Order/C_Order/Order_Next/Order_Prior/
 *    Data/Merge/Lock/UnLock/Assign/SQL_/Concat/amp/get seats -
 *    no collisions (the u33 Concat(Global) and u34 SQL_Table
 *    (String, Int) seats bind unchanged; Mstring.Concat and
 *    operator& owners were untranslated).
 *  - h017 naming: vendor names kept; C++ operator<< and
 *    operator& have no Kotlin overload - operator<< renders as
 *    streamInsert (unit-31 operator-fold precedent), operator&
 *    as the amp family (ampersand fold, one-line Concat
 *    delegates); get() renders as the get extension (boundary
 *    completion, see above).
 *  - h018 preprocessor_conditional: none in this chunk.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - operator<<: get_global fills tmp, then TWO tpx==0 tests -
 *    the second (ErrorMessage "global not found", -1) is dead
 *    after the throw; kept as a comment.
 *  - Merge: refs are prefixed with '^', chars below 30 are
 *    remapped to 128, and one trailing 128,128 pair is chopped
 *    (only the last pair; kept).  ::Merge is the sysfunc runtime
 *    (row 226 owner).
 *  - Get(char* s) fills the caller's buffer but returns the
 *    MEMBER tmp (vendor bug; kept - returns the carrier tmp).
 *  - Get(long*) carries a second dead tpx==0 ErrorMessage arm.
 *  - C_Order: an invalid dir errors but still proceeds with
 *    ORDERPREV (the vendor else arm assigns it); kept.
 *  - Data(): static long i = strlen(ref) is a dead store (i is
 *    immediately overwritten by atol(tmp)); kept as the
 *    Misc7State.i assignment.
 *  - SQL_Format(): cmd is built with a TRAILING SPACE
 *    ("sql/c ") even in the no-arg form - fossil, kept.
 *  - SQL_Table(char*, int): the "ztabzize" vendor typo lives in
 *    the u34 seat (SQLTable.kt); this unit renders only the
 *    untranslated siblings.
 *  - SQL_Open/SQL_Native: mstring m; if (m==1) folds to a
 *    string comparison with "1" (mstring's operator== domain).
 */

private const val STR_MAX = 32000 // vendor strmax.h:3 (file-private, cf. unit 22/97)
private const val RETRIEVE = 0 // vendor defines.h:83
private const val STORE = 1 // vendor defines.h:84
private const val ORDERNEXT = 12 // vendor defines.h (Order next op)
private const val ORDERPREV = 13 // vendor defines.h (Order prior op)
private const val GLOBAL_NOT_FOUND = 2 // vendor libmpscpp.h:71
private const val DOLLAR = 36.toChar() // vendor '$' (h015 carrier)

/**
 * Carrier for the vendor file-scope statics of this chunk:
 * Set(long)/Set(int)/Set(double) share static char tmp[64];
 * Data() owns static char tmp[4] and static long i
 * (h004 globals_ext; the Data() i store is dead, kept).
 */
private object Misc7State {
    /** vendor: static char tmp[64] (Set numeric forms). */
    val tmp64 = CharArray(64)

    /** vendor: static char tmp[4] (Data()). */
    val tmp4 = CharArray(4)

    /** vendor: static long i (Data()). */
    var i: Long = 0
}

/** Per-file C-string bridge (h007; Misc-6 precedent). */
private fun toCbuf(s: String): CharArray {
    val b = CharArray(STR_MAX)
    writeCstring(b, s)
    return b
}

/**
 * (u218) vendor 3464-3466 global::get() { return ref; } - the
 * misc-6 chunk tail (u188 boundary completion, journaled): the
 * B7 render's chunk ended at the ctor epilogue and did not
 * reach this tail; rendered here so the Get/Set call sites
 * below bind to a real seat.
 */
fun Global.get(): CharArray = toCbuf(ref)

/**
 * vendor 3468-3484 operator<<(ostream&, global x) - print a
 * global's value to a stream (h017: operator<< fold, unit-31
 * operator-fold precedent).  The dead second tpx test is kept
 * as a comment.
 */
fun Global.streamInsert(out: java.io.PrintStream): java.io.PrintStream {
    val svPtr: Msv = MdhState.svPtr
    val tmp = CharArray(STR_MAX)

    // vendor: get_global(x.get(), tmp);  [macro RESOLVED in MdhStubs]
    get_global(toCbuf(ref), tmp)

    if (svPtr.tpx == 0L) {
        println("\n*** Global not found: " + ref)
        MdhState.globalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    // vendor dead arm: if (svPtr->tpx==0) ErrorMessage("global
    // not found",-1); - unreachable after the throw; kept as note.

    out.print(cstringAt(tmp))
    return out
}

/**
 * vendor 3486-3503 global::Merge(global rhs) - merge this
 * global into rhs via the sysfunc runtime Merge (InterpStubs
 * stub; owner interp-sysfunc.cpp::Merge, row 226 this batch).
 * '^' prefix, below-30 to 128 remap, one trailing 128,128 pair
 * chop - kept verbatim.
 */
fun Global.Merge(rhs: Global): Int {
    val tmp0 = CharArray(STR_MAX)
    val tmp1 = CharArray(STR_MAX)

    tmp0[0] = '^'
    writeCstring(tmp0, ref, 1)
    tmp1[0] = '^'
    writeCstring(tmp1, rhs.ref, 1)

    var i = 0
    while (tmp0[i] != NUL) {
        if (tmp0[i].code < 30) tmp0[i] = 128.toChar() // vendor: <30 remap
        i++
    }
    if (tmp0[i - 2] == 128.toChar() && tmp0[i - 1] == 128.toChar()) tmp0[i - 1] = NUL

    i = 0
    while (tmp1[i] != NUL) {
        if (tmp1[i].code < 30) tmp1[i] = 128.toChar()
        i++
    }
    if (tmp1[i - 2] == 128.toChar() && tmp1[i - 1] == 128.toChar()) tmp1[i - 1] = NUL

    return Merge(tmp0, 0, tmp1, 0, MdhState.svPtr)
}

/**
 * vendor 3505-3513 global::Lock() - incremental lock of the
 * whole global reference; tpx carries the success flag.
 */
fun Global.Lock(): Int {
    val atmp = CharArray(32)
    Mltoa(ProcessHandle.current().pid(), atmp) // vendor: Mltoa(GETPID(), atmp)

    val r = makeLock(ref, "1", cstringAt(atmp), ' ', null)
    MdhState.svPtr.tpx = if (r == "1") 1L else 0L
    return MdhState.svPtr.tpx.toInt()
}

/**
 * vendor 3515-3523 global::UnLock() - same as Lock with the
 * '-' release flag.
 */
fun Global.UnLock(): Int {
    val atmp = CharArray(32)
    Mltoa(ProcessHandle.current().pid(), atmp)

    val r = makeLock(ref, "1", cstringAt(atmp), '-', null)
    MdhState.svPtr.tpx = if (r == "1") 1L else 0L
    return MdhState.svPtr.tpx.toInt()
}

/**
 * vendor 3525-3532 CleanLocks(void) - free all locks held by
 * this job (cleanLocks epilogue gate: LockDone).
 */
fun CleanLocks() {
    val atmp = CharArray(32)
    Mltoa(ProcessHandle.current().pid(), atmp) // vendor comment: free all locks first

    if (MdhState.svPtr.LockDone != 0) cleanLocks(cstringAt(atmp))
}

/** vendor 3534-3536 CleanAllLocks(void) - remove the lock table. */
fun CleanAllLocks() {
    java.io.File("./Mumps.Locks").delete() // vendor: remove("./Mumps.Locks")
}

/**
 * vendor 3538-3541 global::Set(char* s) (and the 3543-3545
 * string form - folds here, both write the C-string through the
 * set_global macro).  Returns the set value.
 */
fun Global.Set(s: String): String {
    set_global(toCbuf(ref), toCbuf(s))
    return s
}

/** vendor 3547-3552 global::Set(long s) - static tmp[64]. */
fun Global.Set(s: Long): Long {
    writeCstring(Misc7State.tmp64, s.toString()) // vendor: sprintf(tmp,"%ld",s)
    set_global(toCbuf(ref), Misc7State.tmp64)
    return s
}

/** vendor 3554-3559 global::Set(int s). */
fun Global.Set(s: Int): Int {
    writeCstring(Misc7State.tmp64, s.toString()) // vendor: sprintf(tmp,"%d",s)
    set_global(toCbuf(ref), Misc7State.tmp64)
    return s
}

/** vendor 3561-3567 global::Set(double s) - %f format. */
fun Global.Set(s: Double): Double {
    writeCstring(Misc7State.tmp64, String.format(java.util.Locale.getDefault(), "%f", s)) // vendor: sprintf(tmp,"%f",s)
    set_global(toCbuf(ref), Misc7State.tmp64)
    return s
}

/**
 * vendor 3569-3578 global::Get(char* s) - the caller's buffer
 * is filled by get_global but the MEMBER tmp is returned
 * (vendor bug, kept); the 3580-3589 string& form folds here
 * (s = tmp on return, journaled).
 */
fun Global.Get(s: CharArray): CharArray {
    val svPtr: Msv = MdhState.svPtr
    get_global(toCbuf(ref), s)

    if (svPtr.tpx == 0L) {
        MdhState.globalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    return tmp // carrier member (u67), not s - vendor bug kept
}

/**
 * vendor 3591-3599 global::Get() - fills the member tmp and
 * returns it.
 */
fun Global.Get(): CharArray {
    val svPtr: Msv = MdhState.svPtr
    get_global(toCbuf(ref), tmp)

    if (svPtr.tpx == 0L) {
        MdhState.globalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    return tmp
}

/**
 * vendor 3601-3611 global::Get(double* x) - retrieve into the
 * member tmp, convert with atof (atof fold, u131 atof_
 * precedent), write back through the pointer fold.
 */
fun Global.Get(x: DoubleArray): Double {
    val svPtr: Msv = MdhState.svPtr
    get_global(toCbuf(ref), tmp)

    if (svPtr.tpx == 0L) {
        MdhState.globalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    x[0] = cstringAt(tmp).toDouble()
    return x[0]
}

/**
 * vendor 3613-3625 global::Get(long* x) - carries a second,
 * dead tpx==0 ErrorMessage arm after the throw (kept as note;
 * atol fold, u131 atol_ precedent).
 */
fun Global.Get(x: LongArray): Long {
    val svPtr: Msv = MdhState.svPtr
    get_global(toCbuf(ref), tmp)

    if (svPtr.tpx == 0L) {
        MdhState.globalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    // vendor dead arm: if (svPtr->tpx==0) ErrorMessage("global
    // not found",-1); - unreachable after the throw.

    x[0] = cstringAt(tmp).toLong()
    return x[0]
}

/**
 * vendor 3641-3645 global::Order(int dir) - order over the
 * global via C_Order, returning an mstring.
 */
fun Global.Order(dir: Int): Mstring {
    // vendor: tmp = (*this).C_Order(dir); return tmp;
    return Mstring(cstringAt(C_Order(dir)))
}

/**
 * vendor 3647-3657 global::C_Order(int dir) - invalid dir
 * errors via ErrorMessage but still proceeds with ORDERPREV
 * (the vendor else arm; kept).  Fills the member tmp.
 */
fun Global.C_Order(dir: Int): CharArray {
    if (dir != 1 && dir != -1) ErrorMessage(toCbuf("Unknown parameter to Order\n"), 0L)

    val d = if (dir == 1) ORDERNEXT else ORDERPREV
    MdhState.svPtr.tpx = mglobal(d, toCbuf(ref), tmp, MdhState.svPtr).toLong()
    return tmp
}

/** vendor 3659-3662 global::Order_Next(char* s). */
fun Global.Order_Next(s: CharArray): Int {
    MdhState.svPtr.tpx = mglobal(ORDERNEXT, toCbuf(ref), s, MdhState.svPtr).toLong()
    return MdhState.svPtr.tpx.toInt()
}

/** vendor 3664-3667 global::Order_Prior(char* s). */
fun Global.Order_Prior(s: CharArray): Int {
    MdhState.svPtr.tpx = mglobal(ORDERPREV, toCbuf(ref), s, MdhState.svPtr).toLong()
    return MdhState.svPtr.tpx.toInt()
}

/**
 * vendor 3669-3677 global::Data() - the u204 three-arg
 * GlobalData real seat; static tmp[4]/static long i carried on
 * Misc7State (the i=strlen(ref) store is dead, kept).
 */
fun Global.Data(): Int {
    Misc7State.i = ref.length.toLong() // vendor dead store: i=strlen(ref)

    GlobalData(toCbuf(ref), Misc7State.tmp4, MdhState.svPtr)
    Misc7State.i = cstringAt(Misc7State.tmp4).toLong() // vendor: i=atol(tmp)
    return Misc7State.i.toInt()
}

// --- mstring::Assign family (vendor 3679-3745) ---

/**
 * vendor 3741-3745 mstring::Assign(char* b) - the set-command
 * core: "set <data>=\"<b>\"" through the interpreter (u155
 * interpret seat).  The 3679-3681 string form delegates here.
 */
fun Mstring.Assign(b: String) {
    // vendor: char tmp[STR_MAX]="set "; strcpy(&tmp[4],data);
    //         strcat(tmp,"=\""); strcat(tmp,b); strcat(tmp,"\"");
    val cmd = "set " + value + "=\"" + b + "\""
    val f = interpret(cmd, MdhState.svPtr)

    if (f != 0) {
        MdhState.globalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }
}

/**
 * vendor 3683-3693 mstring::Assign(global b) - retrieve b's
 * value with Mglobal(RETRIEVE,...) (throwing when absent) and
 * assign it.
 */
fun Mstring.Assign(b: Global) {
    val gtmp = CharArray(STR_MAX)
    val f = mglobal(RETRIEVE, toCbuf(b.ref), gtmp, MdhState.svPtr)

    if (f == 0) {
        MdhState.globalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    Assign(cstringAt(gtmp))
}

/** vendor 3695-3699 mstring::Assign(mstring b). */
fun Mstring.Assign(b: Mstring) {
    Assign(b.value)
}

/** vendor 3701-3706 mstring::Assign(int b) - sprintf "%d". */
fun Mstring.Assign(b: Int) {
    Assign(b.toString())
}

/** vendor 3708-3713 mstring::Assign(double b) - sprintf "%f". */
fun Mstring.Assign(b: Double) {
    Assign(String.format(java.util.Locale.getDefault(), "%f", b)) // explicit default locale (u182 precedent)
}

/** vendor 3715-3720 mstring::Assign(long b) - sprintf "%ld". */
fun Mstring.Assign(b: Long) {
    Assign(b.toString())
}

// --- SQL_ family (vendor 3747-3877) ---

/** vendor 3747-3750 SQL_Disconnect(). */
fun SQL_Disconnect(): Int = interpret("sql/c", MdhState.svPtr)

/**
 * vendor 3752-3757 SQL_Connect(char* s); the 3759-3761 string
 * and 3763-3765 mstring forms delegate here (folds).
 */
fun SQL_Connect(s: String): Int = interpret("sql/d " + s, MdhState.svPtr)

/** vendor 3763-3765 SQL_Connect(mstring s). */
fun SQL_Connect(s: Mstring): Int = SQL_Connect(s.value)

/**
 * vendor 3767-3772 SQL_Format() - the "sql/c " command keeps
 * its TRAILING SPACE fossil (kept).
 */
fun SQL_Format(): Int = interpret("sql/c ", MdhState.svPtr)

/**
 * vendor 3774-3781 SQL_Format(char* s); the 3783-3785 mstring
 * and 3787-3789 string forms delegate here.
 */
fun SQL_Format(s: String): Int = interpret("sql/c " + s, MdhState.svPtr)

/** vendor 3783-3785 SQL_Format(mstring s). */
fun SQL_Format(s: Mstring): Int = SQL_Format(s.value)

/**
 * vendor 3791-3797 SQL_Command(char* s); the 3799-3801 mstring
 * and 3803-3805 string forms delegate here.
 */
fun SQL_Command(s: String): Int = interpret("sql " + s, MdhState.svPtr)

/** vendor 3799-3801 SQL_Command(mstring s). */
fun SQL_Command(s: Mstring): Int = SQL_Command(s.value)

/**
 * vendor 3807-3812 SQL_Table() - the no-arg "<dollar>ztable" form
 * (the char*(,int) seat is u34, SQLTable.kt, front-run
 * verification only; its string/mstring siblings are below).
 */
fun SQL_Table(): Mstring {
    var m = Mstring(DOLLAR + "ztable") // vendor: mstring m="<dollar>ztable";
    m = m.Eval()
    return m
}

/**
 * vendor 3844-3846 SQL_Table(mstring s, int c) - delegates to
 * the u34 (String, Int) seat (the 3841-3843 string sibling is
 * that seat's own fold; journaled).
 */
fun SQL_Table(s: Mstring, c: Int): Mstring = SQL_Table(s.value, c)

/** vendor 3848-3853 SQL_Message() - "<dollar>zsql" evaluation. */
fun SQL_Message(): Mstring {
    var m = Mstring(DOLLAR + "zsql")
    m = m.Eval()
    return m
}

/**
 * vendor 3855-3862 SQL_Open() - "<dollar>zsqlOpen"; the vendor
 * mstring==1 compare folds to the string compare with "1".
 */
fun SQL_Open(): Boolean {
    val m = Mstring(DOLLAR + "zsqlOpen").Eval()
    return m.value == "1"
}

/** vendor 3864-3870 SQL_Native() - "<dollar>znative" (same fold). */
fun SQL_Native(): Boolean {
    val m = Mstring(DOLLAR + "znative").Eval()
    return m.value == "1"
}

// --- operator& family (vendor 3880-3934) ---
// h017: C++ operator& has no Kotlin overload; each member is a
// one-line delegate to the matching Concat member (ampersand
// fold, unit-31 operator-fold precedent).

/** vendor 3880-3882 global::operator&(double b). */
infix fun Global.amp(b: Double): Mstring = Concat(b)

/** vendor 3884-3886 global::operator&(long b). */
infix fun Global.amp(b: Long): Mstring = Concat(b)

/** vendor 3888-3890 global::operator&(int b). */
infix fun Global.amp(b: Int): Mstring = Concat(b)

/** vendor 3892-3894 global::operator&(global b) - u33 Concat(Global). */
infix fun Global.amp(b: Global): Mstring = Concat(b)

/** vendor 3896-3898 global::operator&(char* b); string form folds. */
infix fun Global.amp(b: String): Mstring = Concat(b)

/** vendor 3904-3906 global::operator&(mstring b). */
infix fun Global.amp(b: Mstring): Mstring = Concat(b)

/** vendor 3908-3910 mstring::operator&(double b). */
infix fun Mstring.amp(b: Double): Mstring = Concat(b)

/** vendor 3912-3914 mstring::operator&(long b). */
infix fun Mstring.amp(b: Long): Mstring = Concat(b)

/** vendor 3916-3918 mstring::operator&(int b). */
infix fun Mstring.amp(b: Int): Mstring = Concat(b)

/** vendor 3920-3922 mstring::operator&(mstring b). */
infix fun Mstring.amp(b: Mstring): Mstring = Concat(b)

/** vendor 3924-3926 mstring::operator&(char* b); string form folds. */
infix fun Mstring.amp(b: String): Mstring = Concat(b)

/** vendor 3930-3932 mstring::operator&(global b). */
infix fun Mstring.amp(b: Global): Mstring = Concat(b)

// --- global::Concat family (vendor 3936-4076; the global-arg
// member 4049-4077 is B3 unit 91 - verification note only) ---

/**
 * Shared retrieve core for the numeric Concat members (vendor
 * 3936-4014 pattern): retrieve this global's value into gtmp
 * (throwing when absent), then append the formatted rslt and
 * return the concatenation.
 */
private fun Global.concatRetrieve(): CharArray {
    val gtmp = CharArray(STR_MAX)
    val f = mglobal(RETRIEVE, toCbuf(ref), gtmp, MdhState.svPtr)

    if (f == 0) {
        MdhState.globalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    return gtmp
}

/** vendor 3936-3952 global::Concat(double b) - rslt=b; strcat. */
fun Global.Concat(b: Double): Mstring {
    val gtmp = concatRetrieve()
    return Mstring(cstringAt(gtmp) + String.format(java.util.Locale.getDefault(), "%f", b)) // vendor: rslt=b (%f); explicit default locale (u182 precedent)
}

/** vendor 3954-3970 global::Concat(long b). */
fun Global.Concat(b: Long): Mstring {
    val gtmp = concatRetrieve()
    return Mstring(cstringAt(gtmp) + b.toString()) // vendor: sprintf "%ld"
}

/** vendor 3972-3988 global::Concat(int b) - sprintf "%d". */
fun Global.Concat(b: Int): Mstring {
    val gtmp = concatRetrieve()
    return Mstring(cstringAt(gtmp) + b.toString())
}

/**
 * vendor 3990-4006 global::Concat(char* b) and 4008-4024
 * Concat(string b) - both strcat the argument (string sibling
 * folds here, journaled).
 */
fun Global.Concat(b: String): Mstring {
    val gtmp = concatRetrieve()
    return Mstring(cstringAt(gtmp) + b)
}

/** vendor 4026-4040 global::Concat(mstring b). */
fun Global.Concat(b: Mstring): Mstring {
    val gtmp = concatRetrieve()
    return Mstring(cstringAt(gtmp) + b.value)
}

/**
 * vendor 4049-4077 global::Concat(global b) - B3 unit 91
 * (front-run: verification note only; the GlobalConcat.kt seat
 * binds unchanged, h004 one-owner policy).
 */

// --- mstring::Concat family (vendor 4078-4148) ---

/**
 * vendor 4078-4090 mstring::Concat(global b) - retrieve b's
 * value (throwing when absent) and prepend this mstring's data.
 */
fun Mstring.Concat(b: Global): Mstring {
    val gtmp = CharArray(STR_MAX)
    val f = mglobal(RETRIEVE, toCbuf(b.ref), gtmp, MdhState.svPtr)

    if (f == 0) {
        MdhState.globalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    return Mstring(value + cstringAt(gtmp)) // vendor: rslt=data+gtmp
}

/** vendor 4092-4098 mstring::Concat(double b) - rslt=b (%f). */
fun Mstring.Concat(b: Double): Mstring = Mstring(value + String.format(java.util.Locale.getDefault(), "%f", b))

/** vendor 4100-4106 mstring::Concat(long b) - sprintf "%ld". */
fun Mstring.Concat(b: Long): Mstring = Mstring(value + b.toString())

/** vendor 4108-4114 mstring::Concat(int b) - sprintf "%d". */
fun Mstring.Concat(b: Int): Mstring = Mstring(value + b.toString())

/**
 * vendor 4116-4122 mstring::Concat(char* b) and 4124-4130
 * Concat(string b) - both fold to data+b here.
 */
fun Mstring.Concat(b: String): Mstring = Mstring(value + b)

/** vendor 4132-4138 mstring::Concat(mstring b). */
fun Mstring.Concat(b: Mstring): Mstring = Mstring(value + b.value)
