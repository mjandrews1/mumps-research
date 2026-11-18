package mumps.interp

/**
 * PRO-arm translation of interp-rpm.cpp::perl_pm (roster row 257,
 * batch 9; units.csv: loc 51, comment_ratio 0.176,
 * pointer_marks 2, control_marks 17, score 46.1, rank 329).
 * Translated independently from the pristine vendor source
 * (mumps-14-may-2026.tgz, sha256 621deb25...ffdbe), reference
 * source lines 158-232 (prototype at vendor :106; NITEMS macro
 * and the regex.h include are the residue's, not this body's).
 * RESOLVES the InterpStubs perlPm stub (the rpm/perlmatch/
 * perlcpp call sites in InterpRpmMisc.kt bind unchanged on the
 * offset-carrying signature).
 *
 * perlPm(string, stringOff, pattern, patternOff, allowPartial,
 * svPtr): the regex engine entry - builds the anchored
 * "^<pattern>$" in reg[512], regcomp()s it POSIX-ERE NOSUB, and
 * returns 0 on NO match, 1 on match (the C regexec code
 * inverted).
 *
 * Rules applied (wp2/arms/PRO/rules_v0.pl):
 *  - h009 stdlib_function_semantics: regcomp/regexec/regfree
 *    (POSIX ERE, REG_NOSUB) fold onto java.util.regex.Pattern
 *    (JVM has no POSIX API; Java regex is an ERE superset -
 *    accepted-class divergence journaled, value-identical for
 *    the corpus patterns; the regfree is a GC no-op analog).
 *  - h012 unsigned_byte_domain: reg[512] is a NUL-terminated
 *    byte domain; str binds the cstringAt window of string.
 *  - h013 dead_store        : the i=regcomp assignment IS live
 *    (the error cascade tests it); the regmatch_t pmatch[1]'s
 *    buffer is dead under REG_NOSUB - kept as a comment, not
 *    rendered; allow_partial is NEVER read by the vendor body -
 *    kept in the signature, noted.
 *  - h014 out_of_scope_call : sigint (Sigint.kt:136 real
 *    seat); the printf error cascade rides System.out.print -
 *    the vendor's own bytes, verbatim; exit(EXIT_FAILURE) ->
 *    exitProcess(1) (Fcn.kt:305 precedent).
 *  - h015 grammar_collision: new top-level seat named perlPm
 *    (the vendor underscore name folded, rpm-history precedent
 *    InterpRpmMisc.kt); perlPm replaces the removed stub
 *    one-for-one.
 *  - h017 naming           : perlPm keeps the fold surname; the
 *    parameters keep the stub's offset-shape names.
 *
 * Vendor notes (translated faithfully, not "fixed"):
 *  - The regcomp error cascade prints per POSIX errno code -
 *    the JVM raises PatternSyntaxException instead; the banner
 *    "*** regcomp() parse error on regex" is printed verbatim
 *    and the per-code texts fold onto the exception message
 *    print (an accepted carrier divergence, journaled).
 *  - The return inversion: "if (i==1) return 0; else return 1"
 *    (no match -> 0, match or error != 1 -> 1) kept.
 *  - The pattern is anchored BOTH ends with literal "^"/"$"
 *    bytes outside the engine anchors (kept as string bytes).
 */
fun perlPm(
    string: CharArray, stringOff: Int,
    pattern: CharArray, patternOff: Int,
    allowPartial: Int, svPtr: Msv,
): Int {
    val reg = CharArray(512)

    // vendor: strcpy(reg,"^"); strcat(reg,pattern); strcat(reg,"$")
    val sb = StringBuilder("^")
    var p = patternOff
    while (pattern[p] != NUL) {
        sb.append(pattern[p])
        p++
    }
    sb.append("$")
    var w = 0
    while (w < sb.length && w < 511) {
        reg[w] = sb[w]
        w++
    }
    reg[w] = NUL

    val str = cstringAt(string, stringOff)

    // vendor: if (i = regcomp(&regex, reg, REG_NOSUB|REG_EXTENDED))
    val compiled = try {
        java.util.regex.Pattern.compile(String(reg, 0, w))
    } catch (e: java.util.regex.PatternSyntaxException) {
        // vendor: printf("\n*** regcomp() parse error on regex\n");
        print("\n*** regcomp() parse error on regex\n")
        // the vendor's per-errno texts (REG_BADBR .. REG_ESUBREG)
        // fold onto the exception description print - the carrier
        // divergence is journaled above (h014 note)
        print("\t" + e.message + "\n")
        sigint(100)
        kotlin.system.exitProcess(1) // exit(EXIT_FAILURE)
    }

    // vendor: i = regexec(&regex, str, 1, pmatch, 0); regfree(&regex);
    // (pmatch is dead under REG_NOSUB, h013; regfree -> GC)
    val i = if (compiled.matcher(str).find()) 0 else 1

    // vendor: if (i == 1) return 0; else return 1;
    return if (i == 1) 0 else 1
}
