package both

// Unit 262 (BOTH B9, core): libmpscpp.cpp::mstring::FixGblRef
// (25 LOC). Batch_plan ordinal 262; units.csv row 262 - the
// mstringFixGblRefZb TODO stub raised at the B8 unit-217 close
// for this owner releases here (the h014 delegate-when-owner-
// lands flip; the stub face in LibmpscppMisc7.kt is removed).
// The special-case global-reference fixer: a reference of the
// form ^gbl() converts to ^gbl(""); anything without the
// caret-alpha-open-empty-close shape returns unchanged (or
// empty when the data is empty/non-global).
//
// BOTH consults (find_rule before render, events logged):
//   h007 char_buffer - reused: char tmp[STR_MAX] is the working
//     copy of data (CharArray, NUL-terminated copy-in); the
//     in-place rewrite writes the quote pair and terminator at
//     the vendor indices; the mstring returns fold to String
//     (the class_member_carrier miss precedent).
//   h012 unsigned_byte_domain - reused: the isalpha test rides
//     the C byte-domain seat (isalphaCZb family shape,
//     file-private in the arm - re-seated locally).
//   h014 out_of_scope_call - reused: strlen/strcpy/isalpha
//     ride the arm seats (cStringOf/copy-in fold).
//   h017 naming - reused: the member face rides the String
//     receiver as mstringFixGblRef (the mstringTranslate1
//     precedent); b/tmp/i/j/data keep vendor names.
//   Dafny: EXEMPT - the rewrite is a fixed 4-byte suffix
//     splice over a byte buffer (emitter/procedural family
//     precedent, the DAF B6 T3 ruling); no new spec.
//
// Vendor quirks kept (ledgered):
//   - the loop exits with i at the first NON-ALPHA byte OR at
//     j (all-alpha tail); the tmp[i] != '(' test then reads
//     the NUL at tmp[j] - kept verbatim (the CharArray holds
//     the terminator at j, so the read is in-bounds and
//     behaves identically).
//   - the rewrite writes tmp[i+1..i+4] with no bounds check
//     (the STR_MAX capacity contract); kept by the same-size
//     CharArray scratch.
//   - return (*this) on the tmp[i+1] != ')' arm returns the
//     ORIGINAL data (not the tmp copy) - kept (the receiver
//     is returned, tmp's earlier mutations are unobservable
//     at that point since none happened on this path).

internal fun String.mstringFixGblRef(): String {
    // mstring b = ""; strcpy(tmp, data.c_str());
    val b = ""
    val tmp = CharArray(STR_MAX)
    for (idx in this.indices) tmp[idx] = this[idx]
    tmp[this.length] = 0.toChar()

    // if ((j = strlen(tmp)) == 0) return b;
    val j = this.length
    if (j == 0) return b

    // if (tmp[0] != '^') return b;
    if (tmp[0] != '^') return b

    // for (i = 1; i < j; i++) { if (isalpha(tmp[i])) continue; break; }
    var i = 1
    while (i < j) {
        if (isalphaFixZb(tmp[i])) {
            i++
            continue
        }
        break
    }

    // if (tmp[i] != '(') return b;   (tmp[j] is the NUL when i == j)
    if (tmp[i] != '(') return b

    // if (tmp[i+1] != ')') return (*this);
    if (tmp[i + 1] != ')') return this

    // tmp[i+1] = '\"'; tmp[i+2] = '\"'; tmp[i+3] = ')'; tmp[i+4] = 0;
    tmp[i + 1] = '\"'
    tmp[i + 2] = '\"'
    tmp[i + 3] = ')'
    tmp[i + 4] = 0.toChar()

    // b = tmp; return b;
    return cStringOf(tmp)
}

// C isalpha over the byte domain (the isalphaCZb family shape).
private fun isalphaFixZb(c: Char): Boolean = c in 'A'..'Z' || c in 'a'..'z'
