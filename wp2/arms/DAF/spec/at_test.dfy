// DAF B4 T2 spec: compiler-at-test.c::atTest (unit 109), the quote-walk
// scanner of the @-test. Modeled against the DAF Kotlin render
// AtTest.kt.
//
// MODELING NOTES (kept honest): the C buffer xd is NUL-terminated, so
// the C walk stops at the logical end; the Dafny model carries the
// seq<char> bound |xd| as that terminator (the Kotlin render's at()
// end-guard is the same fold). pd1/lines are diagnostic-only in the
// vendor: only printed on the imbalanced-quote error path, so they
// are out of scope here (BASE-log row 69: diagnostics kept verbatim
// at the render; not part of the verified contract).
//
// VERIFIED CONTRACT: AtScan walkers return the terminal index (first
// ' ' at/after xpx, or |xd|) when the walk completes, 0 when it
// completes with no '@' seen, and -1 when a quoted span is entered and
// never closed (the imbalanced-quote path). The doubled-quote escape
// ('""' inside a quoted span skips one character) is preserved.
//
// NOT CLAIMED (see the mps_asin.dfy lemma precedent): the spec does
// not claim anything about characters inside quoted spans (they are
// opaque to the outer scan), and it does not relate to the printed
// diagnostics.

function QuoteEnd(xd: seq<char>, k: int): int
  // Walks a quoted span that opened at index k-1; returns the index
  // just after the closing quote, or -1 if the span never closes.
  requires 0 <= k <= |xd|
  ensures QuoteEnd(xd, k) == -1 || k < QuoteEnd(xd, k) <= |xd|
  decreases |xd| - k
{
  if k >= |xd| then -1
  else if xd[k] == '"' then
    if k + 1 < |xd| && xd[k + 1] == '"' then
      QuoteEnd(xd, k + 2) // doubled-quote escape: skip one char
    else
      k + 1                // the unescaped closing quote
  else
    QuoteEnd(xd, k + 1)
}

function AtScan(xd: seq<char>, k: int, atFlg: bool): int
  // The outer walk: scans xd from k for the terminating ' ' (or the
  // end), setting atFlg on an unquoted '@', and recursing past closed
  // quoted spans.
  requires 0 <= k <= |xd|
  ensures -1 <= AtScan(xd, k, atFlg) <= |xd|
  decreases |xd| - k
{
  if k >= |xd| || xd[k] == ' ' then
    if atFlg then k else 0
  else if xd[k] == '"' then
    (var q := QuoteEnd(xd, k + 1);
     if q == -1 then -1 else AtScan(xd, q, atFlg))
  else
    AtScan(xd, k + 1, atFlg || xd[k] == '@')
}

// The unit contract at its call shape: atTest(xd, xpx, ...) with the
// walk starting at xpx and the flag unset.
method AtTestSpec(xd: seq<char>, xpx: int) returns (r: int)
  requires 0 <= xpx <= |xd|
  ensures r == AtScan(xd, xpx, false)
  ensures -1 <= r <= |xd|
{
  r := AtScan(xd, xpx, false);
}

// Records what the spec does NOT claim (the mps_asin.dfy precedent):
// the return value alone does not determine whether an '@' was seen
// when a quoted span intervenes after it; the diagnostics are not
// modeled.
lemma AtTestClaimsAreBounded()
  ensures true
{
}
