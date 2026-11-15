#!/bin/bash
# smoke_test.sh — verify the PRO-arm knowledge tooling before the arm starts
# (per wp2/arms/ARM_WORKING_NOTES.md section 2).
#
# Usage (from anywhere; the script locates the repo root itself):
#   bash wp2/arms/PRO/smoke_test.sh
#
# Two Prolog pitfalls baked into this script's design:
#   1. Paths must be QUOTED atoms — bare wp2/arms/PRO/rules_v0 parses the
#      uppercase PRO as a VARIABLE ("Arguments are not sufficiently
#      instantiated").
#   2. Rules cannot be defined at the interactive toplevel ("Rules must be
#      loaded from a file") — so the checks live in a temp .pl file that
#      swipl loads, not in a heredoc.
#
# The smoke test runs only lookups — it does NOT record any events, so
# wp2/logs/PRO_knowledge_events.csv stays clean.

set -u
HERE="$(cd "$(dirname "$0")" && pwd)"
REPO_ROOT="$(cd "$HERE/../../.." && pwd)"
cd "$REPO_ROOT"

TMP="$(mktemp /tmp/pro_smoke_XXXXXX.pl)"
trap 'rm -f "$TMP"' EXIT

cat > "$TMP" <<'EOF'
% proper quoting: uppercase path components are variables unless quoted
:- consult('wp2/arms/PRO/rules_v0').
:- consult('wp2/arms/PRO/knowledge_tooling').

% every trigger family must resolve to its rule
check_family(Family, ExpectedId) :-
    (   rule(ExpectedId, trigger(Family), _, _, _, _, _)
    ->  format('ok: ~w -> ~w~n', [Family, ExpectedId])
    ;   format('FAIL: ~w did not resolve to ~w~n', [Family, ExpectedId]),
        fail
    ).

:- maplist(check_family,
        [io_stream_param, simple_output_macro, state_macro, globals_ext,
         out_param_bundle, out_param_holder, char_buffer, static_scratch,
         format_string, numeric_promotion, goto, unsigned_byte_domain,
         dead_store, out_of_scope_call, grammar_collision, comma_operator,
         naming],
        [h001, h002, h003, h004, h005, h006, h007, h008, h009, h010,
         h011, h012, h013, h014, h015, h016, h017]).

% an unknown family must find nothing (prints "no rule for trigger ...")
:- find_rule(nonsense_family).

% coverage_report must run on an empty event log
:- coverage_report.

:- format('SMOKE TEST PASSED~n').
:- halt.
EOF

swipl "$TMP"
