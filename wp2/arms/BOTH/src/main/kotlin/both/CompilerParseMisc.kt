package both

// Unit 219 render: compiler-parse.c::<misc> (roster 219, batch 8,
// 232 LOC dump; the TU head). The compiler-parse.c file-scope
// residue: the opcode/define block, the TU prototypes, and the
// five small helpers (ConCheck, strcasecmp, strncasecmp, cvar,
// Extract1). memcheck is NOT re-rendered - the ExtractCompiler.kt
// seat (record 190 family) carries the identical body, verified
// line-for-line this session (the verification-fold precedent).
//
// Consults (rule base wp2/arms/BOTH/rules_v0.pl):
//   h004 include_only - reused: the #define block is a value
//     ledger; the seats below carry the vendor values and line
//     pins; the pre-existing package consts (RETRIEVE/STORE/
//     CLOSE/NEXT/KILL/JOB, BothCRuntime/GlobalData/ServerMain/
//     InterpHMisc pins) are NOT re-declared (h015 collision).
//   h013 dead_store - hit: Extract1's static tmp2[64] is never
//     written (the vendor pairs tmp1/tmp2 and uses only tmp1);
//     the seat drops the unused buffer, ledgered. The TU globals
//     unsigned char *SSS,*TTT: SSS is the ConCheck walk scratch
//     (local), TTT has no use in the pristine tree (ledgered,
//     not seated).
//   h014 out_of_scope_call - reused: cvar reads the
//     CompilerGlobalsExt cvars/cvarx seats; the externs
//     MaxTmp/iiitmp/ErrChar are the same seats.
//   h015 grammar-collision - reused: ASCII only, no backticks,
//     no raw comment dollars, trailing newline.
//   h017 naming - reused: ConCheck/strcasecmp/strncasecmp/cvar/
//     Extract1 keep vendor names (no Kotlin collisions; the
//     POSIX twins are vendor-local redefinitions).
//
// Quirks kept (ledgered):
//   - the define block re-declares RETRIEVE/STORE/NEXT/XNEXT/
//     CLOSE with the defines.h values (the compiler TU re-uses
//     its own macro copy; the pin map rides the ledger below).
//   - strcasecmp returns 0 when *a==0 even if *b!=0 (the vendor
//     loop exits on the NUL without comparing the tail - kept
//     exact).
//   - strncasecmp decrements i BEFORE the pointer walk (the
//     vendor counts the first compare - kept).
//   - Extract1's memcpy can overflow tmp1[64] in C; the seat
//     clamps at 64 (the UB quantization family).
//   - the QUOTE define is '\'' (char); CodedOpen/CodedComma/
//     CodedClose/CodedColon are the 206-209 family already
//     seated as ZB_OPENC pins (BifsZfcn).

// The define ledger (compiler-parse.c TU head, values verbatim;
// the sym_ opcodes feed the record 231/232/233 renders):
internal object CompilerParseDefs {
    const val SymStore = 0
    const val SymRetrieve = 1
    const val SymDeleteExplicit = 2
    const val SymNext = 3
    const val SymOrder = 33
    const val SymDeleteAll = 4
    const val SymDeleteAllExcept = 5
    const val SymData = 6
    const val SymName = 10
    const val KILL1 = 30 // KILL op in the sym_ family
    const val NEXTX = 20
    const val PREVIOUS = 9
    // command numbers (the numeric block: JOB 0 ... BADCMD 21)
    const val SET = 1
    const val GOTO = 2
    const val WRITE = 3
    const val IF = 4
    const val ZCMDS = 6
    const val READ = 7
    const val QUIT = 8
    const val XECUTE = 9
    const val TAB = 9
    const val FOR = 10
    const val HALT = 11
    const val ELSE = 12
    const val ORDERNEXT = 12
    const val ORDERPREV = 13
    const val DO = 13
    const val COMMENT = 18
    const val BREAK = 19
    const val BADCMD = 21
    const val KILL2 = 20
    const val FCN = 20
    const val LBL = 21
    const val QUOTE = '\''
    // the operator block (OPEN 0 ... EXPONEN 29)
    const val OPEN = 0
    const val DIVIDE = 1
    const val MULTIPLY = 2
    const val MINUS = 3
    const val PLUS = 4
    const val OPERAND = 5
    const val OPENC = 6
    const val CONCAT = 7
    const val EQUALS = 8
    const val GREATER = 9
    const val EMPTY = 10
    const val LESSTHAN = 11
    const val NOT = 12
    const val INTDIVIDE = 13
    const val MODULO = 14
    const val CONTAINS = 15
    const val FOLLOWS = 16
    const val PATTERN = 17
    const val NOTEQ = 18
    const val NOTGREATER = 19
    const val NOTLESS = 20
    const val NOTCONTAINS = 21
    const val NOTFOLLOWS = 22
    const val NOTPATTERN = 23
    const val AND = 24
    const val OR = 25
    const val NOTAND = 26
    const val NOTOR = 27
    const val INDIRECT = 28
    const val EXPONEN = 29
    const val PREDICATEVAR = '~'
    const val CodedOpen = 206
    const val CodedClose = 207
    const val CodedComma = 208
    const val CodedColon = 209
    const val GBLDEFERED = 100
    const val LCLDEFERED = 101
    const val NAME = 102
    const val GET = 103
    const val ORDER = 104
}

