% rules_v0.pl — Heuristic rule base v0 (arm PRO)
% Seeded 2026-11-15 from wp2/logs/BASE_heuristic_log.csv under the
% pre-registered capture criteria (wp1/capture_criteria.md): every rule
% satisfies T1 (recurrence >=2); h010/h015/h016 also via T2/T3
% (red builds / external reference). Mirrors DOC cookbook v0 entry-for-
% entry (H-### <-> hNNN): identical triggers, conditions, actions,
% examples, provenance, verification. Gates G1-G4 checked at seeding.
%
% rule(Id, trigger(Family), condition(Cond), action(Action),
%      example(Unit), provenance(Arm,Batch,Date), verification(Status)).

rule(h001, trigger(io_stream_param),
     condition(all_signatures_except_program_edges),
     action('FILE* -> java.io.Writer everywhere; PrintWriter only at edges; Writer surfaces IOException like C'),
     example('compiler-copyright-notice.c'),
     provenance(base, b1, '2026-11-14'), verification(unit_tested)).

rule(h002, trigger(simple_output_macro),
     condition(no_state_beyond_arguments),
     action('macro -> local helper fun preserving call-site shape'),
     example('compiler-copyright-notice.c'),
     provenance(base, b1, '2026-11-14'), verification(unit_tested)).

rule(h003, trigger(state_macro),
     condition(unit_global_set_exists),
     action('macro -> member fun of the state singleton object'),
     example('compiler-if.c'),
     provenance(base, b1, '2026-11-14'), verification(unit_tested)).

rule(h004, trigger(globals_ext),
     condition(one_owner_object_per_c_tu_global_set),
     action('file-scope globals -> singleton object properties; register each with source header line'),
     example('compiler-globals-ext.h'),
     provenance(base, b1, '2026-11-14'), verification(unit_tested)).

rule(h005, trigger(out_param_bundle),
     condition(return_slot_free_and_values_coherent),
     action('pointer out-param -> small data class returned'),
     example('compiler-if.c'),
     provenance(base, b1, '2026-11-14'), verification(unit_tested)).

rule(h006, trigger(out_param_holder),
     condition(callers_reuse_same_variable_or_api_shape_must_survive),
     action('out-param -> typed single-element holder array; read/write [0] at call sites'),
     example('compiler-localarray.c'),
     provenance(base, 'b1-b2', '2026-11-15'), verification(unit_tested)).

rule(h007, trigger(char_buffer),
     condition(classify_by_role),
     action('scan-only -> String; mutable scratch -> StringBuilder; pointer-into-buffer -> offset param'),
     example('compiler-if.c'),
     provenance(base, b1, '2026-11-14'), verification(unit_tested)).

rule(h008, trigger(static_scratch),
     condition(no_caller_aliases_buffer_across_calls_otherwise_document_divergence),
     action('static shared scratch -> fresh String per call; NULL-in returns ""; fast path returns original object'),
     example('interp-sql.cpp'),
     provenance(base, b2, '2026-11-15'), verification(unit_tested)).

rule(h009, trigger(format_string),
     condition(byte_equivalence_required),
     action('exact Kotlin formatter with Locale.US; collapse idioms first (%2x of byte = 2-char hex); keep trailing-newline behavior verbatim'),
     example('compiler-copyright-notice.c'),
     provenance(base, 'b1-b2', '2026-11-14'), verification(unit_tested)).

rule(h010, trigger(numeric_promotion),
     condition(document_every_widening),
     action('explicit widening Short->Int etc.; never rely on mixed-type operator overloads (UShort+Short has none)'),
     example('interp-sym.cpp'),
     provenance(base, 'b1-b2', '2026-11-15'), verification(unit_tested)).

rule(h011, trigger(goto),
     condition(preserve_iteration_and_fallthrough_order),
     action('backward goto -> while(true)+flag; forward goto -> flag fallthrough'),
     example('compiler-if.c'),
     provenance(base, b1, '2026-11-14'), verification(unit_tested)).

rule(h012, trigger(unsigned_byte_domain),
     condition(c_compares_unsigned_char),
     action('bytes -> Char(0..255); range comparison exact for byte domain'),
     example('interp-native-btree.cpp'),
     provenance(base, 'b1-b2', '2026-11-15'), verification(unit_tested)).

rule(h013, trigger(dead_store),
     condition(provably_unobservable),
     action('drop with comment; never fix by initializing'),
     example('interp-native-btree.cpp'),
     provenance(base, 'b1-b2', '2026-11-15'), verification(unit_tested)).

rule(h014, trigger(out_of_scope_call),
     condition(owner_unit_named_in_units_csv_not_yet_translated),
     action('TODO stub in arm Stubs.kt naming owner unit; delegate when owner lands'),
     example('compiler-globalarray.c'),
     provenance(base, 'b1-b2', '2026-11-15'), verification(untested)).

rule(h015, trigger(grammar_collision),
     condition(applies_to_any_quoted_c_source_before_every_push),
     action('*/ in comments -> reword+reference constant; $z -> escape; control/high bytes -> Char(nnn); scan for-loop shadowing'),
     example('interp-parse.cpp'),
     provenance(base, b1, '2026-11-15'), verification(unit_tested)).

rule(h016, trigger(comma_operator),
     condition(statement_level),
     action('split into sequential statements'),
     example('interp-sym.cpp'),
     provenance(base, b1, '2026-11-15'), verification(unit_tested)).

rule(h017, trigger(naming),
     condition(always_document_per_arm_mapping),
     action('mechanical snake->camel rename; journal keyword collisions (in, is, object)'),
     example('compiler-copyright-notice.c'),
     provenance(base, b1, '2026-11-14'), verification(unit_tested)).
