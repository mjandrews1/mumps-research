#!/bin/bash
# smoke_test.sh — verify the PRO-arm knowledge tooling before the arm starts
# (per wp2/arms/ARM_WORKING_NOTES.md section 2).
#
# Usage (from anywhere; the script locates the repo root itself):
#   bash wp2/arms/PRO/smoke_test.sh
#
# NOTE: Prolog paths must be QUOTED atoms — bare wp2/arms/PRO/rules_v0 is
# parsed as a compound term with PRO as a VARIABLE (uppercase), which fails
# with "Arguments are not sufficiently instantiated". Never bare-consult
# paths containing uppercase components.
#
# The smoke test runs only find_rule/coverage_report lookups — it does NOT
# record any events, so wp2/logs/PRO_knowledge_events.csv stays clean.

set -u
HERE="$(cd "$(dirname "$0")" && pwd)"
REPO_ROOT="$(cd "$HERE/../../.." && pwd)"
cd "$REPO_ROOT"

exec swipl <<'EOF'
% proper quoting: uppercase path components are variables unless quoted
consult('wp2/arms/PRO/rules_v0').
consult('wp2/arms/PRO/knowledge_tooling').

% 1. every trigger family must resolve to its rule
check_family(Family, ExpectedId) :-
    (   rule(ExpectedId, trigger(Family), _, _, _, _, _)
    ->  format('ok: ~w -> ~w~n', [Family, ExpectedId])
    ;   format('FAIL: ~w did not resolve to ~w~n', [Family, ExpectedId])
    ).

maplist(check_family,
        [io_stream_param, simple_output_macro, state_macro, globals_ext,
         out_param_bundle, out_param_holder, char_buffer, static_scratch,
         format_string, numeric_promotion, goto, unsigned_byte_domain,
         dead_store, out_of_scope_call, grammar_collision, comma_operator,
         naming],
        [h001, h002, h003, h004, h005, h006, h007, h008, h009, h010,
         h011, h012, h013, h014, h015, h016, h017]).

% 2. an unknown family must find nothing
find_rule(nonsense_family).

% 3. coverage_report must run on an empty event log
coverage_report.

halt.
EOF