// Prototype owner ledger (the TU head block; NO stubs raised -
// the residue owners land with their bodies): ExpandQS, patrn,
// patrn1 (compiler-parse.c b9/b10 units), xindex (compiler-sys1
// roster 107), errmod_ (b2), add/sub/mult/divx (compiler-arith
// family), numcomp (interp-arith b6), cannon (record 222, this
// batch), MyFunc, sym_ (interp-sym), keyfix (the keyfixC seat),
// fixstr, Extract/Extract2/Extract2a (records 230 family),
// ExpandOrder/ExpandData/ExpandQlength/ExpandNext/ExpandGet/
// ExpandName/ExpandText/ExpandQsub (records 234 family).

// int ConCheck(char * b) - the quoted-integer probe: a pure
// digit string wrapped in quotes shifts left one and drops its
// trailing quote in place (the caller's buffer mutated).
internal fun ConCheck(b: CharArray): Int {
    val s = cStringOf(b)
    if (s.isEmpty() || b[0] != '"') return 0
    if (b[s.length - 1] != '"') return 0
    var i = 1
    while (b[i] != '"') {
        if (!Character.isDigit(b[i])) return 0
        i++
    }
    // for (SSS=b; *SSS !=0; SSS++) *SSS = *(SSS+1); - the
    // whole-string left shift including the NUL.
    var sss = 0
    while (b[sss] != '\u0000') {
        b[sss] = b[sss + 1]
        sss++
    }
    // b[strlen(b)-1]='\0';
    val len = sss // the post-shift strlen
    b[len - 1] = '\u0000'
    return 1
}

// int strcasecmp (char *a, char *b) - the vendor-local POSIX
// twin (returns 0 on equal-or-a-empty, 1 otherwise).
internal fun strcasecmp(a: String, b: String): Int {
    var i = 0
    while (i < a.length && i < b.length &&
        a[i].uppercaseChar() == b[i].uppercaseChar() && a[i] != '\u0000') {
        i++
    }
    if (i >= a.length || a[i] == '\u0000') return 0
    return 1
}

// int strncasecmp (char *a, char *b, int i) - the bounded twin.
internal fun strncasecmp(a: String, b: String, n: Int): Int {
    var i = n
    var pa = 0
    var pb = 0
    while (pa < a.length && pb < b.length &&
        a[pa].uppercaseChar() == b[pb].uppercaseChar() &&
        a[pa] != '\u0000' && i > 0) {
        i--
        pa++
        pb++
    }
    if (i == 0 || pa >= a.length || a[pa] == '\u0000') return 0
    return 1
}

// int cvar(char * str) - the declared-variables probe.
internal fun cvar(str: String): Int {
    val g = CompilerGlobalsExt
    for (i in 0 until g.cvarx) {
        if (str == g.cvars[i]!!.name) return 1
    }
    return 0
}

// char * Extract1 (char * str) - the #-label unwrap: strips the
// "#nn " envelope, keeping the inner name (tmp1[64]; the static
// tmp2[64] twin is dead - h013, ledgered above).
internal fun Extract1(str: String): String {
    if (str.isEmpty() || str[0] != '#') return str
    var i = 3
    while (i < str.length && str[i] != '#' && str[i] != '\u0000') i++
    // memcpy(tmp1,&str[3],i-3); tmp1[i-3]='\0'; - the 64-byte
    // clamp quantizes the C overflow.
    return str.substring(3, minOf(i, 67))
}
