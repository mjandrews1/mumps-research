// DAF B5 T3 spec: the Qlength (unit 145) counting kernel of
// interp-sym.cpp::Qlength: the walk over the v1d register counting
// the subscript markers '\\u0001'-equivalent CODE 1 (the parseDaf
// keyfixD mark byte) and every code above 128 (the vendor > 0x80
// unsigned-char test), stopping at the first NUL, with the
// j == 0 -> "0", else (j - 1) shaping (one marker over-count: the
// parse seats leave exactly one trailing/raw mark, so the length
// count decrements once on non-empty results). Modeled against
// the DAF Kotlin render Qlength.kt.
//
// MODELING NOTE (logged divergence, journal row): the walk is the
// vendor's exact shape only while the register carries a NUL
// terminator within its bounds; the render walks at()-guarded
// reads (row 81 NUL-guard), so termination here is contracted by
// requires 0 in a[..]. The domain is BYTE CODES (seq<nat> capped
// at 255): the vendor's unsigned-char carrier semantics modeled
// exactly (char-code literals are not available in this Dafny
// build's grammar); the render's isMark fold is the same
// c == 1 || c > 128 predicate over the code domain.

const NUL: nat := 0
const MARK1: nat := 1

predicate isMark(c: nat)
{
  c == MARK1 || c > 128
}

ghost predicate byteSeq(s: seq<nat>)
{
  forall i :: 0 <= i < |s| ==> s[i] <= 255
}

// Count of marked codes strictly before the first NUL.
// Requires the terminator (vendor: register is NUL-terminated).
function CountMarked(s: seq<nat>): int
  requires NUL in s
  decreases |s|
{
  if s[0] == NUL then
    0
  else
    (if isMark(s[0]) then 1 else 0) + CountMarked(s[1..])
}

// The terminator survives any suffix split at a non-NUL position.
lemma NULSuffix(s: seq<nat>)
  requires NUL in s
{
  var k :| 0 <= k < |s| && s[k] == NUL;
  if |s| > 0 && s[0] != NUL {
    assert NUL in s[1..];
  }
}

// The loop kernel: i walks the register, j counts the marks.
// Invariants pin the suffix-consumption ledger
// j + CountMarked(a[i..]) == CountMarked(a[..]) and the
// terminator's survival in the unscanned suffix.
method QlengthWalk(a: array<nat>) returns (j: int)
  requires 0 < a.Length
  requires byteSeq(a[..])
  requires NUL in a[..]
  ensures j >= 0
  ensures j == CountMarked(a[..])
{
  var i := 0;
  j := 0;
  NULSuffix(a[..]);
  while a[i] != NUL
    invariant 0 <= i < a.Length
    invariant NUL in a[i..]
    invariant j + CountMarked(a[i..]) == CountMarked(a[..])
    decreases a.Length - i
  {
    if isMark(a[i]) {
      j := j + 1;
    }
    i := i + 1;
    NULSuffix(a[i..]);
  }
}
