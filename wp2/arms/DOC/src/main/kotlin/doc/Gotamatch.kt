package doc

// Unit 17 (B1 tranche 3): interp-bmgsubs.c::gotamatch (batch 1, core
// stratum; units.csv row 17: loc 19, score 22.9, rank 180). The bmg
// pattern-match line acceptor: bumps the success counter, copies the
// current line (through the newline) into the result buffer, and
// either prints it (no action callback) or applies the caller's
// predicate. The BUFSIZE 50000 and result buffer are file-scope
// entities of interp-bmgsubs.c; the counter nsuccess is the bmdata
// struct field bmg_nsuccess behind the nsuccess macro
// (interp-bmgsubs.c:91/99 macro) - one owner object per H-004/H-006
// (BmgStatic). The function pointer action() renders as a nullable
// higher-order function; the NULL return of the vendor (pointer
// domain) renders as the -1 sentinel with this note (no in-corpus
// caller constrains the encoding). printf maps to System.out per
// H-001. The vendor register-qualified parameters are
// indistinguishable in Kotlin. NL is the newline byte, private here
// per H-015 (file-local to the unit). Naming per H-017.

private const val NL: Char = '\n'
// interp-bmgsubs.c:75 define BUFSIZE
private const val BUFSIZE = 50000

// interp-bmgsubs.c file-scope bmdata.bmg_nsuccess behind the nsuccess
// macro (H-004 owner object; only the member this unit touches).
object BmgStatic {
    var nsuccess: Int = 0
}

fun gotamatch(s: CharArray, sIn: Int, pos: Int,
        action: ((CharArray, Int) -> Int)?): Int {
    val result = CharArray(BUFSIZE)
    var r = 0
    var i = sIn

    BmgStatic.nsuccess++
    do {
        result[r] = s[i]
        r++
        val c = s[i]
        i++
    } while (c != NL)
    result[r] = NUL

    if (action == null) {
        System.out.print(CstringOps.cstring(result))
        return i
    } else if (action(result, pos) != 0) {
        return i
    } else {
        return -1
    }
}
