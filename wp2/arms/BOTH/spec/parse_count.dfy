// parse_count.dfy - BOTH-arm spec of batch-5 tranche-2 unit 143
// (interp-rpm.cpp::parse_count): the repeat-count production's
// decidable core. The vendor state advance (accept_it) is a stubbed
// seat in the Kotlin render, so the spec contracts the pure grammar
// mapping instead: given the leading token kind, the lower-bound
// text, whether a DOT follows it, whether an upper-bound INT follows
// the DOT, and that upper text, the production is fully determined.
// Written after the Kotlin render.

datatype Kind = INTK | DOTK | OTHERK

function Braced(s: seq<char>): seq<char>
{
    ['{'] + s + ['}']
}

// INT leading token: opens with "{", closes with "}"; a following DOT
// switches to the bounded ranges "{x,y}" (upper optional per the
// vendor: a missing INT after the DOT yields just the DOT arm with
// the lower bound and the comma... vendor: DOT without INT yields
// "{lower," + "}"). Other follow tokens concatenate the lower bound.
function ParseCountINT(lower: seq<char>, nextIsDot: bool, hasUpper: bool, upper: seq<char>): seq<char>
{
    if !nextIsDot then
        Braced(lower)
    else if hasUpper then
        ['{'] + lower + [','] + upper + ['}']
    else
        ['{'] + lower + [','] + ['}']
}

// Non-INT leading token (the vendor comment: must have been a DOT):
// an INT after it yields "{0,<n>}", anything else "*".
function ParseCountOther(hasInt: bool, upper: seq<char>): seq<char>
{
    if hasInt then ['{'] + ['0'] + [','] + upper + ['}'] else ['*']
}

// The four vendor productions, shape-checked:
method ProductionsExact(lower: seq<char>, upper: seq<char>)
{
    // INT then non-DOT: "{lower}"
    var a := ParseCountINT(lower, false, false, upper);
    assert a == Braced(lower);
    // INT then DOT then INT: "{lower,upper}"
    var b := ParseCountINT(lower, true, true, upper);
    assert b == ['{'] + lower + [','] + upper + ['}'];
    // INT then DOT, no INT: "{lower,}" (vendor keeps the comma)
    var c := ParseCountINT(lower, true, false, upper);
    assert c == ['{'] + lower + [','] + ['}'];
    // DOT arm with INT: "{0,upper}"; without: "*"
    var d := ParseCountOther(true, upper);
    assert d == ['{'] + ['0'] + [','] + upper + ['}'];
    var e := ParseCountOther(false, upper);
    assert e == ['*'];
}

// Coverage non-emptiness: whichever production runs, the vendor
// never returns an empty bstring - the INT arm always emits at least
// the braces, and the DOT arm emits either the braces or "*".
method CoverageNonEmpty(lower: seq<char>, nextIsDot: bool, hasUpper: bool, upper: seq<char>, otherHasInt: bool)
{
    var intArm := ParseCountINT(lower, nextIsDot, hasUpper, upper);
    assert |intArm| >= 2; // "{" prefix + "}" suffix
    assert intArm[0] == '{';
    assert intArm[|intArm| - 1] == '}';
    var otherArm := ParseCountOther(otherHasInt, upper);
    assert |otherArm| >= 1; // "*" or the braced form
}
