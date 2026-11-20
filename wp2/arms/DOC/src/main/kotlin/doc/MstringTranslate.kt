package doc

// Unit 191 (B7 tranche 1): libmpscpp.cpp::mstring::Translate
// (batch 7, simple stratum; LOC 15, score 7.7, rank 57) - the
// TWO-ARGUMENT mstring member (vendor libmpscpp.cpp:4226,
// physically verified this session; nonblank 15 matches the
// roster row; the one-arg sibling at :4246 is a later-batch
// row, and the global member one-arg form is the landed
// unit-131 GlobalTranslate render). Body: the mstring Concat
// command-build chain folds onto the CharArray catAt seat
// adapter (H-014 hit-reuse, the unit-131 fold): command text
// dollar-sign + "translate", commerce-e open, the invocant's
// own data, commerce-d-zero, a1, commerce-d-zero again, a2,
// commerce-f close. The dollar byte rides the plain Char
// literal per H-015 (no raw dollar in source: the DOLLAR_MT
// const carrier per the unit-131/180 precedent). The command
// goes through the fcn H-014 stub into v1d at offset 1 and the
// result reads bd at offset 1 (the unit-131/72 offset-1
// convention). Naming per H-017 (Translate -> translate).

private const val DOLLAR_MT = '$'

fun Mstring.translate(a1: Mstring, a2: Mstring): Mstring {
    val open = Char(0x00ce)
    val comma = Char(0x00d0)
    val close = Char(0x00cf)
    val tmp = CharArray(STR_MAX)

    // m = DOLLAR_91 + "translate"; m = m.Concat(open); m = m.Concat(data);
    // m = m.Concat(comma); m = m.Concat(a1); m = m.Concat(comma);
    // m = m.Concat(a2); m = m.Concat(close);
    CstringOps.strcpyS(tmp, DOLLAR_MT + "translate")
    CstringOps.catAt(tmp, 0, open)
    CstringOps.catAt(tmp, 0, str) // invoking mstring member data (the
    // vendor m.data field maps to the Kotlin str carrier, Carriers.kt)
    CstringOps.catAt(tmp, 0, comma)
    CstringOps.catAt(tmp, 0, a1.c_str())
    CstringOps.catAt(tmp, 0, comma)
    CstringOps.catAt(tmp, 0, a2.c_str())
    CstringOps.catAt(tmp, 0, close)

    // strcpy((char *) &svPtr->v1d[1], m.data.c_str());
    CstringOps.copyAt(Globals.svPtr.v1d, 1, tmp)
    Stubs.fcn(Globals.svPtr)
    val m = Mstring(CstringOps.cstringAt(Globals.svPtr.bd, 1))
    return m
}
