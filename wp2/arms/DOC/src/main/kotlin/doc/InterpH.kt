package doc

// Unit 169 (B6 tranche 2): include/mumpsc/interp.h::<misc>
// (batch 6, core stratum; LOC 69, score 13.3) - the interpreter
// runtime header: the command-char defines the interpreter's
// dispatch switch reads, the operator precedence codes, and the
// prototype roster. Rendered as the header-owner ledger with the
// public const seats (the memsize.h banner precedent: the header
// has no function bodies). All derived from the pristine vendor
// source this session (sha256 621deb...ffdbe).
//
// H-004 single-owner notes: TAB is NOT redeclared here - the
// unit-48 Getstr2 render already seated const val TAB (the
// interp.h value) and this ledger refers to it. The unit-93
// Parse_ render seated a file-private POPEN_93..PREDICATEVAR_93
// subset as private consts with the same interp.h anchors - the
// first-draft-before-the-owner precedent; the public seats below
// are the header-owner's, and the unit-93 privates stay dormant
// (file-private, no collision). The NOT_NOT define duplicates
// the NOTEQ code 18 (vendor quirk, both seated verbatim).
//
// Prototype roster (owner ledger, no stubs raised per the house
// convention of not stubbing not-yet-called functions):
// Interpret (roster row 185, the deferred big three, batch-6
// complex), parse_ - REAL as of unit 93 (Parse_.kt), prnterr
// (units.csv row, later batch), BuildGlobal/BuildLocal (the
// Stubs seats raised by unit 124, batch-8 owner), plus the
// mumpsc/symbasic.h include (roster note only). The _INTERP_
// guard and the TSTACK 10 define ride as consts/notes below.
// Naming per H-017 (interp.h -> InterpH; macro lexemes kept).

// interp.h:32 define TSTACK 10
const val TSTACK = 10

// interp.h:35-59 command chars (the dispatch switch reads)
const val BREAK: Char = 'b'
const val CMMNT: Char = ';'
const val DATAKILL: Char = 'k'
const val DO: Char = 'd'
const val ELSE: Char = 'e'
const val FCN: Char = '$'
const val FILECLOSE: Char = 'c'
const val FILELOCK: Char = 'l'
const val FILEOPEN: Char = 'o'
const val FOR: Char = 'f'
const val GOTO: Char = 'g'
const val HALT: Char = 'h'
const val HTML: Char = '<'
const val IF: Char = 'i'
const val JOB: Char = 'j'
const val QUIT: Char = 'q'
const val READ: Char = 'r'
const val SET: Char = 's'
const val MERGE: Char = 'm'
// interp.h:53 define TAB '\t' - NOT redeclared: the unit-48
// Getstr2 render owns the TAB const (H-004 single owner; the
// value is this header's).
const val USE: Char = 'u'
const val WRITE: Char = 'w'
const val XECUTE: Char = 'x'
const val ZCMDS: Char = 'z'
const val NEW: Char = 'n'

// interp.h:62 QUOTE
const val QUOTE: Char = '\''

// interp.h:64-94 operator/precedence codes
const val POPEN = 0
const val DIVIDE = 1
const val MULTIPLY = 2
const val MINUS = 3
const val PLUS = 4
const val OPERAND = 5
const val OPENC = 6
const val CONCAT = 7
const val EQUALS = 8
const val GREATER = 9
const val PEMPTY = 10
const val LESSTHAN = 11
const val NOT = 12
const val INTDIVIDE = 13
const val MODULO = 14
const val CONTAINS = 15
const val FOLLOWS = 16
const val PATTERN = 17
const val NOTEQ = 18
const val NOT_NOT = 18 // duplicates NOTEQ (vendor quirk, kept)
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

// interp.h:95 define PREDICATEVAR '~'
const val PREDICATEVAR: Char = '~'
