package doc

// Unit 277 (B9 tranche 3): interp-rpm.cpp::perl_pm (batch 9,
// complex stratum; units.csv row 257: loc 51, score 21.6, rank
// 355; physLOC 51 physically verified this session) - the
// POSIX regcomp/regexec whole-string pattern match under the
// rpm/perlmatch/perlcpp drivers. Consult H-015 hit-adapt
// (2026-11-22T05:39:00Z): Stubs.perlPm FLIPS to this render;
// the batch-5 unit-152 InterpRpmMisc.kt driver call sites bind
// unchanged.
//
// H-018 JVM-DOMAIN ADJUDICATION: the vendor compiles the
// double-anchored pattern ("^" + pattern + "\u0024") with
// regcomp(REG_NOSUB | REG_EXTENDED) and matches with
// regexec(...,pmatch,0). The JVM carrier is java.util.regex
// (kotlin Regex over it): REG_NOSUB folds onto
// Regex.containsMatchIn (no capture consult, per the vendor
// flag), REG_EXTENDED onto the default ERE-compatible java
// syntax. regfree folds onto GC (no explicit JVM carrier).
// The regcomp error ladder (REG_BADBR/REG_BADPAT/REG_BADRPT/
// REG_EBRACE/REG_EBRACK/REG_ECOLLATE/REG_ECTYPE/REG_EEND/
// REG_EESCAPE/REG_EPAREN/REG_ERANGE/REG_ESIZE/REG_ESPACE/
// REG_ESUBREG - the POSIX regcomp error codes, kept verbatim
// as the vendor banner comment block) folds to the single
// java.util.regex.PatternSyntaxException catch
// (kotlin.text.RegexSyntaxException does NOT resolve in the
// kotlinc 2.1.20 gate env - H-018 note): the catch prints the
// common banner ("\n*** regcomp() parse error on regex\n"),
// then sigint(100) and exitProcess(EXIT_FAILURE), exactly the
// vendor fatal-arm sequence. The regexec result mapping kept
// verbatim: i == 1 (REG_NOMATCH) returns 0, otherwise 1 - the
// match success returns 1 and the no-match returns 0.
// Naming per H-017 (perl_pm kept; the driver-facing seat name
// Stubs.perlPm is unchanged).

import java.util.regex.PatternSyntaxException

fun perl_pm(
    string_: String,
    pattern: String,
    allow_partial: Int,
    svPtr: Msv
): Int {
    // int i; regex_t regex; regmatch_t pmatch[1]; - the regcomp
    // ladder locals (H-018 fold, header ledger)

    // char reg[512]; strcpy(reg,"^"); strcat(reg,pattern);
    // strcat(reg,"\u0024"); - the double anchoring kept verbatim
    val reg = "^" + pattern + "\u0024"

    val str = string_

    val i = try {
        // if (i = regcomp(&regex, reg, REG_NOSUB|REG_EXTENDED))
        Regex(reg)
        0
    } catch (e: PatternSyntaxException) {
        println("\n*** regcomp() parse error on regex\n")
        // REG_BADBR Invalid use of back reference operator.
        // REG_BADPAT Invalid use of pattern operators such as group or list.
        // REG_BADRPT Invalid use of repetition operators such as using '*' as the first character.
        // REG_EBRACE Un-matched brace interval operators.
        // REG_EBRACK Un-matched bracket list operators.
        // REG_ECOLLATE Invalid collating element.
        // REG_ECTYPE Unknown character class name.
        // REG_EEND Nonspecific error. This is not defined by POSIX.
        // REG_EESCAPE Trailing backslash.
        // REG_EPAREN Un-matched parenthesis group operators.
        // REG_ERANGE Invalid use of the range operator; for example, the ending point of the range occurs prior to the starting point.
        // REG_ESIZE Compiled regular expression requires a pattern buffer larger than 64 kB. This is not defined by POSIX.
        // REG_ESPACE The regex routines ran out of memory.
        // REG_ESUBREG Invalid back reference to a subexpression.
        sigint(100)
        // exit(EXIT_FAILURE) - EXIT_FAILURE equals 1 (the
        // LibMisc1/family precedent)
        kotlin.system.exitProcess(1)
        -1
    }

    // i = regexec(&regex, str, 1, pmatch, 0); (REG_NOSUB consult
    // fold); regfree(&regex); - the GC fold (header ledger)
    val rex = Regex(reg)
    val matched = rex.containsMatchIn(str)

    // if (i == 1) return 0; else return 1;
    if (matched) return 1 else return 0
}
