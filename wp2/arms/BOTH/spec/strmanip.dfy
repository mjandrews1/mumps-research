// strmanip.dfy - BOTH-arm spec of batch-5 tranche-2 unit 141
// (interp-strmanip.cpp::<misc>): the decidable cores of the three
// residual helpers. The C-array mutations of the Kotlin render are
// out of scope; the specs contract the pure scan/pad properties.
// Written after the Kotlin render.
//
//   rindex: the backward scan finds the LAST occurrence of c or
//     reports none; a found offset indexes s exactly.
//   strcatx: the append leaves the original prefix intact and
//     extends by exactly the input segment plus the sentinel. The
//     sentinel is parameterized: the vendor pins it to the byte 1 at
//     the Kotlin layer (control bytes printable in Dafny source is
//     out of scope, T1 h015 precedent).
//   lpad: the space pad lands the input right-aligned at the width
//     (k = l - j - 1 > 0); short inputs (k <= 0) are the vendor
//     no-op quirk and stay out of the contract.

ghost predicate Contains(s: seq<char>, c: char)
{
    exists i :: 0 <= i < |s| && s[i] == c
}

// rindex(s, c): last occurrence, or -1 for none. The C quirk (c==0
// matches at the terminator, i.e. |s|) is out of the String model
// and journaled in the Kotlin render instead.
method RIndexScan(s: seq<char>, c: char) returns (p: int)
    ensures -1 <= p < |s|
    ensures p >= 0 ==> (0 <= p < |s| && s[p] == c && forall q :: p < q < |s| ==> s[q] != c)
    ensures p == -1 ==> forall q :: 0 <= q < |s| ==> s[q] != c
    ensures p == -1 <==> !Contains(s, c)
{
    p := -1;
    var j := 0;
    while j < |s|
        invariant 0 <= j <= |s|
        invariant -1 <= p < j
        invariant p >= 0 ==> s[p] == c
        invariant forall q :: p < q < j ==> s[q] != c
        decreases |s| - j
    {
        if s[j] == c {
            p := j;
        }
        j := j + 1;
    }
    if p >= 0 {
        // the found offset is a real witness
        assert 0 <= p < |s| && s[p] == c;
        assert s[p] == c;
    }
}

// The found offset is a true last-occurrence witness; the -1 arm has
// no occurrence anywhere.
method RIndexWitness(s: seq<char>, c: char)
{
    var p := RIndexScan(s, c);
    assert p == -1 || (0 <= p < |s| && s[p] == c);
}

// strcatx(out, in): the append seat.
function StrcatxFold(prefix: seq<char>, input: seq<char>, sentinel: char): seq<char>
{
    prefix + input + [sentinel]
}

method StrcatxPreservesPrefix(prefix: seq<char>, input: seq<char>, sentinel: char)
{
    var r := StrcatxFold(prefix, input, sentinel);
    assert |r| == |prefix| + |input| + 1;
    assert r[|prefix| + |input|] == sentinel; // the sentinel seat
    assert forall q :: 0 <= q < |prefix| ==> r[q] == prefix[q];
}

// lpad: with k = l - j - 1 (j = |in| - 1, so k = l - |in|), the fold
// is exactly k spaces followed by the input: right-aligned at l.
function LpadPad(k: nat): seq<char>
    decreases k
{
    if k == 0 then [] else LpadPad(k - 1) + [' ']
}

lemma LpadPadAllSpaces(k: nat)
    ensures |LpadPad(k)| == k
    ensures forall w :: 0 <= w < k ==> LpadPad(k)[w] == ' '
    decreases k
{
    if k > 0 {
        LpadPadAllSpaces(k - 1);
    }
}

lemma LpadPadLength(k: nat)
    ensures |LpadPad(k)| == k
    decreases k
{
    if k > 0 {
        LpadPadLength(k - 1);
    }
}

function LpadFold(input: seq<char>, l: int): seq<char>
    requires |input| >= 1
    requires l - (|input| - 1) - 1 > 0
{
    LpadPad(l - (|input| - 1) - 1) + input
}

method LpadPositionsInput(input: seq<char>, l: int)
    requires |input| >= 1
    requires l - (|input| - 1) - 1 > 0
    ensures |LpadFold(input, l)| == l
    ensures LpadFold(input, l)[0] == ' '
    ensures forall i :: 0 <= i < |input| ==> LpadFold(input, l)[l - |input| + i] == input[i]
{
    var k := l - (|input| - 1) - 1;
    assert k == l - |input|; // the pad width reducible to the shift
    LpadPadLength(k);
    LpadPadAllSpaces(k);
    var r := LpadFold(input, l);
    assert |LpadPad(k)| == k;
}
