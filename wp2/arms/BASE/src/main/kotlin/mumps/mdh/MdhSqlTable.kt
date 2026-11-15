package mumps.mdh

import mumps.interp.GlobalExceptionCode
import mumps.interp.MumpsGlobalException
import mumps.interp.interpret

/**
 * Kotlin translation of libmpscpp.cpp::SQL_Table (Study Subset unit 34,
 * Batch 2, arm BASE; translated 2026-11-15).
 *
 * C: mstring SQL_Table(char * s, int c) (libmpscpp.cpp:3828-3848) —
 * sets the SQL table name ($table) and size ($ztabzize) in the
 * interpreter state via Interpret().
 *
 * OVERLOAD SET COLLAPSE: the C++ dispatch set has four entries —
 * SQL_Table() (evaluates "$ztable" via mstring::Eval, libmpscpp.cpp:
 * 3822-3827), SQL_Table(char*,int) (the body translated here), and two
 * forwarders SQL_Table(string,int) / SQL_Table(mstring,int)
 * (3850-3856) that only adapt C++ string types. Kotlin has one String
 * type, so the forwarders collapse into this single entry point; the
 * zero-arg overload requires mstring::Eval, which is outside batch
 * scope, so it is not translated.
 *
 * Buffers: cmd[STR_MAX] built by strcpy/strcat/sprintf -> Kotlin string
 * templates. QUIRK PRESERVED: the C writes "$ztabzize" (apparent typo
 * for $ztablesize) — carried verbatim per the faithful-bugs policy.
 * The MUMPS dollars in the literals are escaped (\$) per heuristic 89.
 */
fun sqlTable(s: String, c: Int): String {
    // C: mstring tab; tab=s;
    val tab = s

    if (c > 10) { // C: if (c>10) { cout << "Table size too large.\n"; ... }
        println("Table size too large.")
        GlobalExceptionCode = GLOBAL_NOT_FOUND
        throw MumpsGlobalException()
    }

    // C: strcpy(cmd,"set $table=\""); strcat(cmd,s); strcat(cmd,"\"");
    val cmd = "set \$table=\"$s\""

    // C: Interpret(cmd,svPtr);
    interpret(cmd, MdhRuntime.svPtr)

    // C: sprintf(cmd,"set $ztabzize=%d",c); Interpret(cmd,svPtr);
    val cmd2 = "set \$ztabzize=$c"
    interpret(cmd2, MdhRuntime.svPtr)

    return tab
}
