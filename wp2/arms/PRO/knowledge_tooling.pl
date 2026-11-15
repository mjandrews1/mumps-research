% knowledge_tooling.pl — PRO-arm knowledge instrumentation (WP2 step 4)
%
% Usage (SWI-Prolog 9.x, from the repo root):
%   ?- consult('wp2/arms/PRO/rules_v0').
%   ?- consult('wp2/arms/PRO/knowledge_tooling').
%   ?- find_rule(out_param_bundle).        % lookup before/during a unit
%   ?- record_hit(h005, 'compiler-if.c').  % consulted
%   ?- record_reuse(h005, 'compiler-if.c').% action applied unmodified
%   ?- record_miss(recursion_guard, 'x.c').% triggered, no rule existed
%   ?- coverage_report.                    % per-batch metrics
%
% Semantics per wp1/capture_criteria.md section 4:
%   hit     = rule consulted before/during editing a unit
%   reuse   = action applied without modification (also counts as hit:
%             record_reuse/2 asserts a hit event too)
%   adapt   = log as record_hit/2 + a journal adaptation note (not reuse)
%   miss    = triggering situation occurred and no entry existed
% Metrics per section 5:
%   capture rate = entries created / triggering situations
%   hit rate     = hits / triggering situations
%   reuse rate   = reuses / hits
% Events persist to wp2/logs/PRO_knowledge_events.csv (one row per call,
% timestamped). Events live in the CSV and are reloaded on consult, so
% re-consulting does not lose them.

:- use_module(library(readutil)).

:- dynamic event/4.            % event(Kind, Target, Unit, Stamp)
:- dynamic event_log_loaded/0.

default_log_path('wp2/logs/PRO_knowledge_events.csv').

load_events :-
    event_log_loaded, !.
load_events :-
    default_log_path(File),
    (   exists_file(File)
    ->  setup_call_cleanup(
            open(File, read, S),
            (   read_line_to_string(S, _Header),
                repeat,
                    read_line_to_string(S, Line),
                    (   Line == end_of_file -> !
                    ;   split_string(Line, ",", "", [Ks, Ts, Us, TS]),
                        % 2026-11-15 fix: split_string yields strings, but
                        % coverage_report queries event/4 with atoms
                        % (record_* assert atoms); convert kind/target/
                        % unit on load. The stamp stays a string, matching
                        % the format_time(string(...)) stamp of the
                        % runtime record_* path.
                        atom_string(K, Ks), atom_string(T, Ts),
                        atom_string(U, Us),
                        assertz(event(K, T, U, TS)),
                        fail
                    )
            ),
            close(S))
    ;   true
    ),
    assertz(event_log_loaded).

:- load_events.

append_event_row(Kind, Target, Unit) :-
    get_time(Now),
    format_time(string(Stamp), '%Y-%m-%dT%H:%M:%S%z', Now),
    assertz(event(Kind, Target, Unit, Stamp)),
    default_log_path(File),
    (   exists_file(File) -> true
    ;   setup_call_cleanup(
            open(File, write, S),
            writeln(S, 'kind,target,unit,timestamp'),
            close(S))
    ),
    setup_call_cleanup(
        open(File, append, S),
        format(S, '~w,~w,~w,~w~n', [Kind, Target, Unit, Stamp]),
        close(S)).

%% find_rule(+ConstructFamily) — list all rules whose trigger matches.
find_rule(Family) :-
    findall(Id, rule(Id, trigger(Family), _, _, _, _, _), Ids),
    (   Ids == []
    ->  format('no rule for trigger ~w~n', [Family])
    ;   forall(
            rule(Id, trigger(Family), condition(C), action(A),
                 example(E), _, V),
            format('rule ~w  trigger ~w~n  condition: ~w~n  action: ~w~n  example: ~w  [~w]~n~n',
                   [Id, Family, C, A, E, V]))
    ).

%% record_hit(+RuleId, +Unit) — rule consulted during a unit.
record_hit(Id, Unit) :-
    (   rule(Id, _, _, _, _, _, _)
    ->  true
    ;   format('warning: ~w is not a known rule id~n', [Id])
    ),
    append_event_row(hit, Id, Unit).

%% record_reuse(+RuleId, +Unit) — applied unmodified (counts as hit too).
record_reuse(Id, Unit) :-
    append_event_row(reuse, Id, Unit),
    append_event_row(hit, Id, Unit).

%% record_miss(+ConstructFamily, +Unit) — triggering situation, no entry.
record_miss(Family, Unit) :-
    append_event_row(miss, Family, Unit).

%% coverage_report — rates per capture_criteria.md section 5.
coverage_report :-
    findall(_, event(hit, _, _, _), H),   length(H, Hits),
    findall(_, event(reuse, _, _, _), R), length(R, Reuses),
    findall(_, event(miss, _, _, _), M),  length(M, Misses),
    Trig is Hits + Misses,                 % logged triggering situations
    (   Trig > 0 -> HitRate is 100 * Hits / Trig ; HitRate = 0.0 ),
    (   Hits > 0 -> ReuseRate is 100 * Reuses / Hits ; ReuseRate = 0.0 ),
    aggregate_all(count, rule(_, _, _, _, _, _, _), Rules),
    format('rules in base:          ~w~n', [Rules]),
    format('triggering situations:  ~w (hits ~w, misses ~w)~n', [Trig, Hits, Misses]),
    format('hit rate:               ~1f%~n', [HitRate]),
    format('reuse rate:             ~1f%~n', [ReuseRate]),
    (   Misses > 0
    ->  format('capture candidates (missed triggers):~n', []),
        forall(event(miss, F, _, _),
               format('  - ~w~n', [F]))
    ;   true
    ).
