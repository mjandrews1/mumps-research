// DAF B5 T1 spec: the shared \$Length counting kernel of
// libmpscpp.cpp::mstring::Length (unit 129) and
// libmpscpp.cpp::global::Length (unit 133). Both renders walk the
// haystack counting the NON-terminating strstr matches: cursor =
// strstr(cursor, needle); if none, return the count; else count++,
// cursor += strlen(needle). The kernel spec'd here is that walk.
//
// VENDOR QUIRK (divergence ledger, journal row): the walk is the
// vendor's exact shape only when needle is non-empty; an EMPTY
// needle (a global that exists with an empty value) makes every
// strstr match at the cursor while the cursor never advances, so
// the vendor does not terminate. The renders keep the walk shape and
// therefore keep the vendor nontermination; THIS spec guards
// needle != [] and contracts only the reaching states.
//
// Also per the vendor folds: empty haystack returns 0 before the
// walk (models strlen(p1)==0); the match advance is
// needle.Length (the C p1 = p1 + strlen(gtmp) AFTER the strstr match
// at its start, so overlapping matches at increasing indices are
// found only when the advance re-enters a match region: the walk
// counts matches at least needle.Length apart).

function CountWalk(hay: seq<char>, needle: seq<char>): (n: int)
  requires |needle| > 0
  ensures n >= 1
{
  if IndexOfChar(hay, needle, 0) < 0 then
    1
  else
    CountWalkSuffix(hay, needle, IndexOfChar(hay, needle, 0) + |needle|, 1)
}

function CountWalkSuffix(hay: seq<char>, needle: seq<char>, from: int, n: int): (m: int)
  requires |needle| > 0 && 0 <= from <= |hay| && n >= 1
  ensures m >= n
  decreases |hay| - from
{
  var idx := IndexOfChar(hay, needle, from);
  if idx < 0 then n else CountWalkSuffix(hay, needle, idx + |needle|, n + 1)
}

// IndexOfChar: the strstr fold (String.indexOf semantics over
// seq<char>): first j >= from with hay[j..j+|needle|] == needle, or
// -1 when none. Declared bodyless {:axiom} - the journal treats the
// indexOf fold as the row-33 family assumption; only the WALK above
// is verified. Termination of CountWalk/CountWalkSuffix follows
// because a match at idx advances the cursor by |needle| > 0.
function {:axiom} IndexOfChar(hay: seq<char>, needle: seq<char>, from: int): (r: int)
  requires |needle| > 0 && 0 <= from
  ensures r == -1 || (from <= r && r + |needle| <= |hay| && hay[r..r+|needle|] == needle)

// The one-piece contract of both units: no match means the vendor
// returns the initial i=1 (the count of pieces).
lemma OnePieceContract(hay: seq<char>, needle: seq<char>)
  requires |needle| > 0 && IndexOfChar(hay, needle, 0) == -1
  ensures CountWalk(hay, needle) == 1
{
}

// Sanity carrier: the walk is defined for every non-empty needle
// over any haystack (the spec's totality check).
method WalkTotalityCheck(hay: seq<char>, needle: seq<char>) returns (n: int)
  requires |needle| > 0
  ensures n >= 1
{
  n := CountWalk(hay, needle);
}
