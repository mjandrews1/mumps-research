package both

// Unit 202 render: libmpscpp.cpp::global::MakeGlobal (roster
// 202, batch 7; units.csv rank 178 region; vendor body
// :6967-7000). The coded-reference builder: assembles the
// name\x01sub1\x01sub2\x01... form into ref and the canonical
// name("s1","s2") form into Sref, over the comma-tallied key
// list and the flg-dispatched varargs (flg==1: %ld longs;
// else: strings).
//
// Consults (rule base wp2/arms/BOTH/rules_v0.pl):
//   h014 out_of_scope_call - reused: countKeyZb (the misc-6
//     count seat) called direct; strcatx (the StrmanipMisc
//     seat) called direct - its CharArray form rides the buf
//     scratch; the Sim1.kt makeGlobalZb TODO stub RELEASED at
//     this render (release note there; the two face call
//     sites now call MakeGlobal direct).
//   h015 grammar-collision - reused: ASCII only, no backticks,
//     no raw comment dollars, trailing newline.
//   h017 naming - reused: MakeGlobal keeps vendor name/case;
//     the released stub carried the Zb qualifier only for the
//     mechanical swap.
//   h019 class_member_carrier - hit: Sref/Sname (globalClass.h
//     :538-540 instance members) and ref fold onto the
//     immutable GlobalRef carrier - the returned object CARRIES
//     the built ref (the C in-place strcpy(ref,buf) on *this
//     folds to construction; the misc-6 Sref-onto-ref fold
//     precedent). ArgCount (:534) is WRITE-ONLY in the seated
//     corpus (the sole libmpscpp.cpp write is :6976; the
//     reads live in generated programs' _ArgCount, a
//     different symbol) - dead-store fold, journaled; the
//     layout owner roster 259 may carry it later.
//
// Quirks kept (ledgered):
//   - count("")==1: the zero-subscript operator()() call shape
//     (MakeGlobal(0,myname,"","")) runs ONE iteration with the
//     empty-string vararg - that is the source of the trailing
//     0x01 ENDCAP PAIR the Dice/Sim1 kernels strip. A missing
//     vararg (fewer subs than count(key)) is C va_arg UB;
//     QUANTIZED to the empty-string read (journaled).
//   - strcmp(key,"\"\"")==0 forces flg=0 (kept exact).
//   - strcatx appends the 0x01 AFTER each component: ref ends
//     with a trailing 0x01 (kept; the kernels' pair-strip seats
//     depend on it).
//   - Sref's close edit replaces the final "," with ")" - the
//     Sref build is folded (the ref carrier divergence); the
//     canonical string is still built and discarded, matching
//     the vendor's member writes with no arm reader (the
//     Sref-onto-ref fold, journaled).
//   - the commented-out strcat(Sref,...) vendor lines are not
//     seats.

// global global::MakeGlobal(int flg, char *gblnme, char *key,
//                           ...) - libmpscpp.cpp:6967
// varargs fold: subs carries Long for the flg==1 arms and
// String for the flg==0 arms (the va_arg dispatch)
internal fun MakeGlobal(flg: Int, gblnme: String, key: String, subs: List<Any>): GlobalRef {
    var flg = flg
    // Sname=gblnme; Sref=gblnme+"(" (folded - the carrier note)
    val sref = StringBuilder(gblnme)
    sref.append('(')

    val j = countKeyZb(key)
    // ArgCount = j - dead store (no seated reader; journaled)

    val buf = StringBuilder()
    buf.append(gblnme) // strcpy(buf,""); strcatx(buf,gblnme)
    buf.append('\u0001')

    if (key == "\"\"") flg = 0 // strcmp(key,"\"\"")==0

    for (i in 0 until j) {
        if (flg == 1) {
            val ix = (subs.getOrElse(i) { "" } as? Long) ?: 0L // va_arg UB quantized
            val tmp = ix.toString() // sprintf(tmp,"%ld",ix)
            buf.append(tmp)
            buf.append('\u0001') // strcatx(buf,tmp)
            sref.append(tmp)
            sref.append(',')
        } else {
            val x = subs.getOrElse(i) { "" } as? String ?: "" // va_arg UB quantized
            buf.append(x)
            buf.append('\u0001') // strcatx(buf,x)
            sref.append('"')
            sref.append(x)
            sref.append('"')
            sref.append(',')
        }
    }

    // va_end; strcpy(ref,buf) - folds to construction (the val
    // carrier); Sref close: Sref[len-1]=')'
    if (sref.isNotEmpty()) sref.setCharAt(sref.length - 1, ')')

    return GlobalRef(buf.toString())
}
